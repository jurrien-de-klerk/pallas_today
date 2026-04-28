# 8. Log level usage guidelines

Date: 2026-04-28

## Status

Accepted

## Context

Without a shared definition of what each log level means, developers make inconsistent choices. This makes log output
harder to filter, monitor, and act on. Alerts based on log levels become unreliable when the levels are not used
consistently across the codebase.

## Decision

Each log level must be used according to the following definitions:

- **Error**: something has gone wrong in a way that cannot be recovered from automatically. Examples: an uncaught
  exception that terminates a flow, a required file that is missing at startup, or a critical dependency that is
  unavailable.
- **Warning**: a problem occurred that does not break the current flow but indicates something unexpected or
  undesirable. The application continues operating. Examples: a retry was needed, a deprecated code path was reached, or
  a non-critical configuration value was absent and a fallback was used.
- **Info**: a notable event occurred in the normal flow of the application. Examples: a user action such as a button
  press, an incoming request, or a background job starting and completing.
- **Debug**: detail about the internal execution path of the application, useful when diagnosing a problem. Examples:
  entering a method, the result of a computation, or the branch taken in a conditional.

The trace level is not used.

## Consequences

Log output becomes consistent and predictable. Operators can configure alerting on Error and Warning levels with
confidence that those levels are meaningful. Debug output can be enabled in lower environments without polluting
production logs. Code reviews must verify that the correct level is used for each log call.
