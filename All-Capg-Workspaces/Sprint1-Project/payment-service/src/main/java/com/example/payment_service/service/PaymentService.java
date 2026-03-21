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

        Transaction transaction = new Transaction();
        transaction.setRechargeId(request.getRechargeId());
        transaction.setUserId(request.getUserId());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setStatus(status);
        transaction.setTransactionTime(LocalDateTime.now());

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
        PaymentResponse response = new PaymentResponse();
        response.setId(transaction.getId());
        response.setRechargeId(transaction.getRechargeId());
        response.setUserId(transaction.getUserId());
        response.setAmount(transaction.getAmount());
        response.setPaymentMethod(transaction.getPaymentMethod());
        response.setStatus(transaction.getStatus());
        response.setTransactionTime(transaction.getTransactionTime());
        return response;
    }
}
