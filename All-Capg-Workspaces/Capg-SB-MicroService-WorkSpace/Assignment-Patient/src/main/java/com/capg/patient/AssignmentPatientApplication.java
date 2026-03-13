package com.capg.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentPatientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentPatientApplication.class, args);
		System.out.println("Patient Service started");
	}

}
