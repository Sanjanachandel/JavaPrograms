package com.capg.rechargenova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.capg.rechargenova.client.*;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Recharge;
import com.capg.rechargenova.repository.RechargeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    private Recharge buildRecharge(Long id, String status) {
        Recharge r = new Recharge();
        r.setId(id);
        r.setUserId(501L);
        r.setOperatorId(101L);
        r.setPlanId(201L);
        r.setMobileNumber("9999999999");
        r.setAmount(BigDecimal.valueOf(299));
        r.setStatus(status);
        r.setPaymentMethod("UPI");
        r.setRechargeType("Prepaid");
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
        req.setRechargeType("Prepaid");
        return req;
    }

    @Test
    void testInitiateRecharge_Success() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        OperatorDto operator = new OperatorDto(101L, "Airtel");
        when(operatorClient.getOperatorById(101L)).thenReturn(operator);
        PlanDto plan = new PlanDto(201L, 101L, BigDecimal.valueOf(299), "28 days", "Unlimited", "Prepaid");
        when(operatorClient.getPlanById(201L)).thenReturn(plan);
        Recharge pending  = buildRecharge(1L, "PENDING");
        Recharge success  = buildRecharge(1L, "SUCCESS");
        when(rechargeRepository.save(any())).thenReturn(pending).thenReturn(success);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus("SUCCESS");
        when(paymentClient.processPayment(any())).thenReturn(paymentResponse);
        RechargeResponse response = rechargeService.initiateRecharge(userId, buildRequest());
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Recharge successful", response.getMessage());
    }

    @Test
    void testInitiateRecharge_UserNotFound() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rechargeService.initiateRecharge(userId, buildRequest()));
    }

    @Test
    void testInitiateRecharge_OperatorNotFound() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(any())).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rechargeService.initiateRecharge(userId, buildRequest()));
    }

    @Test
    void testInitiateRecharge_PlanNotFound() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(any())).thenReturn(new OperatorDto(1L, ""));
        when(operatorClient.getPlanById(any())).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rechargeService.initiateRecharge(userId, buildRequest()));
    }

    @Test
    void testInitiateRecharge_InvalidPlan_OperatorMismatch() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(101L)).thenReturn(new OperatorDto(101L, "Airtel"));
        when(operatorClient.getPlanById(201L)).thenReturn(new PlanDto(201L, 999L, BigDecimal.valueOf(299), "28 days", "Unlimited", "Prepaid"));
        assertThrows(RuntimeException.class, () -> rechargeService.initiateRecharge(userId, buildRequest()));
    }

    @Test
    void testInitiateRecharge_PaymentFailure() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(101L)).thenReturn(new OperatorDto(101L, ""));
        when(operatorClient.getPlanById(201L)).thenReturn(new PlanDto(201L, 101L, BigDecimal.valueOf(299), "", "", "Prepaid"));
        when(rechargeRepository.save(any())).thenReturn(buildRecharge(1L, "PENDING")).thenReturn(buildRecharge(1L, "FAILED"));
        when(paymentClient.processPayment(any())).thenThrow(new RuntimeException("Error"));
        RechargeResponse response = rechargeService.initiateRecharge(userId, buildRequest());
        assertEquals("FAILED", response.getStatus());
    }

    @Test
    void testInitiateRecharge_PaymentResponseNull() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(any())).thenReturn(new OperatorDto(1L, ""));
        when(operatorClient.getPlanById(any())).thenReturn(new PlanDto(1L, 1L, BigDecimal.TEN, "", "", "Prepaid"));
        when(rechargeRepository.save(any())).thenReturn(buildRecharge(1L, "PENDING"));
        when(paymentClient.processPayment(any())).thenReturn(null);
        RechargeResponse response = rechargeService.initiateRecharge(userId, buildRequest());
        assertEquals("FAILED", response.getStatus());
    }

    @Test
    void testGetRechargeById_Found() {
        when(rechargeRepository.findById(1L)).thenReturn(Optional.of(buildRecharge(1L, "SUCCESS")));
        RechargeResponse response = rechargeService.getRechargeById(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetRechargeById_NotFound() {
        when(rechargeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> rechargeService.getRechargeById(99L));
    }

    @Test
    void testInitiateRecharge_Razorpay() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(101L)).thenReturn(new OperatorDto(101L, "Airtel"));
        when(operatorClient.getPlanById(201L)).thenReturn(new PlanDto(201L, 101L, BigDecimal.valueOf(299), "28 days", "Unlimited", "Prepaid"));
        
        RechargeRequest request = buildRequest();
        request.setPaymentMethod("RAZORPAY");
        
        when(rechargeRepository.save(any())).thenReturn(buildRecharge(1L, "FAILED"));

        RechargeResponse response = rechargeService.initiateRecharge(userId, request);
        
        assertNotNull(response);
        assertEquals("FAILED", response.getStatus()); // It defaults to FAILED in builder
        assertTrue(response.getMessage().contains("Razorpay"));
        verify(paymentClient, never()).processPayment(any());
    }

    @Test
    void testGetAllRecharges() {
        Page<Recharge> page = new PageImpl<>(List.of(buildRecharge(1L, "SUCCESS")));
        when(rechargeRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<RechargeResponse> results = rechargeService.getAllRecharges(0, 10);
        assertEquals(1, results.getContent().size());
    }

    @Test
    void testCountRecharges() {
        when(rechargeRepository.count()).thenReturn(10L);
        assertEquals(10L, rechargeService.countRecharges());
    }

    @Test
    void testMapToResponse_NullCreatedAt() {
        Recharge recharge = new Recharge();
        recharge.setId(1L);
        recharge.setCreatedAt(null);
        // We need to call a method that uses mapToResponse
        when(rechargeRepository.findById(1L)).thenReturn(Optional.of(recharge));
        RechargeResponse response = rechargeService.getRechargeById(1L);
        assertNull(response.getCreatedAt());
    }

    @Test
    void testGetRechargesByUserId() {
        Page<Recharge> page = new PageImpl<>(List.of(buildRecharge(1L, "SUCCESS")));
        when(rechargeRepository.findByUserId(eq(501L), any(Pageable.class))).thenReturn(page);
        assertEquals(1, rechargeService.getRechargesByUserId(501L, 0, 10).getContent().size());
    }
}
