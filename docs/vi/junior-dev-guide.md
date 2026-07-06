# 🚀 Micro-JAINSLEE Junior Developer Guide

> **Tài liệu onboarding cho developer mới làm việc với micro-jainslee.**
>
> Last updated: 2026-07-06 | Maintainer: nhanth87
>
> Bộ tài liệu gồm 4 file — đọc theo thứ tự:
>
> 1. **File này** — khái niệm, kiến trúc, build, event flow.
> 2. [sbb-guide.md](sbb-guide.md) — viết SBB (service logic).
> 3. [ra-guide.md](ra-guide.md) — viết Resource Adaptor (3-port contract).
> 4. [app-guide.md](app-guide.md) — wire SBB + RA thành một app Quarkus hoàn chỉnh.

---

## 1. micro-jainslee là gì?

**micro-jainslee** là một **JAIN SLEE 1.1 runtime nhẹ, nhúng được (embeddable)** viết lại từ đầu trên Java 25:

- Bỏ toàn bộ phần nặng của JSLEE cổ điển: JBoss/WildFly, JMX management, deployable-unit XML, profile management phức tạp.
- Giữ lại phần lõi giá trị: **event-driven SBB model, Activity Context, event routing, timer facility, RA contract**.
- Chạy được 3 chế độ: embedded thuần Java 25, **Quarkus (mục tiêu chính — để build GraalVM native)**, Spring Boot.

> ⚠️ **Định hướng hiện tại: chỉ tập trung Quarkus.** Spring/embedded được giữ compile-xanh nhưng không đầu tư thêm. Về dài hạn transport Netty sẽ được thay bằng DPDK datapath (C++/Rust) đẩy event vào app Java native (Quarkus + Graal VM) — vì vậy **mọi transport phải nằm sau interface** (xem `SipTransport`).

### 4 khái niệm bắt buộc phải hiểu


| Khái niệm          | Là gì                                                                                                                   | Ví dụ                                                     |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------- |
| **Event**          | Một sự kiện bất biến (immutable) từ mạng hoặc nội bộ. Implement `SleeEvent`.                                            | `SipInviteEvent`, `HttpUssdBeginEvent`, `TimerFiredEvent` |
| **Activity / ACI** | Một "phiên" protocol (SIP dialog, USSD session…). SBB attach vào `ActivityContextInterface` để nhận event của phiên đó. | Call-ID của SIP dialog = 1 activity                       |
| **SBB**            | Service Building Block — logic nghiệp vụ, xử lý event. Implement `Sbb` + `SleeEventHandler`.                            | `ProxySbb` xử lý INVITE                                   |
| **RA**             | Resource Adaptor — cầu nối protocol ↔ SLEE. Nhận bytes từ mạng → fire event; nhận command từ SBB → gửi ra mạng.         | `ra-sip-servlet`, `ra-diameter`                           |


### Luồng event một chiều (không bao giờ ngược)

```
   Network                RA                    Core                    SBB
     │  bytes    ┌─────────────────┐   ┌──────────────────┐   ┌────────────────┐
     ├──────────►│ parse + classify│──►│ EventRouter      │──►│ onEvent(e, aci)│
     │           │ fireEvent(...)  │   │ (LMAX Disruptor) │   │  business logic│
     │           └─────────────────┘   └──────────────────┘   └───────┬────────┘
     │                    ▲                                           │
     │  bytes    ┌────────┴────────┐          sendCommand(cmd)        │
     ◄───────────│ OutboundSender  │◄──────────────────────────────────┘
                 └─────────────────┘
```

Quy tắc vàng: **SBB không bao giờ tự mở socket, RA không bao giờ chứa business logic.**

---

## 2. Cấu trúc repo

```
micro-jainslee/
├── jainslee-api/        # API thuần Java 25 (Sbb, SleeEvent, ACI, 3-port contract…)
├── jainslee-core/       # Engine: MicroSleeContainer, EventRouter, entity pool, IES
├── jainslee-ra-spi/     # RA SPI kiểu JSLEE 1.1 cổ điển (AbstractResourceAdaptor…)
├── jainslee-scheduler/  # HashedWheelTimer cho SLEE timer
├── jainslee-apt/        # Annotation processor sinh sbb-index.properties
├── jainslee-codegen/    # Javassist sinh concrete SBB cho CMP field
├── jainslee-tx/         # Narayana JTA (tùy chọn)
├── jainslee-cluster/    # Infinispan/JGroups (tùy chọn)
├── jainslee-adapter/
│   ├── adapter-quarkus/     # ★ Quarkus extension (runtime + deployment)
│   ├── adapter-springboot/  # (low priority)
│   └── adapter-jakartaee/   # (low priority)
├── vendor-ras/          # RA có sẵn: ra-sip-servlet, ra-diameter, ra-http-*, ra-grpc-*
└── example/             # App mẫu: example-quarkus-ussdgw (USSD), example-quarkus-sip (SIP GW)…
```

