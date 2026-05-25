package com.example.securitydemo.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public Map<String, String> profile(Principal principal) {
        return Map.of(
                "message", "Authenticated user endpoint",
                "username", principal.getName()
        );
    }
}

