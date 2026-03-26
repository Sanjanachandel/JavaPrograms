package com.capg.rechargenova.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse;
import com.capg.rechargenova.entity.Transaction;
import com.capg.rechargenova.repository.TransactionRepository;
import com.capg.rechargenova.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;

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

    // ✅ TEST 1: SUCCESS CASE
    @Test
    void testProcessPaymentSuccess() {

        PaymentRequest request = new PaymentRequest(
                1L,
                101L,
                BigDecimal.valueOf(299),
                "UPI"
        );

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

    // ❌ TEST 2: FAILURE CASE
    @Test
    void testProcessPaymentFailure() {

        PaymentRequest request = new PaymentRequest(
                1L,
                101L,
                BigDecimal.valueOf(299),
                "UPI"
        );

        when(transactionRepository.save(any()))
                .thenThrow(new RuntimeException("DB Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.processPayment(request);
        });

        assertEquals("DB Error", exception.getMessage());
    }
}