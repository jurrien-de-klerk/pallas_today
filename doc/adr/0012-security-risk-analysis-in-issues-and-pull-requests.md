# 12. Security Risk Analysis in Issues and Pull Requests

Date: 2026-05-08

## Status

Accepted

Implements [11. Secure by Design](0011-secure-by-design.md)

## Context

ADR-0011 requires a security review as part of the definition of done. Without a concrete process, security
considerations are easily missed during issue writing and code review.

## Decision

Every issue must include a security risk analysis. Issues with an identified risk are labelled `security-risk`; the
description must state the risk, its impact, and its mitigation. Pull requests linked to a `security-risk` issue must
include a security risk notice that restates the risk, describes the mitigation, and directs the reviewer to the
relevant parts of the change.

Issues labelled `placeholder` are exempt. When a placeholder is refined, the `placeholder` label is removed and a
security risk analysis must be performed as part of that refinement.

## Consequences

- The issue and pull request templates must be updated to include the new sections.
- The `security-risk` label must exist in the GitHub repository.
- When uncertain, contributors should err on the side of adding the label.
- Reviewers must explicitly address any security risk notice in their review.
