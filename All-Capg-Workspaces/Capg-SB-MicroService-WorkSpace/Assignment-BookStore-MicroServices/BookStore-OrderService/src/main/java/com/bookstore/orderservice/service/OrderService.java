package com.bookstore.orderservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.orderservice.client.BookClient;
import com.bookstore.orderservice.dto.BookDTO;
import com.bookstore.orderservice.model.Order;
import com.bookstore.orderservice.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private BookClient bookClient; 
	
	// GET all orders
	public List<Order> getAllOrders(){
		return repository.findAll();
	}
	
	// GET order by id
	public Optional<Order> getOrderById(Long id){
		return repository.findById(id);
	}
	
	// POST - place new order
	public Order placeOrder(Order order) {
		BookDTO book = bookClient.getBookById(order.getBookId());
		
		if(book == null) {
			throw new RuntimeException("Book not found with id: "+order.getBookId());
		}
		
		order.setTotalPrice(book.getPrice() * order.getQuantity());
		order.setStatus("PLACED");
		order.setOrderDate(LocalDate.now());
		return repository.save(order);
	}
	
	// PUT - update order status
	public Order updateOrderStatus(Long id,String status) {
		Order exists = repository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: "+id));
		
		exists.setStatus(status);
		return repository.save(exists);
	}
	
	// DELETE - delete order by id
	public String deleteOrder(Long id) {
		repository.deleteById(id);
		return "Order delete with ID: "+id;
	}
}
