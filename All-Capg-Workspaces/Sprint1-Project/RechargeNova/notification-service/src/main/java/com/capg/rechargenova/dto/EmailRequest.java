package com.capg.rechargenova.dto;

public class EmailRequest {
    private String recipientEmail;
    private String userName;
    private String mobileNumber;
    private String operatorName;
    private double amount;
    private String validity;
    private String transactionId;

    public EmailRequest() {}

    public EmailRequest(String recipientEmail, String userName, String mobileNumber, String operatorName, double amount, String validity, String transactionId) {
        this.recipientEmail = recipientEmail;
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.operatorName = operatorName;
        this.amount = amount;
        this.validity = validity;
        this.transactionId = transactionId;
    }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getValidity() { return validity; }
    public void setValidity(String validity) { this.validity = validity; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
