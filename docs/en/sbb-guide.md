# 📗 SBB Implementation Guide

> Guide to writing SBBs (Service Building Blocks) for micro-jainslee.
> Read first: [junior-dev-guide.md](junior-dev-guide.md) sections 1 and 4.
>
> Last updated: 2026-07-06

---

## 1. What is an SBB (30-second recap)

SBB = a Java class containing **business logic**, called by the runtime when events arrive. SBB:
- **Does not** open sockets, **does not** know what Netty/HTTP/SIP stack is.
- Receives events via `onEvent(SleeEvent, ActivityContextInterface)`.
- Sends data out **only** via `RaCommandPort.sendCommand(command)`.
- Each **entity** (one instance serving one session) runs sequentially on its own virtual thread → code in SBB **needs no locking**.

---

## 2. Standard template — stateless SBB

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/ProxySbb.java
```java
package com.example.myapp.sbbs;

import com.microjainslee.api.*;
import com.microjainslee.api.annotations.InjectRa;

public class ProxySbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(ProxySbb.class);

    // RA command port — runtime auto-injects when entity is created.
    // name MUST match RaEndpointPort.getRaName() of the RA (exact string!).
    @InjectRa(name = "sip-servlet-ra")
    private volatile RaCommandPort sipRa;

    public ProxySbb() { }   // no-arg constructor: required if SBB has @InitialEventSelect

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
        // clean up local state if any; entity will be reclaimed afterwards
    }
}
```

Registration in bootstrap (details in [app-guide.md](app-guide.md)):

> 📄 File: example/example-quarkus-sip/src/main/java/com/example/sipgateway/bootstrap/SipGatewayBootstrap.java
```java
container.registerSbbType(ProxySbb.class, ProxySbb::new);
container.createIesDispatcher();
container.mapEventToSbb(SipInviteEvent.class, "ProxySbb");
container.mapEventToSbb(SipByeEvent.class,    "ProxySbb");
```

---

## 3. Stateful SBB — session convergence via IES

When you need **one entity holding state for an entire session** (e.g., multi-step USSD dialog), declare `@InitialEventSelect`:

> 📄 File: example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/HttpServerSbb.java
```java
public class UssdSessionSbb implements Sbb, SleeEventHandler {

    // per-session state — safe because entity runs sequentially
    private String msisdn;
    private int step;

    public UssdSessionSbb() { }  // IES requires no-arg ctor (runs on temp instance)

    @InitialEventSelect(name = "ussd-convergence")
    public InitialEventSelectResult select(InitialEventSelectCondition c) {
        if (c.getEvent() instanceof UssdBeginEvent e) {
            // convergence key = msisdn → all events with same msisdn go to same entity
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

Spec semantics (§7.5.5):
- **initial=true, no entity yet** → runtime creates new entity, indexed by convergence key.
- **initial=false, entity exists** → routed to that entity (state preserved).
- **initial=false, no entity** → dropped (or buffered if event implements `SequencedEvent` and OutOfOrderBuffer is on).
- Entity removed → convergence key auto-freed (removal bus handles it, you do nothing).

**IES method note:** must be side-effect-free (called on temp instance, not real entity), 1 parameter `InitialEventSelectCondition`, returns `InitialEventSelectResult`.

---

## 4. CMP fields — declared state (optional)

If you want state stored/loaded via CMP store (for recovery/clustering), use abstract class + `@CmpField`:

> 📄 File: example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/Ss7UssdIngressSbb.java
```java
@SbbAnnotation(name = "UssdSession", vendor = "com.example", version = "1.0")
public abstract class UssdSessionSbb extends CmpBackedSbb implements SleeEventHandler {

    @CmpField("msisdn") public abstract String getMsisdn();
    @CmpField("msisdn") public abstract void setMsisdn(String v);
    // concrete class written by hand ($Concrete) or generated by jainslee-codegen
}
```

- Abstract SBB **cannot** auto-deploy from sbb-index (no concrete no-arg ctor) — runtime logs WARN and skips; you **must** `registerSbbType` with a factory creating `$Concrete`.
- Simple apps: skip CMP, use plain fields (section 3) — sufficient for most cases since entity is already serialized per session.

---

## 5. Lifecycle callbacks — which ones to override?

| Callback | When it runs | Typically used for |
|---|---|---|
| `setSbbContext(ctx)` | Once when object binds to entity | keeping `SbbContext` (timer, tracer…) |
| `sbbCreate()` | New entity created | init state, may throw `CreateException` |
| `sbbActivate()` | Before receiving first event | acquire lightweight resources |
| `sbbPassivate()` | Entity returns to pool | release resources |
| `sbbRemove()` | Entity removed | cancel timers, clean up state |
| `sbbRolledBack(ctx)` | Transaction rollback | compensation |
| `sbbExceptionThrown(...)` | Handler throws exception | log/alarm |

All are `default` no-ops — only override what you need. **Don't** do heavy/blocking work in callbacks.

---

## 6. Timer

> 📄 File: example/example-quarkus/src/main/java/com/example/ussddemo/quarkus/sbbs/GrpcClientSbb.java
```java
// set 30s timer — TimerFiredEvent delivered to this SBB local object
long timerId = container.getTimerPort().setTimer(30_000, sbbLocalObject);

@Override
public void onEvent(SleeEvent event, ActivityContextInterface aci) {
    if (event instanceof TimerFiredEvent t) {
        if (t.getSbbLocalObject() != self) return;  // timer for different entity
        // ... timeout logic ...
    }
}

// cancel when done — ALWAYS cancel in sbbRemove()
container.getTimerPort().cancelTimer(timerId);
```

---

## 7. Child SBB

When one SBB needs to "hire" another SBB to handle part of the work:

```java
ChildRelation children = ((SimpleSbbLocalObject) self).getChildRelation(
        "grpc", container.getChildRelationFactory(GrpcClientSbb.class));
SbbLocalObject child = children.create();
child.setPriority(5);
container.attach(sessionId, child);   // child also receives session events
```

Child is cascade-removed when parent is removed.

---

## 8. PR checklist

- [ ] SBB imports no Netty/Quarkus/Spring/RA-internal classes — only `com.microjainslee.api.*` + RA events/commands.
- [ ] No `routeEvent` on just-received event (anti-pattern #1).
- [ ] `@InjectRa` name matches `getRaName()` — grep to verify.
- [ ] Has handler for `ActivityEndedEvent` if SBB holds state per session.
- [ ] Timers cancelled in `sbbRemove()`.
- [ ] Has no-arg ctor if using `@InitialEventSelect`.
- [ ] Unit test: call `onEvent` directly; integration test: via real container (example: `SipEndToEndTest`).

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
