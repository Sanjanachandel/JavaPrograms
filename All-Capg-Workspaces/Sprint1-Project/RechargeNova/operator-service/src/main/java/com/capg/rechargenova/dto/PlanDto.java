package com.capg.rechargenova.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlanDto {

    private Long id;
    private Long operatorId;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotBlank(message = "Validity is required")
    private String validity;

    @NotBlank(message = "Description is required")
    private String description;

    // Default Constructor
    public PlanDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getValidity() { return validity; }
    public void setValidity(String validity) { this.validity = validity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
