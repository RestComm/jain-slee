# 📕 Application Wiring Guide (Quarkus)

> Hướng dẫn wire SBB + RA thành một app micro-jainslee hoàn chỉnh trên Quarkus.
> Reference: `example/example-quarkus-sip` (SIP gateway) và `example/example-quarkus` (USSD).
>
> Last updated: 2026-07-06

---

## 1. Bức tranh tổng

Một app micro-jainslee = **3 mảnh + 1 bootstrap**:

```
┌──────────────────────── App (Quarkus) ────────────────────────┐
│  Bootstrap (@ApplicationScoped, @PostConstruct)               │
│    1. container.start()          ← nếu adapter chưa start     │
│    2. registerSbbType(...)       ← SBB nào tồn tại            │
│    3. createIesDispatcher()      ← session routing            │
│    4. mapEventToSbb(...)         ← event nào về SBB nào       │
│    5. registerRa(endpoint, cmd)  ← RA nào cung cấp event      │
└───────────────────────────────────────────────────────────────┘
```

Thứ tự 2 → 3 → 4 → 5 là bắt buộc: RA activate xong là event có thể đến ngay, mọi mapping phải sẵn sàng trước.

---

## 2. pom.xml

```xml
<dependencies>
    <!-- Quarkus extension của micro-jainslee: producer MicroSleeContainer + facilities -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>adapter-quarkus</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>
    <!-- RA bạn dùng -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>ra-sip-servlet</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-arc</artifactId>
    </dependency>
</dependencies>
```

`application.properties`:

```properties
# tuning container (đọc bởi adapter-quarkus deployment)
microjainslee.buffer-size=2048
microjainslee.prefer-virtual-threads=true
microjainslee.sbb-pool-max=4096

# config app tự định nghĩa
sip.udp.port=5060
```

---

## 3. Bootstrap hoàn chỉnh (SIP gateway)

```java
@ApplicationScoped
public final class SipGatewayBootstrap {

    @Inject
    MicroSleeContainer container;          // do adapter-quarkus produce

    @ConfigProperty(name = "sip.udp.port", defaultValue = "5060")
    int sipPort;                           // 0 = ephemeral (hữu ích cho test)

    private volatile SipServletRaEndpoint sipEndpoint;

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }

        // (2) SBB types — factory được gọi mỗi khi cần entity mới.
        //     Truyền collaborator qua constructor tại đây (KHÔNG static).
        container.registerSbbType(ProxySbb.class, ProxySbb::new);
        container.registerSbbType(RegistrationSbb.class, RegistrationSbb::new);

        // (3) IES — LUÔN dùng bản container-backed.
        container.createIesDispatcher();

        // (4) routing khai báo: event → SBB type (match cả class cha)
        container.mapEventToSbb(SipInviteEvent.class,   "ProxySbb");
        container.mapEventToSbb(SipByeEvent.class,      "ProxySbb");
        container.mapEventToSbb(SipResponseEvent.class, "ProxySbb");
        container.mapEventToSbb(SipRegisterEvent.class, "RegistrationSbb");

        // (5) RA
        SipRaConfig config = new SipRaConfig();
        config.setHost("0.0.0.0");
        config.setUdpPort(sipPort);
        config.setTcpPort(sipPort);
        config.setDialogIdleSecs(300);        // chống leak dialog bỏ rơi

        SipServletResourceAdaptor ra = new SipServletResourceAdaptor();
        sipEndpoint = new SipServletRaEndpoint(ra);
        sipEndpoint.setConfig(config);
        container.registerRa(sipEndpoint, sipEndpoint);
        // container sẽ activate RA ngay (đã STARTED) → mở UDP/TCP 5060
        // outbound sender mặc định (Netty) tự wire — SBB gửi SendResponse là chạy
    }

    @PreDestroy
    void shutdown() {
        if (sipEndpoint != null) sipEndpoint.deactivate();
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }
}
```

**Hết.** Không cần `acquireEntity` tay, không cần `attach` tay, không cần adapter IES tự chế — runtime lo phần "SLEE".

---

## 4. Truyền collaborator vào SBB đúng cách

SBB thường cần gọi service của app (session store, config…). Đưa qua **constructor trong factory**, kiểu interface:

```java
// app định nghĩa interface hẹp
public interface UssdDemoContext {
    String tierFor(String msisdn);
    void completeSession(String sessionId, String responseText);
}

// bootstrap implement nó, và truyền chính mình vào factory
container.registerSbbType(HttpServerSbb.class,
        () -> new HttpServerSbb(container, this));
```

Quy tắc:
- Tham số kiểu **interface** (`UssdDemoContext`), không phải class bootstrap cụ thể — để test tự mock được (bài học từ example-spring truyền `null`).
- ❌ Không dùng static singleton/holder trong SBB.
- SBB có `@InitialEventSelect` cần **thêm** no-arg ctor (IES temp instance) — các field collaborator để null trong ctor đó là chấp nhận được vì IES method không được dùng chúng.

---

## 5. Chạy thử

```bash
cd example/example-quarkus-sip
mvn quarkus:dev
```

