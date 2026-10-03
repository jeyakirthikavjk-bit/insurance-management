# 🛡️ Insurance Policy Management System

A full-stack Insurance Policy Management System built with **Spring Boot 3**, **JPA/Hibernate**, **SQLite**, and **Thymeleaf**.

---

## 🏗️ Architecture Overview

```
insurance-management/
├── pom.xml
└── src/main/
    ├── java/com/insurance/
    │   ├── InsuranceManagementApplication.java
    │   ├── config/
    │   │   ├── DataInitializer.java         ← Seeds demo data on startup
    │   │   └── WebConfig.java               ← Static resource & upload config
    │   ├── entity/                          ← OOP: 10 JPA entity classes
    │   │   ├── User.java
    │   │   ├── Agent.java
    │   │   ├── Customer.java
    │   │   ├── Policy.java                  ← abstract base class
    │   │   ├── LifePolicy.java              ← extends Policy
    │   │   ├── HealthPolicy.java            ← extends Policy
    │   │   ├── VehiclePolicy.java           ← extends Policy
    │   │   ├── Claim.java
    │   │   ├── Payment.java
    │   │   └── Document.java
    │   ├── repository/                      ← Spring Data JPA interfaces
    │   ├── service/                         ← Business logic layer
    │   ├── controller/
    │   │   ├── DashboardController.java     ← Web UI (Thymeleaf)
    │   │   ├── CustomerController.java      ← REST API
    │   │   ├── PolicyController.java        ← REST API
    │   │   ├── ClaimController.java         ← REST API
    │   │   ├── PaymentController.java       ← REST API
    │   │   ├── AgentController.java         ← REST API
    │   │   └── DocumentController.java      ← REST API + file upload/download
    │   └── exception/
    │       ├── GlobalExceptionHandler.java
    │       ├── ResourceNotFoundException.java
    │       └── DuplicateResourceException.java
    └── resources/
        ├── application.properties
        ├── static/css/style.css
        └── templates/                       ← 10 Thymeleaf HTML pages
```

---

## 🧠 OOP Concepts Demonstrated

| Concept | Where |
|---|---|
| **Encapsulation** | All entity classes use private fields + Lombok `@Data` (getters/setters) |
| **Inheritance** | `LifePolicy`, `HealthPolicy`, `VehiclePolicy` all extend abstract `Policy` |
| **Polymorphism** | `calculatePremium()` and `getPolicyDetails()` overridden in each policy subclass |
| **Abstraction** | `Policy` is an abstract class with abstract methods, using `@Inheritance(SINGLE_TABLE)` |

---

## 🗄️ Database Schema

Uses **SQLite** with Hibernate auto-DDL. Key relationships:

- `Customer` → `Policy` (OneToMany)
- `Policy` → `Claim` (OneToMany)
- `Policy` → `Payment` (OneToMany)
- `Customer` → `Document` (OneToMany)
- `Agent` → `Customer` (OneToMany)
- `Agent` → `Policy` (OneToMany)

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
# 1. Clone / navigate to project
cd insurance-management

# 2. Build
mvn clean package -DskipTests

# 3. Run
mvn spring-boot:run
# OR
java -jar target/insurance-management-1.0.0.jar
```

### Access
- **Home:** http://localhost:8080/
- **Login:** http://localhost:8080/login
- **Dashboard:** http://localhost:8080/dashboard

The SQLite database file (`insurance.db`) is created automatically in the project root on first run with demo data seeded.

---

## 📡 REST API Endpoints

### Customers
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Create new customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Deactivate customer |
| GET | `/api/customers/search?keyword=x` | Search customers |

### Policies
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/policies` | Get all policies |
| POST | `/api/policies/life?customerId=x` | Create life policy |
| POST | `/api/policies/health?customerId=x` | Create health policy |
| POST | `/api/policies/vehicle?customerId=x` | Create vehicle policy |
| PATCH | `/api/policies/{id}/status?status=x` | Update policy status |
| GET | `/api/policies/customer/{id}` | Policies by customer |
| GET | `/api/policies/stats` | Active count + revenue |

### Claims
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/claims/file?policyId=x` | File a claim |
| PATCH | `/api/claims/{id}/approve?approvedAmount=x` | Approve claim |
| PATCH | `/api/claims/{id}/reject?reason=x` | Reject claim |
| PATCH | `/api/claims/{id}/status?status=SETTLED` | Settle claim |
| GET | `/api/claims/status/{status}` | Filter by status |

### Payments
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments?policyId=x` | Record payment |
| GET | `/api/payments/policy/{id}` | Payments by policy |
| GET | `/api/payments/stats` | Total revenue |

### Documents
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/documents/upload` | Upload file (multipart) |
| GET | `/api/documents/{id}/download` | Download file |
| DELETE | `/api/documents/{id}` | Delete document |

---

## 🖥️ Web Pages

| URL | Page |
|---|---|
| `/` | Home / Landing page |
| `/login` | Login |
| `/dashboard` | Dashboard with charts |
| `/customers` | Customer management |
| `/policies` | Policy management |
| `/claims` | Claims processing |
| `/payments` | Payment records |
| `/agents` | Agent management |
| `/documents` | Document upload/download |
| `/reports` | Reports & analytics |

---

## 📮 Postman Collection

Import `InsuranceSystem_Postman_Collection.json` into Postman to test all REST endpoints immediately.

---

## 🔧 Maven Dependencies

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | REST API |
| `spring-boot-starter-data-jpa` | ORM / JPA |
| `spring-boot-starter-thymeleaf` | HTML templating |
| `spring-boot-starter-validation` | Input validation |
| `sqlite-jdbc` | SQLite driver |
| `hibernate-community-dialects` | SQLite Hibernate dialect |
| `lombok` | Reduce boilerplate |

---

## 👤 Demo Credentials

- **Username:** `admin`
- **Password:** `admin123`

Demo data auto-seeded: 2 agents, 3 customers, 3 policies (one of each type), 2 claims, 3 payments.
