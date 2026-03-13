package com.demoorg.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demoorg.customerservice.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
