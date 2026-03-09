package com.capg.assessment.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.capg.assessment.entity.Order;
import com.capg.assessment.entity.SupportTicket;
import com.capg.assessment.util.JPAUtil;

public class TicketService {
	public void raiseTicket(Long orderId,String issueDescription) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {
			tx.begin();
			Order order = em.find(Order.class, orderId);
			SupportTicket ticket = new SupportTicket();
			ticket.setOrder(order);
			ticket.setIssue(issueDescription);
			em.persist(ticket);
			tx.commit();
			System.out.println("Support ticket raised successfully!");
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
