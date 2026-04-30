package com.capg.rechargenova.controller;

import com.capg.rechargenova.dto.OperatorDto;
import com.capg.rechargenova.dto.PlanDto;
import com.capg.rechargenova.service.OperatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OperatorController.class)
class OperatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperatorService operatorService;

    @Autowired
    private ObjectMapper objectMapper;

    private OperatorDto operatorDto;
    private PlanDto planDto;

    @BeforeEach
    void setUp() {
        operatorDto = new OperatorDto();
        operatorDto.setId(1L);
        operatorDto.setName("Jio");
        operatorDto.setCircle("India");

        planDto = new PlanDto();
        planDto.setId(1L);
        planDto.setAmount(199.0);
        planDto.setValidity(28);
        planDto.setDescription("Unlimited Calls");
        planDto.setType("Prepaid");
        planDto.setOperatorId(1L);
    }

    @Test
    void testGetAllOperators() throws Exception {
        when(operatorService.getAllOperators()).thenReturn(List.of(operatorDto));
        mockMvc.perform(get("/operators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Jio"));
    }

    @Test
    void testGetOperatorById() throws Exception {
        when(operatorService.getOperatorById(1L)).thenReturn(operatorDto);
        mockMvc.perform(get("/operators/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jio"));
    }

    @Test
    void testCreateOperator_Admin() throws Exception {
        when(operatorService.createOperator(any())).thenReturn(operatorDto);
        mockMvc.perform(post("/operators")
                .header("X-User-Role", "ROLE_ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operatorDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateOperator_Unauthorized() throws Exception {
        mockMvc.perform(post("/operators")
                .header("X-User-Role", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operatorDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateOperator_Admin() throws Exception {
        when(operatorService.updateOperator(eq(1L), any())).thenReturn(operatorDto);
        mockMvc.perform(put("/operators/1")
                .header("X-User-Role", "ROLE_ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operatorDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOperator_Unauthorized() throws Exception {
        mockMvc.perform(put("/operators/1")
                .header("X-User-Role", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operatorDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteOperator_Admin() throws Exception {
        mockMvc.perform(delete("/operators/1")
                .header("X-User-Role", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("Operator deleted successfully"));
    }

    @Test
    void testDeleteOperator_Unauthorized() throws Exception {
        mockMvc.perform(delete("/operators/1")
                .header("X-User-Role", "ROLE_USER"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPlanById() throws Exception {
        when(operatorService.getPlanById(1L)).thenReturn(planDto);
        mockMvc.perform(get("/operators/plans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(199.0));
    }

    @Test
    void testCreatePlan_Admin() throws Exception {
        when(operatorService.createPlan(eq(1L), any())).thenReturn(planDto);
        mockMvc.perform(post("/operators/1/plans")
                .header("X-User-Role", "ROLE_ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreatePlan_Unauthorized() throws Exception {
        mockMvc.perform(post("/operators/1/plans")
                .header("X-User-Role", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePlan_Admin() throws Exception {
        when(operatorService.updatePlan(eq(1L), any())).thenReturn(planDto);
        mockMvc.perform(put("/operators/plans/1")
                .header("X-User-Role", "ROLE_ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePlan_Unauthorized() throws Exception {
        mockMvc.perform(put("/operators/plans/1")
                .header("X-User-Role", "ROLE_USER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePlan_Admin() throws Exception {
        mockMvc.perform(delete("/operators/plans/1")
                .header("X-User-Role", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("Plan deleted successfully"));
    }

    @Test
    void testDeletePlan_Unauthorized() throws Exception {
        mockMvc.perform(delete("/operators/plans/1")
                .header("X-User-Role", "ROLE_USER"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCountOperators() throws Exception {
        when(operatorService.countOperators()).thenReturn(5L);
        mockMvc.perform(get("/operators/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testCountPlans() throws Exception {
        when(operatorService.countPlans()).thenReturn(10L);
        mockMvc.perform(get("/operators/plans/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void testGetPlansByOperatorPaginated() throws Exception {
        Page<PlanDto> page = new PageImpl<>(List.of(planDto));
        when(operatorService.getPlansByOperator(eq(1L), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/operators/1/plans/paginated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].amount").value(199.0));
    }
}
