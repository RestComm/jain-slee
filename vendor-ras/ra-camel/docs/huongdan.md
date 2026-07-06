# Hướng dẫn — Cắm một Apache Camel extension bất kỳ vào Camel RA

> Bản tiếng Việt của [howto.md](howto.md) (bản tiếng Anh là bản chuẩn,
> cập nhật trước).
>
> Một RA duy nhất cho **mọi** Camel component. Bạn chỉ khai báo *endpoint
> URI nào cần consume*; Camel tự tìm component từ scheme của URI theo cơ
> chế chuẩn của nó. RA không hard-code component nào.
>
> Ví dụ dưới đây: **camel-kafka** và **camel-ftp** — công thức giống hệt
> cho ~300 extension trong [catalogue Camel Quarkus](https://quarkus.io/extensions/?search-regex=camel).

---

## 0. Mô hình tư duy (30 giây)

```
  Kafka topic / thư mục FTP / MQTT / gRPC / ...        SBB (logic của bạn)
        │                                                    ▲
        ▼                                                    │ CamelInboundEvent
  Camel component (tự resolve từ URI scheme)                 │
        │  Exchange                                          │
        ▼                                                    │
  CamelResourceAdaptor ──fireEvent──► MicroSleeContainer ────┘
        ▲                                            (mapEventToSbb + IES)
        │ SendToEndpoint / RequestFromEndpoint / ReplyToExchange
        └──────────────── RaCommandPort (@InjectRa) ◄── SBB
```

Cấu trúc module theo đúng template chung của vendor-ras:

```
ra-camel/
├── CamelResourceAdaptor.java   # lõi: routes ⇄ SLEE
├── CamelRaEndpoint.java        # 3-port wrapper (RaEndpointPort + RaCommandPort)
├── CamelRaConfig.java          # consumer specs, timeout, tên RA
├── events/                     # SBB NHẬN gì   (CamelInboundEvent, CamelResponseEvent)
├── command/                    # SBB GỬI gì    (SendToEndpoint, RequestFromEndpoint,
│                               #                ReplyToExchange, EndCamelActivity)
└── collab/                     # strategy cắm được + các registry
    ├── CamelEventFactory.java      # Exchange → SleeEvent (có thể typed theo app)
    ├── CamelActivityRegistry.java  # correlation-id → SLEE activity (+ idle expiry)
    └── PendingReplyRegistry.java   # exchange in-out đang chờ SBB trả lời
```

RA nào trong vendor-ras cũng có đúng 3 thư mục này (ra-sip-servlet,
ra-grpc-server, ra-http-server…) — học một cái là hiểu tất cả.

---

## 1. Thêm dependency của Camel extension

RA **không kèm component nào**. Bạn tự mang component mình cần:

**App Quarkus (khuyến nghị — sẵn sàng GraalVM native):**

```xml
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-kafka</artifactId>   <!-- hoặc camel-quarkus-ftp -->
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-core</artifactId>
</dependency>
```

**Java thuần / test:**

```xml
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-kafka</artifactId>           <!-- hoặc camel-ftp -->
    <version>4.8.1</version>
</dependency>
```

Đó là bước *duy nhất* phụ thuộc protocol. Code RA không đổi một dòng.

---

## 2. Khai báo consume cái gì

```java
CamelRaConfig config = new CamelRaConfig()
    .name("camel-ra")                                  // tên cho @InjectRa trong SBB
    // Kafka: event một chiều, mỗi Kafka key = một SLEE activity
    .consume(CamelConsumerSpec.inOnly(
            "kafka:orders?brokers=localhost:9092&groupId=slee")
        .correlatedBy("kafka.KEY"))
    // FTP: poll thư mục; mỗi file = một event/activity
    .consume(CamelConsumerSpec.inOnly(
            "ftp://user@ftp.example.com/inbox?password=secret&delete=true"))
    .activityIdleSecs(300);                            // chống leak
```

- `inOnly(uri)` — chỉ bắn event.
- `inOut(uri)` — request/reply: exchange **chờ** (giới hạn bởi
  `replyTimeoutMillis`, mặc định 30 s) đến khi một SBB gửi
  `ReplyToExchange(exchangeId, body)`. Dùng cho `platform-http:`,
  `grpc:`, `netty:`… (những endpoint phải trả lời trên wire).
- `correlatedBy(header)` — các exchange cùng giá trị header hội tụ về
  **một** SLEE activity → SBB stateful theo session chạy ngay. Không có
  nó thì mỗi exchange là một activity dùng-một-lần, tự end sau xử lý.

---

## 3. Wire RA trong bootstrap

```java
@ApplicationScoped
public class KafkaGatewayBootstrap {

    @Inject MicroSleeContainer container;
    @Inject CamelContext camelContext;      // do camel-quarkus-core cung cấp

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }
        container.registerSbbType(OrderSbb.class, OrderSbb::new);
        container.createIesDispatcher();
        container.mapEventToSbb(CamelInboundEvent.class, "OrderSbb");

        CamelRaEndpoint endpoint = new CamelRaEndpoint();
        endpoint.setConfig(config);              // từ bước 2
        endpoint.setCamelContext(camelContext);  // Quarkus quản lý context
        container.registerRa(endpoint, endpoint);
    }
}
```

Ngoài Quarkus thì bỏ `setCamelContext(...)` — RA sẽ tự sở hữu một
`DefaultCamelContext` và start/stop nó theo lifecycle RA.

---

## 4. SBB nhận event và gửi command

```java
public class OrderSbb implements Sbb, SleeEventHandler {

    @InjectRa(name = "camel-ra")
    private volatile RaCommandPort camel;

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof CamelInboundEvent in) {
            if (in.endpointUri().startsWith("kafka:")) {
                String order = in.bodyAsString();
                // ... business logic ...
                camel.sendCommand(new SendToEndpoint(
                        "kafka:order-results?brokers=localhost:9092", "OK:" + order));
            } else if (in.endpointUri().startsWith("ftp:")) {
                byte[] file = (byte[]) in.body();   // header CamelFileName có sẵn
            }
            if (in.requiresReply()) {   // chỉ với consumer inOut
                camel.sendCommand(new ReplyToExchange(in.exchangeId(), "answer"));
            }
        }
        if (event instanceof CamelResponseEvent resp) {
            // reply async của command RequestFromEndpoint
        }
    }
}
```

Các command chiều ra:

| Command | Pattern | Kết quả |
|---|---|---|
| `SendToEndpoint(uri, body, headers)` | InOnly | producer gửi, không chờ |
| `RequestFromEndpoint(corrId, uri, body)` | InOut (async) | `CamelResponseEvent` bắn về activity `corrId` |
| `ReplyToExchange(exchangeId, body)` | reply consumer | hoàn tất exchange in-out đang chờ |
| `EndCamelActivity(activityId)` | lifecycle | kết thúc activity session |

---

## 5. Event typed thay cho event generic (tùy chọn)

Muốn mỗi endpoint về một SBB khác nhau — tự emit event type riêng:

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

## 6. Bảng tra nhanh các extension phổ biến

| Extension | Consumer URI ví dụ | Ghi chú |
|---|---|---|
| camel-quarkus-kafka | `kafka:topic?brokers=host:9092&groupId=g` | correlate theo `kafka.KEY` |
| camel-quarkus-ftp | `ftp://user@host/dir?password=x&delete=true` | body = bytes file; header `CamelFileName` |
| camel-quarkus-sftp | `sftp://user@host/dir?privateKeyFile=...` | như ftp |
| camel-quarkus-platform-http | `platform-http:/api/charge` | dùng `inOut(...)` + `ReplyToExchange` |
| camel-quarkus-paho-mqtt5 | `paho-mqtt5:tele/device/+?brokerUrl=tcp://...` | correlate theo topic header |
| camel-quarkus-grpc | `grpc://0.0.0.0:9090/my.Service?synchronous=true` | `inOut(...)`; hoặc dùng `ra-grpc-server` bytes-level |
| camel-quarkus-jms / sjms2 | `sjms2:queue:orders` | correlate theo `JMSCorrelationID` |
| camel-quarkus-timer | `timer:tick?period=60000` | event nội bộ kiểu cron |
| camel-quarkus-file | `file:/data/in?delete=true` | spool thư mục local |

Extension khác cũng y hệt: tra cú pháp URI trong docs Camel, thêm
dependency, thêm một dòng `consume(...)`.

---

## 7. Lỗi thường gặp

| Triệu chứng | Nguyên nhân / cách sửa |
|---|---|
| `NoSuchEndpointException: ... check your classpath` | Thiếu jar component — thêm dependency `camel-quarkus-*` / `camel-*` (bước 1) |
| `No SBB reply within 30000ms for exchange ...` | Consumer `inOut` nhưng không SBB nào gửi `ReplyToExchange` — kiểm tra `mapEventToSbb` và tên `@InjectRa` (`camel-ra`) |
| Event đến nhưng state mất giữa các message | Quên `correlatedBy(...)` — mỗi exchange một activity riêng |
| Activity tăng mãi không giảm | Session correlated không được end — gửi `EndCamelActivity` khi xong, hoặc dựa vào `activityIdleSecs` |
| Chạy JVM ok, native fail | Dùng extension `camel-quarkus-*` (không phải jar `camel-*` thường) và inject `CamelContext` do Quarkus quản lý |
