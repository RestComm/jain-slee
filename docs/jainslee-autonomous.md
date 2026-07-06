# jainslee-autonomous — Self-Healing & Policy Engine

> **Module:** `jainslee-autonomous` (tách biệt khỏi `jainslee-telemetry`)
>
> Telemetry thu thập dữ liệu → **Autonomous ra quyết định** → Hành động tự động.

---

## Tách biệt hai module

| Module | Vai trò |
|--------|---------|
| [`jainslee-telemetry`](./jainslee-telemetry.md) | Thu thập: SBB/RA stats, errors, CPU/RAM, spunk, stale |
| **`jainslee-autonomous`** | Ra quyết định: scale pool, suspend SBB, restart RA, alarm |

---

## Kiến trúc

```
┌─────────────────┐     ┌─────────────────────┐
│  TelemetryPort   │────►│  AutonomousEngine    │
│  .snapshot()     │ 30s │  .evaluate(snapshot) │
│                  │     │                      │
│  sbbs, ras,      │     │  MemoryPressurePolicy│
│  resources,       │     │  CpuPressurePolicy    │
│  errors, spunks,  │     │  LoadSpikePolicy      │
│  stales           │     │  ErrorStormPolicy     │
│                  │     │  StaleEntityPolicy     │
│                  │     │  RaCrashPolicy         │
└─────────────────┘     └──────────┬───────────┘
                                   │
                          ┌────────┴───────────┐
                          │  MicroSleeContainer  │
                          │  .scalePool()        │
                          │  .suspendSbb()       │
                          │  .restartRa()        │
                          └──────────────────────┘
```

---

## AutonomousPolicy Interface

```java
public interface AutonomousPolicy {
    String name();
    List<Action> evaluate(TelemetryPort.TelemetrySnapshot snapshot);

    sealed interface Action {
        String reason();
    }
    record ScalePool(String sbbType, int min, int max, String reason) implements Action {}
    record SuspendSbb(String sbbType, String reason) implements Action {}
    record RestartRa(String raName, String reason) implements Action {}
    record TriggerGc(String reason) implements Action {}
    record Notify(AlarmLevel level, String message) implements Action {}
}
```

---

## Built-in Policies

| Policy | Trigger | Action |
|--------|---------|--------|
| `MemoryPressurePolicy` | Heap > 85% | `ScalePool` — giảm pool max 50% |
| | Heap > 95% | `ScalePool` — release stale + `TriggerGc` |
| `CpuPressurePolicy` | CPU > 80% | `ScalePool` — giảm RA event loop threads |
| `LoadSpikePolicy` | EPS > 3x baseline | `ScalePool` — tăng pool max 2x |
| `ErrorStormPolicy` | >100 errors/min | `SuspendSbb` — suspend SBB type |
| `StaleEntityPolicy` | Idle > 30min | `ScalePool` — force release entity |
| `RaCrashPolicy` | RA state = ERROR | `RestartRa` — restart RA |

---

## Wiring trong Bootstrap

```java
// HelloWorldBootstrap.java
@PostConstruct void init() {
    container.start();

    // 1. Telemetry (data collection)
    var telemetry = new MicrometerTelemetryPort(registry, container);
    container.bindTelemetryPort(telemetry);
    telemetry.start();

    // 2. Autonomous (decision engine)
    var autonomous = new DefaultAutonomousEngine(container);
    autonomous.addPolicy(new MemoryPressurePolicy());
    autonomous.addPolicy(new CpuPressurePolicy());
    autonomous.addPolicy(new LoadSpikePolicy());
    autonomous.addPolicy(new ErrorStormPolicy());
    autonomous.addPolicy(new StaleEntityPolicy());
    autonomous.addPolicy(new RaCrashPolicy());
    autonomous.addPolicy(new DialogTimeoutPolicy());  // SS7-specific!
    container.bindAutonomousEngine(autonomous);
    autonomous.start(30, TimeUnit.SECONDS);

    // 3. EventRouter → telemetry (passive hooks)
    container.getEventRouter().setTelemetryPort(telemetry);
}
```

---

## Custom Policy — SS7 Example

```java
public final class DialogTimeoutPolicy implements AutonomousPolicy {
    @Override public String name() { return "dialog-timeout"; }

    @Override
    public List<Action> evaluate(TelemetryPort.TelemetrySnapshot snap) {
        // Check custom metrics from telemetry
        long staleDialogs = snap.customMetrics().stream()
            .filter(m -> "ss7_stale_dialogues".equals(m.name()))
            .mapToLong(m -> m.gaugeValue().longValue())
            .sum();

        if (staleDialogs > 100) {
            return List.of(new Notify(AlarmLevel.WARNING,
                "SS7 stale dialogues exceed threshold: " + staleDialogs));
        }
        return List.of();
    }
}
```

---

## References

- [`jainslee-telemetry`](./jainslee-telemetry.md) — Data collection engine
- [`design-ideas/jainslee-autonomous.md`](../design-ideas/jainslee-autonomous.md) — Full design spec
