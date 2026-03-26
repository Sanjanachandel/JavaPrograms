package com.capg.rechargenova.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capg.rechargenova.dto.NotificationResponse;
import com.capg.rechargenova.service.NotificationService;

import java.util.List;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: NotificationController
 * DESCRIPTION:
 * This controller handles all REST API endpoints related to 
 * notifications. It provides operations to fetch all notifications,
 * retrieve notification by ID, and get notifications for a 
 * specific user. It uses NotificationService for business logic
 * and Log4j2 for logging API activities.
 * ================================================================
 */

@Tag(name = "Notification APIs", description = "Manage notifications")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LogManager.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * ================================================================
     * METHOD: getAllNotifications
     * DESCRIPTION:
     * Retrieves all notifications available in the system.
     * ================================================================
     */
    @Operation(summary = "Get all notifications")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        logger.info("API Call: Get all notifications");
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    /**
     * ================================================================
     * METHOD: getNotificationById
     * DESCRIPTION:
     * Retrieves a notification based on its unique ID.
     * ================================================================
     */
    @Operation(summary = "Get notification by ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        logger.info("API Call: Get notification {}", id);
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    /**
     * ================================================================
     * METHOD: getNotificationsByUserId
     * DESCRIPTION:
     * Retrieves all notifications associated with a specific user.
     * ================================================================
     */
    @Operation(summary = "Get notifications by User ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable Long userId) {
        logger.info("API Call: Get notifications for user {}", userId);
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }
}