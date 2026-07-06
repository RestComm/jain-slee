# example-spring-ussdgw

Spring Boot 3 USSD gateway demo integrated via `adapter-springboot`.
USSD traffic uses **HttpIngressResourceAdaptor** on port **8081** (`spring.main.web-application-type=none`).

---

## English — Run

**Prerequisites:** JDK 25, Maven 3.9+, micro-jainslee 1.1.0 + `adapter-springboot` installed locally.

```bash
# Install core + Spring adapter (once)
cd jain-slee/jain-slee
mvn -B -ntp install -DskipTests \
  -pl jainslee-api,jainslee-scheduler,jainslee-core,jainslee-apt,adapter-springboot -am

# Terminal A — gRPC menu backend
cd example/grpc-simulator
mvn -B -ntp package
java -cp target/grpc-simulator.jar:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
  com.example.grpcsimulator.GrpcSimulatorMain 9090

# Terminal B — Spring app (HTTP RA on 8081)
cd example/example-spring-ussdgw
mvn -B -ntp spring-boot:run

# Terminal C — USSD gateway simulator
cd example/ussdgw-simulator
mvn -B -ntp package
java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8081 251911000001 '*123#'
```

**Health check:**

```bash
curl http://127.0.0.1:8081/health
# {"status":"ok"}
```

**Config** (`application.properties`):

| Property | Default | Purpose |
|----------|---------|---------|
| `ussd.demo.http.port` | `8081` | HTTP ingress RA |
| `ussd.demo.grpc.host` / `ussd.demo.grpc.port` | `127.0.0.1` / `9090` | grpc-simulator |
| `ussd.demo.grpc.use-in-memory` | `false` | `true` in tests only |

---

## English — Run tests

Boots `MicroSleeContainer` + both RAs without starting Spring Boot (Java 25 bytecode compatibility).

```bash
cd example/example-spring-ussdgw
mvn -B -ntp test
```

| Test class | What it verifies |
|------------|------------------|
| `SpringUssdSmokeTest` | E2E via HTTP RA with in-memory gRPC; callback + session polling paths |

Expected: **2 tests, 0 failures**.

> **Note:** Full `@SpringBootTest` on Java 25 requires Spring Boot 3.4+.

---

## Tiếng Việt — Chạy thử

**Yêu cầu:** JDK 25, Maven 3.9+, đã cài micro-jainslee và `adapter-springboot`.

```bash
# Cài core + adapter (một lần)
cd jain-slee/jain-slee
mvn -B -ntp install -DskipTests \
  -pl jainslee-api,jainslee-scheduler,jainslee-core,jainslee-apt,adapter-springboot -am

# Terminal A — grpc-simulator
cd example/grpc-simulator
mvn -B -ntp package
java -cp target/grpc-simulator.jar:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) \
  com.example.grpcsimulator.GrpcSimulatorMain 9090

# Terminal B — ứng dụng Spring (HTTP RA port 8081)
cd example/example-spring-ussdgw
mvn -B -ntp spring-boot:run

# Terminal C — mô phỏng USSD GW
cd example/ussdgw-simulator
mvn -B -ntp package
java -jar target/ussdgw-simulator-1.0.0-SNAPSHOT.jar \
  http://127.0.0.1:8081 251911000001 '*123#'
```

Spring không mở Spring MVC — toàn bộ USSD đi qua HTTP RA của micro-jainslee.

---

## Tiếng Việt — Chạy test

Khởi động container + RA trực tiếp, không boot Spring Boot (tương thích bytecode Java 25).

```bash
cd example/example-spring-ussdgw
mvn -B -ntp test
```

| Lớp test | Nội dung kiểm tra |
|----------|-------------------|
| `SpringUssdSmokeTest` | E2E qua HTTP RA; callback và polling session |

Kỳ vọng: **2 test, 0 lỗi**.
