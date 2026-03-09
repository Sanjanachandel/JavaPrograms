package com.capg.hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;



public class InsertStudent {
	public static void main(String[] args)
	{
		//1. start Hibernate framework
		Configuration cfg=new Configuration();
		cfg.configure();//loads hibernate.cgf.xml
		//2. Build SessionFactory
		SessionFactory factory=cfg.buildSessionFactory();
		//3 Open session
		Session session =factory.openSession();
		//4 Begin transaction
		Transaction tx=session.beginTransaction();
		try {
			//5 create student object
			Student s1= new Student();
					s1.setSno(9);
					s1.setSname("Sanjana");
					s1.setEmail("sanjana@gmail.com");
					s1.setMobile(99999);
					session.save(s1);
					//6 commit Transaction
					tx.commit();
					System.out.println("Record insert successfully");
		}catch(Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
			factory.close();
		}
	}
}
