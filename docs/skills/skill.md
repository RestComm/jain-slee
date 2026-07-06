# 🛠️ SKILL: jainslee — Bootstrap micro-jainslee Applications & RAs

> **Skill for:** Cline, Claude Code, OpenAI Codex, and any LLM coding agent.
>
> **Purpose:** Generate a complete, build-ready JAIN SLEE application or Resource Adaptor
> skeleton in one command — no more manually copying from examples.

---

## Quick Start

### Trigger syntax

```
/skill jainslee app <AppName>       → New JAIN SLEE application (SBB + events + bootstrap)
/skill jainslee ra <RaName>         → New Resource Adaptor (3-port contract)
```

### Examples

```
/skill jainslee app myEchoService
/skill jainslee app com.acme.billing.BillProcessor
/skill jainslee ra my-kafka-bridge
/skill jainslee ra custom-http-filter
```

---

## Generated Files — MODE `app`

| # | File | Purpose |
|---|------|---------|
| 1 | `pom.xml` | Maven build — dependencies on `jainslee-core`, `jainslee-api`, `jainslee-apt`, log4j2 |
| 2 | `{AppName}Main.java` | Entry point with `main()`, creates container, starts, keeps alive |
| 3 | `{AppName}Bootstrap.java` | Wires `registerSbb()`, `mapEventToSbb()`, optional `registerRa()` |
| 4 | `events/{AppName}RequestEvent.java` | `@EventType`, `implements SleeEvent`, immutable |
| 5 | `events/{AppName}ResponseEvent.java` | `@EventType`, `implements SleeEvent`, immutable |
| 6 | `sbbs/{AppName}Sbb.java` | `implements Sbb, SleeEventHandler`, `@InjectRa`, pattern-matching `switch` |

### Structure
```
{appName}/
├── pom.xml
└── src/main/java/{basePackage}/
    ├── {AppName}Main.java
    ├── {AppName}Bootstrap.java
    ├── events/
    │   ├── {AppName}RequestEvent.java
    │   └── {AppName}ResponseEvent.java
    └── sbbs/
        └── {AppName}Sbb.java
```

---

## Generated Files — MODE `ra`

| # | File | Purpose |
|---|------|---------|
| 1 | `pom.xml` | Maven build — dependencies on `jainslee-api`, `jainslee-ra-spi`, log4j2 |
| 2 | `{RaName}RaEndpoint.java` | **WRAPPER** — `implements RaEndpointPort, RaCommandPort` |
| 3 | `{RaName}ResourceAdaptor.java` | **DELEGATE** — transport + business logic |
| 4 | `command/{RaName}Command.java` | `sealed interface extends OutboundCommand` |

### Structure
```
{raName}/
├── pom.xml
└── src/main/java/com/microjainslee/ra/{raname}/
    ├── {RaName}RaEndpoint.java
    ├── {RaName}ResourceAdaptor.java
    └── command/
        └── {RaName}Command.java
```

---

## 3-Port Contract Pattern (RA)

Every RA follows the **WRAPPER + DELEGATE** pattern:

```
┌──────────────────────────────────────────────────────┐
│  {RaName}RaEndpoint  (WRAPPER)                       │
│  implements RaEndpointPort, RaCommandPort            │
│                                                      │
│  ┌────────────────────────────────────────────────┐  │
│  │  {RaName}ResourceAdaptor  (DELEGATE)           │  │
│  │  - Transport logic (HTTP, gRPC, TCP, ...)      │  │
│  │  - fireInboundEvent() → bootstrap.fireEvent()  │  │
│  │  - handleCommand() → route to external system  │  │
│  └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

### Anti-Patterns (DO NOT):
- ❌ Do NOT extend `AbstractResourceAdaptor` directly for new RAs
- ❌ Do NOT create `new Disruptor<>()` — container already has EventRouter
- ❌ Do NOT use 1 generic event type — use **sealed hierarchies**
- ❌ Do NOT fire events directly on RA threads without `bootstrapPort`

### Correct Patterns (DO):
- ✅ WRAPPER implements `RaEndpointPort` + `RaCommandPort`
- ✅ DELEGATE handles transport, fires via `bootstrapPort.fireEvent()`
- ✅ `sealed interface` for events and commands
- ✅ `registerRa(endpoint, endpoint)` — same object serves both ports
- ✅ `ConcurrentHashMap` for session tracking

---

## Conventions

| Convention | App | RA |
|------------|-----|-----|
| **Java** | 25 | 25 |
| **Logging** | Log4j2 | Log4j2 |
| **Classes** | `final` | `final` |
| **Events** | `@EventType` + `implements SleeEvent` | sealed hierarchy |
| **SBB lifecycle** | `sbbCreate/Activate/Passivate/Remove` | N/A |
| **RA lifecycle** | N/A | `doConfigure/Start/Stop` via wrapper |
| **Session tracking** | `EntitySlotPool` (container-managed) | `ConcurrentHashMap` |
| **Container wiring** | `registerSbb()` + `mapEventToSbb()` | `registerRa(endpoint, endpoint)` |
| **Base package** | `{user.package}` | `com.microjainslee.ra.{raname}` |
| **Module location** | `example/` | `vendor-ras/` |

---

## References

- `docs/junior-dev-guide.md` — Complete developer guide (Phụ lục C: app pattern, Phụ lục F: RA checklist)
- `vendor-ras/ra-http-server/` — Reference RA implementation
- `vendor-ras/ra-grpc-client/` — Reference gRPC RA implementation
- `example/example-embedded-j25-ussdgw/` — Reference app implementation
- `.clinerules` — Workspace auto-load rules for Cline

---

## Copy-Paste for Custom LLM Agents

To use this skill with any LLM (Claude, GPT, Gemini), paste the prompt below:

```
You are a micro-jainslee code generator. When the user says:

  "create a JAIN SLEE app called <Name>"
  "/skill jainslee app <Name>"
  "/skill jainslee ra <Name>"

You MUST generate ALL files for a complete, build-ready project.

APP mode generates:
  pom.xml + {Name}Main.java + {Name}Bootstrap.java +
  events/{Name}RequestEvent.java + events/{Name}ResponseEvent.java +
  sbbs/{Name}Sbb.java

RA mode generates:
  pom.xml + {Name}RaEndpoint.java (implements RaEndpointPort, RaCommandPort) +
  {Name}ResourceAdaptor.java (transport logic) +
  command/{Name}Command.java (sealed interface extends OutboundCommand)

Rules:
- Java 25, final classes, Log4j2 logging
- Events: @EventType, implements SleeEvent, immutable
- SBBs: implements Sbb + SleeEventHandler, switch pattern matching
- RAs: WRAPPER+DELEGATE pattern, NEVER new Disruptor<>(), use bootstrap.fireEvent()
- RA commands: sealed interface extends OutboundCommand
- Reference: micro-jainslee workspace at vendor-ras/ra-http-server/ and example/example-embedded-j25-ussdgw/
```
