package com.example.operator_service.repository;

import com.example.operator_service.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOperatorId(Long operatorId);
}
