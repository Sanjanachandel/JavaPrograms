package com.capg.springboot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.capg.springboot.entity.Employee;
import com.capg.springboot.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repo;

    public EmployeeController(EmployeeRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Employee save(@RequestBody Employee c) {
        return repo.save(c);
    }

    @GetMapping
    public List<Employee> getAll() {
        return repo.findAll();
    }
}