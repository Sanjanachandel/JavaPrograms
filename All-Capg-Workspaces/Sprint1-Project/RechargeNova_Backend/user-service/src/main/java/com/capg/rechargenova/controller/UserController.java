package com.capg.rechargenova.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capg.rechargenova.dto.AuthResponse;
import com.capg.rechargenova.dto.LoginRequest;
import com.capg.rechargenova.dto.UserRegistrationRequest;
import com.capg.rechargenova.dto.UserResponse;
import com.capg.rechargenova.service.UserService;

import jakarta.validation.Valid;
/*
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: UserController
 * DESCRIPTION:
 * Manages all user-related API requests such as registration,
 * authentication.
 * Integrates with UserService to handle business logic and
 * ensures proper request validation and response handling.
 * ================================================================
 */
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

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Admin API called to list all users (Page: {}, Size: {})", page, size);
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(userService.countUsers());
    }
    
    @PutMapping("/profile/picture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(

            @RequestPart("picture") MultipartFile picture,

            @RequestHeader("X-User-Id") Long userId
    ) throws IOException {

        logger.info("User [{}] uploading profile picture", userId);

        String url = userService.updateProfilePicture(userId, picture);

        logger.info("Profile picture updated for user [{}]", userId);

        return new ResponseEntity<>(Map.of("profilePictureUrl", url), HttpStatus.OK);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody com.capg.rechargenova.dto.ForgotPasswordRequest request) {
        logger.info("Forgot password API called for email: {}", request.getEmail());
        userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody com.capg.rechargenova.dto.ResetPasswordRequest request) {
        logger.info("Reset password API called for email: {}", request.getEmail());
        userService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
}