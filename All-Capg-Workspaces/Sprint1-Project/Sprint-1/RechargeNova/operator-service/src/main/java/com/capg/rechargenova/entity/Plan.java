package com.capg.rechargenova.entity;

import jakarta.persistence.*;

@Entity
public class Plan {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_seq")
    @SequenceGenerator(name = "plan_seq", sequenceName = "plan_seq", allocationSize = 1)
    private Long id;

    private Double amount;
    private String validity;
    private String description;

//    @ManyToOne
//    @JoinColumn(name = "operator_id")
//    private Operator operator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id", nullable = false)
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
