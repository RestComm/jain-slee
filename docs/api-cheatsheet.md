# JAIN SLEE 1.1 — API Cheat Sheet cho Junior Developers
## (Vietnamese + English — Telecom-Ready Reference)

> **Target**: Developers mới làm quen JAIN SLEE 1.1, cần tra nhanh interface, lifecycle, RA pattern.  
> Tất cả code examples được viết theo mô hình thực tế trong USSD 7.3 / micro-jainslee / RestComm SLEE 8.

---

## Table of Contents

1. [Sbb Interface & Lifecycle](#1-sbb-interface--lifecycle)
2. [Annotations](#2-annotations-sbb-eventtype-resourceadaptor)
3. [ActivityContextInterface (ACI)](#3-activitycontextinterface-aci)
4. [TimerFacility](#4-timerfacility)
5. [ResourceAdaptor Interface](#5-resourceadaptor-interface)
6. [FireableEventType](#6-fireableeventtype)
7. [InitialEventSelector](#7-initialeventselector)
8. [ProfileFacility](#8-profilefacility)
9. [SbbLocalObject & Child SBB](#9-sbblocalobject--child-sbb)
10. [NullActivityContextInterfaceFactory](#10-nullactivitycontextinterfacefactory)

---

## 1. Sbb Interface & Lifecycle

### Interface tổng quan (The big picture)

`javax.slee.Sbb` — mọi SBB abstract class phải implement interface này. Container gọi lifecycle methods tuần tự theo state machine:

```
                      ┌──────────────┐
   newInstance() ───▶ │   POOLED     │ ◀─── sbbRemove() returns
                      └──────┬───────┘
                      sbbCreate() │ (gán SBB Entity)
                      ┌──────▼───────┐
                      │    READY     │ ◀── event handlers chạy ở đây
                      └──────┬───────┘
                      sbbPassivate() │  sbbActivate()
                      ┌──────▼───────┐
                      │   PASSIVE    │
                      └──────────────┘
```

### Các method cốt lõi

| Method | Khi nào được gọi (When called) | State chạy |
|--------|-------------------------------|------------|
| `setSbbContext(SbbContext)` | Ngay sau khi newInstance. Inject context. | POOLED |
| `sbbCreate()` | Tạo SBB Entity mới. Có transaction context. | POOLED → READY |
| `sbbPostCreate()` | Sau sbbCreate(), cho init thêm. | POOLED → READY |
| `sbbActivate()` | SBB từ PASSIVE quay lại READY (unpassivate). | PASSIVE → READY |
| `sbbPassivate()` | SBB bị đẩy ra khỏi memory (passivate). Lưu CMP. | READY → PASSIVE |
| `sbbRemove()` | Xóa SBB entity. Cleanup resources. | READY → POOLED |
| `unsetSbbContext()` | Ngay trước khi return vào pool. Nullify refs. | POOLED |
| `sbbLoad()` | Load CMP fields từ persistent store. | Gọi trước activation |
| `sbbStore()` | Store CMP fields vào persistent store. | Gọi sau passivation |
| `sbbExceptionThrown()` | Exception handler per-SBB. | READY |

### Code Example: Clean SBB Lifecycle

```java
package com.example.slee.sbb;

import javax.slee.*;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerFacility;

/**
 * SBB xử lý USSD Gateway session. (Vietnamese example)
 * Mỗi instance gắn với một MAP Dialog.
 */
public abstract class UssdSessionSbb implements Sbb {

    // ── CMP fields (Container-Managed Persistence) ──
    public abstract String getSessionId();
    public abstract void setSessionId(String id);
    public abstract TimerID getDialogTimerID();
    public abstract void setDialogTimerID(TimerID timerID);

    // ── RA Interface (abstract — SLEE generates impl) ──
    public abstract MapRaSbbInterface getMapRa();

    // ── Child Relation ──
    public abstract ChildRelation getChildRelation();

    // ────────────── LIFECYCLE ──────────────

    public void setSbbContext(SbbContext ctx) {
        this.sbbContext = ctx;           // Bước 1: Inject context
    }

    public void sbbCreate() throws CreateException {
        setSessionId("session-" + System.currentTimeMillis());
        // Set timer để tránh dialog treo (leak prevention)
        TimerFacility timerFacility = getSbbContext().getTimerFacility();
        TimerID tid = timerFacility.setTimer(
            null, null,
            System.currentTimeMillis() + 30000, new TimerOptions());
        setDialogTimerID(tid);
    }

    public void sbbActivate() {
        // KHÔNG set Timer ở đây vì không có transaction!
    }

    public void sbbPassivate() {
        // CMP fields được container tự động lưu
    }

    public void sbbRemove() {
        // ⚠️ QUAN TRỌNG: Cancel timers ở đây để tránh timer leak!
        TimerID tid = getDialogTimerID();
        if (tid != null) {
            try {
                getSbbContext().getTimerFacility().cancelTimer(tid);
            } catch (Exception e) { /* log */ }
        }
    }

    public void unsetSbbContext() {
        this.sbbContext = null;
    }

    private SbbContext sbbContext;
    private SbbContext getSbbContext() { return sbbContext; }
}
```

### 🔑 Pattern quan trọng (Key patterns)

1. **sbbCreate() luôn set timer** — nếu event handler không cancel kịp, timer sẽ cleanup.
2. **sbbRemove() luôn cancel timer** — tránh timer leak (timer chạy trên SBB đã removed).
3. **Không lookup RA trong setSbbContext()** — RA chưa sẵn sàng. Lookup trong `sbbCreate()`.
4. **Không set timer trong sbbActivate()** — không có transaction context.

---

## 2. Annotations (@Sbb, @EventType, @ResourceAdaptor)

Mobicents/RestComm hỗ trợ annotation-based thay cho XML deployment descriptor.  
Annotations nằm trong package `org.mobicents.slee.annotations`.

### @Sbb

```java
@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface Sbb {
    String name();                          // REQUIRED: tên SBB
    String vendor();                        // REQUIRED
    String version();                       // REQUIRED
    String alias() default "";              // Alias trong JNDI
    Class<? extends SbbLocalObject> localInterface()
        default SbbLocalObject.class;
    Class<? extends ActivityContextInterface> activityContextInterface()
        default ActivityContextInterface.class;
    LibraryRef[] libraryRefs() default {};
    SbbRef[] sbbRefs() default {};          // Tham chiếu SBB khác
    ProfileSpecRef[] profileSpecRefs() default {};
    ConfigProperty[] properties() default {};
}
```

### @EventType (Marker trên event class)

```java
@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface EventType {
    String name();
    String vendor();
    String version();
}
```

### @ResourceAdaptor (Marker trên RA class)

```java
@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptor {
    String name();    String vendor();    String version();
    boolean ignoreRATypeEventTypeCheck() default false;
    boolean supportsActiveReconfiguration() default false;
    Class<?> usageParametersInterface() default Object.class;
}
```

### Complete Annotated Example

```java
// ── Event Class ──
@EventType(name = "UssdRequestEvent", vendor = "com.example", version = "1.0")
public class UssdRequestEvent {
    private final String msisdn, ussdString;
    public UssdRequestEvent(String msisdn, String ussdString) {
        this.msisdn = msisdn; this.ussdString = ussdString;
    }
    public String getMsisdn() { return msisdn; }
    public String getUssdString() { return ussdString; }
}

// ── SBB Class ──
@Sbb(
    name = "UssdGatewaySbb", vendor = "com.example", version = "1.0",
    localInterface = UssdGatewaySbbLocalObject.class,
    activityContextInterface = UssdGatewayAci.class,
    sbbRefs = {@SbbRef(name="CdrChildSbb",vendor="com.example",version="1.0",alias="cdrChild")},
    properties = {@ConfigProperty(name="dialogTimeout", type=Integer.class)}
)
public abstract class UssdGatewaySbb implements Sbb {

    @EventHandler(eventTypeRef = @EventTypeRef(
        name="UssdRequestEvent", vendor="com.example", version="1.0"))
    public abstract void onUssdRequestEvent(UssdRequestEvent event, UssdGatewayAci aci);

    @TimerEventHandler
    public abstract void onTimerEvent(TimerEvent event);

    @FireEvent(eventTypeRef = @EventTypeRef(
        name="UssdResponseEvent", vendor="com.example", version="1.0"))
    public abstract void fireUssdResponseEvent(
        UssdResponseEvent event, ActivityContextInterface aci, Address defaultAddress);

    @InitialEventSelectorMethod
    public abstract InitialEventSelector selectInitialEvent(InitialEventSelector ies);

    public abstract String getSessionId();
    public abstract void setSessionId(String id);

    @Override public void sbbCreate() throws CreateException { /* init */ }
    @Override public void sbbRemove() { /* cleanup */ }
    @Override public void setSbbContext(SbbContext ctx) { /* injection */ }
    @Override public void unsetSbbContext() { /* cleanup */ }
}

// ── Supporting interfaces ──
interface UssdGatewaySbbLocalObject extends SbbLocalObject {
    void sendUssdResponse(String sessionId, String responseText);
}
interface UssdGatewayAci extends ActivityContextInterface {
    String getSessionId();  void setSessionId(String id);
}
```

### @ResourceAdaptor Complete Example

```java
@ResourceAdaptor(name = "GrpcAsResourceAdaptor", vendor = "com.example",
    version = "1.0", supportsActiveReconfiguration = true)
public class GrpcAsResourceAdaptor implements ResourceAdaptor {
    private ResourceAdaptorContext raContext;
    private SleeEndpoint sleeEndpoint;
    private io.grpc.Server grpcServer;

    @Override
    public void setResourceAdaptorContext(ResourceAdaptorContext ctx) {
        this.raContext = ctx;
        this.sleeEndpoint = ctx.getSleeEndpoint();
    }

    @Override
    public void unsetResourceAdaptorContext() {
        this.raContext = null; this.sleeEndpoint = null;
    }

    @Override
    public void raConfigure(ConfigProperties props) {
        String port = (String) props.getProperty("port").getValue();
        System.setProperty("grpc.port", port);
    }

    @Override
    public void raActive() { startGrpcServer(); }

    @Override
    public void raStopping() { /* stop accepting new connections */ }

    @Override
    public void raInactive() { shutdownGrpcServer(); }

---

## 3. ActivityContextInterface (ACI)

`javax.slee.ActivityContextInterface` — đại diện cho một Activity Context trong SLEE.

```java
public interface ActivityContextInterface {
    Object getActivity();                         // Activity object từ RA
    void attach(SbbLocalObject sbb);              // Gắn SBB vào Activity
    void detach(SbbLocalObject sbb);              // Gỡ SBB khỏi Activity
    boolean isAttached(SbbLocalObject sbb);       // Kiểm tra attach (SLEE 1.1)
    TimerFacility getTimerFacility();             // Access Timer (SLEE 1.1)
}
```

⚠️ Tất cả methods là **mandatory transactional methods**.

### Usage: Attach/Detach trong Event Handler

```java
public void onDialogAccepted(MapDialogAcceptedEvent event,
        ActivityContextInterface aci) {
    // (1) Attach SBB vào Activity
    SbbLocalObject self = getSbbContext().getSbbLocalObject();
    if (!aci.isAttached(self)) {
        aci.attach(self);
    }
    // (2) Get Activity object từ RA
    MapDialog dialog = (MapDialog) aci.getActivity();
    // (3) Set timer qua ACI
    TimerFacility tf = aci.getTimerFacility();        // SLEE 1.1 shortcut
    TimerID tid = tf.setTimer(aci, null,
        System.currentTimeMillis() + 30000, new TimerOptions());
    setDialogTimerID(tid);
}

public void cleanupDialog(ActivityContextInterface aci) {
    aci.detach(getSbbContext().getSbbLocalObject());
}
```

### Pattern: Tạo ACI từ RA Activity

```java
// B1: Lấy RA SBB Interface
MapRaSbbInterface mapRa = getMapRa();
// B2: Tạo Activity mới
MapDialogActivity activity = mapRa.createActivity();
// B3: ACI factory wrap thành ACI
ActivityContextInterface aci = getMapAciFactory().getActivityContextInterface(activity);
// B4: Attach SBB
aci.attach(getSbbContext().getSbbLocalObject());
```

---

## 4. TimerFacility

`javax.slee.facilities.TimerFacility` — transactional timer facility. Timers chỉ được set/cancel khi transaction commit thành công.

```java
public interface TimerFacility {
    String JNDI_NAME = "java:comp/env/slee/facilities/timer";

    TimerID setTimer(ActivityContextInterface aci, Address address,
                     long expireTime, TimerOptions options);
    TimerID setTimer(ActivityContextInterface aci, Address address,
                     long startTime, long period, int numRepetitions,
                     TimerOptions options);
    void cancelTimer(TimerID timerID);
    ActivityContextInterface getActivityContextInterface(TimerID timerID);
    long getResolution();
    long getDefaultTimeout();
}
```

### Timer Event Handler Signature

```java
public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
    // event.getTimerID() — ID của timer đã fire
    // aci — Activity Context mà timer được set trên đó
}
```

### ⚠️ Pattern: Timer cleanup để tránh leak

```java
public abstract class UssdSessionSbb implements Sbb {
    public abstract TimerID getDialogTimerID();
    public abstract void setDialogTimerID(TimerID tid);

    public void sbbCreate() throws CreateException {
        // Bootstrap với NullActivity khi chưa có Activity thật
        NullActivity na = getSbbContext().getNullActivityFactory().createNullActivity();
        ActivityContextInterface nullAci = getSbbContext()
            .getNullActivityContextInterfaceFactory().getActivityContextInterface(na);

        TimerID tid = getSbbContext().getTimerFacility().setTimer(
            nullAci, null, System.currentTimeMillis() + 30000, new TimerOptions());
        setDialogTimerID(tid);
        nullAci.attach(getSbbContext().getSbbLocalObject());
    }

    public void sbbRemove() {
        TimerID tid = getDialogTimerID();
        if (tid != null) {
            try {
                getSbbContext().getTimerFacility().cancelTimer(tid);
            } catch (Exception e) { /* log — best-effort cleanup */ }
            setDialogTimerID(null);
        }
    }

    // Reset timer (cancel old + set new)
    private void resetDialogTimer(ActivityContextInterface aci) {
        TimerID oldTid = getDialogTimerID();
        if (oldTid != null) {
            try { getSbbContext().getTimerFacility().cancelTimer(oldTid); }
            catch (Exception e) { /* ignore */ }
        }
        TimerID newTid = getSbbContext().getTimerFacility().setTimer(
            aci, null, System.currentTimeMillis() + 30000, new TimerOptions());
        setDialogTimerID(newTid);
    }
}
```

### 🔑 Timer Rules

1. **Luôn lưu TimerID vào CMP field** — để cleanup khi passivate/reactivate.
2. **Luôn cancel trong `sbbRemove()`** — nếu không, timer leak vĩnh viễn.
3. **Dùng `getSbbContext().getTimerFacility()` để cancel** — ACI có thể đã detached.
4. **Timer với `numRepetitions=0`** là infinite timer — PHẢI cancel thủ công.

---

## 5. ResourceAdaptor Interface

`javax.slee.resource.ResourceAdaptor` — lifecycle của một Resource Adaptor entity.

```java
public interface ResourceAdaptor {
    void setResourceAdaptorContext(ResourceAdaptorContext context);
    void unsetResourceAdaptorContext();
    void raConfigure(ConfigProperties properties);
    void raUnconfigure();
    void raActive();                             // Start creating activities
    void raStopping();                           // Stop creating new activities
    void raInactive();                           // Cleanup resources
    void raVerifyConfiguration(ConfigProperties props)
        throws InvalidConfigurationException;
    void raConfigurationUpdate(ConfigProperties props);
}
```

### ResourceAdaptorContext

```java
public interface ResourceAdaptorContext {
    SleeEndpoint getSleeEndpoint();              // Fire event vào SLEE
    TimerFacility getTimerFacility();            // RA dùng Timer
    AlarmFacility getAlarmFacility();            // RA raise alarm
    EventLookupFacility getEventLookupFacility();// Lookup EventTypeID
}
```

### gRPC RA Example

```java
public class GrpcAsResourceAdaptor implements ResourceAdaptor {
    private ResourceAdaptorContext raContext;
    private Server grpcServer;
    private EventTypeID incomingRequestEventID;

    @Override
    public void setResourceAdaptorContext(ResourceAdaptorContext ctx) {
        this.raContext = ctx;
    }
    @Override
    public void unsetResourceAdaptorContext() { this.raContext = null; }

    @Override
    public void raConfigure(ConfigProperties props) {
        int port = Integer.parseInt((String) props.getProperty("port").getValue());
    }

    @Override
    public void raActive() {
        // Lookup EventTypeID SAU khi Active (events đã deployed)
        this.incomingRequestEventID = raContext.getEventLookupFacility()
            .getEventTypeID("UssdRequestEvent", "com.example", "1.0");
        startGrpcServer();
    }

    @Override
    public void raStopping() {
        if (grpcServer != null) grpcServer.shutdown(); // Graceful
    }

    @Override
    public void raInactive() {
        if (grpcServer != null && !grpcServer.isTerminated())
            grpcServer.shutdownNow();
        grpcServer = null;
    }

    /** Fire event vào SLEE */
    void fireIncomingRequest(byte[] payload, String sessionId) {
        try {
            GrpcActivityHandle handle = new GrpcActivityHandle(sessionId);
            UssdRequestEvent event = new UssdRequestEvent(payload, sessionId);
            raContext.getSleeEndpoint().fireEvent(
                handle, incomingRequestEventID, event,
                null, null, EventFlags.NO_FLAGS);
        } catch (Exception e) { /* log — RA must not throw */ }
    }
}
```

### HTTP RA Example (Simplified)

```java
public class HttpServerResourceAdaptor implements ResourceAdaptor {
    private OkHttpClient httpClient;

    @Override public void raActive() {
        httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS).build();
    }
    @Override public void raStopping() { /* stop accept */ }
    @Override public void raInactive() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
            httpClient = null;
        }
    }
}
```

### 🔑 RA Rules
1. **Lookup EventTypeID trong `raActive()`** — events chưa deployed trước đó.
2. **Không tạo activity trong `raStopping()`** — chỉ cleanup.
3. **Luôn `try-catch` khi fire event** — RA không được để exception escape.

---

## 6. FireableEventType

`javax.slee.resource.FireableEventType` — RA dùng để mô tả event type.

```java
public interface FireableEventType {
    EventTypeID getEventType();
    String getEventClassName();
    ClassLoader getEventClassLoader();
}
```

### Cách RA sử dụng qua SleeEndpoint

```java
// Cách 1: EventTypeID trực tiếp (phổ biến nhất)
EventTypeID etid = raContext.getEventLookupFacility()
    .getEventTypeID("UssdRequestEvent", "com.example", "1.0");
raContext.getSleeEndpoint().fireEvent(
    activityHandle, etid, eventObject, null, null, EventFlags.NO_FLAGS);

// Cách 2: FireableEventType khi cần custom classloader
FireableEventType fet = raContext.getEventLookupFacility()
    .getFireableEventType(etid);
String className = fet.getEventClassName();
```

### Custom Event Definition

```java
/** Mọi SLEE event object phải implement Serializable (cluster replication) */
@EventType(name = "UssdRequestEvent", vendor = "com.example", version = "1.0")
public class UssdRequestEvent implements Serializable {
    private static final long serialVersionUID = 1L;  // ⚠️ LUÔN khai báo

    private final String msisdn;
    private final String ussdString;
    private final String sessionId;
    private final long timestamp;

    public UssdRequestEvent(String msisdn, String ussdString, String sessionId) {
        this.msisdn = msisdn;
        this.ussdString = ussdString;
        this.sessionId = sessionId;
        this.timestamp = System.currentTimeMillis();
    }
    // Getters only (immutable pattern)
    public String getMsisdn() { return msisdn; }
    public String getUssdString() { return ussdString; }
    public String getSessionId() { return sessionId; }
    public long getTimestamp() { return timestamp; }
}
```

---

## 7. InitialEventSelector

`javax.slee.InitialEventSelector` — cho phép SBB quyết định cách routing initial event.

```java
public interface InitialEventSelector {
    boolean isActivityContextSelected();
    void setActivityContextSelected(boolean select);

    boolean isAddressProfileSelected();
    void setAddressProfileSelected(boolean select);

    boolean isAddressSelected();
    void setAddressSelected(boolean select);

    Address getAddress();
    void setAddress(Address address);
}
```

### Usage: Map event type → SBB routing

```java
public abstract class UssdGatewaySbb implements Sbb {

    /**
     * Initial Event Selector method.
     * Chạy trên SBB trong POOLED state — không có transaction.
     * @param ies initial event selector object từ SLEE
     * @return modified ies
     */
    public InitialEventSelector selectUssdInitialEvent(
            InitialEventSelector ies) {
        // (1) Các event trên cùng Activity sẽ tới cùng SBB entity
        ies.setActivityContextSelected(true);
        // (2) KHÔNG dùng Address hoặc Address Profile
        ies.setAddressSelected(false);
        ies.setAddressProfileSelected(false);
        return ies;
    }
}
```

### 🔑 Rules
1. Method phải return `InitialEventSelector` (không void).
2. Trả về chính object `ies` (đã modified).
3. Được gọi trên SBB trong **POOLED state** — không transaction context.
4. Nếu `isActivityContextSelected() == true`, events trên cùng Activity → cùng SBB entity.

### XML Configuration (nếu không dùng annotations)

```xml
<sbb>
    <sbb-name>UssdGatewaySbb</sbb-name>
    <event>
        <event-name>UssdRequestEvent</event-name>
        <initial-event-select>True</initial-event-select>
        <initial-event-selector-method-name>
            selectUssdInitialEvent
        </initial-event-selector-method-name>
    </event>
</sbb>
```

---

## 8. ProfileFacility

`javax.slee.profile.ProfileFacility` — truy vấn profile database. Thay thế cho Profile CMP Interface (deprecated).

```java
public interface ProfileFacility {
    String JNDI_NAME = "java:comp/env/slee/facilities/profile";

    // Deprecated — dùng getProfileTable() thay thế
    Collection getProfiles(String profileTableName);
    Collection findProfilesByAttribute(String table, String attr, Object value);

    // SLEE 1.1 — Modern API
    ProfileTable getProfileTable(String profileTableName);
}
```

### ProfileTable Interface

```java
public interface ProfileTable {
    ProfileLocalObject find(ProfileID profileID);
    Collection findAll();
    Collection findByAttribute(String attr, Object value);
    ProfileLocalObject getDefaultProfile();
    int size();
}
```

### Usage Example

```java
public abstract class UssdRoutingSbb implements Sbb {

    private String findUssdRoute(String msisdn) {
        try {
            ProfileTable routingTable = getSbbContext()
                .getProfileFacility().getProfileTable("UssdRoutingTable");

            // Tìm profile theo MSISDN
            Collection<ProfileLocalObject> profiles = routingTable
                .findByAttribute("msisdn", msisdn);

            for (ProfileLocalObject profile : profiles) {
                String route = (String) profile.getAttribute("ussdRoute");
                if (route != null) return route;
            }

            // Fallback default
            ProfileLocalObject def = routingTable.getDefaultProfile();
            if (def != null)
                return (String) def.getAttribute("defaultUssdRoute");
        } catch (Exception e) { /* log */ }
        return "DEFAULT_GW";
    }
}
```

### 🔑 Profile Rules
1. **Dùng `getProfileTable()` thay vì `getProfiles()`** — API 1.0 đã deprecated.
2. Profile lookup là transactional — cần transaction context.
3. Mọi profile table phải có ít nhất 1 default profile.

---

## 9. SbbLocalObject & Child SBB

`javax.slee.SbbLocalObject` — interface cơ bản cho mọi SBB local interface.

```java
public interface SbbLocalObject {
    boolean isIdentical(SbbLocalObject obj);       // So sánh identity
    void setSbbPriority(byte priority);            // Set priority (-128 to 127)
    byte getSbbPriority();                         // Get priority
    void remove();                                 // Xóa SBB entity (cascade)
}
```

### Custom SbbLocalObject

```java
public interface CdrChildSbbLocalObject extends SbbLocalObject {
    void generateCdr(String sessionId, String msisdn,
                     String ussdString, long duration);
    boolean isCdrGenerated(String sessionId);
}
```

### ChildRelation — Tạo và quản lý Child SBB

```java
public abstract class UssdGatewaySbb implements Sbb {
    public abstract ChildRelation getCdrChildRelation();
    private CdrChildSbbLocalObject cdrChild;

    public void sbbCreate() throws CreateException {
        ChildRelation cr = getCdrChildRelation();
        CdrChildSbbLocalObject child = (CdrChildSbbLocalObject) cr.create();
        child.generateCdr(getSessionId(), getMsisdn(), getUssdString(), 0);
        this.cdrChild = child;
    }

    public void sbbRemove() {
        // Cascade: parent bị remove → children tự động bị remove
        ChildRelation cr = getCdrChildRelation();
        if (cr.size() > 0 && cdrChild != null) {
            try { cdrChild.generateCdr(getSessionId(), getMsisdn(),
                    getUssdString(), calculateDuration()); }
            catch (Exception e) { /* log */ }
        }
    }

    public void onUssdEndEvent(UssdEndEvent event, ActivityContextInterface aci) {
        if (cdrChild != null) {
            cdrChild.generateCdr(getSessionId(), getMsisdn(),
                getUssdString(), event.getDuration());
        }
    }
}
```

### 🔑 Child SBB Rules
1. **Cascade remove** — parent removed → all children removed.
2. **Gọi child methods là synchronous** — block đến khi child xử lý xong.
3. **Không nên tạo quá nhiều child** — mỗi child là một transaction boundary.

---

## 10. NullActivityContextInterfaceFactory

Dùng khi SBB cần ACI nhưng chưa có Activity thực (VD: set timer trong `sbbCreate()`).

```java
public interface NullActivityContextInterfaceFactory {
    String JNDI_NAME =
        "java:comp/env/slee/nullactivity/activitycontextinterfacefactory";
    ActivityContextInterface getActivityContextInterface(NullActivity activity);
}
public interface NullActivityFactory {
    String JNDI_NAME = "java:comp/env/slee/nullactivity/activityfactory";
    NullActivity createNullActivity();
}
```

### Full Usage Pattern

```java
public abstract class UssdSessionSbb implements Sbb {
    private ActivityContextInterface bootstrapAci;

    public void sbbCreate() throws CreateException {
        NullActivity nullActivity = getSbbContext()
            .getNullActivityFactory().createNullActivity();
        this.bootstrapAci = getSbbContext()
            .getNullActivityContextInterfaceFactory()
            .getActivityContextInterface(nullActivity);
        bootstrapAci.attach(getSbbContext().getSbbLocalObject());

        TimerID tid = getSbbContext().getTimerFacility().setTimer(
            bootstrapAci, null, System.currentTimeMillis() + 30000,
            new TimerOptions());
        setDialogTimerID(tid);
    }

    public void sbbRemove() {
        if (bootstrapAci != null) {
            try { bootstrapAci.detach(getSbbContext().getSbbLocalObject()); }
            catch (Exception e) { /* ignore */ }
            bootstrapAci = null;
        }
    }
}
```

### 🔑 NullActivity Rules
1. **Dùng khi chưa có Activity thực** — bootstrap phase, `sbbCreate()`.
2. **Luôn detach khi không dùng nữa** — tránh reference leak.
3. **Timer trên NullActivity vẫn hoạt động** — container xử lý delivery.

---

## 11. 3-Port Contract API (GOAL 1-5 ✅)

> **micro-jainslee 1.2.0+** — Các interface mới thay thế abstract RA accessor và `ResourceAdaptor` cũ.  
> Package: `com.microjainslee.api` / `com.microjainslee.api.annotations`

### 11.1 RaEndpointPort — RA Lifecycle Interface

```java
package com.microjainslee.api;

/**
 * 3-port contract — RA expose interface này cho container.
 * Container gọi activate(bootstrap) khi start, deactivate() khi stop.
 */
public interface RaEndpointPort {
    void activate(RaBootstrapPort bootstrap);
    void deactivate();
    String getRaName();   // Logical RA entity name, unique trong container
}
```

**Usage — RA implement RaEndpointPort:**

```java
public class UssdGatewayRa implements RaEndpointPort, RaCommandPort {

    private RaBootstrapPort bootstrap;

    @Override public String getRaName() { return "ussd-gateway"; }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrap = bootstrap;
        startSs7Stack();   // Mở SS7 connection
    }

    @Override
    public void deactivate() {
        stopSs7Stack();
        this.bootstrap = null;
    }

    // ... RaCommandPort implementation
}
```

### 11.2 RaCommandPort — SBB → RA Command Channel

```java
package com.microjainslee.api;

/**
 * SBB-facing half của 3-port contract. SBB gọi sendCommand()
 * để gửi outbound command tới RA.
 */
public interface RaCommandPort {
    void sendCommand(OutboundCommand command);
}
```

**Usage — SBB sends command:**

```java
@InjectRa(name = "ussd-gateway")
private RaCommandPort ussdRa;

public void onUssdRequest(UssdRequestEvent event, ActivityContextInterface aci) {
    ussdRa.sendCommand(new SendUssdResponseCommand(sessionId, "Welcome!"));
}
```

### 11.3 RaBootstrapPort — Container → RA Primitives

```java
package com.microjainslee.api;

/**
 * Bootstrap port handed to RA during activate().
 * Cung cấp 2 primitives: createActivityHandle + fireEvent.
 */
public interface RaBootstrapPort {
    ActivityHandle createActivityHandle(String id);
    void fireEvent(SleeEvent event, ActivityHandle handle, Address address);
}
```

**Usage — RA fires event into SLEE:**

```java
// Trong RA khi nhận incoming SS7 message
ActivityHandle handle = bootstrap.createActivityHandle(dialog.getDialogId());
SleeEvent event = new UssdBeginEvent(msisdn, ussdString, dialog.getDialogId());
bootstrap.fireEvent(event, handle, new Address(msisdn));
```

### 11.4 OutboundCommand — Marker Interface

```java
package com.microjainslee.api;

/**
 * Marker interface cho outbound command từ SBB → RA.
 * Mỗi protocol RA định nghĩa concrete command types.
 */
public interface OutboundCommand {
}
```

**Usage — Define protocol-specific commands:**

```java
record SendUssdResponseCommand(String sessionId, String ussdText)
    implements OutboundCommand {}

record StartCallCommand(String caller, String callee)
    implements OutboundCommand {}

record SendSmsCommand(String msisdn, String text)
    implements OutboundCommand {}
```

### 11.5 @InjectRa — Annotation Injection

```java
package com.microjainslee.api.annotations;

/**
 * Inject RaCommandPort vào SBB field. Container resolve RA
 * theo annotation's name() value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface InjectRa {
    String name() default "";  // RA entity name; "" = auto-infer
}
```

**Usage — SBB with @InjectRa:**

```java
public class PolyVoiceSbb implements Sbb {

    @InjectRa(name = "ussd-gateway")
    private RaCommandPort ussdRa;

    @InjectRa         // name="" → container infers from field type or default
    private RaCommandPort sipRa;

    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        ussdRa.sendCommand(new SendUssdResponseCommand(sid, "OK"));
    }
}
```

### 11.6 Container Wiring — registerRa() + mapEventToSbb()

```java
MicroSleeContainer container = MicroSleeContainer.create(config);

// 1. Tạo RA (cùng class implement cả endpoint + command port)
UssdGatewayRa ussdRa = new UssdGatewayRa();

// 2. GOAL 2 — Register RA qua 3-port contract
container.registerRa(ussdRa, ussdRa);   // (RaEndpointPort, RaCommandPort)

// 3. GOAL 2 — Map event type → SBB cho convergent routing
container.mapEventToSbb(UssdBeginEvent.class, "UssdSessionSbb");

// 4. Container tự gọi endpoint.activate(bootstrap) khi start()
container.start();

// ...khi shutdown...
container.stop();  // Container gọi endpoint.deactivate()
```

### 11.7 Comparison Table (Old vs New)

| Feature | Old (JAIN SLEE 1.0) | New GOAL 1-5 |
|---------|---------------------|--------------|
| RA Interface | `javax.slee.resource.ResourceAdaptor` (5 lifecycle methods) | `RaEndpointPort` (3 methods) |
| Fire Event | `raContext.getSleeEndpoint().fireEvent(handle, etid, event, ...)` | `bootstrap.fireEvent(event, handle, address)` |
| Activity Handle | Custom `ActivityHandle` subclass | `bootstrap.createActivityHandle(id)` |
| SBB → RA | Abstract `getXxxRa()` accessor method | `@InjectRa RaCommandPort` + `sendCommand()` |
| RA Discovery | JNDI lookup / `@ResourceAdaptor` annotation | `container.registerRa(endpoint, command)` |
| Event Routing | SBB manually select | `container.mapEventToSbb(eventType, sbbName)` |

---

## Summary: Full SBB Lifecycle Pattern

```java
public abstract class CompleteSbbExample implements Sbb {
    public abstract String getState();
    public abstract void setState(String state);
    public abstract TimerID getTimerID();
    public abstract void setTimerID(TimerID tid);
    private SbbContext ctx;

    @Override public void setSbbContext(SbbContext ctx) { this.ctx = ctx; }

    @Override public void sbbCreate() throws CreateException {
        setState("INIT");                     // ✅ Set CMP
        // ✅ Set timer, create child...
    }
    @Override public void sbbActivate() {
        // ❌ KHÔNG set timer (no tx) — ✅ Re-init transient
    }
    @Override public void sbbRemove() {
        TimerID tid = getTimerID();
        if (tid != null) {
            try { ctx.getTimerFacility().cancelTimer(tid); }
            catch (Exception e) { /* log */ }
        }
        // ✅ Child SBBs auto-removed (cascade)
    }
    @Override public void unsetSbbContext() { this.ctx = null; }

    public void onSomeEvent(SomeEvent ev, ActivityContextInterface aci) {
        aci.attach(ctx.getSbbLocalObject());      // ✅ Attach
        setState("PROCESSING");                   // ✅ CMP
        TimerID tid = ctx.getTimerFacility().setTimer(
            aci, null, System.currentTimeMillis()+5000, new TimerOptions());
        setTimerID(tid);                          // ✅ Timer on ACI
    }
    public void onTimerEvent(TimerEvent ev, ActivityContextInterface aci) {
        setState("TIMEOUT");
    }
}
```

---

## Quick Reference Table (Bảng tra nhanh)

| Interface | JNDI Name | Key Methods | Dùng khi |
|-----------|-----------|-------------|----------|
| `Sbb` | (abstract class) | `sbbCreate(), sbbRemove(), sbbActivate()` | Lifecycle |
| `ActivityContextInterface` | (qua RA factory) | `attach(), detach(), getActivity()` | Gắn/gỡ SBB vào Activity |
| `TimerFacility` | `...slee/facilities/timer` | `setTimer(), cancelTimer()` | Timeout, scheduling |
| `ProfileFacility` | `...slee/facilities/profile` | `getProfileTable()` | Tra cứu subscriber profile |
| `NullActivityContextInterfaceFactory` | `...nullactivity/...` | `getActivityContextInterface()` | Bootstrap ACI |
| `NullActivityFactory` | `...nullactivity/activityfactory` | `createNullActivity()` | Tạo NullActivity |
| `ResourceAdaptor` | (RA class) | `raActive(), raStopping(), raInactive()` | RA lifecycle |
| `SbbLocalObject` | `SbbContext.getSbbLocalObject()` | `remove(), setSbbPriority()` | Sync SBB call |
| `InitialEventSelector` | (passed to selector) | `setActivityContextSelected()` | Routing initial event |
| `FireableEventType` | `EventLookupFacility` | `getEventType(), getEventClassName()` | RA fire event |
| `ChildRelation` | (abstract in SBB) | `create(), size(), iterator()` | Parent-child SBB |

---

> **Remember (Ghi nhớ)**:
> - **Luôn `cancelTimer()` trong `sbbRemove()`** — timer leak là lỗi phổ biến nhất.
> - **Event objects phải `Serializable`** — SLEE có thể replicate.
> - **Không giữ reference đến SbbContext sau `unsetSbbContext()`** — lỗi NPE.
> - **Dùng `@Sbb`, `@EventType`, `@ResourceAdaptor` annotations** — nhanh hơn XML.
> - **Testing**: mock `SbbContext`, `TimerFacility`, `ActivityContextInterface`.