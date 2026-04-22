package com.capg.rechargenova.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capg.rechargenova.dto.RechargeRequest;
import com.capg.rechargenova.dto.RechargeResponse;
import com.capg.rechargenova.service.RechargeService;

import java.util.List;

@RestController
@RequestMapping("/recharges")
@Tag(name = "Recharge API", description = "APIs for handling mobile recharge operations")
public class RechargeController {

    private static final Logger logger = LogManager.getLogger(RechargeController.class);

    private final RechargeService rechargeService;

    /**
     * ================================================================
     * METHOD: Constructor
     * DESCRIPTION:
     * Constructor-based dependency injection for RechargeService.
     * ================================================================
     */
    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    /**
     * ================================================================
     * METHOD: initiateRecharge
     * DESCRIPTION:
     * Initiates a new recharge request, processes the recharge, and triggers payment flow.
     * ================================================================
     */
    @Operation(
        summary = "Initiate Recharge",
        description = "Creates a new recharge request and processes payment using user ID from header"
    )
    @ApiResponse(responseCode = "200", description = "Recharge successful")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public ResponseEntity<RechargeResponse> initiateRecharge(

            @Parameter(description = "User ID from header", required = true)
            @RequestHeader(value = "X-User-Id", required = true) Long userId,

            @Valid @RequestBody RechargeRequest request) {

        logger.info("Initiating recharge for userId: {}", userId);

        return ResponseEntity.ok(
                rechargeService.initiateRecharge(userId, request)
        );
    }

    /**
     * ================================================================
     * METHOD: getRechargeById
     * DESCRIPTION:
     * Retrieves recharge details by recharge ID.
     * ================================================================
     */
    @Operation(
        summary = "Get Recharge by ID",
        description = "Fetch recharge details using recharge ID"
    )
    @ApiResponse(responseCode = "200", description = "Recharge found")
    @ApiResponse(responseCode = "404", description = "Recharge not found")
    @GetMapping("/{id}")
    public ResponseEntity<RechargeResponse> getRechargeById(
            @PathVariable @Min(1) Long id) {

        logger.info("Fetching recharge by id: {}", id);

        return ResponseEntity.ok(
                rechargeService.getRechargeById(id)
        );
    }

    /**
     * ================================================================
     * METHOD: getRechargesByUserId
     * DESCRIPTION:
     * Retrieves all recharge records for a specific user.
     * ================================================================
     */
    @Operation(
        summary = "Get Recharges by User",
        description = "Fetch all recharge records for a specific user"
    )
    @ApiResponse(responseCode = "200", description = "Recharges fetched successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RechargeResponse>> getRechargesByUserId(
            @PathVariable @Min(1) Long userId) {

        logger.info("Fetching recharges for userId: {}", userId);

        return ResponseEntity.ok(
                rechargeService.getRechargesByUserId(userId)
        );
    }
}