package com.alexander.banking_app.service;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.User;

public interface UserService {

    User registerUser(UserDto userDto);

    User findByUsername(String username);

    User updateUser(Long userId, UserDto userDto);
}