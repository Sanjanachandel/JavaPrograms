package com.capg.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentDoctorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentDoctorApplication.class, args);
		System.out.println("Doctor service started");
	}

}
