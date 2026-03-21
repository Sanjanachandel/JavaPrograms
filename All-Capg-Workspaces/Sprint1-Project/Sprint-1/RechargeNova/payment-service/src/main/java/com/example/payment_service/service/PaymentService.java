package com.example.payment_service.service;

import com.example.payment_service.config.RabbitMQConfig;
import com.example.payment_service.dto.PaymentEvent;
import com.example.payment_service.dto.PaymentRequest;
import com.example.payment_service.dto.PaymentResponse;
import com.example.payment_service.entity.Transaction;
import com.example.payment_service.repository.TransactionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public PaymentResponse processPayment(PaymentRequest request) {
        // Logic to process payment
        // For simplicity, we assume payment is SUCCESS
        String status = "SUCCESS";

        Transaction transaction = Transaction.builder()
                .rechargeId(request.getRechargeId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(status)
                .transactionTime(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        // Publish event to RabbitMQ
        PaymentEvent event = new PaymentEvent(
                transaction.getId(),
                transaction.getRechargeId(),
                transaction.getUserId(),
                transaction.getStatus()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);

        return mapToResponse(transaction);
    }

    public PaymentResponse getPaymentById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
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
