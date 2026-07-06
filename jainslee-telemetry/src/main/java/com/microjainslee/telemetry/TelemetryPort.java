package com.microjainslee.telemetry;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Self-healing telemetry engine API — passive collection, Prometheus export,
 * auto-reconfiguration, and extensible app-defined custom metrics.
 *
 * <h3>App-defined custom telemetry</h3>
 * Every app domain (SS7, Diameter, SIP, SMS, USSD, …) can register its own
 * counters and gauges at runtime via {@link #customCounter(String, String...)}
 * and {@link #customGauge(String, Supplier&lt;Number&gt;, String...)}.
 * They automatically appear in {@code snapshot().customMetrics} and
 * Prometheus scrape output — zero extra wiring needed.
 *
 * <pre>{@code
 * // In an SS7 bootstrap or SBB:
 * TelemetryPort telemetry = container.getTelemetryPort();
 *
 * telemetry.customCounter("ss7_tcap_total",           "opcode", "begin");
 * telemetry.customCounter("ss7_map_messages",          "opcode", "atsi");
 * telemetry.customCounter("ss7_map_failures",          "cause",  "absentSubscriber");
 * telemetry.customGauge("ss7_map_stale_dialogues",
 *     () -> staleCounter.get(), "host", "HOST-A");
 * }</pre>
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

    // ── App-defined custom metrics (extensible) ──

    /**
     * Increment-only counter with optional tag key-value pairs.
     * <p>Usage: {@code telemetry.customCounter("ss7_tcap_total", "opcode", "begin").increment();}
     * <p>Automatically exported in Prometheus scrape and custom snapshot.
     */
    Counter customCounter(String name, String... tagPairs);

    /**
     * Sampled gauge backed by a {@link Supplier&lt;Number&gt;}.
     * <p>The supplier is polled at snapshot/scrape time — keep it trivial
     * (e.g. {@code AtomicLong::get}) to stay zero-CPU.
     * <p>Automatically exported in Prometheus scrape and custom snapshot.
     */
    Gauge customGauge(String name, Supplier<Number> supplier, String... tagPairs);

    /**
     * Lightweight increment-only counter contract.
     */
    interface Counter {
        void increment();
        void increment(long n);
        long count();
    }

    /**
     * Lightweight sampled gauge contract.
     */
    interface Gauge {
        Number value();
    }

    /**
     * Snapshot of a single custom metric.
     */
    record CustomMetric(String name, Map<String, String> tags, long counterValue, Number gaugeValue, boolean isGauge) {}

    // ── Prometheus ──

    /** OpenMetrics text format (includes custom metrics automatically). */
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
            boolean autoReconfigEnabled,
            List<CustomMetric> customMetrics
    ) {}

    TelemetrySnapshot snapshot();
}
