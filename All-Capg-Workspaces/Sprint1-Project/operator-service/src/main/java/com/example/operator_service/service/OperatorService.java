package com.example.operator_service.service;

import com.example.operator_service.dto.OperatorDto;
import com.example.operator_service.dto.PlanDto;
import com.example.operator_service.entity.Operator;
import com.example.operator_service.entity.Plan;
import com.example.operator_service.repository.OperatorRepository;
import com.example.operator_service.repository.PlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private PlanRepository planRepository;

    // Get all operators
    public List<OperatorDto> getAllOperators() {
        return operatorRepository.findAll()
                .stream()
                .map(this::mapToOperatorDto)
                .collect(Collectors.toList());
    }

    // Get operator by ID
    public OperatorDto getOperatorById(Long id) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        return mapToOperatorDto(operator);
    }

    // Get plan by ID
    public PlanDto getPlanById(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        return mapToPlanDto(plan);
    }

    // Convert Operator → DTO
    private OperatorDto mapToOperatorDto(Operator operator) {

        OperatorDto dto = new OperatorDto();

        dto.setId(operator.getId());
        dto.setName(operator.getName());
        dto.setType(operator.getType());
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

    // Convert Plan → DTO
    private PlanDto mapToPlanDto(Plan plan) {

        PlanDto dto = new PlanDto();

        dto.setId(plan.getId());
        dto.setOperatorId(
                plan.getOperator() != null ? plan.getOperator().getId() : null
        );
        dto.setAmount(plan.getAmount());
        dto.setValidity(plan.getValidity());
        dto.setDescription(plan.getDescription());

        return dto;
    }

    // --- ADMINISTRATIVE OPERATIONS FOR OPERATOR ---

    public OperatorDto createOperator(OperatorDto operatorDto) {
        Operator operator = new Operator();
        operator.setName(operatorDto.getName());
        operator.setType(operatorDto.getType());
        operator.setCircle(operatorDto.getCircle());
        
        Operator savedOperator = operatorRepository.save(operator);
        return mapToOperatorDto(savedOperator);
    }

    public OperatorDto updateOperator(Long id, OperatorDto operatorDto) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        operator.setName(operatorDto.getName());
        operator.setType(operatorDto.getType());
        operator.setCircle(operatorDto.getCircle());

        Operator updatedOperator = operatorRepository.save(operator);
        return mapToOperatorDto(updatedOperator);
    }

    public void deleteOperator(Long id) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operator not found"));
        operatorRepository.delete(operator);
    }

    // --- ADMINISTRATIVE OPERATIONS FOR PLAN ---

    public PlanDto createPlan(Long operatorId, PlanDto planDto) {
        Operator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        Plan plan = new Plan();
        plan.setAmount(planDto.getAmount());
        plan.setValidity(planDto.getValidity());
        plan.setDescription(planDto.getDescription());
        plan.setOperator(operator);

        Plan savedPlan = planRepository.save(plan);
        return mapToPlanDto(savedPlan);
    }

    public PlanDto updatePlan(Long planId, PlanDto planDto) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setAmount(planDto.getAmount());
        plan.setValidity(planDto.getValidity());
        plan.setDescription(planDto.getDescription());

        Plan updatedPlan = planRepository.save(plan);
        return mapToPlanDto(updatedPlan);
    }

    public void deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        planRepository.delete(plan);
    }
}