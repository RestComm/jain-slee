# App Guide: example-quarkus-sip (SIP Gateway)

> Hướng dẫn chi tiết app SIP Gateway — SIP proxy/registrar hoàn chỉnh với ICE/STUN/DNS SRV trên Quarkus 3 + micro-jainslee.
> Tham khảo: `docs/vi/app-guide.md` (wiring pattern), `vendor-ras/ra-sip-servlet/` (RA implementation).
> Last updated: 2026-07-07

---

## 1. Ví dụ này làm gì

Đây là một SIP Gateway hoàn chỉnh chạy trên Quarkus 3 + micro-jainslee. Nó lắng nghe UDP/TCP port 5060, nhận SIP messages từ mạng (Netty transport), phân loại thành các typed event (INVITE, REGISTER, BYE, OPTIONS, ICE candidate…), route đến SBB tương ứng để xử lý business logic, rồi gửi SIP response ngược lại qua cùng transport. Ứng dụng hỗ trợ DNS SRV resolution, STUN binding, và ICE candidate negotiation — sẵn sàng cho môi trường IMS/VoLTE/VoNR.

---

## 2. Cấu trúc thư mục

```
example/example-quarkus-sip/
├── pom.xml                                              ← Maven project, dependencies Quarkus + micro-jainslee + ra-sip-servlet
├── src/main/resources/
│   └── application.properties                          ← cấu hình Quarkus HTTP port (18080), microjainslee tuning, SIP RA config
├── src/main/java/com/example/sipgateway/
│   ├── bootstrap/
│   │   └── SipGatewayBootstrap.java                    ← @ApplicationScoped CDI bean: wire RA + SBB, @PostConstruct init, @PreDestroy cleanup
│   ├── sbbs/
│   │   ├── ProxySbb.java                               ← Proxy SBB: xử lý INVITE, BYE, ACK, CANCEL, OPTIONS, RESPONSE, SUBSCRIBE, NOTIFY…
│   │   ├── RegistrationSbb.java                        ← Registrar SBB: xử lý REGISTER, lưu AoR→Contact mapping, gửi 200 OK
│   │   └── IceNegotiationSbb.java                      ← ICE SBB: nhận IceCandidateEvent, chọn candidate tốt nhất, gửi SelectIceCandidate
│   ├── commands/
│   │   └── RegisterAorCommand.java                     ← App-defined outbound command (đăng ký/hủy AoR programmatically)
│   └── events/
│       └── RegistrationUpdatedEvent.java               ← App-defined event (@EventType), fire khi registration thay đổi
```

---

## 3. pom.xml

> 📄 File: example/example-quarkus-sip/pom.xml

```xml
<dependencies>
    <!-- Quarkus runtime: REST + CDI + JSON logging -->
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
        <artifactId>quarkus-logging-json</artifactId>
    </dependency>

    <!-- micro-jainslee core: MicroSleeContainer, EventRouter, SBB lifecycle -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>jainslee-core</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>
    <!-- micro-jainslee API: Sbb, SleeEvent, OutboundCommand, @InjectRa, @EventType -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>jainslee-api</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>
    <!-- micro-jainslee APT: compile-time annotation processing (@InjectRa, @EventType) -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>jainslee-apt</artifactId>
        <version>1.2.0-SNAPSHOT</version>
        <optional>true</optional>
    </dependency>
    <!-- adapter-quarkus: CDI producer cho MicroSleeContainer, auto-start -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>adapter-quarkus</artifactId>
        <version>1.1.0</version>
    </dependency>
    <!-- ra-sip-servlet: SIP RA — Netty transports + JAIN-SIP parser + STUN/ICE + DNS -->
    <dependency>
        <groupId>com.microjainslee</groupId>
        <artifactId>ra-sip-servlet</artifactId>
        <version>1.2.0-SNAPSHOT</version>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-junit5</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

`application.properties`:

> 📄 File: example/example-quarkus-sip/src/main/resources/application.properties

```properties
# Quarkus admin HTTP (SIP traffic uses SIP RA on its own port)
quarkus.http.port=18080
quarkus.http.test-port=0

# MicroSLEE container tuning
microjainslee.buffer-size=2048
microjainslee.prefer-virtual-threads=true
microjainslee.sbb-pool-min=16

