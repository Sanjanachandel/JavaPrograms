package com.bookstore.bookservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.service.BookService;

/**
 * BookController - REST API Layer
 * Exposes 5 CRUD endpoints
 */
@RestController
@RequestMapping("/api/books")
public class BookController {
	@Autowired
	private BookService service;
	
	// GET all books
    // http://localhost:8081/api/books
	@GetMapping
	public List<Book> getAllBooks(){
		return service.getAllBooks();
	}
	
	// GET book by id
    // http://localhost:8081/api/books/1
	@GetMapping("/{id}")
	public Book getBookById(@PathVariable Long id) {
		return service.getBookById(id).orElseThrow(() -> new RuntimeException("Book not found with id: "+id));
	}
	
	// POST - create new book
    // http://localhost:8081/api/books
	@PostMapping
	public Book createBook(@RequestBody Book book) {
		return service.createBook(book);
	}
	
	// PUT - update existing book
    // http://localhost:8081/api/books/1O
	@PutMapping("/{id}")
	public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
		return service.updateBook(id, book);
	}
	
	//DELETE - delete book by id
    // http://localhost:8081/api/books/1
	@DeleteMapping("/{id}")
	public String deleteBook(@PathVariable Long id) {
		return service.deleteBook(id);
	}
	
	
}
