package com.example.recharge_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class OperatorDto {
    private Long id;
    private String name;
    private String type;
    private String circle;
    private List<PlanDto> plans;
}
