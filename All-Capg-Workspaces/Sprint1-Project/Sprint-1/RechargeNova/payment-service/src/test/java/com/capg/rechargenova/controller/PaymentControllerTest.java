package com.capg.rechargenova.controller;

import com.capg.rechargenova.controller.PaymentController;
import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse;
import com.capg.rechargenova.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ TEST: SUCCESS CASE
    @Test
    void testProcessPaymentSuccess() throws Exception {

        PaymentRequest request = new PaymentRequest(
                1L,
                101L,
                BigDecimal.valueOf(299),
                "UPI"
        );

        PaymentResponse response = new PaymentResponse(
                1L,
                1L,
                101L,
                BigDecimal.valueOf(299),
                "UPI",
                "SUCCESS",
                LocalDateTime.now()
        );

        Mockito.when(paymentService.processPayment(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())   // ✅ FIXED
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    // ❌ TEST: FAILURE CASE
    @Test
    void testProcessPaymentFailure() throws Exception {

        PaymentRequest request = new PaymentRequest(
                1L,
                101L,
                BigDecimal.valueOf(299),
                "UPI"
        );

        Mockito.when(paymentService.processPayment(Mockito.any()))
                .thenThrow(new RuntimeException("Payment Failed"));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof RuntimeException)
                );  // ✅ FIXED (no handler needed)
    }
}