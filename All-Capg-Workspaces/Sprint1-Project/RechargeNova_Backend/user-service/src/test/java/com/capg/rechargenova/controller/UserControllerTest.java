package com.capg.rechargenova.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.service.UserService;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testRegister() {
        UserRegistrationRequest req = new UserRegistrationRequest();
        UserResponse mockResp = new UserResponse();
        when(userService.registerUser(req)).thenReturn(mockResp);
        
        ResponseEntity<UserResponse> res = userController.register(req);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertEquals(mockResp, res.getBody());
    }

    @Test
    void testLogin() {
        LoginRequest req = new LoginRequest();
        AuthResponse mockResp = new AuthResponse();
        when(userService.loginUser(req)).thenReturn(mockResp);
        
        ResponseEntity<AuthResponse> res = userController.login(req);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(mockResp, res.getBody());
    }

    @Test
    void testGetUser() {
        UserResponse mockResp = new UserResponse();
        when(userService.getUserById(1L)).thenReturn(mockResp);
        
        ResponseEntity<UserResponse> res = userController.getUser(1L);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(mockResp, res.getBody());
    }

    @Test
    void testUploadProfilePicture() throws IOException {
        MockMultipartFile file = new MockMultipartFile("picture", "test.jpg", "image/jpeg", "image data".getBytes());
        when(userService.updateProfilePicture(1L, file)).thenReturn("http://url");
        
        ResponseEntity<Map<String, String>> res = userController.uploadProfilePicture(file, 1L);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        Map<String, String> body = res.getBody();
        org.junit.jupiter.api.Assertions.assertNotNull(body);
        assertEquals("http://url", body.get("profilePictureUrl"));
    }

    @Test
    void testGetAllUsers() {
        Page<UserResponse> page = new PageImpl<>(List.of(new UserResponse()));
        when(userService.getAllUsers(0, 10)).thenReturn(page);
        ResponseEntity<Page<UserResponse>> res = userController.getAllUsers(0, 10);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        Page<UserResponse> body = res.getBody();
        org.junit.jupiter.api.Assertions.assertNotNull(body);
        assertEquals(1, body.getContent().size());
    }

    @Test
    void testForgotPassword() {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("test@example.com");
        var res = userController.forgotPassword(req);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(userService, times(1)).forgotPassword("test@example.com");
    }

    @Test
    void testResetPassword() {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setEmail("test@example.com");
        req.setOtp("123456");
        req.setNewPassword("pass");
        var res = userController.resetPassword(req);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(userService, times(1)).resetPassword("test@example.com", "123456", "pass");
    }

    @Test
    void testCountUsers() {
        when(userService.countUsers()).thenReturn(10L);
        var res = userController.countUsers();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(10L, res.getBody());
    }
}
