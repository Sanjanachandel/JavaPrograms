package com.bookstore.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.orderservice.model.Order;

/**
 * OrderRepository - No code needed
 * JpaRepository auto-generates all CRUD methods
 * <Order, Long> - Entity type is Order, Primary Key type is Long
 */

public interface OrderRepository extends JpaRepository<Order, Long>{

}
