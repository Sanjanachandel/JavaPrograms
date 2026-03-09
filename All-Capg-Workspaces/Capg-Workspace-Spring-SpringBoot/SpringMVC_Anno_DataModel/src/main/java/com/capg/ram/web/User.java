package com.capg.ram.web;

/*
 POJO class used for:
 Data Binding
 Transferring data between layers
*/
public class User {

    // Must match input field names
    private String username;
    private String password;

    // Getter & Setter required for Spring binding
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}