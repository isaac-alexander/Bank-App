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
    public CommandLineRunner run() {
        return args -> {

            // create admin user
            if (userRepository.findByUsername("admin") == null) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setPassword("1234");
                admin.setPhoneNumber("08000000000");
                admin.setRole("ADMIN");
                userRepository.save(admin);

                System.out.println("admin user created");
            }

            // create customer1
            if (userRepository.findByUsername("stephen") == null) {

                User stephen = new User();
                stephen.setUsername("Stephen");
                stephen.setPassword("1234");
                stephen.setPhoneNumber("08011111111");
                stephen.setRole("CUSTOMER");
                userRepository.save(stephen);

                System.out.println("stephen user created");
            }

            // create customer2
            if (userRepository.findByUsername("miracle") == null) {

                User miracle = new User();
                miracle.setUsername("Miracle");
                miracle.setPassword("1234");
                miracle.setPhoneNumber("08022222222");
                miracle.setRole("CUSTOMER");
                userRepository.save(miracle);

                System.out.println("miracle user created");
            }
        };
    }
}