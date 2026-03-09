package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import com.hibernate.entity.BankBean;

public class BankDao {

	SessionFactory sf = new AnnotationConfiguration()
	        .configure("hibernate.cfg.xml")
	        .buildSessionFactory();



    /* CREATE ACCOUNT */
    public void createAccount(BankBean bank) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        session.save(bank);

        session.createSQLQuery(
                "INSERT INTO transactions(account_number, message) " +
                "VALUES (:acc, :msg)")
                .setParameter("acc", bank.getAccountNumber())
                .setParameter("msg", "You created a new account")
                .executeUpdate();

        tx.commit();
        session.close();
    }


    /* GET ACCOUNT */
    public BankBean getAccount(long accNo) {

        Session session = sf.openSession();

        BankBean bank = (BankBean) session.get(BankBean.class, accNo);

        session.close();

        return bank;
    }


    /* DEPOSIT */
    public boolean deposit(long accNo, double amount) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        BankBean bank = (BankBean) session.get(BankBean.class, accNo);

        if(bank == null){
            session.close();
            return false;
        }

        bank.setBalance(bank.getBalance() + amount);

        session.createSQLQuery(
                "INSERT INTO transactions(account_number, message) " +
                "VALUES (:acc, :msg)")
                .setParameter("acc", accNo)
                .setParameter("msg", amount + " deposited successfully")
                .executeUpdate();

        tx.commit();
        session.close();

        return true;
    }


    /* WITHDRAW */
    public boolean withdraw(long accNo, double amount) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        BankBean bank = (BankBean) session.get(BankBean.class, accNo);

        if(bank == null || bank.getBalance() < amount){
            session.close();
            return false;
        }

        bank.setBalance(bank.getBalance() - amount);

        session.createSQLQuery(
                "INSERT INTO transactions(account_number, message) " +
                "VALUES (:acc, :msg)")
                .setParameter("acc", accNo)
                .setParameter("msg", amount + " withdrawn successfully")
                .executeUpdate();

        tx.commit();
        session.close();

        return true;
    }


    /* FUND TRANSFER */
    public boolean fundTransfer(long fromAcc, long toAcc, double amount) {

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        BankBean sender = (BankBean) session.get(BankBean.class, fromAcc);
        BankBean receiver = (BankBean) session.get(BankBean.class, toAcc);

        if(sender == null || receiver == null || sender.getBalance() < amount){
            session.close();
            return false;
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        session.createSQLQuery(
                "INSERT INTO transactions(account_number, message) VALUES (:acc,:msg)")
                .setParameter("acc", fromAcc)
                .setParameter("msg", amount + " transferred to account " + toAcc)
                .executeUpdate();

        session.createSQLQuery(
                "INSERT INTO transactions(account_number, message) VALUES (:acc,:msg)")
                .setParameter("acc", toAcc)
                .setParameter("msg", amount + " received from account " + fromAcc)
                .executeUpdate();

        tx.commit();
        session.close();

        return true;
    }


    /* SHOW TRANSACTIONS */
    public void showTransactions(long accNo) {

        Session session = sf.openSession();

        List<String> list = session.createSQLQuery(
                "SELECT message FROM transactions WHERE account_number = :acc")
                .setParameter("acc", accNo)
                .list();

        for(String msg : list){
        	
            System.out.println(msg);
        }

        session.close();
    }
}
