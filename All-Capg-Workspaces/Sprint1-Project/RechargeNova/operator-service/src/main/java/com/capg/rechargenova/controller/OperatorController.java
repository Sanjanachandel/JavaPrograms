package com.capg.rechargenova.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.capg.rechargenova.dto.OperatorDto;
import com.capg.rechargenova.dto.PlanDto;
import com.capg.rechargenova.service.OperatorService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: OperatorController
 * DESCRIPTION:
 * Handles all API requests for operators and plans including
 * CRUD operations and fetching details by ID. Uses OperatorService
 * for business logic.
 * ================================================================
 */
@RestController
@RequestMapping("/operators")
@Tag(name = "Operator API", description = "Manage Operators and Plans")
public class OperatorController {

    private static final Logger logger = LogManager.getLogger(OperatorController.class);

    @Autowired
    private OperatorService operatorService;

    /**
     * Retrieves all operators available in the system.
     * @return list of OperatorDto objects
     */
    @Operation(summary = "Get all operators")
    @GetMapping
    public List<OperatorDto> getAllOperators() {
        logger.info("API Call: Get all operators");
        return operatorService.getAllOperators();
    }

    /**
     * Retrieves a specific operator by its ID.
     * @param id the operator ID; must not be null
     * @return OperatorDto object
     * @throws RuntimeException if operator is not found
     */
    @Operation(summary = "Get operator by ID")
    @GetMapping("/{id}")
    public OperatorDto getOperatorById(@PathVariable Long id) {
        logger.info("API Call: Get operator by ID {}", id);
        return operatorService.getOperatorById(id);
    }

    /**
     * Creates a new operator.
     * @param operatorDto the operator details to be created
     * @return created OperatorDto object
     */
    @Operation(summary = "Create operator")
    @PostMapping
    public OperatorDto createOperator(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody OperatorDto operatorDto) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Unauthorized: Only Admin can perform this action");
        }
        logger.info("CREATING OPERATOR: {}", operatorDto.getName());
        return operatorService.createOperator(operatorDto);
    }

    /**
     * Updates an existing operator.
     * @param id the operator ID; must not be null
     * @param operatorDto updated operator details
     * @return updated OperatorDto object
     * @throws RuntimeException if operator is not found
     */
    @Operation(summary = "Update operator")
    @PutMapping("/{id}")
    public OperatorDto updateOperator(@PathVariable Long id,
                                      @RequestHeader(value = "X-User-Role", required = false) String role,
                                      @Valid @RequestBody OperatorDto operatorDto) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Unauthorized: Only Admin can perform this action");
        }
        return operatorService.updateOperator(id, operatorDto);
    }

    /**
     * Deletes an operator by ID.
     * @param id the operator ID; must not be null
     * @return success message
     */
    @Operation(summary = "Delete operator")
    @DeleteMapping("/{id}")
    public String deleteOperator(@PathVariable Long id,
                                 @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Unauthorized: Only Admin can perform this action");
        }
        operatorService.deleteOperator(id);
        return "Operator deleted successfully";
    }

    /**
     * Retrieves a specific plan by its ID.
     * @param id the plan ID; must not be null
     * @return PlanDto object
     * @throws RuntimeException if plan is not found
     */
    @Operation(summary = "Get plan by ID")
    @GetMapping("/plans/{id}")
    public PlanDto getPlanById(@PathVariable Long id) {
        logger.info("API Call: Get plan {}", id);
        return operatorService.getPlanById(id);
    }

    /**
     * Creates a new plan for a specific operator.
     * @param operatorId the operator ID; must not be null
     * @param planDto the plan details to be created
     * @return created PlanDto object
     */
    @Operation(summary = "Create plan")
    @PostMapping("/{operatorId}/plans")
    public PlanDto createPlan(@PathVariable Long operatorId,
                              @RequestHeader(value = "X-User-Role", required = false) String role,
                              @Valid @RequestBody PlanDto planDto) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Unauthorized: Only Admin can perform this action");
        }
        logger.info("API Call: Create plan for operator {}", operatorId);
        return operatorService.createPlan(operatorId, planDto);
    }

    /**
     * Updates an exist
     * @param planId the plan ID; must not be null
     * @param planDto updated plan details
     * @return updated PlanDto object
     * @throws RuntimeException if plan is not found
     */
    @Operation(summary = "Update plan")
    @PutMapping("/plans/{planId}")
    public PlanDto updatePlan(@PathVariable Long planId,
                              @RequestHeader(value = "X-User-Role", required = false) String role,
                              @Valid @RequestBody PlanDto planDto) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Unauthorized: Only Admin can perform this action");
        }
        logger.info("API Call: Update plan {}", planId);
        return operatorService.updatePlan(planId, planDto);
    }

    /**
     * Deletes a plan by its ID.
     * @param planId the plan ID; must not be null
     * @return success message
     */
    @Operation(summary = "Delete plan")
    @DeleteMapping("/plans/{planId}")
    public String deletePlan(@PathVariable Long planId,
                             @RequestHeader(value = "X-User-Role", required = false) String role) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Unauthorized: Only Admin can perform this action");
        }
        logger.warn("API Call: Delete plan {}", planId);
        operatorService.deletePlan(planId);
        return "Plan deleted successfully";
    }
}