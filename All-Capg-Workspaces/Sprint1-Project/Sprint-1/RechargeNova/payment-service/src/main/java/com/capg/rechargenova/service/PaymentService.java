package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.rechargenova.config.RabbitMQConfig;
import com.capg.rechargenova.dto.PaymentEvent;
import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse;
import com.capg.rechargenova.entity.Transaction;
import com.capg.rechargenova.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * ================================================================
     * METHOD: processPayment
     * DESCRIPTION:
     * Processes a payment request, saves the transaction, sends an event
     * to RabbitMQ, and returns the PaymentResponse.
     * ================================================================
     */
    public PaymentResponse processPayment(PaymentRequest request) {

        logger.info("Processing payment for userId: {}", request.getUserId());

        String status = "SUCCESS";

        Transaction transaction = new Transaction();
        transaction.setRechargeId(request.getRechargeId());
        transaction.setUserId(request.getUserId());
        transaction.setAmount(request.getAmount());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setStatus(status);
        transaction.setTransactionTime(LocalDateTime.now());

        transaction = transactionRepository.save(transaction);

        logger.info("Transaction saved with ID: {}", transaction.getId());

        PaymentEvent event = new PaymentEvent(
                transaction.getId(),
                transaction.getRechargeId(),
                transaction.getUserId(),
                transaction.getStatus()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        logger.info("Event sent to RabbitMQ for transactionId: {}", transaction.getId());

        return mapToResponse(transaction);
    }

    /**
     * ================================================================
     * METHOD: getPaymentById
     * DESCRIPTION:
     * Retrieves a payment transaction by its unique ID.
     * ================================================================
     */
    public PaymentResponse getPaymentById(Long id) {

        logger.info("Fetching payment by ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Transaction not found for ID: {}", id);
                    return new RuntimeException("Transaction not found");
                });

        return mapToResponse(transaction);
    }

    /**
     * ================================================================
     * METHOD: getPaymentsByUserId
     * DESCRIPTION:
     * Retrieves all payments associated with a specific user ID.
     * ================================================================
     */
    public List<PaymentResponse> getPaymentsByUserId(Long userId) {

        logger.info("Fetching payments for userId: {}", userId);

        return transactionRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================================================================
     * METHOD: getPaymentsByRechargeId
     * DESCRIPTION:
     * Retrieves all payments associated with a specific recharge ID.
     * ================================================================
     */
    public List<PaymentResponse> getPaymentsByRechargeId(Long rechargeId) {

        logger.info("Fetching payments for rechargeId: {}", rechargeId);

        return transactionRepository.findByRechargeId(rechargeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * ================================================================
     * METHOD: mapToResponse
     * DESCRIPTION:
     * Maps a Transaction entity to a PaymentResponse DTO.
     * ================================================================
     */
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