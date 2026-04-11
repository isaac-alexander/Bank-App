package com.alexander.banking_app.service;

import com.alexander.banking_app.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(Long userId, String type); // create account

    List<Account> getUserAccounts(Long userId); // get user accounts

}