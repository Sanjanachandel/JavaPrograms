package com.capg.rechargenova.controller;

import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse;
import com.capg.rechargenova.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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
    void testProcessPayment() throws Exception {
        PaymentRequest request = new PaymentRequest(1L, 101L, BigDecimal.valueOf(299), "UPI");
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