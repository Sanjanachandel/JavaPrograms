package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {
    private String recipientEmail;
    private String userName;
    private String mobileNumber;
    private String operatorName;
    private double amount;
    private String validity;
    private String transactionId;
    private String rechargeType;

    

    

    
    

    
    

    
    

    
    

    
    

    
    

    
    
}
