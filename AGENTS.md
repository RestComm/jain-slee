# AGENTS.md — Micro-JAINSLEE Runtime

## JAVA 25 ONLY — NON-NEGOTIABLE

**This project supports Java 25 only. No exceptions.**

- Bytecode target, compiler `release`, runtime, CI, and docs: **JDK 25**.
- Do **not** keep or reintroduce Java 8 / 11 / 17 / 21 / 24 as supported
  baselines (“for Quarkus ASM”, “for WildFly”, “for downstream portability”, etc.).
- Do **not** set `maven.compiler.release` / `source` / `target` below **25**
  in runtime modules, adapters, examples, or vendor RAs.
- Toolchain: project `mise.toml` → `java = "zulu-25"`. Build with that JDK
  (e.g. `mise exec -- mvn …` with a clean PATH if Cursor shims break).
  Ignore global mise defaults that pin older JDKs (e.g. `zulu-8`).
- If a dependency or framework cannot run on Java 25: **upgrade the
  dependency/framework**, or drop it — never lower the Java baseline.
- Virtual Threads and other Java 25 APIs are first-class; do not add
  reflection shims “for older JVMs”.

## MISSION
Re-architect the micro-jainslee RUNTIME (not the app) to natively support
local/internal RA and SBB development pattern defined in docs/junior-dev-guide.md.

## CONTEXT — READ THESE FILES FIRST (in order)
1. docs/junior-dev-guide.md          ← NEW architecture (source of truth)
2. docs/gap-analysis.md              ← what is already implemented
3. docs/microjainslee-design.md      ← current runtime design
Do NOT start coding until you have read all 3 files.

## CURRENT STATE (runtime modules)
```
jainslee-api        ← Pure Java 25 API (JSR-240)
jainslee-core       ← Engine: MicroSleeContainer, EventRouter,
                       VirtualThreadSbbEntityPool, SleeTimerSchedulerBridge,
                       SbbIndexLoader, ServiceRegistry, SbbTransactionContext,
                       DefaultInitialEventSelector, DefaultErrorHandlingPolicy
jainslee-scheduler  ← Netty HashedWheelTimer (10ms), TimerType.SLEE_TIMER
jainslee-apt        ← APT codegen, GeneratedEventTypes, SbbIndexLoader
adapter-quarkus     ← Quarkus/CDI/GraalVM adapter
adapter-spring      ← Spring Boot adapter
ra-connectors       ← vendor RA stubs
```

## TARGET STATE — what needs to change in the RUNTIME

### GOAL 1: Formalize 3-port contract as first-class API
**File:** `jainslee-api/src/main/java/...`

Ensure these interfaces exist and are stable:
```java
RaEndpointPort    { void activate(RaBootstrapPort); void deactivate(); String getRaName(); }
RaCommandPort     { void sendCommand(OutboundCommand); }
RaBootstrapPort   { ActivityHandle createActivityHandle(String id);
                    void fireEvent(SleeEvent, ActivityHandle, Address); }
OutboundCommand   (marker sealed interface)
```
- If they already exist: verify signatures match exactly the above
- If missing or different: add/fix them — do NOT break existing classes

### GOAL 2: MicroSleeContainer must accept local RA registration
**File:** `jainslee-core/.../MicroSleeContainer.java`
- Add method: `registerRa(RaEndpointPort endpoint, RaCommandPort command)`
- Add method: `mapEventToSbb(Class<? extends SleeEvent> eventType, String sbbName)`
- On container start: call `endpoint.activate(bootstrap)` for each registered RA
- On container stop: call `endpoint.deactivate()` in reverse order
- RaBootstrapPort impl must route `fireEvent()` → EventRouter (existing)

### GOAL 3: SbbIndexLoader must support programmatic SBB registration
**File:** `jainslee-core/.../SbbIndexLoader.java` (or `ServiceRegistry.java`)
- Add method: `registerSbb(Class<? extends Sbb> sbbClass)`
- This supplements (does NOT replace) existing META-INF/services discovery
- Programmatic registration takes priority over descriptor-based

### GOAL 4: Inject RaCommandPort into SBB
- SBB fields annotated with `@InjectRa(name="ra-name")` must be injected
  with the registered RaCommandPort at SBB creation time
