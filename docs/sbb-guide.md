# 📗 SBB Implementation Guide

> Hướng dẫn viết SBB (Service Building Block) cho micro-jainslee.
> Đọc trước: [junior-dev-guide.md](junior-dev-guide.md) mục 1 và 4.
>
> Last updated: 2026-07-06

---

## 1. SBB là gì (nhắc lại 30 giây)

SBB = một class Java chứa **logic nghiệp vụ**, được runtime gọi khi có event. SBB:
- **Không** mở socket, **không** biết Netty/HTTP/SIP stack là gì.
- Nhận event qua `onEvent(SleeEvent, ActivityContextInterface)`.
- Gửi dữ liệu ra ngoài **duy nhất** qua `RaCommandPort.sendCommand(command)`.
- Mỗi **entity** (một instance phục vụ một session) chạy tuần tự trên virtual thread riêng → code trong SBB **không cần lock**.

---

## 2. Template chuẩn — SBB stateless

```java
package com.example.myapp.sbbs;

import com.microjainslee.api.*;
import com.microjainslee.api.annotations.InjectRa;

public class ProxySbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(ProxySbb.class);

    // Command port của RA — runtime tự inject khi tạo entity.
    // name PHẢI trùng RaEndpointPort.getRaName() của RA (từng ký tự!).
    @InjectRa(name = "sip-servlet-ra")
    private volatile RaCommandPort sipRa;

    public ProxySbb() { }   // no-arg constructor: bắt buộc nếu SBB có @InitialEventSelect

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        switch (event) {
            case SipInviteEvent e -> onInvite(e);
            case SipByeEvent e    -> onBye(e);
            case ActivityEndedEvent e -> onDialogEnded(e);
            default -> LOG.trace("Unhandled: {}", event.getClass().getSimpleName());
        }
    }

    private void onInvite(SipInviteEvent e) {
        // ... business logic ...
        sipRa.sendCommand(new SendResponse(e.callId(), 200, "OK"));
    }

    private void onBye(SipByeEvent e) {
        sipRa.sendCommand(new SendResponse(e.callId(), 200, "OK"));
    }

    private void onDialogEnded(ActivityEndedEvent e) {
        // dọn state cục bộ nếu có; entity sẽ được thu hồi sau đó
    }
}
```

Đăng ký trong bootstrap (chi tiết ở [app-guide.md](app-guide.md)):

```java
container.registerSbbType(ProxySbb.class, ProxySbb::new);
container.createIesDispatcher();
container.mapEventToSbb(SipInviteEvent.class, "ProxySbb");
container.mapEventToSbb(SipByeEvent.class,    "ProxySbb");
```

---

## 3. SBB stateful — session convergence bằng IES

Khi cần **một entity giữ state cho cả session** (VD: USSD dialog nhiều bước), khai báo `@InitialEventSelect`:

```java
public class UssdSessionSbb implements Sbb, SleeEventHandler {

    // state per-session — an toàn vì entity chạy tuần tự
    private String msisdn;
    private int step;

    public UssdSessionSbb() { }  // IES cần no-arg ctor (chạy trên temp instance)

    @InitialEventSelect(name = "ussd-convergence")
    public InitialEventSelectResult select(InitialEventSelectCondition c) {
        if (c.getEvent() instanceof UssdBeginEvent e) {
            // convergence key = msisdn → mọi event cùng msisdn về cùng entity
            return InitialEventSelectResult.forSession(e.msisdn(), true);   // initial
        }
        if (c.getEvent() instanceof UssdContinueEvent e) {
            return InitialEventSelectResult.forSession(e.msisdn(), false);  // non-initial
        }
        return InitialEventSelectResult.builder().initialEvent(false).build();
    }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) { ... }
}
```

Ngữ nghĩa spec (§7.5.5):
- **initial=true, chưa có entity** → runtime tạo entity mới, index theo convergence key.
- **initial=false, đã có entity** → route về entity đó (state còn nguyên).
- **initial=false, chưa có entity** → drop (hoặc buffer nếu event implement `SequencedEvent` và OutOfOrderBuffer bật).
- Entity bị remove → convergence key tự giải phóng (removal bus lo, bạn không phải làm gì).

**Lưu ý IES method:** phải side-effect-free (được gọi trên temp instance, không phải entity thật), 1 tham số `InitialEventSelectCondition`, trả `InitialEventSelectResult`.

---

## 4. CMP fields — state khai báo (tùy chọn)

Nếu muốn state được store/load qua CMP store (phục vụ recovery/cluster), dùng abstract class + `@CmpField`:

```java
@SbbAnnotation(name = "UssdSession", vendor = "com.example", version = "1.0")
public abstract class UssdSessionSbb extends CmpBackedSbb implements SleeEventHandler {

    @CmpField("msisdn") public abstract String getMsisdn();
    @CmpField("msisdn") public abstract void setMsisdn(String v);
    // concrete class do bạn viet tay ($Concrete) hoặc jainslee-codegen sinh
}
```