---

## 4. Cách RA kết nối vào jainslee

Bootstrap trong `SipGatewayBootstrap` thực hiện đúng thứ tự 5 bước:

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/bootstrap/SipGatewayBootstrap.java

### Bước 1: Start container

```java
@Inject
MicroSleeContainer container;    // do adapter-quarkus produce

@PostConstruct
void init() {
    if (container.getState() != MicroSleeContainer.State.STARTED) {
        container.start();        // khởi động EventRouter (Disruptor ring buffer)
    }
```

### Bước 2: Đăng ký SBB types

```java
    container.registerSbbType(ProxySbb.class,          ProxySbb::new);
    container.registerSbbType(RegistrationSbb.class,    RegistrationSbb::new);
    container.registerSbbType(IceNegotiationSbb.class,  IceNegotiationSbb::new);
```

Mỗi SBB được đăng ký kèm factory (method reference). Factory được gọi mỗi khi cần entity mới từ pool. Các SBB này dùng no-arg constructor — không cần collaborator interface vì tất cả giao tiếp qua `@InjectRa`.

### Bước 3: Tạo IES dispatcher

```java
    container.createIesDispatcher();
```

Tạo `InitialEventSelectorDispatcher` — component quyết định SBB nào xử lý event đầu tiên của một activity (session). Dùng bản container-backed: entity được tạo qua `acquireEntity()` nên có đầy đủ lifecycle (`SbbContext`, `@InjectRa`, removal-bus cleanup).

### Bước 4: Map event → SBB

```java
    container.mapEventToSbb(SipInviteEvent.class,   "ProxySbb");
    container.mapEventToSbb(SipResponseEvent.class,  "ProxySbb");
    container.mapEventToSbb(SipRegisterEvent.class,  "RegistrationSbb");
    container.mapEventToSbb(IceCandidateEvent.class, "IceNegotiationSbb");
    container.mapEventToSbb(IceCompletedEvent.class, "IceNegotiationSbb");
    container.mapEventToSbb(IceFailedEvent.class,    "IceNegotiationSbb");
```

Mapping dùng tên class đơn giản (không fully-qualified). Tất cả SIP method khác (BYE, ACK, OPTIONS, SUBSCRIBE…) được xử lý bởi `ProxySbb` thông qua pattern matching trong `onEvent()` — không cần map riêng vì ProxySbb nhận mọi event và dispatch bằng `switch/case`.

### Bước 5: Tạo và register RA

```java
    SipRaConfig config = new SipRaConfig();
    config.setHost("0.0.0.0");
    config.setUdpPort(5060);
    config.setTcpPort(5060);
    config.setDnsEnabled(true);
    config.setStunServer("stun.l.google.com");
    config.setStunPort(3478);
    config.setIceEnabled(true);
    config.setIceKeepAliveSecs(30);

    SipServletResourceAdaptor ra = new SipServletResourceAdaptor();
    sipEndpoint = new SipServletRaEndpoint(ra);
    sipEndpoint.setConfig(config);

    container.registerRa(sipEndpoint, sipEndpoint);
```

`SipServletRaEndpoint` implements cả `RaEndpointPort` và `RaCommandPort` (cùng object cho cả hai interface). `registerRa()` gọi `endpoint.getRaName()` → trả về `"sip-servlet-ra"` — đây là giá trị khớp với `@InjectRa(name = "sip-servlet-ra")` trong tất cả SBB.

Khi `registerRa()` được gọi, container gọi `endpoint.activate(bootstrapPort)`:
- `SipServletRaEndpoint.activate()` → `delegate.raConfigure()` → `delegate.raActive()`
- `raActive()` khởi động `UdpTransport` và `TcpTransport` (Netty), wire `NettySipOutboundSender` mặc định, khởi động STUN client, ICE collector, và dialog sweeper.

**Cleanup** trong `@PreDestroy`:

```java
@PreDestroy
void shutdown() {
    if (sipEndpoint != null) {
        sipEndpoint.deactivate();    // gọi delegate.raInactive() → đóng Netty transports
    }
    if (container.getState() == MicroSleeContainer.State.STARTED) {
        container.stop();
    }
}
```

