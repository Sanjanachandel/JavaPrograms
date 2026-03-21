package com.example.operator_service.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Operator {

	 @Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operator_seq")
	 @SequenceGenerator(name = "operator_seq", sequenceName = "operator_seq", allocationSize = 1)
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
