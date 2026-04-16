package com.alexander.banking_app.repository;

import com.alexander.banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // find user by username
    Optional<User> findByUsername(String username);
}