**Ràng buộc kiến trúc (không được vi phạm):**

- `jainslee-api` và `jainslee-core`: **zero framework dependency** (không Spring/Quarkus import).
- Hạn chế reflection trong core (mục tiêu GraalVM native).
- App code chỉ phụ thuộc `jainslee-api` + `jainslee-core` + RA modules — không đụng internals.

---

## 3. Build &amp; chạy

```bash
# Yêu cầu: JDK 25 (mise.toml đã khai báo zulu-25), Maven 3.9+
mvn clean install              # build 24 module runtime
mvn -Pexamples clean install   # build kèm 4 app mẫu
mvn -Pexamples test            # chạy toàn bộ test (400+)

# Chạy SIP gateway mẫu (Quarkus dev mode)
cd example/example-quarkus-sip && mvn quarkus:dev

# Chạy USSD demo
cd example/example-quarkus-ussdgw && mvn quarkus:dev
```

---

## 4. Event routing — SBB nhận event bằng cách nào?

Đây là phần quan trọng nhất của runtime. Khi RA fire một event lên ACI, `MicroSleeContainer.routeEvent()` quyết định SBB nào nhận, theo thứ tự:

### 4.1. `mapEventToSbb()` — khai báo cấp chính quyền (khuyến nghị)

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/bootstrap/SipGatewayBootstrap.java

```java
container.registerSbbType(ProxySbb.class, ProxySbb::new); // đăng ký type + factory
container.createIesDispatcher();                          // bật convergence routing
container.mapEventToSbb(SipInviteEvent.class, "ProxySbb"); // event → SBB type
```

Khi `SipInviteEvent` đến một ACI:

1. Nếu ACI **đã có** một SBB đúng type attach rồi → xong (không tạo trùng).
2. Nếu chưa → hỏi **IES dispatcher** (mục 4.2) để tìm/tạo entity theo convergence name, rồi attach.
3. Nếu không bind IES → tạo entity định danh `Type/aciName` (1 entity / activity).

Mapping match theo **cả class cha** — map `SipEvent.class` sẽ bắt mọi event con.

### 4.2. Initial Event Selector (IES) — session convergence

IES trả lời câu hỏi *"event này thuộc về session/entity nào?"* (JSLEE 1.1 §7.5). SBB khai báo bằng annotation:

> 📄 File: example/example-quarkus-ussdgw/src/main/java/com/example/ussddemo/quarkus/sbbs/HttpServerSbb.java

```java
@InitialEventSelect(name = "ussd-session-convergence")
public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
    if (c.getEvent() instanceof Ss7UssdBeginEvent e) {
        // Mọi event cùng msisdn hội tụ về CÙNG một SBB entity
        return InitialEventSelectResult.forSession(e.getMsisdn(), true);
    }
    return InitialEventSelectResult.builder().initialEvent(false).build();
}
```

- **Luôn dùng `container.createIesDispatcher()`** để bind. ❌ Đừng bao giờ tự viết `SbbEntityPool` adapter — adapter tự chế tạo raw entity bỏ qua lifecycle (không `@InjectRa`, không CMP, không cleanup) và là nguồn bug kinh điển của repo này.
- SBB có `@InitialEventSelect` **bắt buộc có no-arg constructor** (IES chạy trên temp instance).

### 4.3. Fallback

Không có mapping, ACI trống → chọn SBB **đăng ký sớm nhất có `EventMask` chấp nhận event** (đăng ký programmatic ưu tiên hơn auto-deploy từ sbb-index). Fallback này chỉ hợp cho app 1 SBB — app thật hãy dùng 4.1.

### 4.4. Những gì router bảo đảm

