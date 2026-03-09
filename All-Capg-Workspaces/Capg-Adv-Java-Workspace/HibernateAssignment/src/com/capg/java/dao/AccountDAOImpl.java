package com.capg.java.dao;



import com.capg.java.entity.Account;
import com.capg.java.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AccountDAOImpl implements AccountDAO {

    public void save(Account acc) {

        Session s = HibernateUtil.getFactory().openSession();
        Transaction tx = s.beginTransaction();

        s.save(acc);

        tx.commit();
        s.close();
    }

    public Account findById(Long id) {

        Session s = HibernateUtil.getFactory().openSession();
        Account acc = (Account) s.get(Account.class, id);

        s.close();
        return acc;
    }

    public void update(Account acc) {

        Session s = HibernateUtil.getFactory().openSession();
        Transaction tx = s.beginTransaction();

        s.update(acc);

        tx.commit();
        s.close();
    }
}

