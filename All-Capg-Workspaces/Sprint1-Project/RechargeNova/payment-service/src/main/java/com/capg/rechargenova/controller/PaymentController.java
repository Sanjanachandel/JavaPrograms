package com.capg.rechargenova.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse; 
import com.capg.rechargenova.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment API", description = "Manage payment operations")
public class PaymentController {

    private static final Logger logger = LogManager.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * ================================================================
     * METHOD: processPayment
     * DESCRIPTION:
     * Processes a new payment request for a user and returns the result.
     * ================================================================
     */
    @Operation(summary = "Process a new payment")
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        logger.info("Received payment request for userId: {}", request.getUserId());

        PaymentResponse response = paymentService.processPayment(request);

        logger.info("Payment processed successfully for userId: {}", request.getUserId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * ================================================================
     * METHOD: getPaymentById
     * DESCRIPTION:
     * Fetches a payment by its unique payment ID.
     * ================================================================
     */
    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        logger.info("Fetching payment with ID: {}", id);
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    /**
     * ================================================================
     * METHOD: getPaymentsByUserId
     * DESCRIPTION:
     * Retrieves all payments associated with a specific user.
     * ================================================================
     */
    @Operation(summary = "Get payments by User ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUserId(@PathVariable Long userId) {
        logger.info("Fetching payments for userId: {}", userId);
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    /**
     * ================================================================
     * METHOD: getPaymentsByRechargeId
     * DESCRIPTION:
     * Retrieves all payments associated with a specific recharge ID.
     * ================================================================
     */
    @Operation(summary = "Get payments by Recharge ID")
    @GetMapping("/recharge/{id}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByRechargeId(@PathVariable Long id) {
        logger.info("Fetching payments for rechargeId: {}", id);
        return ResponseEntity.ok(paymentService.getPaymentsByRechargeId(id));
    }
}