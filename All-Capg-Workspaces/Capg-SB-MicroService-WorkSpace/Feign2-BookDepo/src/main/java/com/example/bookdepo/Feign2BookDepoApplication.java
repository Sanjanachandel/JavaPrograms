package com.example.bookdepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.example.bookdepo.client")
@SpringBootApplication
public class Feign2BookDepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Feign2BookDepoApplication.class, args);
		System.out.println("Book Depo Server Started");
	}

}