microjainslee.sbb-pool-max=4096
microjainslee.sbb-per-virtual-thread=true

# SIP RA configuration
sip.ra.host=0.0.0.0
sip.ra.udp-port=5060

---

## 5. SBB — business logic từng file

### 5.1 ProxySbb

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/ProxySbb.java

**Mục đích**: SIP proxy — xử lý tất cả SIP methods (INVITE, BYE, ACK, CANCEL, REGISTER, OPTIONS, SUBSCRIBE, NOTIFY, REFER, MESSAGE, INFO, UPDATE, PRACK, PUBLISH) và RESPONSE. Route dựa trên routing table domain.

**State**: routing table tĩnh (3 domain → next-hop mapping), không có state per-dialog (stateless proxy).

**Event handling** — dùng pattern matching với `switch/case`:

```java
@Override
public void onEvent(SleeEvent event, ActivityContextInterface aci) {
    switch (event) {
        case SipInviteEvent e      -> onInvite(e, aci);
        case SipByeEvent e         -> onBye(e);
        case SipAckEvent e         -> onAck(e);
        case SipCancelEvent e      -> onCancel(e);
        case SipOptionsEvent e     -> onOptions(e);
        case SipResponseEvent e    -> onResponse(e);
        // ... tất cả các SIP method khác và ICE event
        default -> LOG.trace("[ProxySbb] Unhandled: {}",
                    event.getClass().getSimpleName());
    }
}
```

**INVITE**: trích xuất domain từ `toUri` → lookup routing table → gửi `SendInvite`:

```java
void onInvite(SipInviteEvent e, ActivityContextInterface aci) {
    String domain = extractDomain(e.toUri());
    String nextHop = ROUTING_TABLE.getOrDefault(domain, e.toUri());
    send(new SendInvite(e.callId(), nextHop, e.fromUri(), e.sdpBody()));
}
```

**RESPONSE**: nếu provisional → forward nguyên trạng. Nếu success + có SDP → gửi `SendSdpUpdate`. Nếu final → gửi `SendResponse`.

**Các method khác** (BYE, ACK, CANCEL, OPTIONS, SUBSCRIBE…) → gửi `SendResponse(200, "OK")` hoặc command tương ứng.


### 5.2 RegistrationSbb

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/RegistrationSbb.java

**Mục đích**: SIP Registrar — RFC 3261 REGISTER handler. Lưu AoR → Contact bindings trong `ConcurrentHashMap`.

**State**: `ConcurrentHashMap<String, List<String>> registrations` — ánh xạ AoR → danh sách contact URIs.

**REGISTER handling**:

```java
public void onSipRegisterEvent(SipRegisterEvent event, ActivityContextInterface aci) {
    String aor = event.toUri();
    String contact = event.contactUri();
    int expires = event.expires();

    if (expires == 0) {
        // Unregister: xóa contact khỏi danh sách
        registrations.computeIfPresent(aor, (k, contacts) -> {
            contacts.remove(contact);
            return contacts.isEmpty() ? null : contacts;
        });
    } else {
        // Register: thêm contact vào danh sách (không trùng lặp)
        registrations.merge(aor,
                new ArrayList<>(List.of(contact)),
                (old, nu) -> {
                    if (!old.contains(contact)) old.add(contact);
                    return old;
                });
    }
    // Gửi 200 OK qua @InjectRa
    RaCommandPort port = this.sipRa;
    if (port != null) {
        port.sendCommand(new SendResponse(event.callId(), 200, "OK"));
    }
}
```

### 5.3 IceNegotiationSbb

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/IceNegotiationSbb.java

**Mục đích**: ICE Negotiation — RFC 8445 candidate selection. Nhận `IceCandidateEvent` từ SIP RA sau khi STUN binding hoàn tất, chọn candidate tối ưu bằng RFC 5245 priority formula.

**State**: không có state per-dialog.

**ICE candidate selection**: Sắp xếp candidate theo priority (host > srflx > relay). Gửi `SelectIceCandidate` về RA:

