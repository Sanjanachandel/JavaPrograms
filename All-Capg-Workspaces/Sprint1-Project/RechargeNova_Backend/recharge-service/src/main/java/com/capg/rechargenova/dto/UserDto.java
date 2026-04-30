package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets")
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[A-Za-z_]+$", message = "Role must contain only characters and underscores")
    private String role;

    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String phoneNumber;

    // ✅ IMPORTANT: Keep as String to avoid Feign Date parsing issues
    private String createdAt;

    // ✅ Default Constructor (Required)
    

    // ✅ Parameterized Constructor (Useful for testing / Mockito)
    

    // ✅ GETTERS

    

    

    

    

    

    

    // ✅ SETTERS

    

    

    

    

    

    
}