# App Guide: example-spring-helloworld-web (HelloWorld Web trên Spring Boot)

> Hướng dẫn chi tiết app HelloWorld Web — HTTP ingress qua ra-http-server + JAIN SLEE SBB pipeline trên Spring Boot 3.
> Tham khảo: `docs/vi/app-guide.md` (wiring pattern), `vendor-ras/ra-http-server/` (RA implementation).
> Last updated: 2026-07-07

---

## 1. Ví dụ này làm gì

Đây là một web app "Hello World" tối giản chạy trên Spring Boot 3 + micro-jainslee. Nó có 2 port: Spring MVC port 8080 phục vụ static HTML UI (từ `src/main/resources/static/`) và REST endpoint, và `ra-http-server` port 8081 là HTTP ingress cho JAIN SLEE event pipeline. Khi browser gửi request đến port 8081, RA tạo `HttpWebRequestEvent` (từ `com.microjainslee.ra.httpserver.events`), route đến `HelloWorldSbb`, SBB log "Hello World". Khác với bản Quarkus, bản Spring dùng `adapter-springboot` và `SmartLifecycle` để quản lý lifecycle.

---

## 2. Cấu trúc thư mục

```
example/example-spring-helloworld-web/
├── pom.xml                                              ← Maven project, Spring Boot 3.3.0 + micro-jainslee + ra-http-server
├── src/main/resources/
│   ├── application.properties                          ← Spring server.port=8080, microjainslee tuning, http.ra.port=8081
│   └── static/
│       └── index.html                                  ← Static HTML UI (served by Spring MVC)
├── src/main/java/com/example/helloworld/spring/
│   ├── HelloWorldSpringApplication.java                ← @SpringBootApplication entry point
│   ├── HelloWorldContext.java                          ← @Component singleton: static container + session tracking
│   ├── config/
│   │   └── HelloWorldBootstrap.java                    ← @Configuration: define RA beans + SmartLifecycle wiring
│   ├── sbbs/
│   │   └── HelloWorldSbb.java                          ← SBB: nhận HttpWebRequestEvent, log "Hello World"
│   ├── events/
│   │   └── HttpWebRequestEvent.java                    ← App-defined event (@EventType "HttpWebRequest")
│   ├── command/
│   │   └── HelloWorldCommand.java                      ← Sealed outbound command hierarchy
│   └── rest/
│       └── HelloController.java                        ← @RestController: GET / → forward:/index.html, GET /health
```


---

## 3. pom.xml

> 📄 File: example/example-spring-helloworld-web/pom.xml

```xml
<dependencies>
    <!-- Spring Boot: Web MVC + Log4j2 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
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

    <!-- adapter-springboot: Spring bean producer cho MicroSleeContainer -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>adapter-springboot</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>

    <!-- ra-http-server: HTTP ingress (Vert.x) -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>ra-http-server</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>

    <!-- Jakarta annotation -->
    <dependency>
        <groupId>jakarta.annotation</groupId>
        <artifactId>jakarta.annotation-api</artifactId>
        <version>2.1.1</version>
    </dependency>
</dependencies>
```

`application.properties`:

---

## 4. Cách RA kết nối vào jainslee

Khác với Quarkus dùng `@PostConstruct`, Spring Boot dùng `SmartLifecycle` để đảm bảo thứ tự khởi động sau khi tất cả beans đã sẵn sàng.

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/config/HelloWorldBootstrap.java

### Cấu trúc @Configuration

`HelloWorldBootstrap` là `@Configuration` class, định nghĩa 3 beans:

```java
@Configuration
public class HelloWorldBootstrap {

    @Autowired
    private MicroSleeContainer container;    // do adapter-springboot produce

    @Autowired
    private HelloWorldContext helloContext;  // @Component singleton

    @Value("${http.ra.port:8081}")
    private int httpPort;
```

### Bean 1: HttpServerResourceAdaptor

```java
    @Bean
    public HttpServerResourceAdaptor httpServerRa() {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpPort);
        return ra;
    }
```

### Bean 2: HttpServerRaEndpoint

```java
    @Bean
    public HttpServerRaEndpoint httpServerEndpoint(HttpServerResourceAdaptor ra) {
        httpEndpoint = new HttpServerRaEndpoint(ra);
        httpEndpoint.setPort(httpPort);
        return httpEndpoint;
    }
```

### Bean 3: SmartLifecycle (wiring)

Đây là điểm khác biệt chính với Quarkus. `SmartLifecycle` với `getPhase() = Integer.MIN_VALUE + 200` đảm bảo chạy sớm trong quá trình khởi động:

```java
    @Bean
    public SmartLifecycle helloWorldLifecycle() {
        return new SmartLifecycle() {
            private volatile boolean running;

            @Override
            public boolean isAutoStartup() { return true; }

            @Override
            public int getPhase() { return Integer.MIN_VALUE + 200; }

            @Override
            public void start() {
                // Bước 1: Set container vào static context
                helloContext.setContainer(container);

                // Bước 2: Register SBB type
                container.registerSbbType(HelloWorldSbb.class,
                        () -> new HelloWorldSbb(container, helloContext));

                // Bước 3: Map event → SBB
                container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");

                // Bước 4: Create IES dispatcher
                container.createIesDispatcher();

                running = true;
            }

            @Override
            public void stop() {
                if (httpEndpoint != null) {
                    httpEndpoint.deactivate();
                }
                running = false;
            }

            @Override
            public boolean isRunning() { return running; }
        };
    }
```

