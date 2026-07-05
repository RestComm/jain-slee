# 🚀 Micro-JAINSLEE Junior Developer Guide

> **Onboarding document for new developers working with micro-jainslee.**
>
> Last updated: 2026-07-06 | Maintainer: nhanth87
>
> This document set has 4 files — read in order:
> 1. **This file** — concepts, architecture, build, event flow.
> 2. [sbb-guide.md](sbb-guide.md) — writing SBBs (service logic).
> 3. [ra-guide.md](ra-guide.md) — writing Resource Adaptors (3-port contract).
> 4. [app-guide.md](app-guide.md) — wiring SBB + RA into a complete Quarkus app.

---

## 1. What is micro-jainslee?

**micro-jainslee** is a **lightweight, embeddable JAIN SLEE 1.1 runtime** rewritten from scratch on Java 25:

- Removes all heavyweight parts of classic JSLEE: JBoss/WildFly, JMX management, deployable-unit XML, complex profile management.
- Keeps the core value: **event-driven SBB model, Activity Context, event routing, timer facility, RA contract**.
- Runs in 3 modes: pure Java 25 embedded, **Quarkus (primary target — for GraalVM native builds)**, Spring Boot.

> ⚠️ **Current direction: Quarkus-only focus.** Spring/embedded are kept compile-green but no further investment. Long-term, Netty transport will be replaced by a DPDK datapath (C++/Rust) pushing events into native Java apps — therefore **all transport must sit behind an interface** (see `SipTransport`).

### 4 mandatory concepts

| Concept | What it is | Example |
|---|---|---|
| **Event** | An immutable event from the network or internal. Implements `SleeEvent`. | `SipInviteEvent`, `HttpUssdBeginEvent`, `TimerFiredEvent` |
| **Activity / ACI** | A protocol "session" (SIP dialog, USSD session…). SBBs attach to `ActivityContextInterface` to receive events for that session. | SIP dialog Call-ID = 1 activity |
| **SBB** | Service Building Block — business logic, handles events. Implements `Sbb` + `SleeEventHandler`. | `ProxySbb` handles INVITE |
| **RA** | Resource Adaptor — protocol ↔ SLEE bridge. Receives bytes from network → fires events; receives commands from SBBs → sends to network. | `ra-sip-servlet`, `ra-diameter` |

### One-way event flow (never reverses)

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

Golden rule: **SBBs never open sockets, RAs never contain business logic.**

---

## 2. Repo structure

```
micro-jainslee/
├── jainslee-api/        # Pure Java 25 API (Sbb, SleeEvent, ACI, 3-port contract…)
├── jainslee-core/       # Engine: MicroSleeContainer, EventRouter, entity pool, IES
├── jainslee-ra-spi/     # RA SPI in classic JSLEE 1.1 style (AbstractResourceAdaptor…)
├── jainslee-scheduler/  # HashedWheelTimer for SLEE timers
├── jainslee-apt/        # Annotation processor generating sbb-index.properties
├── jainslee-codegen/    # Javassist-generated concrete SBB for CMP fields
├── jainslee-tx/         # Narayana JTA (optional)
├── jainslee-cluster/    # Infinispan/JGroups (optional)
├── jainslee-adapter/
│   ├── adapter-quarkus/     # ★ Quarkus extension (runtime + deployment)
│   ├── adapter-springboot/  # (low priority)
│   └── adapter-jakartaee/   # (low priority)
├── vendor-ras/          # Bundled RAs: ra-sip-servlet, ra-diameter, ra-http-*, ra-grpc-*
└── example/             # Sample apps: example-quarkus (USSD), example-quarkus-sip (SIP GW)…
```

**Architectural constraints (must not violate):**
- `jainslee-api` and `jainslee-core`: **zero framework dependency** (no Spring/Quarkus imports).
- Minimize reflection in core (targeting GraalVM native).
- App code only depends on `jainslee-api` + `jainslee-core` + RA modules — never touch internals.

---

## 3. Build & run

```bash
# Requirements: JDK 25 (mise.toml declares zulu-25), Maven 3.9+
mvn clean install              # build 24 runtime modules
mvn -Pexamples clean install   # build with 4 sample apps
mvn -Pexamples test            # run all tests (400+)

# Run SIP gateway sample (Quarkus dev mode)
cd example/example-quarkus-sip && mvn quarkus:dev

# Run USSD demo
cd example/example-quarkus && mvn quarkus:dev
```

---

## 4. Event routing — how does an SBB receive events?

This is the most important part of the runtime. When an RA fires an event on an ACI, `MicroSleeContainer.routeEvent()` decides which SBB receives it, in order:

