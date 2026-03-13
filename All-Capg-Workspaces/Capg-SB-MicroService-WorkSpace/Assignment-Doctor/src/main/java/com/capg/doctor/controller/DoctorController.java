package com.capg.doctor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    /**
     * Returns true if product is in stock
     */
    @GetMapping("/check/{doctorId}")
    public boolean checkStock(@PathVariable Long doctorId) {

        // Dummy logic: only productId 101 has stock
        return doctorId == 101;
    }
}
// http://localhost:9083/doctor/check/101
// true