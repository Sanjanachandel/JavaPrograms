package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.rechargenova.entity.Plan;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOperatorId(Long operatorId);
}
