# 10. Domain Logic Belongs in the Domain Layer

Date: 2026-05-06

## Status

Superseded by [13. App Domain Layer for Data Aggregation](0013-app-domain-layer-for-data-aggregation.md)

## Context

Each microservice in Pallas Today is structured in four layers: API, Application, Domain, and Data. The domain layer is
the intended home for business rules and domain model behaviour. Without a clear guideline, logic tends to drift into
the application layer or, worse, into API controllers — making it harder to test, reason about, and reuse.

## Decision

All domain logic must be implemented in the domain layer of the microservice that owns it. Business rules, invariants,
and domain model behaviour belong there and nowhere else.

Deviation is permitted only when there is a well-founded technical reason — for example, a rule that is inherently
cross-service and cannot be owned by a single domain model without introducing inappropriate coupling. Any deviation
must be documented with a clear justification in the code (and, if structural, in an ADR).

## Consequences

- Domain logic is easy to locate, test in isolation, and reason about.
- The application layer stays thin: it orchestrates calls but does not contain business rules.
- Developers must actively justify placing logic outside the domain layer, which keeps exceptions visible and
  intentional.
- Cross-service rules may require coordination patterns (e.g. a saga or an anti-corruption layer) rather than shared
  code; this adds some complexity but preserves bounded context isolation.
