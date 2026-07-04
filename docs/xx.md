# 🚀 Micro-JAINSLEE Junior Developer Guide

> **Tài liệu onboarding cho developer mới làm việc với micro-jainslee**
>
> Last updated: 2026-07-03 | Maintainer: nhanth87
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

## 4. PolyVoice & 3-Port Contract Pattern

### PolyVoice Pattern

**PolyVoice** là pattern kiến trúc cho phép một SBB xử lý **nhiều loại voice/session** (USSD, SIP, gRPC) thông qua cùng một service logic. Pattern này tận dụng **Resource Adaptor abstraction** để tách biệt protocol khỏi business logic.

```
PolyVoice SBB:
  Input Ports      │  Output Ports      │  Internal Ports
  ─────────────────┼────────────────────┼───────────────────
  USSD RA Port    │  USSD RA Port     │  TimerPort
  (MAP/USSD events)│  (sendUSSD)       │  ActivityContextInterface
  SIP RA Port     │  SIP RA Port      │
  gRPC RA Port    │  gRPC RA Port     │
```

### 3-Port Contract

Mỗi SBB trong JAIN SLEE tuân theo **3-Port Contract**:

| Port | Interface | Mục đích |
|------|-----------|----------|
| **Port 1: Event Handler** | `onXxxEvent(EventType, ACI)` | Nhận event từ EventRouter |
| **Port 2: Resource Adaptor** | `RAInterface.sendXxx(...)` | Gửi command/response ra ngoài |
| **Port 3: SLEE Facilities** | `TimerFacility`, `ACI`, `SbbLocalObject` | SLEE facilities |

### Ví dụ PolyVoice 3-Port SBB

```java
@Sbb(id = "PolyVoiceSBB", service = "PolyVoiceService")
public abstract class PolyVoiceSbb implements Sbb {

    // === PORT 1: Event Handlers ===
    public void onUssdRequest(UssdRequestEvent event, ActivityContextInterface aci) {
        String response = processVoiceRequest(
            aci.getActivity().toString(), event.getMsisdn(),
            event.getUssdString(), "USSD");
        getUssdRa().sendUssdResponse(sessionId, response);
    }

    public void onSipInvite(SipInviteEvent event, ActivityContextInterface aci) {
        processSipCall(event, aci);
    }

    // === PORT 2: RA Accessors ===
    public abstract UssdResourceAdaptor getUssdRa();
    public abstract SipResourceAdaptor getSipRa();

    // === PORT 3: SLEE Facilities ===
    public abstract TimerFacility getTimerFacility();

    public void onTimer(TimerEvent event, ActivityContextInterface aci) {
        handleTimeout(event.getTimerID(), aci);
    }

    // Shared business logic
    private String processVoiceRequest(String sid, String msisdn,
                                        String input, String proto) {
        return "Welcome to PolyVoice [" + proto + "]";
    }
}
```

---

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



