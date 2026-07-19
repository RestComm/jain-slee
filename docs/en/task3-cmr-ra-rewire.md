# Task 3 — example-cmr rewired onto one `ra-http-server` + one gateway SBB

**Status:** ✅ Complete and verified live (2026-07-09)

Rewire the whole CMR example so it runs **entirely behind the SLEE RA contract** —
no app-level Vert.x, no second HTTP server. One `ra-http-server` per app + one
path-routing gateway SBB owns every HTTP surface (public site, admin, telemetry
dashboard, autonomous health, AI REST).

> **Architecture rule (why):** application / example / template code must NEVER
> spin up Vert.x directly. Vert.x lives ONLY inside RAs. Any RA that wants Vert.x
> must expose an SBB and go through `ra-http-server` / `ra-http-client`.
> `ra-http-server` fires a single event type (`HttpWebRequestEvent`) and
> `mapEventToSbb` keys on the event class, so exactly **one** gateway SBB must
> route all paths.

---

## New app files (zero Vert.x)

| File | Role |
|------|------|
| `http/HttpReply.java` | Framework-neutral response record `(status, contentType, text, byte[] binary, Map headers)` with `html/json/text/bytes/noContent/notFound/redirect/withHeader`. |
| `http/MonitorHandler.java` | Serves the steampunk dashboard GUI (from the `jainslee-monitor` jar's `META-INF/resources`) + `/api/telemetry/*`, `/api/autonomous/health`, `/api/ai/*` as **Jackson** JSON. Returns `Optional.empty()` for paths it doesn't own so the gateway falls through to the site. |
| `http/SiteHandler.java` | The whole admin + public site as a pure function `(HttpWebRequestEvent) → HttpReply`. Ports the old `AdminRouter` + `PublicRouter`. Cookies via `Set-Cookie`, 303 redirects, form/multipart parsing from the event, writes fired as CMR events into the SLEE pipeline (`container.routeEvent`). |
| `sbbs/HttpGatewaySbb.java` | `@InjectRa(name="http-server-ra") RaCommandPort`; `onEvent` → `monitor.handle(req).orElseGet(() -> site.handle(req))` → `HttpServerCommand.HttpResponseExCommand`. |

## Rewritten (Vert.x stripped)

| File | Change |
|------|--------|
| `telemetry/AppTelemetry.java` | `install(container)` (no `Vertx` param). Collectors + dispatch observer + Prometheus RA + log sink only. **No** dashboard server — the GUI now rides `ra-http-server` via `MonitorHandler`. |
| `autonomous/AppAutonomous.java` | Replaced `mountRoutes(Router)` with `String healthJson()` (Jackson). No `io.vertx`. |
| `autonomous/AppAiAgent.java` | Removed `mountRoutes(Router)`; keeps `install` + `engine()`. `MonitorHandler` owns `/api/ai/*`. |
| `telemetry/TelemetryLogSink.java` | `summarize()` uses Jackson `ObjectNode`/`ArrayNode` instead of `io.vertx.core.json`. |
| `bootstrap/CmrBootstrap.java` | Registers one `ra-http-server` on `cmr.http.port` + `HttpGatewaySbb`; `mapEventToSbb(HttpWebRequestEvent.class, "HttpGatewaySbb")`; builds `MonitorHandler` (null when telemetry off) + `SiteHandler` with a `Consumer<SleeEvent>` sink that fires via `container.routeEvent(ev, container.createActivityContext(...))`. Removed `@Inject Vertx`. |

## Deleted

- `ra/CmrHttpResourceAdaptor.java`, `ra/CmrHttpRaEndpoint.java`, `ra/AdminRouter.java`, `ra/PublicRouter.java` (all Vert.x).

## pom.xml

- Removed `io.quarkus:quarkus-vertx-http` and `io.vertx:vertx-web`.
- Added `com.microjainslee:ra-http-server` (Vert.x now transitive, inside the RA only).

---

## Two real bugs found & fixed while wiring the round-trip

### 1. `ra-http-server` never created / ended the per-request activity

Symptom: every request failed with `IllegalStateException: Unknown activity
handle: <uuid>` (500). The RA's endpoint bridge fired the event against an
activity context that was never started — unlike `ra-http-client`, which calls
`bp.createActivityHandle(id)` first.

Fix (`HttpServerRaEndpoint` + `HttpServerResourceAdaptor`):
- Bridge `getSleeEndpointPort()` now **create-then-fire** (`bp.createActivityHandle`
  then `bp.fireEvent`), and `startActivity` / `endActivity` delegate to `bp`.
- After the response is written, the RA ends the per-request activity via
  `vertx.runOnContext(...)` — **off** the Disruptor consumer thread. Ending it on
  the consumer thread would fire `ActivityEndedEvent` re-entrantly and corrupt
  that thread's transaction context. Deferring releases the named activity so it
  doesn't leak in the naming facility.

### 2. Core transaction-context leak — `ActivityContextTransactionRegistry`

Symptom: only the first request per app succeeded; every later event failed with
`IllegalStateException: Nested transaction mismatch: existing=… requested=…` and
the single Disruptor consumer thread effectively hung the whole app.

Root cause: `begin()` bound the transaction to the current thread **and**
`dispatchUnderLock` then wrapped the body in `runInTransaction(tx, …)`, which
captured that just-installed tx as its `previous` value and **restored** it in
`finally` — so `CURRENT` never cleared. The next event's `begin()` then saw a
stale binding and threw.

Fix: `begin()` no longer binds — it only creates + begins the context.
`runInTransaction` is the single bracketing authority (binds on entry, restores
the *real* previous — normally `null` on a pooled worker — on exit). Both callers
(`EventRouter:480` and `SbbTransactionContextTest`) already wrap in
`runInTransaction`, so this is safe.

Side effects of the fix:
- Turned **2 red core unit tests green** (`ScopedValueTransactionIsolationTest`
  — the failing tests were cross-class contamination from the same
  `begin()+runInTransaction` double-bind on the JUnit runner thread). Full
  `jainslee-core` suite is now **416/416**.
- Also fixed an unrelated compile error blocking the core build:
  `VirtualThreadSbbEntityPool.java:417` used `OffHeapArena::close` on
  `OffHeapSlotArena` values whose `close()` (from `AutoCloseable`) throws a
  checked exception → replaced with a try/catch lambda.

---

## Verification (live, packaged jar)

Ports: `quarkus.http.port=8081` (CDI host only), `cmr.http.port=8082` (the RA).

| Check | Result |
|-------|--------|
| 60/60 sequential `GET /` | ✅ consumer survives, no hang |
| `/telemetry`, `/telemetry/monitoring.js` | ✅ 200 (GUI from monitor jar) |
| `/api/telemetry/{snapshot,metrics}`, `/api/autonomous/health`, `/api/ai/status` | ✅ 200 JSON/text |
| admin `POST /admin/login` | ✅ 303 + `Set-Cookie: cmr_session` |
| `GET /admin/dashboard` (cookie) | ✅ 200 admin content |
| `POST /admin/article` → fires `ArticleCreatedEvent` → `ArticleSbb` renders MD → saves → public `/` shows it | ✅ write-through-events works |
| `GET /news/<slug>`, `GET /news/does-not-exist` | ✅ 200 / 404 |
| `grep io.vertx` across all app source | ✅ zero matches |
| `grep "Nested transaction mismatch"` in run log | ✅ zero |

---

## Build / run cheatsheet

```bash
# install the modified core + RA into the local repo
mvn -o install -pl jainslee-core -DskipTests
(cd vendor-ras/ra-http-server && mvn -o install -DskipTests)

# package + run the example
cd example/example-cmr
mvn -o package -DskipTests
java -jar target/quarkus-app/quarkus-run.jar
# site + admin + dashboard all on http://localhost:8082
#   /            public reader site
#   /admin       admin (admin/admin by default)
#   /telemetry   steampunk observability dashboard
```

---

## Remaining from the original 5-task sequence

- **Task 4** — same Vert.x-removal treatment for the `example-quarkus-helloworld-web`
  template; route the gRPC examples (`example-spring-ussdgw`,
  `example-embedded-j25-ussdgw`) through `ra-grpc-client` instead of building raw
  `NettyChannelBuilder` channels.
- **Task 5** — `jainslee-ai` config discussion: where `url` / `api_key` / `model`
  should live (currently `microjainslee.ai.*` in `application.properties`).

> Note: the helloworld example currently shares the **same** two bugs fixed above
> (it routes every HTTP request through `ra-http-server` → SBB but its SBB never
> sends a response, so the round-trip was a stub). The core + RA fixes here already
> benefit it; Task 4 will finish stripping its app-level Vert.x.

---

# Task 4 — helloworld template Vert.x removal + gRPC violations

**Status:** ✅ Done (2026-07-09)

## 4a. `example-quarkus-helloworld-web` — same treatment as CMR

Same drop-in transformation, so the reference template matches the mandated
architecture (one `ra-http-server` + one gateway SBB, zero app Vert.x).

**New:** `http/HttpReply.java`, `http/MonitorHandler.java` (identical to CMR,
package `com.example.helloworld.quarkus.http`).

**Rewritten (Vert.x stripped):**
- `telemetry/AppTelemetry.java` → `install(container)`, no dashboard server.
- `autonomous/AppAutonomous.java` → `healthJson()` (Jackson), no `mountRoutes`.
- `autonomous/AppAiAgent.java` → engine only, no `mountRoutes`.
- `telemetry/TelemetryLogSink.java` → Jackson instead of `io.vertx.core.json`.
- `sbbs/HelloWorldSbb.java` → now the **gateway SBB**: `@InjectRa` command port,
  `monitor.handle(req).orElseGet(hello)` → `HttpResponseExCommand`. Previously it
  was a stub that only stored the response in a session map and never replied.
- `bootstrap/HelloWorldBootstrap.java` → no `@Inject Vertx`; `install(container)`;
  builds `MonitorHandler`; registers one `ra-http-server` + the gateway SBB;
  `mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb")`.

**Deleted (dead / superseded):** `sbbs/TelemetrySbb.java` (unused stub referencing
Vert.x in comments), `command/HelloWorldCommand.java` + `events/HttpWebRequestEvent.java`
(dead local duplicates — the app uses the `ra-http-server` event/command types),
`bootstrap/HelloWorldContext.java` (session-map bridge no longer needed).

**Verified:** `mvn compile` + `mvn package -Dquarkus.build.skip=false` (augmentation)
**BUILD SUCCESS**; zero `io.vertx` in app source. Runtime path is byte-for-byte the
same gateway/RA/core flow verified live for CMR (60/60 + full round-trip). Ports:
`quarkus.http.port=8080` (CDI host), `http.ra.port=8081` (the RA serves app +
dashboard + APIs). No more `:8090` dashboard.

## 4b. gRPC — transport moved out of app code into the RA

**Violation:** `example-embedded-j25-ussdgw` and `example-spring-ussdgw` built raw
`NettyChannelBuilder.forAddress(...).build()` `ManagedChannel`s in app/bootstrap
code and injected them via the `GrpcMenuUpstream` lambda. Transport belongs in the
RA, not the app (same rule as Vert.x). The proto **stub** is generated from the
app's `.proto`, so it can't move — but the **channel** can.

**Fix — `ra-grpc-client` (`GrpcMenuResourceAdaptor` + `GrpcMenuRaEndpoint`):**
additive channel ownership (no signature change to `GrpcMenuUpstream`, so the clean
Quarkus stub + RA tests are untouched):
- `setTarget(host, port)` — configure the upstream endpoint.
- The RA builds + owns the `ManagedChannel` (plaintext, Netty) across its active
  lifetime (`raActive` opens, `raInactive`/`raUnconfigure` shut down) — exactly like
  the already-channel-owning `GenericGrpcClientRa`.
- `channel()` — accessor the app uses to build **only** its generated stub:
  `SomeServiceGrpc.newBlockingStub(ra.channel())`.

**Apps:** removed every `NettyChannelBuilder`/`ManagedChannel` from bootstrap code;
they now call `endpoint.setTarget(host, port)` and build the stub from
`endpoint.channel()` / `ra.channel()` lazily at call time.

**Verified:** `ra-grpc-client` installed with tests **4/4 green**
(incl. `GrpcMenuResourceAdaptorTest`); app-side channel building **eliminated**
(only comments mention `ManagedChannel`). `grpc-simulator` legitimately keeps raw
gRPC — it is the external test **server/peer**, not a SLEE app.

> **Pre-existing, out-of-scope breakage** (NOT caused by Task 4, all in untouched
> files): the three `*-ussdgw` examples do not currently compile offline for
> unrelated reasons — `example-spring-ussdgw` (Spring-web deps unresolved offline,
> `UssdRestController`), `example-embedded-j25-ussdgw` (`wirePrometheusRa` calls a
> 1-arg `registerRa`; `HttpServerSbb` uses Jackson with no Jackson dep), and
> `example-quarkus-ussdgw` (`UssdSessionStore` stale-target `HttpServerSessionStore`
> resolution — the class **is** present in the installed `ra-http-server` jar).
> These predate this work; the gRPC-transport fix itself is complete and the RA
> change is installed + tested. Per the Quarkus-native focus, cleaning up the
> Spring/embedded examples' unrelated breakage is deferred.
