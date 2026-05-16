# 13. App Domain Layer for Data Aggregation

Date: 2026-05-16

## Status

Accepted

Supersedes [10. Domain Logic Belongs in the Domain Layer](0010-domain-logic-belongs-in-domain-layer.md)

## Context

ADR 10 established that all domain logic belongs in the domain layer of the microservice that owns it. This remains true
for backend services. However, the Pallas app needs to present aggregated views that combine data from multiple
microservices (e.g. stories with their images and community metadata). Fetching all data sequentially before rendering
makes the app feel slow. A domain layer in the app can hold aggregated models and drive progressive loading, showing
partial results as each service responds.

## Decision

A domain layer is added to the Pallas app. Its sole responsibility is aggregating data from multiple backend services
into composite models that the presentation layer can render progressively.

The following rules apply:

1. **Aggregation only.** The only business logic permitted in the app domain layer is the composition of data from
   different services into a single model. No other business rules may live here.
1. **Backend services retain ownership.** Each microservice continues to encapsulate and enforce its own domain logic.
   The app domain layer is a read-side consumer; it does not duplicate or replace backend rules.
1. **Progressive loading.** Aggregated models are designed to allow partial population, so the presentation layer can
   render available data while outstanding service calls complete.
1. **Exceptions require justification.** Adding any business logic beyond aggregation to the app domain layer is
   permitted only with a strong, documented motivation (in code comments or a follow-up ADR). Where feasible, such logic
   must be pushed to the relevant backend microservice instead.

## Consequences

- The app feels faster: the presentation layer can start rendering as soon as the first service responds.
- Each backend service still owns and enforces its domain logic; bounded contexts remain intact.
- Developers have a clear rule: only aggregation logic belongs in the app domain layer, keeping it thin and testable.
- Any deviation (additional business logic in the app) must be explicitly justified, keeping exceptions visible and
  intentional.
- The app domain layer introduces a thin extra abstraction; developers must be careful not to let it grow beyond its
  aggregation purpose.
