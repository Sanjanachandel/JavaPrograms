package com.example.recharge_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PlanDto {
    private Long id;
    private Long operatorId;
    private BigDecimal amount;
    private Integer validity;
    private String description;
}
