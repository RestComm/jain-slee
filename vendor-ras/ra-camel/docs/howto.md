# HOWTO — Plug any Apache Camel extension into the generic Camel RA

> One RA, every Camel component. You declare *which* endpoint URIs to
> consume; Camel resolves the component from the URI scheme by its own
> standard discovery. The RA never hard-codes a component.
>
> Examples below: **camel-kafka** and **camel-ftp**. The recipe is identical
> for all ~300 components in the [Camel Quarkus catalogue](https://quarkus.io/extensions/?search-regex=camel).

---

## 0. Mental model (30 seconds)

```
  Kafka topic / FTP dir / MQTT / gRPC / ...            SBB (your logic)
        │                                                    ▲
        ▼                                                    │ CamelInboundEvent
  Camel component (resolved from URI scheme)                 │
        │  Exchange                                          │
        ▼                                                    │
  CamelResourceAdaptor ──fireEvent──► MicroSleeContainer ────┘
        ▲                                            (mapEventToSbb + IES)
        │ SendToEndpoint / RequestFromEndpoint / ReplyToExchange
        └──────────────── RaCommandPort (@InjectRa) ◄── SBB
```

Module structure follows the standard vendor-ras template:

```
ra-camel/
├── CamelResourceAdaptor.java   # core: routes ⇄ SLEE
├── CamelRaEndpoint.java        # 3-port wrapper (RaEndpointPort + RaCommandPort)
├── CamelRaConfig.java          # consumer specs, timeouts, RA name
├── events/                     # what SBBs RECEIVE  (CamelInboundEvent, CamelResponseEvent)
├── command/                    # what SBBs SEND     (SendToEndpoint, RequestFromEndpoint,
│                               #                     ReplyToExchange, EndCamelActivity)
└── collab/                     # pluggable strategy + registries
    ├── CamelEventFactory.java      # Exchange → (optionally app-typed) SleeEvent
    ├── CamelActivityRegistry.java  # correlation-id → SLEE activity (+ idle expiry)
    └── PendingReplyRegistry.java   # in-out exchanges waiting for an SBB reply
```

Every other RA (ra-sip-servlet, ra-grpc-server, ra-http-server, …) has the
same three folders — learn one, you know them all.

---

## 1. Add the Camel extension dependency

The RA ships **zero components**. You bring the one you want:

**Quarkus app (recommended — GraalVM-native ready):**

```xml
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-kafka</artifactId>   <!-- or camel-quarkus-ftp -->
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-core</artifactId>
</dependency>
```

**Plain Java / tests:**

```xml
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-kafka</artifactId>           <!-- or camel-ftp -->
    <version>4.8.1</version>
</dependency>
```

That is the *only* per-protocol step. No code in the RA changes.

---

## 2. Declare what to consume

```java
CamelRaConfig config = new CamelRaConfig()
    .name("camel-ra")                                  // @InjectRa name for SBBs
    // Kafka: fire-and-forget events, one SLEE activity per Kafka key
    .consume(CamelConsumerSpec.inOnly(
            "kafka:orders?brokers=localhost:9092&groupId=slee")
        .correlatedBy("kafka.KEY"))
    // FTP: poll a directory; every file becomes one event/activity
    .consume(CamelConsumerSpec.inOnly(
            "ftp://user@ftp.example.com/inbox?password=secret&delete=true"))
    .activityIdleSecs(300);                            // leak protection
```

- `inOnly(uri)` — events only.
- `inOut(uri)` — request/reply: the exchange **waits** (bounded by
  `replyTimeoutMillis`, default 30 s) until an SBB sends
  `ReplyToExchange(exchangeId, body)`. Use for `platform-http:`, `grpc:`,
  `netty:` style endpoints that must answer on the wire.
- `correlatedBy(header)` — exchanges carrying the same header value share
  one SLEE activity → stateful SBB sessions work out of the box. Without
  it each exchange gets a one-shot activity that is ended automatically.

---

## 3. Wire the RA in your bootstrap

```java
@ApplicationScoped
public class KafkaGatewayBootstrap {

    @Inject MicroSleeContainer container;
    @Inject CamelContext camelContext;      // provided by camel-quarkus-core

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }
        container.registerSbbType(OrderSbb.class, OrderSbb::new);
        container.createIesDispatcher();
        container.mapEventToSbb(CamelInboundEvent.class, "OrderSbb");

        CamelRaEndpoint endpoint = new CamelRaEndpoint();
        endpoint.setConfig(config);              // from step 2
        endpoint.setCamelContext(camelContext);  // Quarkus manages the context
        container.registerRa(endpoint, endpoint);
    }
}
```

Omit `setCamelContext(...)` outside Quarkus — the RA then owns a
`DefaultCamelContext` and starts/stops it with the RA lifecycle.

---

## 4. Consume events and send commands in your SBB

```java
public class OrderSbb implements Sbb, SleeEventHandler {

    @InjectRa(name = "camel-ra")
    private volatile RaCommandPort camel;

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof CamelInboundEvent in) {
            switch (in.endpointUri()) {
                // Kafka message
                case String u when u.startsWith("kafka:") -> {
                    String order = in.bodyAsString();
                    // ... business logic ...
                    // publish a result to another topic:
                    camel.sendCommand(new SendToEndpoint(
                            "kafka:order-results?brokers=localhost:9092", "OK:" + order));
                }
                // FTP file
                case String u when u.startsWith("ftp:") -> {
                    byte[] file = (byte[]) in.body();
                    // headers carry CamelFileName etc.
                }
                default -> { }
            }
            if (in.requiresReply()) {   // only for inOut consumers
                camel.sendCommand(new ReplyToExchange(in.exchangeId(), "answer"));
            }
        }
        if (event instanceof CamelResponseEvent resp) {
            // async reply to a RequestFromEndpoint command
        }
    }
}
```

Outbound options:

| Command | Pattern | Result |
|---|---|---|
| `SendToEndpoint(uri, body, headers)` | InOnly | fire-and-forget producer send |
| `RequestFromEndpoint(corrId, uri, body)` | InOut (async) | `CamelResponseEvent` fired on activity `corrId` |
| `ReplyToExchange(exchangeId, body)` | consumer reply | completes a waiting in-out exchange |
| `EndCamelActivity(activityId)` | lifecycle | ends a correlated session activity |

---

## 5. Typed events instead of the generic one (optional)

Map different endpoints to different SBBs by emitting your own event
types:

```java
endpoint.setEventFactory((uri, exchangeId, activityId, body, headers, requiresReply) ->
        uri.startsWith("kafka:")
            ? new OrderReceivedEvent(activityId, (String) body)
            : new FileArrivedEvent(activityId, (byte[]) body,
                    String.valueOf(headers.get("CamelFileName"))));

container.mapEventToSbb(OrderReceivedEvent.class, "OrderSbb");
container.mapEventToSbb(FileArrivedEvent.class,   "FileSbb");
```

---

## 6. Cheat-sheet: popular extensions

| Extension | Consumer URI example | Notes |
|---|---|---|
| camel-quarkus-kafka | `kafka:topic?brokers=host:9092&groupId=g` | correlate by `kafka.KEY` |
| camel-quarkus-ftp | `ftp://user@host/dir?password=x&delete=true` | body = file bytes; `CamelFileName` header |
| camel-quarkus-sftp | `sftp://user@host/dir?privateKeyFile=...` | same as ftp |
| camel-quarkus-platform-http | `platform-http:/api/charge` | use `inOut(...)` + `ReplyToExchange` |
| camel-quarkus-paho-mqtt5 | `paho-mqtt5:tele/device/+?brokerUrl=tcp://...` | correlate by topic header |
| camel-quarkus-grpc | `grpc://0.0.0.0:9090/my.Service?synchronous=true` | `inOut(...)`; or use `ra-grpc-server` for stub-less bytes |
| camel-quarkus-jms / sjms2 | `sjms2:queue:orders` | correlate by `JMSCorrelationID` |
| camel-quarkus-timer | `timer:tick?period=60000` | cron-like internal events |
| camel-quarkus-file | `file:/data/in?delete=true` | local spool directories |

Any other component works the same way — check its URI syntax in the
Camel docs, add the dependency, add a `consume(...)` line.

---

## 7. Troubleshooting

| Symptom | Cause / fix |
|---|---|
| `NoSuchEndpointException: ... check your classpath` | The component jar is missing — add the `camel-quarkus-*` / `camel-*` dependency (step 1) |
| `No SBB reply within 30000ms for exchange ...` | `inOut` consumer but no SBB sent `ReplyToExchange` — check `mapEventToSbb` and the `@InjectRa` name (`camel-ra`) |
| Events arrive but state is lost between messages | You forgot `correlatedBy(...)` — each exchange got its own activity |
| Activities keep growing | Correlated sessions are never ended — send `EndCamelActivity` when the session is done, or rely on `activityIdleSecs` |
| Works on JVM, fails in native image | Use the `camel-quarkus-*` extension (not the plain `camel-*` jar) and inject the Quarkus-managed `CamelContext` |
