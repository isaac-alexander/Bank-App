# Banking Application (Spring Boot)

This is a **Banking Application** that allows:

* Admins to manage users and perform transactions (deposit, withdraw)
* Customers to view account details and transaction history
* Money transfers between accounts
* Tracking of transactions with **date and time**
* Role-based access control (**ADMIN / CUSTOMER**)

---

## Technologies Used

* Java (Spring Boot)
* Spring MVC
* Spring Security
* Spring Data JPA
* Thymeleaf
* MySQL
* Bootstrap 5

---

## Setup Instructions / ## Cloning from GitHub

* Open a terminal or Git Bash

* Navigate to the folder where you want to clone the project

* Run the clone command (replace `<repo-url>` with your GitHub repository URL):

* git clone <repo-url>

* Navigate into the cloned folder:

* cd <project-folder-name>

* Open the project in your IDE (IntelliJ recommended)

* Configure the database connection in `application.yml`

---

## Configure Database

Update your `application.yml` file with your MySQL details:

## application.yml

```
spring:
  application:
    name: banking-app

  datasource:
    url: jdbc:mysql://localhost:3306/banking_db?&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
    username: your_username
    password: your_password

  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update

server:
  error:
    whitelabel:
      enabled: false
  port: 9090
```

---

## In your SQL Workbench run

```
create database banking_db;
```

---

## Run the Application

Run the application from the main class:

`BankingAppApplication`

App will start on:
http://localhost:9090

---

## Default Login Accounts

| Role     | Username | Password |
| -------- | -------- | -------- |
| Admin    | admin    | 1234     |
| Customer | stephen  | 1234     |
| Customer | miracle  | 1234     |

---

## Features

## Admin

* View all users
* View all transactions
* Deposit money into any account
* Withdraw money from any account

## Customer

* View personal dashboard
* View account details (account number, type, balance)
* View transaction history
* Transfer money to another account
* Edit profile

---

## Transaction System

* Supports **Deposit, Withdraw, Transfer**
* Each transaction stores:

    * Type
    * Amount
    * Account reference
    * Date and time
* Transactions are displayed in **descending order (latest first)**

---

## Security

* Spring Security authentication
* Role-based authorization:

    * ADMIN - full access
    * CUSTOMER - limited access
* Passwords are encrypted using **BCrypt**

---

## Notes

* Account number is generated from the user’s phone number
* Each user gets an account automatically on registration
* Default balance is set to **1000**
* Only admins can perform deposit and withdraw operations

---
