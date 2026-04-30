package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capg.rechargenova.entity.Recharge;

import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {

    // ✅ Fetch all recharges by user (Paginated)
    org.springframework.data.domain.Page<Recharge> findByUserId(Long userId, org.springframework.data.domain.Pageable pageable);

    // ✅ (OPTIONAL BUT USEFUL) Fetch latest recharges first
    List<Recharge> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT SUM(r.amount) FROM Recharge r WHERE r.status = 'SUCCESS'")
    Double sumTotalRevenue();
}