### 4.1. `mapEventToSbb()` — the recommended primary approach

```java
container.registerSbbType(ProxySbb.class, ProxySbb::new); // register type + factory
container.createIesDispatcher();                          // enable convergence routing
container.mapEventToSbb(SipInviteEvent.class, "ProxySbb"); // event → SBB type
```

When `SipInviteEvent` arrives on an ACI:
1. If the ACI **already has** an SBB of the right type attached → done (no duplicate creation).
2. If not → ask the **IES dispatcher** (section 4.2) to find/create entity by convergence name, then attach.
3. If no IES binding → create entity with identity `Type/aciName` (1 entity / activity).

Mapping matches **parent classes too** — mapping `SipEvent.class` catches all child events.

### 4.2. Initial Event Selector (IES) — session convergence

IES answers *"which session/entity does this event belong to?"* (JSLEE 1.1 §7.5). SBBs declare it via annotation:

```java
@InitialEventSelect(name = "ussd-session-convergence")
public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
    if (c.getEvent() instanceof Ss7UssdBeginEvent e) {
        // All events with the same msisdn converge to the SAME SBB entity
        return InitialEventSelectResult.forSession(e.getMsisdn(), true);
    }
    return InitialEventSelectResult.builder().initialEvent(false).build();
}
```

- **Always use `container.createIesDispatcher()`** to bind. ❌ Never write your own `SbbEntityPool` adapter — DIY adapters create raw entities skipping lifecycle (no `@InjectRa`, no CMP, no cleanup) and are the classic bug source in this repo.
- SBBs with `@InitialEventSelect` **must have a no-arg constructor** (IES runs on a temp instance).

### 4.3. Fallback

No mapping, empty ACI → selects the **earliest-registered SBB whose `EventMask` accepts the event** (programmatic registration takes priority over auto-deploy from sbb-index). This fallback only works for 1-SBB apps — real apps should use 4.1.

### 4.4. What the router guarantees

- Events for the **same entity** run sequentially on a **dedicated virtual thread** (no locking needed in SBBs).
- SBBs attached to the same ACI receive events in **descending priority order** (`localObject.setPriority(n)`).
- SBB exceptions **don't kill the router** — handed to `ErrorHandlingPolicy` + log; disruptor always stays alive.
- Entity removed while events are in queue → events dropped safely (counted in `missingEntityCount`).

---

## 5. Lifecycle

### SBB entity
```
registerSbbType ──► acquireEntity/IES allocate ──► setSbbContext → sbbCreate
   → sbbPostCreate → sbbActivate → READY ──(events)──► remove() → sbbRemove
```
Activation runs **async** on the entity thread. If you need certainty that READY is reached before calling methods directly (outside the event path): `localObject.awaitReady(5, SECONDS)`. Events via the router **don't need** waiting — the entity queue guarantees ordering.

### RA (3-port)
```
container.registerRa(endpoint, commandPort)
   → (container STARTED) endpoint.activate(bootstrapPort)   // RA opens transport
   → ... running ...
   → container.stop() → endpoint.deactivate()               // RA closes transport
```
When a protocol session ends (BYE, timeout…), the RA **must** call `bootstrapPort.endActivity(handle)` — attached SBBs receive `ActivityEndedEvent` and the ACI is reclaimed. Forgetting this = memory leak.

---

## 6. Classic mistakes (all have happened in this repo)

| # | Anti-pattern | Consequence | Correct approach |
|---|---|---|---|
| 1 | SBB receives event X then calls `container.routeEvent(X, aci)` for the same event | **Infinite loop** (300k+ events/s) | Never re-route an event you received. RA already fired on ACI, all attached SBBs received it |
| 2 | RA receives a command and publishes the request event back on the same ACI | Infinite SBB ↔ RA loop | Command is outbound — never mirror back as an event |
| 3 | DIY IES `SbbEntityPool` adapter | Entity has no lifecycle/`@InjectRa`; convergence deleted immediately on creation | `container.createIesDispatcher()` |
| 4 | `@InjectRa(name="grpcMenuRa")` but RA registered with name `grpc-menu-ra` | Port null, commands silently dropped | Name in `@InjectRa` = value of `RaEndpointPort.getRaName()` exactly character-by-character |
| 5 | `dialogs`/`sessions` map in RA with put but no remove | OOM after hours of runtime | Remove on protocol end + idle sweeper (see `DialogRegistry`) |
| 6 | Transport callback receives only `byte[]` | Can't reply (UDP) | Sink must include peer address (`SipMessageSink`) |
| 7 | Sleep/poll waiting for entity READY | Flaky tests, thread hangs | `awaitReady(timeout)` or let router handle it |
| 8 | Business logic calls RA class directly (`ra.doSomething()`) | Untestable, breaks on RA swap | SBB only talks via `RaCommandPort.sendCommand(cmd)` |

