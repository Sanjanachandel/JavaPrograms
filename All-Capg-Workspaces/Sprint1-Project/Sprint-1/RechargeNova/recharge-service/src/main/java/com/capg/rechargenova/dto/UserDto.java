package com.capg.rechargenova.dto;

public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;

    // ✅ IMPORTANT: Keep as String to avoid Feign Date parsing issues
    private String createdAt;

    // ✅ Default Constructor (Required)
    public UserDto() {}

    // ✅ Parameterized Constructor (Useful for testing / Mockito)
    public UserDto(Long id, String name, String email,
                   String role, String phoneNumber,
                   String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    // ✅ GETTERS

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // ✅ SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}