# jainslee-telemetry — Công Cụ Tự Phục Hồi Không Tốn CPU

> **Module:** `jainslee-telemetry`
>
> **Thay thế:** JAIN SLEE 1.1 AlarmFacility, UsageFacility, TraceFacility
>
> **Triết lý:** Thu thập thụ động, không polling, bộ đếm AtomicLong, một daemon VT scheduler duy nhất

---

## Tổng Quan

Module `jainslee-telemetry` cung cấp một lớp quan sát (observability) hiện đại, không tốn chi phí

cho micro-jainslee. Thay vì sử dụng JMX MBeans nặng nề, cảnh báo dựa trên JMS, và

theo dõi usage dựa trên polling theo yêu cầu của JAIN SLEE 1.1, module này sử dụng:

- **Bộ đếm AtomicLong** — tích lũy metric không khóa, không tranh chấp
- **Ring buffers** — lịch sử lỗi và cảnh báo có giới hạn, không khóa
- **Một Virtual Thread daemon duy nhất** — một VT lên lịch tất cả các lần quét định kỳ (tài nguyên,

  phát hiện stale, đánh giá auto-reconfig) mỗi 30 giây
- **Micrometer + Prometheus** — xuất metric theo chuẩn công nghiệp, không có định dạng truyền tải tùy chỉnh
- **Callback thụ động từ EventRouter** — không polling, không can thiệp, chỉ một

  dòng `.record()` sau mỗi lần dispatch sự kiện

```
┌──────────────────────────────────────────────────────────────────┐
│  micro-jainslee                                                  │
│                                                                  │
│  ┌──────────────┐  ┌───────────────────────────────────────────┐ │
│  │ jainslee-api │  │ jainslee-telemetry                        │ │
│  │              │  │                                            ││
│  │TelemetryPort │◄─┤ MicrometerTelemetryPort                    ││
│  │ (interface)  │  │   ├─ SbbCollector       (AtomicLong)       ││
│  │              │  │   ├─ RaCollector        (AtomicLong)       ││
│  │              │  │   ├─ ErrorCollector     (RingBuffer 1000)  ││
│  │              │  │   ├─ ResourceMonitor    (Daemon VT, 30s)   ││
│  │              │  │   ├─ SpunkDetector      (callback onEvent) ││
│  │              │  │   ├─ StaleDetector      (heartbeat + quét) ││
│  │  ┌──────────────┐  │   ├─ AlarmEngine        (RingBuffer 500)   ││
│  │  │jainslee-core │  │   ├─ AutoReconfigEngine (đánh giá 30s) ⚡  ││
│  │  │              │  │   └─ PrometheusExporter (OpenMetrics)      ││
│  │  │  Container ◄─┤  │                                            ││
│  │  │  EventRouter◄┼──┤ onEventProcessed() → SbbCollector.record() ││
│  │  │  SbbPool    │  │ onError()          → ErrorCollector.record()││
│  │  └──────────────┘  └───────────────────────────────────────────┘ │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │  jainslee-telemetry-vertx (GUI — module riêng)               ││
│  │  GET /telemetry/        → index.html (steampunk dashboard)   ││
│  │  GET /api/telemetry/*   → JSON endpoints                     ││
│  └──────────────────────────────────────────────────────────────┘│
└──────────────────────────────────────────────────────────────────┘
```

---

## Kiến Trúc

### TelemetryPort API (`jainslee-api`)

