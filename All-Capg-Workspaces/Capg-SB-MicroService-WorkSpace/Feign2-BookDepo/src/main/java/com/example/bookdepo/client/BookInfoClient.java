package com.example.bookdepo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.bookdepo.model.Book;

@FeignClient(name = "bookInfoClient", url = "${bookinfo.service.url}")
public interface BookInfoClient {
	@GetMapping("/books/{bookId}")
	Book getBookById(@PathVariable String bookId);
}
