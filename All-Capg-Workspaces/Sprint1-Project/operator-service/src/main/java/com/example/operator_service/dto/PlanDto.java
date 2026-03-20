//package com.example.operator_service.dto;
//
//import lombok.Builder;
//import lombok.Data;
//import java.math.BigDecimal;
//
//@Data
//@Builder
//public class PlanDto {
//    private Long id;
//    private Long operatorId;
//    private BigDecimal amount;
//    private Integer validity;
//    private String description;
//}
package com.example.operator_service.dto;

public class PlanDto {

    private Long id;
    private Long operatorId;
    private Double amount;
    private String validity;
    private String description;

    // Default Constructor
    public PlanDto() {}

    // Parameterized Constructor
    public PlanDto(Long id, Long operatorId, Double amount, String validity, String description) {
        this.id = id;
        this.operatorId = operatorId;
        this.amount = amount;
        this.validity = validity;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getValidity() { return validity; }
    public void setValidity(String validity) { this.validity = validity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
