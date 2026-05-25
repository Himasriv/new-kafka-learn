package com.example.securitydemo.config;

import com.example.securitydemo.user.AppUser;
import com.example.securitydemo.user.Role;
import com.example.securitydemo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(new AppUser("admin", passwordEncoder.encode("admin123"), Role.ADMIN));
            }
        };
    }
}

