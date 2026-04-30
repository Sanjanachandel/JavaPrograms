package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private String profilePictureUrl;
    private LocalDateTime createdAt;

    

    

    

    
    
    
    
    
    
    

    
    
    
    
    
    
    
}