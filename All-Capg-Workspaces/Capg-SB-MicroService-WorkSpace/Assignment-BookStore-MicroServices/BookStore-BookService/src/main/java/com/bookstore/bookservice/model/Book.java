package com.bookstore.bookservice.model;

import jakarta.persistence.*;

/**
 * Book Entity - maps to 'books' table in Oracle DB
 */
@Entity
@Table(name = "Books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_seq")
	@SequenceGenerator(name = "books_seq", sequenceName = "BOOKS_SEQ",allocationSize = 1)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String author;
	
	@Column(unique = true)
	private String isbn;
	
	@Column(nullable = false)
	private Double price;
	
	@Column
	private Integer quantity;
	
	@Column
	private String category;
	 
	// No-arg constructor
	public Book() {}

	// All-arg constructor
	public Book(Long id, String title, String author, String isbn, Double price, Integer quantity, String category) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	};
	
	
	
}
