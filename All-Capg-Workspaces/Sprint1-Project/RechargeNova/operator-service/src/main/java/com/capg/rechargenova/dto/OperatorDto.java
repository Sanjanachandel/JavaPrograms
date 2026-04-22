
package com.capg.rechargenova.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class OperatorDto {

    private Long id;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets")
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Circle is required")
    private String circle;
    private List<PlanDto> plans;

    // Default Constructor
    public OperatorDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCircle() { return circle; }
    public void setCircle(String circle) { this.circle = circle; }

    public List<PlanDto> getPlans() { return plans; }
    public void setPlans(List<PlanDto> plans) { this.plans = plans; }
}
