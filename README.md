# Book Social Network 📚


## 📋 Table of Contents

- [📄 Overview](#-overview)
- [✨ Features](#-features)
- [🏗️ Microservice Architecture](#️-microservice-architecture)
- [🛠️ Technology Stack](#️-technology-stack)
  - [Backend](#backend)
  - [Frontend](#frontend)

## 📄 Overview

Book Social Network is a full-stack application that enables users to manage their book collections and engage with a community of book enthusiasts. This application is built with a microservice architecture to ensure modularity, scalability, and maintainability. It also features real-time notifications powered by WebSockets, allowing users to receive instant updates on activities such as book borrow requests, returns, and feedback. This ensures users stay engaged and informed, creating a dynamic and interactive experience within the book-loving community.

## ✨ Features

- 📝 User registration and secure email validation
- 📚 Book management: creation, updating, sharing, and archiving
- 🔄 Book borrowing with availability checks
- ✅ Book return functionality and approval of returns
- ⭐ User feedback and reviews
- 🔔 Real-time notifications using WebSocket for updates and interactions
- 🔐 Secure authentication and authorization with JWT tokens
- 🛠️ Adheres to REST API best practices

## 🏗️ Microservice Architecture

The application consists of the following microservices:

- **🔑 AUTH-SERVER**: Manages user authentication and authorization.
- **📚 BOOK-SERVER**: Handles all book-related functionalities, including management and availability checks.
- **💬 FEEDBACK-SERVER**: Manages user feedback and reviews for books.
- **🕒 TRANSACTIONHISTORY-SERVER**: Keeps track of borrowing and return transactions.
- **⚙️ CONFIG-SERVER**: Centralized configuration management for all microservices.
- **🌐 GATEWAY-SERVER**: Acts as an entry point to the system, routing requests to appropriate microservices.
- **🔍 DISCOVERY-SERVER**: Enables service discovery, allowing microservices to locate each other.
- **🔔 NOTIFICATION-SERVER**: Provides real-time notifications via WebSocket for user actions, such as borrowing and returning books.



## 🛠️ Technology Stack

### Backend

- ☕ Spring Boot 3
- 🔐 Spring Security 6
- 🔑 JWT Token Authentication
- 🗄️ Spring Data JPA
- 🛡️ JSR-303 and Spring Validation
- 📖 OpenAPI and Swagger UI Documentation
- 🐳 Docker
- ⚙️ GitHub Actions
- 🏢 Spring Cloud Netflix Eureka (Service Discovery)
- 🏗️ Spring Cloud Config (Centralized Configuration)
- 🌉 Spring Cloud Gateway (API Gateway)
- 📊 Spring Cloud Zipkin (Tracing and Monitoring)
- 🏘️ Spring Cloud OpenFeign (Declarative REST Client)
- 💾 MySQL (Database)
- 📡 WebSocket for real-time communication

### Frontend

- 🌐 Angular 17 
- 🎨 Bootstrap for styling
- ⏳ Lazy Loading
- 🔒 Authentication Guard
- 📖 OpenAPI Generator for Angular
- 🔄 rxjs for reactive programming
- 🔌 sockjs-client and stompjs for WebSocket communication





 
