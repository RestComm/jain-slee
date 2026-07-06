# example-quarkus-ussdgw

Quarkus 3 USSD gateway demo integrated via `adapter-quarkus`.
USSD traffic uses **HttpIngressResourceAdaptor** on port **8080** (not Quarkus REST).
Quarkus HTTP on port **18080** serves admin/health only.

---

## English — Run

**Prerequisites:** JDK 25, Maven 3.9+, micro-jainslee 1.1.0 + `adapter-quarkus` installed locally.

```bash
# Install core + Quarkus adapter (once)
cd jain-slee/jain-slee
mvn -B -ntp install -DskipTests \
  -pl jainslee-api,jainslee-scheduler,jainslee-core,jainslee-apt,adapter-quarkus -am

# Terminal A — gRPC menu backend
cd example/grpc-simulator
mvn -B -ntp package
java -cp target/grpc-simulator.jar:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
  com.example.grpcsimulator.GrpcSimulatorMain 9090

# Terminal B — Quarkus app (HTTP RA on 8080)
cd example/example-quarkus-ussdgw
mvn -B -ntp quarkus:dev -Dquarkus.build.skip=false

# Terminal C — USSD gateway simulator
cd example/ussdgw-simulator
mvn -B -ntp package
java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8080 251911000001 '*123#'
```

**Production-style run** (after `mvn package`):

```bash
java -jar target/example-quarkus-1.0.0-SNAPSHOT-runner.jar
```

**Config** (`application.properties`):

| Property | Default | Purpose |
|----------|---------|---------|
| `ussd.http.port` | `8080` | HTTP ingress RA (ussdgw target) |
| `grpc.host` / `grpc.port` | `127.0.0.1` / `9090` | grpc-simulator |
| `quarkus.http.port` | `18080` | Quarkus admin / health |

---

## English — Run tests

Uses wiring test (no `@QuarkusTest`) — boots container + HTTP RA in-process with stub gRPC.

```bash
cd example/example-quarkus-ussdgw
mvn -B -ntp test
```

| Test class | What it verifies |
|------------|------------------|
| `QuarkusUssdSmokeTest` | E2E via HTTP RA: begin-callback → SBB chain → async callback |

Expected: **2 tests, 0 failures**.

> **Note:** Quarkus 3.15.1 ASM supports up to Java 21 bytecode. Full `@QuarkusTest` on Java 25 requires Quarkus 3.17+.

---

## Tiếng Việt — Chạy thử

**Yêu cầu:** JDK 25, Maven 3.9+, đã cài micro-jainslee và `adapter-quarkus`.

```bash
# Cài core + adapter (một lần)
cd jain-slee/jain-slee
mvn -B -ntp install -DskipTests \
  -pl jainslee-api,jainslee-scheduler,jainslee-core,jainslee-apt,adapter-quarkus -am

# Terminal A — grpc-simulator
cd example/grpc-simulator
mvn -B -ntp package
java -cp target/grpc-simulator.jar:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
  com.example.grpcsimulator.GrpcSimulatorMain 9090

# Terminal B — ứng dụng Quarkus (HTTP RA port 8080)
cd example/example-quarkus-ussdgw
mvn -B -ntp quarkus:dev -Dquarkus.build.skip=false

# Terminal C — mô phỏng USSD GW
cd example/ussdgw-simulator
mvn -B -ntp package
java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8080 251911000001 '*123#'
```

Lưu ý: traffic USSD đi qua **HTTP RA** (port 8080), không qua REST của Quarkus (18080).

---

## Tiếng Việt — Chạy test

Test wiring khởi động container + HTTP RA trong process, dùng gRPC stub — không cần grpc-simulator bên ngoài.

```bash
cd example/example-quarkus-ussdgw
mvn -B -ntp test
```

| Lớp test | Nội dung kiểm tra |
|----------|-------------------|
| `QuarkusUssdSmokeTest` | E2E qua HTTP RA và callback bất đồng bộ |

Kỳ vọng: **2 test, 0 lỗi**.
