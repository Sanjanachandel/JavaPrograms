package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RechargeResponse {

    private Long id;
    private Long userId;
    private Long operatorId;
    private Long planId;
    private String mobileNumber;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String rechargeType;

    // ✅ Fix for JSON serialization (VERY IMPORTANT)
    private OffsetDateTime createdAt;

    private String message;

    // ✅ No-args constructor
    

    // ✅ All-args constructor (important for testing)
    

    // ✅ GETTERS & SETTERS

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}