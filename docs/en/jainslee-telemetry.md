# jainslee-telemetry — Zero-CPU Observability & Metrics Engine

> **Modules:** `jainslee-telemetry` (+ `jainslee-monitor` for HTTP REST API)
>
> **Replaces:** JAIN SLEE 1.1 AlarmFacility, UsageFacility, TraceFacility
>
> **Note:** The `AutoReconfigEngine` (self-healing) lives inside the telemetry
> module itself — it triggers on JVM memory-threshold notifications + Prometheus
> scrape piggyback, with zero timer threads. The [`jainslee-autonomous`](./jainslee-autonomous.md)
> module adds higher-level autonomous decision-making on top.
>
> **Philosophy:** Passive collection, zero polling, AtomicLong counters, **zero timer threads**

---

## Overview

The `jainslee-telemetry` module provides a modern, zero-overhead observability layer

for micro-jainslee. Instead of the heavyweight JMX MBeans, JMS-based alarms, and

polling-based usage tracking required by JAIN SLEE 1.1, this module uses:

- **AtomicLong / LongAdder counters** — zero-lock, zero-contention metric accumulation
- **Ring buffers** — bounded, lock-free error and alarm history
- **Zero-CPU lazy capture** — ResourceMonitor captures only on read (Prometheus scrape,
  dashboard poll); no background threads, zero CPU at idle
- **JVM memory-threshold notifications** — AutoReconfigEngine is event-driven,
  wakes only when tenured pool exceeds 85% after GC
- **Micrometer + Prometheus** — 23 core Micrometer gauges registered on startup,
  industry-standard metrics export, no custom wire format
- **Passive callbacks from EventRouter** — no polling, no interception, just a

  one-line `.record()` after each event dispatch

<p align="center"><img src="../images/jainslee-telemetry-architecture.svg" width="800"/></p>

---

## Architecture

### TelemetryPort API (`jainslee-api`)

The public contract lives in `jainslee-api` as a single interface:

```java
public interface TelemetryPort {
    SbbCollector sbbCollector();
    RaCollector raCollector();
    ErrorCollector errorCollector();
    ResourceMonitor resourceMonitor();
    SpunkDetector spunkDetector();
    StaleDetector staleDetector();
    AlarmEngine alarmEngine();
    AutoReconfigEngine autoReconfig();
    boolean isAutoReconfigEnabled();
    void setAutoReconfigEnabled(boolean enabled);
    String scrape();               // OpenMetrics text format
    TelemetrySnapshot snapshot();  // consolidated for GUI
}
```

### MicrometerTelemetryPort (`jainslee-telemetry`)

The production implementation wraps a `PrometheusMeterRegistry` and wires all

collectors together in a single constructor.

---

## Collectors

### 1. SbbCollector

Tracks every SBB entity lifecycle and event processing. Called **passively** by

EventRouter after each dispatch — no polling, no interception overhead.

```java
public final class SbbCollector {
    long getTotalEntities();
    long getActiveEntities();
    long getEventsProcessed();
    double getEventsPerSecond();
    long getAvgLatencyUs();
    long getP99LatencyUs();
    long getErrorCount();
    long getSpunkCount();
    boolean isHealthy();

    record PerType(String sbbType, long active, long errors,
                   long spunks, double eps, long p99us) {}
    List<PerType> getPerType();

    void onEventProcessed(String sbbType, String entityId,
                          long latencyNs, long memDeltaBytes);
    void onEntityCreated(String sbbType, String entityId);
    void onEntityReleased(String sbbType, String entityId);
}
```


| Metric (Prometheus)                   | Type    | Description               |
| ------------------------------------- | ------- | ------------------------- |
| `microjainslee_sbb_entities_total`    | Gauge   | Total entities created    |
| `microjainslee_sbb_entities_active`   | Gauge   | Currently active entities |
| `microjainslee_sbb_events_total`      | Counter | Events processed          |
| `microjainslee_sbb_events_per_second` | Gauge   | Throughput                |
| `microjainslee_sbb_latency_avg_us`    | Gauge   | Average dispatch latency  |
| `microjainslee_sbb_latency_p99_us`    | Gauge   | 99th percentile latency   |
| `microjainslee_sbb_errors_total`      | Counter | Total error count         |


