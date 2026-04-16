package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDto userDto) {

        // create new user object
        User user = new User();

        user.setUsername(userDto.getUsername());

        // encrypt password before saving
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAddress(userDto.getAddress());

        // default role
        user.setRole("CUSTOMER");

        // save user to database
        User savedUser = userRepository.save(user);

        // create account immediately after user is saved
        Account account = new Account();

        // generate account number from phone number
        String phone = savedUser.getPhoneNumber();
        String accountNumberString = phone.substring(1);

        account.setAccountNumber(Long.parseLong(accountNumberString));

        // set account details
        account.setAccountType(userDto.getAccountType().toUpperCase());
        account.setBalance(1000);
        account.setUser(savedUser);

        // save account
        accountRepository.save(account);

        // return saved user
        return savedUser;
    }

    @Override
    public Optional<User> findByUsername(String username) {

        // directly return optional from repository
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {

        // find user by id
        Optional<User> optionalUser = userRepository.findById(userId);

        // check if user exists
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("user not found");
        }

        // get actual user
        User user = optionalUser.get();

        // update fields
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAddress(userDto.getAddress());

        // save updated user
        return userRepository.save(user);
    }
}