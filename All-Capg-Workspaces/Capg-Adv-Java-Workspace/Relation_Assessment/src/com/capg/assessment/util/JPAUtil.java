package com.capg.assessment.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	 private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA-CRM");

	 public static EntityManager getEntityManager() {
		 return emf.createEntityManager();
	 }

	 public static void closeFactory() {
		 if (emf != null && emf.isOpen()) {
			 emf.close();
	     }
	 }
}