### 2. RaCollector

Monitors every Resource Adaptor: state, port binding, event throughput.

```java
public final class RaCollector {
    record RaStats(String raName, String state, String port,
                   long eventsFired, long commandsReceived,
                   long sessionsActive, long uptimeSeconds) {}

    List<RaStats> getAll();
    Optional<RaStats> get(String raName);
    void onRaActivated(String raName, String port);
    void onRaDeactivated(String raName);
    void onEventFired(String raName);
    void onCommandReceived(String raName);
}
```


| Metric                                     | Type                         | Description         |
| ------------------------------------------ | ---------------------------- | ------------------- |
| `microjainslee_ra_state`                   | Gauge (1=ACTIVE, 0=INACTIVE) | Per-RA state        |
| `microjainslee_ra_events_fired_total`      | Counter                      | Events fired by RA  |
| `microjainslee_ra_commands_received_total` | Counter                      | Commands sent to RA |


### 3. ErrorCollector

Lock-free ring buffer of the last 1000 errors. No locks — AtomicLong write pointer.

Fixed size 1000 entries, `writeIndex` wraps with `& (SIZE - 1)` (power-of-two).

```java
public final class ErrorCollector {
    record ErrorEntry(String sbbType, String entityId,
                      String exceptionType, String message,
                      String stackTrace, long timestamp) {}

    void record(String sbbType, String entityId, Throwable error);
    List<ErrorEntry> recent(int minutes);
    List<ErrorEntry> lastN(int n);
    Map<String, Long> errorRateByType();
    long errorCountLastMinute();
    boolean isErrorStorm(int thresholdPerMinute);
}
```

### 4. ResourceMonitor

Captures JVM resource state via a single daemon Virtual Thread.

```java
public final class ResourceMonitor {
    record ResourceSnapshot(
        long heapUsedMb, long heapMaxMb, double heapUsagePercent,
        double cpuLoad, int activeThreads, int virtualThreads,
        long gcCount, long gcTimeMs, long openFileDescriptors,
        long uptimeSeconds
    ) {}

    ResourceSnapshot snapshot();
    Stream<ResourceSnapshot> history();  // last 60 min (120 samples)
    void start(long interval, TimeUnit unit);
    void stop();
}
```


| Metric                                   | Type    | Description                |
| ---------------------------------------- | ------- | -------------------------- |
| `microjainslee_resource_heap_used_mb`    | Gauge   | Heap used (MB)             |
| `microjainslee_resource_heap_max_mb`     | Gauge   | Max heap (MB)              |
| `microjainslee_resource_heap_usage_pct`  | Gauge   | Heap usage %               |
| `microjainslee_resource_cpu_load`        | Gauge   | Process CPU load (0.0–1.0) |
| `microjainslee_resource_threads_active`  | Gauge   | Active platform threads    |
| `microjainslee_resource_threads_virtual` | Gauge   | Active virtual threads     |
| `microjainslee_resource_gc_count`        | Counter | GC collections             |
| `microjainslee_resource_gc_time_ms`      | Counter | GC pause time (ms)         |


### 5. SpunkDetector

Detects anomalous SBB behavior ("spunk") — SBBs that are misbehaving or

resource-hogging.

```java
public final class SpunkDetector {
    record SpunkAlert(String sbbType, String entityId, String reason,
                      long timestamp, Map<String, Object> context) {}

    void onEventProcessed(String sbbType, String entityId,
                          long latencyNs, long memDeltaBytes);
    List<SpunkAlert> detectSpunks();
    List<SpunkAlert> recent(int minutes);
}
```


| Spunk Condition     | Threshold                                 | Severity |
| ------------------- | ----------------------------------------- | -------- |
| Event loop blocking | `latency > 100ms`                         | WARNING  |
| Memory spike        | `memDelta > 100MB` in single entity       | WARNING  |
| CPU hog             | Single SBB type &gt; 50% total CPU        | CRITICAL |
| Entity explosion    | &gt; 1000 child entities created in 1 min | WARNING  |


### 6. StaleDetector

Identifies entities that haven't received events — either idle (warning) or

