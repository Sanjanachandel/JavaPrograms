package com.capg.rechargenova.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatorDto {

    private Long id;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets")
    @NotBlank(message = "Name is required")
    private String name;

    // ✅ No-args constructor
    

    // ✅ All-args constructor (useful for testing / Mockito)
    

    // ✅ Getter & Setter for id
    

    

    // ✅ Getter & Setter for name (MISSING IN YOUR CODE)
    

    
}