- If `@InjectRa` does not exist: create it in `jainslee-api`
- Injection happens inside `VirtualThreadSbbEntityPool.createSbbEntity()`

### GOAL 5: Update ra-connectors module
- Move existing vendor RA stubs to implement new `RaEndpointPort` + `RaCommandPort`
- Keep backward compat — do NOT delete existing classes

## STRICT RULES
- **JAVA 25 ONLY** — see section above; never lower `release`/`source`/`target`
- NEVER modify application code — only runtime modules above
- NEVER break existing 62+ tests (run: `mvn test` before and after on JDK 25)
- NEVER add Spring/Quarkus imports into `jainslee-api` or `jainslee-core`
- NEVER use reflection in `jainslee-core` (use APT or ServiceLoader instead)
- Keep `jainslee-core` as Pure Java 25, ZERO framework deps

## EXECUTION ORDER
1. Read 3 docs files
2. Confirm JDK 25 (`java -version`) via project `mise.toml` / zulu-25
3. Run: `mvn test`  (baseline — must pass on JDK 25)
4. Implement GOAL 1 (api changes)
5. Implement GOAL 2 (container)
6. Implement GOAL 3 (sbb registration)
7. Implement GOAL 4 (ra injection)
8. Implement GOAL 5 (ra-connectors)
9. Run: `mvn test` on JDK 25 (must still pass)
10. Report: list of changed files + any failing tests

## DONE WHEN
- [ ] Build/tests run on **JDK 25 only** (no lower `release` left behind)
- [ ] `mvn test` passes (same count as baseline)
- [ ] `MicroSleeContainer.registerRa()` works
- [ ] `MicroSleeContainer.registerSbb()` works
- [ ] `MicroSleeContainer.mapEventToSbb()` works
- [ ] `RaBootstrapPort.fireEvent()` routes to EventRouter
- [ ] `@InjectRa` injection works in SBB
- [ ] No framework imports in `jainslee-api` or `jainslee-core`

## BUILD GOTCHAS — recurring issues, fix once and remember

### log4j api/core version skew → `NoSuchFieldError: FlowMessageFactory.INSTANCE`
Any module (esp. vendor RA tests) that pins **log4j-api** to an older version
(e.g. `2.23.1`) while transitively pulling **log4j-core `2.24.3`** from
`jainslee-core` will crash at test/runtime with:
`NoSuchFieldError: ... FlowMessageFactory INSTANCE` (the field only exists in
2.24.x). **Rule:** every `log4j.version` MUST equal jainslee-core's
(`2.24.3`). Do not pin log4j-api independently.
- Parent knobs: `vendor-ras/pom.xml` → `<log4j.version>` (already 2.24.3).
- Already bitten: `jainslee-autonomous` (removed hard-coded 2.23.1),
  `ra-grpc-server` + `ra-grpc-client` (fixed via vendor-ras property bump).

### Agrona `DeadlineTimerWheel` — "tick resolution must be a power of 2"
`jainslee-scheduler` `AgronaTimerWheelFacade`: both `tickResolution` AND
`ticksPerWheel` must be powers of 2 (Agrona 1.23.1). Correct ctor:
`new DeadlineTimerWheel(TimeUnit.NANOSECONDS, System.nanoTime(), TICK_NANOS, WHEEL_SIZE)`
with `TICK_NANOS = 1L << 20` (~1.049 ms), `WHEEL_SIZE = 512`.

### Reactor overwrites good jars with local WIP
A full `mvn -o install` reactor run reinstalls EVERY module (incl. examples).
If a module has broken uncommitted WIP, its jar overwrites the good one in
`~/.m2`. When bisecting, `git stash` the WIP module before a reactor install.

### ARCHITECTURE RULE (do not regress)
App / example / template code MUST NOT create Vert.x, Netty channels, or any
transport directly. Transport lives ONLY inside RAs. HTTP → `ra-http-server` /
`ra-http-client` + a gateway SBB; gRPC channel → RA via `setTarget()`/`channel()`.
Quarkus/GraalVM-native is the priority target; Spring/JakartaEE are low priority.
