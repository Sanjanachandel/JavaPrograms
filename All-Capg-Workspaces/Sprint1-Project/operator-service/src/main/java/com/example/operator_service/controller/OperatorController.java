package com.example.operator_service.controller;


import com.example.operator_service.dto.OperatorDto;
import com.example.operator_service.dto.PlanDto;
import com.example.operator_service.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    @GetMapping("/operators")
    public ResponseEntity<List<OperatorDto>> getAllOperators() {
        return ResponseEntity.ok(operatorService.getAllOperators());
    }

    @GetMapping("/operators/{id}")
    public ResponseEntity<OperatorDto> getOperatorById(@PathVariable Long id) {
        return ResponseEntity.ok(operatorService.getOperatorById(id));
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<PlanDto> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(operatorService.getPlanById(id));
    }

    // --- ADMINISTRATIVE ENDPOINTS ---

    @PostMapping("/operators")
    public ResponseEntity<OperatorDto> createOperator(@RequestBody OperatorDto operatorDto) {
        return ResponseEntity.ok(operatorService.createOperator(operatorDto));
    }

    @PutMapping("/operators/{id}")
    public ResponseEntity<OperatorDto> updateOperator(@PathVariable Long id, @RequestBody OperatorDto operatorDto) {
        return ResponseEntity.ok(operatorService.updateOperator(id, operatorDto));
    }

    @DeleteMapping("/operators/{id}")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        operatorService.deleteOperator(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/operators/{operatorId}/plans")
    public ResponseEntity<PlanDto> createPlan(@PathVariable Long operatorId, @RequestBody PlanDto planDto) {
        return ResponseEntity.ok(operatorService.createPlan(operatorId, planDto));
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<PlanDto> updatePlan(@PathVariable Long id, @RequestBody PlanDto planDto) {
        return ResponseEntity.ok(operatorService.updatePlan(id, planDto));
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        operatorService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