---

## 7. Testing

- **SBB unit test**: call `onEvent(event, aci)` directly with a real ACI from `container.createActivityContext("test")` — SBBs are POJOs.
- **Integration**: set up a real `MicroSleeContainer` in `@Before` (fast, <100ms), register type + RA, fire event, assert with latch. Standard example: `SipEndToEndTest` (ra-sip-servlet) — real UDP socket → SBB → real response.
- **Smoke E2E**: `UssdDemoSmokeTest` (example-quarkus) — HTTP begin → chain of 3 SBBs → poll COMPLETED.
- Quick single test run: `mvn -pl <module> test -Dtest='TestName#methodName'`.

---

## 8. Further reading

- [sbb-guide.md](sbb-guide.md) — checklist + template for writing SBBs.
- [ra-guide.md](ra-guide.md) — 3-port contract, transport, dialog lifecycle, use `ra-sip-servlet` as reference.
- [app-guide.md](app-guide.md) — Quarkus bootstrap step-by-step, config, testing with `sipexer`/`curl`.
- `docs/microjainslee-design.md` — detailed runtime design.
- JAIN SLEE 1.1 spec (JSR-240) — chapter 6 (SBB), 7 (Activity/Event), 12 (RA) if you want the original source.

---

## Appendix: Quick Reference — key files to read

> **Read these first** when exploring the codebase. Each file is annotated with what you'll learn.

### Core Runtime (read in order)

| File | What you'll learn |
|---|---|
| `jainslee-api/src/main/java/com/microjainslee/api/SleeEvent.java` | Base event interface |
| `jainslee-api/src/main/java/com/microjainslee/api/ActivityContextInterface.java` | ACI: how SBBs attach to sessions |
| `jainslee-api/src/main/java/com/microjainslee/api/Sbb.java` + `SleeEventHandler.java` | SBB contract |
| `jainslee-api/src/main/java/com/microjainslee/api/RaEndpointPort.java` + `RaCommandPort.java` + `RaBootstrapPort.java` | 3-port RA contract |
| `jainslee-core/src/main/java/com/microjainslee/core/MicroSleeContainer.java` | Container: start/stop, registerSbbType, registerRa, fireEvent, routeEvent |
| `jainslee-core/src/main/java/com/microjainslee/core/EventRouter.java` | LMAX Disruptor ring buffer event routing |
| `jainslee-core/src/main/java/com/microjainslee/core/IesDispatcher.java` | IES session convergence |
| `jainslee-scheduler/src/main/java/com/microjainslee/scheduler/HashedWheelTimer.java` | Timer facility |

### RA Reference (best example of production-quality RA)

| File | What you'll learn |
|---|---|
| `vendor-ras/ra-sip-servlet/DESIGN.md` | Architecture decisions, thread model, DNS flow |
| `vendor-ras/ra-sip-servlet/src/main/java/.../SipServletResourceAdaptor.java` | RA core: parse → classify → fireEvent; command → encode → send |
| `vendor-ras/ra-sip-servlet/src/main/java/.../SipServletRaEndpoint.java` | 3-port wrapper: activate/deactivate/sendCommand |
| `vendor-ras/ra-sip-servlet/src/main/java/.../collab/DialogRegistry.java` | Session tracking + idle sweeper anti-leak pattern |
| `vendor-ras/ra-sip-servlet/src/main/java/.../transport/SipTransport.java` | Transport interface (how DPDK swap works) |
| `vendor-ras/ra-sip-servlet/src/test/java/.../SipEndToEndTest.java` | ★ How to integration-test RA+SBB end-to-end |

### Bootstrap (app wiring)

| File | What you'll learn |
|---|---|
| `example/example-quarkus-sip/src/main/java/.../bootstrap/SipGatewayBootstrap.java` | SIP app: registerSbbType → createIesDispatcher → mapEventToSbb → registerRa |
| `example/example-quarkus/src/main/java/.../bootstrap/UssdDemoBootstrap.java` | USSD app: same pattern + collaborator injection |
| `example/example-quarkus/src/test/java/.../bootstrap/UssdDemoSmokeTest.java` | Plain-JUnit smoke test without CDI |

### Adapter (Quarkus extension)

| File | What you'll learn |
|---|---|
| `jainslee-adapter/adapter-quarkus/runtime/src/main/java/...` | `MicroSleeContainer` producer, config binding |
| `jainslee-adapter/adapter-quarkus/deployment/src/main/java/...` | Build-time processor: sbb-index, reflective class registration |
