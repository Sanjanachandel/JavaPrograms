package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capg.rechargenova.entity.Recharge;

import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {

    // ✅ Fetch all recharges by user
    List<Recharge> findByUserId(Long userId);

    // ✅ (OPTIONAL BUT USEFUL) Fetch latest recharges first
    List<Recharge> findByUserIdOrderByCreatedAtDesc(Long userId);
}