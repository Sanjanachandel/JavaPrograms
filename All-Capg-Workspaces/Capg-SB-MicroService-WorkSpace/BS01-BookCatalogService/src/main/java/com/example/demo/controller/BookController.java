package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Book;

@RestController
@RequestMapping("/cato")
public class BookController {

    @RequestMapping("/list")
    public Book getallBook() {
        return new Book(1001,"new Dream");
    }

}