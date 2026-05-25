package com.example.security.repository;

import com.example.security.model.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity,Long> {

    public Optional<UserAuthEntity> findByUsername(String username);
}
