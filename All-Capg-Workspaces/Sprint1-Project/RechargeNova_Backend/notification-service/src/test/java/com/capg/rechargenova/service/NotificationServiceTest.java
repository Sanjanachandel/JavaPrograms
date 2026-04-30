package com.capg.rechargenova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capg.rechargenova.dto.NotificationResponse;
import com.capg.rechargenova.entity.Notification;
import com.capg.rechargenova.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: NotificationServiceTest
 * DESCRIPTION:
 * Unit tests for NotificationService. Tests include retrieving all
 * notifications, fetching by ID, and fetching by user ID. Mockito
 * is used for mocking repository dependencies.
 * ================================================================
 */
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setup() {
        notification = new Notification(
                1L,
                101L,
                "Test Message",
                "SMS",
                "SENT",
                LocalDateTime.now()
        );
    }

    // ✅ Test 1: Get All Notifications
    @Test
    void testGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        List<NotificationResponse> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
    }

    // ✅ Test 2: Get Notification By ID (SUCCESS)
    @Test
    void testGetNotificationById_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        NotificationResponse response = notificationService.getNotificationById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    // ❌ Test 3: Get Notification By ID (FAILURE)
    @Test
    void testGetNotificationById_NotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            notificationService.getNotificationById(1L);
        });
    }

    // ✅ Test 4: Get Notifications By User ID
    @Test
    void testGetNotificationsByUserId() {
        when(notificationRepository.findByUserId(101L)).thenReturn(List.of(notification));

        List<NotificationResponse> result = notificationService.getNotificationsByUserId(101L);

        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).getUserId());
    }
}