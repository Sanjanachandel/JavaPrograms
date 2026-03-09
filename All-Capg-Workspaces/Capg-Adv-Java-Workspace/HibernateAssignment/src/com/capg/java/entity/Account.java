package com.capg.java.entity;

import javax.persistence.*;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acc_seq_gen")
    @SequenceGenerator(
        name = "acc_seq_gen",
        sequenceName = "ACC_SEQ",
        allocationSize = 1
    )
    private int accNo;

    private String name;
    private String mobile;
    private double balance;

    // 🔹 Default constructor (Required)
    public Account() {}

    // 🔹 Parameterized constructor (Required for your code)
    public Account(String name, String mobile, double balance) {
        this.name = name;
        this.mobile = mobile;
        this.balance = balance;
    }

    // Getters & Setters

    public int getAccNo() {
        return accNo;
    }

    public void setAccNo(int accNo) {
        this.accNo = accNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
