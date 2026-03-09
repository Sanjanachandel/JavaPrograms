package com.capg.java.service;

import com.capg.java.dao.*;
import com.capg.java.entity.Account;

public class AccountServiceImpl implements AccountService {

    private AccountDAO dao = new AccountDAOImpl();

    public void createAccount(String name, String mobile, double amount) {

        Account acc = new Account(name, mobile, amount);
        dao.save(acc);

        System.out.println("Account Created Successfully!");
    }

    public void deposit(Long id, double amount) {

        Account acc = dao.findById(id);

        acc.setBalance(acc.getBalance() + amount);

        dao.update(acc);

        System.out.println("Deposit Successful");
    }

    public void withdraw(Long id, double amount) {

        Account acc = dao.findById(id);

        if (acc.getBalance() < amount) {
            System.out.println("Insufficient Balance");
            return;
        }

        acc.setBalance(acc.getBalance() - amount);

        dao.update(acc);

        System.out.println("Withdrawal Successful");
    }

    public void transfer(Long fromId, Long toId, double amount) {

        Account from = dao.findById(fromId);
        Account to = dao.findById(toId);

        if (from.getBalance() < amount) {
            System.out.println("Insufficient Balance");
            return;
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        dao.update(from);
        dao.update(to);

        System.out.println("Transfer Successful");
    }

    public void showBalance(Long id) {

        Account acc = dao.findById(id);

        System.out.println("Current Balance: " + acc.getBalance());
    }
}