leaked (critical, requires force-release).

```java
public final class StaleDetector {
    record StaleAlert(String entityId, String sbbType,
                      long lastEventMs, long idleDurationMs,
                      boolean leaked) {}

    void trackHeartbeat(String entityId, String sbbType);
    List<StaleAlert> detectStale(long warningThresholdMs,
                                  long leakThresholdMs);
}
```


| Condition     | Threshold            | Action                               |
| ------------- | -------------------- | ------------------------------------ |
| Idle entity   | No event &gt; 5 min  | `AlarmLevel.INFO` warning            |
| Leaked entity | No event &gt; 30 min | `AlarmLevel.CRITICAL` + auto-release |


### 7. AlarmEngine

Replaces JAIN SLEE 1.1 `AlarmFacility`. Ring buffer of 500 alarms.

```java
public enum AlarmLevel { INFO, WARNING, CRITICAL, FATAL }

public record Alarm(String id, AlarmLevel level, String source,
                    String message, long timestamp,
                    Map<String, Object> context) {}

public final class AlarmEngine {
    void fire(AlarmLevel level, String source, String message,
              Map<String, Object> context);
    boolean acknowledge(String alarmId);
    List<Alarm> active();
    List<Alarm> history(int minutes);
    int activeCount();
}
```

**Alarm lifecycle:**

<p align="center"><img src="../images/jainslee-telemetry-alarm-lifecycle.svg" width="600"/></p>

---

## ⚡ Auto-Reconfig Engine

The AutoReconfigEngine automatically adjusts JAIN SLEE configuration based on

real-time metrics. **No human intervention required.**

### Evaluation Cycle

Single daemon VT evaluates every 30 seconds:

```java
public final class AutoReconfigEngine {
    void evaluate() {
        checkMemoryPressure();
        checkCpuPressure();
        checkSbbLoadSpike();
        checkErrorStorm();
        checkStaleEntities();
        checkRaCrashed();
    }

    void start(long interval, TimeUnit unit);
    void stop();
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
```

### Reconfig Conditions &amp; Actions


| #   | Condition           | Threshold                                 | Action                               | Alarm    | Cooldown          |
| --- | ------------------- | ----------------------------------------- | ------------------------------------ | -------- | ----------------- |
| 1   | High memory         | Heap &gt; 85%                             | Halve SBB pool max                   | WARNING  | 5 min             |
| 2   | Critical memory     | Heap &gt; 95%                             | Release stale entities + System.gc() | CRITICAL | 2 min             |
| 3   | CPU pressure        | CPU &gt; 80% sustained 2 cycles           | Reduce RA event-loop threads by 25%  | WARNING  | 10 min            |
| 4   | CPU recovered       | CPU &lt; 50% sustained 3 cycles           | Restore RA threads to original       | INFO     | —                 |
| 5   | Load spike          | EPS &gt; 3× baseline per SBB type         | Expand SBB pool × 2                  | INFO     | 5 min             |
| 6   | Load normalized     | EPS &lt; 1.5× baseline sustained 5 cycles | Shrink pool back to normal           | INFO     | —                 |
| 7   | Error storm         | &gt; 100 errors/min for single SBB type   | Suspend SBB type                     | CRITICAL | 15 min            |
| 8   | Error storm cleared | 0 errors for suspended SBB in 5 min       | Resume SBB type                      | INFO     | —                 |
| 9   | Entity leak         | Idle &gt; 30 min                          | Force-release entity                 | CRITICAL | None (per entity) |
| 10  | RA crashed          | RA state = ERROR                          | Restart RA                           | CRITICAL | 2 min             |


### Container API for Reconfig

The engine calls back into `MicroSleeContainer`:

```java
// MicroSleeContainer — methods exposed for auto-reconfig
void reduceSbbPoolMax(int newMax);
void expandSbbPool(int newMax);
void suspendSbbType(String sbbType);
void resumeSbbType(String sbbType);
void restartRa(String raName);
void releaseEntity(String entityId);
```

### Cooldown Behavior

Each condition has a cooldown period to prevent oscillation. During cooldown,

the same condition is skipped even if the threshold is still breached. Cooldown

