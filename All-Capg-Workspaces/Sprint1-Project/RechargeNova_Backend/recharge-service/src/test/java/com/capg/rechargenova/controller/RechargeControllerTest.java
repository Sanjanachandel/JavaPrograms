package com.capg.rechargenova.controller;

import com.capg.rechargenova.dto.RechargeRequest;
import com.capg.rechargenova.dto.RechargeResponse;
import com.capg.rechargenova.service.RechargeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RechargeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RechargeService rechargeService;

    @InjectMocks
    private RechargeController rechargeController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rechargeController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private RechargeResponse buildResponse(Long id, String status) {
        return new RechargeResponse(
                id, 501L, 101L, 201L,
                "9999999999", BigDecimal.valueOf(299),
                status, "UPI", "Prepaid", OffsetDateTime.now(), "Recharge successful"
        );
    }

    @Test
    void testInitiateRecharge_Returns200() throws Exception {

        RechargeRequest request = new RechargeRequest();
        request.setOperatorId(101L);
        request.setPlanId(201L);
        request.setMobileNumber("9876543210");
        request.setPaymentMethod("UPI");
        request.setRechargeType("Prepaid");

        when(rechargeService.initiateRecharge(eq(501L), any()))
                .thenReturn(buildResponse(1L, "SUCCESS"));

        mockMvc.perform(post("/recharges")
                        .header("X-User-Id", 501L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.userId").value(501));
    }

    @Test
    void testGetRechargeById_Returns200() throws Exception {

        when(rechargeService.getRechargeById(1L))
                .thenReturn(buildResponse(1L, "SUCCESS"));

        mockMvc.perform(get("/recharges/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void testGetRechargesByUserId_Returns200() throws Exception {
        RechargeResponse r1 = buildResponse(1L, "SUCCESS");
        RechargeResponse r2 = buildResponse(2L, "FAILED");
        Page<RechargeResponse> page = new PageImpl<>(new java.util.ArrayList<>(List.of(r1, r2)), PageRequest.of(0, 10), 2);

        when(rechargeService.getRechargesByUserId(eq(501L), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/recharges/user/501"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$.content[1].status").value("FAILED"));
    }

    @Test
    void testGetAllRecharges_Returns200() throws Exception {
        RechargeResponse r1 = buildResponse(1L, "SUCCESS");
        Page<RechargeResponse> page = new PageImpl<>(new java.util.ArrayList<>(List.of(r1)), PageRequest.of(0, 10), 1);

        when(rechargeService.getAllRecharges(anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/recharges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testCountRecharges_Returns200() throws Exception {
        when(rechargeService.countRecharges()).thenReturn(10L);

        mockMvc.perform(get("/recharges/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }
}
