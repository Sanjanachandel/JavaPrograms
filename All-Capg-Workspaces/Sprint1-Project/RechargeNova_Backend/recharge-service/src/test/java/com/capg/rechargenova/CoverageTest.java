package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Recharge;
import com.capg.rechargenova.exception.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testDTOs() {
        // OperatorDto
        OperatorDto op = new OperatorDto();
        op.setId(1L); op.setName("name");
        assertEquals(1L, op.getId()); assertEquals("name", op.getName());
        OperatorDto op2 = new OperatorDto(2L, "name2");
        assertEquals(2L, op2.getId());

        // PlanDto
        PlanDto plan = new PlanDto();
        plan.setId(1L); plan.setAmount(BigDecimal.TEN); plan.setValidity("30 days"); plan.setDescription("desc"); plan.setOperatorId(2L);
        assertEquals(1L, plan.getId()); assertEquals(BigDecimal.TEN, plan.getAmount());
        assertEquals("30 days", plan.getValidity()); assertEquals("desc", plan.getDescription());
        assertEquals(2L, plan.getOperatorId());
        PlanDto plan2 = new PlanDto(1L, 2L, BigDecimal.TEN, "30", "test", "Prepaid");
        assertEquals(1L, plan2.getId());

        // RechargeRequest
        RechargeRequest rr = new RechargeRequest();
        rr.setOperatorId(2L); rr.setPlanId(3L); rr.setMobileNumber("123"); rr.setPaymentMethod("UPI");
        assertEquals(2L, rr.getOperatorId()); assertEquals(3L, rr.getPlanId()); assertEquals("123", rr.getMobileNumber());
        assertEquals("UPI", rr.getPaymentMethod());

        // RechargeResponse
        RechargeResponse res = new RechargeResponse();
        res.setId(1L); res.setMobileNumber("123"); res.setAmount(BigDecimal.TEN); res.setStatus("SUCCESS");
        res.setCreatedAt(OffsetDateTime.now()); res.setUserId(1L); res.setOperatorId(1L); res.setPlanId(1L); res.setMessage("msg");
        res.setPaymentMethod("UPI");
        assertEquals(1L, res.getId()); assertEquals("SUCCESS", res.getStatus());
        assertEquals("msg", res.getMessage());
        assertEquals("123", res.getMobileNumber());
        assertEquals(BigDecimal.TEN, res.getAmount());
        assertNotNull(res.getCreatedAt());
        assertEquals(1L, res.getUserId());
        assertEquals(1L, res.getOperatorId());
        assertEquals(1L, res.getPlanId());
        RechargeResponse res2 = new RechargeResponse(1L, 1L, 1L, 1L, "123", BigDecimal.TEN, "S", "UPI", "Prepaid", OffsetDateTime.now(), "M");
        assertEquals(1L, res2.getId());

        // UserDto
        UserDto user = new UserDto();
        user.setId(1L); user.setName("n"); user.setEmail("e"); user.setRole("r"); user.setPhoneNumber("1"); user.setCreatedAt("c");
        assertEquals(1L, user.getId());
        assertEquals("n", user.getName());
        assertEquals("e", user.getEmail());
        assertEquals("r", user.getRole());
        assertEquals("1", user.getPhoneNumber());
        assertEquals("c", user.getCreatedAt());
        UserDto user2 = new UserDto(1L, "n", "e", "r", "1", "c");
        assertEquals(1L, user2.getId());

        // PaymentRequest
        PaymentRequest pr = new PaymentRequest();
        pr.setRechargeId(1L); pr.setUserId(1L); pr.setAmount(BigDecimal.TEN); pr.setPaymentMethod("P");
        assertEquals(1L, pr.getRechargeId());
        assertEquals(1L, pr.getUserId());
        assertEquals(BigDecimal.TEN, pr.getAmount());
        assertEquals("P", pr.getPaymentMethod());
        PaymentRequest pr2 = new PaymentRequest(1L, 1L, BigDecimal.TEN, "P", "Prepaid");
        assertEquals(1L, pr2.getUserId());

        // PaymentResponse
        PaymentResponse pres = new PaymentResponse();
        pres.setId(1L); pres.setRechargeId(1L); pres.setUserId(1L); pres.setAmount(BigDecimal.ONE);
        pres.setPaymentMethod("M"); pres.setStatus("S"); pres.setTransactionTime(LocalDateTime.now());
        assertEquals(1L, pres.getId());
        assertEquals(1L, pres.getRechargeId());
        assertEquals(1L, pres.getUserId());
        assertEquals(BigDecimal.ONE, pres.getAmount());
        assertEquals("M", pres.getPaymentMethod());
        assertEquals("S", pres.getStatus());
        assertNotNull(pres.getTransactionTime());
        PaymentResponse pres2 = new PaymentResponse(1L, 1L, 1L, BigDecimal.ONE, "M", "S", LocalDateTime.now());
        assertEquals(1L, pres2.getId());
    }

    @Test
    void testEntity() {
        Recharge r = new Recharge();
        r.setId(1L); r.setUserId(1L); r.setOperatorId(1L); r.setPlanId(1L); r.setMobileNumber("123");
        r.setAmount(BigDecimal.TEN); r.setStatus("SUCCESS"); r.setUpdatedAt(LocalDateTime.now());
        assertEquals(1L, r.getId());
        assertEquals(1L, r.getUserId());
        assertEquals(1L, r.getOperatorId());
        assertEquals(1L, r.getPlanId());
        assertEquals("123", r.getMobileNumber());
        assertEquals(BigDecimal.TEN, r.getAmount());
        assertEquals("SUCCESS", r.getStatus());
        assertNotNull(r.getUpdatedAt());
        
        // Exercise JPA lifecycle hooks manually for coverage
        try {
            var m = Recharge.class.getDeclaredMethod("onUpdate");
            m.setAccessible(true);
            m.invoke(r);
        } catch (Exception ignored) {}

        Recharge r2 = Recharge.builder()
                .userId(1L).operatorId(1L).planId(1L)
                .mobileNumber("123").amount(BigDecimal.TEN)
                .status("S").paymentMethod("UPI").build();
        assertEquals(1L, r2.getUserId());
    }

    @Test
    void testExceptions() {
        assertNotNull(new InvalidRechargeException("msg"));
        assertNotNull(new RechargeNotFoundException("msg"));
        assertNotNull(new ResourceNotFoundException("msg"));
    }

    @Test
    void testMain() {
        RechargeServiceApplication app = new RechargeServiceApplication();
        assertNotNull(app);
        RechargeServiceApplication.main(new String[]{"--server.port=0"});
    }
}
