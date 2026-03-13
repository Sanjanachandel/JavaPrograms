package com.demoorg.demataccountservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DematAccountController {
	
	List<String> allDematAccounts = Stream.of("Demat1","Demat2","Demat3").collect(Collectors.toList());
	
	@GetMapping("/demataccounts")
	public List<String> getAllDematAccounts(){
		return allDematAccounts;
	}
	
}