is tracked per-condition with a `Map<Condition, Long>` of last-fire timestamps.

---

## Core Micrometer Metrics (Prometheus)

`MicrometerTelemetryPort` registers 23 core gauges on startup. All are backed
by the passive collectors — zero-CPU when not scraped.

| Prometheus Metric                    | Source             | Description                        |
|--------------------------------------|--------------------|------------------------------------|
| `jainslee_sbb_entities_total`        | SbbCollector       | Total SBB entities created         |
| `jainslee_sbb_entities_active`       | SbbCollector       | Currently active SBB entities      |
| `jainslee_sbb_events_total`          | SbbCollector       | Total events processed             |
| `jainslee_sbb_events_per_second`     | SbbCollector       | Event throughput (60s window)      |
| `jainslee_sbb_errors_total`          | SbbCollector       | Total processing errors            |
| `jainslee_sbb_spunks_total`          | SbbCollector       | Total anomaly detections           |
| `jainslee_sbb_stale_entities`        | SbbCollector       | Stale (idle) entity count          |
| `jainslee_sbb_leaked_entities`       | SbbCollector       | Leaked entity count                |
| `jainslee_sbb_healthy`               | SbbCollector       | 1=healthy, 0=unhealthy             |
| `jainslee_heap_used_mb`              | ResourceMonitor    | Heap used (MB)                     |
| `jainslee_heap_max_mb`               | ResourceMonitor    | Heap max (MB)                      |
| `jainslee_heap_usage_percent`        | ResourceMonitor    | Heap usage percentage              |
| `jainslee_cpu_load_percent`          | ResourceMonitor    | Process CPU load percentage        |
| `jainslee_threads_active`            | ResourceMonitor    | Active platform threads            |
| `jainslee_threads_virtual`           | ResourceMonitor    | Virtual thread count               |
| `jainslee_gc_count`                  | ResourceMonitor    | Total GC collections               |
| `jainslee_gc_time_ms`                | ResourceMonitor    | Total GC time (ms)                 |
| `jainslee_open_fds`                  | ResourceMonitor    | Open file descriptors              |
| `jainslee_ra_events_fired_total`     | RaCollector        | Total RA events fired              |
| `jainslee_ra_commands_sent_total`    | RaCollector        | Total commands sent to RAs         |
| `jainslee_ra_failures_total`         | RaCollector        | Total RA failures                  |
| `jainslee_stale_tracked_entities`    | StaleDetector      | Entities tracked for staleness     |
| `jainslee_alarms_active`             | AlarmEngine        | Currently active (uncleared) alarms|

---

## jainslee-monitor — HTTP REST Server

The `jainslee-monitor` module provides a standalone Vert.x HTTP server
that exposes all telemetry data as REST endpoints — ready for Prometheus scraping,
dashboard polling, and health checks.

### Dependency

```xml
<dependency>
    <groupId>com.microjainslee</groupId>
    <artifactId>jainslee-monitor</artifactId>
    <version>${microjainslee.version}</version>
</dependency>
```

### Usage

```java
var registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
var telemetry = new MicrometerTelemetryPort(registry, container);
telemetry.start();

var server = new TelemetryVertxServer(telemetry, 8090);
server.start();  // listens on :8090
```

### Endpoints

| Method | Path                                  | Description                    |
|--------|---------------------------------------|--------------------------------|
| GET    | `/metrics`                            | Prometheus OpenMetrics scrape  |
| GET    | `/api/telemetry/snapshot`             | Full JSON snapshot             |
| GET    | `/api/telemetry/alarms`               | Active alarms (JSON array)     |
| GET    | `/api/telemetry/alarms/history?minutes=60` | Alarm history             |
| GET    | `/api/telemetry/health`               | Health status (UP/DEGRADED)    |
| GET    | `/api/telemetry/custom`               | Custom app-defined metrics     |

---

## Integration Guide

### Step 1: Add Dependency

