package com.alexander.banking_app.security;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // get user as optional
        Optional<User> optionalUser = userService.findByUsername(username);

        // if not found throw error
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }

        // get actual user
        User user = optionalUser.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // username
                user.getPassword(), // encrypted password
                Collections.singleton(() -> "ROLE_" + user.getRole()) // role
        );
    }
}