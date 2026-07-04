# 3-Port Contract API (GOAL 1-5)

> **ተመልካች፡** micro-jainslee ላይ አዲስ የ3-ወደብ ውል API ለመማር የሚፈልጉ መሐንዲሶች።
> **የተዘመነ፡** 2026-06-28
> **Branch:** `micro-jainslee`
> **ምንጭ፡** `jainslee-api/src/main/java/com/microjainslee/api/` እና `example/example-embedded-j25/`
> **የእንግሊዘኛ ስሪት፡** [`3-port-contract.md`](3-port-contract.md)

---

## ዝርዝር

1. [አጠቃላይ ማጠቃለያ](#1-አጠቃላይ-ማጠቃለያ)
2. [RaEndpointPort — የRA የህይወት ዑደት ወደብ](#2-raendpointport--የra-የህይወት-ዑደት-ወደብ)
3. [RaCommandPort — የSBB-ወደ-RA ትዕዛዝ ወደብ](#3-racommandport--የsbb-ወደ-ra-ትዕዛዝ-ወደብ)
4. [RaBootstrapPort — የRA ማስጀመሪያ ወደብ](#4-rabootstrapport--የra-ማስጀመሪያ-ወደብ)
5. [OutboundCommand — የወጪ ትዕዛዝ ምልክት በይነገጽ](#5-outboundcommand--የወጪ-ትዕዛዝ-ምልክት-በይነገጽ)
6. [@InjectRa — የRA ትዕዛዝ ወደብ መወጋት](#6-injectra--የra-ትዕዛዝ-ወደብ-መወጋት)
7. [registerRa() እና mapEventToSbb()](#7-registerra-እና-mapeventtosbb)
8. [የተሟላ የPolyVoice SBB ኮድ ምሳሌ](#8-የተሟላ-የpolyvoice-sbb-ኮድ-ምሳሌ)

---

## 1. አጠቃላይ ማጠቃለያ

**3-Port Contract API** ከ micro-jainslee 1.2.0 ጀምሮ የተዋወቀ አዲስ የፕሮግራሚንግ ሞዴል ነው። እያንዳንዱ ሪሶርስ አዳፕተር (RA) ከ SLEE መያዣ ጋር የሚገናኝባቸውን **ሶስት ወደቦች** ይገልጻል፦

| ወደብ | በይነገጽ | አቅጣጫ | ሚና |
|---|---|---|---|
| **ወደብ 1** | `RaEndpointPort` | መያዣ → RA | የRA የህይወት ዑደት (`activate`/`deactivate`) እና ስም |
| **ወደብ 2** | `RaCommandPort` | SBB → RA | SBB ወደ RA ትዕዛዞችን መላክ (`sendCommand`) |
| **ወደብ 3** | `RaBootstrapPort` | መያዣ → RA | RA ወደ መያዣው ክስተቶችን ማስኮት (`fireEvent`) እና የእንቅስቃሴ መያዣ መፍጠር (`createActivityHandle`) |

**PolyVoice** የሚለው ስም የመጣው እያንዳንዱ RA ከመያዣው ጋር በሶስት የተለያዩ \"ድምጾች\" ስለሚናገር ነው — የህይወት ዑደት፣ ትዕዛዝ፣ እና ማስጀመሪያ።

### GOAL 1-5 ማጠቃለያ

| GOAL | ርዕስ | ማብራሪያ | ሁኔታ |
|---|---|---|---|
| **GOAL 1** | 3-Port API በይነገጾች | `RaEndpointPort`, `RaCommandPort`, `RaBootstrapPort`, `OutboundCommand` በ `jainslee-api` | ✅ ተጠናቋል |
| **GOAL 2** | registerRa() + mapEventToSbb() | RA ማስመዝገብ እና ክስተት-ወደ-SBB ካርታ በ `MicroSleeContainer` | ✅ ተጠናቋል |
| **GOAL 3** | Programmatic SBB registration | SBBs በኮድ ማስመዝገብ (ያለ XML) | ✅ ተጠናቋል |
| **GOAL 4** | @InjectRa annotation | የ`RaCommandPort` መወጋት በSBB fields | ✅ ተጠናቋል |
| **GOAL 5** | Vendor RAs ማዘመን | `GrpcMenuRaEndpoint`, `HttpIngressRaEndpoint` በ3-port API ተዘምነዋል | ✅ ተጠናቋል |
| **ፈተናዎች** | 378 ፈተናዎች አልፈዋል | 0 ውድቀቶች | ✅ አረንጓዴ |

### የ3-ወደብ ውል ካርታ

```
                  ┌─────────────────────────────┐
                  │     MicroSleeContainer       │
                  │  ┌─────────┐  ┌───────────┐  │
                  │  │EventRouter│  │RA Registry│  │
                  │  └────▲─────┘  └─────┬─────┘  │
                  └───────┼──────────────┼────────┘
                          │              │
             ወደብ 3      │              │  ወደብ 1
    fireEvent() ─────────┘              └─────→ activate(bootstrap)
                                                 deactivate()
              ┌──────────┐                      getRaName()
              │    RA    │
              │ (endpoint)│            ┌──────────┐
              └──────────┘            │   SBB    │
                    ▲                 │          │
                    │      ወደብ 2     │ @InjectRa│
                    └─────────────────│RaCmdPort │
                         sendCommand()│          │
                                      └──────────┘
```


---

## 2. RaEndpointPort — የRA የህይወት ዑደት ወደብ

### ማብራሪያ

`RaEndpointPort` እያንዳንዱ ሪሶርስ አዳፕተር ለማይክሮ-ጄይን-ስሊ መያዣ የሚያጋልጠው **የመጀመሪያ ደረጃ በይነገጽ** ነው። ከ20+ ሜተዶች ያለውን `javax.slee.resource.ResourceAdaptor` በመተካት **3 ሜተዶች ብቻ** ያቀርባል።

```
የህይወት ዑደት፦
  activate(bootstrap) ──→ [RA በስራ ላይ] ──→ deactivate()
```

| ሜተድ | መቼ ይጠራል | ማብራሪያ |
|---|---|---|
| `activate(RaBootstrapPort)` | RA ሲመዘገብ | መያዣው `RaBootstrapPort` ያስረክባል፤ RA I/O፣ ሰዓት ቆጣሪዎችን፣ ወዘተ ማስጀመር ይችላል |
| `deactivate()` | RA ሲቋረጥ | RA ሁሉንም የፕሮቶኮል ሃብቶች (ሶኬቶች፣ ሰዓት ቆጣሪዎች) መልቀቅ አለበት |
| `getRaName()` | በማንኛውም ጊዜ | የRAውን አመክንዮአዊ ስም ይመልሳል፤ መያዣው `OutboundCommand` ወደ ትክክለኛው RA ለማስተላለፍ ይጠቀምበታል |

### የበይነገጽ ፍቺ (ከምንጭ ኮድ)

```java
// ፓኬጅ፦ com.microjainslee.api
// ይህ 3-ወደብ ውል ነው — እያንዳንዱ RA ለመያዣው የሚያጋልጠው የመጀመሪያ ደረጃ API

public interface RaEndpointPort {

    // መያዣው bootstrap port ያስረክብና RAን ያነቃል
    void activate(RaBootstrapPort bootstrap);

    // RAን ያቦዝናል — ሁሉንም የፕሮቶኮል ሃብቶች መልቀቅ ግዴታ ነው
    void deactivate();

    // በSLEE መያዣ ውስጥ ልዩ የሆነውን አመክንዮአዊ የRA ስም ይመልሳል
    String getRaName();
}
```

### የአጠቃቀም ምሳሌ — HTTP Ingress RA

```java
// የHTTP መግቢያ ሪሶርስ አዳፕተር — RaEndpointPort ን ተግባራዊ ያደርጋል
public final class HttpIngressRaEndpoint implements RaEndpointPort {

    private RaBootstrapPort bootstrap;   // በ activate() ጊዜ ይቀበላል
    private HttpServer server;           // JDK HttpServer ማጣቀሻ
    private volatile boolean active;

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        this.bootstrap = bootstrap;
        this.server = HttpServer.create(new InetSocketAddress(18080), 0);
        this.server.start();
        this.active = true;
    }

    @Override
    public void deactivate() {
        this.active = false;
        if (server != null) server.stop(0);
        this.bootstrap = null;
    }

    @Override
    public String getRaName() {
        return "httpIngressRa";
    }
}
```

---

## 3. RaCommandPort — የSBB-ወደ-RA ትዕዛዝ ወደብ

### ማብራሪያ

`RaCommandPort` አንድ SBB ወደ ሪሶርስ አዳፕተር **የወጪ ትዕዛዞችን** የሚልክበት ወደብ ነው። ይህ የ3-ወደብ ውል የ **SBB-ተኮር** ግማሽ ነው (የRA-ተኮር ግማሹ `RaEndpointPort` ነው)።

| ሜተድ | መቼ ይጠራል | ማብራሪያ |
|---|---|---|
| `sendCommand(OutboundCommand)` | SBB ወደ RA መላክ ሲፈልግ | ትዕዛዙን ወደ RA ያስረክባል፤ ዘዴው **ወዲያውኑ** ይመለሳል — RA ትዕዛዙን ባልተመሳሰለ (async) መንገድ ያስኬደዋል |

### የበይነገጽ ፍቺ

```java
// ፓኬጅ፦ com.microjainslee.api
// የSBB-ወደ-RA ትዕዛዝ ወደብ። SBB ይህን ከ ResourceAdaptorContext ያገኛል
// እና ወደ RA የወጪ ትዕዛዞችን ለመላክ ይጠቀምበታል።

public interface RaCommandPort {

    // ወደ RA የወጪ ትዕዛዝ ላክ። RA ትዕዛዙን ባልተመሳሰለ መንገድ ያስኬደዋል —
    // ይህ ዘዴ ወረፋ ከገባ በኋላ ወዲያውኑ ይመለሳል።
    void sendCommand(OutboundCommand command);
}
```

### የአጠቃቀም ምሳሌ — gRPC ሜኑ ትዕዛዝ

```java
// SBB ውስጥ — @InjectRa በኩል የተወጋ RaCommandPort
@InjectRa(name = "grpcMenuRa")
private volatile RaCommandPort grpcCommandPort;

// የሜኑ ጥያቄ ወደ gRPC RA መላክ
public void requestMenu(String sessionId, String msisdn, String ussdText) {
    RaCommandPort port = this.grpcCommandPort;
    if (port == null) return;  // ገና አልተወጋም

    // OutboundCommand ፍጠርና ላክ — RA ባልተመሳሰለ መንገድ ያስኬደዋል
    port.sendCommand(new GrpcMenuCommand(sessionId, msisdn, ussdText));
}
```

---

## 4. RaBootstrapPort — የRA ማስጀመሪያ ወደብ

### ማብራሪያ

`RaBootstrapPort` መያዣው ለRA በ `activate()` ጊዜ የሚያስረክበው ወደብ ነው። እያንዳንዱ RA በSLEE ክስተት ሞዴል ውስጥ ለመሳተፍ የሚያስፈልጉትን **ሁለት መሰረታዊ አካላት** ያቀርባል።

| ሜተድ | ማብራሪያ |
|---|---|
| `createActivityHandle(String id)` | አንድ የፕሮቶኮል እንቅስቃሴን (ለምሳሌ የSIP ንግግር፣ የSS7 TCAP ውይይት) የሚለይ ግልጽ ያልሆነ መያዣ ይፈጥራል |
| `fireEvent(SleeEvent, ActivityHandle, Address)` | ክስተት ወደ SLEE ክስተት ራውተር ያስኮታል — በDisruptor ring buffer በኩል ወደ ፍላጎት ያላቸው SBBs ይደርሳል |

### የበይነገጽ ፍቺ

```java
// ፓኬጅ፦ com.microjainslee.api
// መያዣው በ RaEndpointPort.activate() ጊዜ ለRA የሚያስረክበው ማስጀመሪያ ወደብ

public interface RaBootstrapPort {

    // ከተሰጠው ግልጽ ያልሆነ መለያ የእንቅስቃሴ መያዣ ይፈጥራል
    ActivityHandle createActivityHandle(String id);

    // ክስተት ወደ SLEE ክስተት ራውተር አስኮት። ክስተቱ ወደ Disruptor
    // ring buffer ይታተምና ወደ ፍላጎት ያላቸው SBBs ባልተመሳሰለ መንገድ ይደርሳል።
    void fireEvent(SleeEvent event, ActivityHandle handle, Address address);
}
```

### የአጠቃቀም ምሳሌ — RA ክስተት ሲያስኮት

```java
// RA ውስጥ — bootstrap port ተቀብሎ ክስተት ማስኮት
public final class HttpIngressRaEndpoint implements RaEndpointPort {

    private RaBootstrapPort bootstrap;  // በ activate() ተቀብሏል

    // የHTTP ጥያቄ ሲመጣ...
    void onHttpRequest(String sessionId, String msisdn, String ussdText) {
        // 1. የእንቅስቃሴ መያዣ ፍጠር
        ActivityHandle handle = bootstrap.createActivityHandle(sessionId);

        // 2. ክስተት ፍጠር
        HttpUssdBeginEvent event = new HttpUssdBeginEvent(sessionId, msisdn, ussdText);

        // 3. ክስተቱን ወደ SLEE ክስተት ራውተር አስኮት
        Address address = new Address(Address.PLAN_E164, msisdn);
        bootstrap.fireEvent(event, handle, address);
    }
}
```


---

## 5. OutboundCommand — የወጪ ትዕዛዝ ምልክት በይነገጽ

### ማብራሪያ

`OutboundCommand` ከSBB ወደ RA የሚላኩ ትዕዛዞችን **ምልክት የሚያደርግ በይነገጽ** (marker interface) ነው። ምንም ሜተድ አይገልጽም — አላማው የፕሮቶኮል-ተኮር ትዕዛዞችን በአይነት-ደህንነቱ በተጠበቀ መንገድ ማስተላለፍ ነው።

```
SBB ያዘጋጃል፦ GrpcMenuCommand implements OutboundCommand
SBB ይልካል፦   raCommandPort.sendCommand(grpcMenuCommand)
RA ይቀበላል፦   if (command instanceof GrpcMenuCommand) { ... }
```

### የበይነገጽ ፍቺ

```java
// ፓኬጅ፦ com.microjainslee.api
// ከSBB ወደ RA በ RaCommandPort.sendCommand() በኩል ለሚላኩ
// የወጪ ትዕዛዞች ምልክት በይነገጽ።
//
// የፕሮቶኮል-ተኮር RAዎች ይህን በይነገጽ የሚተገብሩ ተጨባጭ
// ትዕዛዝ አይነቶችን (ለምሳሌ SendSmsCommand, StartCallCommand)
// ይገልጻሉ። RA በሩንታይም የትዕዛዙን አይነት በመመርመር
// ወደ ተገቢው የፕሮቶኮል ተቆጣጣሪ ያስተላልፋል።

public interface OutboundCommand {
    // ምንም ሜተድ የለም — ምልክት በይነገጽ ብቻ
}
```

### የአጠቃቀም ምሳሌ — ብጁ OutboundCommand

```java
// ለgRPC ሜኑ ጥያቄ የሚሆን ተጨባጭ OutboundCommand
public record GrpcMenuCommand(
    String sessionId,
    String msisdn,
    String ussdText
) implements OutboundCommand { }

// ለHTTP መልሶ መደወያ የሚሆን ተጨባጭ OutboundCommand
public record HttpCallbackCommand(
    String sessionId,
    String responseText,
    String callbackUrl
) implements OutboundCommand { }
```

---

## 6. @InjectRa — የRA ትዕዛዝ ወደብ መወጋት

### ማብራሪያ

`@InjectRa` መያዣው `RaCommandPort`ን ወደ SBB field **በራስ-ሰር እንዲወጋው** የሚያደርግ annotation ነው። ይህ ከMobicents SLEE የ `abstract getXxxRa()` ዘዴ ጋር ሲነጻጸር በጣም ቀላል ነው።

የመወጋቱ ሂደት የሚከናወነው `VirtualThreadSbbEntityPool` ውስጥ SBB entity ሲፈጠር ነው። መያዣው የSBB class ሁሉንም declared fields ይቃኛል፤ `@InjectRa` ያለባቸውን አግኝቶ ከተመዘገቡት RAዎች ውስጥ ተዛማጁን `RaCommandPort` ይወጋል።

### የAnnotation ፍቺ

```java
// ፓኬጅ፦ com.microjainslee.api.annotations
// የሪሶርስ አዳፕተር RaCommandPort ወደ SBB field መወጋት።
// መያዣው RAውን የሚለየው በ annotation የ name() እሴት ነው።

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface InjectRa {

    // አመክንዮአዊ የRA ስም፤ "" ከሆነ መያዣው ከfield አይነት ወይም
    // ከማሰማሪያ አውድ ራሱ ይገምታል
    String name() default "";
}
```

### የአጠቃቀም ምሳሌዎች

```java
// ምሳሌ 1 — በግልጽ የRA ስም መስጠት
@InjectRa(name = "ussd-gateway")
private RaCommandPort ussdRa;

// ምሳሌ 2 — name="" ከሆነ መያዣው ራሱ ይገምታል
@InjectRa  // name="" → ነባሪውን RaCommandPort ይጠቀማል
private RaCommandPort sipRa;

// ምሳሌ 3 — አንድ SBB ከብዙ RAዎች ጋር ሲገናኝ (PolyVoice pattern)
@InjectRa(name = "grpcMenuRa")
private volatile RaCommandPort grpcCommandPort;

@InjectRa(name = "httpIngressRa")
private volatile RaCommandPort httpCommandPort;
```

### የመወጋት ዘዴ — ውስጣዊ አሰራር

```java
// VirtualThreadSbbEntityPool ውስጥ፦ SBB instance ቃኝና @InjectRa fields ውጋ
private void injectRaPorts(Sbb sbb) {
    MicroSleeContainer c = this.container;
    if (c == null || sbb == null) return;

    for (Field field : sbb.getClass().getDeclaredFields()) {
        InjectRa injectRa = field.getAnnotation(InjectRa.class);
        if (injectRa == null) continue;  // @InjectRa የሌለውን field ዝለል

        String raName = injectRa.name();
        RaCommandPort port = (raName == null || raName.isEmpty())
                ? c.getDefaultRaCommandPort()     // ነባሪ
                : c.getRaCommandPort(raName);     // በስም ፈልግ

        if (port != null) {
            field.setAccessible(true);
            field.set(sbb, port);                // ወደቡን ውጋ
        }
    }
}
```



---

## 7. registerRa() እና mapEventToSbb()

### 7.1 registerRa() — RA ማስመዝገብ

`registerRa(RaEndpointPort, RaCommandPort)` አንድን RA በ3-ወደብ ውል ለማስመዝገብ ያገለግላል።

```java
MicroSleeContainer container = new MicroSleeContainer();
container.start();

// የRA endpoint እና command port ፍጠር
HttpIngressRaEndpoint httpEndpoint = new HttpIngressRaEndpoint();
RaCommandPort httpCommandPort = new SimpleRaCommandPort("httpIngressRa");

// RA ን አስመዝግብ — መያዣው በራስ-ሰር activate(bootstrap) ይጠራል
container.registerRa(httpEndpoint, httpCommandPort);
```

### 7.2 mapEventToSbb() — ክስተት ወደ SBB ካርታ

`mapEventToSbb(Class<? extends SleeEvent>, String sbbName)` የ `sbb-jar.xml` XML mappingን ይተካል።

```java
container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb");
container.mapEventToSbb(GrpcBackendResponseEvent.class, "GrpcClientSbb");
// አንድ ክስተት ወደ ብዙ SBBs ሊካርታ ይችላል
container.mapEventToSbb(GrpcBackendResponseEvent.class, "PolyVoiceSBB");
```

### 7.3 ሙሉ የማስመዝገብ ምሳሌ

```java
public void bootstrapContainer() {
    MicroSleeContainer container = new MicroSleeContainer();
    container.start();

    // --- RA ማስመዝገብ ---
    container.registerRa(new GrpcMenuRaEndpoint("localhost", 9090),
                         new SimpleRaCommandPort("grpcMenuRa"));
    container.registerRa(new HttpIngressRaEndpoint(),
                         new SimpleRaCommandPort("httpIngressRa"));

    // --- SBB ማስመዝገብ (ያለ XML) ---
    container.registerSbbType(HttpServerSbb.class, HttpServerSbb::new);
    container.registerSbbType(GrpcClientSbb.class, GrpcClientSbb::new);
    container.registerSbbType(Ss7UssdIngressSbb.class, Ss7UssdIngressSbb::new);

    // --- ክስተት-ወደ-SBB ካርታ ---
    container.mapEventToSbb(HttpUssdBeginEvent.class, "HttpServerSbb");
    container.mapEventToSbb(GrpcBackendResponseEvent.class, "GrpcClientSbb");
    container.mapEventToSbb(Ss7UssdBeginEvent.class, "Ss7UssdIngressSbb");
    container.mapEventToSbb(GrpcMenuResponseEvent.class, "GrpcClientSbb");
    container.mapEventToSbb(UssdCompleteEvent.class, "HttpServerSbb");
}
```

---

## 8. የተሟላ የPolyVoice SBB ኮድ ምሳሌ

ይህ ምሳሌ የ3-ወደብ ውል እንዴት በተግባር እንደሚሰራ የሚያሳይ **ሙሉ SBB** ነው።
ከ `example/example-embedded-j25` ፕሮጀክት የተወሰደ።

```java
/*
 * PolyVoice 3-ወደብ ውል SBB — ሶስቱንም የመገናኛ ወደቦች የሚያሳይ።
 * ምንጭ፦ example/example-embedded-j25/.../PolyVoiceSbbExample.java
 */

package com.example.ussddemo.sbbs;

// --- ማይክሮ-ጄይን-ስሊ API ማስመጣት ---
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.TimerFiredEvent;
import com.microjainslee.api.TimerPort;
import com.microjainslee.api.annotations.InjectRa;

// --- የመተግበሪያ-ተኮር ክስተቶች እና ትዕዛዞች ---
import com.example.ussddemo.commands.GrpcMenuCommand;
import com.example.ussddemo.commands.HttpCallbackCommand;
import com.example.ussddemo.events.GrpcBackendResponseEvent;
import com.example.ussddemo.events.HttpUssdBeginEvent;
import com.example.ussddemo.events.UssdResponseEvent;
import com.example.ussddemo.embedded.EmbeddedUssdMain;

/**
 * የPolyVoice 3-ወደብ ውል SBB — በማይክሮ-ጄይን-ስሊ መያዣ ውስጥ
 * አንድ SBB ሊጠቀምባቸው የሚችላቸውን ሶስቱንም የመገናኛ ወደቦች ያሳያል።
 *
 * ወደብ 1 — ክስተት ተቆጣጣሪ (ወደ ውስጥ)
 *   SBB የSLEE ክስተቶችን በ onEvent() በኩል ይቀበላል።
 *
 * ወደብ 2 — RA ትዕዛዝ ወደብ (ወደ ውጭ)
 *   SBB በ @InjectRa ወደተወጉት RaCommandPort fields በኩል
 *   የወጪ ትዕዛዞችን ወደ RA ይልካል።
 *
 * ወደብ 3 — ሰዓት ቆጣሪ መገልገያ (ውስጣዊ)
 *   SBB በ TimerPort በኩል ሰዓት ቆጣሪዎችን ያስቀምጣል/ያቋርጣል።
 */
public final class PolyVoiceSbbExample implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(PolyVoiceSbbExample.class);

    // ═══════════════════════════════════════════════════════════
    //  ወደብ 1 — የSBBው ማንነት በSLEE ውስጥ።
    // ═══════════════════════════════════════════════════════════

    /** SBB ራሱን የሚያመለክትበት የአካባቢ ማጣቀሻ (ለሰዓት ቆጣሪ ያስፈልጋል) */
    private volatile SbbLocalObject self;

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

    // ═══════════════════════════════════════════════════════════
    //  ወደብ 2 — የተወጉ RA ትዕዛዝ ወደቦች (GOAL 1-5 @InjectRa)።
    // ═══════════════════════════════════════════════════════════

    /** ወደ gRPC ሜኑ RA የወጪ ወደብ */
    @InjectRa(name = "grpcMenuRa")
    private volatile RaCommandPort grpcCommandPort;

    /** ወደ HTTP መግቢያ RA የወጪ ወደብ */
    @InjectRa(name = "httpIngressRa")
    private volatile RaCommandPort httpCommandPort;

    // ═══════════════════════════════════════════════════════════
    //  ወደብ 3 — የሰዓት ቆጣሪ ሁኔታ።
    // ═══════════════════════════════════════════════════════════

    /** ነባሪ የክፍለ-ጊዜ ጊዜ ማብቂያ፦ 25 ሰከንድ */
    private static final long DEFAULT_TIMEOUT_MS = 25_000L;
    private volatile long activeTimerId = -1L;

    // ═══════════════════════════════════════════════════════════
    //  የህይወት ዑደት (ሶስቱም ወደቦች አንድ የህይወት ዑደት ይጋራሉ)።
    // ═══════════════════════════════════════════════════════════

    @Override
    public void sbbCreate() {
        // SBB ሲፈጠር ይጠራል — ገና ወደቦች አልተወጉም
        LOG.debug("PolyVoiceSbbExample ተፈጠረ");
    }

    @Override
    public void sbbActivate() {
        // SBB ሲነቃ — ወደብ 2 fields ተወግተዋል!
        LOG.debug("PolyVoiceSbbExample ነቃ — ወደብ 2 ተወግቷል፦ grpc={}, http={}",
                grpcCommandPort != null, httpCommandPort != null);
    }

    @Override
    public void sbbPassivate() {
        cancelActiveTimer();   // ሰዓት ቆጣሪዎችን አጽዳ
    }

    @Override
    public void sbbRemove() {
        cancelActiveTimer();   // ሁሉንም ሃብቶች አጽዳ
    }

    // ═══════════════════════════════════════════════════════════
    //  ወደብ 1 — ክስተት ተቆጣጣሪ (በEventRouter በኩል የሚገቡ ክስተቶች)።
    // ═══════════════════════════════════════════════════════════

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        // በክስተት አይነት መሰረት ወደ ተገቢው ተቆጣጣሪ ላክ
        if (event instanceof HttpUssdBeginEvent) {
            onHttpBegin((HttpUssdBeginEvent) event, aci);
        } else if (event instanceof GrpcBackendResponseEvent) {
            onGrpcResponse((GrpcBackendResponseEvent) event, aci);
        } else if (event instanceof TimerFiredEvent) {
            onTimer((TimerFiredEvent) event, aci);
        }
    }

    // --- ወደብ 1 ተቆጣጣሪዎች ---

    /** HTTP መጀመሪያ ክስተት — የUSSD ክፍለ-ጊዜ ጅምር */
    private void onHttpBegin(HttpUssdBeginEvent event, ActivityContextInterface aci) {
        LOG.info("[PolyVoice] HTTP መጀመሪያ ክፍለጊዜ={} msisdn={} ጽሁፍ={}",
                event.getSessionId(), event.getMsisdn(), event.getUssdString());

        // ወደብ 3 — የክፍለ-ጊዜ ሰዓት ቆጣሪ አስጀምር
        TimerPort timer = EmbeddedUssdMain.container().getTimerPort();
        activeTimerId = timer.setTimer(DEFAULT_TIMEOUT_MS, self);

        // ወደብ 2 — የሜኑ ጥያቄ በተወጋው gRPC ወደብ ላክ
        sendMenuRequest("USSD:" + event.getUssdString());
    }

    /** gRPC ምላሽ ሲመጣ */
    private void onGrpcResponse(GrpcBackendResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[PolyVoice] gRPC ምላሽ ክፍለጊዜ={} ጽሁፍ={}",
                event.getSessionId(), event.getMenuText());

        // ወደብ 3 — ምላሽ ስለደረሰ ሰዓት ቆጣሪ አቋርጥ
        cancelActiveTimer();

        // የመጨረሻውን የUSSD ምላሽ ወደ ቀጣይ አስተላልፍ
        EmbeddedUssdMain.container().routeEvent(
                new UssdResponseEvent(event.getSessionId(), event.getMenuText()), aci);
    }

    /** ሰዓት ቆጣሪ ሲነሳ — ክፍለ-ጊዜ ጊዜው አልፎበታል */
    private void onTimer(TimerFiredEvent event, ActivityContextInterface aci) {
        if (event.getSbbLocalObject() != self) {
            return; // የኛ ሰዓት ቆጣሪ አይደለም
        }
        LOG.warn("[PolyVoice] ሰዓት ቆጣሪ ነስቷል timerId={} — ጊዜው አልፎበታል",
                event.getTimerId());
        activeTimerId = -1L;
    }

    // ═══════════════════════════════════════════════════════════
    //  ወደብ 2 — የወጪ RA ትዕዛዞች በተወጉ ወደቦች በኩል።
    // ═══════════════════════════════════════════════════════════

    /** የሜኑ ጥያቄ ወደ gRPC RA ላክ */
    public void sendMenuRequest(String menuRequest) {
        RaCommandPort port = this.grpcCommandPort;
        if (port == null) {
            LOG.warn("[PolyVoice] grpcCommandPort አልተወጋም — ትዕዛዝ ተትቷል");
            return;
        }
        // OutboundCommand ላክ — RA ባልተመሳሰለ መንገድ ያስኬደዋል
        port.sendCommand(new GrpcMenuCommand(menuRequest));
        LOG.debug("[PolyVoice] gRPC ሜኑ ትዕዛዝ ተልኳል፦ {}", menuRequest);
    }

    /** የHTTP መልሶ መደወያ ወደ HTTP RA ላክ */
    public void publishCallback(String sessionId, String responseText, String callbackUrl) {
        RaCommandPort port = this.httpCommandPort;
        if (port == null) {
            LOG.warn("[PolyVoice] httpCommandPort አልተወጋም — መልሶ መደወያ ተትቷል");
            return;
        }
        port.sendCommand(new HttpCallbackCommand(sessionId, responseText, callbackUrl));
        LOG.debug("[PolyVoice] HTTP መልሶ መደወያ ወረፋ ገብቷል ክፍለጊዜ={}", sessionId);
    }

    // ═══════════════════════════════════════════════════════════
    //  ወደብ 3 — የሰዓት ቆጣሪ ረዳት ዘዴዎች።
    // ═══════════════════════════════════════════════════════════

    /** ሰዓት ቆጣሪ አስቀምጥ */
    public long scheduleTimeout(long timeoutMs) {
        TimerPort timer = EmbeddedUssdMain.container().getTimerPort();
        long id = timer.setTimer(timeoutMs, self);
        LOG.debug("[PolyVoice] ሰዓት ቆጣሪ ተቀምጧል id={} timeoutMs={}", id, timeoutMs);
        return id;
    }

    /** ሰዓት ቆጣሪ አቋርጥ */
    public void cancelTimer(long timerId) {
        EmbeddedUssdMain.container().getTimerPort().cancelTimer(timerId);
        LOG.debug("[PolyVoice] ሰዓት ቆጣሪ ተቋርጧል id={}", timerId);
    }

    /** አሁን የሚሰራውን ሰዓት ቆጣሪ አቋርጥ */
    private void cancelActiveTimer() {
        long id = this.activeTimerId;
        if (id >= 0L) {
            cancelTimer(id);
            activeTimerId = -1L;
        }
    }
}
```

### የPolyVoice SBB ስነ-ህንፃ ዲያግራም

```
              ┌──────────────────────────────────────┐
              │         PolyVoiceSbbExample          │
              │                                      │
  ወደብ 1 ────▶│  onEvent(SleeEvent, ACI)             │──▶ UssdResponseEvent
  (ወደ ውስጥ)   │    ├─ HttpUssdBeginEvent              │
              │    ├─ GrpcBackendResponseEvent        │
              │    └─ TimerFiredEvent                 │
              │                                      │
  ወደብ 2 ◀────│  @InjectRa grpcCommandPort            │──▶ GrpcMenuCommand
  (ወደ ውጭ)    │  @InjectRa httpCommandPort            │──▶ HttpCallbackCommand
              │                                      │
  ወደብ 3 ◀───▶│  TimerPort.setTimer()                 │
  (ውስጣዊ)     │  TimerPort.cancelTimer()              │
              └──────────────────────────────────────┘
```

### የክስተት ፍሰት ቅደም ተከተል

```
1. HTTP RA ጥያቄ ይቀበላል
2. RaBootstrapPort.fireEvent(HttpUssdBeginEvent, handle, address)
3. EventRouter → PolyVoiceSbbExample.onEvent()
4. SBB ሰዓት ቆጣሪ ያስጀምራል (ወደብ 3)
5. SBB ሜኑ ጥያቄ ይልካል (ወደብ 2 → gRPC RA)
6. gRPC RA ምላሽ ይቀበላል → fireEvent(GrpcBackendResponseEvent)
7. EventRouter → PolyVoiceSbbExample.onEvent()
8. SBB ሰዓት ቆጣሪ ያቋርጣል (ወደብ 3)
9. UssdResponseEvent ወደ ቀጣይ ያስተላልፋል
```

---

## ማጠቃለያ

የ **3-Port Contract API** ከ Mobicents SLEE ጋር ሲነጻጸር የሚከተሉትን ቀለል ያደርጋል፦

| Mobicents SLEE | micro-jainslee 3-Port |
|---|---|
| `javax.slee.resource.ResourceAdaptor` (20+ ሜተዶች) | `RaEndpointPort` (3 ሜተዶች) |
| `javax.slee.resource.SleeEndpoint` (8 ሜተዶች) | `RaBootstrapPort` (2 ሜተዶች) |
| `abstract getXxxRa()` accessor | `@InjectRa RaCommandPort` field |
| JNDI lookup / `@ResourceAdaptor` annotation | `container.registerRa(endpoint, command)` |
| XML `sbb-jar.xml` event mapping | `container.mapEventToSbb(eventType, sbbName)` |
| `OutboundCommand` — የለም | `OutboundCommand` marker interface |

**PolyVoice pattern** እያንዳንዱ ሪሶርስ አዳፕተር ከመያዣው ጋር በሶስት ግልጽና ለሙከራ ምቹ በሆኑ ወደቦች እንዲገናኝ ያስችላል። እያንዳንዱን ወደብ በተናጠል መምሰል (mock) ስለሚቻል የክፍል ፈተና (unit test) በጣም ቀላል ይሆናል።

---

> **R&D ብቻ** — ይህ ሰነድ ለምርምርና ልማት ዓላማ ብቻ ነው። ለUSSD 7.3 ምርት Mobicents SLEE master-era JAR + WildFly 10 ይጠቀሙ።
