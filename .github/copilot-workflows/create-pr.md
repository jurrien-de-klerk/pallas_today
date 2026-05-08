# Workflow: Create a Pull Request

This checklist expands the mandatory rules in `.github/copilot-instructions.md` into concrete steps. Follow every step
in order — do not skip steps.

## Step 0 — Read instructions (MANDATORY, ordered first)

Before doing anything else, confirm you have already read:

- `.github/copilot-instructions.md`
- `.github/PULL_REQUEST_TEMPLATE.md`

If you have not read them yet, STOP and read them now before continuing.

## Step 1 — Gather context

1. Identify the linked issue number from the user's request.
1. Read the linked issue in full: description, in-scope items, out-of-scope items, and acceptance criteria.
1. Confirm the target base branch (default: `main`).
1. Run `git diff <base>...HEAD` to read all actual code changes.

## Step 2 — Cross-check scope

Compare the planned scope (from the issue) against the actual diff:

- List changes that are within the declared in-scope items.
- List changes that fall outside the in-scope items.
- List in-scope items that appear unaddressed by the diff.

Present findings to the developer and wait for confirmation before writing the PR description.

## Step 3 — Verify hooks pass

Run the pre-commit hook from the repository root:

```bash
lefthook run pre-commit
```

All checks must pass. Fix any reported issues and re-run until clean.

## Step 4 — Security risk notice

There are two sources of security risk to check.

**1. Risk from the linked issue** — check whether the linked issue carries the `security-risk` label. If yes, the PR
description must include a security risk notice that restates the risk, describes what was done to mitigate it, and
directs the reviewer to the relevant files or patterns.

**2. Risk introduced by scope changes** — review the items listed under Scope changes (changes that fall outside the
planned issue scope). Assess each scope change for security risk using the same checklist as the issue workflow
(authentication, authorisation, input handling, secrets, dependencies, access control). If any scope change introduces a
risk, it must also be covered in the security risk notice.

If neither source produces a risk, omit the Security risk notice section entirely.

## Step 5 — Fill in the PR template

Using `.github/PULL_REQUEST_TEMPLATE.md` as the structure:

- **Related issue**: `Closes #<issue-number>`
- **In scope**: bullet list copied from the linked issue (adjusted for confirmed scope)
- **Out of scope**: bullet list copied from the linked issue; link to tracking issues where they exist
- **Scope changes**: describe any differences between planned scope and actual changes; create placeholder issues for
  out-of-scope work found in the diff
- **Security risk notice**: include when the linked issue has the `security-risk` label or when a scope change
  introduces a risk (see step 4); omit otherwise
- **Testing**: list every command run and what was verified (e.g. `lefthook run pre-commit` — all hooks passed)

## Step 6 — Create the pull request

Only after the developer confirms the draft, create the pull request using the filled-in template.

## Step 7 — Perform AI code review

After the PR is created, follow the **Workflow: AI code review** steps in `.github/copilot-instructions.md`.
