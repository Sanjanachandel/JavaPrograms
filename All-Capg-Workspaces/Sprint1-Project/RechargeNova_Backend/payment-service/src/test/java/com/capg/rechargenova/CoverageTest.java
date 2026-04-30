package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Transaction;
import com.capg.rechargenova.exception.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testDTOs() {
        // PaymentEvent
        PaymentEvent pe = new PaymentEvent();
        pe.setTransactionId(1L); pe.setRechargeId(1L); pe.setUserId(1L); pe.setStatus("SUCCESS");
        assertEquals(1L, pe.getTransactionId());
        assertEquals(1L, pe.getRechargeId());
        assertEquals(1L, pe.getUserId());
        assertEquals("SUCCESS", pe.getStatus());

        PaymentEvent pe2 = new PaymentEvent(1L, 1L, 1L, "SUCCESS", BigDecimal.TEN, "Prepaid");
        assertEquals(1L, pe2.getUserId());

        // PaymentRequest
        PaymentRequest pr = new PaymentRequest();
        pr.setRechargeId(1L); pr.setUserId(1L); pr.setAmount(BigDecimal.TEN); pr.setPaymentMethod("UPI");
        assertEquals(1L, pr.getRechargeId());
        
        PaymentRequest pr2 = new PaymentRequest(1L, 1L, BigDecimal.TEN, "UPI", "Prepaid");
        assertEquals("UPI", pr2.getPaymentMethod());

        // PaymentResponse
        PaymentResponse pres = new PaymentResponse();
        pres.setId(1L); pres.setRechargeId(1L); pres.setUserId(1L); pres.setStatus("SUCCESS");
        assertEquals(1L, pres.getId());

        PaymentResponse pres2 = new PaymentResponse(1L, 1L, 1L, BigDecimal.TEN, "UPI", "SUCCESS", LocalDateTime.now());
        assertEquals("SUCCESS", pres2.getStatus());
    }

    @Test
    void testEntity() {
        Transaction t = new Transaction();
        t.setId(1L); t.setRechargeId(1L); t.setUserId(1L);
        t.setAmount(BigDecimal.TEN); t.setPaymentMethod("UPI"); t.setStatus("SUCCESS");
        t.setTransactionTime(LocalDateTime.now());
        assertEquals(1L, t.getId());
        
        Transaction t2 = new Transaction(1L, 1L, 1L, BigDecimal.TEN, "UPI", "SUCCESS", LocalDateTime.now());
        assertEquals(1L, t2.getId());
        assertEquals(1L, t2.getRechargeId());
    }

    @Test
    void testResourceNotFoundException() {
        assertNotNull(new ResourceNotFoundException("msg"));
    }

    @Test
    void testMain() {
        PaymentServiceApplication app = new PaymentServiceApplication();
        assertNotNull(app);
        PaymentServiceApplication.main(new String[]{"--server.port=0"});
    }
}
