package com.example.recharge_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RechargeResponse {

    private Long id;
    private Long userId;
    private Long operatorId;
    private Long planId;
    private String mobileNumber;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private String message;

    public RechargeResponse() {}

    public RechargeResponse(Long id, Long userId, Long operatorId, Long planId,
                            String mobileNumber, BigDecimal amount,
                            String status, LocalDateTime createdAt, String message) {
        this.id = id;
        this.userId = userId;
        this.operatorId = operatorId;
        this.planId = planId;
        this.mobileNumber = mobileNumber;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.message = message;
    }

    // GETTERS & SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}