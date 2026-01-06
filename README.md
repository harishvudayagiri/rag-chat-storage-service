# RAG Chat Storage Service

This project implements a backend service for managing chat sessions and messages, similar to the storage layer required for a ChatGPT-style application. The service focuses on session lifecycle management, message persistence, pagination, security, and abuse protection.

The implementation was kept intentionally backend-focused, as per the assignment scope.

---

## Features Implemented

### Chat Session Management
- Create chat sessions for a user
- Rename existing chat sessions
- Mark or unmark sessions as favorite
- Delete a chat session along with all its messages

### Chat Messages
- Add messages to a chat session
- Store sender, content, and optional retrieved context
- Retrieve message history for a session
- Pagination support when fetching messages

### Technical Capabilities
- API key–based authentication
- Rate limiting to prevent API abuse
- Centralized exception handling
- Environment-based configuration
- Dockerized local setup (application + database)
- Health check endpoint

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker & Docker Compose

---

## Configuration

All environment-specific values are provided via environment variables.

An example configuration file is provided as `.env.example`.

```env
DB_NAME=chatdb
DB_USER=chatuser
DB_PASSWORD=chatpass

SECURITY_API_KEY=test-api-key

APP_RATE_LIMIT_REQUESTS_PER_MINUTE=60
```

For local execution, copy it to .env and update values as required.

The .env file is not committed intentionally.

---
## Running the Application (Docker)
Prerequisites

 -- Docker
 -- Docker Compose

Steps
---
```env
cp .env.example .env
docker-compose down -v
docker-compose up --build
```

Once started, the application will be available at:
```env 
http://localhost:8080
```

PostgreSQL runs in a separate container and is accessed internally by the application using Docker service discovery.

---

## Authentication

All APIs are protected using an API key.

Each request must include the following header:

X-API-KEY: <configured-api-key>


Requests without a valid API key will return 401 Unauthorized.

---

## API Overview
```env
Create Chat Session
POST /api/sessions

Rename Chat Session
PUT /api/sessions/{sessionId}/rename

Mark / Unmark Favorite
PUT /api/sessions/{sessionId}/favorite

Add Message to a Session
POST /api/sessions/{sessionId}/messages

Get Messages (Paginated)
GET /api/sessions/{sessionId}/messages?page=0&size=10

Delete Session
DELETE /api/sessions/{sessionId}

Pagination

Message retrieval supports pagination using Spring Data Pageable.

Query parameters:

page – zero-based page index

size – number of records per page

Example:

GET /api/sessions/{sessionId}/messages?page=1&size=20
```
---

## Rate Limiting

Rate limiting is enforced per API key using a fixed time window approach.

The limit is configurable via:
```env
APP_RATE_LIMIT_REQUESTS_PER_MINUTE
```

When the limit is exceeded, the API responds with:

HTTP 429 Too Many Requests

Health Check

A simple health endpoint is exposed:
```env
GET /actuator/health

```
Example response:

{
  "status": "UP"
}

Testing

The APIs can be tested using curl or Postman.

A Postman collection is included in the repository to simplify manual testing of all endpoints, including pagination and rate limiting scenarios.

---

## Postman Collection

A Postman collection is included in the repository under the `postman/` directory.

It contains requests for:
- Creating chat sessions
- Adding messages to a session
- Fetching messages with pagination
- Renaming sessions
- Marking / unmarking favorites
- Deleting sessions

All requests are preconfigured to use the required `X-API-KEY` header.  
The collection can be imported directly into Postman and used against a locally running instance of the application.

