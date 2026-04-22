package com.capg.rechargenova.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {
    @Test
    void testBeans() throws Exception {
        SecurityConfig config = new SecurityConfig();
        CustomUserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        ReflectionTestUtils.setField(config, "userDetailsService", userDetailsService);

        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);

        assertNotNull(config.authenticationProvider());

        AuthenticationConfiguration authConfig = Mockito.mock(AuthenticationConfiguration.class);
        org.springframework.security.authentication.AuthenticationManager am = Mockito.mock(org.springframework.security.authentication.AuthenticationManager.class);
        Mockito.when(authConfig.getAuthenticationManager()).thenReturn(am);
        assertNotNull(config.authenticationManager(authConfig));
    }
}
