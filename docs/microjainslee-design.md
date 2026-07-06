# micro-jainslee — Architecture & Design

> **Target audience:** Contributors and advanced users who need to understand
> the engine internals, module boundaries, and design rationale.

---

## Goals & Non-Goals

### Goals
- Embeddable JAIN SLEE 1.1 runtime in a single JAR
- 100,000+ events/sec throughput on commodity hardware
- Sub-2-second cold start
- Zero external container dependency (no JBoss, no WildFly)
- Virtual-thread-native concurrency model
- GraalVM native-image compatible (in progress)

### Non-Goals
- Full JAIN SLEE 1.1 TCK compliance (R&D phase)
- JSR-77 JMX management MBeans
- Multi-JVM clustering (single-JVM only)
- Deployment descriptor XML parsing

---

## Module Map

```
jainslee-api          ← Public contracts: Sbb, SleeEvent, ACI, 3-port RA
jainslee-core         ← Engine: MicroSleeContainer, EventRouter, SbbPool, IES
jainslee-ra-spi       ← RA base: AbstractResourceAdaptor, lifecycle FSM
jainslee-scheduler    ← HashedWheelTimer — SLEE timer facility
jainslee-apt          ← Annotation processor → sbb-index.properties
jainslee-codegen      ← Javassist → concrete SBB classes for CMP
jainslee-tx           ← JTA via Narayana (optional)
jainslee-cluster      ← Infinispan/JGroups (optional)
adapter-quarkus       ← Quarkus CDI extension (main target)
adapter-springboot    ← Spring Boot adapter
jainslee-telemetry    ← Zero-CPU observability + self-healing
jainslee-telemetry-vertx ← Steampunk dashboard GUI
```

---

## Event Router Pipeline

```
RA.fireEvent(event, aci)
       │
       ▼
EventRouter.enqueue(event, aci)
       │
       ▼
LMAX Disruptor RingBuffer (slot claimed)
       │
       ▼
EventHandler.onEvent(event, sequence, endOfBatch)
       │
       ├── EventRouter.lookupSbbTypes(event.getClass())
       │       ↓
       │   Map<Class<? extends SleeEvent>, List<String>> eventToSbbTypes
       │
       ├── For each SBB type:
       │       ↓
       │   IES.evaluate(event)
       │       ├── matches InitialEventSelect condition?
       │       │   → route to existing entity (session affinity)
       │       │   → OR create new entity
       │       │
       │       ├── VirtualThreadSbbEntityPool.acquire(entityId)
       │       │   → parked VT unparks, runs event
       │       │
       │       └── entity.submit(() -> sbb.onEvent(event, aci))
       │
       └── [telemetry] SbbCollector.onEventProcessed(...)
```

---

## Virtual Thread SBB Pool

Each SBB entity owns a dedicated virtual thread. The thread is parked
(via `LinkedBlockingQueue.take()`) when idle and unparked when an event
arrives. This guarantees JAIN SLEE §8.4 single-threaded-per-SBB ordering
without locks.

```
VirtualThreadSbbEntityPool
│
├── ConcurrentHashMap<String, SbbEntitySlot>
│       entityId → { VT, queue, sbbInstance }
│
├── acquire(entityId, factory)
│       → getOrCreate slot
│       → park VT on queue.take()
│       → on event: queue.offer(runnable) → VT unparks
│
├── release(entityId)
│       → interrupt VT, remove slot
│
└── prewarm(count, factory)
        → create N parked VTs ahead of time
```

### Ordering Guarantee

Events for entity `E` are always delivered on `E`'s owning virtual thread.
The `LinkedBlockingQueue` is FIFO. No locks are held during SBB execution —
the VT simply processes one event at a time.

---

## 3-Port RA Contract

Every Resource Adaptor exposes exactly three ports:

```
┌──────────────────────────────────────────────┐
│                 ResourceAdaptor               │
│                                               │
│  RaEndpointPort   ← Container activates RA    │
│  RaCommandPort    ← SBB sends commands to RA  │
│  RaBootstrapPort  → RA fires events to SLEE   │
└──────────────────────────────────────────────┘
```

### PolyVoice Pattern

Each RA uses a Wrapper+Delegate pattern:

```java
// Wrapper — implements SLEE lifecycle
public class HttpServerRaEndpoint implements RaEndpointPort {
    private final HttpServerResourceAdaptor delegate;

    public void activate(Properties config) { delegate.start(config); }
    public void deactivate() { delegate.stop(); }
    public RaCommandPort getCommandPort() { return delegate; }
    public void setBootstrapPort(RaBootstrapPort port) { delegate.setPort(port); }
}

// Delegate — implements business protocol + command port
public class HttpServerResourceAdaptor implements RaCommandPort {
    public void sendCommand(RaCommand cmd) {
        switch (cmd) {
            case SendResponse r -> send(r);
            ...
        }
    }
}
```

---

## Adapter Pattern

### Quarkus (Primary)

```
Quarkus Build Time                    Quarkus Runtime
─────────────────                    ────────────────
MicroJainsleeProcessor
  ├── scan @Sbb classes (Jandex)
  ├── generate synthetic CDI beans
  └── MicroJainsleeRecorder
        └── stash container config ──→ MicroSleeContainer
                                       @PostConstruct start()
                                       @PreDestroy stop()
```

### Spring Boot

```
MicroJainsleeAutoConfiguration
  └── @Bean MicroSleeContainer
        └── SmartLifecycle → start()/stop()
```

