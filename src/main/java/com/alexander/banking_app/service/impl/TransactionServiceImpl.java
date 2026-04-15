package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.TransactionRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean deposit(Long accountId, double amount) {

        // fetch account safely
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            return false;
        }

        Account account = accountOptional.get();

        account.setBalance(account.getBalance() + amount);

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean withdraw(Long accountId, double amount) {

        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            return false;
        }

        Account account = accountOptional.get();

        if (account.getBalance() < amount) {
            return false;
        }

        account.setBalance(account.getBalance() - amount);

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean transfer(Long fromAccountId, Long toAccountNumber, double amount) {

        // sender
        Optional<Account> senderOptional = accountRepository.findById(fromAccountId);

        if (senderOptional.isEmpty()) {
            return false;
        }

        Account sender = senderOptional.get();

        // find receiver
        Account receiver = null;

        List<Account> allAccounts = accountRepository.findAll();

        for (Account acc : allAccounts) {
            if (acc.getAccountNumber() != null &&
                    acc.getAccountNumber().equals(toAccountNumber)) {

                receiver = acc;
                break;
            }
        }

        // validate receiver
        if (receiver == null) {
            return false;
        }

        // check balance
        if (sender.getBalance() < amount) {
            return false;
        }

        // transfer
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return true;
    }

    @Override
    public List<Transaction> getHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}