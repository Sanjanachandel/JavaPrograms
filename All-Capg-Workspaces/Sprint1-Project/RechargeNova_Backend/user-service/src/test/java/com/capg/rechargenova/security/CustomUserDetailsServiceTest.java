package com.capg.rechargenova.security;

import com.capg.rechargenova.entity.User;
import com.capg.rechargenova.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService service;
    
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "adminEmail", "admin@gmail.com");
        org.springframework.test.util.ReflectionTestUtils.setField(service, "adminPassword", "admin123");
    }

    @Test
    void testLoadUserByUsername_Success() {
        User u = new User();
        u.setEmail("test");
        u.setPassword("pass");
        u.setRole("ROLE_USER");
        when(userRepository.findByEmail("test")).thenReturn(Optional.of(u));
        
        UserDetails ud = service.loadUserByUsername("test");
        assertNotNull(ud);
        assertEquals("test", ud.getUsername());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByEmail("test")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("test"));
    }

    @Test
    void testLoadUserByUsername_AdminSuccess() {
        UserDetails ud = service.loadUserByUsername("admin@gmail.com");
        assertNotNull(ud);
        assertEquals("admin@gmail.com", ud.getUsername());
        assertTrue(ud.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}
