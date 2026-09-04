# Moroccan Documents API

A production-ready REST API providing reference information on Moroccan administrative
and legal documents (national ID, passport, driving license, civil status certificates,
commercial registration, visas, land title, and more) — with full **French** and
**Arabic** localization.

> **Data disclaimer** — fees and processing times reflect commonly published figures for
> each document as of this project's writing and are provided for illustration; they
> change over time and can vary by prefecture/consulate. Location `phone` / `email` /
> `working_hours` values in the seed data are **placeholders** for demo purposes. Verify
> current details with official Moroccan government sources before relying on them for
> a real administrative procedure.

## Tech Stack

| Layer      | Technology                                  |
|------------|----------------------------------------------|
| Language   | Java 17                                       |
| Framework  | Spring Boot 3.5.16 (Web, Data JPA, Validation) |
| Database   | PostgreSQL 16                                 |
| Build      | Maven                                         |
| Testing    | JUnit 5, Mockito, H2 (in-memory)              |
| Container  | Docker / Docker Compose                       |

## Project Structure

```
src/main/java/com/morocco/documentsapi/
├── config/       DataSeeder (loads the 10 reference documents on first boot)
├── controller/   REST controllers (documents, categories, health)
├── dto/          Request/response payloads + the generic ApiResponse<T> envelope
├── enums/        DocumentCategoryEnum (+ JPA AttributeConverter)
├── exception/    Custom exceptions, ErrorCode, GlobalExceptionHandler
├── mapper/       Static entity → DTO conversion (language resolved here)
├── model/        JPA entities (BaseEntity, Document, and its 3 child tables)
├── repository/   Spring Data JPA repositories
└── service/      DocumentService interface + implementation
```

Layering follows `controller → service → repository → entity`; the service layer never
returns JPA entities, and mappers never contain business logic.

## Prerequisites

