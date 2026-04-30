package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent implements Serializable {
    private Long transactionId;
    private Long rechargeId;
    private Long userId;
    private String status;
    private java.math.BigDecimal amount;
    private String rechargeType;

    

    

    
    

    
    

    
    

    
    
}
