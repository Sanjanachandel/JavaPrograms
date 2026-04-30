package com.capg.rechargenova.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void testShouldNotFilter() {
        when(request.getRequestURI()).thenReturn("/users/login");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        when(request.getRequestURI()).thenReturn("/users/register");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        when(request.getRequestURI()).thenReturn("/v3/api-docs");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        when(request.getRequestURI()).thenReturn("/actuator/health");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        when(request.getRequestURI()).thenReturn("/users/profile");
        assertFalse(jwtAuthFilter.shouldNotFilter(request));
    }

    @Test
    void testDoFilterInternal_NoHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testDoFilterInternal_InvalidHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtil.extractUsername("valid-token")).thenReturn("user@gmail.com");
        
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@gmail.com");
        when(userDetailsService.loadUserByUsername("user@gmail.com")).thenReturn(userDetails);
        when(jwtUtil.validateToken("valid-token", "user@gmail.com")).thenReturn(true);
        
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_AlreadyAuthenticated() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtil.extractUsername("valid-token")).thenReturn("user@gmail.com");
        
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(mock(org.springframework.security.core.Authentication.class));
        
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtUtil.extractUsername("invalid-token")).thenReturn("user@gmail.com");
        
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@gmail.com");
        when(userDetailsService.loadUserByUsername("user@gmail.com")).thenReturn(userDetails);
        when(jwtUtil.validateToken("invalid-token", "user@gmail.com")).thenReturn(false);
        
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        
        verify(filterChain).doFilter(request, response);
    }
}
