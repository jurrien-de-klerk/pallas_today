# 11. Secure by Design

Date: 2026-05-07

## Status

Accepted

## Context

Security vulnerabilities are significantly cheaper to fix when addressed during design and development rather than after
deployment. The platform handles user data and must protect it from unauthorised access, data leaks, and abuse.
Retrofitting security onto an insecure design is costly and error-prone.

## Decision

The platform is secure by design. Security is a first-class concern at every layer, not an afterthought. The following
principles apply:

- **Least privilege**: every component, service, and user account is granted only the minimum permissions needed to
  perform its function.
- **Defence in depth**: multiple independent security controls are used so that no single failure exposes the system.
- **Fail secure**: when an error or unexpected condition occurs, the system defaults to a denied or safe state rather
  than an open one.
- **No sensitive data in logs**: as established in ADR-0007, log records must not contain personal or sensitive data.
- **Authentication and authorisation on every endpoint**: all API endpoints require explicit authentication and
  authorisation checks; unauthenticated access must be explicitly opted into and justified.
- **Dependency hygiene**: third-party dependencies are kept up to date and reviewed for known vulnerabilities before
  adoption.
- **Secrets management**: secrets, credentials, and keys are never stored in source code or plain-text configuration
  files; they are injected at runtime via a dedicated secrets mechanism.

## Consequences

- New features must include a security review as part of their definition of done.
- Authentication and authorisation logic must be implemented and tested before a feature can be considered complete.
- Introducing a dependency with a known high-severity vulnerability will block a release until the vulnerability is
  resolved or mitigated.
- Developers must be familiar with the OWASP Top 10 and apply its guidance when designing and reviewing changes.
