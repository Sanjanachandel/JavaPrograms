package com.java.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientProductDao {

    public static void main(String[] args) {

        ApplicationContext factory = new ClassPathXmlApplicationContext("applicationContext_Dao.xml");

        ProductDao dao = (ProductDao) factory.getBean("productDao");

        // Create product object
        Product product = new Product();
        product.setPid(50);
        product.setPname("Laptop");
        product.setPrice(7777);
        product.setQuantity(3);

        // Save into database
        dao.save(product);

        // Retrieve
        Product p = dao.get(50);

        System.out.println("PID: " + p.getPid());
        System.out.println("Name: " + p.getPname());
        System.out.println("Price: " + p.getPrice());
        System.out.println("Quantity: " + p.getQuantity());
    }
}