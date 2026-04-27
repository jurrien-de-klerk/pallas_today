# 6. centralized openapi spec storage

Date: 2026-04-27

## Status

Accepted

## Context

The pallas_server consists of multiple microservices. The pallas_app is a client
that consumes a subset of those services. Each service exposes an HTTP API.

Without a clear convention, API contracts risk being defined ad-hoc inside each
service module, making it hard for the app and other consumers to discover and
use them, and creating duplication when both a server and a client need to
generate code from the same spec.

## Decision

All microservice APIs must be documented using the OpenAPI specification format.

All OpenAPI specs are stored in a central `api-specs/` directory at the
repository root. Each service has its own subdirectory:

```
pallas_today/
  api-specs/
    story-service/
      openapi.yaml
    <other-service>/
      openapi.yaml
  pallas_server/
    StoryService/
    ...
  pallas_app/
    ...
```

The `api-specs/` directory is the single source of truth for all API contracts.

- Each Java microservice references the spec in `api-specs/<service>/openapi.yaml`
  to generate server-side code via the Maven OpenAPI Generator plugin.
- The pallas_app references the relevant specs in `api-specs/` to generate a
  Dart client. A manifest file `api-specs/pallas-app-apis.yaml` lists which
  specs are exposed to the app, making the app-facing API boundary explicit.

## Consequences

- API contracts are discoverable in one place, independent of implementation.
- Both server codegen and client codegen are driven from the same spec,
  eliminating drift between server and client.
- The app-facing API boundary is explicit and auditable via the manifest file.
- Moving an existing spec (such as `pallas_server/StoryService/openapi.yaml`)
  to `api-specs/story-service/openapi.yaml` is a one-time migration cost.
- All contributors must place new specs in `api-specs/` rather than inside the
  service module.
