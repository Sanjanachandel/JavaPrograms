package com.example.operator_service.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OperatorDto {
    private Long id;
    private String name;
    private String type;
    private String circle;
    private List<PlanDto> plans;
}
