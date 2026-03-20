package com.capg.springboot.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.springboot.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}