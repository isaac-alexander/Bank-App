package com.alexander.banking_app.service;

import com.alexander.banking_app.entity.User;

public interface UserService {

    User registerUser(User user); // register new user

    User findByUsername(String username); // find user for login

}
