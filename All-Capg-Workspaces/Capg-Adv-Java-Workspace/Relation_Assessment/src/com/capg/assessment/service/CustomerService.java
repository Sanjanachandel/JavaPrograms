package com.capg.assessment.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.capg.assessment.entity.Address;
import com.capg.assessment.entity.Customer;
import com.capg.assessment.util.JPAUtil;


public class CustomerService {
	public void registerCustomer(String name,String email,String phone) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			Customer customer = new Customer();
			customer.setName(name);
			customer.setEmail(email);
			customer.setPhone(phone);
			em.persist(customer);
			tx.commit();
			System.out.println("Customer registered successfully!");
		}
		catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			em.close();
		}
	}
	
	public void addAddressToCustomer(Long customerId, Address address) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Customer customer = em.find(Customer.class, customerId);
			customer.setAddress(address);
			em.merge(customer);
			tx.commit();
			System.out.println("Address added to customer successfully!"); 
		}
		catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally{
			em.close();
		}
	}
}
