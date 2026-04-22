package com.capg.rechargenova.dto;

import java.math.BigDecimal;

public class RechargeResponse {
    private Long id;
    private Long userId;
    private Long operatorId;
    private Long planId;
    private String mobileNumber;
    private BigDecimal amount;
    private String status;

    public RechargeResponse() {}

    public RechargeResponse(Long id, Long userId, Long operatorId, Long planId, String mobileNumber, BigDecimal amount, String status) {
        this.id = id;
        this.userId = userId;
        this.operatorId = operatorId;
        this.planId = planId;
        this.mobileNumber = mobileNumber;
        this.amount = amount;
        this.status = status;
    }

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
}
