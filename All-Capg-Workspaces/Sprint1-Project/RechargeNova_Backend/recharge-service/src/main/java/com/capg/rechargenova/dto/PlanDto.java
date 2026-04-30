package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {

    private Long id;

    @NotNull(message = "Operator ID is required")
    private Long operatorId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Validity is required")
    private String validity;

    @NotBlank(message = "Description is required")
    private String description;

    private String type;

    // ✅ Default constructor
    

    // ✅ Parameterized constructor (important for Mockito/testing)
    

    // ✅ GETTERS

    

    

    

    

    

    // ✅ SETTERS

    

    

    

    

    
}