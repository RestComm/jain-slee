# ussdgw-simulator

Standalone HTTP client that pretends to be the USSD gateway. **Zero** dependency on
example modules or micro-jainslee — only the JDK
(`java.net.http.HttpClient` + `com.sun.net.httpserver.HttpServer`).

---

## English — Flow

1. Boots an embedded callback receiver (`HttpServer` on a random free port).
2. `POST {baseUrl}/api/ussd/begin-callback?callbackUrl=http://127.0.0.1:{port}/cb`
   with JSON `{"msisdn":"...","ussdString":"*123#"}`.
3. Waits on a `CountDownLatch` until the example server POSTs the result back.
4. Prints the callback body (or exits on timeout).

One outbound HTTP request per session, zero polling.

### Example server ports

Run **one** example at a time:

| Example | Port | Base URL |
|---------|------|----------|
| `example-quarkus-ussdgw` | **8080** | `http://127.0.0.1:8080` |
| `example-spring-ussdgw` | **8081** | `http://127.0.0.1:8081` |
| `example-embedded-j25-ussdgw` | **8082** | `http://127.0.0.1:8082` |

### Build and run

```bash
cd example/ussdgw-simulator
mvn -B -ntp package

java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8080 251911000001 '*123#'   # Quarkus
# java -jar ... http://127.0.0.1:8081 ...     # Spring
# java -jar ... http://127.0.0.1:8082 ...     # embedded-j25
```

### Entry points

| Main class | Purpose |
|------------|---------|
| `Ss7UssdSimulatorMain` | Single session, `[SS7-sim]` prefix (default JAR main) |
| `HttpClientRaStyleMain` | Single session, `[RA-style]` prefix |
| `VirtualThreadUssdHammerMain` | Load test: N concurrent callback flows |

Load test:

```bash
java -cp target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  com.example.ussdgw.VirtualThreadUssdHammerMain \
  http://127.0.0.1:8080 251911 1000 30000
```

### Tests

This module has no unit tests. Validate by running against a live example server
(see [`../README.md`](../README.md) full runbook).

---

## Tiếng Việt — Luồng hoạt động

1. Khởi động HTTP server nhận callback trên port ngẫu nhiên.
2. Gửi `POST {baseUrl}/api/ussd/begin-callback?callbackUrl=...` kèm JSON msisdn + ussdString.
3. Chờ example server POST kết quả về callback URL.
4. In body callback (hoặc timeout).

Một request HTTP ra, không polling.

### Port example tương ứng

Chỉ chạy **một** example tại một thời điểm:

| Example | Port | Base URL |
|---------|------|----------|
| `example-quarkus-ussdgw` | **8080** | `http://127.0.0.1:8080` |
| `example-spring-ussdgw` | **8081** | `http://127.0.0.1:8081` |
| `example-embedded-j25-ussdgw` | **8082** | `http://127.0.0.1:8082` |

### Build và chạy

```bash
cd example/ussdgw-simulator
mvn -B -ntp package

java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8082 251911000001 '*123#'   # ví dụ embedded
```

### Test

Module này không có unit test. Kiểm tra bằng cách chạy cùng grpc-simulator + một example
(xem runbook đầy đủ trong [`../README.md`](../README.md)).
