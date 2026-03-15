package com.bookstore.orderservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bookstore.orderservice.dto.BookDTO;

/**
 * BookClient - Feign Interface
 * Mirrors all 5 endpoints of Book Service
 * Spring generates actual HTTP code automatically
 * name must exactly match spring.application.name in Book Service
 */
@FeignClient(name = "BOOK-SERVICE")
public interface BookClient {
	
	// GET all books
	@GetMapping("/api/books")
	List<BookDTO> getAllBooks();
	
	// GET book by id
	@GetMapping("/api/books/{id}")
	BookDTO getBookById(@PathVariable Long id);
	
	// POST - create book
	@PostMapping("/api/books")
	BookDTO createBook(@RequestBody BookDTO book);
	
	// PUT - update book
	@PutMapping("/api/books/{id}")
	BookDTO updateBook(@PathVariable Long id,@RequestBody BookDTO book);
	
	// DELETE - delete book
	@DeleteMapping("/api/books/{id}")
	String deleteBook(@PathVariable Long id);
	
}
