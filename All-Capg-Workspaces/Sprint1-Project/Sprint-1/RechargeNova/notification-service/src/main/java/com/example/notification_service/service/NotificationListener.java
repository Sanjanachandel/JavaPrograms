package com.example.notification_service.service;

import com.example.notification_service.config.RabbitMQConfig;
import com.example.notification_service.dto.PaymentEvent;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationListener {

    @Autowired
    private NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumePaymentEvent(PaymentEvent event) {
        System.out.println("Received payment event for rechargeId: " + event.getRechargeId() + " Status: " + event.getStatus());
        
        String message = "Your payment for recharge ID " + event.getRechargeId() + " has " + event.getStatus() + ".";
        
        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .message(message)
                .type("SMS")
                .status("SENT")
                .createdAt(LocalDateTime.now())
                .build();
                
        notificationRepository.save(notification);
    }
}