Hợp đồng công khai nằm trong `jainslee-api` dưới dạng một interface duy nhất:

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
    String scrape();               // Định dạng OpenMetrics text
    TelemetrySnapshot snapshot();  // tổng hợp cho GUI
}
```

### MicrometerTelemetryPort (`jainslee-telemetry`)

Implementation sản xuất bao bọc một `PrometheusMeterRegistry` và kết nối tất cả

các collector lại với nhau trong một constructor duy nhất.

---

## Các Bộ Thu Thập (Collectors)

### 1. SbbCollector

Theo dõi mọi vòng đời của SBB entity và quá trình xử lý sự kiện. Được gọi **thụ động** bởi

EventRouter sau mỗi lần dispatch — không polling, không có chi phí can thiệp.

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


| Metric (Prometheus)                   | Loại    | Mô tả                         |
| ------------------------------------- | ------- | ----------------------------- |
| `microjainslee_sbb_entities_total`    | Gauge   | Tổng số entity đã tạo         |
| `microjainslee_sbb_entities_active`   | Gauge   | Entity hiện đang hoạt động    |
| `microjainslee_sbb_events_total`      | Counter | Sự kiện đã xử lý              |
| `microjainslee_sbb_events_per_second` | Gauge   | Thông lượng                   |
| `microjainslee_sbb_latency_avg_us`    | Gauge   | Độ trễ dispatch trung bình    |
| `microjainslee_sbb_latency_p99_us`    | Gauge   | Độ trễ phân vị thứ 99         |
| `microjainslee_sbb_errors_total`      | Counter | Tổng số lỗi                   |


### 2. RaCollector

Giám sát mọi Resource Adaptor: trạng thái, liên kết cổng, thông lượng sự kiện.

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


| Metric                                     | Loại                         | Mô tả                   |
| ------------------------------------------ | ---------------------------- | ----------------------- |
| `microjainslee_ra_state`                   | Gauge (1=ACTIVE, 0=INACTIVE) | Trạng thái từng RA      |
| `microjainslee_ra_events_fired_total`      | Counter                      | Sự kiện được RA bắn ra  |
| `microjainslee_ra_commands_received_total` | Counter                      | Lệnh gửi đến RA         |


### 3. ErrorCollector

Ring buffer không khóa chứa 1000 lỗi gần nhất. Không khóa — con trỏ ghi AtomicLong.

Kích thước cố định 1000 mục, `writeIndex` quay vòng với `& (SIZE - 1)` (lũy thừa của hai).

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

Thu thập trạng thái tài nguyên JVM qua một Virtual Thread daemon duy nhất.

```java
public final class ResourceMonitor {
    record ResourceSnapshot(
        long heapUsedMb, long heapMaxMb, double heapUsagePercent,
        double cpuLoad, int activeThreads, int virtualThreads,
        long gcCount, long gcTimeMs, long openFileDescriptors,
        long uptimeSeconds
    ) {}

    ResourceSnapshot snapshot();
    Stream<ResourceSnapshot> history();  // 60 phút gần nhất (120 mẫu)
    void start(long interval, TimeUnit unit);
    void stop();
}
```


| Metric                                   | Loại    | Mô tả                           |
| ---------------------------------------- | ------- | ------------------------------- |
| `microjainslee_resource_heap_used_mb`    | Gauge   | Heap đã dùng (MB)               |
| `microjainslee_resource_heap_max_mb`     | Gauge   | Heap tối đa (MB)                |
| `microjainslee_resource_heap_usage_pct`  | Gauge   | % Heap đã sử dụng               |
| `microjainslee_resource_cpu_load`        | Gauge   | Tải CPU tiến trình (0.0–1.0)    |
| `microjainslee_resource_threads_active`  | Gauge   | Platform thread đang hoạt động  |
| `microjainslee_resource_threads_virtual` | Gauge   | Virtual thread đang hoạt động   |
| `microjainslee_resource_gc_count`        | Counter | Số lần GC                       |
| `microjainslee_resource_gc_time_ms`      | Counter | Thời gian tạm dừng GC (ms)      |


### 5. SpunkDetector

Phát hiện hành vi SBB bất thường ("spunk") — các SBB hoạt động sai hoặc

ngốn tài nguyên.

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


| Điều kiện Spunk       | Ngưỡng                                    | Mức độ   |
| --------------------- | ----------------------------------------- | -------- |
| Event loop bị chặn    | `latency > 100ms`                         | WARNING  |
| Tăng đột biến bộ nhớ  | `memDelta > 100MB` trong một entity       | WARNING  |
| Ngốn CPU              | Một loại SBB &gt; 50% tổng CPU            | CRITICAL |
| Bùng nổ entity        | &gt; 1000 entity con được tạo trong 1 phút | WARNING  |


### 6. StaleDetector

Xác định các entity đã không nhận sự kiện — hoặc là không hoạt động (cảnh báo) hoặc

bị rò rỉ (nghiêm trọng, yêu cầu force-release).

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


| Điều kiện              | Ngưỡng                          | Hành động                            |
| ---------------------- | ------------------------------- | ------------------------------------ |
| Entity không hoạt động | Không có sự kiện &gt; 5 phút    | Cảnh báo `AlarmLevel.INFO`           |
| Entity bị rò rỉ        | Không có sự kiện &gt; 30 phút   | `AlarmLevel.CRITICAL` + auto-release |


### 7. AlarmEngine

Thay thế JAIN SLEE 1.1 `AlarmFacility`. Ring buffer chứa 500 cảnh báo.

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

**Vòng đời cảnh báo:**

```
fire() → ACTIVE → acknowledge() → lưu trữ trong history ring buffer
                                    (giữ lại trong 60 phút)
