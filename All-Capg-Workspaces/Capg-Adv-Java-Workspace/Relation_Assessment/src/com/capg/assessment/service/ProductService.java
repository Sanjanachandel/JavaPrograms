package com.capg.assessment.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.capg.assessment.entity.Product;
import com.capg.assessment.util.JPAUtil;

public class ProductService {
	public void addProduct(String name, double price) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Product product = new Product();
			product.setProductName(name);
			product.setPrice(price);
			em.persist(product);
			tx.commit();
			System.out.println("Product added successfully!");
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
