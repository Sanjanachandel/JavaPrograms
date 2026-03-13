package com.capg.appointment.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.capg.appointment.model.Patient;


@FeignClient(name="PATIENT-SERVICE",url="${patient.service.url}")
public interface PatientClient {
	 @GetMapping("/patient/{id}")
	    Patient getPatient(@PathVariable ("id") Long id);
}
