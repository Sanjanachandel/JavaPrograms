package com.capg.rechargenova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.capg.rechargenova.client.*;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Recharge;
import com.capg.rechargenova.repository.RechargeRepository;
import com.capg.rechargenova.service.RechargeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RechargeServiceTest {

    @InjectMocks
    private RechargeService rechargeService;

    @Mock
    private RechargeRepository rechargeRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private OperatorClient operatorClient;

    @Mock
    private PaymentClient paymentClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Recharge buildRecharge(Long id, String status) {
        Recharge r = new Recharge();
        r.setId(id);
        r.setUserId(501L);
        r.setOperatorId(101L);
        r.setPlanId(201L);
        r.setMobileNumber("9999999999");
        r.setAmount(BigDecimal.valueOf(299));
        r.setStatus(status);
        // simulate @PrePersist so mapToResponse never NPEs on createdAt
        try {
            var m = Recharge.class.getDeclaredMethod("onCreate");
            m.setAccessible(true);
            m.invoke(r);
        } catch (Exception ignored) {}
        return r;
    }

    private RechargeRequest buildRequest() {
        RechargeRequest req = new RechargeRequest();
        req.setOperatorId(101L);
        req.setPlanId(201L);
        req.setMobileNumber("9999999999");
        req.setPaymentMethod("UPI");
        return req;
    }

    // ── tests ─────────────────────────────────────────────────────────────────

    @Test
    void testInitiateRecharge_Success() {

        Long userId = 501L;

        when(userClient.getUserById(userId)).thenReturn(new UserDto());

        OperatorDto operator = new OperatorDto(101L, "Airtel");
        when(operatorClient.getOperatorById(101L)).thenReturn(operator);

        PlanDto plan = new PlanDto(201L, 101L, BigDecimal.valueOf(299), "28 days", "Unlimited");
        when(operatorClient.getPlanById(201L)).thenReturn(plan);

        Recharge pending  = buildRecharge(1L, "PENDING");
        Recharge success  = buildRecharge(1L, "SUCCESS");

        when(rechargeRepository.save(any()))
                .thenReturn(pending)
                .thenReturn(success);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus("SUCCESS");
        when(paymentClient.processPayment(any())).thenReturn(paymentResponse);

        RechargeResponse response = rechargeService.initiateRecharge(userId, buildRequest());

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(userId, response.getUserId());
        assertEquals(101L, response.getOperatorId());
        assertEquals(201L, response.getPlanId());
        assertEquals("Recharge successful", response.getMessage());

        verify(userClient).getUserById(userId);
        verify(operatorClient).getOperatorById(101L);
        verify(operatorClient).getPlanById(201L);
        verify(paymentClient).processPayment(any());
        verify(rechargeRepository, times(2)).save(any());
    }

    @Test
    void testInitiateRecharge_UserNotFound() {

        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rechargeService.initiateRecharge(userId, buildRequest()));

        assertEquals("User not found", ex.getMessage());
        verify(userClient).getUserById(userId);
        verifyNoInteractions(operatorClient, paymentClient, rechargeRepository);
    }

    @Test
    void testInitiateRecharge_InvalidPlan_OperatorMismatch() {

        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());

        OperatorDto operator = new OperatorDto(101L, "Airtel");
        when(operatorClient.getOperatorById(101L)).thenReturn(operator);

        // plan belongs to a different operator
        PlanDto plan = new PlanDto(201L, 999L, BigDecimal.valueOf(299), "28 days", "Unlimited");
        when(operatorClient.getPlanById(201L)).thenReturn(plan);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rechargeService.initiateRecharge(userId, buildRequest()));

        assertEquals("Invalid Operator or Plan", ex.getMessage());
        verifyNoInteractions(paymentClient, rechargeRepository);
    }

    @Test
    void testInitiateRecharge_PaymentFailure() {

        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());

        OperatorDto operator = new OperatorDto(101L, "Airtel");
        when(operatorClient.getOperatorById(101L)).thenReturn(operator);

        PlanDto plan = new PlanDto(201L, 101L, BigDecimal.valueOf(299), "28 days", "Unlimited");
        when(operatorClient.getPlanById(201L)).thenReturn(plan);

        Recharge pending = buildRecharge(1L, "PENDING");
        Recharge failed  = buildRecharge(1L, "FAILED");

        // first save → PENDING, second save (after failure) → FAILED
        when(rechargeRepository.save(any()))
                .thenReturn(pending)
                .thenReturn(failed);

        when(paymentClient.processPayment(any()))
                .thenThrow(new RuntimeException("Payment gateway error"));

        RechargeResponse response = rechargeService.initiateRecharge(userId, buildRequest());

        assertEquals("FAILED", response.getStatus());
        assertEquals("Recharge failed during payment", response.getMessage());
        verify(rechargeRepository, times(2)).save(any());
    }

    @Test
    void testGetRechargeById_Found() {

        Recharge recharge = buildRecharge(1L, "SUCCESS");
        when(rechargeRepository.findById(1L)).thenReturn(Optional.of(recharge));

        RechargeResponse response = rechargeService.getRechargeById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    void testGetRechargeById_NotFound() {

        when(rechargeRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rechargeService.getRechargeById(99L));

        assertEquals("Recharge not found", ex.getMessage());
    }

    @Test
    void testGetRechargesByUserId() {

        Recharge r1 = buildRecharge(1L, "SUCCESS");
        Recharge r2 = buildRecharge(2L, "FAILED");
        when(rechargeRepository.findByUserId(501L)).thenReturn(List.of(r1, r2));

        List<RechargeResponse> responses = rechargeService.getRechargesByUserId(501L);

        assertEquals(2, responses.size());
        assertEquals("SUCCESS", responses.get(0).getStatus());
        assertEquals("FAILED",  responses.get(1).getStatus());
    }
}
