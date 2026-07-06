package com.microjainslee.telemetry;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Detects idle/leaked entities — no polling, heartbeat-based.
 * Scheduled evaluation (via AlarmEngine/AutoReconfigEngine) scans
 * the heartbeat map periodically.
 */
public final class StaleDetector {

    public record StaleAlert(String entityId, String sbbType,
                             long lastEventMs, long idleDurationMs, boolean leaked) {}

    private static final class Heartbeat {
        final String sbbType;
        volatile long lastEventMs;

        Heartbeat(String sbbType) {
            this.sbbType = sbbType;
            this.lastEventMs = System.currentTimeMillis();
        }
    }

    private final ConcurrentHashMap<String, Heartbeat> heartbeats =
            new ConcurrentHashMap<>();

    /** Passive callback — update last-seen timestamp for an entity. */
    public void trackHeartbeat(String entityId, String sbbType) {
        Heartbeat h = heartbeats.computeIfAbsent(entityId, k -> new Heartbeat(sbbType));
        h.lastEventMs = System.currentTimeMillis();
    }

    /** Called when an entity is released so it stops showing as stale. */
    public void untrackHeartbeat(String entityId) {
        heartbeats.remove(entityId);
    }

    /**
     * Scan for stale/leaked entities.
     * Called periodically (e.g. every 60s) by the scheduler.
     *
     * @param idleWarningMs  threshold for warning (e.g. 5 min)
     * @param leakCriticalMs threshold for leaked/critical (e.g. 30 min)
     */
    public List<StaleAlert> detectStale(long idleWarningMs, long leakCriticalMs) {
        long now = System.currentTimeMillis();
        return heartbeats.entrySet().stream()
                .map(e -> {
                    Heartbeat h = e.getValue();
                    long idle = now - h.lastEventMs;
                    if (idle >= idleWarningMs) {
                        return new StaleAlert(e.getKey(), h.sbbType,
                                h.lastEventMs, idle, idle >= leakCriticalMs);
                    }
                    return null;
                })
                .filter(a -> a != null)
                .toList();
    }

    public int trackedEntityCount() {
        return heartbeats.size();
    }
}