```xml
[[ORCA_RAW_HTML_BLOCK:%3Cdependency%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CgroupId%3E]]com.microjainslee[[ORCA_RAW_HTML_INLINE:%3C%2FgroupId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CartifactId%3E]]jainslee-telemetry[[ORCA_RAW_HTML_INLINE:%3C%2FartifactId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3Cversion%3E]]${microjainslee.version}[[ORCA_RAW_HTML_INLINE:%3C%2Fversion%3E]]
[[ORCA_RAW_HTML_BLOCK:%3C%2Fdependency%3E]]
[[ORCA_RAW_HTML_BLOCK:%3Cdependency%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CgroupId%3E]]com.microjainslee[[ORCA_RAW_HTML_INLINE:%3C%2FgroupId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CartifactId%3E]]jainslee-monitor[[ORCA_RAW_HTML_INLINE:%3C%2FartifactId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3Cversion%3E]]${microjainslee.version}[[ORCA_RAW_HTML_INLINE:%3C%2Fversion%3E]]
[[ORCA_RAW_HTML_BLOCK:%3C%2Fdependency%3E]]
```

### Step 2: Wire in Bootstrap

Use the drop-in `AppTelemetry` module (from the app template) — one call wires
collectors, Prometheus, the batched Log4j sink and the dashboard:

```java
@PostConstruct
void init() {
    container.start();
    TelemetryPort telemetry = appTelemetry.install(container, vertx);
    // Prometheus RA on :9090, dashboard + REST on :8090, telemetry.log batch sink running.
}
```

`AppTelemetry.install()` under the hood:

```java
var registry  = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
var telemetry = new MicrometerTelemetryPort(registry, container);
telemetry.start();                       // arms zero-CPU resource monitor + auto-reconfig

new TelemetryLogSink(telemetry).start(); // batched JSON-lines Log4j sink

var promRa = new PrometheusResourceAdaptor();
promRa.setPort(9090);
var promEndpoint = new PrometheusRaEndpoint(promRa);
container.registerRa(promEndpoint, promEndpoint);  // endpoint is both ports

Router router = Router.router(vertx);
router.route("/telemetry/*").handler(StaticHandler.create("META-INF/resources"));
router.get("/api/telemetry/snapshot").handler(ctx -> ctx.json(telemetry.snapshot()));
router.get("/api/telemetry/metrics").handler(ctx -> ctx.end(telemetry.scrape()));
vertx.createHttpServer().requestHandler(router).listen(8090);
```

### Step 3: EventRouter Feeding — DispatchObserver

The per-SBB collectors are fed straight from the dispatch path through the
core's **`DispatchObserver`** seam. `jainslee-core` owns the interface (so it
owes telemetry no dependency); `jainslee-telemetry` ships the bridge that fans
each delivery outcome out to the right collectors:

```java
// One line in your bootstrap (AppTelemetry.install() already does this):
container.getEventRouter().setDispatchObserver(
        new TelemetryDispatchObserver(telemetry));
```

What one registration feeds:

| Delivery outcome | Collector updates |
|------------------|-------------------|
| `onEvent` returned | `SbbCollector.onEventProcessed` (throughput, p99 latency) + `SpunkDetector` sample + `StaleDetector` heartbeat |
| `onEvent` threw | `SbbCollector.onError` + `ErrorCollector.record` (ring buffer) + `StaleDetector` heartbeat — an erroring entity shows as an **error storm**, not a leak |

The router notifies the observer on **every** delivery path (inline,
per-entity virtual thread, ASYNC_COMMIT), measures the wall time actually
spent inside `onEvent`, and shields itself from a throwing observer.
When no observer is registered the cost is **one volatile read per delivery**
— nothing else. Latency is measured on the delivering thread, so p99 numbers
are the SBB's real processing time, not queue time.

### Step 4: Verify

```bash
curl http://localhost:8080/api/telemetry/metrics
curl http://localhost:8080/api/telemetry/snapshot | jq .
open http://localhost:8080/telemetry/
```

---

## App-Defined Custom Metrics (Extensible)

Every app domain can register its own counters and gauges at runtime.  
They automatically appear in `snapshot().customMetrics`, Prometheus scrape,  
and the dashboard GUI — **zero extra wiring**.

### Usage

