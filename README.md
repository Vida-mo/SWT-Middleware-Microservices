# Enterprise Middleware & Cloud Architecture Portfolio

Dieses Repository ist ein kompaktes Showcase für moderne **Enterprise-Middleware- und Microservice-Architekturen** auf Basis von **Java 21**, **Spring Boot 3** und **Spring Cloud**.

Es demonstriert praxisnah zentrale Entwurfsmuster und Kernkonzepte für verteilte, fehlertolerante und skalierbare Systeme:

- **Event-Driven Microservices (EDA):** Asynchrone Geschäftslogik mit CQRS (Command Query Responsibility Segregation), Event Sourcing und verteilten Saga-Transaktionen (Axon Framework & Axon Server).
- **Cloud Security & Identity Management:** Absicherung im Zero-Trust-Modell via OAuth2 & OpenID Connect (OIDC), JWT-Token-Validierung, Token Relay über ein API-Gateway und CloudFoundry UAA.
- **Resilienz & Fehlertoleranz:** Schutz vor Kaskadenausfällen in verteilten Systemen durch Circuit Breaker, Rate Limiter, Bulkhead und Time Limiter (Resilience4j).
- **Service Mesh & API-Routing:** Dynamische Service Discovery und Client-Side Load Balancing (Netflix Eureka) sowie intelligentes Routing, Path Rewriting und Filterketten (Spring Cloud Gateway).
- **Clean Architecture & Domain Modeling:** RESTful APIs nach dem Boundary-Control-Entity-Muster (BCE) mit Spring Data JPA und automatisierten Integrationstests.

---

### 📂 Projektstruktur

- [bike-rental-axoniq-microservices/](https://github.com/Vida-mo/SWT-Middleware-Microservices/blob/main/bike-rental-axoniq-microservices) — Event-Driven Microservices mit Axon Framework, CQRS, Event Sourcing & Saga-Orchestrierung
- [2025-swt-spring-cloud-sec-02-oauth2-all-grant-types/](https://github.com/Vida-mo/SWT-Middleware-Microservices/blob/main/2025-swt-spring-cloud-sec-02-oauth2-all-grant-types) — OAuth2 & OIDC Multi-Flow-Authentifizierung mit CloudFoundry UAA, API Gateway & Resource Server
- [2025-swt-resilience-demo/](https://github.com/Vida-mo/SWT-Middleware-Microservices/blob/main/2025-swt-resilience-demo) — Resilience4j Patterns (Circuit Breaker, Bulkhead, Rate Limiter, Time Limiter) mit Lasttest-Skripten
- [2025-swt-spring-cloud-gw-registry/](https://github.com/Vida-mo/SWT-Middleware-Microservices/blob/main/2025-swt-spring-cloud-gw-registry) — Dynamic Routing mit Spring Cloud Gateway und Netflix Eureka Service Registry im Multi-Container-Setup
- [2024-swt-demo-person-spring/](https://github.com/Vida-mo/SWT-Middleware-Microservices/blob/main/2024-swt-demo-person-spring) — REST API mit Schichtenarchitektur (BCE-Muster), Spring Data JPA und Integrationstests

---

**Tech-Stack:** Java 21, Spring Boot 3.4, Spring Cloud (Gateway, Eureka), Spring Security (OAuth2, OIDC, JWT), Axon Framework 4.11, Axon Server, Resilience4j, CloudFoundry UAA, Docker & Docker Compose, Maven, Gradle.
