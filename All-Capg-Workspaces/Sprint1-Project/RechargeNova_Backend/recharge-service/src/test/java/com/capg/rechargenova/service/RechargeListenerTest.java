package com.capg.rechargenova.service;

import com.capg.rechargenova.dto.PaymentEvent;
import com.capg.rechargenova.entity.Recharge;
import com.capg.rechargenova.repository.RechargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class RechargeListenerTest {

    @Mock
    private RechargeRepository rechargeRepository;

    @InjectMocks
    private RechargeListener rechargeListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumePaymentEvent_Success() {
        PaymentEvent event = new PaymentEvent();
        event.setRechargeId(1L);
        event.setStatus("SUCCESS");

        Recharge recharge = new Recharge();
        recharge.setId(1L);
        recharge.setStatus("FAILED");

        when(rechargeRepository.findById(1L)).thenReturn(Optional.of(recharge));

        rechargeListener.consumePaymentEvent(event);

        verify(rechargeRepository, times(1)).findById(1L);
        verify(rechargeRepository, times(1)).save(recharge);
        assert recharge.getStatus().equals("SUCCESS");
    }

    @Test
    void testConsumePaymentEvent_NotSuccess() {
        PaymentEvent event = new PaymentEvent();
        event.setRechargeId(1L);
        event.setStatus("FAILED");

        rechargeListener.consumePaymentEvent(event);

        verify(rechargeRepository, never()).findById(anyLong());
    }

    @Test
    void testConsumePaymentEvent_NullId() {
        PaymentEvent event = new PaymentEvent();
        event.setRechargeId(null);
        event.setStatus("SUCCESS");

        rechargeListener.consumePaymentEvent(event);

        verify(rechargeRepository, never()).findById(anyLong());
    }
}
