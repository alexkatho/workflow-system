# Workflow System

Ein modernes Spring Boot Backend-Projekt als Portfolio für **Java Full-Stack / Backend Development**.  
Das Projekt simuliert ein internes Workflow- und Approval-System für Anträge (z.B. Urlaub, Hardware, Budget) in einem Unternehmen.

## 🔹 Features

- Benutzer-Management mit Rollen: `USER`, `MANAGER`, `ADMIN`
- Anträge erstellen, prüfen, genehmigen oder ablehnen
- Status-Transitionen (`PENDING`, `APPROVED`, `REJECTED`) im Domain Model implementiert
- Audit Logging für alle Aktionen
- Event-getriebene Architektur mit Apache Kafka (Producer/Consumer)
- RESTful API mit OpenAPI/Swagger Dokumentation
- Docker-Containerisierung für App + PostgreSQL
- Unit- und Integration-Tests (JUnit 5, Mockito, Testcontainers)
- Security: JWT Authentifizierung + Rollenbasierter Zugriff

## 🔹 Tech Stack

- **Backend:** Java 21, Spring Boot 4.0.3, Maven 3.9
- **Persistence:** PostgreSQL 15+, JPA/Hibernate
- **Messaging:** Apache Kafka 3+, Spring for Apache Kafka
- **Mapping:** MapStruct (DTO ↔ Entity)
- **Security:** Spring Security + JWT
- **Testing:** JUnit 5, Mockito, Testcontainers, REST Assured
- **Containerization:** Docker, Docker Compose
- **CI/CD:** GitHub Actions (Build, Test, Docker-Image, Deployment)
- **Optional Frontend:** React 18+ (nur für API Demo)

## 🔹 Architektur

- **Modular Monolith** mit klaren Layern:
  - `domain/model` → Business-Logik, Status-Methoden
  - `entity` → JPA-Entities für DB-Persistenz
  - `dto` → REST Input/Output
  - `service` → Business Services
  - `controller` → REST Endpoints
  - `event` → Kafka Producer/Consumer
- Clean Architecture / Hexagonal Elemente für Testbarkeit und Erweiterbarkeit

## 🔹 Setup & Run

1. Docker starten (PostgreSQL + App):
```bash
docker-compose up --build
