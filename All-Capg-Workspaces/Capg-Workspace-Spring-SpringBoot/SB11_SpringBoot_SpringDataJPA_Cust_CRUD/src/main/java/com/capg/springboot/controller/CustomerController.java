package com.capg.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capg.springboot.entity.Customer;
import com.capg.springboot.service.CustomerService;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@PostMapping("/addcustomer")
	public int addCust(@RequestBody Customer cust) {
		return service.addCustomer(cust);
	}
	
	@GetMapping("/allcustomers")
	public List<Customer> getAllCust(){
		return service.getAllCustomer();
	}
	
	@GetMapping("/getcustomer/{id}")
	public Customer getCustById(@PathVariable int id) {
		return service.getCustomer(id);
	}
	
	@PutMapping("/updatecustomer")
	public int updateCust(@RequestBody Customer cust) {
		return service.updateCustomer(cust);
	}
	
	@DeleteMapping("/deletecustomer/{id}")
	public int deleteCust(@PathVariable int id) {
		return service.deleteCustomer(id);
	}
}
