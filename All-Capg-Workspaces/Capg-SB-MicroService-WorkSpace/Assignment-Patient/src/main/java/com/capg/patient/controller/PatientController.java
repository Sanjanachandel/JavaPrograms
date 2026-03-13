package com.capg.patient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.patient.model.Patient;

@RestController
@RequestMapping("/patient")
public class PatientController {
    /**
     * Returns product details by product id
     */
    @GetMapping("/{id}")
    public Patient getProduct(@PathVariable Long id) {
        // Dummy data (Normally from DB)
        return new Patient(id, "Piyush", 7890934);
    }
}

// http://localhost:9082/patient/101