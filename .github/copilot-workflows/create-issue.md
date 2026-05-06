# Workflow: Create an Issue

This checklist expands the mandatory rules in `.github/copilot-instructions.md` into concrete steps. Follow every step
in order — do not skip steps.

## Step 0 — Read instructions (MANDATORY, ordered first)

Before doing anything else, confirm you have already read:

- `.github/copilot-instructions.md`

If you have not read it yet, STOP and read it now before continuing.

## Step 1 — Build context

Use the developer's prompt to understand the description and scope of the change. Apply the following rules:

- If the **description** is unclear, ask the developer specifically to clarify what the feature or bug fix is about
  before proceeding.
- If the **scope** is unclear, ask specifically what is in scope and what is out of scope. Use the following areas as a
  checklist to prompt the conversation:
  - Documentation
  - Tool setup (linters, formatters, CI hooks)
  - `pallas_app` (Flutter frontend)
  - Microservices in `pallas_server/` (ask which subfolder(s))
  - Infrastructure changes (Docker, Kubernetes, `compose.yaml`)
  - API definitions in `api-specs/`

Do not proceed to the next step until the description and scope are clear.

## Step 2 — Estimate size and adjust scope

Estimate the number of lines of code that the in-scope changes are likely to require. If the estimate exceeds 200 lines:

1. Explain to the developer that the scope appears large.
1. Suggest concrete ways to split the work: which areas could be deferred to a separate issue while still delivering
   something meaningful.
1. Adjust the scope based on the developer's response before continuing.

## Step 3 — Ensure out-of-scope work is tracked

For each item listed as out of scope, check whether an existing open issue already covers it. Ask the developer if
unsure. If no issue exists for an out-of-scope item:

- Suggest creating a placeholder issue for it (using `.github/ISSUE_TEMPLATE/placeholder.md`).
- Do not create the placeholder automatically; let the developer decide.
- Once the developer confirms, note any placeholder issue numbers so they can be referenced in the feature issue.

## Step 4 — Determine labels

Assign one or more of the following labels based on what is in scope. **Only use labels from this table — do not suggest
any label not listed here:**

| Area                                                        | Label                 |
| ----------------------------------------------------------- | --------------------- |
| `pallas_app` (Flutter frontend)                             | `frontend`            |
| A microservice implemented in the `pallas_server/` codebase | `backend`             |
| Installing or configuring a third-party service             | `third-party-service` |
| Documentation                                               | `documentation`       |
| CI/CD                                                       | `ci-cd`               |

If you are unsure whether a label exists in the repository, check the repository's label list before suggesting it.

## Step 5 — Draft and create the issue

Fill in `.github/ISSUE_TEMPLATE/feature.md`:

- **Description**: short description of what the feature or bug fix is about.
- **In scope**: bullet list of what is included, using the areas confirmed in step 1.
- **Out of scope**: bullet list of what is explicitly not included; link to tracking issues where they exist.

Present the draft to the developer for review using a YAML code block with `type="draft-issue"` — **never use raw
Markdown for the draft**. Include the proposed labels in the draft.

**Do not call any tool to create the issue until the developer explicitly confirms the draft.** Only after confirmation,
call the GitHub issue creation tool with the repository and issue details.
