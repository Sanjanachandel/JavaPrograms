package com.bookstore.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class BookStoreEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreEurekaServerApplication.class, args);
		System.out.println("Eureka Server Started...");
	}

}
