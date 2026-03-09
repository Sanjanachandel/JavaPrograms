package com.capg.assessment.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.capg.assessment.entity.Customer;
import com.capg.assessment.entity.Order;
import com.capg.assessment.entity.Product;
import com.capg.assessment.util.JPAUtil;

public class OrderService {
	public void placeOrder(Long customerId,List<Long> productIds) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			Customer customer = em.find(Customer.class, customerId);
			List<Product> products = new ArrayList<>();	
			double totalAmount = 0.0;
			
			for(Long productId:productIds) {
				Product product = em.find(Product.class, productId);
				if(product != null) {
					products.add(product);
					totalAmount += product.getPrice();
				}
			}
			
			Order order = new Order();
			order.setCustomer(customer);
			order.setProducts(products);
			order.setTotalAmount(totalAmount);
			
			em.persist(order);
			tx.commit();
			System.out.println("Order placed successfully! Total Amount: " + totalAmount); 
		}
		catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			em.close();
		}
	}
}
