# Copilot PR workflow (MANDATORY)

## PR-trigger phrases

Whenever the user's request contains any of the following phrases — **"create a PR"**, **"open a PR"**, **"make a pull
request"**, **"generate a pull request"**, **"PR for issue"** — the rules in this section apply **immediately and
without exception**.

## Rule 0 — Always read these instructions first (ORDERING CONSTRAINT)

When the user asks to create, open, make, or generate a pull request, you MUST first open and read **all** of the
following files before taking any other action:

1. `.github/copilot-instructions.md` (this file)
1. `.github/PULL_REQUEST_TEMPLATE.md`

Only AFTER you have read every file listed above, proceed with the PR creation steps described in
`.github/copilot-workflows/create-pr.md`.

If any of the above files cannot be accessed or read, you MUST STOP and ask the user to provide them or grant access
before continuing.

## Rule 1 — Pre-flight checklist (MUST complete before touching any code or description)

1. Confirm the `owner/repo` and the target base branch.
1. Open and read the referenced issue in full; extract the acceptance criteria, in-scope, and out-of-scope items.
1. Identify the tests that need to pass and the hook/lint commands that must run (this repo uses `lefthook`).

## Rule 2 — Hook / formatting gate

All changes MUST pass `lefthook run pre-commit` before the PR is created or updated. If any new text-based file types
are introduced by the change, you MUST update `lefthook.yml` in the same commit to add matching linter/formatter tooling
for those types (repository ignore files are exempt from linting but must still be formatted).

## Rule 3 — PR description requirements

The PR description MUST:

- Include `Closes #<issue-number>` in the **Related issue** section.
- Fill in the **In scope** and **Out of scope** sections from the linked issue.
- Describe any deviations from planned scope under **Scope changes**.
- Include a short **Testing** section listing the commands you ran and what was verified.

______________________________________________________________________

## Copilot issue workflow (MANDATORY)

### Issue-trigger phrases

Whenever the user's request contains any of the following phrases — **"create an issue"**, **"open an issue"**, **"make
an issue"**, **"create a ticket"**, **"file an issue"** — the rules in this section apply **immediately and without
exception**.

### Rule 0 — Read instructions before creating an issue (ORDERING CONSTRAINT)

When the user asks to create, open, make, or file an issue, you MUST first open and read **all** of the following files
before taking any other action:

1. `.github/copilot-instructions.md` (this file)

Only AFTER you have read every file listed above, proceed with the issue creation steps described in
`.github/copilot-workflows/create-issue.md`.

If any of the above files cannot be accessed or read, you MUST STOP and ask the user to provide them or grant access
before continuing.

______________________________________________________________________

## Architecture Decision Records

All architecture decisions for this project are documented as ADRs in [`doc/adr/`](../doc/adr/).

**Always read and respect every ADR in `doc/adr/` before responding to any prompt.** When suggesting code, architecture,
tooling, or patterns, ensure your output is consistent with the decisions recorded there. If a new request conflicts
with an existing ADR, point out the conflict explicitly before proceeding.

## Workflow: Creating a New ADR

1. From the project root, run:

   ```bash
   adr new <title>
   ```

   Keep the title short (max 7 words).

1. The command creates a new file from the template. Fill in the template sections with short, simple text — do not
   change the structure.

1. From the project root, regenerate the table of contents:

   ```bash
   adr generate toc > doc/adr/readme.md
   ```

## Linting and formatting policy

- Use `lefthook` as the execution entrypoint for linting and formatting.
- Ensure all text-based file types present in the repository are covered by a linter and a formatter in `lefthook`
  configuration, except repository ignore files.
- Whenever a new text-based file type is introduced, update `lefthook` in the same change to add matching
  linter/formatter tooling.
- Exception: repository ignore files (for example `.gitignore`) must be formatted but do not require a dedicated lint
  tool.
- Exclude binary files (for example images) from linting/formatting checks.

## Workflow: Committing files

When Copilot commits changes, use this workflow:

1. Stage files from the project root:
   - Use `git add .` when all repository changes should be committed.
   - Otherwise, use `git add <files to commit>` for a selective commit.
1. Start the commit process with `git commit -m <generated commit message>`.
1. If `lefthook` runs linting/formatting and reports issues, analyze the output and fix all reported suggestions.
1. Stage the changed files from the project root:
   - Use `git add .` when all repository changes should be committed.
   - Otherwise, use `git add <files to commit>` for a selective commit.
1. Repeat steps 2 and 4 until the commit succeeds.

## Workflow: Dart client codegen changes

