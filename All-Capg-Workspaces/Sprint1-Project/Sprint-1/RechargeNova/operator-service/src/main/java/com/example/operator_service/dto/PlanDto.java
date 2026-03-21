package com.example.operator_service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PlanDto {
    private Long id;
    private Long operatorId;
    private BigDecimal amount;
    private Integer validity;
    private String description;
}
