# Perfect Core — Wiring Guide

Hướng dẫn integrate các class mới vào micro-jainslee hiện tại.
Mỗi section = 1 sprint, chỉ ra cụ thể file nào cần sửa và sửa gì.

---

## S1 — JTA Wiring (jainslee-core)

### File cần sửa: `EventRouter.java` (dòng ~182)

```java
// TRƯỚC — direct dispatch (no transaction)
private void deliverEvent(SleeEvent event, ActivityContextInterface aci) {
    SbbLocalObject sbb = pool.acquire(event.getSbbId(), ...);
    sbb.onEvent(event, aci, eventContext);
    pool.releaseEntity(event.getSbbId());
}

// SAU — wrap trong JTA transaction
private final SleeTransactionManager txManager; // inject vào constructor

private void deliverEvent(SleeEvent event, ActivityContextInterface aci) {
    txManager.executeInTransaction(() -> {
        SbbLocalObject sbb = pool.acquire(event.getSbbId(), ...);
        try {
            sbb.onEvent(event, aci, eventContext);
        } finally {
            pool.releaseEntity(event.getSbbId());
        }
        // MDC logging sau transaction
        EventMdc.set(event.getSbbId(), aci.getName(),
                     event.getClass().getSimpleName(), txManager.currentStatusName());
    });
}
```

### File mới cần thêm: `jainslee-tx/pom.xml`
```xml
<dependency>
    <groupId>org.jboss.narayana.jta</groupId>
    <artifactId>narayana-jta</artifactId>
    <version>7.0.0.Final</version>
</dependency>
```

### VirtualThread pinning check (CI script)
```bash
# Thêm vào JVM args trong test
export JAVA_TOOL_OPTIONS="-Djdk.tracePinnedThreads=full"
mvn test -pl jainslee-core 2>&1 | grep -A3 "Pinned\|pinned" || echo "No pinning detected ✓"
```

---

## S2 — CMP Codegen Wiring (VirtualThreadSbbEntityPool)

### File cần sửa: `VirtualThreadSbbEntityPool.java`

```java
// Thêm ConcreteSbbGenerator
private final ConcreteSbbGenerator codeGen = new ConcreteSbbGenerator();
private final Path deployDir = Path.of(System.getProperty("jainslee.deploy.dir", "/tmp/slee-deploy"));

// TRƯỚC — instantiate abstract class trực tiếp (sẽ fail vì CMP methods abstract)
private Sbb createSbbInstance(Class<?> sbbClass) {
    return (Sbb) sbbClass.getDeclaredConstructor().newInstance(); // AbstractMethodError!
}

// SAU — generate concrete class first, then instantiate
private final Map<Class<?>, Class<?>> concreteClassCache = new ConcurrentHashMap<>();

private Sbb createSbbInstance(Class<?> sbbClass) throws Exception {
    Class<?> concreteClass = concreteClassCache.computeIfAbsent(sbbClass, cls -> {
        try {
            return codeGen.getOrGenerate(cls, deployDir);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate concrete SBB: " + cls.getName(), e);
        }
    });
    return (Sbb) concreteClass.getDeclaredConstructor().newInstance();
}
```

### @InitialEventSelect annotation (jainslee-api)

```java
// com.microjainslee.api.annotation.InitialEventSelect (NEW)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitialEventSelect {
    String name() default "";  // Optional: name for logging/metrics
}
```

---

## S3 — IES Wiring (EventRouter)

### File cần sửa: `EventRouter.java`

```java
// Inject IES dispatcher
private final InitialEventSelectorDispatcher iesDispatcher;

// TRƯỚC — direct route (creates new SBB each time)
public void routeIncomingEvent(Object event, Class<?> targetSbbClass) {
    String entityId = pool.allocateNew(targetSbbClass);  // ALWAYS new → broken for USSD
    dispatchToEntity(entityId, event, aci);
}

// SAU — IES resolves correct entity
public void routeIncomingEvent(Object event, ActivityContextInterface aci,
                                Class<?> targetSbbClass) {
    String entityId = iesDispatcher.resolveTarget(event, aci, targetSbbClass);
    if (entityId == null) {
        LOG.debugf("IES dropped event %s (non-initial, no existing entity)",
                   event.getClass().getSimpleName());
        return; // Spec §7.5.5 — silently drop
    }
    dispatchToEntity(entityId, event, aci);
}
```

