# Copilot Instructions

## Architecture Decision Records

All architecture decisions for this project are documented as ADRs in [`doc/adr/`](../doc/adr/).

**Always read and respect every ADR in `doc/adr/` before responding to any prompt.** When suggesting code, architecture, tooling, or patterns, ensure your output is consistent with the decisions recorded there. If a new request conflicts with an existing ADR, point out the conflict explicitly before proceeding.

## Workflow: Creating a New ADR

1. From the project root, run:
   ```bash
   adr new <title>
   ```
   Keep the title short (max 7 words).

2. The command creates a new file from the template. Fill in the template sections with short, simple text — do not change the structure.

3. From the project root, regenerate the table of contents:
   ```bash
   adr generate toc > doc/adr/readme.md
   ```