```

---

## ⚡ Auto-Reconfig Engine

AutoReconfigEngine tự động điều chỉnh cấu hình JAIN SLEE dựa trên

các metric thời gian thực. **Không cần can thiệp thủ công.**

### Chu Kỳ Đánh Giá

Một VT daemon duy nhất đánh giá mỗi 30 giây:

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

### Các Điều Kiện & Hành Động Reconfig


| #   | Điều kiện               | Ngưỡng                                    | Hành động                            | Mức báo động | Thời gian hồi      |
| --- | ----------------------- | ----------------------------------------- | ------------------------------------ | ------------ | ------------------ |
| 1   | Bộ nhớ cao              | Heap &gt; 85%                             | Giảm một nửa SBB pool max            | WARNING      | 5 phút             |
| 2   | Bộ nhớ nghiêm trọng     | Heap &gt; 95%                             | Giải phóng entity stale + System.gc() | CRITICAL     | 2 phút             |
| 3   | Áp lực CPU              | CPU &gt; 80% duy trì 2 chu kỳ             | Giảm 25% luồng event-loop của RA     | WARNING      | 10 phút            |
| 4   | CPU đã phục hồi         | CPU &lt; 50% duy trì 3 chu kỳ             | Khôi phục luồng RA về ban đầu        | INFO         | —                  |
| 5   | Tăng đột biến tải       | EPS &gt; 3× baseline cho mỗi loại SBB     | Mở rộng SBB pool × 2                 | INFO         | 5 phút             |
| 6   | Tải trở lại bình thường | EPS &lt; 1.5× baseline duy trì 5 chu kỳ   | Thu hẹp pool về bình thường          | INFO         | —                  |
| 7   | Bão lỗi                 | &gt; 100 lỗi/phút cho một loại SBB        | Tạm ngưng loại SBB đó                | CRITICAL     | 15 phút            |
| 8   | Bão lỗi đã tan          | 0 lỗi cho SBB bị tạm ngưng trong 5 phút   | Tiếp tục loại SBB đó                 | INFO         | —                  |
| 9   | Rò rỉ entity            | Không hoạt động &gt; 30 phút              | Force-release entity                 | CRITICAL     | Không (theo entity) |
| 10  | RA bị sập               | Trạng thái RA = ERROR                     | Khởi động lại RA                     | CRITICAL     | 2 phút             |


### Container API cho Reconfig

Engine gọi ngược vào `MicroSleeContainer`:

```java
// MicroSleeContainer — các phương thức được lộ ra cho auto-reconfig
void reduceSbbPoolMax(int newMax);
void expandSbbPool(int newMax);
void suspendSbbType(String sbbType);
void resumeSbbType(String sbbType);
void restartRa(String raName);
void releaseEntity(String entityId);
```

### Hành Vi Cooldown

Mỗi điều kiện có một khoảng thời gian cooldown để ngăn dao động. Trong thời gian cooldown,

cùng điều kiện đó sẽ bị bỏ qua ngay cả khi ngưỡng vẫn bị vi phạm. Cooldown

được theo dõi theo từng điều kiện với một `Map<Condition, Long>` chứa timestamp lần kích hoạt cuối.

---

## Hướng Dẫn Tích Hợp

### Bước 1: Thêm Dependency

```xml
[[ORCA_RAW_HTML_BLOCK:%3Cdependency%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CgroupId%3E]]com.microjainslee[[ORCA_RAW_HTML_INLINE:%3C%2FgroupId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CartifactId%3E]]jainslee-telemetry[[ORCA_RAW_HTML_INLINE:%3C%2FartifactId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3Cversion%3E]]${microjainslee.version}[[ORCA_RAW_HTML_INLINE:%3C%2Fversion%3E]]
[[ORCA_RAW_HTML_BLOCK:%3C%2Fdependency%3E]]
[[ORCA_RAW_HTML_BLOCK:%3Cdependency%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CgroupId%3E]]com.microjainslee[[ORCA_RAW_HTML_INLINE:%3C%2FgroupId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3CartifactId%3E]]jainslee-telemetry-vertx[[ORCA_RAW_HTML_INLINE:%3C%2FartifactId%3E]]
    [[ORCA_RAW_HTML_INLINE:%3Cversion%3E]]${microjainslee.version}[[ORCA_RAW_HTML_INLINE:%3C%2Fversion%3E]]
