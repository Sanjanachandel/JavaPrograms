package com.capg.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients
@SpringBootApplication
public class OspCustomerService1Application {

	public static void main(String[] args) {
		SpringApplication.run(OspCustomerService1Application.class, args);
		System.out.println("Customer service started");
	}

}
