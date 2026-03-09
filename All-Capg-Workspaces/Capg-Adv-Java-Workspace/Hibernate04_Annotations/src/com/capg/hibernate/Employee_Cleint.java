package com.capg.hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
public class Employee_Cleint {
	public static void main(String[] args)
	{
		SessionFactory sessionFactory =new AnnotationConfiguration().configure("hibernate_annotation.cfg.xml").buildSessionFactory();
		Session session =sessionFactory.openSession();
		Transaction txn=session.beginTransaction();
		try {
			Employee employee =new Employee();
			employee.setFirstName("Sanjana");
			employee.setLastName("Chandel");
			employee.setSalary(900000);
			session.save(employee);
			txn.commit();
			
		}
		catch(HibernateException e)
		{
			txn.rollback();
			System.out.println("exception while creating employee "+e);
			e.printStackTrace();
		}
		finally {
			session.close();
		}
	}
}
