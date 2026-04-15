package com.alexander.banking_app.service;

import com.alexander.banking_app.entity.Account;

public interface AccountService {

    // get single account for a user
    Account getUserAccount(Long userId);

    // save account
    Account saveAccount(Account account);

}