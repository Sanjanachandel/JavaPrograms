package com.capg.rechargenova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.capg.rechargenova.dto.AuthResponse;
import com.capg.rechargenova.dto.LoginRequest;
import com.capg.rechargenova.dto.UserRegistrationRequest;
import com.capg.rechargenova.dto.UserResponse;
import com.capg.rechargenova.entity.User;
import com.capg.rechargenova.repository.UserRepository;
import com.capg.rechargenova.security.JwtUtil;
import com.capg.rechargenova.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");
        user.setPhoneNumber("1234567890");
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
    }

    @Test
    void testLoginUserSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        user.setRole("ROLE_USER");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId()))
                .thenReturn("jwtToken");

        AuthResponse response = userService.loginUser(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
    }
}