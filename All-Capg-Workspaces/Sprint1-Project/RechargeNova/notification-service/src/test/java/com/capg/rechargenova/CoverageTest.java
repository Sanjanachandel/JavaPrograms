package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Notification;
import com.capg.rechargenova.exception.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testDTOs() {
        // EmailRequest
        EmailRequest er = new EmailRequest();
        er.setRecipientEmail("to"); er.setUserName("name"); er.setMobileNumber("123");
        er.setOperatorName("op"); er.setAmount(10.0); er.setValidity("30"); er.setTransactionId("tx");
        assertEquals("to", er.getRecipientEmail());

        EmailRequest er2 = new EmailRequest("to", "name", "123", "op", 10.0, "30", "tx");
        assertEquals("name", er2.getUserName());

        // NotificationResponse
        NotificationResponse nr = new NotificationResponse();
        nr.setId(1L); nr.setUserId(1L); nr.setMessage("msg"); nr.setType("type"); nr.setStatus("status"); nr.setCreatedAt(LocalDateTime.now());
        assertEquals(1L, nr.getId());
        assertEquals(1L, nr.getUserId());
        assertEquals("msg", nr.getMessage());
        assertEquals("type", nr.getType());
        assertEquals("status", nr.getStatus());
        assertNotNull(nr.getCreatedAt());

        NotificationResponse nr2 = new NotificationResponse(1L, 1L, "msg", "type", "status", LocalDateTime.now());
        assertEquals(1L, nr2.getUserId());

        // OperatorDto
        OperatorDto op = new OperatorDto();
        op.setId(1L); op.setName("name"); op.setType("Mobile"); op.setCircle("Circle");
        assertEquals(1L, op.getId());
        assertEquals("name", op.getName());
        assertEquals("Mobile", op.getType());
        assertEquals("Circle", op.getCircle());

        OperatorDto op2 = new OperatorDto(1L, "name", "type", "circle");
        assertEquals("name", op2.getName());

        // PaymentEvent
        PaymentEvent pe = new PaymentEvent();
        pe.setTransactionId(1L); pe.setRechargeId(1L); pe.setUserId(1L); pe.setStatus("SUCCESS");
        assertEquals(1L, pe.getTransactionId());
        assertEquals(1L, pe.getRechargeId());
        assertEquals(1L, pe.getUserId());
        assertEquals("SUCCESS", pe.getStatus());

        PaymentEvent pe2 = new PaymentEvent(1L, 1L, 1L, "SUCCESS");
        assertEquals(1L, pe2.getUserId());

        // PlanDto
        PlanDto p = new PlanDto();
        p.setId(1L); p.setOperatorId(2L); p.setAmount(10.0); p.setValidity("30"); p.setDescription("desc");
        assertEquals(1L, p.getId());
        assertEquals(2L, p.getOperatorId());
        assertEquals(10.0, p.getAmount());
        assertEquals("30", p.getValidity());
        assertEquals("desc", p.getDescription());

        PlanDto p2 = new PlanDto(1L, 2L, 10.0, "30", "desc");
        assertEquals(1L, p2.getId());

        // RechargeResponse
        RechargeResponse rr = new RechargeResponse();
        rr.setId(1L); rr.setUserId(1L); rr.setOperatorId(2L); rr.setPlanId(3L); rr.setMobileNumber("123"); 
        rr.setAmount(BigDecimal.TEN); rr.setStatus("S");
        assertEquals(1L, rr.getId());
        assertEquals(1L, rr.getUserId());
        assertEquals(2L, rr.getOperatorId());
        assertEquals(3L, rr.getPlanId());
        assertEquals("123", rr.getMobileNumber());
        assertEquals(BigDecimal.TEN, rr.getAmount());
        assertEquals("S", rr.getStatus());
        
        RechargeResponse rr2 = new RechargeResponse(1L, 1L, 2L, 3L, "123", BigDecimal.TEN, "S");
        assertEquals("123", rr2.getMobileNumber());

        // UserResponse
        UserResponse ur = new UserResponse();
        ur.setId(1L); ur.setName("name"); ur.setEmail("email"); ur.setPhoneNumber("123");
        assertEquals(1L, ur.getId());
        assertEquals("name", ur.getName());
        assertEquals("email", ur.getEmail());
        assertEquals("123", ur.getPhoneNumber());
        
        UserResponse ur2 = new UserResponse(1L, "name", "email", "123");
        assertEquals("email", ur2.getEmail());
    }

    @Test
    void testEntity() {
        Notification n = new Notification();
        n.setId(1L); n.setUserId(1L); n.setMessage("msg"); n.setType("type"); n.setStatus("status"); n.setCreatedAt(LocalDateTime.now());
        assertEquals(1L, n.getId());
        assertEquals(1L, n.getUserId());
        assertEquals("msg", n.getMessage());
        assertEquals("type", n.getType());
        assertEquals("status", n.getStatus());
        assertNotNull(n.getCreatedAt());

        Notification n2 = new Notification(1L, 1L, "msg", "type", "status", LocalDateTime.now());
        assertEquals(1L, n2.getUserId());
    }

    @Test
    void testExceptions() {
        assertNotNull(new NotificationNotFoundException("msg"));
        assertNotNull(new ResourceNotFoundException("msg"));
    }

    @Test
    void testMain() {
        // Just invoke the constructor and main with empty args to cover entry point
        NotificationServiceApplication app = new NotificationServiceApplication();
        assertNotNull(app);
        // We can't easily run the real main without starting the full app, 
        // but we can at least exercise the class loading and constructor.
    }
}
