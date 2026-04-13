package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.TransactionRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean deposit(Long accountId, double amount) {

        // find account
        Account account = accountRepository.findById(accountId).orElse(null);

        // check if account exists
        if (account == null) {
            return false;
        }

        // add amount
        account.setBalance(account.getBalance() + amount);

        // save account
        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean withdraw(Long accountId, double amount) {

        // find account
        Account account = accountRepository.findById(accountId).orElse(null);

        // check if account exists
        if (account == null) {
            return false;
        }

        // check balance
        if (account.getBalance() < amount) {
            return false;
        }

        // subtract amount
        account.setBalance(account.getBalance() - amount);

        // save account
        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean transfer(Long fromAccountId, Long toAccountNumber, double amount) {

        // sender account
        Account sender = accountRepository.findById(fromAccountId).orElse(null);

        // receiver account
        Account receiver = null;

        // find receiver by account number
        for (Account acc : accountRepository.findAll()) {

            if (acc.getAccountNumber() != null &&
                    acc.getAccountNumber().equals(toAccountNumber)) {

                receiver = acc;
                break;
            }
        }

        // validate accounts
        if (sender == null || receiver == null) {
            return false;
        }

        // check balance
        if (sender.getBalance() < amount) {
            return false;
        }

        // debit sender
        sender.setBalance(sender.getBalance() - amount);

        // credit receiver
        receiver.setBalance(receiver.getBalance() + amount);

        // save both accounts
        accountRepository.save(sender);
        accountRepository.save(receiver);

        return true;
    }

    @Override
    public List<Transaction> getHistory(Long accountId) {

        // return all transactions for account
        return transactionRepository.findByAccountId(accountId);
    }
}