# Project Overview

The **Journal API** (`journal_app`) is a backend REST service built with Spring Boot and MongoDB for journal and journal entry management. It provides complete CRUD operations for managing journals, journal entries with date-range lookups, and integrates OpenAPI / Swagger UI documentation.

## Tech Stack & Architecture

- **Language / Runtime**: Java 21
- **Framework**: Spring Boot 3.1.4 (`spring-boot-starter-web`, `spring-boot-starter-data-mongodb`)
- **Build Tool**: Gradle (using Gradle Wrapper)
- **Database / Storage**: MongoDB (configured via Spring Data MongoDB)
- **Object Mapping & Boilerplate**: MapStruct 1.6.2 & Lombok
- **API Documentation**: Springdoc OpenAPI / Swagger UI 2.2.0 (`/swagger-ui.html`, `/v3/api-docs`)
- **Code Formatting**: Spotless 8.1.0 (Google Java Format)
- **Testing**: Spring Boot Starter Test (JUnit 5 / JUnit Platform, MockMvc)

## Directory Structure

```text
journal-api/
├── gradle/wrapper/                                    # Gradle wrapper binaries & properties
├── src/
│   ├── main/
│   │   ├── java/com/mds/journal_app/
│   │   │   ├── configs/                              # MongoDB and OpenAPI configurations
│   │   │   ├── controller/                           # REST controllers (e.g., JournalController)
│   │   │   ├── dao/                                  # MongoDB entity documents & Spring Data repositories
│   │   │   ├── exceptions/                           # Custom exceptions & global exception handlers
│   │   │   ├── mapper/                               # MapStruct DTO/Entity mappers
│   │   │   ├── pojo/                                 # Request/Response DTOs & common responses
│   │   │   ├── service/                              # Core business logic
│   │   │   └── JournalAppApplication.java            # Spring Boot application entry point
│   │   └── resources/
│   │       ├── application.yml                       # Default configuration (port 8081, MongoDB URI)
│   │       ├── application-dev.yml                   # Dev profile configuration
│   │       ├── application-default.yml               # Fallback profile configuration
│   │       └── data/                                 # Sample data & Postman collections
│   └── test/java/com/mds/journal_app/                # Controller, Service unit & integration tests
├── build.gradle                                      # Build script, plugins, and dependencies
├── Dockerfile                                        # Container build instructions
├── gradlew / gradlew.bat                             # Gradle wrapper executables
└── settings.gradle                                   # Project definition
```

## Key Commands

### Build & Run
- **Build application**:
  ```bash
  ./gradlew build
  ```
  *(Windows: `.\gradlew.bat build`)*
- **Run application locally**:
  ```bash
  ./gradlew bootRun
  ```
  *(Windows: `.\gradlew.bat bootRun`)*
- **Run with specific profile** (e.g., `dev`):
  ```bash
  ./gradlew bootRun --args='--spring.profiles.active=dev'
  ```

### Code Quality & Formatting
- **Check code formatting (Spotless)**:
  ```bash
  ./gradlew spotlessCheck
  ```
- **Apply Google Java Format (Spotless)**:
  ```bash
  ./gradlew spotlessApply
  ```

### Testing
- **Run all tests**:
  ```bash
  ./gradlew test
  ```

## Code Guidelines & Standards

- **Java Version**: Java 21 source compatibility.
- **Formatting**: Strictly follow Google Java Format via Spotless (`./gradlew spotlessApply` before committing).
- **Architecture Layers**: Maintain clean separation across `controller` -> `service` -> `dao` (repository/entities).
- **DTOs & Mapping**:
  - Expose request and response POJOs in `pojo/` rather than internal MongoDB documents.
  - Use MapStruct mappers (`mapper/`) for converting between entities and DTOs.
- **API Responses**:
  - Encapsulate controller JSON responses in `BaseApiResponse<T>` where appropriate.
  - Annotate API endpoints with `@Operation` and tags from `io.swagger.v3.oas.annotations`.
- **Exception Handling**:
  - Throw `ApiException` for expected application-level errors.
  - Keep centralized handling inside `GlobalExceptionHandler.java`.
- **Lombok Usage**: Use `@RequiredArgsConstructor`, `@Getter`, `@Setter`, and `@Slf4j` to avoid repetitive boilerplate.
