package com.alexander.banking_app.service;

import com.alexander.banking_app.entity.Transaction;

import java.util.List;

public interface TransactionService {

    void deposit(Long accountId, double amount); // add money

    boolean withdraw(Long accountId, double amount); // remove money

    List<Transaction> getHistory(Long accountId); // history

}