- Event của **cùng một entity** chạy tuần tự trên **một virtual thread riêng** (không cần lock trong SBB).
- SBB attach cùng ACI nhận event theo **priority giảm dần** (`localObject.setPriority(n)`).
- SBB exception **không giết router** — được đưa vào `ErrorHandlingPolicy` + log; disruptor luôn sống.
- Entity bị remove khi event còn trong queue → event bị drop an toàn (đếm vào `missingEntityCount`).

---

## 5. Lifecycle

### SBB entity

```
registerSbbType ──► acquireEntity/IES allocate ──► setSbbContext → sbbCreate
   → sbbPostCreate → sbbActivate → READY ──(events)──► remove() → sbbRemove
```

Activation chạy **async** trên entity thread. Nếu cần chắc chắn READY trước khi gọi method trực tiếp (ngoài event path): `localObject.awaitReady(5, SECONDS)`. Event qua router **không cần** chờ — queue của entity tự bảo đảm thứ tự.

### RA (3-port)

```
container.registerRa(endpoint, commandPort)
   → (container STARTED) endpoint.activate(bootstrapPort)   // RA mở transport
   → ... hoạt động ...
   → container.stop() → endpoint.deactivate()               // RA đóng transport
```

Khi protocol session kết thúc (BYE, timeout…), RA **phải** gọi `bootstrapPort.endActivity(handle)` — SBB attach sẽ nhận `ActivityEndedEvent` và ACI được thu hồi. Quên bước này = memory leak.

---

## 6. Các lỗi kinh điển (đều từng xảy ra trong repo này)


| #   | Anti-pattern                                                           | Hậu quả                                                                | Cách đúng                                                                               |
| --- | ---------------------------------------------------------------------- | ---------------------------------------------------------------------- | --------------------------------------------------------------------------------------- |
| 1   | SBB nhận event X rồi `container.routeEvent(X, aci)` lại chính event đó | **Vòng lặp vô hạn** (300k+ event/s)                                    | Không bao giờ re-route event mình nhận. RA đã fire lên ACI, mọi SBB attach đều nhận rồi |
| 2   | RA nhận command rồi publish lại request event lên cùng ACI             | Vòng lặp SBB ↔ RA                                                      | Command là chiều đi ra — không mirror ngược thành event                                 |
| 3   | Tự viết IES `SbbEntityPool` adapter                                    | Entity không có lifecycle/`@InjectRa`; convergence bị xóa ngay khi tạo | `container.createIesDispatcher()`                                                       |
| 4   | `@InjectRa(name="grpcMenuRa")` nhưng RA đăng ký tên `grpc-menu-ra`     | Port null, command bị drop im lặng                                     | Tên trong `@InjectRa` = giá trị `RaEndpointPort.getRaName()` chính xác từng ký tự       |
| 5   | Map `dialogs`/`sessions` trong RA chỉ put không remove                 | OOM sau vài giờ chạy                                                   | Remove khi protocol kết thúc + idle sweeper (xem `DialogRegistry`)                      |
| 6   | Transport callback chỉ nhận `byte[]`                                   | Không biết trả lời về đâu (UDP)                                        | Sink phải kèm peer address (`SipMessageSink`)                                           |
| 7   | Sleep/poll chờ entity READY                                            | Flaky test, treo thread                                                | `awaitReady(timeout)` hoặc để router tự xử lý                                           |
| 8   | Business logic gọi thẳng class RA (`ra.doSomething()`)                 | Không test được, gãy khi swap RA                                       | SBB chỉ nói chuyện qua `RaCommandPort.sendCommand(cmd)`                                 |


---

## 7. Testing

- **Unit test SBB**: gọi thẳng `onEvent(event, aci)` với ACI thật từ `container.createActivityContext("test")` — SBB là POJO.
- **Integration**: dựng `MicroSleeContainer` thật trong `@Before` (nhanh, &lt;100ms), đăng ký type + RA, bắn event, assert bằng latch. Mẫu chuẩn: `SipEndToEndTest` (ra-sip-servlet) — UDP socket thật → SBB → response thật.
- **Smoke E2E**: `UssdDemoSmokeTest` (example-quarkus-ussdgw) — HTTP begin → chuỗi 3 SBB → poll COMPLETED.
- Chạy nhanh 1 test: `mvn -pl [[ORCA_RAW_HTML_INLINE:%3Cmodule%3E]] test -Dtest='TenTest#tenMethod'`.

