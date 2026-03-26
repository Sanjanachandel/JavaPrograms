package com.capg.rechargenova.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    private Long rechargeId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;

    public PaymentRequest() {}

    public PaymentRequest(Long rechargeId, Long userId, BigDecimal amount, String paymentMethod) {
        this.rechargeId = rechargeId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Long getRechargeId() { return rechargeId; }
    public void setRechargeId(Long rechargeId) { this.rechargeId = rechargeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
