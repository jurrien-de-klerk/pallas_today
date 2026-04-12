# 3. lint and format text files

Date: 2026-04-11

## Status

Superseded by [4. Selective linting and formatting of text files](0004-selective-linting-and-formatting-of-text-files.md)

## Context

Text-based files are part of the product quality surface and should stay
readable, consistent, and easy to review.

Without a single execution entrypoint for linting and formatting, checks are
applied inconsistently, and pull requests become noisy with avoidable style
drift.

At the same time, binary files (for example images) are not suitable for text
linting/formatting.

## Decision

We lint and format all text-based files in this repository.

We use `lefthook` as the standard tool to execute all linters and formatters.

The repository must keep `lefthook` configuration complete for all currently
used text-based file types.

Whenever a new text-based file type is introduced, an appropriate linter and
formatter for that file type must be added to `lefthook` configuration as part
of the same change.

Repository ignore files (for example `.gitignore`) are an exception to the
linter requirement: they should be formatted, but do not require a dedicated
lint tool.

This decision is explicitly limited to text-based files. Binary files (such as
images) are excluded.

## Consequences

Formatting and linting become consistent and repeatable across contributors.

Pull requests should contain fewer style-only changes and clearer reviews.

Contributors must maintain `lefthook` configuration as file types evolve.

If a new text file type is added without extending `lefthook`, quality checks
will be incomplete and must be corrected.

Ignore files remain covered by formatting checks, while avoiding unnecessary
lint-tool requirements.

Binary assets are intentionally not passed through lint/format tools, avoiding
meaningless checks.
