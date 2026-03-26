package com.example.operator_service.service;

import com.example.operator_service.dto.OperatorDto;
import com.example.operator_service.dto.PlanDto;
import com.example.operator_service.entity.Operator;
import com.example.operator_service.entity.Plan;
import com.example.operator_service.repository.OperatorRepository;
import com.example.operator_service.repository.PlanRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
        operator.setType("Prepaid");
        operator.setCircle("India");

        plan = new Plan();
        plan.setId(1L);
        plan.setAmount(199.0);
        plan.setValidity("28 Days");
        plan.setDescription("Unlimited Calls");
        plan.setOperator(operator);
    }

    // ✅ 1. Get All Operators
    @Test
    void testGetAllOperators() {
        when(operatorRepository.findAll()).thenReturn(List.of(operator));

        List<OperatorDto> result = operatorService.getAllOperators();

        assertEquals(1, result.size());
        assertEquals("Jio", result.get(0).getName());
    }

    // ✅ 2. Get Operator By ID (SUCCESS)
    @Test
    void testGetOperatorById_Success() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));

        OperatorDto result = operatorService.getOperatorById(1L);

        assertEquals("Jio", result.getName());
    }

    // ❌ 3. Get Operator By ID (NOT FOUND)
    @Test
    void testGetOperatorById_NotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            operatorService.getOperatorById(1L);
        });
    }

    // ✅ 4. Create Operator
    @Test
    void testCreateOperator() {
        OperatorDto dto = new OperatorDto();
        dto.setName("Airtel");
        dto.setType("Postpaid");
        dto.setCircle("India");

        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);

        OperatorDto result = operatorService.createOperator(dto);

        assertEquals("Jio", result.getName());
        verify(operatorRepository, times(1)).save(any(Operator.class));
    }

    // ✅ 5. Update Operator
    @Test
    void testUpdateOperator() {
        OperatorDto dto = new OperatorDto();
        dto.setName("Updated");
        dto.setType("Prepaid");
        dto.setCircle("India");

        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);

        OperatorDto result = operatorService.updateOperator(1L, dto);

        assertEquals("Updated", result.getName());
    }

    // ❌ 6. Update Operator (NOT FOUND)
    @Test
    void testUpdateOperator_NotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            operatorService.updateOperator(1L, new OperatorDto());
        });
    }

    // ✅ 7. Delete Operator
    @Test
    void testDeleteOperator() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));

        operatorService.deleteOperator(1L);

        verify(operatorRepository, times(1)).delete(operator);
    }

    // ❌ 8. Delete Operator (NOT FOUND)
    @Test
    void testDeleteOperator_NotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            operatorService.deleteOperator(1L);
        });
    }

    // ✅ 9. Get Plan By ID
    @Test
    void testGetPlanById() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        PlanDto result = operatorService.getPlanById(1L);

        assertEquals(199.0, result.getAmount());
    }

    // ❌ 10. Plan Not Found
    @Test
    void testGetPlanById_NotFound() {
        when(planRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            operatorService.getPlanById(1L);
        });
    }

    // ✅ 11. Create Plan
    @Test
    void testCreatePlan() {
        PlanDto dto = new PlanDto();
        dto.setAmount(299.0);
        dto.setValidity("56 Days");
        dto.setDescription("Data Plan");

        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        PlanDto result = operatorService.createPlan(1L, dto);

        assertEquals(199.0, result.getAmount());
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    // ❌ 12. Create Plan (Operator Not Found)
    @Test
    void testCreatePlan_OperatorNotFound() {
        when(operatorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            operatorService.createPlan(1L, new PlanDto());
        });
    }

    // ✅ 13. Delete Plan
    @Test
    void testDeletePlan() {
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        operatorService.deletePlan(1L);

        verify(planRepository, times(1)).delete(plan);
    }
}