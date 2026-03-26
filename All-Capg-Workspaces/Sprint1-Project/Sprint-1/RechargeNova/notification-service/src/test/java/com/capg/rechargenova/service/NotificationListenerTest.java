package com.capg.rechargenova.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capg.rechargenova.dto.PaymentEvent;
import com.capg.rechargenova.repository.NotificationRepository;
import com.capg.rechargenova.service.NotificationListener;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Required for JUnit 5 + Mockito
public class NotificationListenerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    void testConsumePaymentEvent() {
        PaymentEvent event = new PaymentEvent(1L, 200L, 101L, "SUCCESS");
        notificationListener.consumePaymentEvent(event);

        verify(notificationRepository, times(1)).save(any());
    }
}