package com.capg.java.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

    private static final SessionFactory factory =
            new AnnotationConfiguration()
            .configure()
            .buildSessionFactory();

    public static SessionFactory getFactory() {
        return factory;
    }
}
