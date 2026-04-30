package com.capg.rechargenova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capg.rechargenova.dto.OperatorDto;
import com.capg.rechargenova.dto.PlanDto;
import com.capg.rechargenova.entity.Operator;
import com.capg.rechargenova.entity.Plan;
import com.capg.rechargenova.repository.OperatorRepository;
import com.capg.rechargenova.repository.PlanRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperatorServiceTest {

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private OperatorService operatorService;

    private Operator operator;
    private Plan plan;

    @BeforeEach
    void setUp() {
        operator = new Operator();
        operator.setId(1L);
        operator.setName("Jio");
        operator.setCircle("India");

        plan = new Plan();
        plan.setId(1L);
        plan.setAmount(199.0);
        plan.setValidity(28);
        plan.setDescription("Unlimited Calls");
        plan.setType("Prepaid");
        plan.setOperator(operator);
    }

    @Test
    void testGetAllOperators() {
        when(operatorRepository.findAll()).thenReturn(List.of(operator));
        List<OperatorDto> result = operatorService.getAllOperators();
        assertEquals(1, result.size());
        assertEquals("Jio", result.get(0).getName());
    }

    @Test
    void testGetOperatorById_Success() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        OperatorDto result = operatorService.getOperatorById(1L);
        assertEquals("Jio", result.getName());
    }

    @Test
    void testGetOperatorById_NotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> operatorService.getOperatorById(1L));
    }

    @Test
    void testCreateOperator() {
        OperatorDto dto = new OperatorDto();
        dto.setName("Airtel");
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);
        OperatorDto result = operatorService.createOperator(dto);
        assertNotNull(result);
        verify(operatorRepository, times(1)).save(any(Operator.class));
    }

    @Test
    void testUpdateOperator() {
        OperatorDto dto = new OperatorDto();
        dto.setName("Updated");
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);
        OperatorDto result = operatorService.updateOperator(1L, dto);
        assertEquals("Updated", result.getName());
    }

    @Test
    void testUpdateOperator_NotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> operatorService.updateOperator(1L, new OperatorDto()));
    }

    @Test
    void testMapToOperatorDto_WithPlans() {
        Plan p = new Plan();
        p.setId(2L);
        p.setAmount(100.0);
        operator.setPlans(List.of(p));
        
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        
        OperatorDto result = operatorService.getOperatorById(1L);
        assertNotNull(result.getPlans());
        assertEquals(1, result.getPlans().size());
        assertEquals(100.0, result.getPlans().get(0).getAmount());
    }

    @Test
    void testDeleteOperator() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        operatorService.deleteOperator(1L);
        verify(operatorRepository, times(1)).delete(operator);
    }

    @Test
    void testDeleteOperator_NotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> operatorService.deleteOperator(1L));
    }

    @Test
    void testGetPlanById() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        PlanDto result = operatorService.getPlanById(1L);
        assertEquals(199.0, result.getAmount());
    }

    @Test
    void testGetPlanById_NotFound() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> operatorService.getPlanById(1L));
    }

    @Test
    void testCreatePlan() {
        PlanDto dto = new PlanDto();
        dto.setAmount(299.0);
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);
        PlanDto result = operatorService.createPlan(1L, dto);
        assertNotNull(result);
    }

    @Test
    void testCreatePlan_OperatorNotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> operatorService.createPlan(1L, new PlanDto()));
    }

    @Test
    void testUpdatePlan_Success() {
        PlanDto dto = new PlanDto();
        dto.setAmount(150.0);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);
        PlanDto result = operatorService.updatePlan(1L, dto);
        assertEquals(150.0, result.getAmount());
    }

    @Test
    void testUpdatePlan_NotFound() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> operatorService.updatePlan(1L, new PlanDto()));
    }

    @Test
    void testDeletePlan() {
        when(planRepository.existsById(1L)).thenReturn(true);
        operatorService.deletePlan(1L);
        verify(planRepository, times(1)).deletePlanById(1L);
    }

    @Test
    void testDeletePlan_NotFound() {
        when(planRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> operatorService.deletePlan(1L));
    }

    @Test
    void testMapToOperatorDto_NoPlans() {
        operator.setPlans(null);
        when(operatorRepository.findAll()).thenReturn(List.of(operator));
        List<OperatorDto> result = operatorService.getAllOperators();
        assertNull(result.get(0).getPlans());
    }

    @Test
    void testMapToPlanDto_NoOperator() {
        plan.setOperator(null);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        PlanDto result = operatorService.getPlanById(1L);
        assertNull(result.getOperatorId());
    }

    @Test
    void testCountOperators() {
        when(operatorRepository.count()).thenReturn(5L);
        assertEquals(5L, operatorService.countOperators());
    }

    @Test
    void testCountPlans() {
        when(planRepository.count()).thenReturn(10L);
        assertEquals(10L, operatorService.countPlans());
    }
}