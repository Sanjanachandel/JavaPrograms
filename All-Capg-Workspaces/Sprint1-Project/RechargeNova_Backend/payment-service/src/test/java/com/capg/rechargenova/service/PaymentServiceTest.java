package com.capg.rechargenova.service;

import com.capg.rechargenova.config.RabbitMQConfig;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Transaction;
import com.capg.rechargenova.repository.TransactionRepository;
import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Mock
    private RazorpayClient razorpayClient;

    @Mock
    private OrderClient orderClient;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(paymentService, "keyId", "test_key");
        ReflectionTestUtils.setField(paymentService, "keySecret", "test_secret");

        java.lang.reflect.Field ordersField = RazorpayClient.class.getDeclaredField("orders");
        ordersField.setAccessible(true);
        ordersField.set(razorpayClient, orderClient);
    }

    @Test
    void testCreateOrder_Success() throws RazorpayException {
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setAmount(100.0);
        request.setCurrency("INR");
        request.setReceipt("receipt_1");
        request.setRechargeId(1L);

        Order mockOrder = mock(Order.class);
        when(mockOrder.get("id")).thenReturn("order_123");
        when(mockOrder.get("currency")).thenReturn("INR");
        when(mockOrder.get("amount")).thenReturn(10000);

        when(orderClient.create(any(JSONObject.class))).thenReturn(mockOrder);

        PaymentOrderResponse response = paymentService.createOrder(request);

        assertNotNull(response);
        assertEquals("order_123", response.getOrderId());
        assertEquals("INR", response.getCurrency());
        assertEquals("test_key", response.getKeyId());
    }

    @Test
    void testCreateOrder_MissingKeyId() {
        ReflectionTestUtils.setField(paymentService, "keyId", "");
        PaymentOrderRequest request = new PaymentOrderRequest();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.createOrder(request));
        assertEquals("Razorpay Key ID is not configured. Check your .env file.", exception.getMessage());
    }

    @Test
    void testCreateOrder_NullKeyId() {
        ReflectionTestUtils.setField(paymentService, "keyId", null);
        PaymentOrderRequest request = new PaymentOrderRequest();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.createOrder(request));
        assertEquals("Razorpay Key ID is not configured. Check your .env file.", exception.getMessage());
    }

    @Test
    void testCreateOrder_DefaultCurrencyAndReceipt() throws RazorpayException {
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setAmount(50.0);
        request.setRechargeId(5L);

        Order mockOrder = mock(Order.class);
        when(orderClient.create(any(JSONObject.class))).thenReturn(mockOrder);

        paymentService.createOrder(request);
        verify(orderClient, times(1)).create(any(JSONObject.class));
    }

    @Test
    void testVerifyPayment_Success() {
        PaymentVerificationRequest request = new PaymentVerificationRequest();
        request.setRazorpayOrderId("order_123");
        request.setRazorpayPaymentId("pay_123");
        request.setRazorpaySignature("signature_123");
        request.setRechargeId(1L);
        request.setUserId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        Transaction savedTransaction = Transaction.builder().id(10L).amount(BigDecimal.valueOf(100)).build();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyPaymentSignature(any(JSONObject.class), eq("test_secret"))).thenReturn(true);

            boolean result = paymentService.verifyPayment(request);

            assertTrue(result);
            verify(transactionRepository, times(1)).save(any(Transaction.class));
            verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.EXCHANGE), eq(RabbitMQConfig.ROUTING_KEY), any(PaymentEvent.class));
        }
    }

    @Test
    void testVerifyPayment_Success_NullAmount() {
        PaymentVerificationRequest request = new PaymentVerificationRequest();
        request.setAmount(null);

        Transaction savedTransaction = Transaction.builder().id(10L).amount(BigDecimal.ZERO).build();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyPaymentSignature(any(JSONObject.class), eq("test_secret"))).thenReturn(true);

            boolean result = paymentService.verifyPayment(request);

            assertTrue(result);
        }
    }

    @Test
    void testVerifyPayment_InvalidSignature() {
        PaymentVerificationRequest request = new PaymentVerificationRequest();

        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyPaymentSignature(any(JSONObject.class), eq("test_secret"))).thenReturn(false);

            boolean result = paymentService.verifyPayment(request);

            assertFalse(result);
            verify(transactionRepository, never()).save(any(Transaction.class));
        }
    }

    @Test
    void testVerifyPayment_Exception() {
        PaymentVerificationRequest request = new PaymentVerificationRequest();

        try (MockedStatic<Utils> utilsMock = mockStatic(Utils.class)) {
            utilsMock.when(() -> Utils.verifyPaymentSignature(any(JSONObject.class), eq("test_secret")))
                    .thenThrow(new RuntimeException("Test Exception"));

            boolean result = paymentService.verifyPayment(request);

            assertFalse(result);
        }
    }

    @Test
    void testProcessPaymentSuccess() {
        PaymentRequest request = new PaymentRequest(1L, 101L, BigDecimal.valueOf(299), "UPI", "Prepaid");
        Transaction savedTransaction = Transaction.builder()
                .id(1L)
                .rechargeId(1L)
                .userId(101L)
                .amount(BigDecimal.valueOf(299))
                .paymentMethod("UPI")
                .status("SUCCESS")
                .transactionTime(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any())).thenReturn(savedTransaction);

        PaymentResponse response = paymentService.processPayment(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void testGetPaymentById_Success() {
        Transaction t = Transaction.builder().id(1L).build();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(t));

        PaymentResponse response = paymentService.getPaymentById(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetPaymentsByUserId() {
        Transaction t = Transaction.builder().userId(101L).build();
        when(transactionRepository.findByUserId(101L)).thenReturn(List.of(t));

        List<PaymentResponse> result = paymentService.getPaymentsByUserId(101L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetPaymentsByRechargeId() {
        Transaction t = Transaction.builder().rechargeId(1L).build();
        when(transactionRepository.findByRechargeId(1L)).thenReturn(List.of(t));

        List<PaymentResponse> result = paymentService.getPaymentsByRechargeId(1L);
        assertEquals(1, result.size());
    }
}