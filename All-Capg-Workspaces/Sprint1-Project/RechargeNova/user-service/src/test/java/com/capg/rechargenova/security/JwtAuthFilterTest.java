package com.capg.rechargenova.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testShouldNotFilter() throws Exception {
        String[] publicPaths = {
            "/users/login", 
            "/users/register", 
            "/swagger-ui/index.html", 
            "/v3/api-docs/something", 
            "/swagger-ui.html", 
            "/actuator/health"
        };
        java.lang.reflect.Method method = JwtAuthFilter.class.getDeclaredMethod("shouldNotFilter", HttpServletRequest.class);
        method.setAccessible(true);
        
        for (String path : publicPaths) {
            when(request.getRequestURI()).thenReturn(path);
            assertTrue((Boolean) method.invoke(jwtAuthFilter, request));
        }

        when(request.getRequestURI()).thenReturn("/users/profile");
        assertFalse((Boolean) method.invoke(jwtAuthFilter, request));
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.extractUsername("validToken")).thenReturn("test@test.com");
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        when(jwtUtil.validateToken("validToken", "test@test.com")).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(new java.util.ArrayList<>());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_InvalidTokenFormat() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_UsernameNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.extractUsername("token")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_AlreadyAuthenticated() throws ServletException, IOException {
        // Set up authentication in context
        SecurityContextHolder.getContext().setAuthentication(mock(org.springframework.security.core.Authentication.class));
        
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtUtil.extractUsername("validToken")).thenReturn("test@test.com");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void testDoFilterInternal_TokenInvalid() throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtUtil.extractUsername("invalidToken")).thenReturn("test@test.com");
        when(userDetailsService.loadUserByUsername("test@test.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        when(jwtUtil.validateToken("invalidToken", "test@test.com")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
