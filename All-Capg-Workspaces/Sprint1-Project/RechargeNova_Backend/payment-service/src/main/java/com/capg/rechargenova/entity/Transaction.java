package com.capg.rechargenova.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Recharge ID is required")
    private Long rechargeId;

    @Column(nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;

    @Column(nullable = false)
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; 

    @Column(nullable = false)
    @NotBlank(message = "Status is required")
    private String status; 

    private LocalDateTime transactionTime;

    

    

    
    

    
    

    
    

    
    

    
    

    
    

    
    
}
