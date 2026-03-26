package com.example.notification_service.service;

import com.example.notification_service.dto.PaymentEvent;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.NotificationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class NotificationListenerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationListener notificationListener;

    // ✅ IMPORTANT FIX (instead of MockitoExtension)
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumePaymentEvent() {

        PaymentEvent event = new PaymentEvent(
                1L,
                200L,
                101L,
                "SUCCESS"
        );

        notificationListener.consumePaymentEvent(event);

        // ✅ Verify that save() is called
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}