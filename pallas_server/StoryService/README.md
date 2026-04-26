# StoryService

Spring Boot microservice for managing stories.

## API

The service provides a CRUD REST API for stories. See [openapi.yaml](openapi.yaml)
for the complete API specification.

**Swagger UI:** Once the application is running, access the interactive API
documentation at `http://localhost:8080/swagger-ui.html`

**Current endpoints:**

- `POST /stories` - Create a story
- `GET /stories` - List all stories (with pagination)
- `GET /stories/{id}` - Get a specific story
- `PUT /stories/{id}` - Update a story
- `DELETE /stories/{id}` - Delete a story

**Storage:** Currently using in-memory storage (ConcurrentHashMap). Data is
lost on restart.

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

Then test it:

```bash
curl http://localhost:8080/hello
```

### Access Swagger UI

Once the application is running, open your browser to:

`http://localhost:8080/swagger-ui.html`

This provides an interactive API documentation where you can test all CRUD
operations.

### Test the API

Create a story:

```bash
curl -X POST http://localhost:8080/stories \
    -H "Content-Type: application/json" \
    -d '{"story": "Once upon a time..."}'
```

List stories:

```bash
curl http://localhost:8080/stories
```
