# App Guide: example-quarkus-helloworld-web (HelloWorld Web on Quarkus)

> Detailed guide for the HelloWorld Web app — HTTP ingress via `ra-http-server` + JAIN SLEE SBB pipeline on Quarkus 3.
> See also: `docs/en/app-guide.md` (wiring pattern), `vendor-ras/ra-http-server/` (RA implementation).
> Last updated: 2026-07-19

---

## 1. What this example does

A minimal "Hello World" web app on Quarkus 3 + micro-jainslee. It uses two HTTP ports:

| Port | Owner | Role |
|---|---|---|
| **8080** | Quarkus Undertow | Static UI (`META-INF/resources/`) + Quarkus REST (`GET /health`) |
| **8081** | `ra-http-server` | JAIN SLEE event ingress — app responses, telemetry dashboard, `/api/telemetry/*` |

A request on port 8081 becomes an `HttpWebRequestEvent`, is routed to `HelloWorldSbb`, which either serves the telemetry/monitor surface or returns a Hello World HTML page. Responses go back through the injected `ra-http-server` command port — no app-level Vert.x.

Optional telemetry (`microjainslee.telemetry.enabled`) adds the dashboard at `/telemetry`, Prometheus scrape RA, and a batched log sink.

---

## 2. Directory layout

```
example/example-quarkus-helloworld-web/
├── pom.xml
├── build/
│   ├── build.xml                    ← Ant → Maven wrapper (Java 25)
│   ├── run.sh                       ← run packaged app
│   └── package-dist.sh              ← classic dist/<app>-jainslee/ layout
├── src/main/resources/
│   ├── application.properties
│   ├── log4j2.xml
│   └── META-INF/resources/
│       └── index.html               ← static UI (Quarkus :8080)
└── src/main/java/com/example/helloworld/quarkus/
    ├── bootstrap/
    │   └── HelloWorldBootstrap.java ← CDI: start container, telemetry, RA, SBB
    ├── telemetry/
    │   ├── AppTelemetry.java        ← Micrometer port + Prometheus RA + log sink
    │   └── TelemetryLogSink.java
    ├── http/
    │   ├── MonitorHandler.java      ← /telemetry GUI + /api/telemetry/*
    │   └── HttpReply.java
    ├── sbbs/
    │   └── HelloWorldSbb.java       ← gateway SBB (monitor → Hello World)
    └── rest/
        └── HealthResource.java      ← Quarkus GET /health → {"status":"ok"}
```

---

## 3. Dependencies and config

> File: `pom.xml`

Key dependencies:

- **Quarkus**: `quarkus-rest`, `quarkus-rest-jackson`, `quarkus-arc`, `quarkus-undertow`
- **micro-jainslee**: `jainslee-core`, `jainslee-api`, `jainslee-apt`, **`adapter-quarkus`** (CDI producer for `MicroSleeContainer`)
- **RAs**: `ra-http-server`, `ra-prometheus-exporter`
- **Observability**: `jainslee-telemetry`, `jainslee-monitor`, Micrometer Prometheus registry

> Note: this example **does** use `adapter-quarkus`. `HelloWorldBootstrap` injects `MicroSleeContainer` from that extension.

`application.properties`:

```properties
# Quarkus HTTP — serves static web UI
quarkus.http.port=8080

# ra-http-server port — JAIN SLEE event ingress
http.ra.port=8081

# micro-jainslee core config (adapter-quarkus build-time mapping)
microjainslee.container.buffer-size=4096
microjainslee.container.prefer-virtual-threads=true
microjainslee.container.sbb-pool-min=16
microjainslee.container.sbb-pool-max=10000
quarkus.config.mapping.validate-unknown=false

# Optional telemetry (dashboard + /api/telemetry/* via ra-http-server)
microjainslee.telemetry.enabled=true
```

---

## 4. How the RA connects into jainslee

`HelloWorldBootstrap` wires everything in a fixed order.

> File: `src/main/java/.../bootstrap/HelloWorldBootstrap.java`

### Step 1: Observe `StartupEvent` (eager RA wiring)

`@ApplicationScoped` beans are lazy. If nothing injects the bootstrap, `@PostConstruct`
alone never runs and the HTTP RA stays unwired. Observe Quarkus `StartupEvent` instead:

```java
@Inject
MicroSleeContainer container;

void onStart(@Observes StartupEvent ev) {
    if (container.getState() != MicroSleeContainer.State.STARTED) {
        container.start();
    }
```

### Step 2: Optional telemetry

```java
TelemetryPort telemetry = null;
if (telemetryEnabled) {
    telemetry = appTelemetry.install(container);
}
MonitorHandler monitor = telemetry == null ? null : new MonitorHandler(telemetry);
```

### Step 3: Register the SBB (constructor collaborator)

```java
container.registerSbbType(HelloWorldSbb.class, () -> new HelloWorldSbb(monitor));
```

`MonitorHandler` is nullable when telemetry is off. The SBB receives it via constructor (collaborator pattern).

### Step 4: IES dispatcher + event map

```java
container.createIesDispatcher();
container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");
```

One event type is mapped: every HTTP request from `ra-http-server` fires
`com.microjainslee.ra.httpserver.events.HttpWebRequestEvent`.

### Step 5: Wire the HTTP RA

```java
HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
ra.setPort(httpRaPort);
ra.setHost("0.0.0.0");

httpEndpoint = new HttpServerRaEndpoint(ra);
httpEndpoint.setPort(httpRaPort);
container.registerRa(httpEndpoint, httpEndpoint);
```

On `registerRa()`:

