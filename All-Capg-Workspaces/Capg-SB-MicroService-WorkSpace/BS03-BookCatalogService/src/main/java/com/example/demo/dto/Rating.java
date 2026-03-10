package com.example.demo.dto;

public class Rating {

	private int bookId;
	private int bookRating;

	public Rating() {
	}

	public Rating(int bookId, int bookRating) {
		this.bookId = bookId;
		this.bookRating = bookRating;
	}

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
}