When the `Dart client codegen guard` hook blocks a commit because `pallas_app/packages/openapi/` changed:

1. Run `git diff --stat pallas_app/packages/openapi/` to see which files changed.
1. **If only documentation files changed** (i.e. all changed files are `*.md`):
   - These are expected, low-risk doc updates that correspond directly to the spec change.
   - Stage them with `git add pallas_app/packages/openapi/` and retry the commit without asking.
1. **If code files changed alongside or instead of docs** (e.g. `lib/src/**/*.dart`), run
   `git diff pallas_app/packages/openapi/` to read the full diff, then apply the rules below.
   - **Small and clearly spec-driven** (e.g. a new field on one model, a new operation method, the `secure` metadata
     updated on existing operations — all directly traceable to the OpenAPI spec change in the same commit):
     - Stage with `git add pallas_app/packages/openapi/` and retry the commit without asking.
   - **Large or wide-ranging** (e.g. many files regenerated, structural client changes, dependency version bumps,
     changes in files unrelated to the current spec diff):
     - Do **not** stage and commit.

     - Stop and report to the programmer with the following structure:

       ```text
       Dart client codegen produced changes that require your review before committing.

       Changed files:
       - <file path>: <one-line description of what changed>
       - ...

       Likely cause: <which part of the OpenAPI spec change triggered this>

       Unexpected changes (no obvious spec cause):
       - <file path>: <description> — please verify this is not a generator side-effect

       To proceed: review the changes above, then ask to commit changes again."
       ```

## Commit troubleshooting requirements

- Never suggest or use `git commit --no-verify`.
- If a commit fails, explain the exact hook/tooling errors clearly.
- If issues are fixable in this repository (for example `lefthook` command setup or file-level lint/format findings),
  fix them and retry the commit using normal hooks.
- Only proceed with standard verified commits that pass configured hooks.

## Workflow: Creating an issue

The developer describes the intended change; the AI guides the process and drafts the issue. The developer must review
and confirm before the issue is created.

Follow the steps described in `.github/copilot-workflows/create-issue.md`.

## Workflow: Creating a pull request

Use this workflow when the developer asks to create a GitHub pull request.

1. Identify the linked issue number. If no issue exists, ask the developer to create one first.
1. Read the linked issue in full to establish the planned scope: description and in/out-of-scope sections.
1. Run `git diff main...HEAD` (or the appropriate base branch) to read the actual code changes.
1. Cross-check planned scope against actual changes:
   - List changes that are within the in-scope items.
   - List changes that fall outside the in-scope items.
   - List in-scope items that appear unaddressed by the diff.
1. Present the cross-check findings to the developer and wait for their response before filling in the PR template. The
   developer may confirm, adjust scope, or create placeholder issues for out-of-scope work.
1. Fill in `.github/PULL_REQUEST_TEMPLATE.md` using the issue, the diff, and the confirmed cross-check outcome:
   - **Related issue**: set `Closes #<number>`.
   - **Original scope / In scope**: copy the in-scope items from the linked issue.
   - **Original scope / Out of scope**: copy the out-of-scope items from the linked issue.
   - **Scope changes**: describe any differences between the planned scope and the actual changes. If out-of-scope
     changes exist, create a placeholder issue first (using `.github/ISSUE_TEMPLATE/placeholder.md`) and reference it
     here.
1. After the pull request is created, perform a code review (see Workflow: AI code review below).

## Workflow: AI code review

Perform this review after a pull request is created or when explicitly asked.

1. Read the linked issue in full to understand the description and in/out-of-scope sections.
1. Read all changed files in the pull request, excluding generated code (see Code review scope below).
1. For each changed file, check:
   - Is the change within the declared in-scope items?
   - Does it violate any ADR in `doc/adr/`?
   - Are there obvious correctness issues, security concerns, or missing error handling?
1. Flag any in-scope items not addressed by the diff.
1. Flag any changes that fall outside the in-scope items; suggest they move to a separate issue.
1. Present findings grouped as: **Scope issues**, **ADR violations**, **Code issues**. If a group is empty, omit it.

## Code review scope

- During code review, do not review generated Flutter platform code.
- Exclude these generated platform directories from review scope: `pallas_app/ios/`, `pallas_app/android/`,
  `pallas_app/linux/`, `pallas_app/macos/`, `pallas_app/web/`, `pallas_app/windows/`.
- Do not review generated Dart client code in `pallas_app/packages/openapi/`. This code is auto-generated from OpenAPI
  specs in `api-specs/` and must not be manually edited or reviewed.
