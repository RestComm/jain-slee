/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.autonomous;

import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.offheap.OffHeapSlotArena;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Self-protection brain for run-forever deployments (Quarkus/GraalVM
 * native, no restarts, no JVM-flag changes possible).
 *
 * <h2>Zero-CPU, zero-dependency design</h2>
 * The guardian owns <b>no thread</b> and touches <b>no</b>
 * {@code java.lang.management} / JMX bean (native-image clean). Heap pressure
 * is read on demand from {@link Runtime} and evaluated only when something
 * pokes {@link #checkNow()} — the telemetry scrape path and the health
 * evaluator do exactly that on their existing cadence. Idle system → zero CPU,
 * zero allocation, no background wake-ups.
 *
 * <h2>Escalation ladder</h2>
 * <pre>
 *   heap ≥ elevated% → ELEVATED  : relieve participants (trim caches,
 *                                  expire dedup/OOB entries, idle dialogs)
 *   heap ≥ critical% → CRITICAL  : + compact off-heap arenas
 *                                  + guarded System.gc() (rate-limited)
 *   heap ≥ emergency% → EMERGENCY: + application emergency hook
 *                                  (shed load / refuse new activities)
 * </pre>
 * Every action is cooldown-protected so a saw-toothing heap cannot turn
 * the guardian itself into a CPU or GC storm.
 */
public final class AutonomousGuardian implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AutonomousGuardian.class);

    /** Registered relief participants consulted under pressure (thread-safe list). */
    private final List<MemoryReliefParticipant> participants = new CopyOnWriteArrayList<>();

    /** Per-action-key cooldown timestamps (key → last-fire epoch-millis). */
    private final Map<String, AtomicLong> cooldowns = new ConcurrentHashMap<>();

    /** Heap-usage ratio (0.0–1.0) at which ELEVATED actions begin. Default 0.75. */
    private volatile double elevatedPercent = 0.75;

    /** Heap-usage ratio (0.0–1.0) at which CRITICAL actions begin. Default 0.88. */
    private volatile double criticalPercent = 0.88;

    /** Heap-usage ratio (0.0–1.0) at which EMERGENCY actions begin. Default 0.96. */
    private volatile double emergencyPercent = 0.96;

    /** Minimum interval (ms) between relief-run invocations. Default 60s. */
    private volatile long actionCooldownMillis = 60_000L;

    /** Minimum interval (ms) between guarded System.gc() calls. Default 5 min. */
    private volatile long gcCooldownMillis = 300_000L;

    /** Application hook invoked only at EMERGENCY level (load-shedding, alarms). */
    private volatile Consumer<PressureLevel> emergencyHook;

    /**
     * Optional telemetry bridge invoked on every pressure evaluation
     * (e.g. {@code AutoReconfigEngine::evaluateNow}).
     */
    private volatile Runnable pressureEvaluateHook;

    /** Whether the guardian is armed (accepting {@link #checkNow()} evaluations). */
    private volatile boolean started;

    /** Monotonic counter of relief-run cycles completed. */
    private final AtomicLong reliefRuns = new AtomicLong();

    /** Last computed pressure level (volatile snapshot for telemetry reads). */
    private volatile PressureLevel lastLevel = PressureLevel.NORMAL;

    public AutonomousGuardian() {
    }

    // ── configuration ───────────────────────────────────────────────

    public AutonomousGuardian watermarks(double elevated, double critical, double emergency) {
        if (!(elevated < critical && critical < emergency && emergency <= 1.0)) {
            throw new IllegalArgumentException("watermarks must be ascending and ≤ 1.0");
        }
        this.elevatedPercent = elevated;
        this.criticalPercent = critical;
        this.emergencyPercent = emergency;
        return this;
    }

    public AutonomousGuardian actionCooldownMillis(long millis) {
        this.actionCooldownMillis = millis;
        return this;
    }

    public AutonomousGuardian gcCooldownMillis(long millis) {
        this.gcCooldownMillis = millis;
        return this;
    }

    /** Application load-shedding hook — invoked at EMERGENCY only. */
    public AutonomousGuardian onEmergency(Consumer<PressureLevel> hook) {
        this.emergencyHook = hook;
        return this;
    }

    /** Optional bridge into telemetry (e.g. {@code autoReconfig::evaluateNow}). */
    public AutonomousGuardian onPressureEvaluate(Runnable hook) {
        this.pressureEvaluateHook = hook;
        return this;
    }

    public void register(MemoryReliefParticipant participant) {
        participants.add(participant);
        LOG.info("[autonomous] relief participant registered: {}", participant.name());
    }

    public List<MemoryReliefParticipant> participants() {
        return List.copyOf(participants);
    }

    /**
     * Register the standard participants for a container: IES result
     * cache, dedup window, out-of-order buffer, and off-heap arena
     * compaction. Call after the container (and its RAs) are wired.
     */
    public AutonomousGuardian attach(MicroSleeContainer container) {
        register(new MemoryReliefParticipant() {
            @Override public String name() { return "dedup-window"; }
            @Override public long relieve(PressureLevel level) {
                var window = container.getDedupWindow();
                return window == null ? 0 : window.evictExpired(System.currentTimeMillis());
            }
        });
        register(new MemoryReliefParticipant() {
            @Override public String name() { return "out-of-order-buffer"; }
            @Override public long relieve(PressureLevel level) {
                var buffer = container.getOutOfOrderBuffer();
                return buffer == null ? 0 : buffer.evictExpired();
            }
        });
        register(new MemoryReliefParticipant() {
            @Override public String name() { return "ies-result-cache"; }
            @Override public long relieve(PressureLevel level) {
                if (level.ordinal() < PressureLevel.CRITICAL.ordinal()) {
                    return 0; // the cache is cheap to keep — only drop when critical
                }
                Object dispatcher = container.getEventRouter().getInitialEventSelectorDispatcher();
                if (dispatcher instanceof com.microjainslee.core.ies.InitialEventSelectorDispatcher ies) {
                    ies.clearIesResultCache();
                    return 1;
                }
                return 0;
            }
        });
        register(new MemoryReliefParticipant() {
            @Override public String name() { return "offheap-arena-compaction"; }
            @Override public long relieve(PressureLevel level) {
                if (level.ordinal() < PressureLevel.CRITICAL.ordinal()) {
                    return 0;
                }
                long moved = 0;
                for (OffHeapSlotArena arena : container.getSbbEntityPool().getOffHeapArenas()) {
                    if (arena.fragmentationRatio() > 0.25) {
                        moved += arena.compact();
                    }
                }
                return moved;
            }
        });
        return this;
    }

    // ── lifecycle ───────────────────────────────────────────────────

    /**
     * Arm the guardian. Creates no thread and registers no JVM/JMX listener —
     * evaluation happens when {@link #checkNow()} is poked (telemetry scrape,
     * health evaluator, or an app on its own cadence).
     */
    public synchronized void start() {
        if (started) {
            return;
        }
        started = true;
        LOG.info("[autonomous] guardian armed (watermarks {}/{}/{}, reactive, no threads)",
                elevatedPercent, criticalPercent, emergencyPercent);
    }

    public synchronized void stop() {
        started = false;
    }

    @Override
    public void close() {
        stop();
    }

    public boolean isStarted() { return started; }
    public PressureLevel lastLevel() { return lastLevel; }
    public long reliefRunCount() { return reliefRuns.get(); }

    // ── evaluation ──────────────────────────────────────────────────

    /** Evaluate current heap pressure and act. Safe from any thread. */
    public PressureLevel checkNow() {
        return evaluate(currentHeapUsageRatio());
    }

    /** Test hook — run the ladder for a given usage ratio (0.0–1.0). */
    PressureLevel evaluate(double usageRatio) {
        PressureLevel level = levelFor(usageRatio);
        lastLevel = level;
        if (level == PressureLevel.NORMAL) {
            return level;
        }
        LOG.warn("[autonomous] heap pressure {}% → {}",
                Math.round(usageRatio * 100), level);
        Runnable evaluateHook = this.pressureEvaluateHook;
        if (evaluateHook != null) {
            runQuietly("pressure-evaluate-hook", evaluateHook);
        }
        if (cooldownPermits("relief", actionCooldownMillis)) {
            runParticipants(level);
        }
        if (level.ordinal() >= PressureLevel.CRITICAL.ordinal()
                && cooldownPermits("gc", gcCooldownMillis)) {
            LOG.warn("[autonomous] guarded GC (level={})", level);
            System.gc();
        }
        if (level == PressureLevel.EMERGENCY) {
            Consumer<PressureLevel> hook = this.emergencyHook;
            if (hook != null && cooldownPermits("emergency", actionCooldownMillis)) {
                runQuietly("emergency-hook", () -> hook.accept(level));
            }
        }
        return level;
    }

    private void runParticipants(PressureLevel level) {
        reliefRuns.incrementAndGet();
        for (MemoryReliefParticipant participant : participants) {
            try {
                long released = participant.relieve(level);
                if (released > 0) {
                    LOG.info("[autonomous] {} released {} at {}",
                            participant.name(), released, level);
                }
            } catch (RuntimeException e) {
                LOG.warn("[autonomous] participant {} failed: {}",
                        participant.name(), e.getMessage());
            }
        }
    }

    private PressureLevel levelFor(double ratio) {
        if (ratio >= emergencyPercent) return PressureLevel.EMERGENCY;
        if (ratio >= criticalPercent) return PressureLevel.CRITICAL;
        if (ratio >= elevatedPercent) return PressureLevel.ELEVATED;
        return PressureLevel.NORMAL;
    }

    /**
     * Current heap-usage ratio (0.0–1.0) read from {@link Runtime} — no
     * management bean. {@code used = total − free}; the ceiling is
     * {@code maxMemory()} when the JVM defines one, else the current total.
     */
    private double currentHeapUsageRatio() {
        Runtime rt = Runtime.getRuntime();
        long used = rt.totalMemory() - rt.freeMemory();
        long max = rt.maxMemory();
        if (max == Long.MAX_VALUE || max <= 0) {
            max = rt.totalMemory();
        }
        return max > 0 ? (double) used / max : 0.0;
    }

    private boolean cooldownPermits(String key, long cooldownMillis) {
        AtomicLong last = cooldowns.computeIfAbsent(key, k -> new AtomicLong());
        long now = System.currentTimeMillis();
        long prev = last.get();
        return now - prev >= cooldownMillis && last.compareAndSet(prev, now);
    }

    private static void runQuietly(String what, Runnable r) {
        try {
            r.run();
        } catch (RuntimeException e) {
            LOG.warn("[autonomous] {} failed: {}", what, e.getMessage());
        }
    }
}
