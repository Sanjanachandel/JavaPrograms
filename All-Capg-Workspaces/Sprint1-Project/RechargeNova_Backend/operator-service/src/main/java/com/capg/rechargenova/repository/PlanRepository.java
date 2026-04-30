package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capg.rechargenova.entity.Plan;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOperatorId(Long operatorId);
    org.springframework.data.domain.Page<Plan> findByOperatorId(Long operatorId, org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Plan p WHERE p.id = :id")
    void deletePlanById(Long id);
}
