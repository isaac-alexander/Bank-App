package com.alexander.banking_app.service;

import com.alexander.banking_app.entity.Transaction;

import java.util.List;

public interface TransactionService {

    // get history for one account
    List<Transaction> getHistory(Long accountId);

    // get all transactions (admin)
    List<Transaction> getAllTransactions();

    // deposit money
    void deposit(Long accountId, double amount);

    // withdraw money
    void withdraw(Long accountId, double amount);

    // transfer money
    void transfer(Long fromAccountId, Long toAccountNumber, double amount);
}