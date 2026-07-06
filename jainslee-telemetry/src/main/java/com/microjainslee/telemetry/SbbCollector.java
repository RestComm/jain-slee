package com.microjainslee.telemetry;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.LongAdder;

public final class SbbCollector {

    private final LongAdder totalEntities = new LongAdder();
    private final AtomicLong activeEntities = new AtomicLong();
    private final LongAdder eventsProcessed = new LongAdder();
    private final LongAdder errorCount = new LongAdder();
    private final LongAdder spunkCount = new LongAdder();
    private final AtomicLong staleEntities = new AtomicLong();
    private final AtomicLong leakedEntities = new AtomicLong();

    private static final int EPS_WINDOW_SECONDS = 60;
    private final AtomicReferenceArray<long[]> epsWindow
            = new AtomicReferenceArray<>(EPS_WINDOW_SECONDS);

    private final ConcurrentHashMap<String, PerTypeStats> perTypeStats
            = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Double> baselineEps
            = new ConcurrentHashMap<>();

    public record PerType(String sbbType, long active, long errors, long spunks,
                          double eps, long p99us) {}

    private static final class PerTypeStats {
        final LongAdder events = new LongAdder();
        final LongAdder errors = new LongAdder();
        final LongAdder spunks = new LongAdder();
        final AtomicLong active = new AtomicLong();
        final long[] latencyRing = new long[100];
        volatile int latencyIdx;

        void recordLatency(long latencyUs) {
            int idx = latencyIdx;
            latencyRing[idx % latencyRing.length] = latencyUs;
            latencyIdx = idx + 1;
        }

        long computeP99() {
            long[] copy = latencyRing.clone();
            java.util.Arrays.sort(copy);
            int p99Idx = (int) (copy.length * 0.99);
            if (p99Idx >= copy.length) p99Idx = copy.length - 1;
            if (p99Idx < 0) p99Idx = 0;
            return copy[p99Idx];
        }
    }

    public void onEventProcessed(String sbbType, String entityId,
                                  long latencyNs, long memDeltaBytes) {
        eventsProcessed.increment();
        PerTypeStats stats = perTypeStats.computeIfAbsent(sbbType, k -> new PerTypeStats());
        stats.events.increment();
        stats.recordLatency(latencyNs / 1000L);
        recordEpsTick();
    }

    public void onEntityCreated(String sbbType, String entityId) {
        totalEntities.increment();
        activeEntities.incrementAndGet();
        PerTypeStats stats = perTypeStats.get(sbbType);
        if (stats != null) stats.active.incrementAndGet();
    }

    public void onEntityReleased(String sbbType, String entityId) {
        activeEntities.decrementAndGet();
        PerTypeStats stats = perTypeStats.get(sbbType);
        if (stats != null) stats.active.decrementAndGet();
    }

    public void onError(String sbbType, String entityId) {
        errorCount.increment();
        PerTypeStats stats = perTypeStats.get(sbbType);
        if (stats != null) stats.errors.increment();
    }

    public void onSpunk(String sbbType, String entityId) {
        spunkCount.increment();
        PerTypeStats stats = perTypeStats.get(sbbType);
        if (stats != null) stats.spunks.increment();
    }

    public void markStaleEntities(long count) { staleEntities.set(count); }
    public void markLeakedEntities(long count) { leakedEntities.set(count); }

    public double getBaselineEps(String sbbType) {
        return baselineEps.getOrDefault(sbbType, 0.0);
    }

    public void setBaselineEps(String sbbType, double eps) {
        baselineEps.put(sbbType, eps);
    }

    public long getTotalEntities() { return totalEntities.sum(); }
    public long getActiveEntities() { return activeEntities.get(); }
    public long getEventsProcessed() { return eventsProcessed.sum(); }
    public long getErrorCount() { return errorCount.sum(); }
    public long getSpunkCount() { return spunkCount.sum(); }
    public long getStaleEntities() { return staleEntities.get(); }
    public long getLeakedEntities() { return leakedEntities.get(); }

    public double getEventsPerSecond() {
        long total = 0;
        for (int i = 0; i < EPS_WINDOW_SECONDS; i++) {
            long[] tick = epsWindow.get(i);
            if (tick != null) total += tick[0];
        }
        return (double) total / EPS_WINDOW_SECONDS;
    }

    public boolean isHealthy() {
        long processed = eventsProcessed.sum();
        return processed == 0 || errorCount.sum() < processed * 0.05;
    }

    public List<PerType> perType() {
        return perTypeStats.entrySet().stream()
                .map(e -> {
                    PerTypeStats s = e.getValue();
                    return new PerType(e.getKey(), s.active.get(), s.errors.sum(),
                            s.spunks.sum(), computePerTypeEps(e.getKey()), s.computeP99());
                })
                .toList();
    }

    private double computePerTypeEps(String type) {
        PerTypeStats stats = perTypeStats.get(type);
        if (stats == null) return 0.0;
        long typeEvents = stats.events.sum();
        long totalEv = eventsProcessed.sum();
        if (totalEv == 0) return 0.0;
        return getEventsPerSecond() * ((double) typeEvents / totalEv);
    }

    private void recordEpsTick() {
        long nowSec = System.currentTimeMillis() / 1000;
        int idx = (int) (nowSec % EPS_WINDOW_SECONDS);
        long[] tick = epsWindow.get(idx);
        if (tick == null) {
            tick = new long[1];
            epsWindow.set(idx, tick);
        }
        tick[0]++;
    }
}
