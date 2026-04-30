package com.capg.rechargenova.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrderRequest {
    private Double amount;
    private String currency;
    private String receipt;
    private Long rechargeId;
    private Long userId;
    private String rechargeType;
}
