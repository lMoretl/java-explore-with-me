# Explore With Me

Explore With Me is a microservice-based backend application for publishing, searching and participating in events.

The project consists of two independent services:

- **Main Service** — provides the business logic of the application.
- **Statistics Service** — collects and provides event view statistics.

---

## Architecture

The project includes two microservices.

### ewm-main-service

Main application service.

Functionality:

- user management;
- category management;
- event creation;
- event moderation by administrators;
- event publication;
- event search and filtering;
- participation requests;
- event comments;
- event view statistics.

Port:

```
8080
```

---

### stats-server

Statistics service.

Functionality:

- storing endpoint hits;
- calculating event views;
- providing statistics by URI.

Port:

```
9090
```

---

## Technologies

- Java 21
- Spring Boot 3.3.2
- Spring Data JPA
- Hibernate
- PostgreSQL 16
- Maven
- Docker
- Docker Compose
- Lombok

---

## Features

- REST API
- Microservice architecture
- Event management
- Event moderation
- Categories
- Participation requests
- Event comments
- View statistics
- Docker support

---

## Project Structure

```
java-explore-with-me
│
├── ewm-main-service
│
├── stats
│   ├── stats-dto
│   ├── stats-client
│   └── stats-server
│
├── postman
│   └── feature.json
│
├── docker-compose.yml
│
└── README.md
```

---

## Build

Run from the project root:

```bash
mvn clean package
```

After successful build:

```
ewm-main-service/target/ewm-main-service-0.0.1-SNAPSHOT.jar

stats/stats-server/target/stats-server-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

Build and start all services:

```bash
docker compose up --build -d
```

Stop services:

```bash
docker compose down
```

Remove containers and volumes:

```bash
docker compose down -v
```

---

## Databases

### Main Service

Database:

```
ewm_main
```

Port:

```
6542
```

---

### Statistics Service

Database:

```
ewm_stats
```

Port:

```
6541
```

---

## REST API

Main endpoint groups:

### Public API

```
/events
/categories
/comments
```

### User API

```
/users/{userId}/*
```

### Administration API

```
/admin/*
/admin/comments
```

### Statistics API

```
/hit
/stats
```

---

## Statistics

Whenever the following public endpoints are called:

```
GET /events
GET /events/{eventId}
```

the main service sends request information to the Statistics Service.

The statistics service stores endpoint hits and calculates the number of event views.

---

## API Testing

The project includes a Postman collection located at:

```
postman/feature.json
```

The collection automatically performs the following scenario:

- creates a user;
- creates a category;
- creates an event;
- verifies that comments cannot be created for unpublished events;
- publishes the event;
- creates a comment;
- retrieves a comment by id;
- retrieves all comments for an event;
- updates a comment;
- retrieves all comments as administrator;
- deletes a comment by author;
- creates a new comment;
- deletes a comment by administrator.

---

## Pull Request

Feature implementation:

https://github.com/lMoretl/java-explore-with-me/pull/5

---

## Author

Anton Besedin

Java Backend Developer

Developed as a graduation project for the Yandex Practicum Java Developer course.