package com.capg.rechargenova.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse;
import com.capg.rechargenova.entity.Transaction;
import com.capg.rechargenova.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPaymentSuccess() {
        PaymentRequest request = new PaymentRequest(1L, 101L, BigDecimal.valueOf(299), "UPI");
        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        savedTransaction.setRechargeId(1L);
        savedTransaction.setUserId(101L);
        savedTransaction.setAmount(BigDecimal.valueOf(299));
        savedTransaction.setPaymentMethod("UPI");
        savedTransaction.setStatus("SUCCESS");

        when(transactionRepository.save(any())).thenReturn(savedTransaction);
        PaymentResponse response = paymentService.processPayment(request);
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void testProcessPaymentFailure() {
        PaymentRequest request = new PaymentRequest(1L, 101L, BigDecimal.valueOf(299), "UPI");
        when(transactionRepository.save(any())).thenThrow(new RuntimeException("DB Error"));
        assertThrows(RuntimeException.class, () -> paymentService.processPayment(request));
    }

    @Test
    void testGetPaymentById_Success() {
        Transaction t = new Transaction();
        t.setId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(t));
        PaymentResponse response = paymentService.getPaymentById(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.getPaymentById(1L));
    }

    @Test
    void testGetPaymentsByUserId() {
        Transaction t = new Transaction();
        t.setUserId(101L);
        when(transactionRepository.findByUserId(101L)).thenReturn(List.of(t));
        List<PaymentResponse> result = paymentService.getPaymentsByUserId(101L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetPaymentsByRechargeId() {
        Transaction t = new Transaction();
        t.setRechargeId(1L);
        when(transactionRepository.findByRechargeId(1L)).thenReturn(List.of(t));
        List<PaymentResponse> result = paymentService.getPaymentsByRechargeId(1L);
        assertEquals(1, result.size());
    }
}