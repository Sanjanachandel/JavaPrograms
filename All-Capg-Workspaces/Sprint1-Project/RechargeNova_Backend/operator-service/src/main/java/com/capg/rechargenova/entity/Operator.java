package com.capg.rechargenova.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets")
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Circle is required")
    private String circle;

//    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
//    private List<Plan> plans;
    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Plan> plans;



    
    

    
    

    
    

    
    

    
    
}
