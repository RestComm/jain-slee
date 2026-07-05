# App Guide: example-quarkus-helloworld-web (HelloWorld Web trên Quarkus)

> Hướng dẫn chi tiết app HelloWorld Web — HTTP ingress qua ra-http-server + JAIN SLEE SBB pipeline trên Quarkus 3.
> Tham khảo: `docs/vi/app-guide.md` (wiring pattern), `vendor-ras/ra-http-server/` (RA implementation).
> Last updated: 2026-07-07

---

## 1. Ví dụ này làm gì

Đây là một web app "Hello World" tối giản chạy trên Quarkus 3 + micro-jainslee. Nó có 2 port: Quarkus HTTP port 8080 phục vụ static HTML UI (từ `META-INF/resources/`), và `ra-http-server` port 8081 là HTTP ingress cho JAIN SLEE event pipeline. Khi browser gửi request đến port 8081, RA tạo `HttpWebRequestEvent`, route đến `HelloWorldSbb`, SBB log "Hello World" và hoàn tất session qua context bridge.

---

## 2. Cấu trúc thư mục

```
example/example-quarkus-helloworld-web/
├── pom.xml                                              ← Maven project, dependencies Quarkus + micro-jainslee + ra-http-server
├── src/main/resources/
│   ├── application.properties                          ← Quarkus port 8080, ra-http-server port 8081, microjainslee tuning
│   └── META-INF/resources/
│       └── index.html                                  ← Static HTML UI (served by Quarkus Undertow)
├── src/main/java/com/example/helloworld/quarkus/
│   ├── bootstrap/
│   │   ├── HelloWorldBootstrap.java                    ← @ApplicationScoped CDI bean: wire RA + SBB, implement HelloWorldContext
│   │   └── HelloWorldContext.java                      ← Interface bridge: container(), completeSession(), httpEntityId()
│   ├── sbbs/
│   │   └── HelloWorldSbb.java                          ← SBB: nhận HttpWebRequestEvent, log "Hello World", complete session
│   ├── events/
│   │   └── HttpWebRequestEvent.java                    ← App-defined event (@EventType "HttpWebRequest")
│   ├── command/
│   │   └── HelloWorldCommand.java                      ← Sealed outbound command hierarchy (HttpResponseCommand)
│   └── rest/
│       └── HealthResource.java                         ← Quarkus REST health endpoint: GET /health → {"status":"ok"}
```

---

## 3. pom.xml

> 📄 File: example/example-quarkus-helloworld-web/pom.xml

```xml
<dependencies>
    <!-- Quarkus REST + CDI + Undertow (serves static resources from META-INF/resources/) -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-rest</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-rest-jackson</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-arc</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-undertow</artifactId>
    </dependency>

    <!-- micro-jainslee core + API + APT -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>jainslee-core</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>jainslee-api</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>jainslee-apt</artifactId>
        <version>1.2.0-SNAPSHOT</version>
        <optional>true</optional>
    </dependency>

    <!-- ra-http-server: HTTP ingress (Vert.x), fires HttpWebRequestEvent -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>ra-http-server</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-junit5</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

> Lưu ý: example này KHÔNG dùng `adapter-quarkus`. Container (`MicroSleeContainer`) được inject trực tiếp — có thể do Quarkus CDI producer từ một dependency khác hoặc do example dùng phiên bản cũ hơn.

`application.properties`:

> 📄 File: example/example-quarkus-helloworld-web/src/main/resources/application.properties

```properties
# Quarkus HTTP — serves static web UI
quarkus.http.port=8080

# ra-http-server port — JAIN SLEE event ingress
http.ra.port=8081

# micro-jainslee core config
microjainslee.buffer-size=4096
microjainslee.prefer-virtual-threads=true
microjainslee.sbb-pool-min=16
microjainslee.sbb-pool-max=10000
```


---

## 4. Cách RA kết nối vào jainslee

Bootstrap trong `HelloWorldBootstrap` implement `HelloWorldContext` và thực hiện đúng thứ tự:

> 📄 File: example/example-quarkus-helloworld-web/src/main/java/com/example/helloworld/quarkus/bootstrap/HelloWorldBootstrap.java

### Bước 1: Start container

```java
@Inject
MicroSleeContainer container;

