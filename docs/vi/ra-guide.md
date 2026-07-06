# 📙 Resource Adaptor Implementation Guide

> Hướng dẫn viết Resource Adaptor (RA) theo **3-port contract** của micro-jainslee.
> Reference implementation đầy đủ nhất: `vendor-ras/ra-sip-servlet` — đọc song song với guide này.
>
> Last updated: 2026-07-06

---

## 1. RA làm gì?

RA là cầu nối **protocol ↔ SLEE**, và chỉ làm đúng 4 việc:

1. **Mở/đóng transport** theo lifecycle (`activate`/`deactivate`).
2. **Chiều vào**: bytes → parse → xác định activity (session id) → tạo typed event → `fireEvent`.
3. **Chiều ra**: nhận `OutboundCommand` từ SBB → encode → gửi ra mạng.
4. **Quản lý vòng đời activity**: session protocol kết thúc → `endActivity` + dọn state.

RA **không chứa business logic** — không quyết định trả lời gì, chỉ biết *cách* gửi/nhận.

---

## 2. 3-Port Contract (jainslee-api)

> 📄 File: jainslee-api/src/main/java/com/microjainslee/api/RaEndpointPort.java
```java
// Port 1 — lifecycle, container gọi
public interface RaEndpointPort {
    void activate(RaBootstrapPort bootstrap);  // mở transport, giữ bootstrap
    void deactivate();                          // đóng transport
    String getRaName();                         // tên duy nhất, vd "sip-servlet-ra"
}

// Port 2 — chiều SBB → RA
public interface RaCommandPort {
    void sendCommand(OutboundCommand command);
}

// Port 3 — chiều RA → SLEE, container cung cấp trong activate()
public interface RaBootstrapPort {
    ActivityHandle createActivityHandle(String id);              // tạo activity (ACI)
    void fireEvent(SleeEvent event, ActivityHandle h, Address a); // bắn event vào router
    default void endActivity(ActivityHandle handle) {}            // kết thúc activity
}
```

Một class thường implement cả `RaEndpointPort` + `RaCommandPort` (xem `SipServletRaEndpoint`) và delegate xuống RA core object.

Đăng ký với container:

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/bootstrap/SipGatewayBootstrap.java
```java
container.registerRa(endpoint, endpoint);
// container sẽ gọi endpoint.activate(bootstrap) khi start (hoặc ngay nếu đã start)
// và endpoint.deactivate() khi stop.
// RaCommandPort được index theo getRaName() để @InjectRa tra cứu.
```

---

## 3. Khung một RA hoàn chỉnh (rút từ ra-sip-servlet)

### 3.1. Định nghĩa event + command

```java
// Event: immutable record, implement SleeEvent
public record MyProtoRequestEvent(String sessionId, String payload) implements SleeEvent {}

// Command: sealed interface + records — SBB chỉ được gửi những command này
public sealed interface MyProtoCommand extends OutboundCommand
        permits SendReply, CloseSession {
    String sessionId();
}
public record SendReply(String sessionId, String body) implements MyProtoCommand {}
```

Sealed interface giúp `switch` pattern-matching exhaustive ở cả SBB lẫn RA.

### 3.2. Transport — LUÔN kèm peer address

> 📄 File: vendor-ras/ra-sip-servlet/src/main/java/com/microjainslee/ra/sipservlet/transport/SipMessageSink.java
```java
// Sink chiều vào: bytes + địa chỉ nguồn + tên transport.
// Thiếu peer address = không thể trả lời (bài học UDP của ra-sip-servlet).
@FunctionalInterface
public interface MessageSink {
    void onMessage(byte[] raw, InetSocketAddress peer, String transport);
}

// Transport interface hẹp — để sau này swap Netty → DPDK không đụng RA
public interface MyTransport {
    void start();
    void stop();
    String protocol();
    boolean send(byte[] data, InetSocketAddress target);
}
```

Quy tắc transport:
- **Stream (TCP/TLS)**: bắt buộc có frame decoder (message boundary — xem `SipTcpFrameDecoder` framing theo Content-Length). Netty đưa chunk tùy ý, không phải message.
- **Stream**: giữ registry `peer → Channel` để reply trên đúng connection (RFC 3261 §18.2.2 với SIP; nguyên tắc chung cho mọi protocol).
- **UDP**: reply bằng `DatagramPacket(data, peerAddress)` trên server channel.

### 3.3. RA core object

> 📄 File: vendor-ras/ra-sip-servlet/src/main/java/com/microjainslee/ra/sipservlet/SipServletResourceAdaptor.java
```java
public final class MyProtoResourceAdaptor {
    private RaBootstrapPort bootstrap;
    private final Map<String, MyTransport> transports = new ConcurrentHashMap<>();
    private final Map<String, ActivityHandle> sessions = new ConcurrentHashMap<>();
    // + SessionRegistry: peer/transport/lastActivity per session (xem DialogRegistry)

    public void setBootstrapPort(RaBootstrapPort bp) { this.bootstrap = bp; }

    public void raActive() {
        transports.put("UDP", new UdpMyTransport(config, this::onRawMessage));
        transports.values().forEach(MyTransport::start);
        // idle sweeper: session bỏ rơi phải bị expire (xem 3.5)
    }

    public void raInactive() {
        transports.values().forEach(MyTransport::stop);
        transports.clear();
        sessions.clear();
    }

    // ── chiều vào ──
    void onRawMessage(byte[] raw, InetSocketAddress peer, String transport) {
        MyMessage msg = parse(raw);
        String sid = msg.sessionId();
        ActivityHandle handle = sessions.computeIfAbsent(sid,
                id -> bootstrap.createActivityHandle(id));
        registry.recordInbound(sid, handle, msg, peer, transport); // nhớ peer để reply!
        bootstrap.fireEvent(classify(msg), handle, null);
        if (isSessionTerminating(msg)) {
            endSession(sid);   // sau khi đã fire event cuối cho SBB
        }
    }

    // ── chiều ra ──
    public void sendOutbound(MyProtoCommand cmd) {
        var session = registry.find(cmd.sessionId());
        byte[] wire = encode(cmd, session);
        transports.get(session.transport()).send(wire, session.peer());
    }

    // ── activity lifecycle ──
    public void endSession(String sid) {
        registry.remove(sid);
        ActivityHandle h = sessions.remove(sid);
        if (h != null && bootstrap != null) {
            bootstrap.endActivity(h);   // SBB nhận ActivityEndedEvent, ACI được thu hồi
        }
    }
}
```

