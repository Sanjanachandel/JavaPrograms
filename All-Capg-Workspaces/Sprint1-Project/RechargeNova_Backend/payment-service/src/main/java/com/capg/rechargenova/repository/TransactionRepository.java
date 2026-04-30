package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.rechargenova.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByRechargeId(Long rechargeId);
}
