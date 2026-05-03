# 9. Issue and pull request workflow

Date: 2026-05-03

## Status

Accepted

## Context

AI-assisted code review requires understanding the intended scope of a change. Without a structured workflow the link
between planned scope (issue) and implemented scope (pull request) is implicit, making review unreliable.

## Decision

All code changes follow this workflow:

1. **Create issue** — the developer describes the change; the AI drafts and creates the issue. The developer confirms
   before submission.
1. **Create branch** — linked to the issue.
1. **Make changes** — implementation on the branch.
1. **Create pull request** — the developer asks the AI to create the PR. The AI reads the issue for planned scope, reads
   the code diff for actual changes, cross-checks the two, and presents any differences to the developer. The developer
   confirms before the AI fills in the PR template and creates the PR.

Supporting artefacts in `.github/`:

- `ISSUE_TEMPLATE/feature.md` — issue template for features and bug fixes.
- `ISSUE_TEMPLATE/placeholder.md` — issue template to capture scope for work identified as out of scope in another
  issue.
- `PULL_REQUEST_TEMPLATE.md` — PR description template.
- `copilot-instructions.md` — AI instructions for each step above.

## Consequences

Every PR has a traceable scope definition. AI agents can flag out-of-scope changes. Developers must have an issue before
opening a PR.
