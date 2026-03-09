package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.rating.Rating;

@RestController
@RequestMapping("/cato")
public class RatingController {
@RequestMapping("/list")
public Rating getallData()
{
	return new Rating(1001,4);
}
}
