package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import com.capg.rechargenova.dto.*;
import com.capg.rechargenova.entity.*;
import com.capg.rechargenova.exception.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PojoTest {

    @Test
    void testAuthResponse() {
        UserResponse ur = new UserResponse(1L, "a", "b", "c", "d", null);
        AuthResponse ar = new AuthResponse();
        ar.setToken("token");
        ar.setUser(ur);
        assertEquals("token", ar.getToken());
        assertNotNull(ar.getUser());
        assertNotNull(new AuthResponse("t", ur));
    }

    @Test
    void testLoginRequest() {
        LoginRequest lr = new LoginRequest();
        lr.setEmail("e");
        lr.setPassword("p");
        assertEquals("e", lr.getEmail());
        assertEquals("p", lr.getPassword());
    }

    @Test
    void testUserRegistrationRequest() {
        UserRegistrationRequest ur = new UserRegistrationRequest();
        ur.setEmail("e");
        ur.setName("n");
        ur.setPassword("p");
        ur.setPhoneNumber("ph");
        assertEquals("e", ur.getEmail());
        assertEquals("n", ur.getName());
        assertEquals("p", ur.getPassword());
        assertEquals("ph", ur.getPhoneNumber());
    }

    @Test
    void testUserResponse() {
        UserResponse ur = new UserResponse();
        ur.setId(1L);
        ur.setName("n");
        ur.setEmail("e");
        ur.setRole("r");
        ur.setPhoneNumber("ph");
        ur.setCreatedAt(null);
        assertEquals(1L, ur.getId());
        assertEquals("n", ur.getName());
        assertEquals("e", ur.getEmail());
        assertEquals("r", ur.getRole());
        assertEquals("ph", ur.getPhoneNumber());
        assertNull(ur.getCreatedAt());
    }

    @Test
    void testUser() {
        LocalDateTime now = LocalDateTime.now();
        User u1 = new User(1L, "n", "e", "p", "r", "ph", now);
        assertEquals(1L, u1.getId());
        assertEquals("n", u1.getName());
        assertEquals("e", u1.getEmail());
        assertEquals("p", u1.getPassword());
        assertEquals("r", u1.getRole());
        assertEquals("ph", u1.getPhoneNumber());
        assertEquals(now, u1.getCreatedAt());

        User u = new User();
        u.setId(1L);
        u.setName("n");
        u.setEmail("e");
        u.setPassword("p");
        u.setPhoneNumber("ph");
        u.setRole("r");
        u.setProfilePictureUrl("url");
        u.setCreatedAt(now);
        assertEquals(1L, u.getId());
        assertEquals("n", u.getName());
        assertEquals("e", u.getEmail());
        assertEquals("p", u.getPassword());
        assertEquals("ph", u.getPhoneNumber());
        assertEquals("r", u.getRole());
        assertEquals("url", u.getProfilePictureUrl());
        assertEquals(now, u.getCreatedAt());

        java.lang.reflect.Method onCreate;
        try {
            onCreate = User.class.getDeclaredMethod("onCreate");
            onCreate.setAccessible(true);
            onCreate.invoke(u);
            assertNotNull(u.getCreatedAt());
        } catch (Exception e) {
            fail("Reflection failed");
        }
    }
    
    @Test
    void testExceptions() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        assertEquals("Not found", ex.getMessage());
        
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        assertNotNull(handler);
    }
}
