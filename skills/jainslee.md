# Skill: jainslee — Bootstrap micro-jainslee Applications & RAs

**Skill name:** `jainslee`
**Scope:** Workspace (`micro-jainslee-2`)
**Platforms:** Cline, Claude Code, OpenAI, any LLM coding agent

---

## Trigger

Invoke via slash command. Two modes:

```
/skill jainslee app <AppName>        → Generate a complete JAIN SLEE application
/skill jainslee ra <RaName>          → Generate a Resource Adaptor (3-port contract)
```

### Examples

```
/skill jainslee app myEchoService
/skill jainslee app com.acme.billing.BillProcessor
/skill jainslee ra my-kafka-bridge
/skill jainslee ra custom-tcp-gateway
```

---

## MODE 1: `/skill jainslee app <AppName>`

Generates a **complete, build-ready** JAIN SLEE application.

### Output (6 files)

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

### File specs

**pom.xml** — groupId=`{basePackage}`, artifactId=`{appName}`, deps: `jainslee-core`, `jainslee-api`, `jainslee-apt`, `log4j-api`, `log4j-core`. Java 25. APT on annotationProcessorPath.

**{AppName}Main.java** — `MicroSleeConfiguration.builder()` (bufSize=4096, poolMin=32, poolMax=10k). Create container, install bootstrap, start. Shutdown hook. `Thread.join()` keep-alive.

**{AppName}Bootstrap.java** — `install()` calls `registerSbbTypes()` (`container.registerSbb(Sbb.class, Sbb::new)`) + `bindEventMappings()` (`container.mapEventToSbb(ReqEvent.class, "SbbName")`). `shutdown()` cleans up RAs reverse-order.

**events/{AppName}RequestEvent.java** — `@EventType(name, vendor, version)`, `final class implements SleeEvent`, immutable fields (`sessionId`, `payload`), all-args constructor + getters.

**events/{AppName}ResponseEvent.java** — `@EventType`, `final class implements SleeEvent`, immutable fields (`sessionId`, `result`).

**sbbs/{AppName}Sbb.java** — `final class implements Sbb, SleeEventHandler`. Lifecycle: `sbbCreate/Activate/Passivate/Remove`. `onEvent()` uses Java 25 `switch` pattern matching. `@InjectRa` for RA ports. Business logic via `aci.fireEvent()`.

---

## MODE 2: `/skill jainslee ra <RaName>`

Generates a **3-port contract** Resource Adaptor (WRAPPER + DELEGATE).

### Output (4 files)

```
{raName}/
├── pom.xml
└── src/main/java/com/microjainslee/ra/{raname}/
    ├── {RaName}RaEndpoint.java          ← WRAPPER: RaEndpointPort + RaCommandPort
    ├── {RaName}ResourceAdaptor.java     ← DELEGATE: transport + logic
    └── command/
        └── {RaName}Command.java         ← sealed interface extends OutboundCommand
```

### File specs

**pom.xml** — groupId=`com.microjainslee`, artifactId=`{raName}`, deps: `jainslee-api`, `jainslee-ra-spi`, `log4j-api`. Java 25.

**{RaName}RaEndpoint.java (WRAPPER)** — `final class implements RaEndpointPort, RaCommandPort`. Constructor takes delegate. Collaborator setters delegate. `getRaName()` = `"{raName}"` (kebab-case). `activate(RaBootstrapPort)` → `delegate.setBootstrap(bp)` → `doConfigure()` → `doStart()`. `deactivate()` → `doStop()` → `setBootstrap(null)`. `sendCommand(OutboundCommand)` pattern-matches `{RaName}Command`. Exposes `delegate()`.

**{RaName}ResourceAdaptor.java (DELEGATE)** — `final class`. `volatile RaBootstrapPort bootstrap` + setter. `AtomicBoolean active` guard. `ConcurrentHashMap<String, ActivityHandle> sessions`. `doConfigure()`, `doStart()`, `doStop()`. `fireInboundEvent(SleeEvent, String)` uses `bootstrap.createActivityHandle()` + `bootstrap.fireEvent()`. `handleCommand({RaName}Command)` processes outbound. **NEVER `new Disruptor<>()`** — always `bootstrap.fireEvent()`.

**command/{RaName}Command.java** — `sealed interface extends OutboundCommand`. Add `record` subtypes per command type.

---

## Conventions (both modes)

| Rule | App | RA |
|------|-----|-----|
| Java | 25 | 25 |
| Logging | Log4j2 | Log4j2 |
| Classes | `final` | `final` |
| Events | `@EventType implements SleeEvent` | sealed hierarchy |
| Dispatch | `switch` pattern matching | `bootstrap.fireEvent()` |
| Wiring | `registerSbb()` + `mapEventToSbb()` | `registerRa(ep, ep)` |
| Package | `{user.package}` | `com.microjainslee.ra.{raname}` |
| Location | `example/` | `vendor-ras/` |

---

## Anti-Patterns

| ❌ Never | ✅ Always |
|----------|----------|
| `extends AbstractResourceAdaptor` for new RAs | `RaEndpointPort` + `RaCommandPort` wrapper |
| `new Disruptor<>()` in RA | `bootstrapPort.fireEvent()` |
| 1 generic event type | sealed event hierarchy |
| `java.util.logging` or SLF4J | Log4j2 |

---

## References

- `example/example-embedded-j25/` — complete working app
- `vendor-ras/ra-http-server/` — reference RA (HttpServerRaEndpoint + HttpServerResourceAdaptor)
- `vendor-ras/ra-grpc-client/` — reference gRPC RA
- `docs/junior-dev-guide.md` — Phụ lục C (app pattern), Phụ lục F (RA checklist)
