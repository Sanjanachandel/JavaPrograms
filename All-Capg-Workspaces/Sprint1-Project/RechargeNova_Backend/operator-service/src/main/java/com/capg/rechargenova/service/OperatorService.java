package com.capg.rechargenova.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.rechargenova.dto.OperatorDto;
import com.capg.rechargenova.dto.PlanDto;
import com.capg.rechargenova.entity.Operator;
import com.capg.rechargenova.entity.Plan;
import com.capg.rechargenova.repository.OperatorRepository;
import com.capg.rechargenova.repository.PlanRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: OperatorService
 * DESCRIPTION:
 * This service handles all business logic for managing operators
 * and plans. It provides CRUD operations for operators and plans
 * and handles the conversion between entities and DTOs.
 * ================================================================
 */
@Service
public class OperatorService {

    private static final Logger logger = LogManager.getLogger(OperatorService.class);

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private PlanRepository planRepository;

    /* ================================================================
     * METHOD: getAllOperators
     * DESCRIPTION:
     * Fetches all operators from the database and converts them into
     * OperatorDto objects.
     * ================================================================ */
    public List<OperatorDto> getAllOperators() {
        logger.info("Fetching all operators");

        List<OperatorDto> operators = operatorRepository.findAll()
                .stream()
                .map(this::mapToOperatorDto)
                .collect(Collectors.toList());

        logger.debug("Total operators fetched: {}", operators.size());
        return operators;
    }

