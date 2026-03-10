package com.example.demo.dto;


public class Book {
private int bookId;
private String bookName;
public Book(int bookId, String bookName) {
	super();
	this.bookId = bookId;
	this.bookName = bookName;
}
public int getbId() {
	return bookId;
}
public void setbId(int bookId) {
	this.bookId = bookId;
}
public String getbName() {
	return bookName;
}
public void setbName(String bookName) {
	this.bookName = bookName;
}




}
