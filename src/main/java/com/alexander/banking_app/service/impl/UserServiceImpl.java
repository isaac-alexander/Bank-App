package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.dto.UserDto;
import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.UserService;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public User registerUser(UserDto userDto) {

        // map dto to entity
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAddress(userDto.getAddress());

        user.setRole("CUSTOMER");

        // save user first
        User savedUser = userRepository.save(user);

        // create account immediately after registration
        Account account = new Account();

        // account number generation using phone
        String phone = savedUser.getPhoneNumber();
        String accountNumberString = phone.substring(1);

        account.setAccountNumber(Long.parseLong(accountNumberString));
        account.setAccountType(userDto.getAccountType().toUpperCase());
        account.setBalance(1000);
        account.setUser(savedUser);


        accountRepository.save(account);

        // return user
        return savedUser;
    }
    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("user not found");
        }

        User user = optionalUser.get();

        // update
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setAddress(userDto.getAddress());


        return userRepository.save(user);
    }
}