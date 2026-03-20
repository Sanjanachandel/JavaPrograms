package com.capg.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OspLoginServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OspLoginServiceApplication.class, args);
		System.out.println("Login Server started");
	}

}
