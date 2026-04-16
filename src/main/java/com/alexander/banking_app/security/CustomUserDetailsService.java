package com.alexander.banking_app.security;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);

        if (user == null) { // if not found
            throw new UsernameNotFoundException("user not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // username
                user.getPassword(), // password
                Collections.singleton(() -> "ROLE_" + user.getRole()) // role
        );
    }
}