package com.capg.rechargenova.security;

import com.capg.rechargenova.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {
    @Test
    void testCustomUserDetails() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("pass");
        user.setRole("ROLE_USER");
        
        CustomUserDetails details = new CustomUserDetails(user);
        
        Collection<? extends GrantedAuthority> auths = details.getAuthorities();
        assertNotNull(auths);
        assertEquals(1, auths.size());
        assertEquals("ROLE_USER", auths.iterator().next().getAuthority());
        
        assertEquals("pass", details.getPassword());
        assertEquals("test@test.com", details.getUsername());
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}
