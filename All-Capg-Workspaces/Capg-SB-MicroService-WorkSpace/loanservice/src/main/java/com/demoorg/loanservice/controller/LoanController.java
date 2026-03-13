package com.demoorg.loanservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class LoanController {
	List<String> allLoans = Stream.of("Loan1","Loan2","Loan3").collect(Collectors.toList());
	
	@GetMapping("/loans")
	public  List<String> getAllLoans() {
		return allLoans;
	}
}
