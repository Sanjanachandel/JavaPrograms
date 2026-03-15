package com.bookstore.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.orderservice.model.Order;
import com.bookstore.orderservice.service.OrderService;

/**
 * OrderController - REST API Layer
 * Exposes 5 CRUD endpoints
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	private OrderService service;
	
	// GET all orders
    // http://localhost:8082/api/orders
	@GetMapping
	public List<Order> getAllOrders(){
		return service.getAllOrders();
	}
	
	// GET order by id
    // http://localhost:8082/api/orders/1
	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable Long id) {
		return service.getOrderById(id).orElseThrow(() -> new RuntimeException("Order not found with id: "+id));
				
	}
	
	// POST - place new order
    // http://localhost:8082/api/orders
	@PostMapping
	public Order placeOrder(@RequestBody Order order) {
		return service.placeOrder(order);
	}
	
	// PUT - update order status
    // http://localhost:8082/api/orders/1
	@PutMapping("/{id}")
	public Order updateOrderStatus(@PathVariable Long id, @RequestBody Order order) {
		return service.updateOrderStatus(id, order.getStatus());
	}
	
	// DELETE - delete order by id
    // http://localhost:8082/api/orders/1
	@DeleteMapping("/{id}")
	public String deleteOrder(@PathVariable Long id) {
		return service.deleteOrder(id);
	}
	
}
