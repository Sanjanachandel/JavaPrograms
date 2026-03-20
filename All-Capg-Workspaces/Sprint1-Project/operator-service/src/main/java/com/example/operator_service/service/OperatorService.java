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
}