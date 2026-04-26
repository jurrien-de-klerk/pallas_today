# StoryService

Minimal Spring Boot microservice with one endpoint:

- `GET /hello` -> `world!`

## Run in container

From `pallas_server` directory:

```bash
docker compose up
```

Then test it:

```bash
curl http://localhost:8080/hello
```

The service is built and started with Maven inside the container.
