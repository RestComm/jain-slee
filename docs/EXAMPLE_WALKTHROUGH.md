# 🧭 Example Walkthrough — Từng dòng code giải thích

> Giải thích từng class, từng dòng code trong `example-embedded-j25`,
> cách các thành phần liên kết với nhau, và flow của 1 USSD request.
>
> Last updated: 2026-07-04

---

## Mục lục

1. [Tổng quan kiến trúc example](#1-tổng-quan-kiến-trúc-example)
2. [Entry Point: EmbeddedUssdMain.java — khởi động container](#2-entry-point-embeddedussdmainjava)
3. [Bootstrap: EmbeddedUssdBootstrap.java — wire toàn bộ app](#3-bootstrap-embeddedussdbootstrapjava)
4. [Events: cách event được tạo và routing](#4-events)
5. [SBBs: container tạo và quản lý SBB entity thế nào](#5-sbbs)
6. [@InjectRa: SBB gọi RA qua RaCommandPort](#6-injectra)
7. [RAs: cách RA kết nối vào JAIN SLEE app](#7-ras)
8. [Full request flow — step by step](#8-full-request-flow)
9. [Profile & Timer](#9-profile--timer)

---

## 1. Tổng quan kiến trúc example

```
example-embedded-j25/src/main/java/com/example/ussddemo/
│
├── EmbeddedUssdMain.java          ← 1. Entry point: tạo container + bootstrap
├── EmbeddedUssdBootstrap.java     ← 2. Wire: đăng ký SBB, RA, event mapping
├── UssdDemoRuntime.java           ← 3. Helper: session logging
├── UssdSubscriberProfile.java     ← 4. Profile: GOLD/SILVER subscriber
│
├── events/                        ← 5. 5 SleeEvent types
│   ├── HttpUssdBeginEvent.java    ←    HTTP ingress → SBB
│   ├── Ss7UssdBeginEvent.java     ←    HttpServerSbb → Ss7UssdIngressSbb
│   ├── GrpcMenuRequestEvent.java  ←    Ss7UssdIngressSbb → GrpcClientSbb
│   ├── GrpcMenuResponseEvent.java ←    gRPC RA → GrpcClientSbb
│   └── UssdResponseEvent.java     ←    Final response → HttpServerSbb
│
└── sbbs/                          ← 6. 3 SBB implementations
    ├── HttpServerSbb.java         ←    Entry point từ HTTP
    ├── GrpcClientSbb.java         ←    Bridge tới gRPC RA
    └── Ss7UssdIngressSbb.java     ←    Core logic + CMP + IES + Timer
```

### Nguyên tắc thiết kế

1. **Không có ra/ directory** — RA code nằm trong `vendor-ras/`
2. **Không có DU XML** — tất cả đăng ký qua Java API trong Bootstrap
3. **SBBs không import RA trực tiếp** — chỉ dùng `RaCommandPort` + `@InjectRa`
4. **Events immutable** — chỉ chứa dữ liệu, không có logic

---

## 2. Entry Point: EmbeddedUssdMain.java

Đây là `public static void main()` — nơi mọi thứ bắt đầu.

### Code phân tích

```java
public static void main(String[] args) throws Exception {
    // Bước 1: Đọc config từ command line hoặc system properties
    int httpPort = args.length > 0 ? Integer.parseInt(args[0]) : 8082;
    String grpcHost = "127.0.0.1";
    int grpcPort = 9090;

    // Bước 2: Tạo MicroSleeConfiguration — cấu hình container
    MicroSleeConfiguration configuration = MicroSleeConfiguration.builder()
            .eventRouterBufferSize(2048)       // Ring buffer size cho EventRouter
            .preferVirtualThreads(true)         // SBB entity = virtual thread
            .sbbPoolMin(16)                     // Pool min size
            .sbbPoolMax(4096)                   // Pool max size
            .build();

    // Bước 3: Tạo container (bên trong: EventRouter, SbbEntityPool, TimerPort...)
    container = new MicroSleeContainer(configuration);

    // Bước 4: Tạo bootstrap — object wire toàn bộ app
    runtime = new UssdDemoRuntime();
    bootstrap = new EmbeddedUssdBootstrap(container);

    // Bước 5: Bind Initial Event Selector TRƯỚC khi start container
    bootstrap.bindInitialEventSelector();

    // Bước 6: Start container → EventRouter bắt đầu dispatch thread
    container.start();

    // Bước 7: Install RAs + register SBBs + map events
    //         (CHỈ gọi sau khi container.start())
    bootstrap.install(httpPort, grpcHost, grpcPort);

    // Bước 8: Block main thread — chờ shutdown hook
    CountDownLatch shutdownLatch = new CountDownLatch(1);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        bootstrap.shutdown();   // deactivate RAs
        container.stop();       // stop EventRouter
        shutdownLatch.countDown();
    }));
    shutdownLatch.await();  // ← main thread parked here forever
}
```

### Tại sao thứ tự quan trọng?

```
1. new MicroSleeContainer()    → tạo EventRouter, SbbEntityPool (chưa chạy)
2. bindInitialEventSelector()  → đăng ký IES dispatcher (cần trước start)
3. container.start()           → EventRouter bắt đầu accept event
4. bootstrap.install()         → registerSbbTypes() + registerRa() + mapEventToSbb()
```

Nếu gọi `registerSbbType()` trước `container.start()`: container từ chối vì chưa sẵn sàng.
Nếu gọi `container.start()` sau `bootstrap.install()`: RAs đã đăng ký nhưng chưa active.

---

## 3. Bootstrap: EmbeddedUssdBootstrap.java

Đây là "trái tim" của app — nơi wire tất cả lại với nhau.

### `install()` — 6 bước tuần tự

```java
public void install(int httpPort, String grpcHost, int grpcPort) {
    seedProfiles();                       // 1. Tạo profile GOLD/SILVER
    registerSbbTypes();                   // 2. Đăng ký 3 SBB types vào pool
    wireHttpServerRa(httpPort);           // 3. Tạo + đăng ký HTTP server RA
    wireHttpCallbackRa();                 // 4. Tạo + đăng ký HTTP callback RA
    wireGrpcMenuRa(grpcHost, grpcPort);   // 5. Tạo + đăng ký gRPC client RA
    bindEventMappings();                  // 6. Map Event → SBB
}
```

### 3.1 `registerSbbTypes()` — Đăng ký SBB vào pool

```java
private void registerSbbTypes() {
    // Ss7UssdIngressSbb là abstract → cần factory lambda $Concrete::new
    container.registerSbbType(Ss7UssdIngressSbb.class, Ss7UssdIngressSbb.$Concrete::new);
    // HttpServerSbb là concrete → dùng constructor reference
    container.registerSbbType(HttpServerSbb.class, HttpServerSbb::new);
    // GrpcClientSbb là concrete → dùng constructor reference
    container.registerSbbType(GrpcClientSbb.class, GrpcClientSbb::new);
}
```

**Điều gì xảy ra bên trong `registerSbbType()`?**

```
MicroSleeContainer.registerSbbType(Ss7UssdIngressSbb.class, Ss7UssdIngressSbb.$Concrete::new)
  → VirtualThreadSbbEntityPool.registerType(Ss7UssdIngressSbb.class, $Concrete::new)
    → Lưu factory vào Map<Class<? extends Sbb>, Supplier<Sbb>>
    → Sau này khi cần entity mới: gọi factory.get() → new $Concrete()
```

### 3.2 `wireHttpServerRa()` — Tạo RA từ vendor-ras

```java
private void wireHttpServerRa(int port) {
    // 1. Tạo RA delegate (bên trong vendor-ras)
    HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
    ra.setPort(port);

    // 2. Set collaborator interfaces (dependency injection manual)
    ra.setBeginEventFactory((sessionId, msisdn, ussd, cbUrl) ->
        new HttpUssdBeginEvent(sessionId, msisdn, ussd, cbUrl));

    ra.setActivityContextFactory((sessionId, ctx) ->
        container.createActivityContext(sessionId));

    ra.setSessionPreparer(this::prepareHttpSession);

    // 3. Wrap vào Endpoint (3-port contract)
    httpServerEndpoint = new HttpServerRaEndpoint(ra);

    // 4. Đăng ký với container
    container.registerRa(httpServerEndpoint, httpServerEndpoint);
    //                      ↑ RaEndpointPort    ↑ RaCommandPort
}
```

### 3.3 `bindEventMappings()` — Event routing table

```java
private void bindEventMappings() {
    container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb");
    container.mapEventToSbb(Ss7UssdBeginEvent.class, "Ss7UssdIngress");
    container.mapEventToSbb(GrpcMenuRequestEvent.class, "GrpcClientSbb");
    container.mapEventToSbb(GrpcMenuResponseEvent.class, "Ss7UssdIngress");
    container.mapEventToSbb(UssdResponseEvent.class, "HttpServerSbb");
}
```

**Điều gì xảy ra bên trong?**

```
container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb")
  → EventRouter.registerMapping(HttpUssdBeginEvent.class → "HttpServerSbb")
  → Lưu vào ConcurrentHashMap<Class<? extends SleeEvent>, String>
  → Khi RA gọi fireEvent(HttpUssdBeginEvent), EventRouter lookup bảng này
    → Tìm thấy "HttpServerSbb" → route event đến SBB đó
```

---

## 4. Events — cách event được tạo và routing

### 5 SleeEvent types trong example

| Event | Tạo bởi | Nhận bởi | Mục đích |
|-------|---------|----------|----------|
| `HttpUssdBeginEvent` | `HttpServerResourceAdaptor` (RA) | `HttpServerSbb` | Bắt đầu 1 USSD session |
| `Ss7UssdBeginEvent` | `HttpServerSbb.onHttpBegin()` | `Ss7UssdIngressSbb` | Internal routing sau profile lookup |
| `GrpcMenuRequestEvent` | `Ss7UssdIngressSbb.onSs7Begin()` | `GrpcClientSbb` | Yêu cầu gọi gRPC menu |
| `GrpcMenuResponseEvent` | `GrpcMenuResourceAdaptor` (RA) | `GrpcClientSbb` | Kết quả gRPC menu |
| `UssdResponseEvent` | `Ss7UssdIngressSbb.onGrpcResponse()` | `HttpServerSbb` | Final response text |

### Lifecycle của 1 event

```
1. RA gọi: endpoint().fireEvent(activityHandle, event)
      ↓
2. RaEndpoint → bridgeContext → RaBootstrapPort.fireEvent()
      ↓
3. MicroSleeContainer.fireEvent(event, activityHandle, address)
      ↓
4. EventRouter.routeEvent(event, aci)
      ↓
5. EventRouter lookup: HttpUssdBeginEvent → "HttpServerSbb"
      ↓
6. SbbEntityPool.acquire("HttpServerSbb") → HttpServerSbb instance
      ↓
7. HttpServerSbb.onEvent(event, aci) ← SBB nhận event
```

### Tại sao event là immutable records?

```java
public final class HttpUssdBeginEvent implements SleeEvent {
    private final String sessionId;    // final — không thể thay đổi sau khi tạo
    private final String msisdn;

    public HttpUssdBeginEvent(String sessionId, String msisdn, ...) {
        this.sessionId = sessionId;    // set 1 lần duy nhất
    }
    // KHÔNG có setter — event là read-only snapshot
}
```

Event được tạo bởi RA hoặc SBB, sau đó route qua EventRouter đến SBB đích.
SBB đọc dữ liệu từ event (getter) nhưng không sửa được — đảm bảo thread safety.

---

## 5. SBBs — container tạo và quản lý SBB entity thế nào

### 3 loại SBB

| SBB | Loại | Đặc điểm |
|-----|------|----------|
| `HttpServerSbb` | Concrete, pooled | Nhận HTTP event, lookup profile |
| `GrpcClientSbb` | Concrete, pooled | Bridge gọi gRPC RA |
| `Ss7UssdIngressSbb` | Abstract + CMP | Core logic, CMP fields, IES, Timer |

### SBB lifecycle

```
1. registerSbbType(HttpServerSbb.class, HttpServerSbb::new)
   → Pool biết cách tạo HttpServerSbb instance

2. Khi event đầu tiên đến: pool.acquire("HttpServerSbb")
   → Gọi factory.get() → new HttpServerSbb()
   → Container gọi: sbb.sbbCreate()  → sbb.sbbActivate()

3. @InjectRa injection xảy ra TRONG sbbCreate():
   → Container scan field có @InjectRa(name="httpCallbackRa")
   → Lookup RaCommandPort đã đăng ký với tên đó
   → Set field = raCommandPort

4. SBB xử lý event trong onEvent()

5. Khi session kết thúc: container.releaseEntity(entityId)
   → Container gọi: sbb.sbbPassivate() → sbb.sbbRemove()
   → Return instance về pool
```

### Ss7UssdIngressSbb — Tại sao abstract + $Concrete?

```java
@SbbAnnotation(name = "Ss7UssdIngress", vendor = "com.example.ussddemo", version = "1.0")
public abstract class Ss7UssdIngressSbb extends CmpBackedSbb implements SleeEventHandler {

    // CMP fields — abstract, container sẽ sinh implementation
    @CmpField("sessionId")
    public abstract String getSessionId();
    @CmpField("sessionId")
    public abstract void setSessionId(String sessionId);

    // $Concrete — hand-written subclass (production: auto-generated)
    public static final class $Concrete extends Ss7UssdIngressSbb {
        private final Map<String, Object> local = new ConcurrentHashMap<>();

        @Override public String getSessionId() {
            // 1. Thử đọc từ local cache
            Object v = local.get("sessionId");
            if (v instanceof String s) return s;
            // 2. Fallback: gọi cmpRead() để đọc từ container CMP store
            return (String) cmpRead(getter("getSessionId"));
        }

        @Override public void setSessionId(String v) {
            local.put("sessionId", v);                         // cache local
            cmpWrite(setter("setSessionId", String.class), v); // persist CMP
        }
    }
}
```

**Tại sao cần CMP (Container Managed Persistence)?**

- Session data (sessionId, msisdn, tier) cần sống qua nhiều event
- Không dùng field thường vì container SbbEntityPool có thể swap SBB instance
- CMP fields được container quản lý — đọc/ghi qua `cmpRead()`/`cmpWrite()`
- `$Concrete` là concrete implementation của abstract CMP accessor

### Initial Event Selector (IES)

```java
@InitialEventSelect(name = "ussd-session-convergence")
public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
    Object event = c.getEvent();
    if (event instanceof Ss7UssdBeginEvent e) {
        // Trả về convergence name = msisdn, isInitial = true
        return InitialEventSelectResult.forSession(e.getMsisdn(), true);
    }
    return InitialEventSelectResult.builder().initialEvent(false).build();
}
```

**IES dùng để làm gì?**

- Khi event mới (chưa có entity) đến → IES quyết định:
  - Event này có phải initial event không?
  - Nếu có → tạo entity mới, attach vào activity context
  - Tên convergence = msisdn → đảm bảo cùng subscriber = cùng session

---

## 6. @InjectRa — SBB gọi RA qua RaCommandPort

### Pattern

```java
public final class HttpServerSbb implements Sbb, SleeEventHandler {

    // 1. Khai báo field với @InjectRa
    @InjectRa(name = "httpCallbackRa")
    private volatile RaCommandPort httpCallbackPort;
    //             ^^^^^^^^^^^^^^ interface, không phải concrete class

    // 2. SBB gọi RA qua RaCommandPort.sendCommand()
    public void publishCallback(String sessionId, String responseText, String callbackUrl) {
        RaCommandPort port = this.httpCallbackPort;
        if (port == null) return;  // chưa được inject

        // 3. Tạo OutboundCommand — RA sẽ parse và xử lý
        port.sendCommand(new HttpCallbackCommand(sessionId, callbackUrl, responseText));
    }
}
```

### Điều gì xảy ra khi container inject?

```
1. sbbCreate() được gọi
2. Container scan class tìm field @InjectRa
3. Thấy: @InjectRa(name = "httpCallbackRa")
4. Lookup trong internal registry: Map<String, RaCommandPort>
   → Tìm key "httpCallbackRa" → HttpCallbackRaEndpoint instance
5. Set field: httpCallbackPort = httpCallbackEndpoint
6. Giờ SBB có thể gọi RA qua interface
```

### Tại sao dùng interface thay vì import RA class?

| Cách | Code | Vấn đề |
|------|------|--------|
| ❌ Import RA | `import com.microjainslee.ra.httpclient.HttpCallbackRaEndpoint` | Tight coupling, khó test |
| ✅ Interface | `@InjectRa RaCommandPort port` | Loose coupling, testable với mock |

SBB chỉ cần biết:
- `RaCommandPort` — interface chung cho mọi RA
- `HttpCallbackCommand` — OutboundCommand record chứa data

SBB KHÔNG cần biết RA implement thế nào (HTTP, gRPC, mock, ...).

---

## 7. RAs — cách RA kết nối vào JAIN SLEE app

### 3-port contract pattern

Mỗi RA có 2 class trong vendor-ras:

```
vendor-ras/ra-http-server/
├── HttpServerResourceAdaptor.java   ← Core logic (extends AbstractResourceAdaptor)
└── HttpServerRaEndpoint.java        ← 3-port adapter (RaEndpointPort + RaCommandPort)
```

### Cách RA được wire vào app

```java
// Trong EmbeddedUssdBootstrap.wireHttpServerRa():

// 1. Tạo RA delegate
HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
ra.setPort(8080);

// 2. Wrap vào Endpoint
HttpServerRaEndpoint ep = new HttpServerRaEndpoint(ra);

// 3. Register: container gọi ep.activate(bootstrapPort)
container.registerRa(ep, ep);
//                    ↑ RaEndpointPort  ↑ RaCommandPort
//                      (lifecycle)      (SBB outbound)
```

### Bên trong `container.registerRa()`

```
1. Lưu RaEndpointPort vào registry để quản lý lifecycle
2. Lưu RaCommandPort vào registry với key = ep.getRaName()
   → "http-server-ra" → ep (RaCommandPort)
3. Gọi ep.activate(bootstrapPort)
   → bootstrapPort = RaBootstrapContextImpl (bridge đến container)
   → RA gọi: delegate.setResourceAdaptorContext(bridgedContext)
   → RA gọi: delegate.raConfigure() → delegate.raActive()
   → HttpServerResourceAdaptor bắt đầu listen HTTP port 8080
```

### Collaborator interfaces — bridge giữa RA và app

RA không biết gì về app code. App cung cấp collaborator qua lambda:

```java
// Event Factory — RA gọi để tạo event (app-specific type)
ra.setBeginEventFactory((sessionId, msisdn, ussd, cbUrl) ->
    new HttpUssdBeginEvent(sessionId, msisdn, ussd, cbUrl));

// Activity Context Factory — RA gọi để tạo activity context
ra.setActivityContextFactory((sessionId, ctx) ->
    container.createActivityContext(sessionId));

// Session Preparer — RA gọi TRƯỚC khi fire event
ra.setSessionPreparer(this::prepareHttpSession);

// gRPC Upstream — RA gọi để gửi request ra ngoài
ra.setGrpcMenuUpstream((msisdn, ussd, sid) -> {
    var resp = gRpcStub.resolveMenu(req);
    return new GrpcMenuUpstreamResult() { ... };
});
```

### RA kết nối 2 chiều

```
┌─────────────┐                    ┌──────────────┐
│  App Code    │                    │  RA (vendor) │
│  (example)   │                    │              │
├─────────────┤                    ├──────────────┤
│             │  setEventFactory()  │              │
│  lambda ─────────────────────────→  RA dùng      │
│             │                    │  lambda để    │
│             │                    │  tạo event    │
│             │                    │              │
│             │  fireEvent()        │              │
│  SBB ←──────── event ────────────  RA gọi        │
│             │                    │  bootstrap    │
│             │                    │              │
│  SBB gọi    │  sendCommand()     │              │
│  @InjectRa ─────────────────────→  RA nhận       │
│             │                    │  command      │
└─────────────┘                    └──────────────┘
```

---

## 8. Full request flow — step by step

### Giả sử: ussd-client-simulator gửi `*123#` cho subscriber `251911000001`

```
═══════════════════════════════════════════════════════════════
STEP 1: Client gửi HTTP POST
═══════════════════════════════════════════════════════════════

POST http://127.0.0.1:8082/api/ussd/begin-callback?callbackUrl=http://127.0.0.1:9001/cb
Body: {"msisdn":"251911000001", "ussdString":"*123#"}

        ↓ HttpServerResourceAdaptor (đang listen port 8082)
        ↓ BeginHandler.handle(exchange)
        ↓ Đọc body → msisdn, ussdString
        ↓ Tạo sessionId = UUID.randomUUID()
        ↓
        ↓ sessionPreparer.prepare(sessionId, callbackUrl, aci)
        ↓   → EmbeddedUssdBootstrap.prepareHttpSession()
        ↓     → new HttpServerSbb()
        ↓     → container.registerSbb("HttpServer/sessionId", httpSbb)
        ↓     → container.attach(sessionId, httpLo)
        ↓     → wait for HttpServerSbb READY
        ↓
        ↓ beginEventFactory.createBeginEvent(sessionId, msisdn, ussd, callbackUrl)
        ↓   → new HttpUssdBeginEvent(sessionId, msisdn, ussd, callbackUrl)
        ↓
        ↓ endpoint().fireEvent(activityHandle, httpUssdBeginEvent)

═══════════════════════════════════════════════════════════════
STEP 2: EventRouter route event đến SBB
═══════════════════════════════════════════════════════════════

EventRouter.routeEvent(httpUssdBeginEvent, aci)
  → Lookup: HttpUssdBeginEvent.class → "HttpServerSbb"
  → pool.acquire("HttpServer/...")
    → Tìm thấy HttpServerSbb entity đã tạo ở step 1
  → HttpServerSbb.onEvent(httpUssdBeginEvent, aci)

═══════════════════════════════════════════════════════════════
STEP 3: HttpServerSbb.onHttpBegin()
═══════════════════════════════════════════════════════════════

HttpServerSbb.onEvent(HttpUssdBeginEvent, aci)
  → lookupTier("251911000001") → "GOLD" (từ profile)
  → container.acquireEntity("Ss7UssdIngress/sessionId", Ss7UssdIngressSbb.class)
    → Pool gọi $Concrete::new → new $Concrete()
    → Container gọi: sbbCreate() → @InjectRa injection → sbbActivate()
  → ss7Sbb.initCmp(sessionId, "251911000001", "GOLD")
    → setSessionId(sessionId) → local.put + cmpWrite
    → setMsisdn("251911000001")
    → setMenuTier("GOLD")
  → container.attach(sessionId, ss7Lo)
  → container.routeEvent(new Ss7UssdBeginEvent(...), aci)

        ↓ EventRouter lookup: Ss7UssdBeginEvent → "Ss7UssdIngress"
        ↓

═══════════════════════════════════════════════════════════════
STEP 4: Ss7UssdIngressSbb.onSs7Begin()
═══════════════════════════════════════════════════════════════

Ss7UssdIngressSbb.onEvent(Ss7UssdBeginEvent, aci)
  → Set session timer: 30 giây timeout
  → Tạo child GrpcClientSbb qua ChildRelation
    → grpcChildren.create() → new GrpcClientSbb()
    → sbbCreate() → @InjectRa grpcCommandPort injection
    → sbbActivate()
    → container.attach(sessionId, grpcLo)
  → container.routeEvent(new GrpcMenuRequestEvent(...), aci)

        ↓ EventRouter lookup: GrpcMenuRequestEvent → "GrpcClientSbb"
        ↓

═══════════════════════════════════════════════════════════════
STEP 5: GrpcClientSbb gọi gRPC RA
═══════════════════════════════════════════════════════════════

GrpcClientSbb.onEvent(GrpcMenuRequestEvent, aci)
  → @InjectRa grpcCommandPort.sendCommand(
        new GrpcMenuCommand(sessionId, msisdn, ussdString, aci))

  → GrpcMenuRaEndpoint.sendCommand(command)
    → delegate.requestMenu(sessionId, msisdn, ussdString, responseAci)
      → FIRE request event (GrpcMenuRequestEvent) lên session ACI
      → workerPool.submit(() -> doCall(...))

        ↓ doCall() chạy trên virtual thread:
        ↓
  → upstream.resolveMenu("251911000001", "*123#", sessionId)
    → gRPC stub gọi grpc-server-simulator:9090

═══════════════════════════════════════════════════════════════
STEP 6: grpc-server-simulator xử lý
═══════════════════════════════════════════════════════════════

gRPC ResolveMenu(sessionId, msisdn, ussdString)
  → MultiLevelMenuService.resolveMenu()
  → Session lookup: sessionId → existing session
  → "*123#" → root menu
  → Return: status=OK, text="Welcome!\n1. Balance\n2. Bundle\n3. Settings\n0. Exit"

        ↓ gRPC response
        ↓

  → eventFactory.createResponseEvent(sessionId, "OK", menuText, null)
    → new GrpcMenuResponseEvent(sessionId, "OK", menuText, null)
  → routeResponse(responseAci, grpcMenuResponseEvent)
    → MicroSleeContainer.routeEvent(event, responseAci)

═══════════════════════════════════════════════════════════════
STEP 7: GrpcClientSbb nhận response
═══════════════════════════════════════════════════════════════

EventRouter: GrpcMenuResponseEvent → "Ss7UssdIngress"
  → Ss7UssdIngressSbb.onGrpcResponse(grpcMenuResponseEvent, aci)
    → cancelSessionTimer()
    → Tạo USSD response text
    → container.routeEvent(new UssdResponseEvent(sessionId, responseText), aci)

═══════════════════════════════════════════════════════════════
STEP 8: HttpServerSbb gửi callback
═══════════════════════════════════════════════════════════════

EventRouter: UssdResponseEvent → "HttpServerSbb"
  → HttpServerSbb.onUssdResponse(ussdResponseEvent, aci)
    → publishCallback(sessionId, responseText, callbackUrl)
      → @InjectRa httpCallbackPort.sendCommand(
            new HttpCallbackCommand(sessionId, callbackUrl, responseText))
        → HttpCallbackClientRa.sendCallback(...)
          → HTTP POST callbackUrl với JSON body
            → ussd-client-simulator nhận callback
    → releaseSession(sessionId)
      → container.releaseEntity("Ss7UssdIngress/...")
      → container.releaseEntity("HttpServer/...")
      → callbackUrls.remove(sessionId)
```

---

## 9. Profile & Timer

### Profile — subscriber data

```java
// Trong EmbeddedUssdBootstrap.seedProfiles():
ProfileFacility facility = container.getProfileFacility();
facility.createProfileTable("ussdSubscribers");  // tạo table

ProfileLocalObject plo = facility.createProfile(
    "ussdSubscribers",            // table name
    "251911000001",                // profile name (= msisdn)
    UssdSubscriberProfile.class); // profile class

UssdSubscriberProfile sub = (UssdSubscriberProfile) plo.getProfile();
sub.setMsisdn("251911000001");
sub.setTier("GOLD");              // GOLD subscriber → premium menu
```

SBB lookup profile:
```java
// HttpServerSbb.lookupTier() → EmbeddedUssdBootstrap.tierFor(msisdn)
String tier = tiersByMsisdn.getOrDefault("251911000001", "STANDARD");
// → "GOLD"
```

### Timer — session timeout

```java
// Ss7UssdIngressSbb.onSs7Begin():
sessionTimerId = container.getTimerPort().setTimer(30_000L, self);

// 30 giây sau, nếu session chưa complete:
// → container.fireEvent(new TimerFiredEvent(timerId, self))
// → Ss7UssdIngressSbb.onTimer()
//   → EmbeddedUssdMain.runtime().failSession(sessionId, "session timeout")
//   → EmbeddedUssdMain.bootstrap().releaseSession(sessionId)

// Nếu session complete TRƯỚC timeout:
// → Ss7UssdIngressSbb.cancelSessionTimer()
//   → container.getTimerPort().cancelTimer(sessionTimerId)
```

---

## Tóm tắt: Các mối quan hệ chính

```
                        EmbeddedUssdMain
                              │
                     tạo MicroSleeContainer
                              │
                    EmbeddedUssdBootstrap.install()
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                      ▼
  registerSbbTypes()    registerRa()          mapEventToSbb()
        │                     │                      │
  SbbEntityPool         RaRegistry            EventRouter
  (factory map)         (lifecycle+commands)  (routing table)
        │                     │                      │
        │                     │                      │
  ┌─────┴─────┐         ┌─────┴─────┐         ┌─────┴──────────┐
  │ HttpServer │         │HTTP Server│         │HttpUssdBegin   │
  │ Sbb        │         │RA :8082   │         │→ HttpServerSbb │
  │            │         │           │         │                │
  │ Ss7Ussd    │         │HTTP Client│         │Ss7UssdBegin    │
  │ IngressSbb │         │RA (cb)    │         │→ Ss7UssdIngress│
  │            │         │           │         │                │
  │ GrpcClient │         │gRPC Client│         │GrpcMenuRequest │
  │ Sbb        │         │RA → :9090 │         │→ GrpcClientSbb │
  └───────────┘         └───────────┘         └────────────────┘
```

### Mỗi SBB entity = 1 virtual thread (parked)
- HttpServerSbb entity: parked VT, chờ event
- Ss7UssdIngressSbb entity: parked VT, chờ event
- GrpcClientSbb entity: parked VT, chờ event

Khi event đến → EventRouter unpark VT → SBB.onEvent() chạy → park lại.
100K sessions = 300K SBB entities = ~42 OS threads (Java 25 VT).
