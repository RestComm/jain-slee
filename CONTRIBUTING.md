# Contributing to micro-jainslee

Thank you for your interest in contributing to **micro-jainslee**! This
document explains how to set up a development environment, file issues,
submit pull requests, and run the AI-assisted CI pipeline that the
project uses for automated test planning.

**Maintainer:** Tran Nhan ([nhanth87@gmail.com](mailto:nhanth87@gmail.com))
**License:** Dual — GPLv3 OR Commercial (see [`LICENSE`](LICENSE))

---

## Table of contents

1. [Code of conduct](#code-of-conduct)
2. [What to contribute](#what-to-contribute)
3. [Development setup](#development-setup)
4. [Building and testing locally](#building-and-testing-locally)
5. [Coding style](#coding-style)
6. [Commit message format](#commit-message-format)
7. [Pull request process](#pull-request-process)
8. [Issue triage and labels](#issue-triage-and-labels)
9. [AI-assisted CI (OpenHands + DeerFlow)](#ai-assisted-ci-openhands--deerflow)

---

## Code of conduct

This project follows the Contributor Covenant (see
[`CODE_OF_CONDUCT.md`](CODE_OF_CONDUCT.md)). By participating you agree
to its terms.

---

## What to contribute

micro-jainslee is R&D software, so the most useful contributions are:

- **Bug reports** with a minimal reproducer (Java version, OS,
  expected vs actual, full stack trace).
- **Tests** that exercise an existing class with a new scenario —
  especially edge cases in `EventRouter`, `VirtualThreadSbbEntityPool`,
  or `SleeTimerSchedulerBridge`.
- **Documentation improvements** — typos, broken links, unclear
  phrasing in `README.md` or `docs/`.
- **Performance benchmarks** — your numbers from running
  `SbbEntityPoolStressTest` on different hardware (please include
  `lscpu`, `java -version`, and the test output).
- **TCK gap analysis** — if you run a slice of the JAIN SLEE 1.1 TCK
  against micro-jainslee and find a spec deviation, open an issue
  with the TCK test name and the deviation.
- **Jakarta EE / Spring Boot / Quarkus integration samples** — small
  apps showing how to wire micro-jainslee into your stack.

**Out of scope** for this repo: production-grade TCK compliance, cluster
mode, JSR-77 management MBeans, JTA integration — these are tracked as
"known limitations" in [`CHANGELOG.md`](CHANGELOG.md) and are owned by
the upstream RestComm JAIN-SLEE v8 codebase.

---

## Development setup

### Prerequisites

- **JDK 25 LTS** — required to exercise Java 25 virtual threads and the
  full stress test. The build source/target is Java 25 across all modules.
- **Maven 3.9+**.
- **Git**.

### Fork & clone

```bash
git clone https://github.com/<your-fork>/jain-slee.git
cd jain-slee
git checkout micro-jainslee
```

### Install the API jar first (chicken-and-egg workaround)

`jainslee-apt` and `jainslee-core` both depend on `jainslee-api` being in
your local `~/.m2/repository`. Build it once before any other goal:

```bash
mvn -pl jainslee-apt install -DskipTests
```

Now you can run any other module.

---

## Building and testing locally

### Full reactor build + test (~17 seconds on a 4-core machine)

```bash
mvn -pl jainslee-api,jainslee-core,jainslee-apt,jainslee-spring-boot-starter,adapters/adapter-quarkus/runtime,adapters/adapter-quarkus/deployment,adapters/adapter-jakartaee -am test
```

### Stress test (Java 25 only — virtual threads required)

```bash
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest -DargLine="-Xmx4g -XX:+UseZGC"
```

See [`docs/run-testcase-100k-sbb.md`](docs/run-testcase-100k-sbb.md) for
the full guide.

### APT fixture test

```bash
mvn -pl jainslee-apt install -DskipTests
mkdir -p /tmp/apt-test/src/com/example
cat > /tmp/apt-test/src/com/example/MySbb.java <<'JAVA'
package com.example;
import com.microjainslee.api.annotations.SbbAnnotation;
@SbbAnnotation(name="MySbb", vendor="com.example", version="1.0")
public class MySbb { }
JAVA
CP="$HOME/.m2/repository/com/microjainslee/jainslee-api/1.1.0/jainslee-api-1.1.0.jar:$HOME/.m2/repository/com/microjainslee/jainslee-apt/1.1.0/jainslee-apt-1.1.0.jar:$HOME/.m2/repository/org/apache/logging/log4j/log4j-api/2.23.1/log4j-api-2.23.1.jar:$HOME/.m2/repository/org/apache/logging/log4j/log4j-core/2.23.1/log4j-core-2.23.1.jar"
mkdir -p /tmp/apt-test/out
javac -cp "$CP" -processorpath "$CP" -d /tmp/apt-test/out /tmp/apt-test/src/com/example/*.java
cat /tmp/apt-test/out/META-INF/microjainslee/sbb-index.properties
```

### JavaDoc

```bash
mvn -pl jainslee-api,jainslee-core javadoc:aggregate
# Browse: jainslee-api/target/reports/apidocs/index.html
```

---

## Coding style

These rules are enforced by review; there is no Checkstyle plugin yet.

- **Java 25 source/target** on every micro-jainslee module — uses
  `Thread.ofVirtual()` natively for virtual threads.
- **One top-level class per file**, with the file name matching the
  class.
- **All new logging via `org.apache.logging.log4j.Logger`** via
  `LogManager.getLogger(MyClass.class)`. Never `System.out.println`
  or `System.err`.
- **`jakarta.*` for Spring Boot 3 + Quarkus 3 + Jakarta EE 9**
  adapters. **`javax.annotation.processing.*` is fine** for the
  annotation processor (it is JDK API, not the EE namespace debate).
- **`javax.naming.*` for JNDI** in the Jakarta EE adapter (Jakarta EE 9
  left JNDI for a later release).
- **License header** at the top of every `.java` file (see the existing
  files for the template).
- **Javadoc** for every public class and every public method that is
  part of the JAIN SLEE spec contract.
- **One `@Test` per scenario** — do not pack multiple assertions into a
  single test method.

---

## Commit message format

We use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

| Type | Use for |
|---|---|
| `feat` | New feature visible to users |
| `fix` | Bug fix |
| `docs` | Documentation only |
| `style` | Formatting / whitespace only (no logic change) |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `perf` | Performance improvement with a benchmark |
| `test` | Adding or fixing tests |
| `chore` | Build, CI, dependencies, license, governance |

### Scope (the component name)

Pick one of: `jainslee-api`, `jainslee-core`, `jainslee-apt`,
`jainslee-spring-boot-starter`, `adapter-quarkus`, `adapter-jakartaee`,
`docs`, `ci`, `pom`.

### Examples

```
feat(jainslee-core): per-SBB virtual thread pinning

Adds VirtualThreadSbbEntityPool, one parked virtual thread per SBB ID
with a LinkedBlockingQueue<Runnable> for event delivery. Guarantees
JAIN SLEE §8.4 single-threaded per-SBB ordering without locks.

Closes #42
```

```
fix(jainslee-core): clear pool entity on shutdown race

Previously a race between acquire() and shutdown() could leave a
SbbEntity in the map with shutdown=true but queue non-empty. Now we
drain the queue in shutdown() before closing the executor.
```

```
docs: dual license and design docs

Replace inherited AGPL-3.0 with GPLv3 + Commercial dual license.
Add docs/microjainslee-design.md and docs/run-testcase-100k-sbb.md.
```

### Commit author

All commits in this repository are authored by **Tran Nhan**
(`nhanth87 <nhanth87@users.noreply.github.com>`) — the sole maintainer.
External contributions are typically rebased / squashed before merge,
so the final commit author will be the maintainer unless otherwise
agreed.

---

## Pull request process

1. **Fork** the repo and create a feature branch off `micro-jainslee`:
   `git checkout -b feat/my-change`
2. **Make focused commits** — one logical change per commit, with a
   Conventional Commits subject.
3. **Run the full local test suite** before pushing:
   `mvn -pl jainslee-api,jainslee-core,jainslee-apt,jainslee-spring-boot-starter,adapters/adapter-quarkus/runtime,adapters/adapter-quarkus/deployment,adapters/adapter-jakartaee -am test -DargLine="-Xmx4g"`
4. **Add new tests** for any bug fix or new behavior.
5. **Update docs** if your change affects the public API or the
   `README.md` quickstart.
6. **Open a PR** against `micro-jainslee`. The CI workflow
   (`.github/workflows/ci.yml`) runs build + test + license-header
   check + APT fixture test on JDK 17 and JDK 25.
7. **Address review comments** within 14 days. After that the PR may be
   closed to keep the queue manageable.

PRs that touch the production JBoss stack under `container/`, `api/`,
or `tools/` will be **rejected** — those directories still ship under
their original AGPL-3.0 from RestComm and are out of micro-jainslee scope.

---

## Issue triage and labels

| Label | Meaning |
|---|---|
| `bug` | Confirmed reproducible defect |
| `enhancement` | New feature proposal |
| `docs` | Documentation-only change |
| `question` | Support / clarification |
| `duplicate` | Already tracked elsewhere |
| `wontfix` | Will not be addressed (with rationale) |
| `good first issue` | Beginner-friendly, low complexity |
| `help wanted` | Maintainer requests community help |
| `ai-test` | Triggers the AI Test Agent workflow (see below) |
| `tech-debt` | Internal cleanup (e.g. extracted from the `8 unresolved debts` list) |

Please apply or request labels when you open an issue.

---

## AI-assisted CI (OpenHands + DeerFlow)

This project ships an **AI-assisted CI workflow**
(`.github/workflows/ai-test-agent.yml`) adapted from the local
`.ai-agents-cicd-testing-guide`. The workflow:

1. **Detects** a new PR or an issue labelled `ai-test`.
2. **Calls DeerFlow** to produce a structured test plan from the PR diff.
3. **Posts the plan as a PR comment** for human review.
4. **Runs the full test matrix** with the standard Maven CI.
5. **Reports results** back to DeerFlow for analysis.

To enable this workflow on your fork:

1. Deploy DeerFlow 2.0 and OpenHands per the
   `.ai-agents-cicd-testing-guide` (sections 3 and 4).
2. Add two repository secrets in GitHub Settings → Secrets:
   - `DEERFLOW_URL`  — e.g. `https://deerflow.example.com`
   - `OPENHANDS_URL` — e.g. `https://openhands.example.com`
3. Push a PR — the workflow will detect the secrets and start
   commenting.

The workflow is **off by default** in this public repo because the
secrets are not configured. It is provided as a reference template;
forkers are encouraged to enable it in their own CI.

---

## Reporting security issues

See [`SECURITY.md`](SECURITY.md). Do **not** open a public issue for
security vulnerabilities — email the maintainer directly.

---

*Document version 1.0 — maintained alongside micro-jainslee 1.1.0.*
