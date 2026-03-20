package com.capg.springboot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ADDRESS3")
public class Address {

    @Id
    @Column(name = "ADDRESS_ID")   // 🔥 matches DB column
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    public Address() {}

    public Address(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}