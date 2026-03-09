package com.capg.java.service;

public interface AccountService {

    void createAccount(String name, String mobile, double amount);

    void deposit(Long id, double amount);

    void withdraw(Long id, double amount);

    void transfer(Long fromId, Long toId, double amount);

    void showBalance(Long id);
}
