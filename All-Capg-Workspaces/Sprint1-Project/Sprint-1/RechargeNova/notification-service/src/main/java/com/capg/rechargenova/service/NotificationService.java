package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.rechargenova.dto.NotificationResponse;
import com.capg.rechargenova.entity.Notification;
import com.capg.rechargenova.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger logger = LogManager.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId the user ID; must not be null
     * @return list of NotificationResponse objects
     */
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        logger.info("Fetching notifications for userId: {}", userId);

        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a notification by its ID.
     *
     * @param id the notification ID; must not be null
     * @return NotificationResponse object
     * @throws RuntimeException if notification is not found
     */
    public NotificationResponse getNotificationById(Long id) {
        logger.info("Fetching notification with ID: {}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notification not found: {}", id);
                    return new RuntimeException("Notification not found");
                });

        return mapToResponse(notification);
    }

    /**
     * Retrieves all notifications from the database.
     *
     * @return list of NotificationResponse objects
     */
    public List<NotificationResponse> getAllNotifications() {
        logger.info("Fetching all notifications");

        return notificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Notification entity into a NotificationResponse DTO.
     *
     * @param notification the Notification entity; must not be null
     * @return mapped NotificationResponse object
     */
    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setStatus(notification.getStatus());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}