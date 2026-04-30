package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private Long id;
    private Long operatorId;
    private Double amount;
    private String validity;
    private String description;
    private String type;

    

    

    
    

    
    

    
    

    
    

    
    
}
