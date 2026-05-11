#!/usr/bin/env python3
"""AI remediation agent for security vulnerabilities in dependencies.

Reads OWASP dependency-check and Dart pub audit reports, asks the GitHub
Models API to suggest safe version bumps, and applies those changes to the
relevant dependency files (pom.xml files and pubspec.yaml).
"""

import argparse
import json
import os
import re
import sys
from pathlib import Path


def load_json_file(path: str) -> dict | list | None:
    """Load a JSON file, returning None if the file does not exist or is malformed."""
    p = Path(path)
    if not p.exists():
        print(f"[warn] report not found: {path}")
        return None
    with p.open() as f:
        try:
            return json.load(f)
        except json.JSONDecodeError as e:
            print(f"[warn] report at {path} is not valid JSON, skipping: {e}")
            return None


def extract_owasp_vulnerabilities(report: dict) -> list[dict]:
    """Extract vulnerable dependency info from an OWASP dependency-check JSON report."""
    vulns = []
    dependencies = report.get("dependencies", [])
    for dep in dependencies:
        dep_vulns = dep.get("vulnerabilities", [])
        if not dep_vulns:
            continue
        packages = dep.get("packages", [])
        coord = dep.get("fileName", dep.get("filePath", "unknown"))
        for v in dep_vulns:
            vulns.append(
                {
                    "source": "owasp",
                    "dependency": coord,
                    "packages": packages,
                    "cve": v.get("name", ""),
                    "severity": v.get("severity", ""),
                    "cvss": v.get("cvssv3", {}).get(
                        "baseScore", v.get("cvssv2", {}).get("score", 0)
                    ),
                    "description": v.get("description", ""),
                }
            )
    return vulns


def extract_dart_vulnerabilities(report: dict | list) -> list[dict]:
    """Extract vulnerable package info from a Dart pub audit JSON report."""
    vulns = []
    # `dart pub audit --json` returns {"packages": [...], "advisories": [...]}
    if isinstance(report, list):
        advisories = report
    else:
        advisories = report.get("advisories", [])
    for adv in advisories:
        affected = adv.get("affectedPackages", adv.get("affected", []))
        for pkg in affected:
            vulns.append(
                {
                    "source": "dart",
                    "dependency": pkg.get("name", "unknown"),
                    "current_version": pkg.get("version", "unknown"),
                    "patched_versions": pkg.get(
                        "patchedVersions", adv.get("patchedVersions", [])
                    ),
                    "cve": adv.get("id", ""),
                    "severity": adv.get("severity", ""),
                    "description": adv.get("summary", adv.get("description", "")),
                }
            )
    return vulns


def read_dependency_files() -> dict[str, str]:
    """Read all pom.xml and pubspec.yaml files in the repository."""
    files = {}
    for pattern in ["**/pom.xml", "**/pubspec.yaml"]:
        for p in Path(".").rglob(pattern):
            # Skip generated or build output paths
            parts = p.parts
            if any(
                part in ("target", "build", ".dart_tool", "node_modules")
                for part in parts
            ):
                continue
            files[str(p)] = p.read_text()
    return files


def call_ai_agent(
    vulnerabilities: list[dict], dep_files: dict[str, str], github_token: str
) -> str:
    """Call the GitHub Models API to get remediation suggestions."""
    try:
        from openai import OpenAI
    except ImportError:
        print("[error] openai package not installed. Run: pip install openai")
        sys.exit(1)

    client = OpenAI(
        base_url="https://models.inference.ai.azure.com",
        api_key=github_token,
    )

    vuln_summary = json.dumps(vulnerabilities, indent=2)
    files_summary = "\n\n".join(
        f"=== {path} ===\n{content}" for path, content in dep_files.items()
    )

    prompt = f"""You are a security engineer. The following vulnerabilities were found in project dependencies:

{vuln_summary}

Here are the current dependency files:

{files_summary}

Your task:
1. For each vulnerability, identify the affected dependency in the files above.
2. Suggest the minimum safe version upgrade that resolves the CVE.
3. Return ONLY a JSON object with this exact structure (no prose, no markdown fences):

{{
  "changes": [
    {{
      "file": "<relative path to the file, e.g. pallas_server/StoryService/pom.xml>",
      "dependency": "<dependency name or artifactId>",
      "old_version": "<exact string currently in the file>",
      "new_version": "<replacement string>",
      "cve": "<CVE ID>",
      "reason": "<one sentence explaining the fix>"
    }}
  ]
}}

Rules:
- Only include changes for vulnerabilities that can be fixed by a version bump.
- For Maven pom.xml files, match the <version> tag adjacent to the <artifactId>.
- For pubspec.yaml files, match the version constraint string (e.g. ^5.2.0).
- Use the latest stable patched version known to you.
- If no safe upgrade is known, omit that vulnerability from the output.
- Do not suggest changes for dependencies that are not directly declared in the files."""

    print("[info] Calling GitHub Models API (gpt-4o)...")
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=[{"role": "user", "content": prompt}],
        temperature=0,
        max_tokens=4096,
    )
    return response.choices[0].message.content