- SBB abstract **không thể** auto-deploy từ sbb-index (không có no-arg concrete ctor) — runtime sẽ log WARN và bỏ qua; bạn **phải** `registerSbbType` với factory tạo `$Concrete`.
- App đơn giản: bỏ qua CMP, dùng field thường (mục 3) — đủ cho hầu hết trường hợp vì entity đã được serialize hóa theo session.

---

## 5. Lifecycle callbacks — cái nào cần override?

| Callback | Khi nào chạy | Thường dùng để |
|---|---|---|
| `setSbbContext(ctx)` | 1 lần khi object gắn vào entity | giữ `SbbContext` (timer, tracer…) |
| `sbbCreate()` | entity mới được tạo | init state, có thể throw `CreateException` |
| `sbbActivate()` | trước khi nhận event đầu | acquire tài nguyên nhẹ |
| `sbbPassivate()` | entity về pool | nhả tài nguyên |
| `sbbRemove()` | entity bị remove | cancel timer, dọn state |
| `sbbRolledBack(ctx)` | transaction rollback | bù trừ (compensation) |
| `sbbExceptionThrown(...)` | handler ném exception | log/alarm |

Tất cả là `default` no-op — chỉ override cái cần. **Đừng** làm việc nặng/blocking dài trong callbacks.

---

## 6. Timer

```java
// đặt timer 30s — TimerFiredEvent sẽ được deliver về SBB local object này
long timerId = container.getTimerPort().setTimer(30_000, sbbLocalObject);

@Override
public void onEvent(SleeEvent event, ActivityContextInterface aci) {
    if (event instanceof TimerFiredEvent t) {
        if (t.getSbbLocalObject() != self) return;  // timer của entity khác
        // ... timeout logic ...
    }
}

// hủy khi xong việc — LUÔN cancel trong sbbRemove()
container.getTimerPort().cancelTimer(timerId);
```

---

## 7. Child SBB

Khi một SBB cần "thuê" SBB khác xử lý một phần việc:

```java
ChildRelation children = ((SimpleSbbLocalObject) self).getChildRelation(
        "grpc", container.getChildRelationFactory(GrpcClientSbb.class));
SbbLocalObject child = children.create();
child.setPriority(5);
container.attach(sessionId, child);   // child cũng nhận event của session
```

Child bị cascade-remove khi parent remove.

---

## 8. Checklist trước khi mở PR

- [ ] SBB không import Netty/Quarkus/Spring/RA-internal class nào — chỉ `com.microjainslee.api.*` + event/command của RA.
- [ ] Không `routeEvent` lại event vừa nhận (anti-pattern #1).
- [ ] `@InjectRa` name khớp `getRaName()` — grep để chắc chắn.
- [ ] Có handler cho `ActivityEndedEvent` nếu SBB giữ state theo session.
- [ ] Timer được cancel trong `sbbRemove()`.
- [ ] Có no-arg ctor nếu dùng `@InitialEventSelect`.
- [ ] Unit test: gọi `onEvent` trực tiếp; integration test: qua container thật (mẫu: `SipEndToEndTest`).

---

## Appendix: Real Source Tree — SBB files in examples

### SIP Gateway SBBs (`example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/`)

| File | Role | Key patterns |
|---|---|---|
| [`ProxySbb.java`](../../example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/ProxySbb.java) | Stateless request routing | `@InjectRa(name="sip-servlet-ra")`, `switch` on event type, `sendCommand(SendResponse/...)` |
| [`RegistrationSbb.java`](../../example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/RegistrationSbb.java) | REGISTER handler + AOR store | `RegisterAorCommand`, `RegistrationUpdatedEvent`, maintains in-memory AOR map |
| [`IceNegotiationSbb.java`](../../example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/IceNegotiationSbb.java) | ICE/STUN candidate exchange | `IceCandidateEvent`, `IceCompletedEvent`, `IceFailedEvent`, `StartIce` command |

### USSD Demo SBBs (`example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/`)

| File | Role | Key patterns |
|---|---|---|
| [`HttpServerSbb.java`](../../example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/HttpServerSbb.java) | HTTP-to-USSD adapter | `@InitialEventSelect` convergence by msisdn, `UssdDemoContext` collaborator injected via constructor |
| [`Ss7UssdIngressSbb.java`](../../example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/Ss7UssdIngressSbb.java) | SS7 MAP USSD ingress | Production ingress path, `@InitialEventSelect` with different convergence name |
| [`GrpcClientSbb.java`](../../example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/GrpcClientSbb.java) | gRPC menu lookup (child SBB) | Child SBB pattern: `childRelation.create()`, `container.attach()`, handles `GrpcMenuRequestEvent`/`GrpcMenuResponseEvent` |

### Bootstrap wiring reference

| File | Role |
|---|---|
| [`SipGatewayBootstrap.java`](../../example/example-quarkus-sip/src/main/java/com/example/sipgateway/bootstrap/SipGatewayBootstrap.java) | registerSbbType → createIesDispatcher → mapEventToSbb → registerRa |
| [`UssdDemoBootstrap.java`](../../example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/bootstrap/UssdDemoBootstrap.java) | Same pattern, with collaborator injection via constructor factory |
