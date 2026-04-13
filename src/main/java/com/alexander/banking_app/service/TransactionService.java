package com.alexander.banking_app.service;

import com.alexander.banking_app.entity.Transaction;

import java.util.List;

public interface TransactionService {

    // add money to account (admin only)
    boolean deposit(Long accountId, double amount); // return true if success, false if account not found

    // remove money from account (admin only)
    boolean withdraw(Long accountId, double amount); // return false if fails

    // get all transactions for an account
    List<Transaction> getHistory(Long accountId); // transaction history

    // transfer between accounts
    boolean transfer(Long fromAccountId, Long toAccountNumber, double amount); // transfer money
}