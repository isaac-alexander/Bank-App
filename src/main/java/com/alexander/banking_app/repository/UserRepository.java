package com.alexander.banking_app.repository;

import com.alexander.banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username); // find user by username

}