package com.capg.rechargenova.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capg.rechargenova.entity.User;
import com.capg.rechargenova.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @org.springframework.beans.factory.annotation.Value("${admin.credentials.email:admin@gmail.com}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${admin.credentials.password:admin123}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminEmail.equals(username)) {
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(adminPassword);
            adminUser.setRole("ROLE_ADMIN");
            return new CustomUserDetails(adminUser);
        }

        Optional<User> user = userRepository.findByEmail(username);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with email :" + username));
    }
}
