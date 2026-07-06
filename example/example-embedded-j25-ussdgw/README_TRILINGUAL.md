# PolyVoice 3-Port Contract Example

─── English ─────────────────────────────────────────────────────────

## PolyVoice 3-Port Contract Example

The **PolyVoice SBB** (`PolyVoiceSbbExample.java`) demonstrates the complete
3-port contract every micro-jainslee SBB can use to interact with the SLEE
container and its Resource Adaptors.

### What This Example Does

A JAIN SLEE Service Building Block (SBB) has three communication ports:

| Port | Direction | Mechanism | Used For |
|------|-----------|-----------|----------|
| **Port 1** — Event Handler | Inbound | `onEvent(SleeEvent, ActivityContextInterface)` | Receiving protocol events (USSD begin, gRPC response, timer fire) from the EventRouter |
| **Port 2** — RA Command Port | Outbound | `@InjectRa` → `RaCommandPort.sendCommand(OutboundCommand)` | Sending commands to Resource Adaptors (gRPC menu lookup, HTTP callback delivery) |
| **Port 3** — Timer Facility | Internal | `TimerPort.setTimer()` / `cancelTimer()` | Session timeout management; timer fires arrive as `TimerFiredEvent` on Port 1 |

The example shows an SBB that:

1. **Receives** an `HttpUssdBeginEvent` (Port 1)
2. **Arms** a session timeout timer (Port 3)
3. **Sends** a menu request to the gRPC RA via the injected command port (Port 2)
4. **Receives** the `GrpcBackendResponseEvent` (Port 1)
5. **Cancels** the timer (Port 3)
6. **Routes** the final USSD response onward

### Files

| File | Purpose |
|------|---------|
| `sbbs/PolyVoiceSbbExample.java` | Full 3-port demonstration SBB |
| `sbbs/GrpcClientSbb.java` | Child SBB with `@InjectRa(name = "grpcMenuRa")` |
| `sbbs/HttpServerSbb.java` | Entry SBB with `@InjectRa(name = "httpIngressRa")` |
| `commands/GrpcMenuCommand.java` | Local `OutboundCommand` for gRPC menu requests |
| `commands/HttpCallbackCommand.java` | Local `OutboundCommand` for HTTP callbacks |
| `embedded/EmbeddedUssdSmokeTest.java` | Tests for `registerRa()` and `mapEventToSbb()` contract |

─── Vietnamese — Tiếng Việt ─────────────────────────────────────────

## Ví dụ PolyVoice 3-Port Contract

**PolyVoice SBB** (`PolyVoiceSbbExample.java`) minh họa đầy đủ hợp đồng
3 cổng (3-port contract) mà mọi SBB trong micro-jainslee có thể sử dụng
để giao tiếp với container SLEE và các Resource Adaptor.

### Ví dụ này làm gì

Một JAIN SLEE Service Building Block (SBB) có ba cổng giao tiếp:

| Cổng | Hướng | Cơ chế | Mục đích |
|------|-------|--------|----------|
| **Cổng 1** — Event Handler | Vào | `onEvent(SleeEvent, ActivityContextInterface)` | Nhận sự kiện giao thức (USSD begin, gRPC response, timer fire) từ EventRouter |
| **Cổng 2** — RA Command Port | Ra | `@InjectRa` → `RaCommandPort.sendCommand(OutboundCommand)` | Gửi lệnh đến Resource Adaptor (tra cứu menu gRPC, gửi HTTP callback) |
| **Cổng 3** — Timer Facility | Nội bộ | `TimerPort.setTimer()` / `cancelTimer()` | Quản lý timeout phiên; timer fire đến dưới dạng `TimerFiredEvent` trên Cổng 1 |

Ví dụ minh họa một SBB:

1. **Nhận** `HttpUssdBeginEvent` (Cổng 1)
2. **Kích hoạt** timer timeout cho phiên (Cổng 3)
3. **Gửi** yêu cầu menu đến gRPC RA qua cổng command đã inject (Cổng 2)
4. **Nhận** `GrpcBackendResponseEvent` (Cổng 1)
5. **Hủy** timer (Cổng 3)
6. **Chuyển tiếp** phản hồi USSD cuối cùng

### Các file

| File | Mục đích |
|------|----------|
| `sbbs/PolyVoiceSbbExample.java` | SBB minh họa đầy đủ 3 cổng |
| `sbbs/GrpcClientSbb.java` | Child SBB với `@InjectRa(name = "grpcMenuRa")` |
| `sbbs/HttpServerSbb.java` | Entry SBB với `@InjectRa(name = "httpIngressRa")` |
| `commands/GrpcMenuCommand.java` | `OutboundCommand` cục bộ cho yêu cầu menu gRPC |


