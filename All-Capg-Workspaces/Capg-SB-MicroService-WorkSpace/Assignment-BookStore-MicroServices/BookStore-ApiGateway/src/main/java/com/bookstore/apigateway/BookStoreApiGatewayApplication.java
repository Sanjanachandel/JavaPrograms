package com.bookstore.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookStoreApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApiGatewayApplication.class, args);
		System.out.println("ApiGateWay Server Stared...");
	}

}
