package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    // constructor injection (cleaner than @Autowired)
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // get single account for a user
    @Override
    public Account getUserAccount(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    // save account (used when user registers)
    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}