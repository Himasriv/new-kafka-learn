package com.example.security.service;

import com.example.security.model.UserAuthEntity;
import com.example.security.repository.UserAuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthEntityService implements UserDetailsService {
    private UserAuthRepository userAuthRepository;

    public UserAuthEntityService(UserAuthRepository   userAuthRepository){
        this.userAuthRepository = userAuthRepository;
    }

    public UserDetails saveUser(UserAuthEntity userAuthEntity){
        return userAuthRepository.save(userAuthEntity);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAuthRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