@PostConstruct
void init() {
    if (container.getState() != MicroSleeContainer.State.STARTED) {
        container.start();
    }
```

### Bước 2: Đăng ký SBB type — có collaborator qua constructor

```java
    container.registerSbbType(HelloWorldSbb.class,
            () -> new HelloWorldSbb(container, this));
```

`this` là `HelloWorldBootstrap` — nó implement `HelloWorldContext`. SBB nhận context qua constructor, tuân thủ quy tắc: collaborator qua **interface** (không static, không concrete class).

### Bước 3: Tạo IES dispatcher

```java
    container.createIesDispatcher();
```

### Bước 4: Map event → SBB

```java
    container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");
```

Chỉ có 1 event type duy nhất được map — mọi HTTP request (trừ `/health`) từ `ra-http-server` đều fire `HttpWebRequestEvent`.

### Bước 5: Wire HTTP RA

Khi `registerRa()` được gọi:
- `HttpServerRaEndpoint.activate(bootstrap)` → tạo `ResourceAdaptorContext` bridge từ `RaBootstrapPort` → gọi `delegate.raConfigure()` → `delegate.raActive()`
- `raActive()` khởi động Vert.x HTTP server, bind vào `host:port`, đăng ký `requestHandler` → gọi `this::route` cho mỗi request.

**Cleanup**:

```java
@PreDestroy
void shutdown() {
    if (httpEndpoint != null) {
        httpEndpoint.deactivate();   // → delegate.raInactive() → đóng Vert.x server
    }
    if (container.getState() == MicroSleeContainer.State.STARTED) {
        container.stop();
    }
}
```

### Collaborator pattern: HelloWorldContext

> 📄 File: example/example-quarkus-helloworld-web/src/main/java/com/example/helloworld/quarkus/bootstrap/HelloWorldContext.java

```java
public interface HelloWorldContext {
    MicroSleeContainer container();
    void completeSession(String sessionId, String responseText);
    void failSession(String sessionId, String message);
    String httpEntityId(String sessionId);

---

## 5. SBB — business logic từng file

### 5.1 HelloWorldSbb

> 📄 File: example/example-quarkus-helloworld-web/src/main/java/com/example/helloworld/quarkus/sbbs/HelloWorldSbb.java

**Mục đích**: SBB tối giản xử lý HTTP web requests từ `ra-http-server`. Log "Hello World" với User-Agent và hoàn tất session.

**Constructor**: nhận `MicroSleeContainer` và `HelloWorldContext` qua constructor (collaborator pattern).

```java
public HelloWorldSbb(MicroSleeContainer container, HelloWorldContext context) {
    this.container = container;
    this.context = context;
}
```

**Event handling**: Chỉ xử lý `HttpWebRequestEvent`:

```java
@Override
public void onEvent(SleeEvent event, ActivityContextInterface aci) {
    if (event instanceof HttpWebRequestEvent req) {
        onWebRequest(req, aci);
    }
}

private void onWebRequest(HttpWebRequestEvent event, ActivityContextInterface aci) {
    String userAgent = event.getUserAgent() != null
            ? event.getUserAgent() : "unknown";
    LOG.info("[HelloWorld] Hello World {}", userAgent);
    context.completeSession(event.getSessionId(),
            "Hello World " + userAgent);
}
```

**@InjectRa**: Có field `@InjectRa(name = "http-server-ra")` — sẵn sàng gửi `HttpResponseCommand` nhưng hiện tại SBB chỉ complete session qua context bridge.

### 5.2 App-defined events & commands

**HttpWebRequestEvent** (app-specific):

> 📄 File: example/example-quarkus-helloworld-web/src/main/java/com/example/helloworld/quarkus/events/HttpWebRequestEvent.java

```java
@EventType(name = "HttpWebRequest", vendor = "com.example.helloworld", version = "1.0")
public final class HttpWebRequestEvent implements SleeEvent {
    private final String sessionId, method, path, userAgent;
}
```

> Lưu ý: Đây là app-defined event, KHÔNG phải `com.microjainslee.ra.httpserver.events.HttpWebRequestEvent` từ RA. App tự định nghĩa event riêng.

**HelloWorldCommand**:

> 📄 File: example/example-quarkus-helloworld-web/src/main/java/com/example/helloworld/quarkus/command/HelloWorldCommand.java

```java
public sealed interface HelloWorldCommand extends OutboundCommand
        permits HelloWorldCommand.HttpResponseCommand {
    record HttpResponseCommand(String sessionId, int statusCode,
                               String contentType, String body)
            implements HelloWorldCommand { }

---

## 6. Events & Commands

| Event | Nguồn | SBB xử lý | Command gửi về RA |
|---|---|---|---|
| `HttpWebRequestEvent` (app-defined) | HTTP request → RA pipeline → EventRouter | `HelloWorldSbb` | (không gửi command, chỉ complete session qua `HelloWorldContext`) |

---

## 7. Call flow trace

```
┌─────────────┐     POST /api/ussd/begin  ┌──────────────────────────┐
│  Browser /   │ ────────────────────────▶ │  Vert.x HTTP Server      │
│  curl        │   port 8081               │  (HttpServerResource     │
└─────────────┘                           │   Adaptor.raActive)      │
                                           └────────────┬─────────────┘
                                                        │ HttpServerRequest
                                                        ▼
                                           ┌──────────────────────────┐
                                           │ HttpServerResourceAdaptor │
                                           │  .route(req)             │
                                           │  ├─ skip /health         │
                                           │  ├─ read body async      │
                                           │  └─ fireHttpRequest()    │
                                           │     ├─ UUID sessionId    │
                                           │     ├─ store response    │
                                           │     │  in pendingResponses│
                                           │     ├─ new HttpWebRequest│
                                           │     │  Event(sessionId,  │
                                           │     │  method,path,      │
                                           │     │  headers,body)     │
                                           │     └─ vertx.execute     │
                                           │        Blocking(() ->    │
                                           │        endpoint()        │
                                           │        .fireEvent(...))  │
                                           └────────────┬─────────────┘
                                                        │ HttpWebRequestEvent
                                                        ▼
                                           ┌──────────────────────────┐
                                           │  HttpServerRaEndpoint    │
                                           │  (bridgeContext)         │
                                           │  └─ bp.fireEvent(event,  │
                                           │      handle, null)       │
                                           └────────────┬─────────────┘
                                                        │
                                                        ▼
                                           ┌──────────────────────────┐
                                           │     EventRouter          │
                                           │  (Disruptor ring buffer) │
                                           │  └─ IES dispatcher       │
                                           │     "HttpWebRequestEvent"│
                                           │     → "HelloWorldSbb"    │
                                           └────────────┬─────────────┘
                                                        │ HttpWebRequestEvent
                                                        ▼
                                           ┌──────────────────────────┐
                                           │    HelloWorldSbb         │
                                           │  .onEvent(event, aci)    │
                                           │  ├─ log method + path    │
                                           │  ├─ log "Hello World"    │
                                           │  │  + userAgent          │
                                           │  └─ context              │
                                           │     .completeSession(    │
                                           │       sessionId, text)   │
                                           └──────────────────────────┘
```

}
```

}
```

Bootstrap implement interface này, cung cấp session storage (`ConcurrentHashMap`) và entity ID generation. SBB chỉ phụ thuộc vào interface — testable, mockable.


```java
    private void wireHttpRa() {

---

## 8. Cách chạy

```bash
cd example/example-quarkus-helloworld-web
mvn quarkus:dev
```

Sau khi start:
- **UI**: mở browser `http://localhost:8080/` — hiển thị static HTML
- **Health**: `curl http://localhost:8080/health` → `{"status":"ok"}`
- **JAIN SLEE pipeline**: POST đến port 8081:

```bash
curl -X POST http://localhost:8081/api/ussd/begin \
     -H 'Content-Type: application/json' \
     -d '{"msisdn":"84901234567","ussdString":"*101#"}'
```

Log sẽ hiển thị:

```
[HelloWorld] web request session=<uuid> POST /api/ussd/begin
[HelloWorld] Hello World curl/8.x.x
```

---

## 9. Test

Example này chưa có unit test riêng. Cấu trúc test có thể theo pattern từ `UssdDemoSmokeTest`:

- Tạo `MicroSleeContainer` với buffer nhỏ, không virtual thread.
- Set `httpRaPort = 0` để bind ephemeral port.
- Gọi `bootstrap.init()` rồi dùng `bootstrap.httpEndpoint.port()` để lấy port thật.
- Gửi HTTP request và poll session đến khi COMPLETED.

```bash
mvn test
```

        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpRaPort);   // default 8081, từ @ConfigProperty

        httpEndpoint = new HttpServerRaEndpoint(ra);
        httpEndpoint.setPort(httpRaPort);

        container.registerRa(httpEndpoint, httpEndpoint);
    }
```

`HttpServerRaEndpoint` implements cả `RaEndpointPort` và `RaCommandPort`. `getRaName()` trả về `"http-server-ra"` — khớp với `@InjectRa(name = "http-server-ra")` trong `HelloWorldSbb`.
