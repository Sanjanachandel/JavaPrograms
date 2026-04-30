package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.*;
import com.capg.rechargenova.exception.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testDTOs() {
        // OperatorDto
        OperatorDto op = new OperatorDto();
        op.setId(1L); op.setName("name"); op.setCircle("Circle1");
        op.setPlans(Collections.emptyList());
        assertEquals(1L, op.getId());
        assertEquals("name", op.getName());

        assertEquals("Circle1", op.getCircle());
        assertEquals(0, op.getPlans().size());

        // PlanDto
        PlanDto plan = new PlanDto();
        plan.setId(1L); plan.setOperatorId(1L); plan.setAmount(10.0); plan.setValidity(30); plan.setDescription("desc"); plan.setType("Prepaid");
        assertEquals(1L, plan.getId());
        assertEquals(10.0, plan.getAmount());
        assertEquals(30, plan.getValidity());
        assertEquals("desc", plan.getDescription());
        assertEquals("Prepaid", plan.getType());
        assertEquals(1L, plan.getOperatorId());
    }

    @Test
    void testEntities() {
        // Operator
        Operator o = new Operator();
        o.setId(1L); o.setName("name"); o.setCircle("Circle1");
        o.setPlans(Collections.emptyList());
        assertEquals(1L, o.getId());
        assertEquals("name", o.getName());

        assertEquals("Circle1", o.getCircle());
        assertEquals(0, o.getPlans().size());

        // Plan
        Plan p = new Plan();
        p.setId(1L); p.setAmount(100.0); p.setValidity(30); p.setDescription("desc"); p.setType("Prepaid"); p.setOperator(new Operator());
        assertEquals(1L, p.getId());
        assertEquals(100.0, p.getAmount());
        assertEquals(30, p.getValidity());
        assertEquals("desc", p.getDescription());
        assertEquals("Prepaid", p.getType());
        assertNotNull(p.getOperator());
    }

    @Test
    void testResourceNotFoundException() {
        assertNotNull(new ResourceNotFoundException("msg"));
    }

    @Test
    void testMain() {
        OperatorServiceApplication app = new OperatorServiceApplication();
        assertNotNull(app);
    }
}
