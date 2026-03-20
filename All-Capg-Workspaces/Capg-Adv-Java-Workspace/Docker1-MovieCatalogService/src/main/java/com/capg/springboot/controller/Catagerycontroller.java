package com.capg.springboot.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.capg.springboot.dto.*;

@RestController 
@RequestMapping("/cato")
public class Catagerycontroller {
	
	//http://localhost:9093/cato/list
	@RequestMapping("/list")
	public Movie geetAllData() {
		
		return new Movie(1001,"RRR");
	}

}
