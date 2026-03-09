package com.capg.java.dao;



import com.capg.java.entity.Account;

public interface AccountDAO {

    void save(Account acc);

    Account findById(Long id);

    void update(Account acc);
}