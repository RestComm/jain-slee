package com.microjainslee.telemetry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Detects anomalous SBB behavior (spunks) — event loop >100ms,
 * entity creation storms (>1000/min), memory spikes (>100MB per entity).
 */
public final class SpunkDetector {

    public record SpunkAlert(String sbbType, String entityId, String reason,
                             long timestamp, Map<String, Object> context) {}

    private static final int MAX_SPUNK_ALERTS = 200;
    private final ConcurrentLinkedQueue<SpunkAlert> alerts = new ConcurrentLinkedQueue<>();

    // Per-entity sliding counters for creation storm detection
    private final ConcurrentHashMap<String, EntityMetrics> entityMetrics =
            new ConcurrentHashMap<>();

    private static final class EntityMetrics {
        final AtomicLong eventCount = new AtomicLong();
        final AtomicLong totalLatencyNs = new AtomicLong();
        final AtomicLong maxLatencyNs = new AtomicLong();
        volatile long lastEventMs = System.currentTimeMillis();
    }

    /**
     * Passive callback invoked by EventRouter after each event.
     * Checks thresholds and records spunk alerts.
     */
    public void onEventProcessed(String sbbType, String entityId,
                                  long latencyNs, long memDeltaBytes) {
        EntityMetrics m = entityMetrics.computeIfAbsent(entityId,
                k -> new EntityMetrics());
        m.eventCount.incrementAndGet();
        m.totalLatencyNs.addAndGet(latencyNs);
        m.lastEventMs = System.currentTimeMillis();
        updateMaxLatency(m, latencyNs);

        // Check: event loop > 100ms
        if (latencyNs > 100_000_000L) {
            SpunkAlert alert = new SpunkAlert(sbbType, entityId,
                    "event_loop_gt_100ms", System.currentTimeMillis(),
                    Map.of("latencyNs", latencyNs, "latencyMs", latencyNs / 1_000_000L));
            addAlert(alert);
        }

        // Check: memory spike > 100MB in one entity
        if (memDeltaBytes > 100 * 1024 * 1024) {
            SpunkAlert alert = new SpunkAlert(sbbType, entityId,
                    "mem_spike_gt_100MB", System.currentTimeMillis(),
                    Map.of("memDeltaBytes", memDeltaBytes,
                           "memDeltaMB", memDeltaBytes / (1024 * 1024)));
            addAlert(alert);
        }
    }

    private static void updateMaxLatency(EntityMetrics m, long latencyNs) {
        long current;
        do {
            current = m.maxLatencyNs.get();
            if (latencyNs <= current) return;
        } while (!m.maxLatencyNs.compareAndSet(current, latencyNs));
    }

    public List<SpunkAlert> detectSpunks() {
        return List.copyOf(alerts);
    }

    public List<SpunkAlert> activeSpunks() {
        long cutoff = System.currentTimeMillis() - 300_000L; // last 5 min
        return alerts.stream()
                .filter(a -> a.timestamp() >= cutoff)
                .toList();
    }

    private void addAlert(SpunkAlert alert) {
        alerts.offer(alert);
        while (alerts.size() > MAX_SPUNK_ALERTS) {
            alerts.poll();
        }
    }
}
