package com.hibernate.service;

import com.hibernate.dao.BankDao;
import com.hibernate.entity.BankBean;

public class BankService {

    BankDao dao = new BankDao();


    /* CREATE */
    public void createAccount(long accNo, String name, long mobile, double balance) {

        BankBean bank = new BankBean();

        bank.setAccountNumber(accNo);
        bank.setName(name);
        bank.setMobile(mobile);
        bank.setBalance(balance);

        dao.createAccount(bank);

        System.out.println("Account Created Successfully");
    }


    /* SHOW BALANCE */
    public void showBalance(long accNo) {

        BankBean bank = dao.getAccount(accNo);

        if(bank != null){
            System.out.println("Name : " + bank.getName());
            System.out.println("Balance : " + bank.getBalance());
        }
        else{
            System.out.println("Account Not Found");
        }
    }


    /* DEPOSIT */
    public void deposit(long accNo, double amount) {

        if(dao.deposit(accNo, amount))
            System.out.println("Deposit Successful");
        else
            System.out.println("Deposit Failed");
    }


    /* WITHDRAW */
    public void withdraw(long accNo, double amount) {

        if(dao.withdraw(accNo, amount))
            System.out.println("Withdrawal Successful");
        else
            System.out.println("Insufficient Balance or Account Not Found");
    }


    /* FUND TRANSFER */
    public void fundTransfer(long fromAcc, long toAcc, double amount) {

        if(dao.fundTransfer(fromAcc, toAcc, amount))
            System.out.println("Fund Transfer Successful");
        else
            System.out.println("Transfer Failed");
    }


    /* PRINT TRANSACTIONS */
    public void showTransactions(long accNo) {

        dao.showTransactions(accNo);
    }
}
