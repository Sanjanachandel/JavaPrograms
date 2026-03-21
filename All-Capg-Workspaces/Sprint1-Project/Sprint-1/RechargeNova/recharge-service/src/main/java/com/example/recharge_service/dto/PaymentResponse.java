package com.example.recharge_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long rechargeId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime transactionTime;
}
