# 🏗️ micro-jainslee Architecture Deep Dive

> **Tài liệu kiến trúc chuyên sâu — dành cho senior architect & lead developer**
>
> Last updated: 2025-07-04 | Maintainer: nhanth87
> Nguồn: codebase analysis (container/router/, jainslee-core/, jainslee-scheduler/)

---

## Mục lục

1. [EventRouter Architecture](#1-eventrouter-architecture)
   - [Production: LMAX Disruptor Pipeline](#11-production-lmax-disruptor-pipeline)
   - [R&D: In-Memory EventRouter (micro-jainslee)](#12-rd-in-memory-eventrouter-micro-jainslee)
2. [VirtualThreadSbbEntityPool](#2-virtualthreadsbbentitypool)
3. [SleeTimerSchedulerBridge](#3-sleetimerschedulerbridge)
4. [ActivityContext Lifecycle](#4-activitycontext-lifecycle)
5. [Session Recovery / Snapshot](#5-session-recovery--snapshot)
6. [Production vs R&D Comparison](#6-production-vs-rd-comparison)

---

## 1. EventRouter Architecture

EventRouter là **trái tim của JAIN SLEE runtime** — nơi tiếp nhận event từ Resource Adaptor, route đến đúng SBB entity, và thực thi event processing theo đúng spec JAIN SLEE 1.1.

### 1.1 Production: LMAX Disruptor Pipeline

Production (RestComm JAIN-SLEE v8) sử dụng **LMAX Disruptor** — một ring-buffer-based messaging framework cho low-latency, high-throughput event processing.

#### Kiến trúc tổng thể

```
┌──────────────────────────────────────────────────────────────────────┐
│                         SLEE CONTAINER                              │
│                                                                     │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐                    │
│  │ RA (SCTP)│     │ RA (HTTP)│     │ RA (MAP) │  ... N RAs         │
│  └────┬─────┘     └────┬─────┘     └────┬─────┘                    │
│       │               │               │                            │
│       │  fireEvent()  │  fireEvent()  │  fireEvent()               │
│       ▼               ▼               ▼                            │
│  ┌─────────────────────────────────────────────────────────┐       │
│  │                 EventRouterImpl                         │       │
│  │                                                         │       │
│  │  ┌───────────────────────────────────────────────────┐  │       │
│  │  │  ActivityHashingEventRouterExecutorMapper         │  │       │
│  │  │  AC.handle.hashCode() % N → executor[N]           │  │       │
│  │  └─────────────────────┬─────────────────────────────┘  │       │
│  │     ┌──────────────────┼──────────────────┐             │       │
│  │     ▼                  ▼                  ▼             │       │
│  │  ┌────────┐       ┌────────┐       ┌────────┐          │       │
│  │  │ Exec 0 │       │ Exec 1 │  ...  │ Exec N │          │       │
│  │  │RingBuf │       │RingBuf │       │RingBuf │          │       │
│  │  │ 262K   │       │ 262K   │       │ 262K   │          │       │
│  │  │Worker  │       │Worker  │       │Worker  │          │       │
│  │  └───┬────┘       └───┬────┘       └───┬────┘          │       │
│  │      │               │               │                 │       │
│  │      ▼               ▼               ▼                 │       │
│  │  ┌─────────────────────────────────────────────────┐   │       │
│  │  │         EventRoutingTask (route per event)       │   │       │
│  │  │  1. Find AC → 2. IES → 3. SBB Entity → 4. Fire  │   │       │
│  │  └─────────────────────────────────────────────────┘   │       │
│  └─────────────────────────────────────────────────────────┘       │
└──────────────────────────────────────────────────────────────────────┘
```

#### Event Flow: RA.fireEvent() → SBB execution

```
RA Thread                      Disruptor Worker Thread          SBB Thread
───────                        ──────────────────────          ──────────

1. RA gọi sleeEndpoint
   .fireEvent(ac, type, event)
   │
2. EventRouterImpl.fireEvent()
   ├─ Tạo EventContext
   ├─ executorMapper.getExecutor(ac)
   │   → ActivityHashing: hash & N-1
   │
3. ringBuffer.publishEvent()
   ├─ Claim slot (sequence number)
   ├─ Copy event data vào slot
   └─ Return NGAY LẬP TỨC
      (non-blocking với SINGLE producer)

                               4. WaitStrategy.signalAllWhenBlocking()
                                  │
                               5. EventHandler.onEvent(slot, seq, endOfBatch)
                                  │
                                  ├─ event.process(endOfBatch)
                                  │   ├─ EventRoutingTaskPool.route()
                                  │   │   ├─ Find AC by handle
                                  │   │   ├─ IES: chọn SBB entity
                                  │   │   └─ Gọi SBB.onFireEvent()
                                  │   │
                                  │   └─ stats.eventRouted(type, latency)
                                  │
                                  └─ event.reset() // clear for reuse
                                                           │
                                                           ▼
                                                     SBB Virtual Thread
                                                     (parked → unpark)
```

#### Cấu hình Ring Buffer

```java
// EventRouterImpl.java - resolveConfiguration()
eventRouterThreads = Math.max(4, Runtime.getRuntime().availableProcessors());
ringSize = Integer.getInteger("jainslee.eventrouter.ringsize", 262144);
waitStrategy = "blocking"; // default: BlockingWaitStrategy
multiProducer = false;     // default: SINGLE producer type

// DisruptorEventRouterExecutorImpl constructor
ringSize = Integer.highestOneBit(ringSize - 1) << 1; // power-of-2 alignment
this.disruptor = new Disruptor<>(
    EVENT_FACTORY, ringSize, threadFactory,
    multiProducer ? ProducerType.MULTI : ProducerType.SINGLE,
    waitStrategyImpl);
```

**Tại sao 262144 slots?**
- Mỗi slot ≈ 64 bytes (EventWrapper: eventContext + miscTask + timestamp + latch)
- 262144 × 64 = ~16MB per executor
- Với 14 executors: ~224MB ring buffer overhead
- Đảm bảo đủ headroom cho burst 100K events/s mà không bị blocking

#### Wait Strategy Options

| Strategy | CPU | Latency | Use case |
|----------|-----|---------|----------|
| `blocking` | Thấp | ~1-5μs | Default, tốt cho hầu hết workload |
| `yielding` | Trung bình | ~0.1-1μs | Low-latency, chấp nhận spin CPU |
| `busyspin` | Rất cao | ~0.05μs | Ultra-low-latency, dedicated cores |

```bash
# JVM flag để chọn wait strategy
-Djainslee.eventrouter.waitstrategy=yielding
```

#### Activity Pinning (ActivityHash)

```java
// ActivityHashingEventRouterExecutorMapper.java
public EventRouterExecutor getExecutor(ActivityContextHandle handle) {
    return executors[(handle.hashCode() & Integer.MAX_VALUE) % executors.length];
}
```

**Guarantee:** Mọi event cho cùng một ActivityContext LUÔN LUÔN được route đến cùng một executor → **single-threaded per AC** → đúng JAIN SLEE spec (section 6.4: "activities are single-threaded").

#### Producer Type

- `SINGLE` (default): RA thread là single publisher vào ring buffer → lock-free, fast-path
- `MULTI`: Dùng khi nhiều RA thread cùng publish → thêm CAS overhead

#### Performance Numbers (Production)

| Metric | Value |
|--------|-------|
| **Ring buffer size** | 262,144 slots per executor |
| **Executors** | N = availableProcessors (tối thiểu 4) |
| **Worker threads** | N (1 per executor) |
| **Throughput** | 100K+ events/s (benchmarked) |
| **Per-event latency** | < 5μs p99 (blocking wait strategy) |
| **Memory (ring buffers only)** | N × 262144 × 64 ≈ N × 16MB |

### 1.2 R&D: In-Memory EventRouter (micro-jainslee)

micro-jainslee dùng một **EventRouter đơn giản hóa**, không phụ thuộc vào LMAX Disruptor.

```
┌──────────────────────────────────────────────────────┐
│              micro-jainslee EventRouter              │
│                                                      │
│  RA.fireEvent()                                      │
│       │                                              │
│       ▼                                              │
│  ┌─────────────────────────────────────────┐         │
│  │  com.microjainslee.core.EventRouter     │         │
│  │                                         │         │
│  │  1. Tạo EventWrapper                    │         │
│  │  2. Tra cứu InMemoryActivityContext     │         │
│  │  3. IES selection (simple)              │         │
│  │  4. Gọi EntitySlot.deliver(event)       │         │
│  └───────────────────┬─────────────────────┘         │
│                      │                               │
│                      ▼                               │
│  ┌─────────────────────────────────────────┐         │
│  │         EntitySlot (per SBB entity)     │         │
│  │                                         │         │
│  │  ┌──────────────────────────────────┐   │         │
│  │  │  EventLoop (virtual thread)      │   │         │
│  │  │                                  │   │         │
│  │  │  while (alive) {                 │   │         │
│  │  │    event = queue.take();         │   │         │
│  │  │    sbb.onFireEvent(event);       │   │         │
│  │  │  }                               │   │         │
│  │  └──────────────────────────────────┘   │         │
│  └─────────────────────────────────────────┘         │
│                                                      │
│  ActivityContextPool: Map<Handle, AC>                 │
│  ┌──────────────────────────────────────┐            │
│  │  ConcurrentHashMap<Handle, AC>       │            │
│  │  + last access time tracking         │            │
│  └──────────────────────────────────────┘            │
└──────────────────────────────────────────────────────┘
```

**Điểm khác biệt chính:**

| Feature | Production (Disruptor) | R&D (In-Memory) |
|---------|----------------------|-----------------|
| **Event queue** | RingBuffer 262K (pre-allocated) | LinkedBlockingQueue per entity |
| **Wait strategy** | Blocking/Yielding/BusySpin | intrinsic `take()` blocking |
| **Multi-producer** | Configurable | N/A (single-threaded per entity) |
| **Batching** | endOfBatch signal | Không có |
| **Memory** | Pre-allocated 16MB/executor | Dynamic allocation |
| **Latency** | ~1-5μs | ~10-50μs |
| **Throughput** | 100K+ events/s | ~10K-50K events/s |

---

## 2. VirtualThreadSbbEntityPool

micro-jainslee tận dụng **Java 25 Virtual Threads** (Project Loom) để mỗi SBB entity chạy trên một virtual thread riêng, parked khi không có event.

### Kiến trúc: 1 SBB Entity = 1 Parked Virtual Thread

```
┌──────────────────────────────────────────────────────────────────┐
│                   VirtualThreadSbbEntityPool                      │
│                                                                  │
│  SBB Entity Types                                                │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │  Service A (USSD)                                           │ │
│  │  ┌───────┐ ┌───────┐ ┌───────┐       ┌───────┐            │ │
│  │  │Entity │ │Entity │ │Entity │  ...  │Entity │  100K total │ │
│  │  │   0   │ │   1   │ │   2   │       │ 99,999│            │ │
│  │  │ VT#1  │ │ VT#2  │ │ VT#3  │       │ VT#N  │            │ │
│  │  └───┬───┘ └───┬───┘ └───┬───┘       └───┬───┘            │ │
│  │      │         │         │               │                 │ │
│  │      │  park() │  park() │  park()       │  park()         │ │
│  │      ▼         ▼         ▼               ▼                 │ │
│  │    ┌──────────────────────────────────────────────┐        │ │
│  │    │        SINGLE ForkJoinPool Scheduler          │        │ │
│  │    │        (14 OS threads backing 100K VTs)       │        │ │
│  │    └──────────────────────────────────────────────┘        │ │
│  └─────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────────┘
```

### Lifecycle: park → unpark → execute → repark

```
   ┌───────────────────────────────────────────────────────────┐
   │              Virtual Thread Lifecycle                      │
   │                                                           │
   │  1. CREATE ENTITY                                         │
   │     │                                                     │
   │     ├─ Thread.ofVirtual().name("sbb-entity-<id>")         │
   │     │    .start(() -> {                                   │
   │     │        while (alive) {                              │
   │     │            event = blockingQueue.take();  ◄── PARK  │
   │     │            sbb.onFireEvent(event);        ◄── EXEC  │
   │     │        }                                            │
   │     │    });                                              │
   │     │                                                     │
   │  2. WAITING (PARKED)                                      │
   │     │  Virtual thread BỊ PARK trên blockingQueue.take()   │
   │     │  → Không chiếm OS thread                            │
   │     │  → Memory footprint: ~200-300 bytes (stack snapshot)│
   │     │                                                     │
   │  3. EVENT ARRIVES                                         │
   │     │  EventRouter.deliver(entity, event)                 │
   │     │  → blockingQueue.offer(event)                       │
   │     │  → Virtual thread UNPARKED                          │
   │     │  → Scheduler gán 1 OS thread (trong 14)             │
   │     │                                                     │
   │  4. EXECUTING                                             │
   │     │  SBB.onFireEvent() chạy trên OS thread              │
   │     │  → Access ActivityContext, CMP fields, Timer,...    │
   │     │  → Có thể gọi fireEvent() recursive                 │
   │     │                                                     │
   │  5. REPARK                                                │
   │     │  onFireEvent() return                               │
   │     │  → Loop back to blockingQueue.take()                │
   │     │  → Virtual thread PARKED lại                        │
   │     │  → OS thread được giải phóng cho VT khác            │
   │     │                                                     │
   │  6. REMOVE                                                │
   │     │  Entity removed → alive = false                     │
   │     │  → Virtual thread exits gracefully                  │
   │     └─ VT resources reclaimed by GC                       │
   └───────────────────────────────────────────────────────────┘
```

### Tại sao 100K SBB entities chỉ dùng ~14 OS threads?

```
┌────────────────────────────────────────────────────────────────┐
│                    Thread Model Comparison                      │
│                                                                │
│  Platform Threads (old model):                                 │
│  ┌──────────────────────────────────────────────────────┐     │
│  │ 100K entities × 1 platform thread = 100K OS threads   │     │
│  │ → 100K × 1MB stack = 100GB RAM                        │     │
│  │ → Context switch overhead: không thể scale            │     │
│  └──────────────────────────────────────────────────────┘     │
│                                                                │
│  Virtual Threads (Java 25):                                    │
│  ┌──────────────────────────────────────────────────────┐     │
│  │ 100K virtual threads                                    │     │
│  │ → Mounted on ForkJoinPool (default: parallelism=cores) │     │
│  │ → 14 CPU cores → 14 OS threads backing 100K VTs       │     │
│  │ → Each VT parked = ~300 bytes heap + stack obj in heap │     │
│  │ → 100K parked VTs ≈ 30MB heap (not 100GB!)            │     │
│  │ → Only ACTIVE VTs (processing events) use OS thread    │     │
│  │ → Concurrency = number of ACTIVE entities at any moment│     │
│  └──────────────────────────────────────────────────────┘     │
└────────────────────────────────────────────────────────────────┘
```

**Memory model:**
- **Parked VT:** Chỉ lưu `Continuation` object (~200-300 bytes) trên heap. Không có stack trong RAM OS.
- **Active VT:** Mounted vào 1 platform thread, dùng stack trên heap (~vài KB đến vài MB tùy call depth).
- **Re-park:** Khi `take()` block, stack frame được "freeze" vào heap object. Platform thread quay lại ForkJoinPool work queue.

### EntitySlot & EventLoop

```java
// EntitySlot.java — conceptual architecture
class EntitySlot {
    final SbbLocalObject sbb;
    final BlockingQueue<EventWrapper> eventQueue;
    volatile boolean alive = true;
    final Thread virtualThread;

    EntitySlot(SbbLocalObject sbb) {
        this.sbb = sbb;
        this.eventQueue = new LinkedBlockingQueue<>();
        this.virtualThread = Thread.ofVirtual()
            .name("sbb-" + sbb.getId())
            .start(this::eventLoop);
    }

    void eventLoop() {
        while (alive) {
            try {
                EventWrapper event = eventQueue.take();  // PARK here
                sbb.onFireEvent(event);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    void deliver(EventWrapper event) {
        eventQueue.offer(event);  // UNPARK virtual thread
    }

    void remove() {
        alive = false;
        virtualThread.interrupt();
    }
}
```

---

## 3. SleeTimerSchedulerBridge

### Bridge Pattern: jSS7 HashedWheelTimer → EventRouter

**Critical constraint:** SBB **KHÔNG BAO GIỜ** được thực thi trực tiếp trên wheel thread. Timer callback phải được route qua EventRouter.

```
┌───────────────────────────────────────────────────────────────────────┐
│                     SleeTimerSchedulerBridge                           │
│                                                                       │
│  ┌──────────────────────────┐         ┌──────────────────────────┐   │
│  │  jSS7 HashedWheelTimer   │         │   JAIN SLEE EventRouter  │   │
│  │                          │         │                          │   │
│  │  ┌────────────────────┐  │  fire   │  ┌───────────────────┐  │   │
│  │  │ Wheel (512 buckets)│  │ Event() │  │  RingBuffer       │  │   │
│  │  │                    │──┼────────►│  │  (production)     │  │   │
│  │  │ 10ms tick          │  │         │  │  or               │  │   │
│  │  │                    │  │         │  │  EntitySlot.queue │  │   │
│  │  │ ┌────────────────┐ │  │         │  │  (micro-jainslee) │  │   │
│  │  │ │ TimerRecord 1  │ │  │         │  └────────┬──────────┘  │   │
│  │  │ │ TimerRecord 2  │ │  │         │           │             │   │
│  │  │ │ TimerRecord N  │ │  │         │           ▼             │   │
│  │  │ └────────────────┘ │  │         │  ┌───────────────────┐  │   │
│  │  └────────────────────┘  │         │  │ SBB onFireEvent() │  │   │
│  │                          │         │  │ (SBB thread/VT)   │  │   │
│  │  Single wheel thread     │         │  └───────────────────┘  │   │
│  └──────────────────────────┘         └──────────────────────────┘   │
│                                                                       │
│  ❌ WRONG:  wheel thread ──direct──► SBB.onFireEvent()                │
│  ✅ RIGHT:  wheel thread ──fireEvent()──► EventRouter ──► SBB         │
│                                                                       │
│  RATIONALE:                                                           │
│  • JAIN SLEE spec requires SBB to execute within EventRouter context  │
│  • EventRouter provides: tx context, AC pinning, stats, error handling│
│  • Direct execution = race condition, no tx, no stats                 │
└───────────────────────────────────────────────────────────────────────┘
```

### Timer Flow: setTimer → fire → SBB execution

```
SBB Thread                  Wheel Thread              EventRouter           SBB Thread
──────────                  ────────────              ───────────           ──────────

1. timerPort.setTimer(
     duration, callback)
   │
   ├─ Tạo TimerRecord
   ├─ Ghi vào TimerStore
   └─ adapter.schedule(
        record, delay)

                            2. Wheel advances
                               (10ms tick)
                               │
                            3. TimerRecord
                               expires
                               │
                            4. callback.execute()
                               │
                            5. fireEvent(TIMER_EVENT,
                               acHandle)
                               │
                               ├──► NEVER call SBB
                               │    directly here!
                               │
                                                   6. EventRouter
                                                      routes event
                                                      │
                                                   7. Find AC → IES
                                                      │
                                                      └──► deliver to
                                                           SBB entity
                                                                     8. SBB.onFireEvent()
                                                                        (TIMER_EVENT)
                                                                        │
                                                                     9. Process timer
                                                                        logic
```

### jSS7 Scheduler API (micro-jainslee)

```java
// org.restcomm.protocols.ss7.scheduler.api.TimerScheduler
public interface TimerScheduler {
    TimerHandle schedule(TimerRecord record, long delay, TimeUnit unit);
    boolean cancel(TimerHandle handle);
    void start();
    void stop();
}

// HashedWheelTimerFacade — wraps Netty/SS7 HashedWheelTimer
//   512 buckets, 10ms tick resolution
//   Single-threaded wheel (non-blocking timer management)
//
// LocalTimerAdapter — bridges jSS7 Scheduler ↔ SLEE TimerPort
//   Stores TimerRecord (serializable) for recovery
//   Callback → fireEvent() NOT direct SBB call
```

**Why 10ms tick?** Đủ resolution cho telecom timers (USSD timeout: 30s, MAP guard: 5-15s). Thấp hơn → nhiều bucket hơn, overhead scan lớn hơn. Cao hơn → resolution kém.

---

## 4. ActivityContext Lifecycle

### AC State Machine

```
                    ┌─────────┐
                    │  ACTIVE  │◄────────────── fireEvent()
                    └────┬─────┘
                         │
              ┌──────────┼──────────┐
              │          │          │
              ▼          ▼          ▼
        ┌─────────┐ ┌────────┐ ┌──────────┐
        │ ENDING  │ │ENDING  │ │  REMOVED  │
        │ (graceful│ │(forced)│ │ (GC'd)   │
        │  detach) │ └───┬────┘ └──────────┘
        └────┬─────┘     │
             │           │
             ▼           ▼
        ┌─────────────────────┐
        │  ActivityEndEvent   │
        │  (fired to SBBs)    │
        └─────────┬───────────┘
                  │
                  ▼
            ┌─────────┐
            │ REMOVED │
            └─────────┘
```

### AC in Production (Infinispan-backed)

```java
// ActivityContextImpl.java — production AC lifecycle
public class ActivityContextImpl implements ActivityContext {
    private final ActivityContextHandle handle;
    private final ActivityContextCacheData cacheData;  // Infinispan node

    public void fireEvent(EventContext event) {
        // Queue → ActivityEventQueueManager → JTA barrier → EventRouter
    }

    public void endActivity() {
        // Set ending flag → fire ActivityEndEvent → remove from Infinispan
    }
}
```

### AC in micro-jainslee (In-Memory)

```java
class InMemoryActivityContext implements ActivityContext {
    private final String handle;
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();
    private final Set<SbbLocalObject> attachedSbbs = ConcurrentHashMap.newKeySet();
    private volatile boolean ending;
    private volatile long lastAccessTime;

    void touch() { lastAccessTime = System.currentTimeMillis(); }
    void endActivity() { ending = true; }
}

class ActivityContextPool {
    private final ConcurrentHashMap<String, InMemoryActivityContext> pool;
    private final ScheduledExecutorService reaper;

    void reapIdle(long ttlMs) {
        long now = System.currentTimeMillis();
        pool.entrySet().removeIf(e ->
            !e.getValue().isEnding()
            && (now - e.getValue().getLastAccessTime()) > ttlMs);
    }
}
```

### fireEvent() flow detail

```
RA.fireEvent(activityHandle, eventType, event)
│
├─ 1. Lookup AC: pool.get(handle), create if needed
├─ 2. Check AC state: reject if ending
├─ 3. Create EventContext(ac, eventType, event)
├─ 4. EventRouter.route(ctx)
│     ├─ IES: match initial events → create SBB or route to existing
│     └─ Deliver to SBB: sbb.onFireEvent(ctx)
└─ 5. Post: commit/rollback, update access time, collect stats
```

---

## 5. Session Recovery / Snapshot

### micro-jainslee: Session Recovery Service

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SessionRecoveryService                            │
│                                                                     │
│  Snapshot Capture                                                   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  TRIGGER: Entity removal (non-SBB_SELF_REMOVE)               │   │
│  │                                                              │   │
│  │  1. EntitySlot.remove() called                               │   │
│  │     ├─ Save: CMP fields, AC handles, Timer handles           │   │
│  │     ├─ Serialize → JSON/byte[]                                │   │
│  │     └─ Store: Map<SbbEntityId, byte[]> snapshots             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  Rehydration                                                        │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  TRIGGER: Event for AC with no attached SBB                  │   │
│  │                                                              │   │
│  │  1. EventRouter.route(event)                                 │   │
│  │     ├─ IES fails → check snapshot store                      │   │
│  │     ├─ If snapshot found: deserialize → new EntitySlot       │   │
│  │     │   → re-attach AC → re-schedule timers → deliver event  │   │
│  │     └─ If no snapshot: IES creates new entity                │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### Snapshot Capture Rules

| Event | Snapshot? |
|-------|-----------|
| Entity removed by container (timeout, GC) | ✅ YES |
| Entity removed by RA.detachSbbEntity() | ✅ YES |
| Entity removed by SBB_SELF_REMOVE | ❌ NO (SBB tự cleanup) |
| Entity removed by child relation cascade | ✅ YES |
| Normal fireEvent() → return | ❌ NO |

---

## 6. Production vs R&D Comparison

### Side-by-side comparison table

| Dimension | RestComm JAIN-SLEE v8 (Production) | micro-jainslee (R&D) |
|-----------|-----------------------------------|-----------------------|
| **Java** | Java 11+ | Java 25 + Virtual Threads |
| **Runtime** | WildFly 10 / JBoss AS7 | Embedded / Spring Boot / Quarkus |
| **Event Router** | LMAX Disruptor 3.4.4 | In-Memory (EntitySlot per SBB) |
| **Ring Buffer** | 262K slots × N executors | Không có ring buffer |
| **Wait Strategy** | blocking/yielding/busyspin | LinkedBlockingQueue intrinsic |
| **Thread Model** | N platform Disruptor workers + SBB pool | 14 OS threads (ForkJoinPool) backing 100K VTs |
| **SBB Entity Scale** | ~10K-50K per node | 100K entities with ~14 OS threads |
| **Timer** | FaultTolerantScheduler (Infinispan/JTA) | jSS7 HashedWheelTimer (10ms tick) |
| **Persistence** | Infinispan tree cache (replicated) | In-Memory Snapshot Store |
| **Transaction** | JTA (Narayana/Arjuna) | Simple tx context (no 2PC) |
| **Cluster** | ✅ HA with Infinispan | ❌ Single node only |
| **TCK** | ✅ TCK compliant | ❌ Not TCK compliant |
| **JMX** | ✅ JSR-77 MBeans | ❌ No JMX |
| **Deployment** | SLEE Deployable Unit (.jar) | Direct classpath / Spring bean |
| **Profiles** | Infinispan-backed | In-Memory HashMap |
| **AC GC** | Reference counting + Infinispan | Time-based reaper |
| **Stats** | JMX MBeans + EventRouter stats | Console logging |
| **Throughput** | 100K+ events/s per node | ~10K-50K events/s |
| **Latency p99** | < 5μs (disruptor) | ~50μs |
| **Startup time** | 30-60s (WildFly boot) | < 1s (embedded) |
| **Memory baseline** | 500MB+ (WildFly + Infinispan) | ~50MB (minimal) |
| **Use case** | Telecom production (USSD 7.3) | R&D, prototyping, testing |

### Architecture Diagram: Side-by-side

```
┌──────────────────────────────────┐   ┌──────────────────────────────┐
│    RESTCOMM SLEE v8 (PROD)       │   │    MICRO-JAINSLEE (R&D)      │
│                                  │   │                              │
│  WildFly 10                      │   │  Spring Boot / Embedded      │
│  ┌────────────────────────┐      │   │  ┌──────────────────────┐    │
│  │ EventRouter (Disruptor)│      │   │  │ EventRouter(In-Mem)  │    │
│  │ ┌──────┐┌──────┐       │      │   │  └─────────┬────────────┘    │
│  │ │Exec0 ││Exec1 │       │      │   │            │                │
│  │ │262K  ││262K  │       │      │   │  ┌─────────▼────────────┐    │
│  │ └──────┘└──────┘       │      │   │  │ EntitySlot Pool      │    │
│  └────────────────────────┘      │   │  │ VT1..VTN (100K VTs)  │    │
│  ┌────────────────────────┐      │   │  │ 14 OS threads        │    │
│  │ AC (Infinispan)        │      │   │  └──────────────────────┘    │
│  │ Timer (FaultTolerant)  │      │   │  ┌──────────────────────┐    │
│  │ JTA (Narayana)         │      │   │  │ AC Pool (CHM)        │    │
│  └────────────────────────┘      │   │  │ SchedulerBridge(jSS7)│    │
│                                  │   │  │ RecoveryService      │    │
│  Cluster: ✅ Infinispan HA       │   │  └──────────────────────┘    │
│  Deploy: .jar via JMX            │   │                              │
│  Memory: ~500MB+                 │   │  Cluster: ❌ (single node)   │
│  Boot: 30-60s                    │   │  Deploy: classpath / Spring  │
│                                  │   │  Memory: ~50MB               │
│                                  │   │  Boot: < 1s                  │
└──────────────────────────────────┘   └──────────────────────────────┘
```

---

## References

- **EventRouter production:** `container/router/.../eventrouter/EventRouterImpl.java`
- **Disruptor executor:** `container/router/.../eventrouter/DisruptorEventRouterExecutorImpl.java`
- **ActivityHash mapper:** `container/router/.../mapping/ActivityHashingEventRouterExecutorMapper.java`
- **ActivityContext:** `container/activities/.../activity/ActivityContextImpl.java`
- **micro-jainslee EventRouter:** `jainslee-core` → `com.microjainslee.core.EventRouter`
- **VirtualThreadSbbEntityPool test:** `jainslee-core` → `VirtualThreadSbbEntityPoolTest`
- **jSS7 Scheduler:** `jainslee-scheduler` → `org.restcomm.protocols.ss7.scheduler`
- **Session recovery test:** `jainslee-core` → `SbbReclaimRecoveryIntegrationTest`
- **JAIN SLEE 1.1 Spec:** Section 6.4 (AC), Section 10 (Event Router)
- **LMAX Disruptor:** https://lmax-exchange.github.io/disruptor/
- **Java 25 Virtual Threads:** JEP 444
