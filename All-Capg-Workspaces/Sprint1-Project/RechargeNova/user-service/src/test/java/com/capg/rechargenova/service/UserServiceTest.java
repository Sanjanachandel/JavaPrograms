package com.capg.rechargenova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.capg.rechargenova.dto.AuthResponse;
import com.capg.rechargenova.dto.LoginRequest;
import com.capg.rechargenova.dto.UserRegistrationRequest;
import com.capg.rechargenova.dto.UserResponse;
import com.capg.rechargenova.entity.User;
import com.capg.rechargenova.repository.UserRepository;
import com.capg.rechargenova.security.JwtUtil;
import com.capg.rechargenova.util.CloudinaryUtil;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

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

    @Mock
    private CloudinaryUtil cloudinaryUtil;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userService, "adminEmail", "admin@gmail.com");
        ReflectionTestUtils.setField(userService, "adminPassword", "admin123");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        closeable.close();
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
        when(userRepository.findByPhoneNumber(request.getPhoneNumber())).thenReturn(Optional.empty());
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

    @Test
    void testLoginAdminSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("admin123");

        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("adminToken");

        AuthResponse response = userService.loginUser(request);

        assertNotNull(response);
        assertEquals("adminToken", response.getToken());
        assertEquals("ROLE_ADMIN", response.getUser().getRole());
    }

    @Test
    void testRegisterUserDuplicateEmail() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("john@example.com");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void testRegisterUserDuplicatePhoneNumber() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("new@example.com");
        request.setPhoneNumber("1234567890");

        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("Phone number already is register", ex.getMessage());
    }

    @Test
    void testLoginUserFail() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrong");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        assertThrows(RuntimeException.class, () -> userService.loginUser(request));
    }

    @Test
    void testGetUserByIdSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse res = userService.getUserById(1L);
        assertEquals("John", res.getName());
    }

    @Test
    void testGetUserByIdFail() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testUpdateProfilePictureSuccess() throws IOException {
        User user = new User();
        user.setId(1L);
        MultipartFile file = mock(MultipartFile.class);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cloudinaryUtil.uploadProfilePicture(file)).thenReturn("http://image.url");
        
        String url = userService.updateProfilePicture(1L, file);
        assertEquals("http://image.url", url);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateProfilePictureFail() {
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateProfilePicture(1L, file));
    }

    @Test
    void testAdminLoginPartialMatch() {
        LoginRequest req = new LoginRequest();
        req.setEmail("admin@gmail.com");
        req.setPassword("wrong");
        
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        
        assertThrows(RuntimeException.class, () -> userService.loginUser(req));
    }

    @Test
    void testLoginUserNotFoundAfterAuth() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.loginUser(request));
    }
}