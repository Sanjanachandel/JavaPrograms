package com.capg.rechargenova.controller;

import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capg.rechargenova.dto.AuthResponse;
import com.capg.rechargenova.dto.LoginRequest;
import com.capg.rechargenova.dto.UserRegistrationRequest;
import com.capg.rechargenova.dto.UserResponse;
import com.capg.rechargenova.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * ================================================================
     * METHOD: register
     * DESCRIPTION:
     * Registers a new user in the system. Validates request and returns
     * created user details with HTTP status 201 (CREATED).
     * ================================================================
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {

        logger.info("Register API called for email: {}", request.getEmail());

        UserResponse response = userService.registerUser(request);

        logger.info("User registered successfully with ID: {}", response.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * ================================================================
     * METHOD: login
     * DESCRIPTION:
     * Authenticates a user and generates a JWT token. Returns AuthResponse
     * with token and user details.
     * ================================================================
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        logger.info("Login API called for email: {}", request.getEmail());

        AuthResponse response = userService.loginUser(request);

        logger.info("Login successful for email: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }

    /**
     * ================================================================
     * METHOD: getUser
     * DESCRIPTION:
     * Fetches user details by user ID. Returns UserResponse with user information.
     * ================================================================
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        logger.info("Get User API called for ID: {}", id);

        UserResponse response = userService.getUserById(id);

        logger.info("User fetched successfully for ID: {}", id);

        return ResponseEntity.ok(response);
    }
}