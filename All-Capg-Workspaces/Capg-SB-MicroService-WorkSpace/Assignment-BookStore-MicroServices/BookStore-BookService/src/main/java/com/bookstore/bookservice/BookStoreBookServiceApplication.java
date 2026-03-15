package com.bookstore.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookStoreBookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreBookServiceApplication.class, args);
		System.out.println("Book Server Started...");
	}

}
