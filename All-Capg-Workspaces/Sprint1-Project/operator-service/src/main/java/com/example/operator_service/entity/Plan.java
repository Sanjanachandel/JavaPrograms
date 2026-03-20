package com.example.operator_service.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "plans")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Plan {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "operator_id", nullable = false)
//    @JsonIgnore
//    private Operator operator;
//
//    @Column(nullable = false)
//    private BigDecimal amount;
//
//    @Column(nullable = false)
//    private Integer validity; // in days
//
//    private String description;
//}package com.example.operator_service.entity;

import jakarta.persistence.*;

@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String validity;
    private String description;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    // ✅ GETTERS & SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getValidity() { return validity; }
    public void setValidity(String validity) { this.validity = validity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Operator getOperator() { return operator; }
    public void setOperator(Operator operator) { this.operator = operator; }
}
