package com.capg.assessment.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.capg.assessment.entity.Customer;
import com.capg.assessment.entity.Lead;
import com.capg.assessment.entity.SalesEmployee;
import com.capg.assessment.util.JPAUtil;

public class LeadService {
	public void createLead(String name,String source,String contactInfo) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			Lead lead = new Lead();
			lead.setName(name);
			lead.setSource(source);
			lead.setContactInfo(contactInfo);
			em.persist(lead);
			tx.commit();
			System.out.println("Lead created successfully!");
		}
		catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally{
			em.close();
		}	
	}
	
	public void assignLeadToEmployee(Long leadId, Long employeeId) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			Lead lead = em.find(Lead.class, leadId);
			SalesEmployee employee = em.find(SalesEmployee.class, employeeId);
			lead.setEmployee(employee);
			em.merge(lead);
			tx.commit();
			System.out.println("Lead assigned to employee successfully!"); 
			
		}
		catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			em.close();
		}
	}
	
	public void convertLeadToCustomer(Long leadId) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Lead lead = em.find(Lead.class,leadId);
			Customer customer = new Customer();
			customer.setName(lead.getName());
			customer.setEmail(lead.getContactInfo());
			customer.setPhone(lead.getContactInfo());
			
			em.persist(customer);
			em.remove(lead);
			tx.commit();
			System.out.println("Lead converted to customer successfully!");
			
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
