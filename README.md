# Workflow System

Ein modernes Spring Boot Backend-Projekt als Portfolio für **Java Backend Development**.  
Das Projekt simuliert ein internes Workflow- und Approval-System für Anträge (z.B. Hardware, Budget) in einem Unternehmen.

> ⚠️ This project is actively under development.

---

## 🔹 Features

- Benutzer-Management mit Rollen: `USER`, `MANAGER`, `ADMIN`
- JWT-basierte Authentifizierung (stateless)
- Rollenbasierte Autorisierung mit Spring Security
- Anträge erstellen, prüfen, genehmigen oder ablehnen
- Klare Status-Transitionen (`PENDING`, `APPROVED`, `REJECTED`) im Domain Model
- Event-getriebene Architektur mit Apache Kafka (Producer + Consumer Logging)
- RESTful API mit OpenAPI / Swagger UI
- PostgreSQL als Datenbank
- Docker-Setup für Infrastruktur (DB + Kafka)

---

## 🔹 Tech Stack

- **Backend:** Java 21, Spring Boot 4  
- **Persistence:** PostgreSQL, Spring Data JPA (Hibernate)  
- **Messaging:** Apache Kafka, Spring Kafka  
- **Security:** Spring Security + JWT  
- **Validation:** Jakarta Bean Validation  
- **API Docs:** springdoc-openapi (Swagger UI)  
- **Containerization:** Docker, Docker Compose  

---

## 🔹 Architektur

Das Projekt folgt einer **modularen Layered Architecture** mit klarer Trennung:

## 🔹 Projektstruktur
```
com.portfolio.workflow
├── user
│ ├── domain
│ │ ├── model
│ │ └── repository
│ │
│ ├── application
│ │ ├── service
│ │ ├── dto
│ │ └── mapper
│ │
│ ├── infrastructure
│ │ ├── persistence
│ │ ├── security
│ │ └── config
│ │
│ └── presentation
│ └── controller
│
├── request
│ ├── domain
│ │ ├── model
│ │ └── repository
│ │
│ ├── application
│ │ ├── service
│ │ ├── dto
│ │ └── mapper
│ │
│ ├── infrastructure
│ │ ├── persistence
│ │ └── messaging
│ │
│ └── presentation
│ └── controller
```

### Layer-Verantwortlichkeiten

#### Domain
- Business-Logik (z.B. Status-Transitionen)
- Enums (Role, Permission, RequestStatus)

#### Application
- Use Cases / Services
- DTOs
- Mapper (DTO ↔ Domain)

#### Infrastructure
- JPA Entities
- Repositories
- Kafka Producer/Consumer
- Security (JWT Filter etc.)

#### Presentation
- REST Controller

👉 Ziel: klare Trennung von Business-Logik und Technik (Clean Architecture Prinzipien)

---

## 🔹 Security Konzept

- JWT Token wird beim Login generiert
- Token enthält:
  - User ID
  - Role
- Jeder Request wird über einen **JWT Filter** authentifiziert
- Zugriff wird über `@PreAuthorize` gesteuert

### Beispiel

```java
@PreAuthorize("hasRole('ADMIN')")
```
## 🔹 Event-Driven Architektur

Bei wichtigen Aktionen werden Events publiziert:

- Request erstellt
- Request genehmigt
- Request abgelehnt

### Zweck

- Entkopplung
- Erweiterbarkeit (z.B. später Audit-Service)

  
### Aktueller Stand

- Producer sendet Events
- Consumer loggt Events

##  🔹 Setup & Run

### Infrastruktur starten

```bash
docker compose up -d db kafka
```

### Anwendung starten
```bash
mvn spring-boot:run
```
##  🔹 API Dokumentation

Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

###  🔹 Demo Login

Für Tests ist ein Admin-User vorhanden:

-  **Email:** admin@workflow.local
-  **Password:** admin123

### 🔹 Beispiel Workflow
1. Admin erstellt User
2. User erstellt Request
3. Manager/Admin:
   - genehmigt oder
   - lehnt ab
4. Event wird an Kafka gesendet

###  🔹 Konfiguration

Die Anwendung nutzt **Environment Variables:**

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/workflow
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092

JWT_SECRET=your-secret
JWT_EXPIRATION=3600000
```

###  🔹 Aktueller Status
-  User Module: ✅ fertig
-  Request Module: ✅ funktional
-  Kafka Integration: ✅ aktiv
-  Security (JWT): ✅ aktiv
-  Tests: 🔄 in Planung / teilweise deaktiviert

  ###  🔹 Roadmap
-  Integration Tests stabilisieren (Testcontainers)
-  Deployment (Render / Railway)
-  Logging & Monitoring verbessern
-  Event Consumer erweitern (z.B. Audit Service)

  ###  🔹 Ziel des Projekts

Dieses Projekt demonstriert:

-  saubere Architektur in Spring Boot
-  Security Best Practices (JWT, RBAC)
-  Event-Driven Design mit Kafka
reale Backend-Strukturen wie in Unternehmen
