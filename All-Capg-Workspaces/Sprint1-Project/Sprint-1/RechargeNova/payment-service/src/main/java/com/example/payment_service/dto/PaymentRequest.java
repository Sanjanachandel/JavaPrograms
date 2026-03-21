package com.example.payment_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long rechargeId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
}