### USSD SBB example (usage trong user code)

```java
@SbbAnnotation(...)
public abstract class UssdSessionSbb implements Sbb {

    // CMP fields (Javassist generates backing field + accessor)
    public abstract String getMsisdn();
    public abstract void setMsisdn(String msisdn);

    public abstract String getMenuState();
    public abstract void setMenuState(String state);

    // IES method — runs on TEMP instance (no side effects on state!)
    @InitialEventSelect
    public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
        UssdEvent e = (UssdEvent) c.getEvent();
        return InitialEventSelectResult.forSession(
            e.getMsisdn() + ":" + e.getDialogId(),  // convergence key
            e.getType() == UssdEventType.BEGIN        // isInitialEvent
        );
    }

    public void onUssdBegin(UssdEvent event, ActivityContextInterface aci, ...) {
        setMsisdn(event.getMsisdn());  // CMP field — stored in entity
        setMenuState("MAIN_MENU");
        // Send menu options...
    }

    public void onUssdContinue(UssdEvent event, ActivityContextInterface aci, ...) {
        // IES already found this SBB entity by convergence name
        String state = getMenuState();  // CMP field — restored from entity ✓
        // Process user input based on state...
    }
}
```

---

## S4 — ChildRelation Wiring (ConcreteSbbGenerator)

### Thêm vào ConcreteSbbGenerator.doGenerate()

```java
// Detect abstract methods returning ChildRelation<T>
// và generate ChildRelationImpl instances cho chúng

private void generateChildRelationAccessors(CtClass concrete, CtClass abstractSbb,
                                             CascadeRemover cascadeRemover,
                                             String entityId) throws Exception {
    for (CtMethod method : abstractSbb.getDeclaredMethods()) {
        if (!Modifier.isAbstract(method.getModifiers())) continue;
        if (!method.getReturnType().getName().contains("ChildRelation")) continue;

        // Generate backing field: private ChildRelation<T> xxxChildRelation
        String fieldName = toCmpFieldName(method.getName()) + "Impl";
        // ... (field injection via constructor or @Inject)
    }
}
```

### VirtualThreadSbbEntityPool: wire CascadeRemover

```java
// Thêm vào pool constructor
private final CascadeRemover cascadeRemover = new CascadeRemover(new CascadeRemover.EntityLookup() {
    @Override public Sbb getSbb(String entityId) {
        SbbEntity entity = entities.get(entityId);
        return entity != null ? entity.getSbb() : null;
    }
    @Override public void releaseEntity(String entityId) {
        entities.remove(entityId);
    }
});

// Thay vì gọi entities.remove() trực tiếp:
// TRƯỚC: entities.remove(sbbId);
// SAU:   cascadeRemover.cascadeRemove(sbbId);
```

---

## S5 — RA Wiring (MicroSleeContainer)

### File cần sửa: `MicroSleeContainer.java`

```java
// Thay SimpleSleeEndpoint → SleeEndpointImpl cho mỗi RA entity
public void registerResourceAdaptor(String raEntityName, ResourceAdaptor ra) {
    // Tạo state machine
    RaEntityStateMachine stateMachine = new RaEntityStateMachine(ra, raEntityName);

    // Tạo endpoint với full validation
    SleeEndpointImpl endpoint = new SleeEndpointImpl(eventRouter, acnf, stateMachine);

    // Tạo context với tất cả facilities
    ResourceAdaptorContextImpl ctx = ResourceAdaptorContextImpl.builder(raEntityName)
        .sleeEndpoint(endpoint)
        .timer(timerFacility)           // SleeTimerSchedulerBridge
        .alarm(alarmFacility)           // SimpleAlarmFacility
        .trace(traceFacility)           // TraceFacility (Logback-backed)
        .nullActivity(nullActivityFactory)
        .eventLookup(eventLookupFacility)
        .build();

    // Lifecycle
    ra.setResourceAdaptorContext(ctx);
    raEntities.put(raEntityName, new RaEntityEntry(ra, stateMachine, endpoint));
    stateMachine.activate(); // → ra.raActive()
}
```

