package com.example.bookinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Feign2BookInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Feign2BookInfoApplication.class, args);
		System.out.println("BookInfo Service Started...");
	}

}
