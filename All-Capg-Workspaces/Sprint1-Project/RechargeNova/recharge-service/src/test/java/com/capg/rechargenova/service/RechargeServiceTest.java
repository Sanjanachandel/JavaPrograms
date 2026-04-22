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
        when(operatorClient.getPlanById(201L)).thenReturn(new PlanDto(201L, 999L, BigDecimal.valueOf(299), "28 days", "Unlimited"));
        assertThrows(RuntimeException.class, () -> rechargeService.initiateRecharge(userId, buildRequest()));
    }

    @Test
    void testInitiateRecharge_PaymentFailure() {
        Long userId = 501L;
        when(userClient.getUserById(userId)).thenReturn(new UserDto());
        when(operatorClient.getOperatorById(101L)).thenReturn(new OperatorDto(101L, ""));
        when(operatorClient.getPlanById(201L)).thenReturn(new PlanDto(201L, 101L, BigDecimal.valueOf(299), "", ""));
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
        when(operatorClient.getPlanById(any())).thenReturn(new PlanDto(1L, 1L, BigDecimal.TEN, "", ""));
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
    void testGetRechargesByUserId() {
        when(rechargeRepository.findByUserId(501L)).thenReturn(List.of(buildRecharge(1L, "SUCCESS")));
        assertEquals(1, rechargeService.getRechargesByUserId(501L).size());
    }
}
