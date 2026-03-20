package com.example.operator_service.controller;


import com.example.operator_service.dto.OperatorDto;
import com.example.operator_service.dto.PlanDto;
import com.example.operator_service.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
