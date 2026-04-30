package com.capg.rechargenova.controller;

import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        paymentResponse = new PaymentResponse();
        paymentResponse.setId(1L);
        paymentResponse.setAmount(BigDecimal.valueOf(299));
        paymentResponse.setStatus("SUCCESS");
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        PaymentOrderRequest request = new PaymentOrderRequest();
        PaymentOrderResponse response = new PaymentOrderResponse();
        response.setOrderId("order_123");

        when(paymentService.createOrder(any(PaymentOrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/payments/create-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("order_123"));
    }

    @Test
    void testCreateOrder_Exception() throws Exception {
        PaymentOrderRequest request = new PaymentOrderRequest();

        when(paymentService.createOrder(any(PaymentOrderRequest.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/payments/create-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testVerifyPayment_Success() throws Exception {
        PaymentVerificationRequest request = new PaymentVerificationRequest();

        when(paymentService.verifyPayment(any(PaymentVerificationRequest.class))).thenReturn(true);

        mockMvc.perform(post("/api/payments/verify-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment verified successfully"));
    }

    @Test
    void testVerifyPayment_InvalidSignature() throws Exception {
        PaymentVerificationRequest request = new PaymentVerificationRequest();

        when(paymentService.verifyPayment(any(PaymentVerificationRequest.class))).thenReturn(false);

        mockMvc.perform(post("/api/payments/verify-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid payment signature"));
    }

    @Test
    void testProcessPayment() throws Exception {
        PaymentRequest request = new PaymentRequest(1L, 101L, BigDecimal.valueOf(299), "UPI", "Prepaid");
        when(paymentService.processPayment(any())).thenReturn(paymentResponse);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void testGetPaymentById() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(paymentResponse);
        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetPaymentsByUserId() throws Exception {
        when(paymentService.getPaymentsByUserId(101L)).thenReturn(List.of(paymentResponse));
        mockMvc.perform(get("/api/payments/user/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetPaymentsByRechargeId() throws Exception {
        when(paymentService.getPaymentsByRechargeId(1L)).thenReturn(List.of(paymentResponse));
        mockMvc.perform(get("/api/payments/recharge/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}