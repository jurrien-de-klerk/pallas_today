# 5. Move Java linting and formatting to Maven

Date: 2026-04-27

## Status

Accepted

Supercedes [4. Selective linting and formatting of text files](0004-selective-linting-and-formatting-of-text-files.md)

## Context

While [ADR-0004](0004-selective-linting-and-formatting-of-text-files.md)
established that text-based files should be linted and formatted via `lefthook`,
Java files require specialized tooling that presents unique challenges:

- Docker-based Java formatting in `lefthook` required downloading a 3.5MB JAR
  (google-java-format) from Maven Central on every commit, making pre-commit
  hooks slow and network-dependent
- Docker container tooling for Java is less mature compared to established Maven
  ecosystem plugins
- Java quality tools (Checkstyle, SpotBugs, PMD, formatting) are already
  available as mature, well-maintained Maven plugins
- Pre-commit hooks should be fast and deterministic; network-dependent downloads
  cause failures in offline environments

## Decision

Move Java linting and formatting from `lefthook` pre-commit hooks to Maven
plugins that execute during the `verify` phase.

The following Maven plugins are used for Java quality checks:

- `fmt-maven-plugin` for code formatting (Google Java Format style)
- `maven-checkstyle-plugin` for style checking
- `spotbugs-maven-plugin` for static analysis and null checking
- `maven-pmd-plugin` for code quality analysis

All plugins are bound to the `verify` phase with `failOnViolation=true` to
enforce quality standards before packaging.

The `lefthook` configuration no longer includes Java formatting hooks. Developers
run `mvn clean verify` before committing Java changes, which executes all quality
checks together.

## Consequences

Pre-commit hooks are faster and no longer require network access. Java formatting
and linting now uses mature, deterministic Maven ecosystem tooling instead of
Docker-based solutions.

All Java quality checks (formatting, style, static analysis) run together during
`mvn clean verify`, providing consistent feedback in a single build step.

Developers must remember to run `mvn clean verify` before committing Java code.
The build process enforces quality standards, failing if violations are detected.

Maven plugin configuration is centralized in `pom.xml` files, making it easier
to maintain and version control tooling configuration alongside the code.
