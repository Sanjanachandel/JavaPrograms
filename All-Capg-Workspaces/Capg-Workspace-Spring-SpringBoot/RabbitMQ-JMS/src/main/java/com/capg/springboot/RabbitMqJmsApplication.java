package com.capg.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqJmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqJmsApplication.class, args);
		System.out.println("SpringBoot Server");
	}

}
