package com.capg.rechargenova.security;

import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", 3600000L);
    }

    @Test
    void testTokenLifecycle() {
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER", 1L);
        assertNotNull(token);

        assertEquals("user@test.com", jwtUtil.extractUsername(token));
        assertEquals("ROLE_USER", jwtUtil.extractRole(token));
        assertEquals(1L, jwtUtil.extractUserId(token));
        
        assertTrue(jwtUtil.validateToken(token, "user@test.com"));
        assertFalse(jwtUtil.validateToken(token, "wrong@test.com"));
    }

    @Test
    void testExpiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", -1000L);
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER", 1L);
        assertFalse(jwtUtil.validateToken(token, "user@test.com"));
    }

    @Test
    void testValidateToken_ExpiredBranch() {
        JwtUtil spyUtil = Mockito.spy(jwtUtil);
        String token = jwtUtil.generateToken("user@test.com", "ROLE_USER", 1L);
        
        // Mock extractExpiration to return a date in the past
        Mockito.doReturn(new java.util.Date(System.currentTimeMillis() - 10000))
               .when(spyUtil).extractExpiration(token);
               
        assertFalse(spyUtil.validateToken(token, "user@test.com"));
    }
}
