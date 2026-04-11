package com.alexander.banking_app;

import com.alexander.banking_app.entity.User;
import com.alexander.banking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingAppApplication {

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

    @Bean
    public CommandLineRunner run() {return args -> {

            // check if test user already exists
            if (userRepository.findByUsername("alex") == null) {

                User user = new User(); // create user

                user.setUsername("alex"); // username
                user.setPassword("1234"); // password
                user.setPhoneNumber("08012345678"); // phone number
                user.setRole("CUSTOMER"); // role

                userRepository.save(user); // save to database

                System.out.println("test user created");
            }
        };
    }

}
