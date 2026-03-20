package com.capg.springboot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.capg.springboot.entity.Address;
import com.capg.springboot.repository.AddressRepository;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressRepository repo;

    public AddressController(AddressRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Address save(@RequestBody Address c) {
        return repo.save(c);
    }

    @GetMapping
    public List<Address> getAll() {
        return repo.findAll();
    }
}