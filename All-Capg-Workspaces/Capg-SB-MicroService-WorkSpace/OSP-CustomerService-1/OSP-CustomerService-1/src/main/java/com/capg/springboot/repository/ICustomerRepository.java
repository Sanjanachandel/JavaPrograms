package com.capg.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.springboot.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, String> {

    Customer findByCustomerId(String customerId);

    List<Customer> findByLocation(String location);
}