─── Amharic — አማርኛ ─────────────────────────────────────────────────

## የPolyVoice 3-Port Contract ምሳሌ

**PolyVoice SBB** (`PolyVoiceSbbExample.java`) እያንዳንዱ የማይክሮ-ጄይን-ስሊ SBB
ከSLEE መያዣ እና ከሪሶርስ አዳፕተሮቹ ጋር ለመገናኘት ሊጠቀምባቸው የሚችላቸውን ሙሉ
የ3-ወደብ ውል (3-port contract) ያሳያል።

### ይህ ምሳሌ ምን ያደርጋል

አንድ JAIN SLEE ሰርቪስ ቢልዲንግ ብሎክ (SBB) ሶስት የመገናኛ ወደቦች አሉት፦

| ወደብ | አቅጣጫ | ዘዴ | ጥቅም |
|------|--------|------|-------|
| **ወደብ 1** — ክስተት ተቆጣጣሪ | ወደ ውስጥ | `onEvent(SleeEvent, ActivityContextInterface)` | የፕሮቶኮል ክስተቶችን ከEventRouter መቀበል |
| **ወደብ 2** — RA ትዕዛዝ ወደብ | ወደ ውጭ | `@InjectRa` → `RaCommandPort.sendCommand(OutboundCommand)` | ትዕዛዞችን ወደ ሪሶርስ አዳፕተሮች መላክ |
| **ወደብ 3** — ሰዓት ቆጣሪ መገልገያ | ውስጣዊ | `TimerPort.setTimer()` / `cancelTimer()` | የክፍለ-ጊዜ ጊዜ ማብቂያ አስተዳደር |

ምሳሌው አንድ SBB እንደሚከተለው ያሳያል፦

1. `HttpUssdBeginEvent` መቀበል (ወደብ 1)
2. የክፍለ-ጊዜ ጊዜ ማብቂያ ሰዓት ቆጣሪ ማስጀመር (ወደብ 3)
3. የሜኑ ጥያቄ ወደ gRPC RA በተወጋው ትዕዛዝ ወደብ መላክ (ወደብ 2)
4. `GrpcBackendResponseEvent` መቀበል (ወደብ 1)
5. ሰዓት ቆጣሪውን ማቋረጥ (ወደብ 3)
6. የመጨረሻውን የUSSD ምላሽ ማስተላለፍ

### ፋይሎች

| ፋይል | ዓላማ |
|------|--------|
| `sbbs/PolyVoiceSbbExample.java` | ሙሉ የ3-ወደብ ማሳያ SBB |
| `sbbs/GrpcClientSbb.java` | የልጅ SBB በ`@InjectRa(name = "grpcMenuRa")` |
| `sbbs/HttpServerSbb.java` | የመግቢያ SBB በ`@InjectRa(name = "httpIngressRa")` |
| `commands/GrpcMenuCommand.java` | አካባቢያዊ `OutboundCommand` ለgRPC ሜኑ ጥያቄዎች |
| `commands/HttpCallbackCommand.java` | አካባቢያዊ `OutboundCommand` ለHTTP መልሶ መደወያ |
| `embedded/EmbeddedUssdSmokeTest.java` | የ`registerRa()` እና `mapEventToSbb()` ውል ፈተናዎች |

─── Architecture ─────────────────────────────────────────────────────

```
              ┌──────────────────────────────────┐
              │       PolyVoiceSbbExample        │
              │                                  │
  Port 1 ────▶│  onEvent(SleeEvent, ACI)         │──▶ UssdResponseEvent
  (inbound)   │    ├─ HttpUssdBeginEvent          │
              │    ├─ GrpcBackendResponseEvent    │
              │    └─ TimerFiredEvent             │
              │                                  │
  Port 2 ◀────│  @InjectRa grpcCommandPort        │──▶ GrpcMenuCommand
  (outbound)  │  @InjectRa httpCommandPort        │──▶ HttpCallbackCommand
              │                                  │
  Port 3 ◀───▶│  TimerPort.setTimer()             │
  (internal)  │  TimerPort.cancelTimer()          │
              └──────────────────────────────────┘
```

| `commands/HttpCallbackCommand.java` | `OutboundCommand` cục bộ cho HTTP callback |
| `embedded/EmbeddedUssdSmokeTest.java` | Kiểm thử cho hợp đồng `registerRa()` và `mapEventToSbb()` |