```java
TelemetryPort telemetry = container.getTelemetryPort();

// Counter (increment-only, zero-CPU)
var tcapTotal = telemetry.customCounter("ss7_tcap_total", "opcode", "begin");
tcapTotal.increment();

var mapAtsi = telemetry.customCounter("ss7_map_messages", "opcode", "atsi");
mapAtsi.increment(5);  // batch increment

// Gauge (sampled, zero-CPU — keep supplier trivial)
var staleDialogues = new AtomicLong();
telemetry.customGauge("ss7_stale_dialogues", staleDialogues::get,
    "host", appConfig.host());
```

### Prometheus output

```
ss7_tcap_total{opcode="begin"} 142
ss7_map_messages{opcode="atsi"} 892
ss7_stale_dialogues{host="HOST-A"} 3
```

### Dashboard

Custom metrics appear in the "App Metrics" card, with 📊 for counters  
and 📈 for gauges. Updates every 2 seconds automatically.

## Integration Guide

## API Reference

All endpoints served by the telemetry Vert.x router under `/api/telemetry/*`.

### GET /api/telemetry/snapshot

Full consolidated state for the dashboard GUI.

```json
{
  "sbbs": [{
    "sbbType": "HelloWorldSbb", "active": 42, "errors": 0,
    "spunks": 0, "eps": 1234.5, "p99us": 450
  }],
  "ras": [{
    "raName": "http-server-ra", "state": "ACTIVE",
    "port": "0.0.0.0:8080", "eventsFired": 10000,
    "commandsReceived": 5000, "sessionsActive": 42,
    "uptimeSeconds": 3600
  }],
  "resources": {
    "heapUsedMb": 128, "heapMaxMb": 512, "heapUsagePercent": 25.0,
    "cpuLoad": 0.15, "activeThreads": 8, "virtualThreads": 1042,
    "gcCount": 12, "gcTimeMs": 45, "openFileDescriptors": 256,
    "uptimeSeconds": 3600
  },
  "recentErrors": [...],
  "spunks": [...],
  "stales": [...],
  "activeAlarms": [...],
  "autoReconfigEnabled": true
}
```

### GET /api/telemetry/metrics

Prometheus OpenMetrics text format. Scrape this with Prometheus.

```
# HELP microjainslee_sbb_entities_active Active SBB entities
# TYPE microjainslee_sbb_entities_active gauge
microjainslee_sbb_entities_active{sbb_type="HelloWorldSbb"} 42
# HELP microjainslee_sbb_events_per_second Events per second
# TYPE microjainslee_sbb_events_per_second gauge
microjainslee_sbb_events_per_second{sbb_type="HelloWorldSbb"} 1234.5
...
```

### GET /api/telemetry/alarms

```json
{
  "active": [
    {"id": "a1", "level": "WARNING", "source": "ResourceMonitor",
     "message": "heap>85%, pool halved", "timestamp": 1718123400000}
  ],
  "activeCount": 1
}
```

### POST /api/telemetry/alarms/{id}/acknowledge

Acknowledge (clear) an alarm. Returns `204 No Content`.

### GET /api/telemetry/alarms/history?minutes=60

Alarm history for the specified time window.

### GET /api/telemetry/resources/history?minutes=60

Resource snapshot history (one entry per 30s).

### POST /api/telemetry/reconfig

```json
{"enabled": true}
```

Enable or disable the AutoReconfigEngine.

### GET /api/telemetry/health

```json
{
  "healthy": true,
  "checks": {
    "sbbCollector": "OK",
    "raCollector": "OK",
    "resourceMonitor": "OK",
    "errorCollector": "OK"
  }
}
```

---

## Export strategy — Prometheus + batched Log4j (and why not Elasticsearch)

Telemetry exports **two complementary ways at once**, chosen for a native
Quarkus/GraalVM target:

| Concern | Choice | Why |
|---------|--------|-----|
| **Live metrics** | **Prometheus** (Micrometer + exporter RA) | Pull-based, industry standard, zero custom wire format, no fat client, native-friendly. Already wired. |
| **Durable event log** | **Batched Log4j2 JSON sink** | Prometheus forgets everything on restart and is not an event log. The sink writes one compact JSON line per sample to `telemetry.log` via an **async** appender, flushed in **batches**. Zero new deps. |
| **Elasticsearch / Loki / Splunk** | **Via the log file + a sidecar** — *not* a direct client | An embedded ES client is heavyweight and reflection-heavy — hostile to GraalVM native images. Ship `telemetry.log` with Filebeat / Promtail / Vector instead: same destination, none of the weight. |

