package com.example.operator_service.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.util.List;
//
//@Entity
//@Table(name = "operators")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Operator {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String name;
//
//    @Column(nullable = false)
//    private String type; // e.g., PREPAID, POSTPAID
//
//    @Column(nullable = false)
//    private String circle;
//
//    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Plan> plans;
//}package com.example.operator_service.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String circle;

    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
    private List<Plan> plans;

    // ✅ GETTERS & SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCircle() { return circle; }
    public void setCircle(String circle) { this.circle = circle; }

    public List<Plan> getPlans() { return plans; }
    public void setPlans(List<Plan> plans) { this.plans = plans; }
}
