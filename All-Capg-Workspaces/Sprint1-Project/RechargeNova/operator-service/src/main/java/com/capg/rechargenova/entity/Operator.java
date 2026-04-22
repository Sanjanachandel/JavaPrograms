package com.capg.rechargenova.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets")
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Circle is required")
    private String circle;

//    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
//    private List<Plan> plans;
    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
