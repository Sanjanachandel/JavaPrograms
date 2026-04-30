package com.capg.rechargenova.service;

import com.capg.rechargenova.config.RabbitMQConfig;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Transaction;
import com.capg.rechargenova.repository.TransactionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${razorpay.key.id:}")
    private String keyId;

    @Value("${razorpay.key.secret:}")
    private String keySecret;

    public PaymentOrderResponse createOrder(PaymentOrderRequest request) throws RazorpayException {
        if (keyId == null || keyId.isEmpty()) {
            throw new RuntimeException("Razorpay Key ID is not configured. Check your .env file.");
        }
        
        int amountInPaise = (int) (request.getAmount() * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
        orderRequest.put("receipt", request.getReceipt() != null ? request.getReceipt() : "recharge_" + request.getRechargeId());

        Order order = razorpayClient.orders.create(orderRequest);

        return PaymentOrderResponse.builder()
                .orderId(order.get("id"))
                .currency(order.get("currency"))
                .amount(order.get("amount"))
                .keyId(keyId)
                .build();
    }

    public boolean verifyPayment(PaymentVerificationRequest request) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret.trim());
            System.out.println("Payment Verification for Order " + request.getRazorpayOrderId() + ": " + isValid);

            if (isValid) {
                Transaction transaction = Transaction.builder()
                        .rechargeId(request.getRechargeId())
                        .userId(request.getUserId())
                        .amount(request.getAmount() != null ? request.getAmount() : BigDecimal.ZERO)
                        .paymentMethod("Razorpay")
                        .status("SUCCESS")
                        .transactionTime(LocalDateTime.now())
                        .build();
                
                transaction = transactionRepository.save(transaction);

                PaymentEvent event = PaymentEvent.builder()
                        .transactionId(transaction.getId())
                        .rechargeId(request.getRechargeId())
                        .userId(request.getUserId())
                        .amount(transaction.getAmount())
                        .status("SUCCESS")
                        .rechargeType(request.getRechargeType())
                        .build();

                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
                return true;
            } else {
                System.err.println("INVALID SIGNATURE for Order: " + request.getRazorpayOrderId());
            }
        } catch (Exception e) {
            System.err.println("Verification Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    // Original Endpoints logic
    public PaymentResponse processPayment(PaymentRequest request) {
        Transaction transaction = Transaction.builder()
                .rechargeId(request.getRechargeId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status("SUCCESS")
                .transactionTime(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        PaymentEvent event = PaymentEvent.builder()
                .transactionId(transaction.getId())
                .rechargeId(request.getRechargeId())
                .userId(request.getUserId())
                .amount(transaction.getAmount())
                .status("SUCCESS")
                .rechargeType(request.getRechargeType())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

        return mapToResponse(transaction);
    }

    public PaymentResponse getPaymentById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        return mapToResponse(transaction);
    }

    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByRechargeId(Long rechargeId) {
        return transactionRepository.findByRechargeId(rechargeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Transaction transaction) {
        return PaymentResponse.builder()
                .id(transaction.getId())
                .rechargeId(transaction.getRechargeId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .paymentMethod(transaction.getPaymentMethod())
                .status(transaction.getStatus())
                .transactionTime(transaction.getTransactionTime())
                .build();
    }
}