def apply_changes(changes: list[dict]) -> list[str]:
    """Apply version bump changes to dependency files. Returns list of modified files."""
    modified = []
    for change in changes:
        path = Path(change["file"])
        if not path.exists():
            print(f"[warn] file not found, skipping: {change['file']}")
            continue
        content = path.read_text()
        old_ver = change["old_version"]
        new_ver = change["new_version"]
        if old_ver not in content:
            print(
                f"[warn] old version '{old_ver}' not found in {change['file']}, skipping"
            )
            continue
        updated = content.replace(old_ver, new_ver, 1)
        path.write_text(updated)
        dep = change["dependency"]
        cve = change["cve"]
        reason = change["reason"]
        print(f"[fix] {change['file']}: {dep} {old_ver} → {new_ver} ({cve}: {reason})")
        if change["file"] not in modified:
            modified.append(change["file"])
    return modified


def main() -> None:
    parser = argparse.ArgumentParser(
        description="AI dependency vulnerability remediation agent"
    )
    parser.add_argument(
        "--owasp-report",
        default=None,
        help="Path to OWASP dependency-check JSON report",
    )
    parser.add_argument(
        "--dart-audit", default=None, help="Path to dart pub audit JSON output"
    )
    parser.add_argument(
        "--java-vuln", default="false", help="Whether Java vulnerabilities were found"
    )
    parser.add_argument(
        "--dart-vuln", default="false", help="Whether Dart vulnerabilities were found"
    )
    args = parser.parse_args()

    github_token = os.environ.get("GITHUB_TOKEN")
    if not github_token:
        print("[error] GITHUB_TOKEN environment variable is required")
        sys.exit(1)

    vulnerabilities: list[dict] = []

    # Load OWASP report
    if args.java_vuln == "true" and args.owasp_report:
        owasp = load_json_file(args.owasp_report)
        if owasp:
            owasp_vulns = extract_owasp_vulnerabilities(owasp)
            print(f"[info] OWASP: {len(owasp_vulns)} vulnerability/ies found")
            vulnerabilities.extend(owasp_vulns)

    # Load Dart audit report
    if args.dart_vuln == "true" and args.dart_audit:
        dart = load_json_file(args.dart_audit)
        if dart:
            dart_vulns = extract_dart_vulnerabilities(dart)
            print(f"[info] Dart: {len(dart_vulns)} vulnerability/ies found")
            vulnerabilities.extend(dart_vulns)

    if not vulnerabilities:
        print("[info] No structured vulnerability data to process. Exiting.")
        sys.exit(0)

    print(f"[info] Total vulnerabilities to remediate: {len(vulnerabilities)}")

    dep_files = read_dependency_files()
    print(f"[info] Dependency files found: {list(dep_files.keys())}")

    raw_response = call_ai_agent(vulnerabilities, dep_files, github_token)
    print(f"[info] AI response:\n{raw_response}")

    # Strip markdown fences if present
    clean = re.sub(r"^```[a-z]*\n?", "", raw_response.strip(), flags=re.MULTILINE)
    clean = re.sub(r"\n?```$", "", clean.strip(), flags=re.MULTILINE)

    try:
        suggestion = json.loads(clean)
    except json.JSONDecodeError as e:
        print(f"[error] Failed to parse AI response as JSON: {e}")
        print(f"[error] Raw response was:\n{raw_response}")
        sys.exit(1)

    changes = suggestion.get("changes", [])
    if not changes:
        print("[info] AI agent found no actionable version bumps.")
        sys.exit(0)

    modified = apply_changes(changes)
    if modified:
        print(f"[info] Modified files: {modified}")
    else:
        print("[info] No files were modified.")


if __name__ == "__main__":
    main()
