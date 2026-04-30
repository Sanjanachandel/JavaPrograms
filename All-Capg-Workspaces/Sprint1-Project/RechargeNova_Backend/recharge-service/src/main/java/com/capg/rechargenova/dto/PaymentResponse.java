package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long rechargeId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime transactionTime;

    // ✅ No-args constructor
    

    // ✅ All-args constructor (IMPORTANT for Mockito/testing)
    

    // ✅ Getters & Setters
    

    

    

    

    

    

    

    

    

    

    

    

    

    
}