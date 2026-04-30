package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import com.capg.rechargenova.dto.AuthResponse;
import com.capg.rechargenova.dto.LoginRequest;
import com.capg.rechargenova.dto.UserRegistrationRequest;
import com.capg.rechargenova.dto.UserResponse;
import com.capg.rechargenova.entity.User;
import com.capg.rechargenova.exception.ResourceNotFoundException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoverageTest {

    @Test
    void testDTOs() {
        // AuthResponse
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("token");
        UserResponse ur = new UserResponse();
        authResponse.setUser(ur);
        assertEquals("token", authResponse.getToken());
        assertEquals(ur, authResponse.getUser());

        AuthResponse authResponse2 = new AuthResponse("token", ur);
        assertEquals("token", authResponse2.getToken());

        // LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email");
        loginRequest.setPassword("pass");
        assertEquals("email", loginRequest.getEmail());
        assertEquals("pass", loginRequest.getPassword());
        
        LoginRequest loginRequest2 = new LoginRequest("email", "pass");
        assertEquals("email", loginRequest2.getEmail());

        // UserRegistrationRequest
        UserRegistrationRequest urr = new UserRegistrationRequest();
        urr.setName("name");
        urr.setEmail("email");
        urr.setPassword("pass");
        urr.setPhoneNumber("123");
        assertEquals("name", urr.getName());
        assertEquals("email", urr.getEmail());
        assertEquals("pass", urr.getPassword());
        assertEquals("123", urr.getPhoneNumber());

        UserRegistrationRequest urr2 = new UserRegistrationRequest();
        urr2.setName("name");
        urr2.setEmail("email");
        urr2.setPassword("pass");
        urr2.setPhoneNumber("123");
        assertEquals("name", urr2.getName());

        // UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("name");
        userResponse.setEmail("email");
        userResponse.setRole("role");
        userResponse.setPhoneNumber("123");
        LocalDateTime now = LocalDateTime.now();
        userResponse.setCreatedAt(now);
        assertEquals(1L, userResponse.getId());
        assertEquals("name", userResponse.getName());
        assertEquals("email", userResponse.getEmail());
        assertEquals("role", userResponse.getRole());
        assertEquals("123", userResponse.getPhoneNumber());
        assertEquals(now, userResponse.getCreatedAt());

        UserResponse userResponse2 = new UserResponse(1L, "name", "email", "role", "123", null, now);
        assertEquals(1L, userResponse2.getId());

        userResponse2.setProfilePictureUrl("url");
        assertEquals("url", userResponse2.getProfilePictureUrl());

        UserResponse userResponse3 = new UserResponse(1L, "name", "email", "role", "123", "url", now);
        assertEquals("url", userResponse3.getProfilePictureUrl());

        // ForgotPasswordRequest
        com.capg.rechargenova.dto.ForgotPasswordRequest fpr = new com.capg.rechargenova.dto.ForgotPasswordRequest();
        fpr.setEmail("test");
        assertEquals("test", fpr.getEmail());

        // ResetPasswordRequest
        com.capg.rechargenova.dto.ResetPasswordRequest rpr = new com.capg.rechargenova.dto.ResetPasswordRequest();
        rpr.setEmail("test"); rpr.setOtp("123"); rpr.setNewPassword("pass");
        assertEquals("test", rpr.getEmail());
        assertEquals("123", rpr.getOtp());
        assertEquals("pass", rpr.getNewPassword());
    }

    @Test
    void testEntity() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email");
        user.setPassword("pass");
        user.setRole("role");
        user.setPhoneNumber("123");
        user.setProfilePictureUrl("url");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("name", user.getName());
        assertEquals("email", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertEquals("role", user.getRole());
        assertEquals("123", user.getPhoneNumber());
        assertEquals("url", user.getProfilePictureUrl());
        assertEquals(now, user.getCreatedAt());

        User user2 = new User(1L, "name", "email", "pass", "role", "123", null, now);
        assertEquals(1L, user2.getId());
    }

    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("message");
        assertEquals("message", ex.getMessage());
    }

    @Test
    void testMain() {
        // Just calling to get coverage on the application class
        assertNotNull(new UserServiceApplication());
    }
}
