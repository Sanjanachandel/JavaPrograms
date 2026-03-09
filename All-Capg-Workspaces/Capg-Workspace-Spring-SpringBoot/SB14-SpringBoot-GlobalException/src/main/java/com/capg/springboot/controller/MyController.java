package com.capg.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.capg.springboot.exceptions.MyCustomException;

@RestController
public class MyController {
@GetMapping("/example/{bid}")
public ResponseEntity<String> example(@PathVariable("bid")boolean bid){
	boolean someCondition=bid;
	System.out.println(someCondition);
	//Stimulate a custom exception
	if(someCondition) {
		throw new MyCustomException("This ia custom exception");
	}
	return ResponseEntity.ok("Request is processing......");
}
}
