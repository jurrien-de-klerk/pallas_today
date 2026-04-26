# StoryService

Minimal Java microservice with one endpoint:

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
