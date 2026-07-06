package com.microjainslee.telemetry;

import java.util.List;
import java.util.Map;

/**
 * Self-healing telemetry engine API — passive collection, Prometheus export,
 * and auto-reconfiguration.
 *
 * <p>All collectors are passive (callback-based) with zero polling loops.
 * Counters use {@link java.util.concurrent.atomic.AtomicLong} /
 * {@link java.util.concurrent.atomic.LongAdder} (lock-free).
 * Ring buffers use {@link java.util.concurrent.atomic.AtomicReferenceArray}
 * for history snapshots.
 */
public interface TelemetryPort {

    // ── Collectors (read-only for apps) ──

    SbbCollector sbbCollector();
    RaCollector raCollector();
    ErrorCollector errorCollector();
    ResourceMonitor resourceMonitor();
    SpunkDetector spunkDetector();
    StaleDetector staleDetector();
    AlarmEngine alarmEngine();

    // ── Auto-reconfig (admin only) ──

    AutoReconfigEngine autoReconfig();
    boolean isAutoReconfigEnabled();
    void setAutoReconfigEnabled(boolean enabled);

    // ── Prometheus ──

    /** OpenMetrics text format. */
    String scrape();

    // ── Snapshot ──

    record TelemetrySnapshot(
            List<SbbCollector.PerType> sbbs,
            List<RaCollector.RaStats> ras,
            ResourceMonitor.ResourceSnapshot resources,
            List<ErrorCollector.ErrorEntry> recentErrors,
            List<SpunkDetector.SpunkAlert> spunks,
            List<StaleDetector.StaleAlert> stales,
            List<AlarmEngine.Alarm> activeAlarms,
            boolean autoReconfigEnabled
    ) {}

    TelemetrySnapshot snapshot();
}
