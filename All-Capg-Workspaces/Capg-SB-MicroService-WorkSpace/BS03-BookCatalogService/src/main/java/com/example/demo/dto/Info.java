package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {

    @JsonProperty("bId")
    private int bookId;

    @JsonProperty("bName")
    private String bookName;

    public Info() {
    }

    public Info(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}