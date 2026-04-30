package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {

    private Long id;
    private Long operatorId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotNull(message = "Validity is required")
    @Positive(message = "Validity must be greater than zero")
    private Integer validity;

    @NotBlank(message = "Description is required")
    private String description;

    private String category;
    private String type;

    // Default Constructor
    

    // Getters and Setters
    
    

    
    

    
    

    
    

    
    

    
    
}
