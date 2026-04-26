# StoryService

Minimal Spring Boot microservice with one endpoint:

- `GET /hello` -> `world!`

## Run in container

From `pallas_server` directory:

```bash
docker compose up
```

Docker Compose will automatically build the image (using the Dockerfile)
and start the service. The image includes the compiled Spring Boot
executable.

### BuildKit caching (optional, for faster rebuilds)

The Dockerfile uses Docker BuildKit cache mounts to cache both Maven
dependencies and compiled artifacts between builds. Enable BuildKit for
faster incremental builds:

```bash
DOCKER_BUILDKIT=1 docker compose up
```

This caches:

- `~/.m2` (Maven dependency repository) — skips redownloading dependencies
- `/build/target` (compiled artifacts) — enables incremental compilation

Subsequent builds only recompile changed source files.

Then test it:

```bash
curl http://localhost:8080/hello
```
