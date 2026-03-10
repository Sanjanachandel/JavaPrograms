package com.example.demo.rating;

public class Rating {
	private int bookId;
	private int bookRating;
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getBookRating() {
		return bookRating;
	}
	public void setBookRating(int bookRating) {
		this.bookRating = bookRating;
	}
	public Rating(int bookId, int bookRating) {
		super();
		this.bookId = bookId;
		this.bookRating = bookRating;
	}

	

}