[[ORCA_RAW_HTML_BLOCK:%3C%2Fdependency%3E]]
```

### Bước 2: Kết Nối Trong Bootstrap

```java
@PostConstruct
void init() {
    container.start();

    // 1. Tạo telemetry engine
    var registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    var telemetry = new MicrometerTelemetryPort(registry, container);
    container.bindTelemetryPort(telemetry);

    // 2. Khởi động các collector
    telemetry.sbbCollector().start();
    telemetry.raCollector().start();
    telemetry.resourceMonitor().start(30, TimeUnit.SECONDS);
    telemetry.spunkDetector().start();
    telemetry.staleDetector().start(60, TimeUnit.SECONDS);

    // 3. Khởi động auto-reconfig (tùy chọn nhưng khuyến nghị)
    telemetry.autoReconfig().start(30, TimeUnit.SECONDS);
    telemetry.setAutoReconfigEnabled(true);

    // 4. Kết nối EventRouter để cấp dữ liệu cho telemetry
    container.getEventRouter().setTelemetryPort(telemetry);

    // 5. Gắn các route cho dashboard
    var router = Router.router(vertx);
    router.route("/telemetry/*")
        .handler(StaticHandler.create("webroot/telemetry"));
    router.get("/api/telemetry/snapshot")
        .handler(ctx -> ctx.json(telemetry.snapshot()));
    router.get("/api/telemetry/metrics")
        .handler(ctx -> ctx.end(telemetry.scrape()));
}
```

### Bước 3: Các Điểm Tích Hợp EventRouter

EventRouter gọi vào telemetry tại hai điểm — sau mỗi lần dispatch thành công

và khi có lỗi:

```java
// Trong EventRouter.dispatch():
long start = System.nanoTime();
long memBefore = Runtime.getRuntime().totalMemory() -
                 Runtime.getRuntime().freeMemory();

try {
    entity.submit(() -> sbb.onEvent(event, aci));
} catch (Throwable t) {
    if (telemetryPort != null)
        telemetryPort.errorCollector().record(sbbType, entityId, t);
    throw t;
} finally {
    if (telemetryPort != null) {
        long latencyNs = System.nanoTime() - start;
        long memAfter = Runtime.getRuntime().totalMemory() -
                        Runtime.getRuntime().freeMemory();
        telemetryPort.sbbCollector()
            .onEventProcessed(sbbType, entityId, latencyNs,
                              memAfter - memBefore);
        telemetryPort.spunkDetector()
            .onEventProcessed(sbbType, entityId, latencyNs,
                              memAfter - memBefore);
        telemetryPort.staleDetector()
            .trackHeartbeat(entityId, sbbType);
    }
}
```

### Bước 4: Xác Minh

```bash
curl http://localhost:8080/api/telemetry/metrics
curl http://localhost:8080/api/telemetry/snapshot | jq .
open http://localhost:8080/telemetry/
```

---

## Custom Metrics Do Ứng Dụng Định Nghĩa (Có Thể Mở Rộng)

Mỗi miền ứng dụng có thể đăng ký các counter và gauge riêng tại runtime.  
Chúng tự động xuất hiện trong `snapshot().customMetrics`, Prometheus scrape,  
và dashboard GUI — **không cần thêm bất kỳ kết nối nào**.

### Cách Sử Dụng

```java
TelemetryPort telemetry = container.getTelemetryPort();

// Counter (chỉ tăng, không tốn CPU)
var tcapTotal = telemetry.customCounter("ss7_tcap_total", "opcode", "begin");
tcapTotal.increment();

var mapAtsi = telemetry.customCounter("ss7_map_messages", "opcode", "atsi");
mapAtsi.increment(5);  // tăng theo batch

// Gauge (lấy mẫu, không tốn CPU — giữ supplier đơn giản)
var staleDialogues = new AtomicLong();
telemetry.customGauge("ss7_stale_dialogues", staleDialogues::get,
    "host", appConfig.host());
