# 16. Disable PMD GuardLogStatement rule

Date: 2026-06-09

## Status

Accepted

## Context

The PMD rule `GuardLogStatement` flags log statements that evaluate a value without an `isXxxEnabled()` guard. The
rule's intent is valid: it avoids evaluating expensive expressions for log records that are discarded anyway.

However, the situation needs more nuance. Guarding a log statement also prevents it from entering the log-backtrace
buffer, which is undesirable: backtraces must contain the full picture of what happened before a Fatal or Error event
(see [ADR 14](0014-log-level-usage-guidelines-v2.md)).

## Decision

The PMD rule `GuardLogStatement` is disabled in all PMD rulesets. Instead, developers are responsible for ensuring that
no expensive function calls are evaluated inside log statements.

## Consequences

Log statements always reach the log-backtrace buffer, keeping backtraces complete. No guard boilerplate is needed around
log calls. The risk of accidentally evaluating expensive expressions in log statements is no longer caught by static
analysis; code reviews must watch for expensive calls in log statements.