---

## 8. Đọc tiếp

- [sbb-guide.md](sbb-guide.md) — checklist + template viết SBB.
- [ra-guide.md](ra-guide.md) — 3-port contract, transport, dialog lifecycle, lấy `ra-sip-servlet` làm mẫu.
- [app-guide.md](app-guide.md) — bootstrap Quarkus từng bước, cấu hình, chạy thử bằng `sipexer`/`curl`.
- `docs/microjainslee-design.md` — thiết kế runtime chi tiết.
- JAIN SLEE 1.1 spec (JSR-240) — chương 6 (SBB), 7 (Activity/Event), 12 (RA) nếu muốn hiểu gốc.

---

## Appendix: Quick Reference — key files to read

> **Read these first** when exploring the codebase. Each file is annotated with what you'll learn.

### Core Runtime (read in order)


| File                                                                                                                   | What you'll learn                                                         |
| ---------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------- |
| `jainslee-api/src/main/java/com/microjainslee/api/SleeEvent.java`                                                      | Base event interface                                                      |
| `jainslee-api/src/main/java/com/microjainslee/api/ActivityContextInterface.java`                                       | ACI: how SBBs attach to sessions                                          |
| `jainslee-api/src/main/java/com/microjainslee/api/Sbb.java` + `SleeEventHandler.java`                                  | SBB contract                                                              |
| `jainslee-api/src/main/java/com/microjainslee/api/RaEndpointPort.java` + `RaCommandPort.java` + `RaBootstrapPort.java` | 3-port RA contract                                                        |
| `jainslee-core/src/main/java/com/microjainslee/core/MicroSleeContainer.java`                                           | Container: start/stop, registerSbbType, registerRa, fireEvent, routeEvent |
| `jainslee-core/src/main/java/com/microjainslee/core/EventRouter.java`                                                  | LMAX Disruptor ring buffer event routing                                  |
| `jainslee-core/src/main/java/com/microjainslee/core/IesDispatcher.java`                                                | IES session convergence                                                   |
| `jainslee-scheduler/src/main/java/com/microjainslee/scheduler/HashedWheelTimer.java`                                   | Timer facility                                                            |


### RA Reference (best example of production-quality RA)


| File                                                                         | What you'll learn                                              |
| ---------------------------------------------------------------------------- | -------------------------------------------------------------- |
| `vendor-ras/ra-sip-servlet/DESIGN.md`                                        | Architecture decisions, thread model, DNS flow                 |
| `vendor-ras/ra-sip-servlet/src/main/java/.../SipServletResourceAdaptor.java` | RA core: parse → classify → fireEvent; command → encode → send |
| `vendor-ras/ra-sip-servlet/src/main/java/.../SipServletRaEndpoint.java`      | 3-port wrapper: activate/deactivate/sendCommand                |
| `vendor-ras/ra-sip-servlet/src/main/java/.../collab/DialogRegistry.java`     | Session tracking + idle sweeper anti-leak pattern              |
| `vendor-ras/ra-sip-servlet/src/main/java/.../transport/SipTransport.java`    | Transport interface (how DPDK swap works)                      |
| `vendor-ras/ra-sip-servlet/src/test/java/.../SipEndToEndTest.java`           | ★ How to integration-test RA+SBB end-to-end                    |


### Bootstrap (app wiring)


| File                                                                               | What you'll learn                                                           |
| ---------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| `example/example-quarkus-sip/src/main/java/.../bootstrap/SipGatewayBootstrap.java` | SIP app: registerSbbType → createIesDispatcher → mapEventToSbb → registerRa |
| `example/example-quarkus-ussdgw/src/main/java/.../bootstrap/UssdDemoBootstrap.java`       | USSD app: same pattern + collaborator injection                             |
| `example/example-quarkus-ussdgw/src/test/java/.../bootstrap/UssdDemoSmokeTest.java`       | Plain-JUnit smoke test without CDI                                          |


### Adapter (Quarkus extension)


| File                                                            | What you'll learn                                              |
| --------------------------------------------------------------- | -------------------------------------------------------------- |
| `jainslee-adapter/adapter-quarkus/runtime/src/main/java/...`    | `MicroSleeContainer` producer, config binding                  |
| `jainslee-adapter/adapter-quarkus/deployment/src/main/java/...` | Build-time processor: sbb-index, reflective class registration |


