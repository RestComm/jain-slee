# jainslee-autonomous — Self-Healing Guardian

> **Module:** `jainslee-autonomous` (separate from `jainslee-telemetry`)
>
> `jainslee-telemetry` **collects** → `jainslee-autonomous` **decides & heals**.

---

## Two modules, one job each

| Module | Role |
|--------|------|
| [`jainslee-telemetry`](./jainslee-telemetry.md) | Collect: SBB/RA stats, errors, CPU/RAM, spunk, stale |
| **`jainslee-autonomous`** | Heal: relieve memory pressure, score node health, raise alarms |

---

## What actually ships today

The module is deliberately small and battle-hardened for **run-forever, native
Quarkus/GraalVM deployments** — no restarts, no JVM-flag changes possible.

```
jainslee-autonomous/
└── com/microjainslee/autonomous/
    ├── AutonomousGuardian.java       ← zero-thread memory guardian (JMX-notification driven)
    ├── PressureLevel.java            ← NORMAL / ELEVATED / CRITICAL / EMERGENCY
    └── MemoryReliefParticipant.java  ← @FunctionalInterface "give memory back" contract
```

### AutonomousGuardian — the zero-CPU brain

The guardian owns **no thread**. It arms the JVM's collection-usage-threshold on
the tenured pool; the JVM pushes a JMX notification when a GC finishes with the
pool still above the watermark — that push is the *only* wake-up. Idle system →
zero CPU, zero allocation. `checkNow()` exists for opportunistic piggybacking
(telemetry scrape) and tests.

**Design principles:**

- **Zero-CPU idle**: No polling threads; JMX notification is the sole wake-up.
- **Cooldown-gated**: Every action has a configurable cooldown (default 60s relief, 5 min GC) — a saw-toothing heap cannot create a CPU/GC storm.
- **Fault-isolated**: One failing participant never blocks the rest of the chain.
- **Thread-safe**: `CopyOnWriteArrayList` for participant list, `ConcurrentHashMap` for cooldowns, `volatile` for configuration fields.
- **AutoCloseable**: `close()` wraps `stop()`, suitable for try-with-resources.

**Escalation ladder** (each level implies the ones below it):

| Level | Trigger (default) | Action |
|-------|-------------------|--------|
| `ELEVATED` | heap ≥ 75% | relieve participants: trim caches, expire dedup / out-of-order buffers |
| `CRITICAL` | heap ≥ 88% | + compact off-heap arenas (fragmentation > 25%) + one guarded, rate-limited `System.gc()` |
| `EMERGENCY` | heap ≥ 96% | + application emergency hook (shed load / refuse new activities) |

Every action is **cooldown-protected** so a saw-toothing heap can never turn the
guardian itself into a CPU or GC storm.

### MemoryReliefParticipant — the owner decides

The runtime never guesses what is safe to drop. Each participant owns a piece of
state (a cache, a buffer, an arena) and gives it back on demand:

```java
@FunctionalInterface
public interface MemoryReliefParticipant {
    String name();                        // stable name for logs/metrics
    long relieve(PressureLevel level);    // returns an indicative count freed
}
```

`AutonomousGuardian.attach(container)` registers the standard participants for a
container automatically: the dedup window, the out-of-order buffer, the IES
result cache (dropped only at CRITICAL) and off-heap arena compaction.

### Integration with jainslee-core

The guardian reads state from the core container via well-defined accessors:

| Core component | Accessor | Guardian action |
|---------------|----------|-----------------|
| `DedupWindow` | `container.getDedupWindow()` | `evictExpired(nowMs)` at any pressure |
| `OutOfOrderBuffer` | `container.getOutOfOrderBuffer()` | `evictExpired()` at any pressure |
| `InitialEventSelectorDispatcher` | `container.getEventRouter().getInitialEventSelectorDispatcher()` | `clearIesResultCache()` at CRITICAL+ |
| `OffHeapArena` (via `VirtualThreadSbbEntityPool`) | `container.getSbbEntityPool().getOffHeapArenas()` | `compact()` at CRITICAL+ when fragmentation > 25% |

