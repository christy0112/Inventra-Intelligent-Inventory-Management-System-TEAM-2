# Inventory Management System

A Spring Boot inventory management system with MySQL database, featuring admin and staff dashboards.

## Quick Start


in "src\main\resources\application.properties" path
```properties
1.spring.mail.username=use your own email
2.spring.mail.password=use your own app passqword
3.insert your my sql password into the this file
```

### 1. Setup Database
Run `create_all_tables.sql` in MySQL Workbench to create the database schema.



### 3. Access Dashboards
- login:http://localhost:8081/login.html
- Admin: http://localhost:8081/inventory.html
- Staff: http://localhost:8081/staff.html

## Features

- Product, Category, and Supplier Management
- Stock IN/OUT Operations
- Low Stock Alerts
- User Authentication (Admin/Staff roles)
- Transaction Logging

## Tech Stack

- Spring Boot 4.0.1
- Java 17
- MySQL 8.0
- HTML/CSS/JavaScript

## Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=your_password
server.port=8081
```


