/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.telemetry;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The dispatch bridge fans one delivery outcome out to the right collectors:
 * success → SBB throughput/latency + spunk sample + stale heartbeat;
 * failure → SBB error counter + error ring buffer + heartbeat (an erroring
 * entity is an error storm, not a leak).
 */
public class TelemetryDispatchObserverTest {

    /** Minimal TelemetryPort exposing REAL collectors — asserts are exact. */
    private static final class CollectorPort implements TelemetryPort {
        final SbbCollector sbb = new SbbCollector();
        final ErrorCollector errors = new ErrorCollector();
        final SpunkDetector spunks = new SpunkDetector();
        final StaleDetector stales = new StaleDetector();

        @Override public SbbCollector sbbCollector() { return sbb; }
        @Override public ErrorCollector errorCollector() { return errors; }
        @Override public SpunkDetector spunkDetector() { return spunks; }
        @Override public StaleDetector staleDetector() { return stales; }

        @Override public RaCollector raCollector() { throw new UnsupportedOperationException(); }
        @Override public ResourceMonitor resourceMonitor() { throw new UnsupportedOperationException(); }
        @Override public AlarmEngine alarmEngine() { throw new UnsupportedOperationException(); }
        @Override public AutoReconfigEngine autoReconfig() { throw new UnsupportedOperationException(); }
        @Override public boolean isAutoReconfigEnabled() { return false; }
        @Override public void setAutoReconfigEnabled(boolean enabled) { }
        @Override public String scrape() { return ""; }
        @Override public Counter customCounter(String name, String... tagPairs) { throw new UnsupportedOperationException(); }
        @Override public Gauge customGauge(String name, Supplier<Number> supplier, String... tagPairs) { throw new UnsupportedOperationException(); }
        @Override public TelemetrySnapshot snapshot() { throw new UnsupportedOperationException(); }
    }

    @Test
    public void successFansOutToThroughputSpunkAndHeartbeat() {
        CollectorPort port = new CollectorPort();
        TelemetryDispatchObserver observer = new TelemetryDispatchObserver(port);

        observer.onEventProcessed("HelloWorldSbb", "e-1", 1_500_000L);
        observer.onEventProcessed("HelloWorldSbb", "e-1", 2_000_000L);

        assertEquals(2, port.sbb.getEventsProcessed());
        assertEquals(0, port.sbb.getErrorCount());
        assertEquals("heartbeat keeps the entity out of the stale list",
                0, port.stales.detectStale(60_000, 3_600_000).size());
    }

    @Test
    public void errorFansOutToErrorCounterRingBufferAndHeartbeat() {
        CollectorPort port = new CollectorPort();
        TelemetryDispatchObserver observer = new TelemetryDispatchObserver(port);

        observer.onDispatchError("HelloWorldSbb", "e-2", new IllegalStateException("boom"));

        assertEquals(1, port.sbb.getErrorCount());
        assertEquals(0, port.sbb.getEventsProcessed());
        var recent = port.errors.recent(1);
        assertFalse(recent.isEmpty());
        assertEquals("HelloWorldSbb", recent.get(0).sbbType());
        assertEquals("e-2", recent.get(0).entityId());
        assertTrue(recent.get(0).exceptionType().contains("IllegalStateException"));
        assertEquals("failing entity still heartbeats — error storm, not leak",
                0, port.stales.detectStale(60_000, 3_600_000).size());
    }
}
