package com.example.operator_service.controller;

import com.example.operator_service.dto.OperatorDto;
import com.example.operator_service.dto.PlanDto;
import com.example.operator_service.service.OperatorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operators")
@Tag(name = "Operator API", description = "Manage Operators and Plans")
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    // ✅ Get all operators
    @GetMapping
    @Operation(summary = "Get all operators")
    public List<OperatorDto> getAllOperators() {
        return operatorService.getAllOperators();
    }

    // ✅ Get operator by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get operator by ID")
    public OperatorDto getOperatorById(@PathVariable Long id) {
        return operatorService.getOperatorById(id);
    }

    // ✅ Create operator
    @PostMapping
    @Operation(summary = "Create new operator")
    public OperatorDto createOperator(@RequestBody OperatorDto operatorDto) {
        return operatorService.createOperator(operatorDto);
    }

    // ✅ Update operator
    @PutMapping("/{id}")
    @Operation(summary = "Update operator")
    public OperatorDto updateOperator(@PathVariable Long id,
                                      @RequestBody OperatorDto operatorDto) {
        return operatorService.updateOperator(id, operatorDto);
    }

    // ✅ Delete operator
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete operator")
    public String deleteOperator(@PathVariable Long id) {
        operatorService.deleteOperator(id);
        return "Operator deleted successfully";
    }

    // ================= PLAN APIs =================

    // ✅ Get plan by ID
    @GetMapping("/plans/{id}")
    @Operation(summary = "Get plan by ID")
    public PlanDto getPlanById(@PathVariable Long id) {
        return operatorService.getPlanById(id);
    }

    // ✅ Create plan
    @PostMapping("/{operatorId}/plans")
    @Operation(summary = "Create plan for operator")
    public PlanDto createPlan(@PathVariable Long operatorId,
                             @RequestBody PlanDto planDto) {
        return operatorService.createPlan(operatorId, planDto);
    }

    // ✅ Update plan
    @PutMapping("/plans/{planId}")
    @Operation(summary = "Update plan")
    public PlanDto updatePlan(@PathVariable Long planId,
                             @RequestBody PlanDto planDto) {
        return operatorService.updatePlan(planId, planDto);
    }

    // ✅ Delete plan
    @DeleteMapping("/plans/{planId}")
    @Operation(summary = "Delete plan")
    public String deletePlan(@PathVariable Long planId) {
        operatorService.deletePlan(planId);
        return "Plan deleted successfully";
    }
}