package com.alexander.banking_app.service.impl;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.Transaction;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.TransactionRepository;
import com.alexander.banking_app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void deposit(Long accountId, double amount) {

        Optional<Account> optional = accountRepository.findById(accountId); // find account

        if (optional.isPresent()) { // if found

            Account account = optional.get(); // get account

            account.setBalance(account.getBalance() + amount); // add money

            accountRepository.save(account); // save

            Transaction t = new Transaction(); // create transaction

            t.setAccountId(accountId); // link account

            t.setAmount(amount); // set amount

            t.setType("DEPOSIT"); // type

            t.setDate(LocalDateTime.now()); // time

            transactionRepository.save(t); // save transaction
        }
    }

    @Override
    public boolean withdraw(Long accountId, double amount) {

        Optional<Account> optional = accountRepository.findById(accountId);

        if (optional.isPresent()) {

            Account account = optional.get();

            if (account.getBalance() >= amount) { // check balance

                account.setBalance(account.getBalance() - amount); // subtract

                accountRepository.save(account);

                Transaction t = new Transaction();

                t.setAccountId(accountId);

                t.setAmount(amount);

                t.setType("WITHDRAW");

                t.setDate(LocalDateTime.now());

                transactionRepository.save(t);

                return true; // success
            }
        }

        return false; // failed
    }

    @Override
    public List<Transaction> getHistory(Long accountId) {

        return transactionRepository.findByAccountId(accountId);
    }
}