- Java 17+
- Maven 3.9+ (or use your IDE's bundled Maven)
- PostgreSQL 16 running locally, **or** Docker + Docker Compose

## Setup

### 1. Clone and configure

```bash
git clone <repository-url>
cd moroccan-documents-api
cp .env.example .env
```

Edit `.env` if you want different credentials than the defaults (`postgres` / `postgres`,
database `morocco_docs`).

### 2a. Run with Docker Compose (recommended)

```bash
docker compose up --build
```

This starts PostgreSQL and the API together. The API is available at
`http://localhost:8080` once the `postgres` healthcheck passes. On first boot, the app
creates its schema (`ddl-auto=update`) and seeds all 10 documents automatically.

### 2b. Run locally with Maven

Start your own PostgreSQL instance and create the database:

```bash
createdb morocco_docs
```

Then run the app (reads `DB_HOST`, `DB_PORT`, `DB_USERNAME`, `DB_PASSWORD` from your
environment, falling back to the local defaults in `application.properties`):

```bash
mvn spring-boot:run

# or, with the dev profile for verbose SQL logging:
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Running Tests

```bash
mvn test
```

Tests run against an in-memory H2 database (PostgreSQL-compatibility mode) via the
`test` profile — no running PostgreSQL instance is required to run the test suite.

- `DocumentServiceImplTest` — unit tests (Mockito), service layer, happy + error paths
- `DocumentRepositoryTest` — `@DataJpaTest` integration tests for custom queries
- `DocumentControllerTest` — `@WebMvcTest` integration tests, HTTP status + JSON shape
- `MoroccanDocumentsApiApplicationTests` — application context smoke test

## Language Support

Every endpoint accepts a `lang` query parameter:

- `lang=fr` — French (default)
- `lang=ar` — Arabic

Responses return content in **only** the requested language. An unsupported value
(anything other than `fr`/`ar`) returns `400 Bad Request` with error code `DOC_002`.

## API Reference

Base URL: `http://localhost:8080/api/v1`

All responses share the same envelope:

```json
{
  "success": true,
  "language": "fr",
  "data": { "...": "..." },
  "code": null,
  "message": null,
  "timestamp": "2026-01-15T10:00:00Z"
}
```

On error, `success` is `false`, `data` is omitted, and `code`/`message` are populated
(see [Error Codes](#error-codes)).

### Health Check

```bash
curl http://localhost:8080/api/v1/health
```

### List All Documents

```bash
curl "http://localhost:8080/api/v1/documents?lang=fr"
curl "http://localhost:8080/api/v1/documents?lang=ar"
```

<details>
<summary>Example response (lang=fr)</summary>

```json
{
  "success": true,
  "language": "fr",
  "data": {
    "count": 10,
    "documents": [
      {
        "code": "national-id",
        "name": "Carte Nationale d'Identité Électronique",
        "description": "Document officiel obligatoire attestant de l'identité...",
        "category": "identity",
        "categoryLabel": "Identité",
        "feeAmount": 100.00,
        "feeCurrency": "MAD",
        "feeVariable": false,
        "feeNote": null,
        "processingDays": 15,
        "location": "Préfecture ou Province (Wilaya) du lieu de résidence"
      }
    ]
  }
}
```
</details>

### Get a Single Document (with requirements, procedures, locations)

```bash
curl "http://localhost:8080/api/v1/documents/national-id?lang=fr"
curl "http://localhost:8080/api/v1/documents/passport?lang=ar"
```

<details>
<summary>Example response (lang=ar, truncated)</summary>

```json
{
  "success": true,
  "language": "ar",
  "data": {
    "document": {
      "code": "passport",
      "name": "جواز السفر المغربي البيومتري",
      "category": "travel",
      "categoryLabel": "السفر",
      "feeAmount": 400.00,
      "feeCurrency": "MAD",
      "processingDays": 10
    },
    "requirements": [
      { "requirement": "نسخة من البطاقة الوطنية للتعريف الإلكترونية", "orderIndex": 1 }
    ],
    "procedures": [
      { "step": "أخذ موعد عبر الإنترنت أو التوجه إلى شباك الوكالة", "orderIndex": 1 }
    ],
    "locations": [
      {
        "name": "الوكالة الوطنية للسجلات المؤمنة",
        "address": "مركز الوكالة الوطنية للسجلات المؤمنة بعمالتكم أو إقليمكم",
        "phone": "+212 5XX-XXXXXX",
        "email": "contact@ants.gov.ma",
        "workingHours": "Lundi - Vendredi : 08h30 - 16h30"
      }
    ]
  }
}
```
</details>

Returns `404` with error code `DOC_001` if the code does not exist.

### Search Documents

```bash
curl "http://localhost:8080/api/v1/documents/search?query=passeport&lang=fr"
curl "http://localhost:8080/api/v1/documents/search?query=%D8%B1%D8%AE%D8%B5%D8%A9&lang=ar"
```

Matches against the document code, and name/description in both languages. Returns `400`
with error code `DOC_003` if `query` is missing or blank.

### List Categories

```bash
curl "http://localhost:8080/api/v1/categories?lang=fr"
```

Returns every category (`identity`, `travel`, `transport`, `civil_status`, `commercial`,
`property`, `residence`) with a localized label and how many seeded documents fall into it.

## Documents Included

| Code                   | Category      | Fee              | Processing |
|-------------------------|---------------|------------------|------------|
| `national-id`            | identity      | 100 MAD          | 15 days    |
| `passport`                | travel        | 400 MAD          | 10 days    |
| `driving-license`          | transport     | 200 MAD          | 5 days     |
| `marriage-certificate`     | civil_status  | 50 MAD           | 3 days     |
| `birth-certificate`        | civil_status  | 30 MAD           | 1 day      |
| `business-license`         | commercial    | 500 MAD          | 5 days     |
| `commercial-register`      | commercial    | 300 MAD          | 7 days     |
| `visa-schengen`            | travel        | 80 EUR           | 15 days    |
| `land-title`               | property      | variable         | 30 days    |
| `proof-of-residence`       | residence     | 10 MAD           | 1 day      |

## Error Codes

Every error returns a stable `code` instead of a raw message, so clients can branch on
it rather than parsing text.

| Code     | Meaning                          | HTTP | FR                                            | AR                                             |
|----------|-----------------------------------|------|------------------------------------------------|--------------------------------------------------|
| `DOC_000`| Unexpected internal error         | 500  | Erreur interne du serveur                      | خطأ داخلي في الخادم                              |
| `DOC_001`| Document not found                | 404  | Document introuvable                           | الوثيقة غير موجودة                                |
| `DOC_002`| Unsupported/invalid `lang`        | 400  | Langue non supportée. Utilisez 'fr' ou 'ar'    | اللغة غير مدعومة. استعمل 'fr' أو 'ar'            |
| `DOC_003`| Missing `query` on search         | 400  | Le paramètre de recherche 'query' est requis   | معطى البحث 'query' مطلوب                          |
| `DOC_004`| Invalid request / unknown route   | 400/404/405 | Requête invalide                        | طلب غير صالح                                     |

## Configuration

All settings are overridable via environment variables (see `.env.example`); nothing
sensitive is hardcoded in source.

| Variable      | Default        | Description                        |
|---------------|-----------------|--------------------------------------|
| `DB_HOST`      | `localhost`     | PostgreSQL host                       |
| `DB_PORT`      | `5432`          | PostgreSQL port                       |
| `DB_NAME`      | `morocco_docs`  | Database name                         |
| `DB_USERNAME`  | `postgres`      | Database user                         |
| `DB_PASSWORD`  | `postgres`      | Database password                     |
| `SERVER_PORT`  | `8080`          | HTTP port the API listens on          |

Profiles: `application.properties` (defaults), `application-dev.properties` (verbose SQL
logging, activate with `-Dspring-boot.run.profiles=dev`), `application-test.properties`
(H2, used automatically by the test suite).

## Docker

```bash
# Build and run API + PostgreSQL
docker compose up --build

# Stop
docker compose down

# Stop and wipe the database volume
docker compose down -v
```

The `Dockerfile` is a multi-stage build (Maven build stage → minimal JRE runtime stage)
running as a non-root user.
