package com.capg.rechargenova.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import com.capg.rechargenova.dto.AuthResponse;
import com.capg.rechargenova.dto.LoginRequest;
import com.capg.rechargenova.dto.UserRegistrationRequest;
import com.capg.rechargenova.dto.UserResponse;
import com.capg.rechargenova.entity.User;
import com.capg.rechargenova.repository.UserRepository;
import com.capg.rechargenova.security.JwtUtil;
import com.capg.rechargenova.util.CloudinaryUtil;

@Service
/**
 * ================================================================
 * AUTHOR: Sanjana
 * CLASS: UserService
 * DESCRIPTION: Handles user registration, login, and user retrieval.
 * ================================================================
 */
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CloudinaryUtil cloudinaryUtil;

    @Value("${admin.credentials.email}")
    private String adminEmail;

    @Value("${admin.credentials.password}")
    private String adminPassword;

    /**
     * ================================================================
     * METHOD: registerUser
     * DESCRIPTION:
     * Registers a new user. Validates that the email does not exist,
     * saves the user with encoded password, and returns UserResponse.
     * ================================================================
     */
    public UserResponse registerUser(UserRegistrationRequest request) {

        logger.info("Registering user: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            logger.error("Phone number already exists: {}", request.getPhoneNumber());
            throw new RuntimeException("Phone number already is register");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole("ROLE_USER");

        user = userRepository.save(user);

        logger.info("User registered successfully with ID: {}", user.getId());

        return mapToUserResponse(user);
    }

    /**
     * ================================================================
     * METHOD: loginUser
     * DESCRIPTION:
     * Authenticates user credentials and generates JWT token.
     * Returns AuthResponse containing token and user details.
     * ================================================================
     */
    public AuthResponse loginUser(LoginRequest request) {

        logger.info("Login attempt for: {}", request.getEmail());

        // Admin login check
        if (adminEmail.equals(request.getEmail()) && adminPassword.equals(request.getPassword())) {
            String token = jwtUtil.generateToken(adminEmail, "ROLE_ADMIN", 0L);
            UserResponse adminResponse = new UserResponse(0L, "Admin", adminEmail, "ROLE_ADMIN", "0000000000", null);
            logger.info("Admin login successful");
            return new AuthResponse(token, adminResponse);
        }

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        if (authenticate.isAuthenticated()) {

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole(),
                    user.getId()
            );

            logger.info("Login successful for: {}", user.getEmail());

            return new AuthResponse(token, mapToUserResponse(user));

        } else {
            logger.error("Invalid login attempt: {}", request.getEmail());
            throw new RuntimeException("Invalid Access");
        }
    }

    /**
     * ================================================================
     * METHOD: getUserById
     * DESCRIPTION:
     * Fetches user details by user ID and returns UserResponse.
     * ================================================================
     */
    public UserResponse getUserById(Long id) {

        logger.info("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", id);
                    return new RuntimeException("User not found");
                });

        return mapToUserResponse(user);
    }

    /**
     * ================================================================
     * METHOD: mapToUserResponse
     * DESCRIPTION:
     * Maps a User entity to UserResponse DTO.
     * ================================================================
     */
    private UserResponse mapToUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getCreatedAt()
        );
    }
    
    
    public String updateProfilePicture(Long userId, MultipartFile picture) throws IOException {

        logger.info("Updating profile picture for user: {}", userId);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            logger.warn("User not found: {}", userId);
            throw new RuntimeException("User not found");
        }

        String url = cloudinaryUtil.uploadProfilePicture(picture);
        user.setProfilePictureUrl(url);
        userRepository.save(user);

        logger.info("Profile picture updated for user: {}", userId);

        return url;
    }
}