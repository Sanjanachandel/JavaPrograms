package com.capg.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.springboot.entity.Customer;
import com.capg.springboot.repository.CustomerRepo;


@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepo repo;
	
	
	public int addCustomer(Customer obj) {
		repo.save(obj);
		
		return obj.getCustid();
	}
	
	public List<Customer> getAllCustomer(){
		
		List<Customer> customers = new ArrayList<>();
		
		repo.findAll().forEach(b1 -> customers.add(b1));
		
		return customers;
	}
	
	public Customer getCustomer(int id) {
		return repo.findById(id).get();
	}
	
	public int updateCustomer(Customer obj) {
		repo.save(obj);
		
		return obj.getCustid();
	}
	
	public int deleteCustomer(int id) {
		repo.deleteById(id);
		return id;
	}
}
