package com.capg.rechargenova.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RechargeRequest {

    @NotNull(message = "Operator ID is required")
    private Long operatorId;

    @NotNull(message = "Plan ID is required")
    private Long planId;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]+$", message = "Mobile number must contain only digits")
    private String mobileNumber;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    public RechargeRequest() {}

    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}