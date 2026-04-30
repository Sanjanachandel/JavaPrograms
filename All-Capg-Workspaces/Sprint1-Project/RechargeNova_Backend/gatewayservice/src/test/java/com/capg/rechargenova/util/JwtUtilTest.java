package com.capg.rechargenova.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", 3600000L);
    }

    @Test
    void testGenerateAndParseToken() {
        String token = jwtUtil.generateToken("test@example.com", "ROLE_USER", 1L);
        assertNotNull(token);
        assertEquals("test@example.com", jwtUtil.extractUsername(token));
        assertEquals("ROLE_USER", jwtUtil.extractRole(token));
        assertEquals(1L, jwtUtil.extractUserId(token));
        assertTrue(jwtUtil.validateToken(token));
        assertTrue(jwtUtil.validateToken(token, "test@example.com"));
    }

    @Test
    void testInvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid-token"));
    }

    @Test
    void testValidateToken_MismatchedEmail() {
        String token = jwtUtil.generateToken("test@example.com", "ROLE_USER", 1L);
        assertFalse(jwtUtil.validateToken(token, "wrong@example.com"));
    }

    @Test
    void testExpiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "expirationTime", -10000L);
        String token = jwtUtil.generateToken("test@example.com", "ROLE_USER", 1L);
        assertFalse(jwtUtil.validateToken(token));
        assertFalse(jwtUtil.validateToken(token, "test@example.com"));
    }
}
