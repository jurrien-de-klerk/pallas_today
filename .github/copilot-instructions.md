# Copilot Instructions

## Architecture Decision Records

All architecture decisions for this project are documented as ADRs in
[`doc/adr/`](../doc/adr/).

**Always read and respect every ADR in `doc/adr/` before responding to any
prompt.** When suggesting code, architecture, tooling, or patterns, ensure your
output is consistent with the decisions recorded there. If a new request
conflicts with an existing ADR, point out the conflict explicitly before
proceeding.

## Workflow: Creating a New ADR

1. From the project root, run:

   ```bash
   adr new <title>
   ```

   Keep the title short (max 7 words).

2. The command creates a new file from the template. Fill in the template
   sections with short, simple text — do not change the structure.

3. From the project root, regenerate the table of contents:

   ```bash
   adr generate toc > doc/adr/readme.md
   ```

## Linting and formatting policy

- Use `lefthook` as the execution entrypoint for linting and formatting.
- Ensure all text-based file types present in the repository are covered by a
   linter and a formatter in `lefthook` configuration, except repository ignore
   files.
- Whenever a new text-based file type is introduced, update `lefthook` in the
   same change to add matching linter/formatter tooling.
- Exception: repository ignore files (for example `.gitignore`) must be
   formatted but do not require a dedicated lint tool.
- Exclude binary files (for example images) from linting/formatting checks.

## Workflow: Committing files

When Copilot commits changes, use this workflow:

1. Stage files from the project root:
   - Use `git add .` when all repository changes should be committed.
   - Otherwise, use `git add <files to commit>` for a selective commit.
2. Start the commit process with `git commit -m <generated commit message>`.
3. If `lefthook` runs linting/formatting and reports issues, analyze the output
   and fix all reported suggestions.
4. Add the changes to git using `git add`
5. Repeat steps 2 and 4 until the commit succeeds.
