# Microservices System: Auth and Notification

## Overview
This project implements a system of two microservices:
1. **Auth Service**: Handles user authentication, authorization, and account management
2. **Notification Service**: Sends email notifications for user events

The system features role-based access control (USER/ADMIN), JWT authentication, and communication via Kafka.

## System Architecture
```
+------------+       +------------+       +-----------------+
| Auth       |       | RabbitMQ   |       | Notification    |
| Service    |-----> | (Message   |-----> | Service         |
| (Spring    |       | Broker)    |       | (Spring Boot)   |
| Boot)      |       +------------+       +-----------------+
+------------+             
       |                           |
       v                           v
+------------+              +-------------+
| PostgreSQL |              | MailDev     |
| Database   |              | (SMTP Server)|
+------------+              +-------------+
```

## Features
- User registration and authentication with JWT
- CRUD operations for user accounts
- Role-based authorization (USER/ADMIN)
- Email notifications for user events
- Database migrations with Liquibase
- Containerized deployment with Docker
- MailDev SMTP server for email testing and development

## Prerequisites
- Docker
- Docker Compose 
- Maven 

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/AlexisIndustries/krainet-auth-project
cd krainet-auth-project
```

### 2. Build and start the services
```bash
docker-compose up --build
```

The system will start with the following services:
- Auth Service: http://localhost:8080
- Notification Service: http://localhost:8081
- PostgreSQL: port 5432
- MailDev Web Interface: http://localhost:1080

### 3. Verify the installation
Check running containers:
```bash
docker-compose ps
```

### 4. Test with sample requests

**Register a new user:**
```bash
curl -X POST 'http://localhost:8080/api/auth/register' \
-H 'Content-Type: application/json' \
-d '{
    "username": "testuser",
    "password": "password123",
    "email": "user@example.com",
    "firstName": "Test",
    "lastName": "User"
}'
```

**Authenticate and get JWT token:**
```bash
curl -X POST 'http://localhost:8080/api/auth/login' \
-H 'Content-Type: application/json' \
-d '{"username":"admin","password":"admin"}'
```

**Get user details (replace <JWT_TOKEN> with actual token):**
```bash
curl -X GET 'http://localhost:8080/api/user/me' \
-H 'Authorization: Bearer <JWT_TOKEN>'
```

## Test Data
The system comes with a pre-configured admin and usual users:
- Username: `admin`
- Password: `superpassword`
- Role: `ADMIN`


- Username: `user`
- Password: `password`
- Role: `User`

## API Documentation

This project exposes Swagger on http://localhost:8080/swagger-ui.html

## License
This project does not have license