### HelloWorldContext — static singleton pattern

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/HelloWorldContext.java

```java
@Component
public final class HelloWorldContext {
    private static volatile MicroSleeContainer container;
    private static volatile HelloWorldContext instance;

    public HelloWorldContext() {
        instance = this;
    }

    // Static accessors (used by SBBs)
    public static MicroSleeContainer container() {
        return require(container, "container");

---

## 5. SBB — business logic từng file

### 5.1 HelloWorldSbb

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/sbbs/HelloWorldSbb.java

**Mục đích**: SBB tối giản xử lý HTTP web requests từ `ra-http-server`. Log "Hello World".

**Constructor**: nhận `MicroSleeContainer` và `HelloWorldContext` qua constructor — giống hệt pattern của bản Quarkus:

```java
public HelloWorldSbb(MicroSleeContainer container, HelloWorldContext context) {
    this.container = container;
    this.context = context;
}
```

**Event handling**: Chỉ xử lý `HttpWebRequestEvent` (từ RA, không phải app-defined):

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
    // KHÔNG gọi context.completeSession() — chỉ log
}
```

> **Khác biệt với bản Quarkus**: SBB này KHÔNG gọi `context.completeSession()`. Nó chỉ log "Hello World". Bản Quarkus gọi `context.completeSession()` để lưu session state.

**@InjectRa**: Field `@InjectRa(name = "http-server-ra")` được khai báo nhưng không dùng trong logic hiện tại.

### 5.2 App-defined events & commands

**HttpWebRequestEvent** (app-specific):

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/events/HttpWebRequestEvent.java

```java
@EventType(name = "HttpWebRequest", vendor = "com.example.helloworld", version = "1.0")
public final class HttpWebRequestEvent implements SleeEvent {
    private final String sessionId, method, path, userAgent;
}
```

**HelloWorldCommand**:

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/command/HelloWorldCommand.java

```java
public sealed interface HelloWorldCommand extends OutboundCommand
        permits HelloWorldCommand.HttpResponseCommand {
    record HttpResponseCommand(String sessionId, int statusCode, String body)
            implements HelloWorldCommand { }
}
```

### 5.3 REST Controller

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/rest/HelloController.java

```java
@RestController
public final class HelloController {
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";      // forward đến static/index.html
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
```

    }

---

## 6. Events & Commands

| Event | Nguồn | SBB xử lý | Command gửi về RA |
|---|---|---|---|
| `HttpWebRequestEvent` (app-defined) | HTTP request → RA pipeline → EventRouter | `HelloWorldSbb` | (không gửi command, chỉ log) |

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
                                           │  └─ fireHttpRequest()    │
                                           │     ├─ UUID sessionId    │
                                           │     ├─ store response    │
                                           │     ├─ new HttpWebRequest│
                                           │     │  Event(sessionId,  │
                                           │     │  method,path,      │
                                           │     │  headers,body)     │
                                           │     └─ vertx.execute     │
                                           │        Blocking(() ->    │
                                           │        endpoint()        │
                                           │        .fireEvent(...))  │
                                           └────────────┬─────────────┘
                                                        │ HttpWebRequestEvent (RA)
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
                                           │  └─ log "Hello World"    │
                                           │     + userAgent          │
                                           └──────────────────────────┘
```

---

## 8. Cách chạy

```bash
cd example/example-spring-helloworld-web
mvn spring-boot:run
```

Hoặc build JAR rồi chạy:

```bash
mvn package -DskipTests
java -jar target/example-spring-helloworld-web-1.0.0-SNAPSHOT.jar
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
HelloWorld bootstrap complete (HTTP RA port=8081)
```

---

## 9. Test

Example này chưa có unit test riêng. Spring Boot cung cấp `@SpringBootTest` cho integration test:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldSmokeTest {
    @LocalServerPort
    int port;

    @Test
    void healthEndpointReturnsOk() {
        // Gọi GET /health → expect 200 + {"status":"ok"}
    }
}
```

Chạy test:

```bash
mvn test
```


    public static HelloWorldContext context() {
        return require(instance, "context");
    }

    public void setContainer(MicroSleeContainer c) { container = c; }

    public void completeSession(String sessionId, String response) {
        sessions.put(sessionId, new SessionRecord(sessionId, "COMPLETED", response, null));
    }
}
```

> **Khác biệt với Quarkus**: Spring version dùng **static singleton** pattern (`static volatile` fields + static accessors) thay vì interface injection qua constructor. Đây là pattern cũ hơn — app-guide khuyến nghị dùng interface collaborator pattern (như bản Quarkus) để testable hơn.

### Entry point

> 📄 File: example/example-spring-helloworld-web/src/main/java/com/example/helloworld/spring/HelloWorldSpringApplication.java

```java
@SpringBootApplication
public final class HelloWorldSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldSpringApplication.class, args);
    }
}
```


> 📄 File: example/example-spring-helloworld-web/src/main/resources/application.properties

```properties
spring.application.name=helloworld-spring-demo
server.port=8080

microjainslee.event-router.buffer-size=2048
microjainslee.event-router.prefer-virtual-threads=true
microjainslee.sbb-pool.min=16
microjainslee.sbb-pool.max=4096
microjainslee.sbb-pool.per-virtual-thread=true

http.ra.port=8081
```
