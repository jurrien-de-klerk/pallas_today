# Pallas Today

## Introduction

Pallas Today is a European social media platform built on the values of privacy, security, and intellectual honesty. It
is designed to bring people together and support collective pursuit of truth — independent of the commercial and
political pressures that shape existing platforms.

**Vision:** A secure, privacy-respecting social media platform that connects people across Europe and empowers them to
seek truth together, free from the influence of large commercial platforms.

**Mission:** To build an open, trustworthy social platform that prioritises user privacy and security, fosters genuine
connection, and is developed and hosted independently within Europe.

## Architecture

The architecture of Pallas Today is documented in [doc/architecture/readme.md](doc/architecture/readme.md).

All architectural decisions for this project are documented as Architecture Decision Records (ADRs).

See [doc/adr/readme.md](doc/adr/readme.md) for the complete list of ADRs.

## Getting Started

### Prerequisites

- Linux machine, or Windows with WSL2 installed
- If using WSL2: VS Code with the WSL extension installed

### Install Required Tools

#### 1. Flutter

Flutter includes the Dart SDK. Follow the official installation guide:

<https://docs.flutter.dev/get-started/install/linux>

After installation, verify:

```bash
flutter doctor
```

#### 2. Java 21

```bash
sudo apt install openjdk-21-jdk -y
```

Verify installation:

```bash
java -version
```

#### 3. Maven

```bash
sudo apt install maven -y
```

Verify installation:

```bash
mvn -version
```

#### 4. Docker and Docker Compose

Follow the official Docker Engine installation guide for your Linux distribution:

<https://docs.docker.com/engine/install/>

After installation, add your user to the docker group:

```bash
sudo usermod -aG docker $USER
```

Log out and back in for the group change to take effect.

#### 5. Lefthook

```bash
curl -fsSL https://github.com/evilmartians/lefthook/releases/latest/download/lefthook_linux_amd64 -o /tmp/lefthook
sudo install /tmp/lefthook /usr/local/bin/lefthook
```

Install git hooks from the project root:

```bash
lefthook install
```

Verify lefthook is working by running the pre-commit hooks:

```bash
lefthook run pre-commit
```

This executes all configured linting and formatting tools. All checks should pass on a clean repository.

#### 6. ADR Tools

```bash
sudo apt install adr-tools -y
```

Verify installation:

```bash
adr help
```

### Verify Setup

Once all tools are installed, verify your environment:

```bash
flutter doctor
java -version
mvn -version
docker --version
docker compose version
adr help
```

### Development Workflow

Before committing changes, run linting and formatting through lefthook:

```bash
lefthook run pre-commit
```

This is the main entrypoint for all lint and format checks. When you commit, these hooks run automatically.