---

## S6 — TCK Setup (CI)

```yaml
# .github/workflows/tck-baseline.yml
name: TCK core validation

on: [push, pull_request]

jobs:
  tck:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21', distribution: 'temurin' }
      - name: Build
        run: mvn install -DskipTests -q
      - name: TCK baseline (5 core groups)
        run: |
          mvn test -pl jainslee-tck-harness \
            -Dtest.groups=EventRouting,SbbLifecycle,TimerFacility,ChildSbbRelation,TransactionTests \
            2>&1 | tee tck-results.txt
      - name: Assert no regression
        run: |
          PASSED=$(grep -c "Tests run:.*Failures: 0, Errors: 0" tck-results.txt || echo 0)
          echo "TCK groups passing: $PASSED / 5"
          [ $PASSED -ge 4 ] || (echo "TCK REGRESSION" && exit 1)
      - uses: actions/upload-artifact@v4
        with:
          name: tck-results
          path: tck-results.txt
```

---

## Module dependency tree sau Perfect Core

```
jainslee-api          (unchanged — just add @InitialEventSelect annotation)
    ↑
jainslee-tx           (NEW — SleeTransactionManager, Narayana)
    ↑
jainslee-codegen      (NEW — ConcreteSbbGenerator, Javassist)
    ↑
jainslee-core         (update EventRouter + VirtualThreadSbbEntityPool)
    ↑                  wire: IES + CascadeRemover + ConcreteSbbGenerator + JTA
jainslee-ra-spi       (update — SleeEndpointImpl, RaEntityStateMachine, RAContext)
    ↑
adapters/             (Quarkus, Spring Boot — minor: add JTA bean + RA wiring)
```

---

## Estimated LOC per sprint

| Sprint | LOC mới | Files chính |
|--------|---------|-------------|
| S1 JTA | ~400 | SleeTransactionManager + EventRouter update |
| S2 Codegen | ~600 | ConcreteSbbGenerator + JavassistDeployTimeCodegen |
| S3 IES | ~500 | IESDispatcher + 3 value types + EventRouter update |
| S4 Child | ~600 | CascadeRemover + ChildRelationImpl + ChildRelation IF |
| S5 RA | ~800 | SleeEndpointImpl + RaStateMachine + RAContextImpl |
| S6 TCK | ~300 | TckHarness updates + CI yaml |
| **Total** | **~3.200** | |

Nhỏ hơn estimate ban đầu (~5.4K) vì nhiều infrastructure (ACNF, Timer, AlarmFacility)
đã tồn tại trong codebase — chỉ cần wire, không cần rewrite.
```


## GOAL 1-5 — registerRa Pattern (3-Port Contract)

### File cần biết: `MicroSleeContainer.java` (GOAL 2 section, dòng ~1000)

`registerRa(RaEndpointPort, RaCommandPort)` là entry point của 3-Port Contract
API — cách mới để wire một local Resource Adaptor mà không cần thông qua
`ResourceAdaptorContext` / `RaEntityStateMachine` truyền thống.

### Flow khi gọi `container.registerRa(endpoint, command)`

```mermaid
sequenceDiagram
    autonumber
    participant APP as Application
    participant CT as MicroSleeContainer
    participant EP as RaEndpointPort
    participant BP as BootstrapPortAdapter
    participant SLEE as SleeEndpointPortImpl

    APP->>CT: registerRa(endpoint, command)
    CT->>CT: validate endpoint != null, command != null
    CT->>EP: name = endpoint.getRaName()
    CT->>CT: assert name non-blank
    CT->>CT: endpointPorts.put(name, endpoint)
    CT->>CT: raCommandPorts.put(name, command)
    alt container.state == STARTED
        CT->>SLEE: new SleeEndpointPortImpl(container, name)
        CT->>BP: new BootstrapPortAdapter(sleeEndpoint)
        CT->>EP: endpoint.activate(bootstrap)
        Note over EP: RA opens sockets, starts listeners
    else container chưa start
        Note over CT: defer — activate trong start()
    end
