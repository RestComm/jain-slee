# 🚀 JAIN SLEE Deployment Guide — Production & R&D

> **Hướng dẫn triển khai toàn diện — Comprehensive Deployment Guide**
>
> Last updated: 2025-07-04 | Maintainer: nhanth87
> Base path: `/home/meodien/Desktop/ethiopia-working-dir/jain-slee/jain-slee/`
>
> Ngôn ngữ: Vietnamese + English (song ngữ)

---

## Mục lục / Table of Contents

**Part 1: Production — RestComm JAIN-SLEE v8 + WildFly 10**
- [1.1 Tổng quan Production Stack](#11-tổng-quan-production-stack)
- [1.2 Hardware Requirements](#12-hardware-requirements)
- [1.3 JVM Tuning](#13-jvm-tuning)
- [1.4 Disruptor Configuration](#14-disruptor-configuration)
- [1.5 SBB Pool Sizing](#15-sbb-pool-sizing)
- [1.6 Timer Configuration](#16-timer-configuration)
- [1.7 Clustering — Infinispan + JGroups](#17-clustering--infinispan--jgroups)
- [1.8 Failover Architecture](#18-failover-architecture)
- [1.9 Sample Java OPTS Command Line](#19-sample-java-opts-command-line)
- [1.10 Troubleshooting Checklist](#110-troubleshooting-checklist)

**Part 2: R&D — micro-jainslee Embedded**
- [2.1 ⚠️ R&D ONLY Warning](#21-⚠️-rd-only-warning)
- [2.2 Tổng quan R&D Stack](#22-tổng-quan-rd-stack)
- [2.3 Spring Boot Integration](#23-spring-boot-integration)
- [2.4 Java 25 + ZGC Tuning](#24-java-25--zgc-tuning)
- [2.5 Embedded RAs Setup](#25-embedded-ras-setup)
- [2.6 Docker Considerations](#26-docker-considerations)
- [2.7 R&D Troubleshooting](#27-rd-troubleshooting)

---

## Phần 1 / Part 1: Production Stack

### 1.1 Tổng quan Production Stack

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     RESTCOMM JAIN-SLEE v8 PRODUCTION STACK                    │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                        WildFly 10.0.0.Final                            │   │
│  │                                                                        │   │
│  │  ┌──────────────────────┐  ┌───────────────────────────────────┐     │   │
│  │  │  SLEE Container      │  │  Clustering Layer                 │     │   │
│  │  │                      │  │                                   │     │   │
│  │  │  • EventRouter       │  │  • Infinispan 8.x (distributed)   │     │   │
│  │  │    (LMAX Disruptor)  │  │    - AC cache                     │     │   │
│  │  │  • SBB Pool          │  │    - Timer state                  │     │   │
│  │  │    (Apache Commons)  │  │    - Profile data                 │     │   │
│  │  │  • Timer             │  │  • JGroups 3.x (membership)       │     │   │
│  │  │    (FaultTolerant)   │  │    - Discovery (TCPGOSSIP)        │     │   │
│  │  │  • Transaction       │  │    - Failure detection (FD)       │     │   │
│  │  │    (JTA/Narayana)    │  │    - Merge/split handling         │     │   │
│  │  └──────────────────────┘  └───────────────────────────────────┘     │   │
│  │                                                                        │   │
│  │  ┌──────────────────────────────────────────────────────────────┐     │   │
│  │  │  Resource Adaptors (RA)                                       │     │   │
│  │  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐        │     │   │
│  │  │  │ MAP RA   │ │ TCAP RA  │ │ SCCP RA  │ │ HTTP RA  │  ...   │     │   │
│  │  │  │ (SS7)    │ │ (SS7)    │ │ (SS7)    │ │ (mgmt)   │        │     │   │
│  │  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘        │     │   │
│  │  └──────────────────────────────────────────────────────────────┘     │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  Deployable Units: services-DU.jar, map-ra-du.jar, ussdhttpdemo.war         │
│  Config: standalone.xml, TcapStack_management.xml, UssdManagement.xml        │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Stack thành phần / Component Stack:**

| Component | Version | Vai trò / Role |
|-----------|---------|----------------|
| **Application Server** | WildFly 10.0.0.Final | SLEE container runtime |
| **SLEE Container** | RestComm JAIN-SLEE v8 | JAIN SLEE 1.1 compliant engine |
| **JDK** | Zulu 8 / JDK 8 | JVM runtime (chứng nhận telecom) |
| **Event Router** | LMAX Disruptor 3.4.4 | Low-latency event dispatching |
| **Cache / Persistence** | Infinispan 8.x | Distributed AC, timer, profile cache |
| **Cluster** | JGroups 3.x | Node discovery, failure detection |
| **Transactions** | Narayana (JTA) | Distributed transaction manager |
| **SS7 Stack** | jSS7 (RestComm) | MAP/TCAP/SCCP protocol stack |

---
### 1.2 Hardware Requirements

#### Bảng sizing phần cứng / Hardware Sizing Table

| Tier | CPU Cores | RAM (GB) | Heap (GB) | Ổ đĩa / Disk | Mạng / Network | Throughput |
|------|-----------|----------|-----------|---------------|----------------|------------|
| **Dev/Test** | 8 vCPU | 16 GB | 8 GB | 50 GB SSD | 1 Gbps | ~10K events/s |
| **Standard** | 16 vCPU | 32 GB | 16 GB | 100 GB SSD | 1-10 Gbps | ~50K events/s |
| **Performance** | 24 vCPU | 48 GB | 24 GB | 200 GB NVMe | 10 Gbps | ~80K events/s |
| **High-End** | 32 vCPU | 64 GB | 32 GB | 500 GB NVMe | 10 Gbps bonded | 100K+ events/s |

#### Yêu cầu tối thiểu / Minimum Requirements (Production)

| Resource | Minimum | Khuyến nghị / Recommended |
|----------|---------|---------------------------|
| **CPU** | 16 cores (physical) | 24-32 cores |
| **CPU Architecture** | x86_64 | x86_64 (Intel Xeon / AMD EPYC) |
| **RAM** | 32 GB | 64 GB ECC |
| **Heap (-Xmx)** | 12 GB | 16-32 GB |
| **Heap (-Xms)** | 8 GB (= -Xmx) | 16-32 GB (= -Xmx) |
| **Disk** | 100 GB SSD | 500 GB NVMe (RAID-1) |
| **Disk IOPS** | 3,000 IOPS | 10,000+ IOPS |
| **Network** | 1 Gbps | 10 Gbps (bonded dual NIC) |
| **OS** | RHEL/CentOS 7.x / Ubuntu 18.04+ | RHEL 8.x / Ubuntu 22.04 LTS |
| **JDK** | Zulu JDK 8 / OpenJDK 8 | Zulu JDK 8 (Azul) |

#### Lưu ý / Notes

- **CPU:** JAIN SLEE EventRouter dùng N threads = số CPU cores. Càng nhiều cores → càng nhiều Disruptor executors song song → throughput cao hơn.
- **RAM:** WildFly 10 baseline chiếm ~500MB. Infinispan cache thêm ~2-4GB. Heap còn lại dành cho SBB entities, events, và JTA transactions.
- **Disk:** SSD/NVMe bắt buộc cho Infinispan file-based cache store và WildFly logging.
- **Network:** Cluster replication (Infinispan) yêu cầu băng thông cao giữa các node. Đề xuất dedicated cluster NIC.

---
### 1.3 JVM Tuning

#### Cấu hình JVM cho Production / Production JVM Configuration

```bash
# ── standalone.conf ──
# File: ${WILDFLY_HOME}/bin/standalone.conf
# Đường dẫn triển khai thực tế:
#   /opt/restcomm/restcomm-ussd-7.3.1-SNAPSHOT/wildfly-10.0.0.Final/bin/standalone.conf

JAVA_OPTS="\
  -server \
  -Xms16g \
  -Xmx16g \
  -Xss256k \
  \
  # ── Metaspace ──
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m \
  \
  # ── GC: G1 (default cho heap >4GB, stable trên JDK 8) ──
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:G1HeapRegionSize=16m \
  -XX:InitiatingHeapOccupancyPercent=45 \
  -XX:+ParallelRefProcEnabled \
  -XX:+DisableExplicitGC \
  \
  # ── G1 Logging (JDK 8) ──
  -XX:+PrintGCDetails \
  -XX:+PrintGCDateStamps \
  -XX:+PrintGCTimeStamps \
  -XX:+PrintGCApplicationStoppedTime \
  -Xloggc:\${WILDFLY_HOME}/standalone/log/gc.log \
  -XX:+UseGCLogFileRotation \
  -XX:NumberOfGCLogFiles=10 \
  -XX:GCLogFileSize=50M \
  \
  # ── Compressed OOPs (tự động với heap <32GB) ──
  -XX:+UseCompressedOops \
  -XX:+UseCompressedClassPointers \
  \
  # ── System ──
  -Djava.net.preferIPv4Stack=true \
  -Djava.awt.headless=true \
  -Dfile.encoding=UTF-8 \
  \
  # ── WildFly ──
  -Djboss.modules.system.pkgs=org.jboss.byteman,org.jboss.logmanager \
  -Djava.util.logging.manager=org.jboss.logmanager.LogManager \
  \
  # ── SLEE ──
  -Djboss.slee.container=true \
  \
  # ── JMX (JSR-77) ──
  -Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.port=9999 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
"
```

#### Giải thích các tham số GC / GC Parameter Explanation

| Tham số / Parameter | Giá trị / Value | Giải thích |
|---------------------|-----------------|------------|
| `-XX:+UseG1GC` | enabled | G1GC tốt nhất cho heap 8-32GB, pause time dự đoán được |
| `-XX:MaxGCPauseMillis=100` | 100ms | Mục tiêu pause tối đa 100ms — phù hợp telecom soft-real-time |
| `-XX:G1HeapRegionSize=16m` | 16MB/region | Với heap 16GB → ~1024 regions. Đủ mịn để G1 quản lý hiệu quả |
| `-XX:InitiatingHeapOccupancyPercent=45` | 45% | Bắt đầu concurrent marking sớm, tránh Full GC evacuation failure |
| `-XX:+ParallelRefProcEnabled` | enabled | Reference processing đa luồng, giảm pause time |
| `-XX:+DisableExplicitGC` | enabled | Ngăn System.gc() từ application code (SLEE container có thể gọi) |

#### Tại sao không dùng ZGC? / Why not ZGC?

ZGC yêu cầu JDK 11+ (production-ready từ JDK 15). Production USSD 7.3 build từ Zulu JDK 8 (Mobicents SLEE master-era JARs). Việc nâng cấp JDK có thể phá vỡ compatibility với các module WildFly 10 và SLEE container.

---
### 1.4 Disruptor Configuration

#### Kiến trúc Ring Buffer / Ring Buffer Architecture

```
┌──────────────────────────────────────────────────────────────────────────┐
│                     LMAX Disruptor EventRouter                             │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐     │
│  │  ActivityHashingEventRouterExecutorMapper                        │     │
│  │  AC.handle.hashCode() % N → executor[N]                          │     │
│  └───────────────────────────┬─────────────────────────────────────┘     │
│         ┌────────────────────┼────────────────────┐                      │
│         ▼                    ▼                     ▼                      │
│  ┌─────────────┐      ┌─────────────┐       ┌─────────────┐              │
│  │  Executor 0  │      │  Executor 1  │  ...  │  Executor N  │              │
│  │  ┌─────────┐ │      │  ┌─────────┐ │       │  ┌─────────┐ │              │
│  │  │RingBuf  │ │      │  │RingBuf  │ │       │  │RingBuf  │ │              │
│  │  │262,144  │ │      │  │262,144  │ │       │  │262,144  │ │              │
│  │  │ slots   │ │      │  │ slots   │ │       │  │ slots   │ │              │
│  │  │~16 MB   │ │      │  │~16 MB   │ │       │  │~16 MB   │ │              │
│  │  └────┬────┘ │      │  └────┬────┘ │       │  └────┬────┘ │              │
│  │       │      │      │       │      │       │       │      │              │
│  │  ┌────▼────┐ │      │  ┌────▼────┐ │       │  ┌────▼────┐ │              │
│  │  │ Worker  │ │      │  │ Worker  │ │       │  │ Worker  │ │              │
│  │  │ Thread  │ │      │  │ Thread  │ │       │  │ Thread  │ │              │
│  │  └─────────┘ │      │  └─────────┘ │       │  └─────────┘ │              │
│  └─────────────┘      └─────────────┘       └─────────────┘              │
│                                                                           │
│  N = max(4, availableProcessors)                                          │
│  Wait Strategy: BLOCKING (default)                                        │
│  Producer Type: SINGLE (per executor)                                     │
└──────────────────────────────────────────────────────────────────────────┘
```

#### JVM Properties Cấu hình Disruptor / Disruptor JVM Properties

```bash
# ── Disruptor EventRouter Properties ──
# File: standalone.conf (thêm vào JAVA_OPTS)

# Số lượng executor threads = số CPU cores (tối thiểu 4)
-Djainslee.eventrouter.threads=16

# Kích thước ring buffer (phải là power-of-2, default 262144)
-Djainslee.eventrouter.ringsize=262144

# Wait strategy: blocking | yielding | busyspin
-Djainslee.eventrouter.waitstrategy=blocking

# Multi-producer mode (default false = SINGLE)
-Djainslee.eventrouter.multiproducer=false
```

#### Bảng tham chiếu kích thước Ring Buffer / Ring Buffer Sizing Reference

| Số lượng Executors | Ring Size | RAM/Ring Buffer | Tổng RAM (Ring Buffers) | Burst Capacity |
|--------------------|-----------|-----------------|------------------------|----------------|
| 4 | 131,072 | ~8 MB | ~32 MB | ~25K events/s |
| 8 | 131,072 | ~8 MB | ~64 MB | ~50K events/s |
| 8 | 262,144 | ~16 MB | ~128 MB | ~80K events/s |
| 16 | 262,144 | ~16 MB | ~256 MB | 100K+ events/s |
| 32 | 262,144 | ~16 MB | ~512 MB | 200K+ events/s |

**Công thức tính / Formula:**
- Ring buffer RAM = `ringSize × 64 bytes` (mỗi slot ≈ 64 byte: EventWrapper)
- Tổng Ring RAM = `N_executors × ringSize × 64 bytes`

#### Wait Strategy Comparison

| Strategy | CPU Usage | Latency P99 | Use Case |
|----------|-----------|-------------|----------|
| **blocking** (default) | Thấp / Low | ~1-5 μs | Hầu hết workload (khuyến nghị). Worker thread park khi ring trống. |
| **yielding** | Trung bình / Medium | ~0.1-1 μs | Low-latency, worker spin-yield khi ring trống. |
| **busyspin** | Rất cao / Very High | ~0.05 μs | Ultra-low-latency, worker spin liên tục. Cần dedicated cores! |

> ⚠️ **Cảnh báo:** `busyspin` sẽ chiếm 100% CPU trên tất cả worker threads ngay cả khi không có event. Chỉ dùng khi có dedicated CPU cores và latency requirement cực thấp.

---
### 1.6 Timer Configuration

#### FaultTolerantScheduler / Cluster-Aware Timer

```bash
# ── Timer Properties ──
# File: standalone.conf (thêm vào JAVA_OPTS)

# Số lượng timer threads (mặc định 4)
-Djainslee.timer.threads=4

# Timer resolution (ms) — ảnh hưởng đến độ chính xác timer
-Djainslee.timer.resolution=100

# Infinispan cache container cho timer state
-Djainslee.timer.cache-container=slee-timer-cache

# Replication timeout (ms) cho timer HA
-Djainslee.timer.replication-timeout=5000
```

| Tham số / Parameter | Mặc định / Default | Khuyến nghị / Recommended | Giải thích |
|---------------------|--------------------|---------------------------|------------|
| `threads` | 4 | 4-8 | Mỗi thread quét 1 phần của timer wheel. Cluster-aware: timer được shard giữa các node |
| `resolution` | 100ms | 100ms | Độ chính xác timer ~100ms. Không nên đặt quá thấp (tăng CPU scan overhead) |
| `replication-timeout` | 5000ms | 5000ms | Timeout replicate timer state sang node khác trước khi failover |

---
### 1.5 SBB Pool Sizing

#### Cấu hình Apache Commons Pool / Apache Commons Pool Configuration

```xml
<!-- standalone.xml → SLEE subsystem configuration -->
<subsystem xmlns="urn:org.mobicents:slee:container:2.0">
    <sbb-pool>
        <!-- Số instance tối thiểu luôn sẵn sàng trong pool -->
        <min-idle>5000</min-idle>

        <!-- Số instance tối đa có thể active đồng thời -->
        <max-active>100000</max-active>

        <!-- Số instance tối đa idle (chờ event) -->
        <max-idle>80000</max-idle>

        <!-- Block khi pool exhausted -->
        <when-exhausted>BLOCK</when-exhausted>

        <!-- Max wait time khi pool exhausted (ms) -->
        <max-wait>5000</max-wait>

        <!-- Eviction policy -->
        <time-between-eviction-runs>60000</time-between-eviction-runs>
        <min-evictable-idle-time>300000</min-evictable-idle-time>
    </sbb-pool>
</subsystem>
```

#### Sizing Guidelines

| Metric | Công thức / Formula | Ví dụ / Example |
|--------|---------------------|-----------------|
| **min-idle** | `(expected_concurrent_sessions × 0.1)` | 50K sessions → 5,000 |
| **max-active** | `(expected_concurrent_sessions × 1.5)` | 50K sessions → 75,000 (round up: 100,000) |
| **max-idle** | `(max-active × 0.8)` | 100,000 → 80,000 |
| **Memory per SBB** | ~2-5KB (CMP fields + context) | 100K SBBs → ~200-500MB |

#### Tại sao pool quan trọng? / Why is pooling important?

- **min-idle:** Tránh object creation latency khi burst traffic. SBB entity tạo sẵn, chỉ cần `sbbActivate()`.
- **max-active:** Giới hạn memory usage. Ngăn OOM khi traffic spike bất thường.
- **max-idle:** SBB entities dư thừa được passivate (ghi CMP vào Infinispan) để tiết kiệm RAM.

---
### 1.7 Clustering — Infinispan + JGroups

#### Kiến trúc Cluster / Cluster Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         JAIN SLEE CLUSTER (N=3)                              │
│                                                                              │
│  ┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐         │
│  │    Node 1        │   │    Node 2        │   │    Node 3        │         │
│  │  (10.0.1.1)      │   │  (10.0.1.2)      │   │  (10.0.1.3)      │         │
│  │                  │   │                  │   │                  │         │
│  │  ┌────────────┐  │   │  ┌────────────┐  │   │  ┌────────────┐  │         │
│  │  │ Infinispan │◄─┼───┼─►│ Infinispan │◄─┼───┼─►│ Infinispan │  │         │
│  │  │ Cache      │  │   │  │ Cache      │  │   │  │ Cache      │  │         │
│  │  │            │  │   │  │            │  │   │  │            │  │         │
│  │  │ • AC (DIST)│  │   │  │ • AC (DIST)│  │   │  │ • AC (DIST)│  │         │
│  │  │ • Timer    │  │   │  │ • Timer    │  │   │  │ • Timer    │  │         │
│  │  │ • Profile  │  │   │  │ • Profile  │  │   │  │ • Profile  │  │         │
│  │  └──────┬─────┘  │   │  └──────┬─────┘  │   │  └──────┬─────┘  │         │
│  │         │        │   │         │        │   │         │        │         │
│  │  ┌──────▼─────┐  │   │  ┌──────▼─────┐  │   │  ┌──────▼─────┐  │         │
│  │  │  JGroups   │  │   │  │  JGroups   │  │   │  │  JGroups   │  │         │
│  │  │  Channel   │◄─┼───┼─►│  Channel   │◄─┼───┼─►│  Channel   │  │         │
│  │  └────────────┘  │   │  └────────────┘  │   │  └────────────┘  │         │
│  └──────────────────┘   └──────────────────┘   └──────────────────┘         │
│           │                      │                      │                   │
│           └──────────────────────┼──────────────────────┘                   │
│                                  │                                          │
│                     ┌────────────▼────────────┐                             │
│                     │  JGroups Discovery       │                             │
│                     │  TCPGOSSIP / MPING       │                             │
│                     │                          │                             │
│                     │  Gossip Router:          │                             │
│                     │  10.0.1.100:12001        │                             │
│                     └─────────────────────────┘                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

#### Infinispan Cache Configuration

```xml
<!-- standalone.xml → Infinispan subsystem -->
<subsystem xmlns="urn:jboss:domain:infinispan:4.0">
    <cache-container name="slee" default-cache="slee-ac-cache"
                     statistics-enabled="true">
        <transport channel="slee-cluster" lock-timeout="60000"/>

        <!-- Activity Context Cache: DISTRIBUTED, owners=2 -->
        <distributed-cache name="slee-ac-cache"
                           owners="2" segments="256"
                           l1-lifespan="60000" mode="SYNC">
            <eviction strategy="LRU" max-entries="100000"/>
            <expiration lifespan="3600000"/>
        </distributed-cache>

        <!-- Timer State Cache: DISTRIBUTED, owners=2 -->
        <distributed-cache name="slee-timer-cache"
                           owners="2" segments="128" mode="SYNC">
            <eviction strategy="NONE"/>
        </distributed-cache>

        <!-- Profile Cache: REPLICATED -->
        <replicated-cache name="slee-profile-cache" mode="SYNC">
            <eviction strategy="NONE"/>
        </replicated-cache>
    </cache-container>
</subsystem>
```

#### JGroups Configuration

```xml
<!-- standalone.xml → JGroups subsystem -->
<subsystem xmlns="urn:jboss:domain:jgroups:4.0">
    <channels>
        <channel name="slee-cluster">
            <stack name="slee-stack"/>
        </channel>
    </channels>
    <stacks>
        <stack name="slee-stack">
            <!-- TCP transport (production, reliable) -->
            <transport type="TCP"
                       bind_addr="${jboss.bind.address:10.0.1.1}"
                       bind_port="7800" port_range="10"
                       recv_buf_size="20m" send_buf_size="20m"
                       enable_bundling="true"
                       thread_pool_max_threads="20"/>

            <!-- Discovery: TCPGOSSIP -->
            <protocol type="TCPGOSSIP">
                <property name="initial_hosts">
                    10.0.1.100[12001],10.0.1.101[12001]
                </property>
                <property name="reconnect_interval">2000</property>
            </protocol>

            <!-- Failure Detection (3 heartbeat misses → 9s total) -->
            <protocol type="FD">
                <property name="timeout">3000</property>
                <property name="max_tries">3</property>
            </protocol>

            <!-- Merge Handling -->
            <protocol type="MERGE3">
                <property name="min_interval">10000</property>
                <property name="max_interval">30000</property>
            </protocol>

            <protocol type="UNICAST3"/>
            <protocol type="pbcast.NAKACK2">
                <property name="use_mcast_xmit">false</property>
            </protocol>
            <protocol type="pbcast.STABLE">
                <property name="desired_avg_gossip">5000</property>
            </protocol>
            <protocol type="pbcast.GMS">
                <property name="join_timeout">5000</property>
            </protocol>
            <protocol type="MFC">
                <property name="max_credits">2M</property>
            </protocol>
            <protocol type="UFC">
                <property name="max_credits">2M</property>
            </protocol>
            <protocol type="FRAG3">
                <property name="frag_size">60000</property>
            </protocol>
        </stack>
    </stacks>
</subsystem>
```

#### Cấu hình chính / Key Configuration Values

| Tham số | Giá trị | Giải thích |
|---------|---------|------------|
| **Cache mode** | DISTRIBUTED (AC, Timer), REPLICATED (Profile) | DIST: data được shard. REPL: mọi node có full copy |
| **owners** | 2 | Mỗi cache entry có 2 bản sao → chịu được 1 node chết |
| **segments** | 256 (AC), 128 (Timer) | Số lượng hash segments cho phân phối đều |
| **mode** | SYNC | Đồng bộ (an toàn hơn ASYNC, latency cao hơn chút) |
| **lock-timeout** | 60000ms | Timeout acquire distributed lock |
| **l1-lifespan** | 60000ms | L1 cache (near cache) lifespan |
| **bind_port** | 7800 | JGroups cluster port (mở firewall) |
| **FD timeout** | 3000ms × 3 = 9s | Tổng thời gian phát hiện node chết |

---
### 1.8 Failover Architecture

#### Scenario: Node 1 fails, Node 2 takes over

```
                        TIME ──────────────────────────────►

  ┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
  │  NORMAL  │     │  DETECT  │     │ TRANSFER │     │ RECOVERY │
  │  STATE   │     │  FAILURE │     │  STATE   │     │  ACTIVE  │
  └────┬─────┘     └────┬─────┘     └────┬─────┘     └────┬─────┘
       │                │                │                │
       │  3 nodes up    │  FD detects    │  Infinispan    │  Node 2/3
       │  all serving   │  Node 1 down   │  redistributes │  serving all
       │  AC: 1,2,3     │  (9s timeout)  │  AC partitions │  traffic
       │                │                │                │
  ──0s──           ──9s──           ──12s──          ──15s───►

  STEP 1: NORMAL              STEP 2: FAILURE DETECTION
  ┌─────────────────┐         ┌─────────────────────────────┐
  │  Node 1 (ACTIVE) │         │  JGroups FD: 3 heartbeat    │
  │  ├─ AC segment A  │         │  misses → SUSPECT          │
  │  ├─ AC segment B  │    X    │  VERIFY_SUSPECT: timeout   │
  │  ├─ Timers: 1-500 │         │  → Node 1 marked DEAD      │
  │  └─ Profile: full │         │  GMS: new view [Node2,3]  │
  │                   │         └─────────────────────────────┘
  │  Node 2 (ACTIVE)  │
  │  ├─ AC segment C  │         STEP 3: STATE TRANSFER
  │  └─ Timers: 501-1K│         ┌─────────────────────────────┐
  │                   │         │  Infinispan rebalance:       │
  │  Node 3 (ACTIVE)  │         │  AC entries from Node1       │
  │  ├─ AC segment D  │         │  → redistributed to Node2,3 │
  │  └─ Timers: 1K-1.5│         │  owners=2 → mỗi entry có    │
  └─────────────────┘         │  bản sao trên 2 node còn lại │
                               │  Timer state: replicate      │
  STEP 4: RECOVERY             │  Profile: đã có full copy    │
  ┌─────────────────────────┐  └─────────────────────────────┘
  │  Node 2:                │
  │  ├─ AC segment A* (+B)  │
  │  ├─ AC segment C        │
  │  └─ Timers: 1-1000      │
  │                         │
  │  Node 3:                │
  │  ├─ AC segment A* (+D)  │
  │  ├─ AC segment B* (+D)  │
  │  └─ Timers: 500-1500    │
  │                         │
  │  * = recovered từ Node1 │
  └─────────────────────────┘
```

#### Failover Timeline

| Thời gian / Time | Sự kiện / Event | Chi tiết / Detail |
|-------------------|-----------------|-------------------|
| **T+0s** | Node 1 crashes | JVM crash, network partition, hoặc hardware failure |
| **T+0-3s** | Heartbeat missed | JGroups FD gửi ARE_YOU_ALIVE, không nhận được ACK |
| **T+3-6s** | VERIFY_SUSPECT | JGroups yêu cầu các node khác xác nhận Node 1 unreachable |
| **T+6-9s** | Node marked DEAD | GMS cập nhật view mới: [Node2, Node3] |
| **T+9-12s** | Infinispan rebalance | AC entries từ Node1 được redistribute sang Node2/Node3 |
| **T+12-15s** | Cache ready | Tất cả AC/Timer data đã có 2 bản sao trên cluster còn lại |
| **T+15s** | **Full recovery** | Cluster hoạt động bình thường, mất ~15s downtime |

> ⚠️ **Lưu ý:** Các session đang active trên Node 1 sẽ bị mất nếu chưa kịp replicate sang node khác (replication async delay). Sử dụng `mode="SYNC"` cho Infinispan để đảm bảo data an toàn.

---
### 1.9 Sample Java OPTS Command Line

#### Full Production Command Line

```bash
#!/bin/bash
# ── Full Production Java OPTS cho USSD Gateway 7.3 ──
# File: ${WILDFLY_HOME}/bin/standalone.conf
# Triển khai tại: /opt/restcomm/restcomm-ussd-7.3.1-SNAPSHOT/wildfly-10.0.0.Final/bin/

export JAVA_HOME=/usr/lib/jvm/zulu-8-amd64
export JBOSS_HOME=/opt/restcomm/restcomm-ussd-7.3.1-SNAPSHOT/wildfly-10.0.0.Final

JAVA_OPTS="\
  # ============================================================
  # HEAP & MEMORY
  # ============================================================
  -server
  -Xms16g
  -Xmx16g
  -Xss256k
  -XX:MetaspaceSize=256m
  -XX:MaxMetaspaceSize=512m
  -XX:+UseCompressedOops
  -XX:+UseCompressedClassPointers

  # ============================================================
  # GARBAGE COLLECTION (G1GC)
  # ============================================================
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=100
  -XX:G1HeapRegionSize=16m
  -XX:InitiatingHeapOccupancyPercent=45
  -XX:+ParallelRefProcEnabled
  -XX:+DisableExplicitGC
  -XX:+PrintGCDetails
  -XX:+PrintGCDateStamps
  -XX:+PrintGCTimeStamps
  -XX:+PrintGCApplicationStoppedTime
  -Xloggc:\${JBOSS_HOME}/standalone/log/gc.log
  -XX:+UseGCLogFileRotation
  -XX:NumberOfGCLogFiles=10
  -XX:GCLogFileSize=50M

  # ============================================================
  # HEAP DUMP (OOM diagnostics)
  # ============================================================
  -XX:+HeapDumpOnOutOfMemoryError
  -XX:HeapDumpPath=\${JBOSS_HOME}/standalone/log/heapdump.hprof
  -XX:OnOutOfMemoryError='kill -9 %p'

  # ============================================================
  # JAIN SLEE — EventRouter (LMAX Disruptor)
  # ============================================================
  -Djainslee.eventrouter.threads=16
  -Djainslee.eventrouter.ringsize=262144
  -Djainslee.eventrouter.waitstrategy=blocking
  -Djainslee.eventrouter.multiproducer=false

  # ============================================================
  # JAIN SLEE — Timer
  # ============================================================
  -Djainslee.timer.threads=4
  -Djainslee.timer.resolution=100
  -Djainslee.timer.replication-timeout=5000

  # ============================================================
  # NETWORK & SYSTEM
  # ============================================================
  -Djava.net.preferIPv4Stack=true
  -Djava.awt.headless=true
  -Dfile.encoding=UTF-8
  -Duser.timezone=UTC
  -Dcom.sun.management.jmxremote
  -Dcom.sun.management.jmxremote.port=9999
  -Dcom.sun.management.jmxremote.authenticate=false
  -Dcom.sun.management.jmxremote.ssl=false

  # ============================================================
  # WILDFLY / JBOSS
  # ============================================================
  -Djboss.modules.system.pkgs=org.jboss.byteman,org.jboss.logmanager
  -Djava.util.logging.manager=org.jboss.logmanager.LogManager
  -Djboss.slee.container=true
  -Djboss.bind.address=0.0.0.0
  -Djboss.bind.address.management=0.0.0.0
  -Djboss.node.name=ussd-node-01
  -Djboss.server.base.dir=\${JBOSS_HOME}/standalone
  -Djboss.server.log.dir=\${JBOSS_HOME}/standalone/log

  # ============================================================
  # CLUSTER (JGroups discovery)
  # ============================================================
  -Djgroups.bind_addr=10.0.1.1
  -Djgroups.tcpgossip.initial_hosts=10.0.1.100[12001],10.0.1.101[12001]

  # ============================================================
  # JOLOKIA (optional — health check)
  # ============================================================
  -Dhawtio.authenticationEnabled=false
"
```

---
### 1.10 Troubleshooting Checklist

#### 🔴 Production Issues — Diagnostic Flow

```
┌─────────────────────────────────────────────────────────────────┐
│              PRODUCTION TROUBLESHOOTING FLOW                     │
│                                                                 │
│  ISSUE REPORTED                                                 │
│       │                                                         │
│       ▼                                                         │
│  ┌─────────────┐                                                │
│  │ 1. CHECK    │ Is WildFly running?                            │
│  │    ALIVE    │ curl http://host:8080/jolokia/version           │
│  └──────┬──────┘                                                │
│         │                                                       │
│    ┌────┴────┐                                                  │
│    │ DEAD    │ → Check: OOM? Crash loop? Disk full?             │
│    │         │ → View: standalone/log/server.log                │
│    │         │ → View: standalone/log/gc.log (last GC before OOM)│
│    │         │ → View: heapdump.hprof (MAT analyzer)            │
│    └────┬────┘                                                  │
│         │                                                       │
│    ┌────┴────┐                                                  │
│    │ ALIVE   │ → Check metrics                                  │
│    │ BUT SLOW│ → GC pause time > 200ms?                         │
│    │         │ → Disruptor ring buffer full?                    │
│    │         │ → Thread pool exhausted?                         │
│    │         │ → Network latency to other nodes?                │
│    └────┬────┘                                                  │
│         │                                                       │
│    ┌────┴────┐                                                  │
│    │ CLUSTER │ → Check cluster health                           │
│    │ ISSUE   │ → JGroups view: all nodes present?               │
│    │         │ → Infinispan: cache entries balanced?            │
│    │         │ → Split-brain? Network partition?                │
│    │         │ → Firewall blocking ports 7800?                  │
│    └────────┘                                                   │
└─────────────────────────────────────────────────────────────────┘
```

#### Bảng checklist / Troubleshooting Checklist Table

| # | Triệu chứng / Symptom | Chẩn đoán / Diagnosis | Hành động / Action |
|---|----------------------|----------------------|-------------------|
| **1** | **WildFly không khởi động** | Port conflict? Permission? Config error? | `netstat -tlnp \| grep 8080`, `tail -f standalone/log/server.log` |
| **2** | **OOM (OutOfMemoryError)** | Heap exhausted | Phân tích `heapdump.hprof` bằng Eclipse MAT. Tăng `-Xmx`. Kiểm tra memory leak (timer, AC, RA connections) |
| **3** | **GC Pause > 1s** | G1GC evacuation failure? | Xem `gc.log`. Tăng `-XX:G1HeapRegionSize`. Giảm `-XX:InitiatingHeapOccupancyPercent` |
| **4** | **Event latency cao** | Ring buffer full? Worker threads blocked? | JMX: `EventRouter.eventQueueSize`. Nếu > 100K → tăng ring size hoặc thêm executors |
| **5** | **SBB pool exhausted** | `max-active` đạt giới hạn | JMX: `SbbPool.activeCount`. Tăng `max-active`. Kiểm tra SBB leak |
| **6** | **Timer không fire** | Timer thread blocked? Replication failed? | JMX: `TimerFacility.activeTimerCount`. Xem log: `FaultTolerantScheduler` errors |
| **7** | **Cluster split-brain** | 2 subsets cùng claim ownership | JGroups log: `Received new cluster view`. Khôi phục: restart node bị tách |
| **8** | **Node không join cluster** | Network partition? Firewall? | `telnet <other-node> 7800`. Kiểm tra `bind_addr` đúng IP |
| **9** | **MAP dialog leak** | Dialog không được release | JMX: `MapRa.activeDialogs`. SBB `sbbRemove()` phải gọi `mapRa.releaseDialog()` |
| **10** | **Disk full** | Log rotation không hoạt động | `du -sh standalone/log/`. Xóa log cũ, cấu hình log rotate |
| **11** | **Infinispan timeout** | Cache lock timeout | Log: `TimeoutException acquiring lock`. Tăng `lock-timeout` |
| **12** | **High CPU idle** | Wait strategy `busyspin` khi không có traffic | Đổi sang `blocking` wait strategy |

#### Lệnh kiểm tra nhanh / Quick Diagnostic Commands

```bash
# ── Health Check ──
# Jolokia (JMX over HTTP)
curl http://localhost:8080/jolokia/version

# WildFly management API (port 9990)
curl -s -u admin:pass http://localhost:9990/management \
  -d '{"operation":"read-attribute","address":[],"name":"server-state"}' \
  -H 'Content-Type: application/json'

# ── Memory ──
# Heap usage via Jolokia
curl http://localhost:8080/jolokia/read/java.lang:type=Memory/HeapMemoryUsage

# ── Threads ──
jstack -l <pid> > thread_dump.txt

# ── GC Stats ──
jstat -gcutil <pid> 1000 10

# ── Cluster ──
# Check JGroups view
grep "Received new cluster view" standalone/log/server.log | tail -5

# ── Network ──
ss -tlnp | grep 7800

# ── File Descriptors ──
cat /proc/<pid>/limits | grep "open files"
# Đảm bảo > 65536. Set: ulimit -n 65536
```

---

## Phần 2 / Part 2: R&D Stack

### 2.1 ⚠️ R&D ONLY Warning

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│   ⛔  CẢNH BÁO NGHIÊM TRỌNG  —  CRITICAL WARNING                            │
│                                                                             │
│   ╔═══════════════════════════════════════════════════════════════════════╗ │
│   ║                                                                       ║ │
│   ║   MICRO-JAINSLEE LÀ R&D ONLY — TUYỆT ĐỐI KHÔNG DÙNG CHO PRODUCTION    ║ │
│   ║                                                                       ║ │
│   ║   micro-jainslee IS FOR RESEARCH & DEVELOPMENT ONLY                   ║ │
│   ║   NEVER DEPLOY TO PRODUCTION                                          ║ │
│   ║                                                                       ║ │
│   ║   LÝ DO / REASONS:                                                    ║ │
│   ║   ❌ Không TCK compliant — Not TCK compliant                          ║ │
│   ║   ❌ Không có cluster HA — No cluster / high availability             ║ │
│   ║   ❌ Không có Infinispan persistence — No distributed persistence     ║ │
│   ║   ❌ Không có JTA transactions — No JTA transaction manager           ║ │
│   ║   ❌ Không có JSR-77 MBean support — No management MBeans             ║ │
│   ║   ❌ EventRouter đơn giản hóa — Simplified EventRouter                ║ │
│   ║   ❌ Timer không fault-tolerant — Timer not fault-tolerant            ║ │
│   ║                                                                       ║ │
│   ║   PRODUCTION PHẢI DÙNG — PRODUCTION MUST USE:                         ║ │
│   ║   ✅ RestComm JAIN-SLEE v8 + WildFly 10                               ║ │
│   ║   ✅ Mobicents SLEE container master-era JARs                          ║ │
│   ║                                                                       ║ │
│   ╚═══════════════════════════════════════════════════════════════════════╝ │
│                                                                             │
│   micro-jainslee targets:                                                   │
│   • Phát triển SBB logic mới (New SBB development)                          │
│   • Prototype nhanh (Rapid prototyping)                                      │
│   • Integration testing (Kiểm thử tích hợp)                                  │
│   • CI/CD pipeline testing                                                   │
│   • Developer workstation (dev machine)                                      │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---
### 2.2 Tổng quan R&D Stack

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     MICRO-JAINSLEE R&D STACK (Java 25)                       │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                    Embedded Runtime (No WildFly!)                      │   │
│  │                                                                        │   │
│  │  ┌────────────────────────┐  ┌──────────────────────────────────┐    │   │
│  │  │  MicroSleeContainer    │  │  VirtualThreadSbbEntityPool       │    │   │
│  │  │  (Bootstrap)           │  │  • 100K SBBs → ~14 OS threads     │    │   │
│  │  │                        │  │  • Parked VT = ~300 bytes         │    │   │
│  │  │  • load SBB index      │  │  • Active VT = mounted on carrier │    │   │
│  │  │  • init RA registry    │  │  • ForkJoinPool scheduler         │    │   │
│  │  │  • start Timer bridge  │  └──────────────────────────────────┘    │   │
│  │  └────────────────────────┘                                           │   │
│  │                                                                        │   │
│  │  ┌────────────────────────┐  ┌──────────────────────────────────┐    │   │
│  │  │  EventRouter (In-Mem)  │  │  SleeTimerSchedulerBridge         │    │   │
│  │  │  • EntitySlot per SBB  │  │  • jSS7 HashedWheelTimer (10ms)   │    │   │
│  │  │  • LinkedBlockingQueue │  │  • fireEvent() NOT direct SBB     │    │   │
│  │  │  • ActivityContextPool │  │  • 512 buckets, single wheel      │    │   │
│  │  └────────────────────────┘  └──────────────────────────────────┘    │   │
│  │                                                                        │   │
│  │  ┌─────────────────────────────────────────────────────────────────┐ │   │
│  │  │  Embedded RAs                                                   │ │   │
│  │  │  ┌────────────┐ ┌────────────┐ ┌──────────────┐                │ │   │
│  │  │  │ gRPC Client│ │ HTTP       │ │ SS7 USSD     │ (simulator)   │ │   │
│  │  │  │ RA         │ │ Ingress RA │ │ Ingress SBB  │                │ │   │
│  │  │  └────────────┘ └────────────┘ └──────────────┘                │ │   │
│  │  └─────────────────────────────────────────────────────────────────┘ │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  Frameworks: Spring Boot 3.x / Quarkus / Plain Java                          │
│  Deploy: fat JAR / classpath / Docker (R&D only)                             │
│  Boot time: < 1 giây / second                                                │
│  Memory baseline: ~50 MB                                                     │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### 2.3 Spring Boot Integration

#### Dependency (pom.xml)

```xml
<!-- micro-jainslee Spring Boot Starter -->
<dependency>
    <groupId>com.microjainslee</groupId>
    <artifactId>jainslee-adapter-springboot</artifactId>
    <version>1.1.0</version>
</dependency>

<!-- Spring Boot (Java 25 compatible) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>3.4.0</version>
</dependency>

<!-- Optional: gRPC RA -->
<dependency>
    <groupId>com.microjainslee</groupId>
    <artifactId>ra-grpc-client</artifactId>
    <version>1.1.0</version>
    <scope>runtime</scope>
</dependency>

<!-- Optional: HTTP Ingress RA -->
<dependency>
    <groupId>com.microjainslee</groupId>
    <artifactId>ra-http-ingress</artifactId>
    <version>1.1.0</version>
    <scope>runtime</scope>
</dependency>
```

#### application.yml

```yaml
# ═══════════════════════════════════════════════════════════════
# application.yml — micro-jainslee Spring Boot Configuration
# ═══════════════════════════════════════════════════════════════

spring:
  application:
    name: ussd-rd-demo
  main:
    web-application-type: none  # Tắt Spring MVC (RA xử lý HTTP)
    banner-mode: off

# ═══════════════════════════════════════════════════════════════
# micro-jainslee Container Configuration
# ═══════════════════════════════════════════════════════════════
micro-jainslee:

  container:
    sbb-index:
      scan-packages:
        - com.example.ussddemo.sbbs
        - com.example.myservice.logic
      loader-threads: 2

  event-router:
    buffer-size: 2048                 # per-entity queue capacity
    prefer-virtual-threads: true      # SBB entity → virtual thread
    dispatch-threads: 4               # RA fireEvent dispatch threads

  sbb-pool:
    min: 16                           # Tối thiểu pre-warmed entities
    max: 4096                         # Tối đa entities (R&D scale)
    per-virtual-thread: true          # 1 entity = 1 parked virtual thread
    idle-timeout-seconds: 300         # Reap idle entities after 5 min
    max-parked-entities: 5000         # Soft limit on parked VTs

  timer:
    threads: 4                        # Wheel threads (I/O dispatch only)
    tick-duration-ms: 10              # 10ms wheel resolution
    buckets: 512                      # HashedWheelTimer buckets
    bridge-mode: fire-via-eventrouter # Timer callback → EventRouter → SBB

  activity-context:
    pool-type: in-memory              # In-Memory (R&D only!)
    max-concurrent: 20000             # Max ACs in memory
    reap-interval-seconds: 60         # GC interval
    idle-ttl-seconds: 3600            # TTL for unreferenced ACs

  recovery:
    enabled: true
    snapshot-on-remove: true
    store: in-memory
    max-snapshots: 10000

# ═══════════════════════════════════════════════════════════════
# Embedded RA Configuration
# ═══════════════════════════════════════════════════════════════
ussd:
  demo:
    http:
      enabled: true
      port: 8081
      acceptor-threads: 2
      worker-threads: 8
    grpc:
      enabled: true
      host: 127.0.0.1
      port: 9090
      use-in-memory: false            # true = bypass network (for tests)
      latency-ms: 10                  # Simulated latency
```

#### application.properties (alternative)

```properties
# ── Spring Boot + micro-jainslee USSD demo ──
spring.application.name=ussd-rd-demo
spring.main.web-application-type=none

microjainslee.event-router.buffer-size=2048
microjainslee.event-router.prefer-virtual-threads=true
microjainslee.sbb-pool.min=16
microjainslee.sbb-pool.max=4096
microjainslee.sbb-pool.per-virtual-thread=true

ussd.demo.http.port=8081
ussd.demo.grpc.host=127.0.0.1
ussd.demo.grpc.port=9090
ussd.demo.grpc.use-in-memory=false
ussd.demo.grpc.latency-ms=10
```

#### Auto-Configured Beans

| Bean | Class | Vai trò |
|------|-------|---------|
| `microSleeConfiguration` | `MicroSleeConfiguration` | Đọc cấu hình từ application.yml |
| `microSleeContainer` | `MicroSleeContainer` | Bootstrap SLEE container |
| `eventRouter` | `EventRouter` (in-memory) | Điều phối event |
| `activityContextPool` | `InMemoryActivityContextNamingFacility` | Quản lý AC |
| `timerPort` | `TimerPort` | SLEE Timer facility |
| `sbbEntityPool` | `VirtualThreadSbbEntityPool` | Quản lý SBB entities (VT) |

---
### 2.4 Java 25 + ZGC Tuning

#### JVM Options cho R&D / R&D JVM Options

```bash
#!/bin/bash
# ── micro-jainslee R&D JVM Options (Java 25) ──

export JAVA_HOME=/usr/lib/jvm/jdk-25

JAVA_OPTS="\
  # ============================================================
  # Java 25 — Virtual Threads (GA, no --enable-preview needed)
  # ============================================================

  # ============================================================
  # HEAP — R&D: nhẹ hơn production
  # ============================================================
  -server
  -Xms512m
  -Xmx4g
  -Xss256k

  # ============================================================
  # GARBAGE COLLECTION — ZGC (Java 25, low-latency)
  # ============================================================
  -XX:+UseZGC
  -XX:+ZGenerational                          # Generational ZGC (Java 25)
  -XX:SoftMaxHeapSize=3g                      # Soft limit 3GB
  -XX:ZUncommitDelay=60                       # Uncommit sau 60s idle
  -XX:+UseStringDeduplication                 # Dedup strings
  -XX:+DisableExplicitGC
  -Xlog:gc=info:file=logs/gc.log::filecount=5,filesize=20M
  -XX:+HeapDumpOnOutOfMemoryError
  -XX:HeapDumpPath=logs/heapdump.hprof

  # ============================================================
  # VIRTUAL THREADS Tuning
  # ============================================================
  # Default carrier threads = Runtime.getRuntime().availableProcessors()
  # Override if needed:
  # -Djdk.virtualThreadScheduler.parallelism=8
  # -Djdk.virtualThreadScheduler.maxPoolSize=256

  # ============================================================
  # MONITORING
  # ============================================================
  -Dcom.sun.management.jmxremote
  -Dcom.sun.management.jmxremote.port=9998
  -Dcom.sun.management.jmxremote.authenticate=false
  -Dcom.sun.management.jmxremote.ssl=false

  # ============================================================
  # NETWORK & SYSTEM
  # ============================================================
  -Djava.net.preferIPv4Stack=true
  -Djava.awt.headless=true
  -Dfile.encoding=UTF-8
  -Duser.timezone=UTC
"
```

#### G1GC vs ZGC Comparison (R&D context)

| Metric | G1GC (Production) | ZGC (R&D, Java 25) |
|--------|-------------------|---------------------|
| **Max pause** | ~100ms (target) | < 1ms |
| **Heap range** | 4GB - 64GB | 8MB - 16TB |
| **Throughput** | ~98% | ~95% |
| **CPU overhead** | ~5% | ~10-15% (concurrent work) |
| **Generational** | Yes (default) | Yes (Java 25) |
| **Production-ready** | ✅ Since JDK 8 | ✅ Since JDK 15 |

> **Khuyến nghị:** ZGC cho R&D vì startup nhanh, pause cực thấp, phù hợp rapid prototyping. G1GC cho production vì stability trên JDK 8.

---

### 2.5 Embedded RAs Setup

#### gRPC Client RA

```java
// Module: vendor-ras/ra-grpc-client
// Config: application.yml (ussd.demo.grpc.*)

@ResourceAdaptor(
    id = "GrpcMenuUpstreamRA",
    vendor = "com.example.ussddemo",
    version = "1.0"
)
public class GrpcMenuUpstreamAdapter implements ResourceAdaptor {

    private ResourceAdaptorContext raContext;
    private ManagedChannel grpcChannel;

    @Override
    public void raActive() {
        // host, port, use-in-memory from config
        // use-in-memory=true → DirectExecutor (bypass network for tests)
    }

    // RA Interface cho SBB gọi:
    public void sendMenuRequest(String sessionId, String msisdn,
                                 String ussdString,
                                 StreamObserver<MenuResponse> observer) {
        MenuRequest req = MenuRequest.newBuilder()
            .setSessionId(sessionId)
            .setMsisdn(msisdn)
            .setUssdString(ussdString)
            .build();
        asyncStub.getMenu(req, observer);
    }

    @Override public void raInactive() {
        if (grpcChannel != null) grpcChannel.shutdownNow();
    }
}
```

#### HTTP Ingress RA

```java
// Module: vendor-ras/ra-http-ingress
// Config: application.yml (ussd.demo.http.*)

@ResourceAdaptor(
    id = "HttpIngressRA",
    vendor = "com.example.ussddemo",
    version = "1.0"
)
public class HttpIngressResourceAdaptor implements ResourceAdaptor {

    private com.sun.net.httpserver.HttpServer httpServer;

    @Override
    public void raActive() {
        int port = getPortFromConfig();
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/ussd", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes());
            fireHttpUssdBeginEvent(body);
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        });
        httpServer.setExecutor(Executors.newFixedThreadPool(8));
        httpServer.start();
    }

    private void fireHttpUssdBeginEvent(String body) {
        // Fire event vào SLEE EventRouter
        raContext.getSleeEndpoint().fireEvent(
            activityHandle, eventTypeId, event, null, null, EventFlags.NO_FLAGS);
    }

    @Override public void raInactive() { httpServer.stop(0); }
}
```

#### SS7 USSD Ingress SBB (Simulator)

```java
// Module: example/ussdgw-simulator

@Sbb(id = "Ss7UssdIngressSBB", service = "UssdGatewayDemo")
public abstract class Ss7UssdIngressSbb implements Sbb {

    @EventHandler
    public void onSs7UssdBegin(Ss7UssdBeginEvent event,
                                ActivityContextInterface aci) {
        // Mô phỏng USSD request từ SS7 network
        // Forward đến GrpcClientSbb để gọi upstream menu service
        fireGrpcMenuRequest(aci, event);
    }
}
```

---
### 2.6 Docker Considerations

> ⚠️ **NHẮC LẠI:** Docker setup dưới đây là cho **R&D MÔI TRƯỜNG PHÁT TRIỂN**. Không dùng cho production!

#### Dockerfile (R&D Only)

```dockerfile
# ═══════════════════════════════════════════════════════════════
# Dockerfile — micro-jainslee R&D DEVELOPMENT ENVIRONMENT
# ⚠️  R&D ONLY — NOT FOR PRODUCTION
# ═══════════════════════════════════════════════════════════════

FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY src/ src/
RUN ./mvnw clean package -DskipTests -pl example/example-spring

FROM eclipse-temurin:25-jre-alpine

LABEL com.microjainslee.purpose="R&D" \
      com.microjainslee.warning="NOT_FOR_PRODUCTION"

WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
USER app:app

COPY --from=builder /app/example/example-spring/target/*.jar app.jar

ENV JAVA_OPTS="\
  -server \
  -Xms256m -Xmx2g \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -XX:+DisableExplicitGC \
  -Djava.net.preferIPv4Stack=true \
  -Duser.timezone=UTC \
"

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8081/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

#### Docker Compose (R&D Local Dev)

```yaml
# ═══════════════════════════════════════════════════════════════
# docker-compose.yml — micro-jainslee R&D Development Stack
# ⚠️  R&D ONLY — NOT FOR PRODUCTION
# ═══════════════════════════════════════════════════════════════

version: "3.8"

services:

  # ── micro-jainslee Spring Boot App (R&D SLEE Container) ──
  ussd-rd:
    build:
      context: .
      dockerfile: Dockerfile
    image: microjainslee-ussd-rd:latest
    container_name: microjainslee-rd
    ports:
      - "8081:8081"   # HTTP Ingress RA
      - "9998:9998"   # JMX (local monitoring)
    environment:
      - JAVA_OPTS=-Xms256m -Xmx2g -XX:+UseZGC -XX:+ZGenerational
      - ussd.demo.grpc.host=grpc-simulator
      - ussd.demo.grpc.port=9090
      - ussd.demo.grpc.use-in-memory=false
    networks:
      - rd-net
    depends_on:
      grpc-simulator:
        condition: service_healthy
    restart: unless-stopped

  # ── gRPC Simulator (fake upstream menu service) ──
  grpc-simulator:
    build:
      context: example/grpc-simulator
      dockerfile: Dockerfile
    image: microjainslee-grpc-sim:latest
    container_name: grpc-simulator
    ports:
      - "9090:9090"
    environment:
      - GRPC_PORT=9090
      - SIMULATED_LATENCY_MS=10
    networks:
      - rd-net
    healthcheck:
      test: ["CMD", "grpc_health_probe", "-addr=:9090"]
      interval: 10s
      timeout: 3s
      retries: 3

  # ── SS7 USSD GW Simulator (optional — load test) ──
  ussdgw-simulator:
    build:
      context: example/ussdgw-simulator
      dockerfile: Dockerfile
    image: microjainslee-ussdgw-sim:latest
    container_name: ussdgw-simulator
    environment:
      - TARGET_HOST=ussd-rd
      - TARGET_PORT=8081
      - SIMULATED_TPS=10
    networks:
      - rd-net
    profiles:
      - simulator
    depends_on:
      - ussd-rd

networks:
  rd-net:
    driver: bridge
    name: microjainslee-rd-net
```

#### Docker Quick Start Commands

```bash
# 1. Build và chạy
docker compose up -d

# 2. Xem logs
docker compose logs -f ussd-rd

# 3. Test HTTP Ingress RA
curl -X POST http://localhost:8081/ussd \
  -H 'Content-Type: application/json' \
  -d '{"msisdn":"251911234567","ussdString":"*123#"}'

# 4. Test với simulator
docker compose --profile simulator up -d

# 5. Dừng
docker compose down

# 6. Cleanup
docker compose down -v --rmi local
```

---

### 2.7 R&D Troubleshooting

| # | Triệu chứng | Nguyên nhân có thể | Fix |
|---|-------------|-------------------|-----|
| **1** | **Virtual threads không hoạt động** | JDK < 21? | Java 25+: VT là GA. Java 21-24: thêm `--enable-preview` |
| **2** | **SBB entity không nhận event** | SBB chưa registered? Index scan sai package? | Kiểm tra `scan-packages` trong application.yml. Xác nhận `sbb-index.properties` tồn tại |
| **3** | **Timer không fire** | HashedWheelTimer chưa start? Bridge mode sai? | `bridge-mode` phải là `fire-via-eventrouter` |
| **4** | **OOM với 100K SBBs** | Heap quá nhỏ cho parked VTs | Mỗi parked VT ~300 bytes → 100K = ~30MB. Tăng `-Xmx` nếu CMP fields lớn |
| **5** | **gRPC connection refused** | gRPC simulator chưa chạy? Port sai? | Dùng `use-in-memory=true` để test bypass network |
| **6** | **Spring Boot không auto-configure** | Thiếu dependency | Thêm `jainslee-adapter-springboot` vào pom.xml |
| **7** | **SBB index không tìm thấy SBBs** | Không có `@Sbb` annotation? | Xác nhận SBB class có `@Sbb` annotation và nằm trong scan package |
| **8** | **"Cannot find EventTypeID"** | Event types chưa được deploy | RA phải lookup EventTypeID trong `raActive()` |

---

## Phụ lục A / Appendix A: Quick Reference — Production vs R&D

| Dimension | Production (RestComm SLEE v8) | R&D (micro-jainslee) |
|-----------|------------------------------|-----------------------|
| **Runtime** | WildFly 10.0.0.Final | Embedded / Spring Boot / Quarkus |
| **JDK** | Zulu 8 (JDK 8) | JDK 25 |
| **GC** | G1GC (pause ~100ms) | ZGC Generational (pause < 1ms) |
| **EventRouter** | LMAX Disruptor (262K ring) | In-Memory (EntitySlot queue) |
| **SBB Pool** | Apache Commons Pool (min 5K, max 100K) | VirtualThreadSbbEntityPool (max ~100K VTs) |
| **Timer** | FaultTolerantScheduler (HA) | jSS7 HashedWheelTimer (10ms tick) |
| **Persistence** | Infinispan (distributed) | In-Memory (HashMap) |
| **Cluster** | ✅ JGroups + Infinispan HA | ❌ Single node only |
| **Transactions** | JTA (Narayana) | Simple tx context |
| **TCK** | ✅ TCK compliant | ❌ Not TCK compliant |
| **JMX** | ✅ JSR-77 MBeans | ❌ No JMX |
| **Boot time** | 30-60 seconds | < 1 second |
| **Memory baseline** | ~500 MB | ~50 MB |
| **Throughput** | 100K+ events/s | ~10K-50K events/s |
| **Deploy** | SLEE Deployable Unit (.jar via JMX) | Classpath / Spring Bean |
| **Production** | ✅ YES | ⛔ NO (R&D ONLY) |

---

## Phụ lục B / Appendix B: File Paths Reference

```
Production Deployment:
  /opt/restcomm/restcomm-ussd-7.3.1-SNAPSHOT/
  ├── wildfly-10.0.0.Final/
  │   ├── bin/standalone.conf          ← JVM options
  │   ├── standalone/
  │   │   ├── configuration/
  │   │   │   ├── standalone.xml       ← SLEE + Infinispan + JGroups config
  │   │   │   └── logging.properties   ← Logging config
  │   │   ├── deployments/             ← SLEE DUs (.jar)
  │   │   │   ├── services-DU.jar
  │   │   │   ├── restcomm-slee-ra-map-du-9.5.0.jar
  │   │   │   └── ussdhttpdemo.war
  │   │   ├── data/
  │   │   │   ├── TcapStack_management.xml
  │   │   │   ├── UssdManagement_ussdproperties.xml
  │   │   │   └── SccpStack_management2.xml
  │   │   └── log/
  │   │       ├── server.log
  │   │       └── gc.log

R&D Development:
  jain-slee/jain-slee/
  ├── example/example-spring/src/main/resources/
  │   └── application.yml              ← micro-jainslee config
  ├── example/example-embedded-j25/    ← Embedded Java 25 example
  ├── vendor-ras/ra-grpc-client/       ← gRPC RA
  ├── vendor-ras/ra-http-ingress/      ← HTTP RA
  └── jainslee-adapter/adapter-springboot/  ← Spring Boot starter
```

---

## Phụ lục C / Appendix C: Port Map

| Port | Service | Stack | Ghi chú |
|------|---------|-------|---------|
| **8080** | HTTP / Jolokia | Production | Health check: `/jolokia/version` |
| **9090** | BPF collector metrics | Production | `/metrics`, `/healthz` |
| **9990** | WildFly management | Production | Management API |
| **9999** | JMX remote | Production | JConsole / VisualVM |
| **7800** | JGroups cluster | Production | Inter-node communication |
| **8081** | HTTP Ingress RA | R&D | USSD test endpoint |
| **9090** | gRPC upstream | R&D | gRPC menu service |
| **9998** | JMX remote | R&D | Local monitoring |

---

> **Remember / Ghi nhớ:**
> - **Production = RestComm JAIN-SLEE v8 + WildFly 10 + Zulu JDK 8**
> - **R&D = micro-jainslee + Spring Boot + Java 25 + ZGC**
> - **micro-jainslee TUYỆT ĐỐI không dùng cho production**
> - **Timer callback LUÔN qua EventRouter — không execute trực tiếp trên wheel thread**
> - **SBB Remove LUÔN cancel timer — tránh timer leak**
> - **Cluster: SYNC mode cho Infinispan — đảm bảo data an toàn khi failover**
