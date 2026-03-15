package com.bookstore.bookservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.repository.BookRepository;

/**
 * BookService - Business Logic Layer
 * Sits between Controller and Repository
 */
@Service
public class BookService {
	@Autowired BookRepository repository;
	
	//GET all books
	public List<Book> getAllBooks(){
		return repository.findAll();
	}
	
	//GET book by Id
	public Optional<Book> getBookById(Long id){
		return repository.findById(id);
	}
	
	// POST - create a new Book
	public Book createBook(Book book) {
		return repository.save(book);
	}
	
	// PUT - update existing book
	public Book updateBook(Long id, Book updatedBook) {
		Book exists = repository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: "+id));
		
		exists.setTitle(updatedBook.getTitle());
		exists.setAuthor(updatedBook.getAuthor());
		exists.setIsbn(updatedBook.getIsbn());
		exists.setPrice(updatedBook.getPrice());
		exists.setQuantity(updatedBook.getQuantity());
		exists.setCategory(updatedBook.getCategory());
		return repository.save(exists);
	}
	
	// DELETE - delete book by id
	public String deleteBook(Long id) {
		repository.deleteById(id);
		return "Book deleted with ID: "+id;
	}
	
}
