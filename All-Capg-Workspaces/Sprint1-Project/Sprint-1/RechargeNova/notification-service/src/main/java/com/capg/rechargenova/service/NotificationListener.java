package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.rechargenova.config.RabbitMQConfig;
import com.capg.rechargenova.dto.PaymentEvent;
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

    /**
     * ================================================================
     * METHOD: consumePaymentEvent
     * DESCRIPTION:
     * Consumes payment events from RabbitMQ queue and creates notifications.
     * It builds a message based on payment status and saves the notification.
     * ================================================================
     *
     * @param event the payment event received from the queue; must not be null
     * @throws RuntimeException if notification persistence fails
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumePaymentEvent(PaymentEvent event) {

        logger.info("Received payment event → RechargeId: {}, Status: {}",
                event.getRechargeId(), event.getStatus());

        /**
         * Builds a user-friendly notification message based on payment status.
         */
        String message = "Your payment for recharge ID " + event.getRechargeId()
                + " has " + event.getStatus() + ".";

        /**
         * Creates and populates the Notification entity.
         */
        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setMessage(message);
        notification.setType("SMS");
        notification.setStatus("SENT");
        notification.setCreatedAt(LocalDateTime.now());

        /**
         * Persists the notification into the database.
         */
        notificationRepository.save(notification);

        logger.debug("Notification saved for userId: {}", event.getUserId());
    }
}