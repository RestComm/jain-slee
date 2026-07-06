# Changelog

All notable changes to **micro-jainslee** are documented here. The format
is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and
this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

**Maintainer:** Tran Nhan ([nhanth87@gmail.com](mailto:nhanth87@gmail.com)) · **License:** Dual GPLv3 + Commercial (see [`LICENSE`](LICENSE))

---

## [Unreleased] - 2026-06-28 - Removed legacy vendor Mobicents directories (api/, container/, release/) — replaced by micro-jainslee built from scratch with JAIN SLEE 1.1 spec subset (jainslee-api, jainslee-core, jainslee-tx, jainslee-codegen, jainslee-cluster, jainslee-tck-harness, jainslee-ra-spi). Vendored reference code preserved in git history.


## [Unreleased] — 2026-07-06 — 3-Port Contract + RA Restructure + HelloWorld Templates

### Added

- **3-Port Contract (PolyVoice Pattern)** — `RaEndpointPort`, `RaCommandPort`,
  `RaBootstrapPort` interfaces for clean RA lifecycle/command/bootstrap
  separation. `@InjectRa(name)` annotation for SBB field injection.
  Wrapper+Delegate pattern for all RAs.
- **ra-http-server restructure** — New `collab/`, `command/`, `events/` sub-packages.
  `HttpServerResourceAdaptor` made generic: removed USSD routes, only
  `GET /health` + `ANY /{path}` fires `HttpWebRequestEvent`. Added
  `sendHttpResponse()` for outbound HTTP. `HttpServerCommand` to `sealed interface`.
- **HttpWebRequestEvent** — `@EventType` with `sessionId`, `method`, `path`,
  `headers`, `body`, `userAgent` for generic HTTP request handling.
- **ra-http-client restructure** — New `collab/`, `command/`, `events/` sub-packages.
  `HttpCallbackCommand` to `sealed interface` + `CallbackRequest` record.
  Retry logic (exponential backoff), `HttpClientSessionStore`,
  `HttpCallbackCompletedEvent`. Uses `java.net.http.HttpClient`.
- **example-quarkus-helloworld-web** — Quarkus template app: `bootstrap/`,
  `command/`, `events/`, `rest/`, `sbbs/` structure. `index.html` in
  `META-INF/resources/` (WAR hosting). "Hello World {userAgent}" feature.
- **example-spring-helloworld-web** — Spring Boot mirror: `@SpringBootApplication`,
  `@Configuration`+`SmartLifecycle`, `@RestController`, `static/index.html`.
  Same userAgent feature.
- **`design-ideas/jainslee-telemetry.md`** — Self-healing telemetry engine design:
  `jainslee-telemetry` module with SbbCollector, RaCollector, ErrorCollector,
  ResourceMonitor, SpunkDetector, StaleDetector, AlarmEngine, AutoReconfigEngine.
  TelemetryPort API. Separate GraalVM JS GUI layer.
- **`design-ideas/telemetry-embedded-nodejs.md`** — Dashboard with embedded Node.js
  + Next.js + shadcn/ui + Recharts + Tremor.
- **`design-ideas/telemetry-graalvm-js.md`** — Dashboard with GraalVM polyglot JS
  SSR and Preact CDN zero-build alternative.
- **`jainslee-telemetry`** — Zero-CPU passive telemetry engine with
  SbbCollector, RaCollector, ErrorCollector, ResourceMonitor,
  SpunkDetector, StaleDetector, AlarmEngine, AutoReconfigEngine,
  PrometheusExporter. AtomicLong counters, ring buffers, single daemon
  VT scheduler (30s evaluate). Micrometer + Prometheus metrics export.
  Auto-reconfig on memory/CPU/load/error/stale/RA-crash conditions
  with independent cooldown timers.
- **`jainslee-telemetry-vertx`** — Steampunk-cyberpunk telemetry dashboard
  GUI. Single `index.html` + `telemetry.js`, zero build step. Served via
  Vert.x StaticHandler at `/telemetry/`. SVG arc gauges (heap/CPU),
  sparkline charts, alarm acknowledgment panel, runtime config sliders.
  2-second polling of `/api/telemetry/snapshot`. CSS-variable theme system.
- **Examples** — `example-quarkus-helloworld-web` and
  `example-spring-helloworld-web` updated with telemetry engine wiring
  and dashboard at `/telemetry/`.
- **`docs/jainslee-telemetry.md`** — 629-line comprehensive guide: architecture,
  collectors deep-dive, auto-reconfig engine, integration guide, API reference,
  Prometheus/Grafana setup, configuration reference.
