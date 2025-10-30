#  Customer Management Microservices Application

A **Spring Boot microservices-based system** for customer registration, authentication, and management — complete with a **React frontend**, **JWT security**, and **Dockerized deployment**.

This project demonstrates a **modern microservices architecture** where each component (Account Service, Data Service, and Frontend) operates independently yet communicates securely through REST APIs.

---

##  Overview

###  Services Architecture

| Service | Port | Description |
|----------|------|-------------|
| **Data Service** | `8080` | REST API for managing customers (CRUD) using Spring Boot + HSQLDB |
| **Account Service** | `8081` | Handles registration, login, and JWT generation |
| **Frontend** | `3000` | React-based UI that consumes the REST APIs |

All services are containerized using **Docker** and orchestrated with **Docker Compose**.

---

##  Project Structure
```
customer-microservices/
│
├── account-service/ # Authentication and JWT token generation
│ ├── src/main/java/com/adp/account_service/
│ ├── Dockerfile
│ └── build.gradle
│
├── data-service/ # Customer CRUD API with JWT validation
│ ├── src/main/java/com/adp/data/
│ ├── Dockerfile
│ └── build.gradle
│
├── client/ # React frontend
│ ├── src/
│ ├── Dockerfile
│ └── package.json
│
├── docker-compose.yml # Multi-service orchestration
└── README.md
```
---

##  Tech Stack

###  Backend
- **Java 21**
- **Spring Boot 3.5**
- **Gradle**
- **Spring Web**, **Spring Data JPA**
- **HSQLDB (in-memory database)**
- **JSON Web Tokens (JWT)** for authentication
- **Mockito / JUnit 5** for testing

###  Frontend
- **React.js (Vite)**
- **Node.js**
- **Nginx** (for static deployment)
- **Fetch API** for backend communication

### 🐳 Deployment
- **Docker** for containerization  
- **Docker Compose** for service orchestration

---

##  Security Overview

###  Authentication Flow

1. The **Frontend** sends login credentials to the **Account Service** (`/account/token`).
2. The Account Service validates credentials with the **Data Service** and issues a **JWT token**.
3. The **Frontend** uses this token in all subsequent requests to the **Data Service** via: Authorization: Bearer <token>
4. The **Data Service’s JwtFilter** validates this token before allowing access.

###  Internal Communication

- Internal calls between Account Service and Data Service (e.g. `/customers/search`) use: <br>
X-Internal-Secret: dev-internal-secret <br>
which bypasses JWT validation for trusted service-to-service communication.

---

##  Testing

###  Unit Tests
- **JwtFilterTest** – verifies that unauthorized requests are blocked and valid tokens are accepted.
- **CustomerAPITest** – checks CRUD operations on customer data.
- **AccountControllerTest** – tests authentication and registration endpoints.

###  Manual API Testing
Use **Postman**:
1. Register a user: <br>
 POST http://localhost:8081/account/register <br>
 Body: { "username": "alice", "email": "alice@adp.com", "password": "alice123" }
2. Login to get a JWT token: <br>
 POST http://localhost:8081/account/token <br>
 Body: { "username": "alice", "password": "alice123" }
3. Use the token to access protected data: <br>
 Header: Authorization: Bearer <token>
4. Internal communication (Account → Data):
 GET http://localhost:8080/api/customers/search?username=alice <br>
 Header: X-Internal-Secret: dev-internal-secret