### 3.4. Endpoint (3-port wrapper)

> 📄 File: vendor-ras/ra-sip-servlet/src/main/java/com/microjainslee/ra/sipservlet/SipServletRaEndpoint.java
```java
public final class MyProtoRaEndpoint implements RaEndpointPort, RaCommandPort {
    private final MyProtoResourceAdaptor delegate;

    @Override public String getRaName() { return "my-proto-ra"; }  // SBB @InjectRa dùng tên này

    @Override public void activate(RaBootstrapPort bootstrap) {
        delegate.setBootstrapPort(bootstrap);
        delegate.raConfigure();
        delegate.raActive();
    }

    @Override public void deactivate() {
        delegate.raInactive();
        delegate.raUnconfigure();
    }

    @Override public void sendCommand(OutboundCommand command) {
        if (command instanceof MyProtoCommand c) {
            delegate.sendOutbound(c);
        } else {
            LOG.warn("unknown command type: {}", command);
        }
    }
}
```

### 3.5. Session/dialog registry — chống leak (BẮT BUỘC)

Ba quy tắc, vi phạm cái nào cũng ra memory leak (đã xảy ra thật với `dialogs` map cũ của ra-sip-servlet):

1. **Có đường remove tự nhiên**: message kết thúc protocol (BYE, STR, FIN…) → `endSession`.
2. **Có idle sweeper**: `ScheduledExecutorService` daemon quét mỗi N giây, expire session im lặng quá `idleSecs` (xem `DialogRegistry.expireIdle`).
3. **`raInactive()` clear tất cả.**

### 3.6. Default outbound sender

RA phải **tự gửi được** mà không cần app cắm gì thêm. Đừng để `OutboundSender` là interface không có impl (bug cũ: mọi `SendResponse` bị drop âm thầm). Pattern đúng — auto-wire trong `raActive()`:

> 📄 File: vendor-ras/ra-sip-servlet/src/main/java/com/microjainslee/ra/sipservlet/SipServletResourceAdaptor.java
```java
if (outboundSender == null) {
    outboundSender = new NettyMyOutboundSender(config, registry, transports);
}
```

App vẫn có thể `setOutboundSender(...)` để override (test/custom stack).

---

## 4. Những điều RA KHÔNG được làm

| ❌ | Vì sao |
|---|---|
| Publish lại event ứng với command vừa nhận | Vòng lặp SBB ↔ RA vô hạn (bug thật của ra-grpc-client) |
| Gọi ngược business method của SBB | Đảo chiều dependency; SBB nhận input duy nhất qua event |
| Nuốt lỗi transport im lặng | Log WARN + metric; drop có chủ đích phải thấy được |
| Giữ `MicroSleeContainer` reference | RA chỉ được biết 3 port. Cần route event → `bootstrap.fireEvent` |
| Block thread transport (Netty event loop) để chờ SBB | Fire-and-forget; router đã async |

---

## 5. Test RA — 3 tầng (mẫu: ra-sip-servlet)

1. **Transport unit**: `EmbeddedChannel` của Netty — framing, fragmentation, keep-alive (`SipTcpFrameDecoderTest`).
2. **RA lifecycle**: fake `RaBootstrapPort` ghi lại `firedEvents`/`endedActivities`, bắn bytes trực tiếp vào `onRawMessage`, assert event + dialog state + endActivity (`SipRaDialogLifecycleTest`). Không cần mở port thật.
3. **E2E socket thật**: container thật + RA thật + SBB thật, gửi request qua `DatagramSocket`, nhận response thật (`SipEndToEndTest`). Đây là test chứng minh "wire" đúng — mỗi RA nên có một cái.

---

## 6. Trạng thái các vendor RA hiện tại

| RA | Transport | Outbound | Ghi chú |
|---|---|---|---|
| `ra-sip-servlet` | Netty UDP/TCP/TLS/SCTP + NIST parser | ✅ `NettySipOutboundSender` mặc định | Reference implementation. Có DialogRegistry + idle sweep + endActivity |
| `ra-diameter` | Netty TCP + jdiameter codec | ⚠️ chưa có default sender | Chưa có CER/CEA, DWR/DWA peer state machine — chưa interop peer thật |
| `ra-http-server` | JDK HttpServer | ✅ | Đủ cho demo/USSD |
| `ra-http-client` | (app cắm) | — | Shell — callback delivery do app cung cấp |
| `ra-grpc-client` / `ra-grpc-server` | (app cắm `GrpcMenuUpstream`) | — | Shell — không có io.grpc dependency; app tự đem stub |

Khi viết RA mới: copy cấu trúc `ra-sip-servlet` (package `transport/`, `collab/`, `command/`, `event/`), thay protocol.

---

## Appendix: Real Source Tree

### `vendor-ras/ra-sip-servlet/` — Reference RA

<p align="center"><img src="../images/ra-guide-vi-1.svg" width="800"/></p>
