package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Book;
import com.example.demo.dto.Info;
import com.example.demo.dto.Rating;

@RestController
@RequestMapping("/info")
public class InfoController {

    RestTemplate rest = new RestTemplate();

    @RequestMapping("/list/{bookId}")
    public List<Book> getAllData(@PathVariable("bookId") int id) {

        Rating rating = rest.getForObject(
                "http://localhost:9091/rating/list", Rating.class);

        Info info = rest.getForObject(
                "http://localhost:9092/cato/list", Info.class);

        return Collections.singletonList(
                new Book(info.getBookId(), info.getBookName(), rating.getBookRating())
        );
    }
}