package com.capg.rechargenova.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {

    @NotNull(message = "Recharge ID cannot be null")
    private Long rechargeId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Payment method cannot be empty")
    private String paymentMethod;

    // ✅ No-args constructor
    public PaymentRequest() {}

    // ✅ All-args constructor (useful for Mockito/testing)
    public PaymentRequest(Long rechargeId, Long userId,
                          BigDecimal amount, String paymentMethod) {
        this.rechargeId = rechargeId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // ✅ Getters & Setters
    public Long getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(Long rechargeId) {
        this.rechargeId = rechargeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}