```

### Code mẫu — RA implement cả 2 port

```java
// RA tự implement cả RaEndpointPort và RaCommandPort
public class HttpIngressRa implements RaEndpointPort, RaCommandPort {

    private RaBootstrapPort bootstrap;
    private volatile boolean active;

    @Override public String getRaName() { return "http-ingress"; }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrap = bootstrap;
        this.active = true;
        // start HTTP server...
    }

    @Override
    public void deactivate() {
        this.active = false;
        // stop HTTP server...
    }

    @Override
    public void sendCommand(OutboundCommand cmd) {
        // handle outbound commands from SBB
    }

    // Called by HTTP handler thread
    public void onHttpRequest(String sessionId, String body) {
        if (!active) return;
        ActivityHandle handle = bootstrap.createActivityHandle(sessionId);
        bootstrap.fireEvent(
            new HttpRequestEvent(body), handle, new StringAddress(sessionId));
    }
}

// Wire vào container
MicroSleeContainer container = ...;
HttpIngressRa ra = new HttpIngressRa();
container.registerRa(ra, ra);                          // RA vừa là endpoint, vừa là command port
container.mapEventToSbb(HttpRequestEvent.class, "HttpSbb");
```

### `BootstrapPortAdapter` — internal bridge

`BootstrapPortAdapter` là `private static final class` trong `MicroSleeContainer`,
implement `RaBootstrapPort`. Nó đóng vai trò adapter giữa interface sạch mà RA nhìn
thấy và implementation thực tế (`SleeEndpointPortImpl`).

```
RaEndpointPort.activate(bootstrap)
        │
        ▼
BootstrapPortAdapter (implements RaBootstrapPort)
        │
        │  createActivityHandle(id)
        ├──► SleeEndpointPortImpl.startActivity(RaActivityHandle, null)
        │       → đăng ký activity với ACNF
        │
        │  fireEvent(event, handle, address)
        └──► SleeEndpointPortImpl.fireEvent(ActivityContextHandle, event)
                → publish vào Disruptor ring buffer → EventRouter
```

Tại sao cần adapter này?

1. **Encapsulation** — RA không cần biết `ActivityContextHandle`, `SleeEndpointPortImpl`
   hay bất kỳ internal class nào của `jainslee-core`.
2. **Testability** — mock `RaBootstrapPort` dễ hơn mock `SleeEndpointPortImpl` khi unit-test RA.
3. **Single responsibility** — `BootstrapPortAdapter` chỉ làm một việc: translate
   từ `RaBootstrapPort` API sang `SleeEndpointPortImpl` API.

### So sánh: registerRa vs registerResourceAdaptor

| | `registerRa(endpoint, command)` | `registerResourceAdaptor(name, ra)` |
|---|---|---|
| API style | 3-port contract (PolyVoice) | JAIN SLEE §12.4 truyền thống |
| Lifecycle | `activate(bootstrap)` / `deactivate()` | `raConfigure → raActive → raStopping → raInactive → raUnconfigure` |
| State machine | Không — RA tự quản lý state | `RaEntityStateMachine` (INACTIVE → ACTIVE → STOPPING → INACTIVE) |
| Outbound commands | `RaCommandPort.sendCommand()` | Không có sẵn — SBB phải tự lookup RA entity |
| Activity creation | `bootstrap.createActivityHandle(id)` | `NullActivityFactory.createNullActivityHandle()` |
| Phù hợp cho | Local / embedded RA, test harness | Production RA với full lifecycle validation |

Cả hai đường dẫn đều được hỗ trợ trong micro-jainslee 1.2.0-P1.
`registerRa` được khuyến nghị cho code mới vì đơn giản hơn và dễ test hơn;
`registerResourceAdaptor` dành cho migration từ code JAIN SLEE 1.1 legacy.

### Test verify

```bash
# GOAL 1-5 3-port contract tests
mvn -pl jainslee-core test -Dtest='MicroSleeContainerTest#registerRa*'
# → assert endpointPorts + raCommandPorts populated
# → assert activate() called with non-null bootstrap
# → assert hot-register works when container already STARTED
# → assert deactivate() called on container stop
```
