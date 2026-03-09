package com.capg.assessment.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.capg.assessment.util.JPAUtil;

public class ReportService {
	public void getEmployeePerformance(Long employeeId) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			Query query = em.createQuery("select count(l.id) from Lead l where l.employee.id = :eid");
			query.setParameter("eid", employeeId);
			Long count = (Long) query.getSingleResult();
			System.out.println("Total Leads Handled by Employee " + employeeId + " : " + count);
		}
		finally {
			em.close();
		}
		
	}
}