---

## Telemetry & Self-Healing

The `jainslee-telemetry` module replaces JAIN SLEE 1.1's AlarmFacility,
UsageFacility, and TraceFacility with a modern, zero-CPU passive collection
system built on Micrometer + Prometheus.

### Architecture

```
┌──────────────────────────────────────────────────────┐
│                  jainslee-telemetry                   │
│                                                       │
│  MicrometerTelemetryPort (implements TelemetryPort)   │
│  │                                                    │
│  ├── SbbCollector ──── AtomicLong counters            │
│  ├── RaCollector ───── AtomicLong counters            │
│  ├── ErrorCollector ── RingBuffer<ErrorEntry>(1000)   │
│  ├── ResourceMonitor ─ Daemon VT, 30s interval        │
│  ├── SpunkDetector ─── onEvent callback               │
│  ├── StaleDetector ─── heartbeat + 60s scan           │
│  ├── AlarmEngine ───── RingBuffer<Alarm>(500)         │
│  ├── AutoReconfigEngine ─ 30s evaluate → container    │
│  └── PrometheusExporter ─ OpenMetrics /metrics        │
│                                                       │
│  Integrated via EventRouter hooks:                    │
│    onEventProcessed() → SbbCollector + SpunkDetector  │
│    onError()          → ErrorCollector                │
│    trackHeartbeat()   → StaleDetector                 │
└──────────────────────────────────────────────────────┘
```

### Self-Healing Conditions

| Condition | Threshold | Action |
|-----------|-----------|--------|
| High memory | Heap > 85% | Halve SBB pool max |
| Critical memory | Heap > 95% | Release stale + GC |
| CPU pressure | CPU > 80% sustained | Reduce RA threads |
| Load spike | EPS > 3× baseline | Expand SBB pool ×2 |
| Error storm | >100 errors/min | Suspend SBB type |
| Entity leak | Idle > 30 min | Force release |
| RA crashed | State = ERROR | Restart RA |

Each condition has an independent cooldown timer (5–15 min) to prevent
oscillation. The engine evaluates every 30 seconds on a single daemon VT.

### Dashboard GUI

The `jainslee-telemetry-vertx` module serves a steampunk-themed dashboard
at `/telemetry/`. Single `index.html` + `telemetry.js`, zero build step,
2-second polling of `/api/telemetry/snapshot`. SVG arc gauges for heap/CPU,
sparkline charts, alarm acknowledgment, and runtime config sliders.

> 📖 Full guide: [`docs/jainslee-telemetry.md`](jainslee-telemetry.md)
> 📖 Dashboard: [`docs/telemetry-gui.md`](telemetry-gui.md)

---

## Timer Bridge

```
SBB calls TimerPort.setTimer(...)
       │
       ▼
SleeTimerSchedulerBridge
       │
       ▼
jSS7 HashedWheelTimer (Netty-based)
       │
       ▼
Timer fires → EventRouter.enqueue(TimerEvent)
       │
       ▼
SBB.onEvent(TimerEvent, aci)
```

---

## Configuration Model

```java
MicroSleeConfiguration config = MicroSleeConfiguration.builder()
    .preferVirtualThreads(true)
    .sbbPoolMin(10)
    .sbbPoolMax(100_000)
    .sbbPerVirtualThread(1)
    .eventRouterRingBufferSize(4096)
    .build();
```

### application.properties (Quarkus/Spring)

```properties
microjainslee.sbb.pool.min=10
microjainslee.sbb.pool.max=100000
microjainslee.event-router.ring-buffer-size=4096
microjainslee.virtual-threads=true
microjainslee.telemetry.enabled=true
microjainslee.telemetry.auto-reconfig.enabled=true
```

---

## Concurrency Contract

| Boundary | Mechanism |
|----------|-----------|
| Event ingest | Single Disruptor producer thread |
| Event dispatch | Per-SBB virtual thread (no shared state) |
| SBB local state | Owned by SBB's VT — no locks needed |
| CMP fields | Protected by synchronized blocks (codegen) |
| Telemetry counters | AtomicLong (lock-free) |
| Error ring buffer | AtomicLong write pointer + volatile array |
| ACNF | ConcurrentHashMap (no global lock) |

---

## Failure Modes

| Failure | Detection | Recovery |
|---------|-----------|----------|
| SBB throws | EventRouter catches → ErrorCollector.record() | SBB continues processing next event |
| RA crashes | RA state → ERROR | AutoReconfigEngine restarts RA |
| OOM | JVM-level | Pre-mortem: AutoReconfigEngine reduces pool |
| Disruptor full | BlockingWaitStrategy back-pressures producer | RA.fireEvent() blocks briefly |
| VT leak | StaleDetector finds idle > 30min entities | Auto-release entity |

---

## Design Decision Log

| Decision | Rationale | Date |
|----------|-----------|------|
| LMAX Disruptor over java.util.Queue | 6M events/sec vs 500K, zero GC | 2024-Q3 |
| Virtual threads over platform thread pool | Million-entity scaling, no pool tuning | 2025-Q4 |
| Micrometer over custom metrics | Industry standard, Prometheus native | 2026-Q2 |
| Single HTML dashboard over React/Node | Zero build, zero deps, 15KB | 2026-Q2 |
| AtomicLong ring buffers over locks | No contention, bounded memory | 2026-Q2 |
| Single daemon VT scheduler | Avoids ScheduledExecutorService overhead | 2026-Q2 |
