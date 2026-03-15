package com.bookstore.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bookstore.bookservice.model.Book;

/**
 * BookRepository - No code needed
 * JpaRepository auto-generates all CRUD methods
 * <Book, Long> - Entity type is Book, Primary Key type is Long
 */

public interface BookRepository extends JpaRepository<Book, Long>{

}