All accessors are null-safe — if a component is not yet wired or has been
disabled, the participant returns 0 and a debug log is emitted.

---

## App-level layer — HealthEvaluator

The guardian reacts to **memory**. The app template adds a **holistic** health
scorer (`example-quarkus-helloworld-web/.../autonomous/HealthEvaluator.java`)
that reads the [`TelemetryPort`](./jainslee-telemetry.md) snapshot on a fixed
cadence and condenses the whole node into a single traffic light.

| Signal | AMBER | RED |
|--------|-------|-----|
| Heap usage | ≥ 75% | ≥ 90% |
| CPU load | ≥ 0.80 | ≥ 0.95 |
| SBB errors (sum) | ≥ 25 | ≥ 100 |
| Spunk alerts | ≥ 1 | — |
| Leaked entities | — | ≥ 1 |

Alarms are **edge-triggered** — exactly one alarm per transition, never a storm.
On **RED** the evaluator also pokes `guardian.checkNow()` so relief runs
immediately instead of waiting for the next JVM notification.

```
GET /api/autonomous/health
{
  "status": "GREEN",
  "heapPct": 24.0, "cpuLoad": 0.11,
  "errors": 0, "spunks": 0, "reasons": [],
  "guardianLevel": "NORMAL", "reliefRuns": 0
}
```

---

## Wiring (the app template)

```java
// MyBootstrap.init()
container.start();

// 1. Telemetry first — it is the health data source.
TelemetryPort telemetry = appTelemetry.install(container, vertx);

// 2. Autonomous: guardian (memory) + health evaluator (holistic).
appAutonomous.install(container, telemetry);
appAutonomous.mountRoutes(appTelemetry.router());   // GET /api/autonomous/health
```

`AppAutonomous.install()` under the hood:

```java
guardian = new AutonomousGuardian()
        .attach(container)
        .watermarks(0.75, 0.88, 0.96)
        .onEmergency(level -> {
            LOG.error("EMERGENCY heap pressure ({}), shedding load", level);
            telemetry.alarmEngine().fire(TelemetryAlarmLevel.FATAL,
                    "guardian", "near-OOM emergency: " + level, null);
        });
guardian.start();                       // arms JVM notification, no thread
new HealthEvaluator(telemetry, guardian).start();   // one daemon VT
```

---

## Custom relief participant — SS7 example

```java
guardian.register(new MemoryReliefParticipant() {
    @Override public String name() { return "ss7-stale-dialogues"; }
    @Override public long relieve(PressureLevel level) {
        // Only pay this cost when things are actually tight.
        if (level.ordinal() < PressureLevel.CRITICAL.ordinal()) return 0;
        return dialogueRegistry.evictStale(System.currentTimeMillis());
    }
});
```

---

## 🤖 AI Agent — `jainslee-ai` (shipped)

The third leg of the autonomous stack: an **LLM ops advisor** that reads the
telemetry snapshot, asks an OpenAI-compatible endpoint (DeepSeek by default)
for structured analysis, and — under strict guardrails — acts through the
guardian/telemetry control surface. Pure JDK `HttpClient` + Jackson, zero
framework, GraalVM-native friendly.

### Configuration (application.properties)

```properties
microjainslee.ai.enabled=false            # GUI toggle can enable at runtime
microjainslee.ai.api-key=                 # prefer env: MICROJAINSLEE_AI_API_KEY / DEEPSEEK_API_KEY
microjainslee.ai.base-url=https://api.deepseek.com/v1
microjainslee.ai.model=deepseek-chat
microjainslee.ai.mode=ADVISORY            # ADVISORY | SEMI_AUTO | FULL_AUTO
microjainslee.ai.interval-seconds=60
microjainslee.ai.confidence-threshold=0.70
microjainslee.ai.action-cooldown-seconds=300
```

Any OpenAI-compatible backend works by changing `base-url`: DeepSeek, OpenAI,
**Ollama / vLLM / LM Studio** for fully local, air-gapped inference.

### The trust ladder

