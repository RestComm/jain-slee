package com.microjainslee.telemetry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.LongAdder;

/**
 * Passive error collector — ring-buffer for recent errors, lock-free.
 */
public final class ErrorCollector {

    public record ErrorEntry(String sbbType, String entityId, String exceptionType,
                             String message, String stackTrace, long timestamp) {}

    private static final int RING_SIZE = 1000;
    private final AtomicReferenceArray<ErrorEntry> ring = new AtomicReferenceArray<>(RING_SIZE);
    private final AtomicInteger ringPos = new AtomicInteger();

    private final ConcurrentHashMap<String, LongAdder> errorRateByType = new ConcurrentHashMap<>();

    /**
     * Record an error that occurred during SBB processing.
     * Called by EventRouter when an exception is caught.
     */
    public void record(String sbbType, String entityId, Throwable error) {
        ErrorEntry entry = new ErrorEntry(
                sbbType, entityId,
                error.getClass().getName(),
                error.getMessage(),
                stackTraceToString(error),
                System.currentTimeMillis()
        );
        int pos = ringPos.getAndIncrement() % RING_SIZE;
        ring.set(pos, entry);

        errorRateByType.computeIfAbsent(entry.exceptionType(), k -> new LongAdder())
                .increment();
    }

    public List<ErrorEntry> recent(int minutes) {
        long cutoff = System.currentTimeMillis() - (minutes * 60_000L);
        List<ErrorEntry> result = new java.util.ArrayList<>();
        for (int i = 0; i < RING_SIZE; i++) {
            ErrorEntry e = ring.get(i);
            if (e != null && e.timestamp() >= cutoff) {
                result.add(e);
            }
        }
        return result;
    }

    public Map<String, Long> errorRateByType() {
        Map<String, Long> result = new java.util.HashMap<>();
        for (var e : errorRateByType.entrySet()) {
            result.put(e.getKey(), e.getValue().sum());
        }
        return result;
    }

    private static String stackTraceToString(Throwable t) {
        if (t == null) return "";
        java.io.StringWriter sw = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }
}
