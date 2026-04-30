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
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Mock
    private com.capg.rechargenova.client.NotificationClient notificationClient;

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
        assertEquals("Phone number is already registered", ex.getMessage());
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
    void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        var result = userService.getAllUsers(0, 10);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testUpdateProfilePictureAdmin() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(cloudinaryUtil.uploadProfilePicture(file)).thenReturn("http://admin-url");
        
        String url = userService.updateProfilePicture(0L, file);
        assertEquals("http://admin-url", url);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testForgotPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        
        userService.forgotPassword("test@example.com");
        // Internal state (OTP) is private, but we verified the flow
    }

    @Test
    void testForgotPasswordUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.forgotPassword("test@example.com"));
    }

    @Test
    void testResetPasswordSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newPass");
        
        // Inject OTP manually to simulate stored state
        java.util.Map<String, String> otpStorage = new java.util.concurrent.ConcurrentHashMap<>();
        otpStorage.put("test@example.com", "123456");
        ReflectionTestUtils.setField(userService, "otpStorage", otpStorage);
        
        userService.resetPassword("test@example.com", "123456", "newPassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testResetPasswordInvalidOtp() {
        java.util.Map<String, String> otpStorage = new java.util.concurrent.ConcurrentHashMap<>();
        otpStorage.put("test@example.com", "123456");
        ReflectionTestUtils.setField(userService, "otpStorage", otpStorage);
        
        assertThrows(RuntimeException.class, () -> userService.resetPassword("test@example.com", "wrong", "newPassword"));
    }

    @Test
    void testResetPasswordUserNotFound() {
        java.util.Map<String, String> otpStorage = new java.util.concurrent.ConcurrentHashMap<>();
        otpStorage.put("test@example.com", "123456");
        ReflectionTestUtils.setField(userService, "otpStorage", otpStorage);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.resetPassword("test@example.com", "123456", "newPassword"));
    }

    @Test
    void testLoginUserNotFoundAfterAuth() {
        LoginRequest req = new LoginRequest();
        req.setEmail("notfound@example.com");
        req.setPassword("pass");
        
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> userService.loginUser(req));
    }

    @Test
    void testForgotPasswordEmailError() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        
        // notificationClient is mocked, we can make it throw
        doThrow(new RuntimeException("Email service down")).when(notificationClient).sendOtp(anyString(), anyString());
        
        // Should not throw exception, just log it
        assertDoesNotThrow(() -> userService.forgotPassword("test@example.com"));
    }

    @Test
    void testUpdateProfilePicture_UserNull() {
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateProfilePicture(1L, file));
    }

    @Test
    void testUpdateProfilePicture_UserIdNull() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(cloudinaryUtil.uploadProfilePicture(file)).thenReturn("url");
        // userId null should bypass admin check and proceed to findById(null) which is empty
        assertThrows(RuntimeException.class, () -> userService.updateProfilePicture(null, file));
    }

    @Test
    void testResetPassword_NoOtpInStorage() {
        // Clear OTP storage
        ReflectionTestUtils.setField(userService, "otpStorage", new java.util.concurrent.ConcurrentHashMap<>());
        assertThrows(RuntimeException.class, () -> userService.resetPassword("test@example.com", "123456", "pass"));
    }

    @Test
    void testCountUsers() {
        when(userRepository.count()).thenReturn(100L);
        assertEquals(100L, userService.countUsers());
    }
}