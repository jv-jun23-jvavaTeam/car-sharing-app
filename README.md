# Car Sharing Service Management System

![Car Sharing Service](car-sharing-service-image.png)

## Introduction

Welcome to the Car Sharing Service Management System! This project aims to revolutionize the car sharing experience in our city by introducing a modern online platform. With our system, users can easily rent cars, check availability, make payments online, and enjoy a seamless experience. Whether you're a team member working on this project or a user interested in our services, this README will provide all the information you need.

**Link to Loom video about this project:** [Watch Video](https://bit.ly/48B6FIo)

## Table of Contents

- [Technologies and Tools](#technologies-and-tools)
- [Functionalities](#functionalities)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Challenges Faced](#challenges-faced)
- [Swagger Documentation](#swagger-documentation)
- [Postman Collection](#postman-collection)
- [Contributing](#contributing)

## Technologies and Tools

Our Car Sharing Service Management System leverages the following technologies and tools:

- **Spring Boot**: For rapid development and configuration management.
- **Spring Security**: Ensuring secure user authentication.
- **Spring Data JPA**: Simplifying database operations.
- **Swagger**: Providing comprehensive API documentation.
- **Maven**: Handling dependencies and building the project.
- **IntelliJ IDEA**: Recommended development environment.

## Functionalities

Our application is designed to offer a wide range of functionalities to enhance the car sharing experience. Here are some key features:

- **Authentication Controller**: Handles user registration and authentication securely.
  ![Authentication](authentication-controller-image.png)

- **Cars Controller**: Manages the car inventory, including adding, updating, and deleting cars.
- **Users Controller**: Provides user management functionalities, including role updates and profile information.
- **Rentals Controller**: Manages user car rentals, with features such as rental creation, listing, and returns.
- **Payments Controller (Stripe)**: Facilitates online payments for car rentals through Stripe integration.
- **Notifications Service (Telegram)**: Sends real-time notifications about rentals, payments, and overdue returns.

## Project Structure

Our project is organized with a clear structure to maintain code quality and readability. Here's a brief overview:

- `.github`: GitHub configuration files.
- `.idea`: Configuration files for IntelliJ IDEA.
- `.mvn`: Maven configuration files.
- `src`: Main project directory containing Java source code.
    - `carsharing`: Root package for the application.
        - `config`: Application configuration classes.
        - `controller`: Controllers handling various functionalities.
        - `dto`: Data Transfer Objects for requests and responses.
        - `enums`: Enumerations used in the application.
        - `exception`: Custom exceptions and exception handling.
        - `mapper`: Mapper classes for data conversion.
        - `model`: Entity classes representing database tables.
        - `repository`: Data repositories.
        - `security`: Security-related classes and authentication filter logic.
        - `service`: Service implementations and interfaces.
    - `validation`: Validation classes for field matching.

- `resources`: Configuration and resource files.
- `changelog`: Liquibase database changelog scripts.
- `test`: Integration tests classes.
- `db-scripts`: SQL scripts for tests setup.
- `application.properties`: Application properties for tests.

## Getting Started

To get started with our Car Sharing Service Management System, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/your-organization/car-sharing-app.git
