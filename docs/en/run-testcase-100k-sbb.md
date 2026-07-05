# How to run the 10K / 50K / 100K SBB stress test

> **Audience:** developers and QA engineers verifying that micro-jainslee
> scales to 100K concurrent SBB entities. The test exercises the full
> **create → pending → cancel** lifecycle at three scale points and
> validates the `VirtualThreadSbbEntityPool` that powers Quarkus,
> Spring Boot, and Jakarta EE embeddings.

**Maintainer:** Tran Nhan ([nhanth87@gmail.com](mailto:nhanth87@gmail.com))  
**Last updated:** 2026-06-28  
**Source file:** [`jainslee-core/src/test/java/com/microjainslee/core/SbbEntityPoolStressTest.java`](../../jainslee-core/src/test/java/com/microjainslee/core/SbbEntityPoolStressTest.java)

---

## Table of contents

1. [What the test verifies](#1-what-the-test-verifies)
2. [Test flow diagram](#2-test-flow-diagram)
3. [Function call flow](#3-function-call-flow)
4. [Prerequisites](#4-prerequisites)
5. [Quick start (single command)](#5-quick-start-single-command)
6. [Per-scenario commands](#6-per-scenario-commands)
7. [Reading the output](#7-reading-the-output)
8. [Tuning for your hardware](#8-tuning-for-your-hardware)
9. [Adding a new scale point](#9-adding-a-new-scale-point)
10. [Troubleshooting](#10-troubleshooting)

---

## 1. What the test verifies

For each scale point (10K, 50K, 100K), the test exercises three
sequential phases that mirror a production SBB's lifecycle:

| Phase | What it does | What it asserts |
|---|---|---|
| **Create** | Calls `pool.acquire("sbb-" + i, factory)` N times | `pool.size() == scale`; every entity has a distinct SBB ID; heap delta is recorded; `liveThreads` count is recorded |
| **Pending** | Submits `TASKS_PER_SBB = 5` `Runnable`s per entity; each task verifies it sees the previous sequence number + 1 | No counter drift; no out-of-order tasks; no `Throwable` escapes the loop; all 5 × N tasks complete within `HARD_LIMIT_MS = 4 minutes` |
| **Cancel** | Calls `pool.shutdown()` | `executor.isTerminated() == true`; a follow-up `acquire(...)` throws `IllegalStateException` |

The pending phase is the strictest invariant: the test verifies that
**every pair of events for the same SBB ID executes in submission
order** on the same virtual thread. This is the JAIN SLEE §8.4
single-threaded SBB contract; violating it would let a SBB see a
USSD BEGIN after the corresponding END and break the entire
service-logic model.

> **Perfect Core S2 — codegen note (2026-06-28):** Starting with
> micro-jainslee 1.2.0, CMP accessors are generated **at deploy time**
> by `jainslee-codegen` (Javassist) instead of going through
> `Method.invoke()` reflection. The 100K stress test currently
> exercises only `SbbEntityPool`, **not** the CMP codegen path — so
> the timings below still reflect the pool / Disruptor / VT pipeline.
>
> If you measure with `MicroSleeConfiguration.codegenEnabled=true`,
> expect CMP-heavy SBBs (e.g. `Ss7UssdIngressSbb`) to run
> **~10× faster** on CMP read/write, but the pool-side numbers
> (create / pending / cancel) stay roughly the same. To compare,
> re-run with `-Dmicrojainslee.codegenEnabled=false` for the
> reflection baseline.

---

## 2. Prerequisites

### Hardware

| Scale | Recommended RAM | Recommended cores |
|------:|---------------:|-----------------:|
| 10K   | 512 MB         | 2                 |
| 50K   | 2 GB           | 4                 |
| 100K  | 4 GB           | 4 (more is better)|

The 100K scenario allocates roughly 290 MB of heap (see the
[measured metrics](#5-reading-the-output) below). On a 4-core machine
the test finishes in under 6 seconds end-to-end.

### Software

- **JDK 21 or later** — virtual threads are required for the
  `sbbPerVirtualThread = true` path. **JDK 25 LTS** is recommended.
- **Maven 3.9+** with the default plugins (no additional setup).
- **Git** to clone the repo.

Verify:

```bash
java -version
# openjdk version "25.0.3" 2026-04-21 LTS
# OpenJDK Runtime Environment Zulu25.34+17-CA

mvn -version
# Apache Maven 3.9.x ...
```

### Clone and install the API jar

```bash
git clone https://github.com/nhanth87/micro-jainslee.git
cd micro-jainslee
mvn -pl jainslee-api install -DskipTests
```

The `install` step is required because `jainslee-core` depends on the
`jainslee-api` JAR being present in the local `~/.m2/repository`.


---

## 3. Quick start (single command)

From the repository root:

```bash
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest
```

This runs all three scenarios sequentially. Expected wall-clock on a
4-core, 8 GB-heap dev machine:

| Scenario | Approx. time |
|---|---:|
| 10K  | ~1.4 s |
| 50K  | ~3 s   |
| 100K | ~6 s   |
| **Total** | **~10 s** |

You will see three `[create] / [pending] / [cancel]` log lines per
scenario plus a final "Done scenario" summary.

### Set a 4 GB heap for the surefire JVM

The surefire plugin defaults to 256 MB which is too small for the
100K scenario (it OOMs around 80K entities). Set:

```bash
MAVEN_OPTS="-Xmx4g" mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest
```

`MAVEN_OPTS` configures the **Maven** JVM, not the forked test JVM —
which is what we actually need. To configure the test JVM specifically,
add the surefire argLine:

```bash
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest \
    -DargLine="-Xmx4g -XX:+UseZGC -XX:+ZGenerational"
```

This combination (`-Xmx4g -XX:+UseZGC`) is what we measured on
Java 25 — ZGC gives sub-millisecond pause times even at 100K entities.

---

## 4. Per-scenario commands

Each scenario is an independent `@Test` method, so you can run them
in isolation:

```bash
# 10K only — fastest sanity check (~1.5 s)
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest#create_10k_succeeds

# 50K only
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest#create_50k_succeeds

# 100K only — full stress
mvn -pl jainslee-core test \
    -Dtest=SbbEntityPoolStressTest#create_100k_succeeds \
    -DargLine="-Xmx4g -XX:+UseZGC"
```

To run the stress test as part of the full reactor:

```bash
mvn clean verify -DargLine="-Xmx4g -XX:+UseZGC"
```

This compiles every module, runs every test, and produces a surefire
report under `jainslee-core/target/surefire-reports/`.

### Headless CI usage

In a CI pipeline (GitHub Actions, GitLab CI, Jenkins) where you
typically don't want TTY-style log output, pipe through `tee`:

```bash
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest \
    -DargLine="-Xmx4g" \
    -B -ntp 2>&1 | tee stress-test.log
```

The `-B` flag enables batch (non-interactive) mode and `-ntp` suppresses
the transfer progress bar.

---

## 5. Reading the output

A successful run produces three blocks of structured log output — one
per scenario. Example for the 100K scenario:

```
21:13:35.619 INFO  com.microjainslee.core.SbbEntityPoolStressTest - === Stress scenario: 100000 SBBs (Java 25.0.3, cores=4) ===
21:13:36.932 INFO  com.microjainslee.core.SbbEntityPoolStressTest - [create] 100000 entities in 1302 ms (13024 ns/op), heap +290 MB, liveThreads=14
21:13:40.616 INFO  com.microjainslee.core.SbbEntityPoolStressTest - [pending] 500000 tasks in 3650 ms (7300 ns/op), errors=0
21:13:41.400 INFO  com.microjainslee.core.SbbEntityPoolStressTest - [cancel] shutdown drained in 779 ms (executor terminated=true)
21:13:41.400 INFO  com.microjainslee.core.SbbEntityPoolStressTest - === Done scenario 100000: total 5770 ms ===
```

### Field-by-field meaning

| Field | Meaning |
|---|---|
| `[create] N entities in T ms (X ns/op)` | Time to allocate N `SbbEntity` instances and spawn N parked virtual threads. Divide T by N for the per-entity cost. |
| `heap +H MB` | Resident heap delta measured before and after the create phase via `MemoryMXBean.getHeapMemoryUsage().getUsed()`. |
| `liveThreads=N` | `ThreadMXBean.getThreadCount()` sampled right after the create phase. With virtual threads, this stays at ~4 × cores = 14 even at 100K. |
| `[pending] N tasks in T ms (X ns/op), errors=N` | Time to enqueue + dispatch N tasks (5 × entity count). `errors` counts any ordering violation or dropped task — must always be 0. |
| `[cancel] shutdown drained in T ms (executor terminated=true)` | Time for `pool.shutdown()` to finish, including the `awaitTermination(5s)` timeout. `executor terminated=true` means all parked VTs exited cleanly. |
| `=== Done scenario ===` | Total wall-clock for the scenario. |

### What success looks like

A passing run shows:

- `errors=0` for every scenario
- `executor terminated=true` for every cancel phase
- `liveThreads` stays at ~14 across all scales (or, on a machine with
  more cores, at ~3.5 × cores)
- Heap delta grows linearly with scale (~3 KB per entity at 100K)

### What failure looks like

If you see any of these, the test will report it with a clear message:

| Symptom | Likely cause |
|---|---|
| `OOM Java heap space` during 100K create | Increase `-Xmx` to at least 4 GB |
| `tasks timed out after 240000 ms` | A virtual thread got pinned by a SBB callback holding a `synchronized` monitor — run with `-Djdk.tracePinnedThreads=full` to find it |
| `expected seq=12, got 13` in the logs | A virtual thread is starving another (very rare with the current implementation); file an issue with the captured log |
| `acquire() after shutdown must throw` failed | The pool was reused after shutdown; check that `@After tearDown()` ran |


---

## 6. Tuning for your hardware

### Scaling beyond 100K

The current test stops at 100K because the stress-test scenarios are
hard-coded in `SbbEntityPoolStressTest.SCALES`. To try larger numbers:

```java
private static final int[] SCALES = new int[] { 10_000, 50_000, 100_000, 500_000, 1_000_000 };
```

Then re-run with enough heap:

```bash
mvn -pl jainslee-core test \
    -Dtest=SbbEntityPoolStressTest \
    -DargLine="-Xmx16g -XX:+UseZGC -XX:+ZGenerational"
```

Each million SBBs needs roughly **2.9 GB of heap** (290 MB per 100K).
On a machine with 32 GB of RAM and 8 cores you can comfortably run
the 1M scenario in under a minute.

### Toggling Perfect Core S2 codegen (`MicroSleeConfiguration.codegenEnabled`)

Starting with micro-jainslee 1.2.0, the container can **generate concrete
SBB classes at deploy time** (Javassist, ~10× faster CMP access than
reflection). The flag `MicroSleeConfiguration.codegenEnabled` controls
this at runtime:

| Value | Behavior | When to use |
|---|---|---|
| `true` (default) | `ConcreteSbbGenerator` runs once per SBB type at startup. CMP reads/writes go direct. | All production paths. |
| `false` | Container falls back to `Method.invoke()` reflection. | Dev with hot-reload; debugging CMP. |

For the 100K pool stress test the toggle has **no measurable effect**
on the pool-side numbers (create / pending / cancel) because the test
constructs `SbbEntity` instances directly without exercising CMP
accessors. To observe the codegen impact, run a CMP-heavy workload
(e.g. `Ss7UssdIngressSbb`):

```bash
# Codegen ON (default)
mvn -pl jainslee-core test \
    -Dtest=SbbEntityPoolStressTest \
    -Dmicrojainslee.codegenEnabled=true \
    -DargLine="-Xmx4g -XX:+UseZGC"

# Codegen OFF (reflection baseline)
mvn -pl jainslee-core test \
    -Dtest=SbbEntityPoolStressTest \
    -Dmicrojainslee.codegenEnabled=false \
    -DargLine="-Xmx4g -XX:+UseZGC"
```

Set the flag via `application.properties`:

```properties
microjainslee.codegenEnabled=true
microjainslee.codegenDeployDir=target/generated-sources/sbb
microjainslee.codegenCacheClass=true
```

Or via system property at JVM start:

```bash
java -Dmicrojainslee.codegenEnabled=false -jar my-ussd-app.jar
```

### Adjusting task count

`TASKS_PER_SBB = 5` is the default. To stress the pending phase more:

```java
private static final int TASKS_PER_SBB = 50;
```

Total tasks = `SCALES × TASKS_PER_SBB`. With the default 5 tasks and
the default scales, the 100K scenario dispatches **500K tasks**. At
50 tasks per SBB, that becomes **5M tasks** — a meaningful stress on
the per-VT queue and the Disruptor consumer.

### GC tuning for very large scenarios

| GC | Recommended for | Trade-off |
|---|---|---|
| ZGC (`-XX:+UseZGC -XX:+ZGenerational`) | Default for micro-jainslee. Sub-millisecond pauses, throughput penalty < 5% vs Parallel | Best latency, slightly more memory |
| Parallel GC (default) | Short-running dev runs where you don't care about pause times | Fastest throughput, can pause 100+ ms at 100K scale |
| G1 GC (`-XX:+UseG1GC`) | Compromise between the two | Predictable pauses but slower than Parallel at small heaps |

Always enable GC logging on the first run of a new scale:

```bash
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest \
    -DargLine="-Xlog:gc*:file=gc.log:time -Xmx4g"
```

---

## 7. Adding a new scale point

The test scenarios are defined in two places:

1. `private static final int[] SCALES = ...` — the scale values
2. Three `@Test` methods `create_Xk_succeeds` — one per scale value

To add a 200K scenario:

```java
private static final int[] SCALES = new int[] { 10_000, 50_000, 100_000, 200_000 };

@Test
public void create_200k_succeeds() throws Exception {
    runScenario(200_000);
}
```

That's it — `runScenario(int scale)` is shared and handles all three
phases.

---

## 8. Troubleshooting

### "Annotation processor '...' not found" during build

The `jainslee-apt` pom deliberately omits `<annotationProcessors>` to
avoid the chicken-and-egg bootstrap problem. If you see this:

```bash
mvn -pl jainslee-apt install -DskipTests
```

then re-run your test. The processor jar ends up in `~/.m2/repository`.

### "Supported source version 'RELEASE_8' from annotation processor ... less than -source '25'"

Harmless warning. The processor declares `RELEASE_8` so it can run on
any JDK, but the test fixture (or your project) may compile at source
level 25. The warning does not affect output.

### OutOfMemoryError at 100K

The default surefire heap is 256 MB. Increase:

```bash
mvn -pl jainslee-core test -Dtest=SbbEntityPoolStressTest \
    -DargLine="-Xmx4g"
```

### Test hangs at the `await` call

The test waits up to 4 minutes (`HARD_LIMIT_MS`) for all tasks to
complete. If it hangs:

1. Attach a profiler (YourKit, async-profiler) to the forked JVM.
2. Run with `-Djdk.tracePinnedThreads=full` to detect virtual-thread
   pinning from `synchronized` blocks.
3. Check that your JDK actually exposes `Executors.newVirtualThreadPerTaskExecutor()` —
   on JDK 17 or earlier, the pool silently falls back to a cached
   platform-thread pool and the per-VT ordering invariant no longer
   holds.

### Test passes locally but fails in CI

Common causes:

- **Different OS thread counts** — the ForkJoinPool common pool
  defaults to `Runtime.getRuntime().availableProcessors() - 1`. CI
  runners often have 1 or 2 vCPUs, which still works but produces
  different `liveThreads` numbers. The test does **not** assert on
  absolute `liveThreads`, so this is informational only.
- **GC under load** — CI machines are often memory-constrained. Set
  `-DargLine="-Xmx4g -XX:+UseZGC"` in your CI workflow to match
  local performance.
- **Slow disk for Maven cache** — first-time runs that populate
  `~/.m2/repository` can be very slow on CI. Use a warm cache.

---

## Further reading

- [`microjainslee-design.md`](microjainslee-design.md) — overall
  architecture, including the `VirtualThreadSbbEntityPool` design
- [`../README.md`](../README.md) — quickstart, Quarkus integration, full
  build matrix

---

*Document version 1.1 — maintained alongside micro-jainslee 1.2.0 (Perfect Core S1–S5).*
*For corrections, open an issue or email [nhanth87@gmail.com](mailto:nhanth87@gmail.com).*