| Mode | Behaviour |
|------|-----------|
| `ADVISORY` | Analyze + report only. Executes **nothing**. Start here. |
| `SEMI_AUTO` | Passive actions (alarms) at threshold; mutating actions only at confidence ≥ 0.85. |
| `FULL_AUTO` | Every allow-listed action above the confidence threshold. |

### Safety layers (all five from the research spec)

1. **Pre-AI filter** — healthy node (heap<50%, cpu<30%, zero errors/alarms/spunks/leaks) → no LLM call, zero token cost.
2. **Allow-list** — the AI can only: `TRIGGER_RELIEF`, `RELEASE_ENTITY` (target must be a leaked-entity id from the snapshot), `ENABLE/DISABLE_AUTO_RECONFIG`, `RAISE_ALARM`, `INVESTIGATE`, `NONE`. Anything else is dropped.
3. **Confidence gate** — below `confidence-threshold` → advisory only.
4. **Cooldown** — mutating actions share one cooldown window.
5. **Circuit breaker** — 3 consecutive endpoint failures → circuit opens, half-opens after 60s; the rule-based guardian keeps running regardless.

A garbled model reply degrades to an "unparsed" analysis with **zero**
executable recommendations — the parser never throws, the loop never dies.

### The operating model: the agent runs the node, the app holds the remote

The intended production posture is **FULL_AUTO**: hand the node to the agent
and let the app steer it exclusively through the **`AIAgentControl`**
interface — the only API an application needs:

```java
AIAgentControl agent = appAiAgent.engine();

agent.setEnabled(true);              // hand the node over (runtime, no restart)
agent.setMode(AIMode.FULL_AUTO);     // full trust — or dial back any time
agent.analyzeNow();                  // force a cycle before a maintenance window
String brief = agent.report(ReportAudience.BOSS);
AIAgentEngine.Status s = agent.status();   // cheap — no LLM call
```

Analysis cadence, guardrails, action execution and circuit breaking are the
agent's own responsibility and deliberately **not** exposed. The REST surface
below (used by the Monitoring Window) is just a transport over the same
interface.

### Three reports, three voices

| Audience | Endpoint | Voice |
|----------|----------|-------|
| 👤 User | `GET /api/ai/report?audience=user` | Plain language, no jargon — "is the service OK?" |
| 🛠 Dev | `GET /api/ai/report?audience=dev` | Metrics, anomalies, root-cause hypotheses, next actions |
| 💼 Boss | `GET /api/ai/report?audience=boss` | Ten lines max: availability verdict, risk, business impact |

### REST surface (Monitoring Window 🤖 tab)

```
GET  /api/ai/status         counters, mode, availability
GET  /api/ai/analysis       latest structured analysis
POST /api/ai/analyze        force one cycle now
GET  /api/ai/report?audience=user|dev|boss
POST /api/ai/config         {"enabled":true, "mode":"SEMI_AUTO"}   ← runtime, no restart
```

### Module optionality

Every module is independent — an app can run with **nothing but the core
container**:

| Installed | You get |
|-----------|---------|
| core only | the event dispatcher, nothing else |
| + telemetry | metrics, alarms, Prometheus, log sink, GUI Telemetry tab |
| + autonomous | memory guardian + health verdict (needs telemetry for the evaluator) |
| + ai | LLM analysis, reports, guarded actions (needs telemetry; guardian optional — `TRIGGER_RELIEF` downgrades to a log line without it) |

---

## Roadmap — policy engine (design only)

A richer **`AutonomousEngine` + `AutonomousPolicy`** design (CPU-pressure,
load-spike, error-storm and RA-restart policies) is specified but not yet
implemented: [`design-ideas/jainslee-autonomous.md`](../../design-ideas/jainslee-autonomous.md).

---

## References

- [`jainslee-telemetry`](./jainslee-telemetry.md) — the data-collection engine this module heals against
- [`design-ideas/jainslee-ai-agent-research.md`](../../design-ideas/jainslee-ai-agent-research.md) — the AI agent research this implementation follows
