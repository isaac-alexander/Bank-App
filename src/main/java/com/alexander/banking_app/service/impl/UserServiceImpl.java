package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; // used to save and fetch user from database

    @Override
    public User registerUser(User user) {

        // check if phone number is null OR not equal to 11 digits
        if (user.getPhoneNumber() == null || user.getPhoneNumber().length() != 11) {

            // throw error if invalid phone number
            throw new RuntimeException("phone number must be 11 digits e.g 08012345678");
        }

        user.setRole("CUSTOMER"); // set default role

        return userRepository.save(user); // save user to database
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username); // find user by username
    }
}