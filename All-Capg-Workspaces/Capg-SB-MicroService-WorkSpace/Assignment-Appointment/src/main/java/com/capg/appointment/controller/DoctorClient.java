package com.capg.appointment.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="DOCTOR-SERVICE", url="${doctor.service.url}")
public interface DoctorClient {
	@GetMapping("/doctor/check/{doctorId}")
    boolean isDoctorAvailable(@PathVariable("doctorId") Long doctorId);
}
