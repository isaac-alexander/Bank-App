package com.alexander.banking_app.repository;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // get single account for a user
    Optional<Account> findByUser(User user);

    // used for transfers
    Optional<Account> findByAccountNumber(Long accountNumber);
}