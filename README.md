Car Sharing Service Management System
<span style="font-size: 24px; color: #30558a">Streamline Car Rentals with Our Online Platform</span>

<span style="display: inline-block; border: 5px solid #30558a; border-radius: 25px; padding: 7px;">
<img src="car-sharing-service-image.png" alt="Car Sharing Service" style="border-radius: 10px; width: 150px">
</span>
Introduction
Welcome to the Car Sharing Service Management System! This project aims to revolutionize the car sharing experience in our city by introducing a modern online platform. With our system, users can easily rent cars, check availability, make payments online, and enjoy a seamless experience. Whether you're a team member working on this project or a user interested in our services, this README will provide all the information you need.

Table of Contents
Technologies and Tools
Functionalities
Project Structure
Getting Started
Challenges Faced
Swagger Documentation
Contributing
Technologies and Tools
Our Car Sharing Service Management System leverages the following technologies and tools:

Spring Boot: For rapid development and configuration management.
Spring Security: Ensuring secure user authentication.
Spring Data JPA: Simplifying database operations.
Swagger: Providing comprehensive API documentation.
Maven: Handling dependencies and building the project.
IntelliJ IDEA: Recommended development environment.
Functionalities
Our application is designed to offer a wide range of functionalities to enhance the car sharing experience. Here are some key features:

Authentication Controller: Handles user registration and authentication securely.
<span style="display: inline-block; border: 5px solid #518a51; border-radius: 25px; padding: 7px;">
<img src="authentication-controller-image.png" alt="Authentication" style="border-radius: 10px; width: 350px">
</span>
Cars Controller: Manages the car inventory, including adding, updating, and deleting cars.
Users Controller: Provides user management functionalities, including role updates and profile information.
Rentals Controller: Manages user car rentals, with features such as rental creation, listing, and returns.
Payments Controller (Stripe): Facilitates online payments for car rentals through Stripe integration.
Notifications Service (Telegram): Sends real-time notifications about rentals, payments, and overdue returns.
Project Structure
Our project is organized with a clear structure to maintain code quality and readability. Here's a brief overview:

.github: GitHub configuration files.

.idea: Configuration files for IntelliJ IDEA.

.mvn: Maven configuration files.

src: Main project directory containing Java source code.

carsharing: Root package for the application.
config: Application configuration classes.
controller: Controllers handling various functionalities.
dto: Data Transfer Objects for requests and responses.
enums: Enumerations used in the application.
exception: Custom exceptions and exception handling.
mapper: Mapper classes for data conversion.
model: Entity classes representing database tables.
repository: Data repositories.
security: Security-related classes and authentication logic.
service: Service implementations and interfaces.
validation: Validation classes for field matching.
resources: Configuration and resource files.

changelog: Liquibase database changelog scripts.

test: Integration tests classes.

db-scripts: SQL scripts for tests setup.

application.properties: Application properties for tests.

Getting Started
To get started with our Car Sharing Service Management System, follow these steps:

Clone the repository:

bash
Copy code
git clone https://github.com/your-organization/car-sharing-app.git
Create a .env file with sensitive information and configurations. You can use the provided .env.sample as a template.

Build and run the project using Maven:

bash
Copy code
cd car-sharing-app
mvn clean install
java -jar target/car-sharing-app.jar
Access the Swagger documentation at http://localhost:8080/swagger-ui.html to explore and test the API endpoints.

Challenges Faced
During the development of this project, we encountered several challenges, including:

Circular dependencies within the service layer, which were resolved by refactoring to utilize repositories.
Ensuring secure handling of user authentication and payment processing.
We continuously strive to overcome these challenges and improve our system.

Swagger Documentation
Our API documentation is available via Swagger, providing detailed information about endpoints, request parameters, and responses. Explore the documentation at Swagger Documentation.

swagger.png

Contributing
If you're interested in contributing to our Car Sharing Service Management System, feel free to reach out on GitHub. Your contributions are highly appreciated as we work together to enhance the car sharing experience for our users.

Thank you for your interest in our Car Sharing Service!