### The batched Log4j sink

`TelemetryLogSink` (app template) runs one daemon virtual thread. Every
`sampleInterval` it reduces the full snapshot to a compact operational summary
(heap %, CPU, active SBBs, EPS, errors, spunks, leaks, alarms — **no** stack
traces or per-entity detail) and buffers it. The buffer is flushed as a single
Log4j event once it reaches `batchSize` **or** `maxBatchAge` elapses — whichever
comes first:

```java
new TelemetryLogSink(telemetry,
        /* sampleIntervalMillis */ 10_000,
        /* batchSize */            30,
        /* maxBatchAgeMillis */    60_000).start();
```

`log4j2.xml` routes the dedicated `microjainslee.telemetry` logger to its own
async rolling file, never the console:

```xml
<Logger name="microjainslee.telemetry" level="INFO" additivity="false">
    <AppenderRef ref="telemetryAsync"/>   <!-- Async → RollingFile telemetry.log -->
</Logger>
```

```json
// one line in telemetry.log
{"ts":1751800000000,"heapUsedMb":128,"heapPct":25.0,"cpu":0.15,"vThreads":1042,
 "sbbActive":42,"sbbErrors":0,"eps":1234.5,"spunks":0,"staleLeaks":0,"alarms":0,
 "autoReconfig":true,"sbbs":[{"type":"HelloWorldSbb","active":42,"eps":1234.5}]}
```

---

## Prometheus + Grafana Integration

### Prometheus Scrape Config

```yaml
scrape_configs:
  - job_name: 'microjainslee'
    metrics_path: '/api/telemetry/metrics'
    static_configs:
      - targets: ['localhost:8080']
    scrape_interval: 15s
```

### Sample Grafana Dashboard Panels


| Panel         | Metric                                          | Visualization       |
| ------------- | ----------------------------------------------- | ------------------- |
| Active SBBs   | `microjainslee_sbb_entities_active`             | Stat (large number) |
| Events/sec    | `microjainslee_sbb_events_per_second`           | Time series (line)  |
| Latency p99   | `microjainslee_sbb_latency_p99_us`              | Time series (area)  |
| Heap Usage    | `microjainslee_resource_heap_usage_pct`         | Gauge (semi-circle) |
| CPU Load      | `microjainslee_resource_cpu_load`               | Time series         |
| Error Rate    | `rate(microjainslee_sbb_errors_total[1m])`      | Time series (red)   |
| RA Events     | `rate(microjainslee_ra_events_fired_total[1m])` | Time series         |
| Active Alarms | `microjainslee_alarms_active`                   | Table               |


---

## Configuration Reference

### application.properties

```properties
# Telemetry
microjainslee.telemetry.enabled=true
microjainslee.telemetry.resource-monitor-interval=30s
microjainslee.telemetry.stale-detector-interval=60s
microjainslee.telemetry.auto-reconfig.enabled=true
microjainslee.telemetry.auto-reconfig.interval=30s

# Thresholds
microjainslee.telemetry.memory.warning-threshold=85
microjainslee.telemetry.memory.critical-threshold=95
microjainslee.telemetry.cpu.warning-threshold=80
microjainslee.telemetry.load.spike-multiplier=3.0
microjainslee.telemetry.error.storm-threshold=100

# Cooldowns
microjainslee.telemetry.cooldown.memory=5m
microjainslee.telemetry.cooldown.cpu=10m
microjainslee.telemetry.cooldown.load=5m
microjainslee.telemetry.cooldown.error=15m

# Stale thresholds
microjainslee.telemetry.stale.warning=5m
microjainslee.telemetry.stale.leak=30m

# Spunk thresholds
microjainslee.telemetry.spunk.blocking-threshold-ms=100
microjainslee.telemetry.spunk.memory-spike-mb=100
```

---

## Module Structure

<p align="center"><img src="../images/jainslee-telemetry-module-tree.svg" width="700"/></p>