```

### Đầu ra Prometheus

```
ss7_tcap_total{opcode="begin"} 142
ss7_map_messages{opcode="atsi"} 892
ss7_stale_dialogues{host="HOST-A"} 3
```

### Dashboard

Custom metrics xuất hiện trong thẻ "App Metrics", với 📊 cho counters  
và 📈 cho gauges. Tự động cập nhật mỗi 2 giây.

## Hướng Dẫn Tích Hợp

## Tham Khảo API

Tất cả các endpoint được phục vụ bởi telemetry Vert.x router dưới `/api/telemetry/*`.

### GET /api/telemetry/snapshot

Trạng thái tổng hợp đầy đủ cho dashboard GUI.

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

Định dạng Prometheus OpenMetrics text. Scrape endpoint này với Prometheus.

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

Xác nhận (xóa) một cảnh báo. Trả về `204 No Content`.

### GET /api/telemetry/alarms/history?minutes=60

Lịch sử cảnh báo trong khoảng thời gian chỉ định.

### GET /api/telemetry/resources/history?minutes=60

Lịch sử snapshot tài nguyên (một mục mỗi 30 giây).

### POST /api/telemetry/reconfig

```json
{"enabled": true}
```

Bật hoặc tắt AutoReconfigEngine.

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

## Tích Hợp Prometheus + Grafana

### Cấu Hình Prometheus Scrape

```yaml
scrape_configs:
  - job_name: 'microjainslee'
    metrics_path: '/api/telemetry/metrics'
    static_configs:
      - targets: ['localhost:8080']
    scrape_interval: 15s
```

### Các Panel Mẫu Cho Grafana Dashboard


| Panel                  | Metric                                          | Trực quan hóa        |
| ---------------------- | ----------------------------------------------- | -------------------- |
| SBB đang hoạt động     | `microjainslee_sbb_entities_active`             | Stat (số lớn)        |
| Sự kiện/giây           | `microjainslee_sbb_events_per_second`           | Time series (đường)  |
| Độ trễ p99             | `microjainslee_sbb_latency_p99_us`              | Time series (vùng)   |
| Sử dụng Heap           | `microjainslee_resource_heap_usage_pct`         | Gauge (bán nguyệt)   |
| Tải CPU                | `microjainslee_resource_cpu_load`               | Time series          |
| Tỉ lệ lỗi              | `rate(microjainslee_sbb_errors_total[1m])`      | Time series (đỏ)     |
| Sự kiện RA             | `rate(microjainslee_ra_events_fired_total[1m])` | Time series          |
| Cảnh báo đang hoạt động| `microjainslee_alarms_active`                   | Bảng                 |


---

## Tham Khảo Cấu Hình

### application.properties

```properties
# Telemetry
microjainslee.telemetry.enabled=true
microjainslee.telemetry.resource-monitor-interval=30s
microjainslee.telemetry.stale-detector-interval=60s
microjainslee.telemetry.auto-reconfig.enabled=true
microjainslee.telemetry.auto-reconfig.interval=30s

# Ngưỡng
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

# Ngưỡng stale
microjainslee.telemetry.stale.warning=5m
microjainslee.telemetry.stale.leak=30m

# Ngưỡng spunk
microjainslee.telemetry.spunk.blocking-threshold-ms=100
microjainslee.telemetry.spunk.memory-spike-mb=100
```

---

## Cấu Trúc Module

```
micro-jainslee/
├── jainslee-api/
│   └── org/microjainslee/api/telemetry/
│       └── TelemetryPort.java              ← interface công khai
├── jainslee-telemetry/                     ← MODULE MỚI
│   ├── pom.xml                             ← deps: micrometer, prometheus
│   └── org/microjainslee/telemetry/
│       ├── MicrometerTelemetryPort.java
│       ├── SbbCollector.java
│       ├── RaCollector.java
│       ├── ErrorCollector.java
│       ├── ResourceMonitor.java
│       ├── SpunkDetector.java
│       ├── StaleDetector.java
│       ├── AlarmEngine.java
│       ├── AutoReconfigEngine.java
│       └── PrometheusExporter.java
├── jainslee-telemetry-vertx/               ← MODULE MỚI (GUI)
│   ├── pom.xml                             ← deps: vertx-web
│   └── src/main/resources/webroot/telemetry/
│       ├── index.html                      ← steampunk dashboard
│       └── telemetry.js                    ← vòng lặp fetch + rendering
├── jainslee-core/
│   ├── MicroSleeContainer.java             ← + bindTelemetryPort(), API reconfig
│   ├── EventRouter.java                    ← + setTelemetryPort(), hook onEvent
│   └── VirtualThreadSbbEntityPool.java     ← + notifyTelemetry()
└── example/
    ├── example-quarkus-helloworld-web/     ← đã kết nối với telemetry
    └── example-spring-helloworld-web/      ← đã kết nối với telemetry
```

