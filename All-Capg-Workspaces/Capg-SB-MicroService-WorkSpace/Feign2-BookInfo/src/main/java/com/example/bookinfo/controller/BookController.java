package com.example.bookinfo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookinfo.model.Book;

@RestController
@RequestMapping("/books")
public class BookController {
	// http://localhost:8001/books/101
	@GetMapping("/{bookId}")
	public Book getBookById(@PathVariable String bookId) {
		return new Book(bookId , "Spring ","Sanjana", 599.0);
	}
}
