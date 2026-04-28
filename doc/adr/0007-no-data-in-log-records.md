# 7. No data in log records

Date: 2026-04-28

## Status

Accepted

## Context

Log records are typically stored in files, forwarded to log aggregation systems, and accessible to operators and
tooling. If application data (such as user input, personal details, or business entities) is included in log messages,
sensitive information can leak outside the application boundary, violating privacy requirements and increasing the
attack surface.

## Decision

Application data must never appear in log records. Log messages may only contain information that describes the
behaviour or flow of the application. Permitted log content includes:

- Counts (for example number of items processed)
- Sizes (for example payload byte length)
- Durations and timestamps
- Identifiers that carry no personal or sensitive meaning on their own (for example a generated correlation ID)
- State transitions and code paths

## Consequences

Log records remain safe to store, forward, and inspect without risk of leaking sensitive data. Developers must be
deliberate when writing log statements and avoid passing data objects or user-supplied values into log messages. Code
reviews must check log calls as part of their standard scope.
