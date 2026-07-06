# example-embedded-j25-ussdgw

Plain Java 25 USSD gateway demo — embeds `jainslee-core` directly (no Quarkus, no Spring).
HTTP ingress goes through **HttpIngressResourceAdaptor** on port **8082**.

---

## English — Run

**Prerequisites:** JDK 25, Maven 3.9+, micro-jainslee 1.1.0 installed locally.

```bash
# From repo root — install core once
cd jain-slee/jain-slee
mvn -B -ntp install -DskipTests \
  -pl jainslee-api,jainslee-scheduler,jainslee-core,jainslee-apt -am

# Terminal A — gRPC menu backend (required for manual demo)
cd example/grpc-simulator
mvn -B -ntp package
java -cp target/grpc-simulator.jar:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
  com.example.grpcsimulator.GrpcSimulatorMain 9090

# Terminal B — embedded USSD gateway
cd example/example-embedded-j25-ussdgw
mvn -B -ntp package
java -jar target/example-embedded-j25-ussdgw.jar          # default port 8082
# java -jar target/example-embedded-j25-ussdgw.jar 8083   # custom port

# Terminal C — fire a session (ussdgw-simulator)
cd example/ussdgw-simulator
mvn -B -ntp package
java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8082 251911000001 '*123#'
```

**Manual curl** (start a local listener on port 9999 first):

```bash
curl -s -X POST 'http://127.0.0.1:8082/api/ussd/begin-callback?callbackUrl=http://127.0.0.1:9999/cb' \
  -H 'Content-Type: application/json' \
  -d '{"msisdn":"251911000001","ussdString":"*123#"}'
```

**Endpoints:** `POST /api/ussd/begin-callback`, `POST /api/ussd/begin`, `GET /api/ussd/sessions/{id}`, `GET /health`

---

## English — Run tests

Tests spin up an in-process gRPC stub server — **no external grpc-simulator required**.

```bash
cd example/example-embedded-j25-ussdgw
mvn -B -ntp test
```

| Test class | What it verifies |
|------------|------------------|
| `EmbeddedUssdSmokeTest` | Full E2E: HTTP RA → HttpServerSbb → Ss7UssdIngressSbb → GrpcClientSbb → callback |
| `HttpServerSbbPipelineTest` | HttpServerSbb event routing and session wiring |
| `ContainerRoutingTest` | `registerSbbType`, `acquireEntity`, child relation factory |

Expected: **7 tests, 0 failures**.

---

## Tiếng Việt — Chạy thử

**Yêu cầu:** JDK 25, Maven 3.9+, đã cài micro-jainslee 1.1.0 vào local Maven repo.

```bash
# Từ thư mục gốc repo — cài core một lần
cd jain-slee/jain-slee
mvn -B -ntp install -DskipTests \
  -pl jainslee-api,jainslee-scheduler,jainslee-core,jainslee-apt -am

# Terminal A — backend menu gRPC (bắt buộc khi chạy demo thủ công)
cd example/grpc-simulator
mvn -B -ntp package
java -cp target/grpc-simulator.jar:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
  com.example.grpcsimulator.GrpcSimulatorMain 9090

# Terminal B — USSD gateway embedded
cd example/example-embedded-j25-ussdgw
mvn -B -ntp package
java -jar target/example-embedded-j25-ussdgw.jar          # mặc định port 8082

# Terminal C — bắn một phiên USSD (ussdgw-simulator)
cd example/ussdgw-simulator
mvn -B -ntp package
java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8082 251911000001 '*123#'
```

Kết quả mong đợi: `202 Accepted`, vài giây sau callback trả về menu có chữ `Balance`.

---

## Tiếng Việt — Chạy test

Test tự khởi động gRPC stub trong process — **không cần** chạy grpc-simulator bên ngoài.

```bash
cd example/example-embedded-j25-ussdgw
mvn -B -ntp test
```

| Lớp test | Nội dung kiểm tra |
|----------|-------------------|
| `EmbeddedUssdSmokeTest` | E2E đầy đủ qua HTTP RA và callback |
| `HttpServerSbbPipelineTest` | Routing event của HttpServerSbb |
| `ContainerRoutingTest` | Pool SBB, acquireEntity, child relation |

Kỳ vọng: **7 test, 0 lỗi**.
