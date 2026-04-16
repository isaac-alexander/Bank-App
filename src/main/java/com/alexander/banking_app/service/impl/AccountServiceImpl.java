package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import com.alexander.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // get single account for a user
    @Override
    public Account getUserAccount(Long userId) {

        // fetch user first
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        // get account using user object
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("account not found"));
    }

    // save account during registration
    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}