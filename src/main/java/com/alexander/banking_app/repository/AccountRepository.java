package com.alexander.banking_app.repository;

import com.alexander.banking_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // get single account using user id
    Account findByUserId(Long userId);

}