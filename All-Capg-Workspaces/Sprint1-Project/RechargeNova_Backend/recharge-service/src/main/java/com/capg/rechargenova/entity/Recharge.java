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
@Table(name = "recharges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;

    @Column(nullable = false)
    @NotNull(message = "Operator ID is required")
    private Long operatorId;

    @Column(nullable = false)
    @NotNull(message = "Plan ID is required")
    private Long planId;

    @Column(nullable = false)
    @Pattern(regexp = "^[0-9]+$", message = "Mobile number must contain only digits")
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @Column(nullable = false, precision = 10, scale = 2) 
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotBlank(message = "Status is required")
    private String status;

    @Column(nullable = false)
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @Column(nullable = false)
    @NotBlank(message = "Recharge type is required")
    private String rechargeType; // Prepaid or Postpaid

    @Column(nullable = false, updatable = false) 
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ✅ Default constructor (REQUIRED for JPA)
    

    // ✅ Parameterized constructor
    

    // ✅ Auto set timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ GETTERS

    

    

    

    

    

    

    

    

    

    // ✅ SETTERS

    

    

    

    

    

    

    

    

    

    
}