```java
List<Candidate> sorted = candidates.stream()
    .sorted(Comparator.comparingLong(
        IceNegotiationSbb::computeEffectivePriority).reversed())
    .toList();
Candidate best = sorted.get(0);
port.sendCommand(new SelectIceCandidate(
    event.callId(), best.address(), best.port(), best.type()));
```

**ICE completed/failed**: log thông tin, không gửi command.


---

## 6. Events & Commands

| Event | Nguồn | SBB xử lý | Command gửi về RA |
|---|---|---|---|
| `SipInviteEvent` | SIP RA (UDP/TCP inbound INVITE) | `ProxySbb` | `SendInvite(callId, nextHop, fromUri, sdp)` |
| `SipByeEvent` | SIP RA (UDP/TCP inbound BYE) | `ProxySbb` | `SendBye(callId)` |
| `SipAckEvent` | SIP RA | `ProxySbb` | `SendAck(callId)` |
| `SipCancelEvent` | SIP RA | `ProxySbb` | `SendCancel(callId)` |
| `SipOptionsEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipResponseEvent` | SIP RA (1xx/2xx/3xx-6xx) | `ProxySbb` | `SendResponse` hoặc `SendSdpUpdate` |
| `SipRegisterEvent` | SIP RA (inbound REGISTER) | `RegistrationSbb` | `SendResponse(callId, 200, "OK")` |
| `SipSubscribeEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipNotifyEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipReferEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 202, "Accepted")` |
| `SipMessageEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipInfoEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipUpdateEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipPrackEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `SipPublishEvent` | SIP RA | `ProxySbb` | `SendResponse(callId, 200, "OK")` |
| `IceCandidateEvent` | SIP RA (STUN binding xong) | `IceNegotiationSbb` | `SelectIceCandidate(callId, addr, port, type)` |
| `IceCompletedEvent` | SIP RA | `IceNegotiationSbb` | (không gửi command) |
| `IceFailedEvent` | SIP RA | `IceNegotiationSbb` | (không gửi command) |


**Gửi command**: tất cả qua `RaCommandPort` được inject với `@InjectRa(name = "sip-servlet-ra")`.


---

## 7. Call flow trace

### 7.1 REGISTER flow (UDP)

```
┌─────────────┐     UDP REGISTER      ┌──────────────────────┐
│  SIP Client  │ ────────────────────▶ │  Netty UdpTransport  │
│  (sipexer)   │   port 5060           │  (Netty Bootstrap)   │
└─────────────┘                       └──────────┬───────────┘
                                                 │ bytes[]
                                                 ▼
                                    ┌────────────────────────────┐
                                    │ SipServletResourceAdaptor  │
                                    │  .onRawMessage(bytes,      │
                                    │    peer, "UDP")            │
                                    │  ├─ StringMsgParser.parse  │
                                    │  ├─ deriveCallId()         │
                                    │  ├─ createActivityHandle() │
                                    │  ├─ classifier.classify()  │
                                    │  │  → SipRegisterEvent     │
                                    │  └─ bootstrapPort          │
                                    │     .fireEvent(event,      │
                                    │       handle, null)        │
                                    └────────────┬───────────────┘
                                                 │ SipRegisterEvent
                                                 ▼
                                    ┌────────────────────────────┐
                                    │     EventRouter            │
                                    │  (Disruptor ring buffer)   │
                                    │  └─ IES dispatcher lookup  │
                                    │     "SipRegisterEvent"     │
                                    │     → "RegistrationSbb"    │
                                    └────────────┬───────────────┘
                                                 │ SipRegisterEvent
                                                 ▼
                                    ┌────────────────────────────┐
                                    │    RegistrationSbb         │
                                    │  .onEvent(event, aci)      │
                                    │  ├─ store AoR→Contact      │
                                    │  │  in ConcurrentHashMap   │
                                    │  └─ sipRa.sendCommand(     │
                                    │      SendResponse(callId,  │
                                    │        200, "OK"))         │
                                    └────────────┬───────────────┘
                                                 │ SendResponse
                                                 ▼
                                    ┌────────────────────────────┐
                                    │  SipServletRaEndpoint      │
                                    │  .sendCommand(cmd)         │
                                    │  └─ delegate.sendOutbound()│
                                    └────────────┬───────────────┘
                                                 │
                                                 ▼
                                    ┌────────────────────────────┐
                                    │  NettySipOutboundSender    │
                                    │  .send(SendResponse)       │
                                    │  ├─ dialogs.find(callId)   │
                                    │  ├─ request.createResponse │
                                    │  │  (200, "OK")            │
                                    │  └─ transmit(response,     │
                                    │      "UDP", peer)          │
                                    │     → UdpTransport.send()  │
                                    └────────────┬───────────────┘
                                                 │ SIP/2.0 200 OK
                                                 ▼
