package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.rechargenova.config.RabbitMQConfig;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Notification;
import com.capg.rechargenova.repository.NotificationRepository;

import java.time.LocalDateTime;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: NotificationListener
 * DESCRIPTION:
 * Listens to payment events from RabbitMQ queue and generates
 * notifications. It processes PaymentEvent data, builds a
 * notification message, and persists it to the database.
 * Logging is done using Log4j2 for debugging and info tracking.
 * ================================================================
 */
@Service
public class NotificationListener {

    private static final Logger logger = LogManager.getLogger(NotificationListener.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private RechargeFeignClient rechargeFeignClient;

    @Autowired
    private OperatorFeignClient operatorFeignClient;

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumePaymentEvent(PaymentEvent event) {

        logger.info("Received payment event → RechargeId: {}, Status: {}",
                event.getRechargeId(), event.getStatus());

        // 1. Save local notification as before
        String message = "Your payment for recharge ID " + event.getRechargeId()
                + " has " + event.getStatus() + ".";

        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setMessage(message);
        notification.setType("SMS");
        notification.setStatus("SENT");
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        // 2. Automatically send Interactive Email if Success
        if ("SUCCESS".equalsIgnoreCase(event.getStatus())) {
            try {
                logger.info("Triggering automatic success email for userId: {}", event.getUserId());
                
                // Fetch User Details (to get Email)
                UserResponse user = userFeignClient.getUserById(event.getUserId());
                
                // Fetch Recharge Details
                RechargeResponse recharge = rechargeFeignClient.getRechargeById(event.getRechargeId());
                
                // Fetch Operator/Plan Details
                OperatorDto operator = operatorFeignClient.getOperatorById(recharge.getOperatorId());
                PlanDto plan = operatorFeignClient.getPlanById(recharge.getPlanId());

                // Build Email Request
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setRecipientEmail(user.getEmail());
                emailRequest.setUserName(user.getName());
                emailRequest.setMobileNumber(recharge.getMobileNumber());
                emailRequest.setOperatorName(operator.getName());
                emailRequest.setAmount(recharge.getAmount().doubleValue());
                emailRequest.setValidity(plan.getValidity());
                emailRequest.setTransactionId(String.valueOf(event.getTransactionId()));
                emailRequest.setRechargeType(recharge.getRechargeType());

                // Send the interactive email
                emailService.sendRechargeSuccessEmail(emailRequest);
                logger.info("Automatic email sent successfully to: {}", user.getEmail());

            } catch (Exception e) {
                logger.error("Failed to send automatic email: {}", e.getMessage());
            }
        }
    }
}