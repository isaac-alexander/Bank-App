package com.alexander.banking_app.service;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.User;

import java.util.Optional;

public interface UserService {

    // register new user
    User registerUser(UserDto userDto);

    // get user by username using optional
    Optional<User> findByUsername(String username);

    // update user details
    User updateUser(Long userId, UserDto userDto);
}