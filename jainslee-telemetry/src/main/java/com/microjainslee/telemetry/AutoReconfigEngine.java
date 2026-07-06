package com.microjainslee.telemetry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microjainslee.core.MicroSleeContainer;

public final class AutoReconfigEngine {

    private static final Logger LOG = LogManager.getLogger(AutoReconfigEngine.class);

    private final SbbCollector sbbCollector;
    private final ErrorCollector errorCollector;
    private final ResourceMonitor resourceMonitor;
    private final StaleDetector staleDetector;
    private final AlarmEngine alarmEngine;
    private final MicroSleeContainer container;

    private final ConcurrentHashMap<String, AtomicLong> cooldowns
            = new ConcurrentHashMap<>();
    private static final long COOLDOWN_MS = 120_000L;

    private ScheduledExecutorService scheduler;
    private volatile boolean started;

    public AutoReconfigEngine(SbbCollector sbbCollector,
                               ErrorCollector errorCollector,
                               ResourceMonitor resourceMonitor,
                               StaleDetector staleDetector,
                               AlarmEngine alarmEngine,
                               MicroSleeContainer container) {
        this.sbbCollector = sbbCollector;
        this.errorCollector = errorCollector;
        this.resourceMonitor = resourceMonitor;
        this.staleDetector = staleDetector;
        this.alarmEngine = alarmEngine;
        this.container = container;
    }

    public void start(long interval, TimeUnit unit) {
        if (started) return;
        started = true;
        ThreadFactory tf = Thread.ofVirtual().name("telemetry-autoreconf").factory();
        scheduler = Executors.newSingleThreadScheduledExecutor(tf);
        scheduler.scheduleAtFixedRate(this::evaluate, interval, interval, unit);
        LOG.info("AutoReconfigEngine started (interval={} {})", interval, unit);
    }

    public void stop() {
        if (scheduler != null) { scheduler.shutdown(); scheduler = null; }
        started = false;
    }

    public boolean isStarted() { return started; }

    void evaluate() {
        try {
            checkMemoryPressure();
            checkCpuPressure();
            checkSbbLoadSpike();
            checkErrorStorm();
            checkStaleEntities();
        } catch (Exception e) {
            LOG.warn("AutoReconfigEngine evaluate() error: {}", e.getMessage(), e);
        }
    }

    private void checkMemoryPressure() {
        var snap = resourceMonitor.snapshot();
        if (snap == null) return;
        if (snap.heapUsagePercent() > 95) {
            if (!cooldownPermits("emergency_gc")) return;
            alarmEngine.fire(AlarmEngine.TelemetryAlarmLevel.CRITICAL,
                    "AutoReconfig", "heap>95%, emergency cleanup",
                    Map.of("heapUsedMb", snap.heapUsedMb(),
                           "heapMaxMb", snap.heapMaxMb()));
            System.gc();
            LOG.warn("AutoReconfig: heap>95% — emergency GC triggered");
        } else if (snap.heapUsagePercent() > 85) {
            if (!cooldownPermits("reduce_pool_mem")) return;
            int currentMax = container.getConfiguration().getSbbPoolMax();
            int newMax = Math.max(
                    container.getConfiguration().getSbbPoolMin(),
                    currentMax / 2);
            alarmEngine.fire(AlarmEngine.TelemetryAlarmLevel.WARNING,
                    "AutoReconfig", "heap>85%, reducing SBB pool",
                    Map.of("oldMax", currentMax, "newMax", newMax,
                           "heapUsagePercent", snap.heapUsagePercent()));
            LOG.warn("AutoReconfig: heap>85% — pool reduced to {}", newMax);
        }
    }

    private void checkCpuPressure() {
        var snap = resourceMonitor.snapshot();
        if (snap == null || snap.cpuLoad() < 0) return;
        if (snap.cpuLoad() > 80) {
            if (!cooldownPermits("cpu_pressure")) return;
            alarmEngine.fire(AlarmEngine.TelemetryAlarmLevel.WARNING,
                    "AutoReconfig", "CPU>80%",
                    Map.of("cpuLoad", snap.cpuLoad()));
            LOG.warn("AutoReconfig: CPU>80%");
        }
    }

    private void checkSbbLoadSpike() {
        for (var sbb : sbbCollector.perType()) {
            double baseline = sbbCollector.getBaselineEps(sbb.sbbType());
            if (baseline <= 0) {
                sbbCollector.setBaselineEps(sbb.sbbType(), sbb.eps());
                continue;
            }
            if (sbb.eps() > baseline * 3) {
                if (!cooldownPermits("load_spike_" + sbb.sbbType())) continue;
                int currentMax = container.getConfiguration().getSbbPoolMax();
                alarmEngine.fire(AlarmEngine.TelemetryAlarmLevel.INFO,
                        "AutoReconfig",
                        sbb.sbbType() + " load spike (eps=" + sbb.eps()
                                + " > baseline " + baseline + "x3)",
                        Map.of("sbbType", sbb.sbbType(), "eps", sbb.eps(),
                               "baseline", baseline, "oldMax", currentMax));
                LOG.info("AutoReconfig: {} load spike", sbb.sbbType());
            }
        }
    }

    private void checkErrorStorm() {
        for (var e : errorCollector.errorRateByType().entrySet()) {
            if (e.getValue() > 100) {
                if (!cooldownPermits("error_storm_" + e.getKey())) continue;
                alarmEngine.fire(AlarmEngine.TelemetryAlarmLevel.CRITICAL,
                        "AutoReconfig",
                        "Error storm: " + e.getKey() + " (" + e.getValue()
                                + " errors/min)",
                        Map.of("exceptionType", e.getKey(),
                               "count", e.getValue()));
                LOG.error("AutoReconfig: Error storm — {} ({} errors/min)",
                        e.getKey(), e.getValue());
            }
        }
    }

    private void checkStaleEntities() {
        var staleList = staleDetector.detectStale(5 * 60_000L, 30 * 60_000L);
        long leaked = staleList.stream().filter(s -> s.leaked()).count();
        if (leaked > 0) {
            if (!cooldownPermits("stale_leaked")) return;
            alarmEngine.fire(AlarmEngine.TelemetryAlarmLevel.CRITICAL,
                    "AutoReconfig", "Leaked entities detected: " + leaked,
                    Map.of("leakedCount", leaked));
            LOG.warn("AutoReconfig: {} leaked entities detected", leaked);
        }
        sbbCollector.markStaleEntities(staleList.size() - leaked);
        sbbCollector.markLeakedEntities(leaked);
    }

    private boolean cooldownPermits(String actionKey) {
        AtomicLong last = cooldowns.computeIfAbsent(actionKey,
                k -> new AtomicLong(0));
        long now = System.currentTimeMillis();
        long prev = last.get();
        if (now - prev < COOLDOWN_MS) return false;
        return last.compareAndSet(prev, now);
    }
}