- `HttpServerRaEndpoint.activate(...)` builds a `ResourceAdaptorContext` from `RaBootstrapPort`
- then `delegate.raConfigure()` → `delegate.raActive()`
- `raActive()` starts the Vert.x HTTP server, binds `host:port`, and routes each request into the SLEE event pipeline

**Cleanup:**

```java
@PreDestroy
void shutdown() {
    appTelemetry.close();
    if (httpEndpoint != null) {
        httpEndpoint.deactivate();   // → raInactive() → close Vert.x server
    }
    if (container.getState() == MicroSleeContainer.State.STARTED) {
        container.stop();
    }
}
```

`HttpServerRaEndpoint` implements both `RaEndpointPort` and `RaCommandPort`.
`getRaName()` returns `"http-server-ra"`, matching `@InjectRa(name = "http-server-ra")` on `HelloWorldSbb`.

---

## 5. SBB — business logic

### 5.1 HelloWorldSbb

> File: `src/main/java/.../sbbs/HelloWorldSbb.java`

**Purpose:** single HTTP gateway SBB. Dispatches by path:

1. `MonitorHandler` — `/telemetry`, `/api/telemetry/*` (empty → fall through)
2. otherwise Hello World HTML

**Constructor collaborator:**

```java
public HelloWorldSbb(MonitorHandler monitor) {
    this.monitor = monitor;
}
```

**Event handling + response via command port:**

```java
@Override
public void onEvent(SleeEvent event, ActivityContextInterface aci) {
    if (!(event instanceof HttpWebRequestEvent req)) {
        return;
    }
    HttpReply reply = dispatch(req);
    http.sendCommand(new HttpServerCommand.HttpResponseExCommand(
            req.getSessionId(), reply.status(), reply.contentType(),
            reply.text(), reply.binary(), reply.headers()));
}
```

`@InjectRa(name = "http-server-ra")` injects the command port used to write the HTTP response.

### 5.2 Event type

This example uses the **RA-provided** event:

`com.microjainslee.ra.httpserver.events.HttpWebRequestEvent`

There is no app-defined duplicate event or command hierarchy.

---

## 6. Events & commands

| Event | Source | Handled by | Reply path |
|---|---|---|---|
| `HttpWebRequestEvent` (RA) | HTTP → Vert.x RA → EventRouter | `HelloWorldSbb` | `HttpResponseExCommand` via `@InjectRa` |

Telemetry surface (when enabled):

| Path | Handler |
|---|---|
| `/telemetry`, `/telemetry/*` | Dashboard GUI (`jainslee-monitor` classpath resources) |
| `/api/telemetry/snapshot` | JSON snapshot |
| `/api/telemetry/metrics` | Prometheus text scrape |
| `/api/telemetry/alarms` | Active alarms |
| `POST /api/telemetry/alarms/{id}/clear` | Clear alarm |
| `POST /api/telemetry/config` | Toggle auto-reconfig |

---

## 7. Call flow

```
┌─────────────┐     GET /  (or /telemetry)   ┌──────────────────────────┐
│  Browser /   │ ───────────────────────────▶ │  Vert.x HTTP Server      │
│  curl        │   port 8081                  │  (HttpServerResource     │
└─────────────┘                               │   Adaptor.raActive)      │
                                               └────────────┬─────────────┘
                                                            │
                                                            ▼
                                               ┌──────────────────────────┐
                                               │ HttpServerResourceAdaptor │
                                               │  .route(req)             │
                                               │  ├─ read body async      │
                                               │  └─ fire HttpWebRequest  │
                                               │     Event → EventRouter  │
                                               └────────────┬─────────────┘
                                                            │
                                                            ▼
                                               ┌──────────────────────────┐
                                               │     EventRouter          │
                                               │  → HelloWorldSbb         │
                                               └────────────┬─────────────┘
                                                            │
                                                            ▼
                                               ┌──────────────────────────┐
                                               │    HelloWorldSbb         │
                                               │  ├─ MonitorHandler?      │
                                               │  │   /telemetry,         │
                                               │  │   /api/telemetry/*    │
                                               │  └─ else Hello World HTML│
                                               │  → HttpResponseExCommand │
                                               └──────────────────────────┘
```

Quarkus port 8080 is separate: static `index.html` + `GET /health`.

---

## 8. How to run

### Dev mode

```bash
cd example/example-quarkus-helloworld-web
mvn -Dquarkus.build.skip=false quarkus:dev
```

### Ant wrapper (recommended for packaging)

```bash
cd example/example-quarkus-helloworld-web
ant -f build/build.xml install-deps   # install reactor modules to ~/.m2
ant -f build/build.xml package        # Quarkus fast-jar → target/quarkus-app/
ant -f build/build.xml run            # ./build/run.sh
ant -f build/build.xml dist           # classic dist/<app>-jainslee/
```

After start:

- **Quarkus UI**: `http://localhost:8080/`
- **Health**: `curl http://localhost:8080/health` → `{"status":"ok"}`
- **HelloWorld (SLEE)**: `curl http://localhost:8081/`
- **Telemetry dashboard**: `http://localhost:8081/telemetry`
- **Telemetry snapshot**: `curl http://localhost:8081/api/telemetry/snapshot`

---

## 9. Tests

Unit tests cover the telemetry log sink (`TelemetryLogSinkTest`). Run:

```bash
mvn test
```

Suggested smoke pattern for the full pipeline (not required in-tree):

- Build a small `MicroSleeContainer` (small buffer, no virtual threads if desired)
- Set `http.ra.port=0` for an ephemeral bind
- Drive `HelloWorldBootstrap` / register RA + SBB as in production
- `curl` the bound port and assert Hello World / telemetry JSON
