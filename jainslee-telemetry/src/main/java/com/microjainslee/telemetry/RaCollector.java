package com.microjainslee.telemetry;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Passive collector for Resource Adaptor statistics — zero-CPU, callback-driven.
 */
public final class RaCollector {

    public record RaStats(String raName, String state, int port, long eventsFired,
                          long commandsSent, long failures) {}

    private static final class RaRecord {
        volatile String state = "UNKNOWN";
        volatile int port;
        final LongAdder eventsFired = new LongAdder();
        final LongAdder commandsSent = new LongAdder();
        final LongAdder failures = new LongAdder();
    }

    private final ConcurrentHashMap<String, RaRecord> records = new ConcurrentHashMap<>();

    /** Called by RA endpoint when state changes. */
    public void updateState(String raName, String state, int port) {
        RaRecord rec = records.computeIfAbsent(raName, k -> new RaRecord());
        rec.state = state;
        rec.port = port;
    }

    /** Called by RA when an event is fired into the SLEE. */
    public void recordEventFired(String raName) {
        RaRecord rec = records.computeIfAbsent(raName, k -> new RaRecord());
        rec.eventsFired.increment();
    }

    /** Called by SBB when a command is sent to the RA. */
    public void recordCommand(String raName) {
        RaRecord rec = records.computeIfAbsent(raName, k -> new RaRecord());
        rec.commandsSent.increment();
    }

    /** Called when an RA operation fails. */
    public void recordFailure(String raName) {
        RaRecord rec = records.computeIfAbsent(raName, k -> new RaRecord());
        rec.failures.increment();
    }

    public List<RaStats> stats() {
        return records.entrySet().stream()
                .map(e -> {
                    RaRecord r = e.getValue();
                    return new RaStats(e.getKey(), r.state, r.port,
                            r.eventsFired.sum(), r.commandsSent.sum(), r.failures.sum());
                })
                .toList();
    }
}
