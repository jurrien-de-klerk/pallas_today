# 14. Log level usage guidelines v2

Date: 2026-05-20

## Status

Accepted

Supersedes [8. Log level usage guidelines](0008-log-level-usage-guidelines.md)

## Context

[ADR 8](0008-log-level-usage-guidelines.md) defined Error as a crash-only level, leaving non-fatal malfunctions (e.g. a
downstream service unavailable) without a backtrace in the log, making them harder to diagnose.

A Fatal level is introduced and Error is redefined to cover non-fatal malfunctions, both with a backtrace.

## Decision

- **Fatal**: application crashes and cannot continue. Backtrace written. Example: unhandled top-level exception.
- **Error**: malfunction that does not crash the application but must be investigated. Backtrace written. Example: a
  required downstream service is unavailable.
- **Warning**: suspicious situation the application recovered from automatically. No backtrace. Example: a retry was
  needed or a fallback value was used.
- **Info**: notable event in normal flow. Example: incoming request, background job completed.
- **Debug**: internal execution detail useful when diagnosing a problem.

The trace level is not used.

## Consequences

Fatal and Error events include a backtrace, making it easy to pinpoint root causes. Warning remains noise-free. Code
reviews must verify the correct level is used, especially distinguishing Error from Warning.