- **`docs/telemetry-gui.md`** — 356-line dashboard guide: screenshot description,
  layout breakdown, real-time update mechanism, SVG gauge implementation,
  alarm acknowledgment flow, config panel, CSS variable theming.
- **`docs/microjainslee-design.md`** — 319-line architecture document: goals,
  module map, event router pipeline, VT pool deep-dive, 3-port RA contract,
  adapter pattern, telemetry & self-healing section, concurrency contract,
  failure modes, design decision log.

### Changed

- **ra-http-server** — `HttpServerSessionStore`, `HttpServerSessionPreparer`
  to `collab/`. `HttpServerCommand` to `command/`. `HttpBeginEventFactory`
  to `events/`. Imports updated across all examples.
- **ra-http-client** — `HttpCallbackCommand` to `command/`. New
  `collab/HttpClientSessionStore`, `events/HttpCallbackCompletedEvent`.
- **USSD examples** (`example-quarkus-ussdgw`, `example-spring-ussdgw`,
  `example-embedded-j25-ussdgw`) updated for generic RA: USSD events now app-level.
- **example/hello-world-web/** removed, replaced by structured templates.

### Deprecated

- USSD-specific endpoints in `ra-http-server`. Apps must handle USSD via
  `HttpWebRequestEvent` or REST controllers.

## [Unreleased]

### Changed

- **Documentation refresh** — `README.md`, `docs/microjainslee-design.md`, and
  `docs/run-testcase-100k-sbb.md` updated with Mermaid architecture/sequence
  diagrams, function-call tables aligned to actual source
  (`MicroJainsleeProcessor`, `MicroSleeContainer.attach`,
  `SleeTimerSchedulerBridge`), and a clarified dual-license section.
  `docs/TCK_TIMER_CUTOVER.md` is kept as background reading for anyone
  migrating from the JBoss/Mobicents stack.

## [1.1.0] — 2026-06-26

First public release after the Phase 3 + Phase 3.5 work landed. R&D-only;
not for production packaging.

### Added

- **VirtualThreadSbbEntityPool** — one parked Java 25 virtual thread per
  SBB ID with a `LinkedBlockingQueue<Runnable>` for event delivery.
  Guarantees JAIN SLEE §8.4 single-threaded per-SBB ordering without
  locks. Reflection-based executor shim keeps `jainslee-core` Java 8
  source/target compatible.
- **`jainslee-spring-boot-starter`** — Spring Boot 3.3.0 auto-configuration:
  `MicroSleeContainer`, `MicroSleeConfiguration`, `EventRouter`,
  `TimerPort`, `InMemoryActivityContextNamingFacility` exposed as
  beans. `SmartLifecycle` mapping for `start()`/`stop()`. `application.yml`
  property binding under `microjainslee.*`. Uses `jakarta.*` namespace.
- **`adapter-quarkus`** (parent + runtime + deployment) — Quarkus 3.15.1
  extension. `MicroJainsleeProcessor` `@BuildStep` chain produces
  synthetic CDI beans; `MicroJainsleeRecorder` `@Recorder` stashes the
  container across the build-time / runtime boundary. Jandex scan for
  `@Sbb`-annotated classes; `ShutdownContextBuildItem` shutdown hook.
- **`adapter-jakartaee`** — Jakarta EE 9 `@Singleton @Startup @LocalBean`
  EJB. `@PostConstruct` builds the container from `microjainslee.config.*`
  system properties and binds `MicroSleeContainer`, `EventRouter`,
  `TimerPort`, `ActivityContextNamingFacility` into JNDI at
  `java:global/microjainslee/*`. Uses `jakarta.ejb.*` /
  `jakarta.annotation.*` / `javax.naming.*`.
- **`jainslee-apt`** — Javac annotation processor. Scans
  `@SbbAnnotation` / `@DeployableUnit` / `@EventType` and emits
  `META-INF/microjainslee/sbb-index.properties` at compile time.
  Uses `AnnotationMirror` API to read `Class[]` members safely. SPI
  registered via `META-INF/services/javax.annotation.processing.Processor`.
- **`SbbEntityPoolStressTest`** — 246 LOC covering create + pending +
  cancel lifecycle at 10K / 50K / 100K SBB scales. Verifies
  `lastSeq+1 == seq` for every task to enforce single-threaded ordering.
  Measured on Java 25.0.3 / 4 cores / 8 GB heap: 100K scenario finishes
  in 5.7 s with `liveThreads=14`.
- **`VirtualThreadSbbEntityPoolTest`** — 12 unit tests covering
  acquire/release/shutdown/prewarm/concurrent-entity-creation races.
- **`MicroJainsleeAutoConfigurationTest`** — Spring Boot context-loads
  the auto-configuration and asserts all 6 beans are present and the
  container is `STARTED`.
- **`docs/microjainslee-design.md`** — 615-line architecture document
  (goals/non-goals, module map, event router pipeline, virtual-thread
  pool deep-dive, timer bridge, annotation processor, runtime
  integrations, configuration model, concurrency contract, failure
  modes, design-decision log).
- **`docs/run-testcase-100k-sbb.md`** — 374-line step-by-step
  stress-test guide (prerequisites, mvn commands per scenario,
  output interpretation, hardware tuning, adding a new scale point,
  troubleshooting).
- **Dual license**: `LICENSE` rewritten as Section A (full GPLv3
  verbatim) + Section B (Commercial License summary). New
  `COMMERCIAL_LICENSE.md` with 8 sections, pricing tiers
  (Indie $1.2K/yr / Small biz $6K/yr / Enterprise $25K+/yr / OEM custom),
  and procurement instructions. Copyright (c) 2026 Tran Nhan.
- **Maintainer info in `pom.xml`**: `<licenses>`, `<developers>` (Tran
  Nhan / nhanth87 / nhanth87@gmail.com), `<scm>`, `<organization>`
  blocks.
- **License headers on every `.java` source file** (52 files) —
  9-line stamp with project name, dual-license line, copyright,
  maintainer email.

### Changed

- **`MicroSleeContainer.registerSbb`** now wires the per-SBB virtual
  thread pool, so events for a given SBB ID execute on the SBB's
  owning VT. The in-line `new SimpleSbbLocalObject(...)` instantiation
  is preserved as a thin path for tests that don't need the pool.
- **`EventRouter`** reworked to look up the SBB entity via the pool
  and dispatch through `entity.submit(...)` rather than running on the
  Disruptor's consumer thread.
- **`MicroSleeConfiguration`** gains `sbbPoolMin` / `sbbPoolMax` /
  `sbbPerVirtualThread` builder fields with validation
  (`min >= 0`, `max >= 1`, `min <= max`).
- **`SbbEntityPool`** refactored as a backward-compat facade over
  `VirtualThreadSbbEntityPool`. New constructor signature
  `SbbEntityPool(int min, int max, boolean perVirtualThread)`;
  the old `SbbEntityPool(int poolSize)` is preserved.
- **`README.md`** rewritten (935 lines) — covers What/Quickstart/
  Modules/Architecture/Code flow/Quarkus/RA/SBB lifecycle/Profile/ACNF/
  Timer/Stress test/Build/Comparison vs JBoss/Operational/Docs/
  License sections. New badges for Java 25, Virtual Threads, dual
  license, and maintainer.
- **All new code uses `org.apache.logging.log4j.Logger`** via
  `LogManager.getLogger` — never `System.out`/`System.err`.

### Security

- Dual-licensed under GPLv3 (open-source) OR a separate Commercial
  License (proprietary use). See [`COMMERCIAL_LICENSE.md`](COMMERCIAL_LICENSE.md).
- Original RestComm JAIN-SLEE v8 inheritance (AGPL-3.0) is **removed**
  from micro-jainslee; the legacy `container/`, `api/`, `tools/`
  directories retain the original AGPL-3.0 license (untouched).

### Known limitations

- No TCK run — explicitly out of scope for R&D.
- No cluster / HA — single-JVM only.
- No JSR-77 management MBeans — embedding runtime is responsible.
- The `SleeTimerSchedulerBridge` reuses jSS7's `TimerType.TCAP_INVOKE_TIMEOUT`
  as a placeholder until jSS7 exposes a dedicated SLEE timer type.
- `adapter-jakartaee` and `jainslee-apt` lack in-process unit tests
  (the former requires WildFly; the latter was verified via a manual
  fixture compilation).

---

## [1.0.0] — 2024 (RestComm inheritance)

- Initial scaffold of `jainslee-api`, `jainslee-core`, `ra-connectors`.
- LMAX Disruptor event router. jSS7 Scheduler 9.4.0 wired through
  `SleeTimerSchedulerBridge`. log4j2 logging.

[1.1.0]: https://github.com/nhanth87/jain-slee/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/nhanth87/jain-slee/releases/tag/v1.0.0
