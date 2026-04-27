# 2. branch based development

Date: 2026-04-11

## Status

Accepted

## Context

We need a clear and consistent Git workflow for parallel issue work, controlled integration, and automated pull request
quality checks.

## Decision

We use a branch-based development approach with these rules:

- `main` is the default branch.
- Every issue is developed in its own branch.
- Branches may only be merged into `main` through pull requests.
- Pull requests are automatically reviewed using CodeRabbit.

## Consequences

This improves traceability per issue, enforces review before merge, and keeps `main` protected.

It adds workflow overhead because all changes require a pull request and automated review configuration.
