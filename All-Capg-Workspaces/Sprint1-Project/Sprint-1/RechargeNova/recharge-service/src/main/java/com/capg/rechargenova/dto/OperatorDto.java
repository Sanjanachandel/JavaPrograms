package com.capg.rechargenova.dto;

public class OperatorDto {

    private Long id;
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