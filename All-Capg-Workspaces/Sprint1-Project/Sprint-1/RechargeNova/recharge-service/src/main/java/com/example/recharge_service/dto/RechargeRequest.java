package com.example.recharge_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RechargeRequest {

    @NotNull
    private Long operatorId;

    @NotNull
    private Long planId;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String paymentMethod; // Pass simple payment method string for simplicity (e.g. UPI)
}
