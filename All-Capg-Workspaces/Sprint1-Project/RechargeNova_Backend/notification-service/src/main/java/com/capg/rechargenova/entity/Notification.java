package com.capg.rechargenova.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;

    @Column(nullable = false)
    @NotBlank(message = "Message is required")
    private String message;

    @Column(nullable = false)
    @NotBlank(message = "Type is required")
    private String type; 

    @Column(nullable = false)
    @NotBlank(message = "Status is required")
    private String status; 

    @Column(updatable = false)
    private LocalDateTime createdAt;

    

    

    
    

    
    

    
    

    
    

    
    

    
    
}
