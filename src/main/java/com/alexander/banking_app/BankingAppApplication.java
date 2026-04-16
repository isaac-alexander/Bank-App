package com.alexander.banking_app;

import com.alexander.banking_app.entity.Account;
import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.AccountRepository;
import com.alexander.banking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class BankingAppApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BankingAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {

            // create admin user
            if (userRepository.findByUsername("admin") == null) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setPhoneNumber("08000000000");
                admin.setRole("ADMIN");

                admin.setFirstName("Admin");
                admin.setLastName("Admin");
                admin.setDateOfBirth(LocalDate.of(2022, 1, 1));
                admin.setAddress("Head Office");

                userRepository.save(admin);

                System.out.println("admin created");
            }

            // customer 1 with savings account
            if (userRepository.findByUsername("stephen") == null) {

                User stephen = new User();

                stephen.setUsername("stephen");
                stephen.setPassword(passwordEncoder.encode("1234"));
                stephen.setPhoneNumber("08011111111");
                stephen.setRole("CUSTOMER");

                stephen.setFirstName("Isaac");
                stephen.setLastName("Stephen");
                stephen.setDateOfBirth(LocalDate.of(2022, 4, 20));
                stephen.setAddress("Ibadan");

                userRepository.save(stephen);

                Account acc1 = new Account();
                acc1.setAccountNumber(8011111111L);
                acc1.setAccountType("SAVINGS");
                acc1.setBalance(1000);
                acc1.setUser(stephen);

                accountRepository.save(acc1);

                System.out.println("stephen created with savings account");
            }

            // customer 2 with checking account
            if (userRepository.findByUsername("miracle") == null) {

                User miracle = new User();

                miracle.setUsername("miracle");
                miracle.setPassword(passwordEncoder.encode("1234"));
                miracle.setPhoneNumber("08022222222");
                miracle.setRole("CUSTOMER");

                miracle.setFirstName("Isaac");
                miracle.setLastName("Miracle");
                miracle.setDateOfBirth(LocalDate.of(2022, 6, 3));
                miracle.setAddress("Lagos");

                userRepository.save(miracle);

                Account acc2 = new Account();
                acc2.setAccountNumber(8022222222L);
                acc2.setAccountType("CHECKING");
                acc2.setBalance(1000);
                acc2.setUser(miracle);

                accountRepository.save(acc2);

                System.out.println("miracle created with checking account");
            }
        };
    }}