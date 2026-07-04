# 🚀 Micro-JAINSLEE Junior Developer Guide

> **Tài liệu onboarding cho developer mới làm việc với micro-jainslee**
>
> Last updated: 2026-07-04 | Maintainer: nhanth87
> Nguồn dữ liệu: Supermemory API (sm_project_default) + codebase analysis

---

## Mục lục

1. [Tổng quan dự án](#1-tổng-quan-dự-án)
2. [Hai stack JAIN-SLEE](#2-hai-stack-jain-slee)
3. [Kiến trúc micro-jainslee](#3-kiến-trúc-micro-jainslee)
4. [PolyVoice & 3-Port Contract Pattern](#4-polyvoice--3-port-contract-pattern)
5. [Cài đặt & Build](#5-cài-đặt--build)
6. [Viết SBB đầu tiên](#6-viết-sbb-đầu-tiên)
7. [Viết Resource Adaptor](#7-viết-resource-adaptor)
8. [Event Flow & EventRouter](#8-event-flow--eventrouter)
9. [Timer & Scheduler](#9-timer--scheduler)
10. [Spring Boot Integration](#10-spring-boot-integration)
11. [Các ràng buộc quan trọng](#11-các-ràng-buộc-quan-trọng)
12. [Testing](#12-testing)
13. [FAQ & Troubleshooting](#13-faq--troubleshooting)

---

## 1. Tổng quan dự án

**micro-jainslee** là một **JAIN SLEE 1.1 runtime nhẹ, có thể nhúng (embeddable)**, được thiết kế cho mục đích **R&D**. Đây không phải là production container — production dùng **RestComm JAIN-SLEE v8** trên WildFly 10.

### Đặc điểm chính

| Đặc điểm | Chi tiết |
|----------|----------|
| **Mục đích** | R&D, prototyping, testing |
| **Java** | Java 25 (đã migrate từ Java 8) |
| **Virtual Threads** | ✅ Mỗi SBB entity = 1 parked virtual thread |
| **Concurrency** | 100K SBB entities ~14 OS threads |
| **Test coverage** | 62+ tests passing |
| **Build** | Maven multi-module (20 modules) |
| **Milestone** | PRODUCTION-1 (đã đạt) |

### Module structure

```
jain-slee/
├── pom.xml                    # Root POM (restcomm-slee-core v8)
├── api/                       # JAIN SLEE API specs
│   ├── descriptors/           # Deployment descriptors
│   ├── extensions/            # SLEE extensions
│   └── jar/                   # API JAR
├── container/                 # SLEE Container (production)
│   ├── activities/            # Activity context management
│   ├── common/                # Shared utilities
│   ├── components/            # Container components
│   ├── events/                # Event processing
│   ├── profiles/              # Profile management
│   ├── resource/              # RA management
│   ├── router/                # EventRouter (LMAX Disruptor)
│   ├── services/              # Service management
│   ├── spi/                   # Container SPI
│   ├── timers/                # FaultTolerantScheduler
│   └── transaction/           # Transaction management
├── jainslee-core/             # micro-jainslee core runtime
├── jainslee-api/              # micro-jainslee API
├── jainslee-scheduler/        # Slim TimerScheduler (jSS7-based)
├── jainslee-adapter/          # Framework adapters
│   ├── adapter-springboot/    # Spring Boot starter
│   ├── adapter-quarkus/       # Quarkus adapter
│   └── adapter-jakartaee/     # Jakarta EE adapter
├── vendor-ras/                # Vendor Resource Adaptors
│   ├── ra-grpc-client/        # gRPC client RA
│   └── ra-http-ingress/       # HTTP ingress RA
├── example/                   # Ví dụ mẫu
│   ├── example-embedded-j25/  # Embedded Java 25 example
│   ├── example-spring/        # Spring Boot example
│   ├── example-quarkus/       # Quarkus example
│   ├── grpc-simulator/        # gRPC simulator
│   └── ussdgw-simulator/      # USSD GW simulator
└── release/                   # Release packaging
```

### Non-Goals (micro-jainslee không làm)

- ❌ TCK compliance
- ❌ Cluster HA
- ❌ JSR-77 MBean support
- ❌ Production deployment

---

## 2. Hai stack JAIN-SLEE

Dự án duy trì **hai stack JAIN-SLEE riêng biệt**:

| | micro-jainslee (R&D only) | RestComm JAIN-SLEE v8 (Production) |
|---|---|---|
| **Java** | Java 25 + Virtual Threads | Java 11+ |
| **Runtime** | Embedded / Spring Boot | WildFly 10 container |
| **SBB Pool** | VirtualThreadSbbEntityPool | Apache Commons Pool |
| **EventRouter** | In-Memory | LMAX Disruptor (262K ring) |
| **Timer** | jSS7 HashedWheelTimer | FaultTolerantScheduler |
| **ActivityContext** | In-Memory | Distributed (Infinispan) |
| **Cluster** | ❌ No | ✅ Full HA |
| **Status** | PRODUCTION-1 | Production-grade |

> ⚠️ **Production Constraint:** micro-jainslee **Tuyệt đối không** được đóng gói vào production.

---

## 3. Kiến trúc micro-jainslee

### Core Infrastructure Classes

| Class | Vai trò |
|-------|---------|
| **MicroSleeContainer** | Bootstrap container, quản lý vòng đời |
| **EventRouter** | Điều phối event từ RA → SBB, đảm bảo ordering |
| **VirtualThreadSbbEntityPool** | Pool SBB dùng virtual thread |
| **SleeTimerSchedulerBridge** | Cầu nối jSS7 HashedWheelTimer → EventRouter |
| **SbbIndexLoader** | Quét classpath tìm SBB annotated class |
| **ServiceRegistry** | Đăng ký và tra cứu SBB services |
| **SbbTransactionContext** | Context giao dịch cho SBB execution |
| **DefaultInitialEventSelector** | Chọn SBB phù hợp cho initial event |
| **DefaultErrorHandlingPolicy** | Policy xử lý lỗi mặc định |

### Spring Boot Starter Beans

Module `jainslee-adapter/adapter-springboot` tự động đăng ký:
`MicroSleeConfiguration`, `MicroSleeContainer`, `InMemoryActivityContextNamingFacility`,

## 4. PolyVoice & 3-Port Contract Pattern (GOAL 1-5 ✅)

### PolyVoice Pattern

**PolyVoice** là pattern kiến trúc cho phép một SBB xử lý **nhiều loại voice/session** (USSD, SIP, gRPC) thông qua cùng một service logic. Pattern này tận dụng **Resource Adaptor abstraction** để tách biệt protocol khỏi business logic.

```
PolyVoice SBB:
  Input Ports      │  Output Ports      │  Internal Ports
  ─────────────────┼────────────────────┼───────────────────
  USSD RA Port    │  USSD RA Port     │  TimerPort
  (MAP/USSD events)│  (sendCommand)    │  ActivityContextInterface
  SIP RA Port     │  SIP RA Port      │
  gRPC RA Port    │  gRPC RA Port     │
```

### 3-Port Contract (GOAL 1-5 API)

Mỗi SBB trong micro-jainslee tuân theo **3-Port Contract**. GOAL 1-5 đã hoàn thiện các interface chuẩn:

| Port | Interface | Mục đích (Purpose) |
|------|-----------|---------------------|
| **Port 1** | **Event Handler** `onXxxEvent(SleeEvent, ACI)` | Nhận event từ EventRouter |
| **Port 2** | **`RaCommandPort`** `sendCommand(OutboundCommand)` | Gửi command ra RA (thay thế abstract RA accessor cũ) |
| **Port 3** | **SLEE Facilities** `TimerPort`, `ACI`, `SbbLocalObject` | SLEE facilities |

### New API interfaces (GOAL 1-5)

| Interface | Vai trò | Package |
|-----------|---------|---------|
| **`RaEndpointPort`** | RA lifecycle: `activate(RaBootstrapPort)`, `deactivate()`, `getRaName()` | `com.microjainslee.api` |
| **`RaCommandPort`** | SBB → RA: `sendCommand(OutboundCommand)` | `com.microjainslee.api` |
| **`RaBootstrapPort`** | Container → RA: `createActivityHandle()`, `fireEvent()` | `com.microjainslee.api` |
| **`OutboundCommand`** | Marker interface cho command gửi từ SBB → RA | `com.microjainslee.api` |
| **`@InjectRa`** | Annotation inject `RaCommandPort` vào SBB field | `com.microjainslee.api.annotations` |

### Ví dụ PolyVoice 3-Port SBB (GOAL 1-5 style)

```java
@Sbb(id = "PolyVoiceSBB", service = "PolyVoiceService")
public class PolyVoiceSbb implements Sbb {

    // === PORT 2: @InjectRa thay thế abstract getUssdRa() cũ ===
    @InjectRa(name = "ussd-gateway")
    private RaCommandPort ussdRa;

    @InjectRa(name = "sip-gateway")
    private RaCommandPort sipRa;

    // === PORT 1: Event Handlers ===
    public void onUssdRequest(UssdRequestEvent event, ActivityContextInterface aci) {
        String response = processVoiceRequest(
            aci.getActivity().toString(), event.getMsisdn(),
            event.getUssdString(), "USSD");

        // Gửi command qua RaCommandPort (thay vì getUssdRa().sendUssdResponse())
        ussdRa.sendCommand(new SendUssdResponseCommand(sessionId, response));
    }

    public void onSipInvite(SipInviteEvent event, ActivityContextInterface aci) {
        sipRa.sendCommand(new StartCallCommand(event.getCaller(), event.getCallee()));
    }

    // === PORT 3: SLEE Facilities ===
    public abstract TimerFacility getTimerFacility();

    public void onTimer(TimerEvent event, ActivityContextInterface aci) {
        handleTimeout(event.getTimerID(), aci);
    }

    // Shared business logic (giữ nguyên)
    private String processVoiceRequest(String sid, String msisdn,
                                        String input, String proto) {
        return "Welcome to PolyVoice [" + proto + "]";
    }
}
```

### Cách container wire RA (GOAL 1-5 style) — registerRa + mapEventToSbb

```java
// Trong bootstrap code (main hoặc CDI @Startup)
MicroSleeContainer container = MicroSleeContainer.create(config);

// 1. Tạo RA — cùng class implement cả RaEndpointPort và RaCommandPort
UssdGatewayRa ussdRa = new UssdGatewayRa();

// 2. Register RA qua 3-port contract
container.registerRa(ussdRa, ussdRa);          // (endpoint, command)

// 3. Map event type → SBB để convergent routing
container.mapEventToSbb(UssdBeginEvent.class, "UssdSessionSbb");

// 4. Container tự gọi ussdRa.activate(bootstrap) khi start()
container.start();
```

### Một RA implement cả 3 port (RaEndpointPort + RaCommandPort)

```java
// RA implement cả RaEndpointPort (lifecycle) và RaCommandPort (nhận command từ SBB)
public class UssdGatewayRa implements RaEndpointPort, RaCommandPort {

    private RaBootstrapPort bootstrap;

    // ── RaEndpointPort ──
    @Override public String getRaName() { return "ussd-gateway"; }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrap = bootstrap;
        startSs7Stack();   // Mở SS7 connection, bắt đầu lắng nghe MAP dialog
    }

    @Override
    public void deactivate() {
        stopSs7Stack();
        this.bootstrap = null;
    }

    // ── RaCommandPort ──
    @Override
    public void sendCommand(OutboundCommand command) {
        switch (command) {
            case SendUssdResponseCommand c ->
                sendUssdOverMap(c.sessionId(), c.ussdText());
            case StartCallCommand c ->
                initiateSipCall(c.caller(), c.callee());
            default -> log.warn("Unknown command: {}", command);
        }
    }

    // ── RA tự fire event vào SLEE khi có incoming message ──
    private void onIncomingUssd(MapDialog dialog, String msisdn, String ussdString) {
        ActivityHandle handle = bootstrap.createActivityHandle(dialog.getDialogId());
        SleeEvent event = new UssdBeginEvent(msisdn, ussdString, dialog.getDialogId());
        bootstrap.fireEvent(event, handle, new Address(msisdn));
    }
}

// Command types (implement OutboundCommand marker interface)
record SendUssdResponseCommand(String sessionId, String ussdText) implements OutboundCommand {}
record StartCallCommand(String caller, String callee) implements OutboundCommand {}
```

## 5. Cài đặt & Build

### Yêu cầu

| Thành phần | Minimum | Khuyến nghị |
|------------|---------|-------------|
| Java | JDK 25 | JDK 25 |
| Maven | 3.8+ | 3.9+ |
| RAM | 8GB | 16-64GB |

### Build

```bash
# Build toàn bộ (20 modules)
cd jain-slee/jain-slee
mvn clean install -DskipTests

# Build với tests
mvn clean verify
```

### JVM Options

```bash
JAVA_OPTS="--enable-preview -Xms4g -Xmx8g -XX:+UseZGC
  -XX:MaxGCPauseMillis=10
  -Djainslee.eventrouter.threads=8
  -Djainslee.eventrouter.ringsize=262144

---

## 6. Viết SBB đầu tiên

Một SBB (Service Building Block) trong micro-jainslee gồm: Abstract class với `@Sbb` annotation, event handler methods (Port 1), abstract RA accessors (Port 2), SLEE facility accessors (Port 3).

### Ví dụ: EchoSbb

```java
@Sbb(id = "EchoSbb", service = "EchoService",
     initialEventSelectors = { EchoInitialEventSelector.class })
public abstract class EchoSbb implements Sbb {

    public void sbbCreate() { /* Khởi tạo */ }
    public void sbbActivate() { /* Activate */ }
    public void sbbPassivate() { /* Passivate */ }
    public void sbbRemove() { /* Cleanup - tránh leak! */ }

    // PORT 1: Event Handler
    public void onEchoRequest(EchoRequestEvent event, ActivityContextInterface aci) {
        String response = "ECHO: " + event.getMessage();
        getEchoRa().sendResponse(aci.getActivity(), response);
    }

    // PORT 2: RA Accessor
    public abstract EchoResourceAdaptor getEchoRa();

    // PORT 3: SLEE Facilities
    public abstract TimerFacility getTimerFacility();

    public void onTimer(TimerEvent event, ActivityContextInterface aci) {
        getEchoRa().sendResponse(aci.getActivity(), "TIMEOUT");
    }
}
```

### Event Types

```java
@EventType(id = "EchoRequestEvent", vendor = "example.com", version = "1.0")
public class EchoRequestEvent implements FireableEventType {
    private final String message;
    public EchoRequestEvent(String message) { this.message = message; }
    public String getMessage() { return message; }
}
```

---

## 7. Viết Resource Adaptor

Resource Adaptor (RA) là cầu nối SLEE container ↔ thế giới bên ngoài.

```
External World ←→ RA Interface (SBB gọi) ←→ RA Impl (fireEvent) ←→ EventRouter → SBB
```

### Ví dụ: gRPC Resource Adaptor

```java
@ResourceAdaptor(id = "GrpcClientRA", vendor = "example.com", version = "1.0")
public class GrpcClientResourceAdaptor implements ResourceAdaptor {

    private ResourceAdaptorContext raContext;

    public void raActive() { initGrpcChannel(); }
    public void raStopping() { shutdownGrpcChannel(); }

    // Interface cho SBB gọi (Port 2)
    public void sendGrpcRequest(ActivityContextInterface aci, String request) {
        grpcStub.send(request, new StreamObserver<Response>() {
            @Override
            public void onNext(Response response) {
                // ⚠️ fireEvent từ RA thread, KHÔNG từ SBB/TimerCallback
                try {
                    aci.fireEvent(new GrpcResponseEvent(response.getData()));
                } catch (Exception e) {
                    raContext.getTracer().severe("Fire event failed", e);
                }
            }
        });
    }

    public void setResourceAdaptorContext(ResourceAdaptorContext ctx) {
        this.raContext = ctx;
    }
}
```

### Ràng buộc RA

| Rule | Giải thích |
|------|-----------|
| Fire event từ RA/SS7 thread | Không từ SBB qua TimerCallback |
| Không block IO trong SBB | Luôn qua RA |
| Thread safety | RA implementation phải thread-safe |
| Cleanup trong raStopping() | Giải phóng tất cả resources |

### Ví dụ: GOAL 1-5 RaEndpointPort RA (3-Port Contract)

Từ micro-jainslee 1.2.0, RA có thể implement **`RaEndpointPort`** (thay vì `ResourceAdaptor`), sử dụng **`RaBootstrapPort`** để fire event và tạo activity handle:

```java
import com.microjainslee.api.*;

public class HttpIngressRa implements RaEndpointPort, RaCommandPort {

    private RaBootstrapPort bootstrap;
    private HttpServer server;

    @Override
    public String getRaName() { return "http-ingress"; }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrap = bootstrap;
        // Mở HTTP server, bắt đầu nhận request
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/ussd", exchange -> {
            String sessionId = UUID.randomUUID().toString();
            // Tạo activity handle cho session này
            ActivityHandle handle = bootstrap.createActivityHandle(sessionId);
            // Fire event vào SLEE EventRouter
            bootstrap.fireEvent(
                new HttpUssdBeginEvent(sessionId, parseMsisdn(exchange)),
                handle,
                new Address(parseMsisdn(exchange))
            );
        });
        server.start();
    }

    @Override
    public void deactivate() {
        server.stop(0);
        this.bootstrap = null;
    }

    @Override
    public void sendCommand(OutboundCommand command) {
        // Xử lý outbound command từ SBB
    }
}
```

**So sánh old vs new RA pattern:**

| | Old (ResourceAdaptor) | New GOAL 1-5 (RaEndpointPort) |
|---|---|---|
| Interface | `javax.slee.resource.ResourceAdaptor` | `com.microjainslee.api.RaEndpointPort` |
| Fire event | `raContext.getSleeEndpoint().fireEvent(...)` | `bootstrap.fireEvent(event, handle, address)` |
| Activity handle | Custom ActivityHandle class | `bootstrap.createActivityHandle(id)` |
| Lifecycle | `raActive()` / `raInactive()` / 5 methods | `activate(bootstrap)` / `deactivate()` |
| SBB communication | Abstract `getXxxRa()` method | `RaCommandPort.sendCommand(OutboundCommand)` |
| Discovery | JNDI / @ResourceAdaptor annotation | `container.registerRa(endpoint, command)` |

---

## 8. Event Flow & EventRouter

```
External Event → ResourceAdaptor.fireEvent() → EventRouter (RingBuffer 262K)
  → N Workers (1/CPU core) → SBB Virtual Thread (parked→unparked)
```

### Production: LMAX Disruptor

| Tham số | Giá trị |
|---------|---------|
| Ring buffer | 262,144 slots |
| Workers | N = CPU cores |
| Throughput | 100K+ events/s |
| 99th latency | <5ms |

### micro-jainslee: In-Memory EventRouter

Đơn giản hơn, phù hợp R&D. Hỗ trợ session recovery với automatic snapshot capture khi entity bị remove (trừ SBB_SELF_REMOVE), rehydration trong EventRouter.

---

## 9. Timer & Scheduler

```
SBB.setTimer() → SleeTimerSchedulerBridge → jSS7 HashedWheelTimer (10ms tick)
  → EventRouter → SBB.onTimer()
```

> ⚠️ **Critical:** SleeTimerSchedulerBridge fire events đến EventRouter — SBB **không bao giờ** execute trực tiếp trên wheel thread.

### Sử dụng Timer

```java
public void onSomeEvent(SomeEvent event, ActivityContextInterface aci) {
    TimerID timerId = getTimerFacility().setTimer(aci, 5000, this, null);
    activeTimers.put(timerId, aci);
}

public void onTimer(TimerEvent event, ActivityContextInterface aci) {
    activeTimers.remove(event.getTimerID());
    handleTimeout(aci);
}
```

### Phân biệt Timer Systems

| Timer | Dùng cho | Scope |
|-------|----------|-------|
| jSS7 HashedWheelTimer | I/O dispatch | jSS7 internal |
| SleeTimerSchedulerBridge | SLEE app timers | micro-jainslee |
| FaultTolerantScheduler | SLEE app timers (HA) | Production only |
| USSD adaptive gate (EWMA) | USSD timeout | ussdgateway |

> Các hệ thống timer **orthogonal** — không thay thế lẫn nhau.

---

## 10. Spring Boot Integration

### Dependency

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>jainslee-adapter-springboot</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Cấu hình application.yml

```yaml
micro-jainslee:
  container:
    event-router:
      threads: 8
      ring-size: 262144
    timer:
      threads: 4
    sbb-pool:
      min: 100
      max: 5000
```

### Auto-configured Beans

`MicroSleeConfiguration`, `MicroSleeContainer`, `InMemoryActivityContextNamingFacility`,
`EventRouter`, `TimerPort`, `MicroJainsleeLifecycle`.

### Khởi động

```java
@SpringBootApplication
public class UssdApplication {
    // ...
}
```


---

## 11. Các ràng buộc quan trọng

### Production Constraints

| # | Ràng buộc |
|---|----------|
| 1 | **micro-jainslee chỉ R&D** — không đóng gói vào production |
| 2 | **Production build** — USSD 7.3 từ Mobicents SLEE master-era JARs |
| 3 | **Không thay EventRouter** — jSS7 Scheduler cho I/O dispatch |
| 4 | **RA fireEvent pattern** — từ RA threads, không từ SBB TimerCallback |
| 5 | **Timer Bridge** — fire events đến EventRouter, không execute trên wheel |

### Code Constraints

- Không blocking IO trong SBB
- Prefer immutable objects
- Không break MAP/SIP dialog state machine
- Maintain protocol compliance (3GPP)
- Kiểm tra: race conditions, memory leaks, timer leaks, dialog leaks, deadlocks

### Tech Debt Known

| Item | Status |
|------|--------|
| Empty `jainslee-apt` module | Pending |
| Empty `adapter-quarkus` module | Pending |
| Missing consumer handler for EventRouter | Pending |
| Duplicated `TimerPort` (api vs core) | Known |
| `@Deprecated` `ProfileTablePort` shim | Retained |

### GOAL Achievements ✅

| GOAL | Mô tả | Status |
|------|-------|--------|
| **GOAL 1** | `RaEndpointPort` — RA lifecycle interface (`activate`, `deactivate`, `getRaName`) | ✅ DONE |
| **GOAL 2** | `RaCommandPort` + `registerRa()` + `mapEventToSbb()` — 3-port RA registration & event routing | ✅ DONE |
| **GOAL 3** | `RaBootstrapPort` — `createActivityHandle()` + `fireEvent()` primitives cho RA | ✅ DONE |
| **GOAL 4** | `@InjectRa` annotation — inject `RaCommandPort` vào SBB field, thay thế abstract RA accessor | ✅ DONE |
| **GOAL 5** | `OutboundCommand` marker interface — type-safe command gửi từ SBB → RA | ✅ DONE |

---

## 12. Testing

```bash
# Chạy tất cả tests
mvn test

# Chạy test cụ thể
mvn test -pl jainslee-core -Dtest=EventRouterTest
```

### Test structure cho SBB

```java
class EchoSbbTest {
    private EchoSbb sbb;
    private FakeEchoRa mockRa;

    @BeforeEach
    void setUp() {
        mockRa = new FakeEchoRa();
        sbb = new EchoSbb() {
            public EchoResourceAdaptor getEchoRa() { return mockRa; }
            public TimerFacility getTimerFacility() { return new FakeTimerFacility(); }
        };
        sbb.sbbCreate();
    }

    @Test
    void shouldEchoMessage() {
        sbb.onEchoRequest(new EchoRequestEvent("Hello"), mockAci());
        assertEquals("ECHO: Hello", mockRa.getLastResponse());
    }

    @AfterEach
    void tearDown() { sbb.sbbRemove(); }
}
```

### Known Test Results

- ✅ 62+ tests passing trên JDK 25
- ✅ 100K SBB entities ~14 OS threads

---

## 13. FAQ & Troubleshooting

**Q: SBB không nhận được event?**
→ Kiểm tra: SBB đã đăng ký? InitialEventSelector đúng SbbID? Event type match? RA fireEvent() đúng?

**Q: Timer không fire?**
→ Kiểm tra: SleeTimerSchedulerBridge khởi tạo? HashedWheelTimer chạy? Timer bị cancel sớm?

**Q: Memory leak?**
→ sbbRemove() cleanup timer? ActivityContext detach? RA đóng connections?

**Q: Virtual Thread không hoạt động?**
→ JDK 25+, dùng `--enable-preview` nếu cần.

**Q: Production constraint?**
→ micro-jainslee TUYỆT ĐỐI không cho production. Production = Mobicents SLEE JARs + WildFly 10.

---

## Phụ lục A: Kiến trúc Production Stack

```
RestComm JAIN-SLEE v8 (WildFly 10):
  └─ LMAX Disruptor EventRouter (N workers, 262K ring)
  └─ Apache Commons Pool (minIdle=5000, maxActive=100000)
  └─ FaultTolerantScheduler (4 threads, cluster-aware)
  └─ Infinispan Distributed AC (HA)
  └─ JGroups Cluster Membership
```

## Phụ lục B: Kiến trúc R&D Stack

```
micro-jainslee (Java 25):
  └─ In-Memory EventRouter
  └─ VirtualThreadSbbEntityPool (~14 OS threads for 100K SBBs)
  └─ SleeTimerSchedulerBridge → jSS7 HashedWheelTimer (10ms)
  └─ Embedded RAs: gRPC Client, HTTP Ingress, USSD GW Simulator
```

---

> **Remember:** micro-jainslee is for R&D only. Happy coding! 🚀

---

## Phụ lục C: Cấu trúc JAIN SLEE Application (Pattern từ example/)

### C.1 Tổ chức thư mục

```
my-ussd-app/
├── pom.xml                         # depends on jainslee-core + vendor-ras
└── src/main/java/com/example/ussd/
    ├── events/                     ← SleeEvent classes
    │   ├── HttpUssdBeginEvent.java
    │   ├── GrpcMenuRequestEvent.java
    │   └── UssdResponseEvent.java
    ├── sbbs/                       ← SBB implementations
    │   ├── HttpServerSbb.java
    │   ├── GrpcClientSbb.java
    │   └── Ss7UssdIngressSbb.java  ← CMP-backed
    ├── MyAppBootstrap.java         ← Wires RAs, SBBs, event mappings
    └── MyAppMain.java              ← Entry point
```

### C.2 Cách thiết kế SBB

```java
@SbbAnnotation(name = "Ss7UssdIngress", vendor = "com.example", version = "1.0")
public abstract class Ss7UssdIngressSbb extends CmpBackedSbb implements SleeEventHandler {

    @CmpField("sessionId")  public abstract String getSessionId();
    @CmpField("sessionId")  public abstract void setSessionId(String v);

    @InitialEventSelect(name = "ussd-convergence")
    public InitialEventSelectResult select(InitialEventSelectCondition c) { /* ... */ }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) { /* ... */ }

    // $Concrete — hand-written (production: auto-generated)
    public static final class $Concrete extends Ss7UssdIngressSbb {
        private final Map<String, Object> local = new ConcurrentHashMap<>();
        @Override public String getSessionId() { return (String) local.get("sessionId"); }
    }
}
```

### C.3 Cách SBB gọi Resource Adaptor

Dùng `@InjectRa` để container inject `RaCommandPort`:

```java
public final class GrpcClientSbb implements Sbb, SleeEventHandler {


### C.4 Cách thiết kế Resource Adaptor

vendor-ras có 4 RA, mỗi RA = 2 class:

```
ra-http-server/src/main/java/com/microjainslee/ra/httpserver/
├── HttpServerResourceAdaptor.java   ← extends AbstractResourceAdaptor
└── HttpServerRaEndpoint.java        ← implements RaEndpointPort + RaCommandPort
```

Pattern Endpoint (3-port contract):

```java
public final class HttpServerRaEndpoint implements RaEndpointPort, RaCommandPort {
    private final HttpServerResourceAdaptor delegate;

    @Override public String getRaName() { return "http-server-ra"; }

    @Override public void activate(RaBootstrapPort bootstrap) {
        delegate.setResourceAdaptorContext(bridgeContext(bootstrap));
        delegate.raConfigure();
        delegate.raActive();
    }

    @Override public void sendCommand(OutboundCommand cmd) {
        if (cmd instanceof HttpServerCommand c) delegate.handleCommand(c);
    }
}
```

Pattern RA Core (extends AbstractResourceAdaptor):

```java
public final class HttpServerResourceAdaptor extends AbstractResourceAdaptor {
    @Override public void raConfigure() { /* setup */ }
    @Override public void raActive()    { /* start HttpServer */ }
    @Override public void raInactive()  { /* stop HttpServer */ }

    // Collaborator interfaces
    public interface ActivityContextFactory { /* ... */ }
}
```

### C.5 Cách wire toàn bộ application

```java
public final class MyAppBootstrap {
    private final MicroSleeContainer container;

    public void install(int httpPort) {
        // 1. Register SBB types
        container.registerSbbType(Ss7UssdIngressSbb.class, Ss7UssdIngressSbb.$Concrete::new);
        container.registerSbbType(GrpcClientSbb.class, GrpcClientSbb::new);

        // 2. Create & register RAs
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpPort);
        ra.setBeginEventFactory((sid, msisdn, ussd, cb) -> new HttpUssdBeginEvent(sid, msisdn, ussd, cb));
        HttpServerRaEndpoint ep = new HttpServerRaEndpoint(ra);
        container.registerRa(ep, ep);  // RaEndpointPort, RaCommandPort

        // 3. Map events → SBBs
        container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb");
        container.mapEventToSbb(GrpcMenuRequestEvent.class, "GrpcClientSbb");
    }
}
```

### C.6 Full request flow

```
ussd-client-simulator → HTTP POST → ra-http-server
  → fireEvent(HttpUssdBeginEvent) → EventRouter
  → HttpServerSbb → Ss7UssdIngressSbb → child GrpcClientSbb
  → @InjectRa grpcPort.sendCommand(GrpcMenuCommand) → ra-grpc-client
  → gRPC ResolveMenu → grpc-server-simulator → menu response
  → fireEvent(GrpcMenuResponseEvent) → GrpcClientSbb → Ss7UssdIngressSbb
  → UssdResponseEvent → HttpServerSbb → ra-http-client callback → simulator
```

    @InjectRa(name = "grpc-menu-ra")          // ← match với getRaName()
    private volatile RaCommandPort grpcPort;

    public void sendMenu(String sid, String msisdn, String ussd, ActivityContextInterface aci) {
        grpcPort.sendCommand(new GrpcMenuCommand(sid, msisdn, ussd, aci));
    }
}
```

> **Key:** SBBs KHÔNG import RA classes trực tiếp. Chỉ biết `RaCommandPort` + `OutboundCommand`.





---

## Phụ lục D: Full Request Walkthrough (Step-by-Step)

> Xem 711 dòng code chi tiết: [`docs/EXAMPLE_WALKTHROUGH.md`](EXAMPLE_WALKTHROUGH.md)

### D.1 8 bước của 1 USSD request (từ HTTP POST đến callback)

```
STEP 1 — Client POST → HTTP Server RA
  POST /api/ussd/begin-callback {"msisdn":"251911000001","ussdString":"*123#"}
  → HttpServerResourceAdaptor.BeginHandler
  → sessionPreparer.prepare() → tạo HttpServerSbb entity, attach vào sessionId
  → beginEventFactory → new HttpUssdBeginEvent
  → fireEvent() → EventRouter

STEP 2 — EventRouter → HttpServerSbb
  Lookup: HttpUssdBeginEvent.class → "HttpServerSbb"
  → pool.acquire() → HttpServerSbb.onEvent()

STEP 3 — HttpServerSbb internal routing
  lookupTier(msisdn) → "GOLD" (Profile)
  → acquireEntity Ss7UssdIngressSbb ($Concrete::new)
  → routeEvent(new Ss7UssdBeginEvent)

STEP 4 — Ss7UssdIngressSbb (core logic)
  setTimer(30s) → child GrpcClientSbb → routeEvent(new GrpcMenuRequestEvent)

STEP 5 — GrpcClientSbb → gRPC RA (outbound)
  @InjectRa grpcCommandPort.sendCommand(new GrpcMenuCommand)
  → GrpcMenuResourceAdaptor.requestMenu()
  → gRPC ResolveMenu → grpc-server-simulator:9090

STEP 6 — gRPC multi-level menu response
  MultiLevelMenuService.resolveMenu() → menu text
  → eventFactory → new GrpcMenuResponseEvent
  → routeResponse() → EventRouter

STEP 7 — Menu text → final response
  GrpcMenuResponseEvent → Ss7UssdIngressSbb
  cancelTimer() → routeEvent(new UssdResponseEvent)

STEP 8 — HTTP callback back to simulator
  UssdResponseEvent → HttpServerSbb
  @InjectRa httpCallbackPort.sendCommand(new HttpCallbackCommand)
  → HTTP POST callback URL → ussd-client-simulator
  → releaseSession()
```

### D.2 SBB entity = parked Virtual Thread (100K sessions = ~42 OS threads)

```
HttpServerSbb entity [session-1]    → parked VT
Ss7UssdIngressSbb entity [session-1] → parked VT  + IES + CMP + Timer
GrpcClientSbb entity [session-1]    → parked VT  + @InjectRa grpcMenuRa

Khi event đến → EventRouter:
  unpark VT → SBB.onEvent() → park VT
```

### D.3 How everything connects

```
                     EmbeddedUssdMain.main()
                            │
                    new MicroSleeContainer(config)
                            │
                    EmbeddedUssdBootstrap.install()
                            │
          ┌─────────────────┼────────────────────┐
          ▼                 ▼                    ▼
    registerSbbTypes()  registerRa()        mapEventToSbb()
          │                 │                    │
    SbbEntityPool      RaRegistry           EventRouter routing table
    (factory map)      RaEndpointPort
          │            RaCommandPort
          │                 │
    ┌─────┴─────┐     ┌─────┴──────────┐
    │HttpServerSbb│   │ra-http-server   │ ← listen :8082
    │            │←──│ (fireEvent)      │
    │            │──→│ra-http-client    │ ← HTTP POST callback
    │            │   │ra-grpc-client    │ ← gRPC → :9090 simulator
    │Ss7Ussd    │    └─────────────────┘
    │IngressSbb │
    │GrpcClient │
    │Sbb        │
    └───────────┘
```

> **Golden Rule:** SBBs KHÔNG import class RA. SBBs chỉ biết `@InjectRa RaCommandPort` + `OutboundCommand record`.
> RA KHÔNG biết class SBB. RA chỉ biết collaborator interfaces (lambda từ bootstrap).

