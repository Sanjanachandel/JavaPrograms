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

    public List<OperatorDto> getAllOperators() {
        return operatorRepository.findAll().stream().map(this::mapToOperatorDto).collect(Collectors.toList());
    }

    public OperatorDto getOperatorById(Long id) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operator not found"));
        return mapToOperatorDto(operator);
    }

    public PlanDto getPlanById(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        return mapToPlanDto(plan);
    }

    // Maps Operator without its plans.
    private OperatorDto mapToOperatorDto(Operator operator) {
        return OperatorDto.builder()
                .id(operator.getId())
                .name(operator.getName())
                .type(operator.getType())
                .circle(operator.getCircle())
                .plans(operator.getPlans() != null ? 
                        operator.getPlans().stream().map(this::mapToPlanDto).collect(Collectors.toList())
                        : null)
                .build();
    }

    private PlanDto mapToPlanDto(Plan plan) {
        return PlanDto.builder()
                .id(plan.getId())
                .operatorId(plan.getOperator() != null ? plan.getOperator().getId() : null)
                .amount(plan.getAmount())
                .validity(plan.getValidity())
                .description(plan.getDescription())
                .build();
    }
}
