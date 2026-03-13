package com.capg.appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.appointment.model.Patient;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
	@Autowired
	private DoctorClient doctorClient;
	
	@Autowired
	private PatientClient patientClient;
	
	@GetMapping("/book/{doctorId}")
	public String bookAppointment(@PathVariable long doctorId) {
		Patient patient=patientClient.getPatient(doctorId);
		boolean isDoctorAvailable=doctorClient.isDoctorAvailable(doctorId);
		if(!isDoctorAvailable) {
			return "No Doctor is available for Appointment";
		} else {
			return "Appointment succesfully booked for "+patient.getName();
		}
	}
}

// http://localhost:9091/appointment/book/101

//http://localhost:8989/doctor/check/101
//http://localhost:8989/patient/101
//http://localhost:8989/appointment/book/101