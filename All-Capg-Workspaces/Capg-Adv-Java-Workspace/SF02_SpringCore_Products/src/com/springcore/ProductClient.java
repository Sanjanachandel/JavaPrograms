package com.springcore;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProductClient {
	public static void main(String[] args)
	{
		ClassPathXmlApplicationContext factory =new ClassPathXmlApplicationContext("applicationContext.xml");
		ProductService p1=(ProductService)factory.getBean("productService1");
		p1.allProducts();
		ProductService p2=(ProductService)factory.getBean("productService2");
		p2.allProducts();
		
	}
}
