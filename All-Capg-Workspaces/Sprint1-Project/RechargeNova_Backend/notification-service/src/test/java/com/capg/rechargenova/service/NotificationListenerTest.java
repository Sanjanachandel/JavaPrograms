package com.capg.rechargenova.service;

import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Notification;
import com.capg.rechargenova.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private RechargeFeignClient rechargeFeignClient;

    @Mock
    private OperatorFeignClient operatorFeignClient;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    void testConsumePaymentEvent_Success() {
        PaymentEvent event = new PaymentEvent(1L, 1L, 1L, "SUCCESS", java.math.BigDecimal.TEN, "Prepaid");
        
        when(userFeignClient.getUserById(1L)).thenReturn(new UserResponse(1L, "name", "email", "123"));
        when(rechargeFeignClient.getRechargeById(1L)).thenReturn(new RechargeResponse(1L, 1L, 1L, 1L, "123", BigDecimal.TEN, "S", "Prepaid"));
        when(operatorFeignClient.getOperatorById(1L)).thenReturn(new OperatorDto(1L, "op", "circle"));
        when(operatorFeignClient.getPlanById(1L)).thenReturn(new PlanDto(1L, 1L, 10.0, "30", "d", "Prepaid"));

        notificationListener.consumePaymentEvent(event);

        verify(notificationRepository, times(1)).save(any(Notification.class));
        try {
            verify(emailService, times(1)).sendRechargeSuccessEmail(any(EmailRequest.class));
        } catch (Exception e) {}
    }

    @Test
    void testConsumePaymentEvent_FailedStatus() {
        PaymentEvent event = new PaymentEvent(1L, 1L, 1L, "FAILED", java.math.BigDecimal.TEN, "Prepaid");

        notificationListener.consumePaymentEvent(event);

        verify(notificationRepository, times(1)).save(any(Notification.class));
        try {
            verify(emailService, never()).sendRechargeSuccessEmail(any());
        } catch (Exception e) {}
    }

    @Test
    void testConsumePaymentEvent_Exception() {
        PaymentEvent event = new PaymentEvent(1L, 1L, 1L, "SUCCESS", java.math.BigDecimal.TEN, "Prepaid");
        when(userFeignClient.getUserById(1L)).thenThrow(new RuntimeException("API Error"));

        notificationListener.consumePaymentEvent(event);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}