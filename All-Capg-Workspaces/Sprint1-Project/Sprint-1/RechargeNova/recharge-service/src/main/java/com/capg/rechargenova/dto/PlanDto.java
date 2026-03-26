package com.capg.rechargenova.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

public class PlanDto {

    private Long id;

    @NotNull(message = "Operator ID cannot be null")
    private Long operatorId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Validity cannot be empty")
    private String validity;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    // ✅ Default constructor
    public PlanDto() {}

    // ✅ Parameterized constructor (important for Mockito/testing)
    public PlanDto(Long id, Long operatorId, BigDecimal amount,
                   String validity, String description) {
        this.id = id;
        this.operatorId = operatorId;
        this.amount = amount;
        this.validity = validity;
        this.description = description;
    }

    // ✅ GETTERS

    public Long getId() {
        return id;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getValidity() {
        return validity;
    }

    public String getDescription() {
        return description;
    }

    // ✅ SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}