┌─────────────┐     UDP 200 OK        ┌──────────────────────┐
│  SIP Client  │ ◀──────────────────── │  Netty UdpTransport  │
└─────────────┘                       └──────────────────────┘
```

### 7.2 INVITE flow (proxy routing)

```
SIP Client A                SIP Gateway (ProxySbb)         SIP Client B
    │                            │                              │
    │── INVITE (UDP:5060) ──────▶│                              │
    │                            │ onRawMessage → parse         │
    │                            │ classifier → SipInviteEvent  │
    │                            │ fireEvent → EventRouter      │
    │                            │ mapEventToSbb → ProxySbb     │
    │                            │ extractDomain(toUri)         │
    │                            │ lookup routing table         │
    │                            │ sendCommand(SendInvite)      │
    │                            │ NettySipOutboundSender       │
    │                            │─────────────────────────────▶│
    │                            │        INVITE (UDP)          │
    │                            │                              │
    │                            │◀─────────────────────────────│
    │                            │    200 OK (SDP)              │
    │                            │ onRawMessage → classifier    │
    │                            │ → SipResponseEvent → ProxySbb│
    │                            │ sendCommand(SendSdpUpdate)   │
    │◀─── 200 OK (SDP) ─────────│                              │
    │                            │                              │
    │─── ACK ───────────────────▶│                              │
    │                            │ SipAckEvent → ProxySbb       │
    │                            │ sendCommand(SendAck)         │

---

## 8. Cách chạy

```bash
cd example/example-quarkus-sip
mvn quarkus:dev
```

Sau khi start, log sẽ hiển thị:

```
=== SIP Gateway Ready — listening UDP:5060 TCP:5060 (DNS SRV, STUN/ICE enabled) ===
```

Gửi SIP request bằng `sipexer`:

```bash
# OPTIONS ping — kỳ vọng 200 OK
sipexer -mt OPTIONS -sd udp:127.0.0.1:5060

# REGISTER
sipexer -mt REGISTER -sd udp:127.0.0.1:5060 \
    -from sip:alice@example.com -to sip:alice@example.com \
    -contact sip:alice@192.168.1.5:5060

# Hoặc thủ công bằng netcat
printf 'OPTIONS sip:gw@127.0.0.1 SIP/2.0\r\nVia: SIP/2.0/UDP 127.0.0.1:9999;branch=z9hG4bK1\r\nMax-Forwards: 70\r\nTo: <sip:gw@x>\r\nFrom: <sip:me@x>;tag=1\r\nCall-ID: t1@x\r\nCSeq: 1 OPTIONS\r\nContent-Length: 0\r\n\r\n' | nc -u -w2 127.0.0.1 5060
```

Kiểm tra health (Quarkus admin port):

```bash
curl http://localhost:18080/health
# → {"status":"ok"}
```

---

## 9. Test

Hiện tại example này chưa có unit test riêng. Các smoke test pattern từ `example-quarkus` (`UssdDemoSmokeTest`) có thể áp dụng:

- Port configurable về 0 (ephemeral) để test không tranh chấp port.
- Bootstrap expose accessor cho endpoint để test lấy port thật.
- Field `@Inject`/`@ConfigProperty` để package-private → test set trực tiếp không cần CDI container.

Chạy toàn bộ test trong project:

```bash
mvn test
```

    │                            │─────────────────────────────▶│
    │                            │          ACK                 │
```


sip.ra.tcp-port=5060
sip.ra.dns-enabled=true
sip.ra.dns-cache-ttl-secs=300
sip.ra.stun-server=stun.l.google.com
sip.ra.stun-port=3478
sip.ra.ice-enabled=true
sip.ra.ice-keep-alive-secs=30
```
