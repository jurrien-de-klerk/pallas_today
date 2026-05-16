# Introduction

## Domain Vision

Pallas Today is a European social media platform built on the values of privacy, security, and intellectual honesty. It
is designed to bring people together and support collective pursuit of truth — independent of the commercial and
political pressures that shape existing platforms. The domain vision concept is described in [1].

## Vision

A secure, privacy-respecting social media platform that connects people across Europe and empowers them to seek truth
together, free from the influence of large commercial platforms.

## Mission

To build an open, trustworthy social platform that prioritises user privacy and security, fosters genuine connection,
and is developed and hosted independently within Europe.

______________________________________________________________________

## System Structure

Pallas Today consists of a cross-platform mobile and desktop application backed by a set of independently deployable
microservices. This section introduces the overall structure from two complementary perspectives: the service
decomposition (vertical slices) and the internal layering within each component (horizontal slices).

Two complementary principles govern where logic lives:

- **Backend services own their domain logic.** Each microservice encapsulates and enforces its own business rules. Logic
  belongs in the domain layer of the service that owns the data. See
  [ADR-0010](../adr/0010-domain-logic-belongs-in-domain-layer.md).
- **The app domain layer is for aggregation only.** The Pallas App adds a domain layer whose sole responsibility is
  combining data from multiple backend services into composite models for progressive loading. No other business rules
  belong in the app. See [ADR-0013](../adr/0013-app-domain-layer-for-data-aggregation.md).

### Services and Components

The system is composed of the following main building blocks:

- **Pallas App** — the cross-platform frontend, targeting mobile and desktop platforms.
- **Backend services** — a collection of microservices, each responsible for a specific functional domain.
- **Ingress** — acts as an API gateway, routing client requests to the appropriate backend service.

Functionality is distributed across microservices along vertical slices: each service owns a distinct piece of domain
behaviour end-to-end [3].

```mermaid
graph TD
    App[Pallas App]

    subgraph Backend
        Ingress[Ingress / API Gateway]
        S1[Story Service]
        S2[... Service]
        SN[... Service]
    end

    App --> Ingress
    Ingress --> S1
    Ingress --> S2
    Ingress --> SN
```

### Layering Within Components

While the system is decomposed into microservices, each component still follows a layered internal structure inspired by
Domain-Driven Design [1, 4].

**Pallas App** is organised in four layers:

| Layer        | Responsibility                                                                                    |
| ------------ | ------------------------------------------------------------------------------------------------- |
| Presentation | Implements the user interface; renders aggregated models progressively as data becomes available. |
| Application  | Translates UI interactions into calls to the domain layer.                                        |
| Domain       | Aggregates data from multiple backend services into composite models. Contains no other logic.    |
| Proxy        | Generated HTTP client code, derived from the OpenAPI specifications.                              |

The proxy layer is a technical addition outside the DDD model; it keeps generated code clearly separated from
hand-written application logic. The domain layer is restricted to aggregation; all other business rules stay in the
corresponding backend microservice (see [ADR-0013](../adr/0013-app-domain-layer-for-data-aggregation.md)).

**Each microservice** is organised in four layers:

| Layer       | Responsibility                                        |
| ----------- | ----------------------------------------------------- |
| API         | Implements the HTTP endpoints exposed to clients.     |
| Application | Orchestrates use cases; may call other microservices. |
| Domain      | Contains the business logic and domain model.         |
| Data        | Manages interaction with persistent storage.          |

The API layer, like the proxy layer in the app, is a technical addition that keeps transport concerns separate from the
domain model. The rule that domain logic belongs in the domain layer of the owning microservice is recorded in
[ADR-0010](../adr/0010-domain-logic-belongs-in-domain-layer.md).

```mermaid
graph TD
    subgraph Pallas App
        Presentation --> Application_App[Application]
        Application_App --> Domain_App[Domain]
        Domain_App --> Proxy
    end

    subgraph Microservice
        API --> Application_Svc[Application]
        Application_Svc --> Domain
        Domain --> Data
    end

    Proxy -->|HTTP| API
```

### Bounded Contexts

Each component — the Pallas App and every microservice — defines its own bounded context [1, 2]. Within a bounded
context, the domain model and its language are self-contained and consistent; concepts are not shared across context
boundaries.

Components communicate through a **shared kernel**: the OpenAPI specifications stored in `api-specs/`. These
specifications define the contract between contexts — the data structures and operations that cross a boundary. Keeping
the shared kernel minimal and explicitly versioned prevents tight coupling between services while still enabling
structured collaboration. The decision to centralise OpenAPI specs is recorded in
[ADR-0006](../adr/0006-centralised-openapi-spec-storage.md).

This means:

- A microservice owns its domain model entirely; no other component reaches into it directly.
- The OpenAPI spec for a service is the single authoritative definition of what it exposes to the outside world.
- The Pallas App consumes these contracts via generated proxy code, ensuring client and server always agree on the
  interface without sharing implementation.

______________________________________________________________________

## Bibliography

- [1] Eric Evans, *Domain-Driven Design: Tackling Complexity in the Heart of Software*, Addison-Wesley, 2003.
- [2] DDD Crew, *Context Mapping*, <https://github.com/ddd-crew/context-mapping>
- [3] Baeldung, *Vertical Slice Architecture in Java*, <https://www.baeldung.com/java-vertical-slice-architecture>
- [4] DDD Practitioners, *Layered Architecture*, <https://ddd-practitioners.com/home/glossary/layered-architecture/>
