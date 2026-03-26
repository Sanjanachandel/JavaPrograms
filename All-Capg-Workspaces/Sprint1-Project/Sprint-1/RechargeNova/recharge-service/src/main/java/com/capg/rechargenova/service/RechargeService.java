package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import com.capg.rechargenova.client.*;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.Recharge;
import com.capg.rechargenova.repository.RechargeRepository;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new RuntimeException("User not found");
        }

        // Validate Operator and Plan
        OperatorDto operator = operatorClient.getOperatorById(request.getOperatorId());
        PlanDto plan = operatorClient.getPlanById(request.getPlanId());
        if (operator == null || plan == null || !plan.getOperatorId().equals(operator.getId())) {
            logger.error("Invalid Operator/Plan for userId: {}", userId);
            throw new RuntimeException("Invalid Operator or Plan");
        }

        // Save Recharge with PENDING status
        Recharge recharge = new Recharge(
                userId,
                operator.getId(),
                plan.getId(),
                request.getMobileNumber(),
                plan.getAmount(),
                "PENDING"
        );
        recharge = rechargeRepository.save(recharge);
        logger.info("Recharge created with ID: {}", recharge.getId());

        // Prepare Payment Request
        PaymentRequest paymentRequest = new PaymentRequest(
                recharge.getId(),
                userId,
                plan.getAmount(),
                request.getPaymentMethod()
        );

        try {
            logger.info("Calling payment service...");

            // Call Payment Service
            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest);
            if (paymentResponse == null) {
                logger.error("Payment response is null for rechargeId: {}", recharge.getId());
                throw new RuntimeException("Payment response is null");
            }

            // Update Recharge Status (SUCCESS/FAILED)
            recharge.setStatus(paymentResponse.getStatus());
            recharge = rechargeRepository.save(recharge);

            logger.info("Recharge successful for id: {}", recharge.getId());

            return mapToResponse(recharge, "Recharge successful");

        } catch (Exception e) {
            logger.error("Payment failed for rechargeId {}: {}", recharge.getId(), e.getMessage());

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
                    return new RuntimeException("Recharge not found");
                });

        return mapToResponse(recharge, "Success");
    }

    /**
     * ================================================================
     * METHOD: getRechargesByUserId
     * DESCRIPTION:
     * Retrieves all recharge records for a specific user.
     * ================================================================
     */
    public List<RechargeResponse> getRechargesByUserId(Long userId) {

        logger.info("Fetching recharges for userId: {}", userId);

        return rechargeRepository.findByUserId(userId).stream()
                .map(r -> mapToResponse(r, "Success"))
                .collect(Collectors.toList());
    }

    /**
     * ================================================================
     * METHOD: mapToResponse
     * DESCRIPTION:
     * Maps a Recharge entity to RechargeResponse DTO with message.
     * ================================================================
     */
    private RechargeResponse mapToResponse(Recharge recharge, String message) {

        return new RechargeResponse(
                recharge.getId(),
                recharge.getUserId(),
                recharge.getOperatorId(),
                recharge.getPlanId(),
                recharge.getMobileNumber(),
                recharge.getAmount(),
                recharge.getStatus(),
                recharge.getCreatedAt(),
                message
        );
    }
}