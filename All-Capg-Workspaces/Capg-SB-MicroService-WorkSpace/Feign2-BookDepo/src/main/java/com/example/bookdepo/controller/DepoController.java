package com.example.bookdepo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookdepo.client.BookInfoClient;
import com.example.bookdepo.model.Book;

@RestController
@RequestMapping("/depo")
public class DepoController {
	private final BookInfoClient bookInfoClient;
	
	public DepoController(BookInfoClient bookInfoClient) {
		this.bookInfoClient = bookInfoClient;
	}
	
	// http://localhost:8002/depo/101
	@GetMapping("/{bookId}")
	public DepoResponse getDepoInfo(@PathVariable String bookId) {
		Book book = bookInfoClient.getBookById(bookId);
		return new DepoResponse("DEPO - "+bookId,"Banglore Warehouse",book);
	}
	
	
	public static class DepoResponse{
		
		private String depoID;
        private String depoAddress;
        private Book book;
        
        public DepoResponse() {}
        
        public DepoResponse(String depoID, String depoAddress, Book book) {
			this.depoID=depoID;
			this.depoAddress=depoAddress;
			this.book=book;
		}

		public String getDepoID() {
			return depoID;
		}

		public void setDepoID(String depoID) {
			this.depoID = depoID;
		}

		public String getDepoAddress() {
			return depoAddress;
		}

		public void setDepoAddress(String depoAddress) {
			this.depoAddress = depoAddress;
		}

		public Book getBook() {
			return book;
		}

		public void setBook(Book book) {
			this.book = book;
		}
        
        
	}
	
}
