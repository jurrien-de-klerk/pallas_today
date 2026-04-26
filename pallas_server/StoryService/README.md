# StoryService

Minimal Spring Boot microservice with one endpoint:

- `GET /hello` -> `world!`

## Prerequisites

- Java 21 (eclipse-temurin or similar)
- Maven 3

## Build locally

From the `StoryService` directory:

```bash
mvn clean package
```

This builds the Spring Boot executable JAR in `target/storyservice-0.0.1-SNAPSHOT.jar`.

## Run in container

From `pallas_server` directory, after building locally:

```bash
docker compose up --build
```

Docker Compose will build the image using the pre-compiled JAR and start the service.

Then test it:

```bash
curl http://localhost:8080/hello
```

## Run locally (without Docker)

From `StoryService` directory:

```bash
mvn spring-boot:run
```

Then test:

```bash
curl http://localhost:8080/hello
```
