# Copilot Instructions

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

1. Run `git diff pallas_app/packages/openapi/` to inspect the generated diff.
1. **If the changes are small** (e.g. a new field added to an existing model, a new operation method, a security scheme
   added — limited to files that directly correspond to the OpenAPI spec change in the same commit):
   - The changes are expected. Stage them with `git add pallas_app/packages/openapi/` and retry the commit.
1. **If the changes are large or wide-ranging** (e.g. many files regenerated, structural changes to the client package,
   dependency version bumps, changes unrelated to the current spec diff):
   - Do not stage and commit automatically.
   - Stop and report to the programmer. Include:
     - Which files changed and a brief description of what changed in each.
     - Which part of the OpenAPI spec change caused it.
     - Any files that changed without an obvious corresponding spec change (potential generator side-effects).
   - The programmer must review and explicitly confirm before the generated code is staged and committed.

## Commit troubleshooting requirements

- Never suggest or use `git commit --no-verify`.
- If a commit fails, explain the exact hook/tooling errors clearly.
- If issues are fixable in this repository (for example `lefthook` command setup or file-level lint/format findings),
  fix them and retry the commit using normal hooks.
- Only proceed with standard verified commits that pass configured hooks.

## Code review scope

- During code review, do not review generated Flutter platform code.
- Exclude these generated platform directories from review scope: `pallas_app/ios/`, `pallas_app/android/`,
  `pallas_app/linux/`, `pallas_app/macos/`, `pallas_app/web/`, `pallas_app/windows/`.
- Do not review generated Dart client code in `pallas_app/packages/openapi/`. This code is auto-generated from OpenAPI
  specs in `api-specs/` and must not be manually edited or reviewed.
