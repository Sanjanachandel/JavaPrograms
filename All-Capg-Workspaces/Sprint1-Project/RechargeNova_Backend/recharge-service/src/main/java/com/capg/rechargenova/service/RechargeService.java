package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import com.capg.rechargenova.client.*;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Recharge;
import com.capg.rechargenova.exception.InvalidRechargeException;
import com.capg.rechargenova.exception.RechargeNotFoundException;
import com.capg.rechargenova.repository.RechargeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class RechargeService {

    private static final Logger logger = LogManager.getLogger(RechargeService.class);

    private final RechargeRepository rechargeRepository;
    private final UserClient userClient;
    private final OperatorClient operatorClient;
    private final PaymentClient paymentClient;

    /**
     * ================================================================
     * METHOD: Constructor
     * DESCRIPTION:
     * Constructor-based dependency injection for all clients and repository.
     * ================================================================
     */
    public RechargeService(RechargeRepository rechargeRepository,
                           UserClient userClient,
                           OperatorClient operatorClient,
                           PaymentClient paymentClient) {
        this.rechargeRepository = rechargeRepository;
        this.userClient = userClient;
        this.operatorClient = operatorClient;
        this.paymentClient = paymentClient;
    }

    /**
     * ================================================================
     * METHOD: initiateRecharge
     * DESCRIPTION:
     * Initiates a recharge process for a user, validates user, operator, plan,
     * processes payment, and updates recharge status.
     * ================================================================
     */
    public RechargeResponse initiateRecharge(Long userId, RechargeRequest request) {

        logger.info("Starting recharge for userId: {}", userId);

        // Validate User
        UserDto user = userClient.getUserById(userId);
        if (user == null) {
            logger.error("User not found: {}", userId);
            throw new InvalidRechargeException("User not found");
        }

        // Validate Operator and Plan
        OperatorDto operator = operatorClient.getOperatorById(request.getOperatorId());
        PlanDto plan = operatorClient.getPlanById(request.getPlanId());
        if (operator == null || plan == null || !plan.getOperatorId().equals(operator.getId())) {
            logger.error("Invalid Operator/Plan for userId: {}", userId);
            throw new RuntimeException("Invalid Operator or Plan");
        }

        // Save Recharge with PENDING status
        Recharge recharge = Recharge.builder()
                .userId(userId)
                .operatorId(operator.getId())
                .planId(plan.getId())
                .mobileNumber(request.getMobileNumber())
                .amount(plan.getAmount())
                .status("FAILED")
                .paymentMethod(request.getPaymentMethod())
                .rechargeType(request.getRechargeType())
                .build();
        recharge = rechargeRepository.save(recharge);
        logger.info("Recharge created with ID: {}", recharge.getId());

        // If payment method is RAZORPAY, we don't process it synchronously.
        // The frontend will handle the Razorpay flow and the payment-service will notify us via RabbitMQ.
        if ("RAZORPAY".equalsIgnoreCase(request.getPaymentMethod())) {
            logger.info("Razorpay payment detected for rechargeId: {}. Skipping sync processing.", recharge.getId());
            return mapToResponse(recharge, "Recharge initiated. Please complete payment via Razorpay.");
        }

        // Prepare Payment Request for other methods (Legacy/Dummy)
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .rechargeId(recharge.getId())
                .userId(userId)
                .amount(plan.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .rechargeType(request.getRechargeType())
                .build();

        try {
            logger.info("Calling payment service for synchronous processing...");

            // Call Payment Service
            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest);
            if (paymentResponse == null) {
                logger.error("Payment response is null for rechargeId: {}", recharge.getId());
                throw new InvalidRechargeException("Payment response is null");
            }

            // Update Recharge Status (SUCCESS/FAILED)
            recharge.setStatus(paymentResponse.getStatus());
            recharge = rechargeRepository.save(recharge);

            logger.info("Synchronous recharge successful for id: {}", recharge.getId());

            return mapToResponse(recharge, "Recharge successful");

        } catch (Exception e) {
            logger.error("Synchronous payment failed for rechargeId {}: {}", recharge.getId(), e.getMessage());

            // Handle Payment Failure
            recharge.setStatus("FAILED");
            rechargeRepository.save(recharge);

            return mapToResponse(recharge, "Recharge failed during payment");
        }
    }

    /**
     * ================================================================
     * METHOD: getRechargeById
     * DESCRIPTION:
     * Fetches a recharge record by its ID.
     * ================================================================
     */
    public RechargeResponse getRechargeById(Long id) {

        logger.info("Fetching recharge by id: {}", id);

        Recharge recharge = rechargeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Recharge not found for id: {}", id);
                    return new RechargeNotFoundException("Recharge not found");
                });

        return mapToResponse(recharge, "Success");
    }

    /**
     * ================================================================
     * METHOD: getRechargesByUserId
     * DESCRIPTION:
     * Retrieves all recharge records for a specific user with pagination.
     * ================================================================
     */
    public Page<RechargeResponse> getRechargesByUserId(Long userId, int page, int size) {
        logger.info("Fetching recharges for userId: {} (Page: {}, Size: {})", userId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return rechargeRepository.findByUserId(userId, pageable)
                .map(r -> mapToResponse(r, "Success"));
    }

    public Page<RechargeResponse> getAllRecharges(int page, int size) {
        logger.info("Fetching all recharges for admin (Page: {}, Size: {})", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return rechargeRepository.findAll(pageable)
                .map(r -> mapToResponse(r, "Success"));
    }

    public long countRecharges() {
        return rechargeRepository.count();
    }

    public Double getTotalRevenue() {
        Double total = rechargeRepository.sumTotalRevenue();
        return total != null ? total : 0.0;
    }

    /**
     * ================================================================
     * METHOD: mapToResponse
     * DESCRIPTION:
     * Maps a Recharge entity to RechargeResponse DTO with message.
     * ================================================================
     */
    private RechargeResponse mapToResponse(Recharge recharge, String message) {
        return RechargeResponse.builder()
                .id(recharge.getId())
                .userId(recharge.getUserId())
                .operatorId(recharge.getOperatorId())
                .planId(recharge.getPlanId())
                .mobileNumber(recharge.getMobileNumber())
                .amount(recharge.getAmount())
                .status(recharge.getStatus())
                .paymentMethod(recharge.getPaymentMethod())
                .rechargeType(recharge.getRechargeType())
                .createdAt(recharge.getCreatedAt() != null ? recharge.getCreatedAt().atOffset(java.time.ZoneOffset.UTC) : null)
                .message(message)
                .build();
    }
}