    /* ================================================================
     * METHOD: getOperatorById
     * DESCRIPTION:
     * Fetches an operator by its ID. Throws an exception if not found.
     * ================================================================ */
    public OperatorDto getOperatorById(Long id) {
        logger.info("Fetching operator with ID: {}", id);

        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Operator not found with ID: {}", id);
                    return new RuntimeException("Operator not found");
                });

        return mapToOperatorDto(operator);
    }

    /* ================================================================
     * METHOD: getPlanById
     * DESCRIPTION:
     * Fetches a plan by its ID. Throws an exception if not found.
     * ================================================================ */
    public PlanDto getPlanById(Long id) {
        logger.info("Fetching plan with ID: {}", id);

        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Plan not found with ID: {}", id);
                    return new RuntimeException("Plan not found");
                });

        return mapToPlanDto(plan);
    }

    /* ================================================================
     * METHOD: mapToOperatorDto
     * DESCRIPTION:
     * Converts an Operator entity into an OperatorDto, including plans.
     * ================================================================ */
    private OperatorDto mapToOperatorDto(Operator operator) {
        OperatorDto dto = new OperatorDto();

        dto.setId(operator.getId());
        dto.setName(operator.getName());
        dto.setCircle(operator.getCircle());

        if (operator.getPlans() != null) {
            List<PlanDto> planDtos = operator.getPlans()
                    .stream()
                    .map(this::mapToPlanDto)
                    .collect(Collectors.toList());

            dto.setPlans(planDtos);
        }

        return dto;
    }

    /* ================================================================
     * METHOD: mapToPlanDto
     * DESCRIPTION:
     * Converts a Plan entity into a PlanDto object.
     * ================================================================ */
    private PlanDto mapToPlanDto(Plan plan) {
        PlanDto dto = new PlanDto();

        dto.setId(plan.getId());
        dto.setOperatorId(
                plan.getOperator() != null ? plan.getOperator().getId() : null
        );
        dto.setAmount(plan.getAmount());
        dto.setValidity(plan.getValidity());
        dto.setDescription(plan.getDescription());
        dto.setCategory(plan.getCategory());
        dto.setType(plan.getType());

        return dto;
    }

    /* ================================================================
     * METHOD: createOperator
     * DESCRIPTION:
     * Creates a new operator and saves it to the database.
     * ================================================================ */
    public OperatorDto createOperator(OperatorDto operatorDto) {
        logger.info("Creating operator: {}", operatorDto.getName());

        Operator operator = new Operator();
        operator.setName(operatorDto.getName());
        operator.setCircle(operatorDto.getCircle());

        Operator savedOperator = operatorRepository.save(operator);

        logger.debug("Operator created with ID: {}", savedOperator.getId());

        return mapToOperatorDto(savedOperator);
    }

    /* ================================================================
     * METHOD: updateOperator
     * DESCRIPTION:
     * Updates an existing operator with new details.
     * ================================================================ */
    public OperatorDto updateOperator(Long id, OperatorDto operatorDto) {
        logger.info("Updating operator with ID: {}", id);

        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Operator not found for update: {}", id);
                    return new RuntimeException("Operator not found");
                });

        operator.setName(operatorDto.getName());
        operator.setCircle(operatorDto.getCircle());

        Operator updatedOperator = operatorRepository.save(operator);

        logger.debug("Operator updated successfully: {}", id);

        return mapToOperatorDto(updatedOperator);
    }

    /* ================================================================
     * METHOD: deleteOperator
     * DESCRIPTION:
     * Deletes an operator by ID from the database.
     * ================================================================ */
    public void deleteOperator(Long id) {
        logger.warn("Deleting operator with ID: {}", id);

        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Operator not found for deletion: {}", id);
                    return new RuntimeException("Operator not found");
                });

        operatorRepository.delete(operator);

        logger.info("Operator deleted successfully: {}", id);
    }

    /* ================================================================
     * METHOD: createPlan
     * DESCRIPTION:
     * Creates a new plan for a given operator.
     * ================================================================ */
    public PlanDto createPlan(Long operatorId, PlanDto planDto) {
        logger.info("Creating plan for operator ID: {}", operatorId);

        Operator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> {
                    logger.error("Operator not found for plan creation: {}", operatorId);
                    return new RuntimeException("Operator not found");
                });

        Plan plan = new Plan();
        plan.setAmount(planDto.getAmount());
        plan.setValidity(planDto.getValidity());
        plan.setDescription(planDto.getDescription());
        plan.setCategory(planDto.getCategory());
        plan.setType(planDto.getType());
        plan.setOperator(operator);

        Plan savedPlan = planRepository.save(plan);

        logger.debug("Plan created with ID: {}", savedPlan.getId());

        return mapToPlanDto(savedPlan);
    }

    /* ================================================================
     * METHOD: updatePlan
     * DESCRIPTION:
     * Updates an existing plan with new details.
     * ================================================================ */
    public PlanDto updatePlan(Long planId, PlanDto planDto) {
        logger.info("Updating plan with ID: {}", planId);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> {
                    logger.error("Plan not found for update: {}", planId);
                    return new RuntimeException("Plan not found");
                });

        plan.setAmount(planDto.getAmount());
        plan.setValidity(planDto.getValidity());
        plan.setDescription(planDto.getDescription());
        plan.setCategory(planDto.getCategory());
        plan.setType(planDto.getType());

        Plan updatedPlan = planRepository.save(plan);

        logger.debug("Plan updated successfully: {}", planId);

        return mapToPlanDto(updatedPlan);
    }

    /* ================================================================
     * METHOD: deletePlan
     * DESCRIPTION:
     * Deletes a plan by ID from the database.
     * ================================================================ */
    public void deletePlan(Long planId) {
        logger.warn("Deleting plan with ID: {}", planId);

        if (!planRepository.existsById(planId)) {
            logger.error("Plan not found for deletion: {}", planId);
            throw new RuntimeException("Plan not found");
        }

        planRepository.deletePlanById(planId);

        logger.info("Plan deleted successfully: {}", planId);
    }

    public long countOperators() {
        return operatorRepository.count();
    }

    public long countPlans() {
        return planRepository.count();
    }

    public Page<PlanDto> getPlansByOperator(Long operatorId, int page, int size) {
        logger.info("Fetching plans for operator {} (Page: {}, Size: {})", operatorId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("amount").ascending());
        return planRepository.findByOperatorId(operatorId, pageable)
                .map(this::mapToPlanDto);
    }
}