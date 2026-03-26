package com.capg.rechargenova.dto;

import java.io.Serializable;

public class PaymentEvent implements Serializable {
    private Long transactionId;
    private Long rechargeId;
    private Long userId;
    private String status;

    public PaymentEvent() {}

    public PaymentEvent(Long transactionId, Long rechargeId, Long userId, String status) {
        this.transactionId = transactionId;
        this.rechargeId = rechargeId;
        this.userId = userId;
        this.status = status;
    }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public Long getRechargeId() { return rechargeId; }
    public void setRechargeId(Long rechargeId) { this.rechargeId = rechargeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
