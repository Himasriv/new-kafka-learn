package com.example.security.controller;

import com.example.security.model.UserAuthEntity;
import com.example.security.service.UserAuthEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private UserAuthEntityService userAuthEntityService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserAuthEntityService userAuthEntityService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userAuthEntityService = userAuthEntityService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserAuthEntity userAuthEntity){
        String password = bCryptPasswordEncoder.encode(userAuthEntity.getPassword());
        userAuthEntity.setPassword(password);
        userAuthEntityService.saveUser(userAuthEntity);
        return new ResponseEntity<>("User registered successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDetails> getUser(){
        String username="uname";
        return new ResponseEntity<>(userAuthEntityService.loadUserByUsername(username),HttpStatus.OK);
    }
}
