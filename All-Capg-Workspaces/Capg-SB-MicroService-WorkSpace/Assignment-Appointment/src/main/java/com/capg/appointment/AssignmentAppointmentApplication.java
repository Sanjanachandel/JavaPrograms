package com.capg.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.capg.appointment.controller")
public class AssignmentAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentAppointmentApplication.class, args);
		System.out.println("Appointment System server started");
	}

}