Gõ SIP bằng [sipexer](https://github.com/miconda/sipexer) hoặc `nc`:

```bash
# OPTIONS ping — kỳ vọng 200 OK
sipexer -mt OPTIONS -sd udp:127.0.0.1:5060

# hoặc thủ công
printf 'OPTIONS sip:gw@127.0.0.1 SIP/2.0\r\nVia: SIP/2.0/UDP 127.0.0.1:9999;branch=z9hG4bK1\r\nMax-Forwards: 70\r\nTo: <sip:gw@x>\r\nFrom: <sip:me@x>;tag=1\r\nCall-ID: t1@x\r\nCSeq: 1 OPTIONS\r\nContent-Length: 0\r\n\r\n' | nc -u -w2 127.0.0.1 5060
```

USSD demo:

```bash
cd example/example-quarkus && mvn quarkus:dev
curl -X POST http://127.0.0.1:8080/api/ussd/begin \
     -H 'Content-Type: application/json' \
     -d '{"msisdn":"251911000001","ussdString":"*123#"}'
# → {"sessionId":"...","status":"PROCESSING"}
curl http://127.0.0.1:8080/api/ussd/sessions/<sessionId>
# → {"sessionId":"...","status":"COMPLETED","responseText":"USSD menu ..."}
```

---

## 6. Viết smoke test cho app (không cần CDI)

Bootstrap nên test được bằng plain JUnit — mẫu: `UssdDemoSmokeTest`:

```java
@BeforeEach
void setUp() {
    container = new MicroSleeContainer(MicroSleeConfiguration.builder()
            .eventRouterBufferSize(64).preferVirtualThreads(false).build());
    bootstrap = new UssdDemoBootstrap();
    bootstrap.container = container;        // field package-private → set trực tiếp
    bootstrap.sessionStore = new UssdSessionStore();
    bootstrap.httpPort = 0;                 // ephemeral port
    bootstrap.init();
    port = bootstrap.httpEndpoint().port(); // port thật sau bind
}

@Test
void flowCompletes() throws Exception {
    // POST begin → poll session endpoint đến khi COMPLETED (deadline 15s)
}
```

Nguyên tắc thiết kế để testable:
- Mọi port lắng nghe phải **configurable và nhận 0** (ephemeral).
- Bootstrap expose accessor cho endpoint (`httpEndpoint()`) để test lấy port thật.
- Field `@Inject`/`@ConfigProperty` để package-private → test set trực tiếp không cần CDI container.

---

## 7. Checklist app mới

- [ ] Bootstrap theo đúng thứ tự: start → registerSbbType → createIesDispatcher → mapEventToSbb → registerRa.
- [ ] Mỗi event type app dùng đều có `mapEventToSbb` (hoặc chủ đích dựa vào attach thủ công — hiếm).
- [ ] `@InjectRa` name khớp `getRaName()` của endpoint.
- [ ] Collaborator vào SBB qua constructor-interface, không static.
- [ ] `@PreDestroy` deactivate RA rồi mới stop container.
- [ ] Port configurable, có smoke test plain-JUnit chạy trong `mvn test`.
- [ ] Build được trong reactor: `mvn -Pexamples test` xanh trước khi mở PR.

---

## 8. GraalVM native (định hướng)

Mục tiêu là `mvn package -Dnative`. Trạng thái hiện tại và việc còn lại (tham khảo trước khi thử):

- `adapter-quarkus` đang record container ở `STATIC_INIT` trong khi `EventRouter` start Disruptor thread ngay trong constructor → **phải chuyển RUNTIME_INIT** trước khi native build được.
- Reflection cần đăng ký (`@InjectRa` field, IES method, JTA `Class.forName`) chưa có `ReflectiveClassBuildItem` trong deployment processor.
- Theo dõi các mục này trong `docs/gap-analysis.md` (mục native-readiness).

---

## Appendix: Real Source Tree

### SIP Gateway app (`example/example-quarkus-sip/`)

```
example/example-quarkus-sip/
├── pom.xml
├── src/main/resources/
│   └── application.properties                          ← microjainslee tuning + sip.udp.port
├── src/main/java/com/example/sipgateway/
│   ├── bootstrap/
│   │   └── SipGatewayBootstrap.java                    ← wire RA + SBB, @PostConstruct init
│   ├── sbbs/
│   │   ├── ProxySbb.java                               ← INVITE/BYE/response routing
│   │   ├── RegistrationSbb.java                        ← REGISTER handler + AOR management
│   │   └── IceNegotiationSbb.java                      ← ICE/STUN candidate negotiation
│   ├── commands/
│   │   └── RegisterAorCommand.java                     ← app-defined command (AOR registry)
│   └── events/
│       └── RegistrationUpdatedEvent.java               ← app-defined event (registration changed)
```

### USSD Demo app (`example/example-quarkus/`)

```
example/example-quarkus/
├── pom.xml
├── README.md
├── src/main/proto/
│   └── ussd_menu.proto                                 ← gRPC menu service definition
├── src/main/resources/
│   └── application.properties
├── src/main/java/com/example/ussddemo/quarkus/
│   ├── bootstrap/
│   │   ├── UssdDemoBootstrap.java                      ← wire: HTTP-RA + gRPC-RA + 3 SBB types
│   │   ├── UssdDemoContext.java                        ← collaborator interface (DI contract)
│   │   ├── UssdSessionStore.java                       ← session state store
│   │   └── UssdSubscriberProfile.java                  ← per-subscriber tier/profile
│   ├── sbbs/
│   │   ├── HttpServerSbb.java                          ← HTTP begin → start USSD flow
│   │   ├── Ss7UssdIngressSbb.java                      ← SS7 MAP begin → USSD flow (production ingress)
│   │   └── GrpcClientSbb.java                          ← gRPC menu lookup (child SBB)
│   ├── events/
│   │   ├── HttpUssdBeginEvent.java
│   │   ├── Ss7UssdBeginEvent.java
│   │   ├── GrpcMenuRequestEvent.java
│   │   ├── GrpcMenuResponseEvent.java
│   │   └── UssdResponseEvent.java
│   └── rest/
│       └── HealthResource.java                         ← health check + REST API
└── src/test/java/com/example/ussddemo/quarkus/
    └── bootstrap/
        └── UssdDemoSmokeTest.java                      ← plain-JUnit E2E smoke test
```
