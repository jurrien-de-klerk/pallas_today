# 15. Service-prefixed URL path segments

Date: 2026-05-22

## Status

Accepted

## Context

Each microservice exposes its own HTTP API. Without a shared URL convention, it is not obvious from a path alone which
service owns an endpoint.

## Decision

The first path segment identifies the owning service: drop the "Service" suffix, lowercase, and pluralise (e.g.
`MemberService` → `/members`, `StoryService` → `/stories`, `CommunityService` → `/communities`). All paths in a
service's OpenAPI spec must start with this segment.

## Consequences

- The owning service is immediately visible from any URL.
- Routing by first segment becomes trivial for API gateways and proxies.
- Existing specs that predate this decision must be updated.
- New service names must not produce a first-segment collision.
