package com.capg.rechargenova.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OperatorDto {

    private Long id;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets")
    @NotBlank(message = "Name is required")
    private String name;

    // ✅ No-args constructor
    public OperatorDto() {}

    // ✅ All-args constructor (useful for testing / Mockito)
    public OperatorDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // ✅ Getter & Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ✅ Getter & Setter for name (MISSING IN YOUR CODE)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}