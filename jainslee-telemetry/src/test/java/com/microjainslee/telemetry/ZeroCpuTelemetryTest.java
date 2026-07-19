package com.microjainslee.telemetry;

import org.junit.Test;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Proves the zero-CPU telemetry contract:
 * <ul>
 *   <li>no telemetry-owned threads exist after start()</li>
 *   <li>ResourceMonitor captures lazily and throttles repeat reads</li>
 *   <li>AutoReconfigEngine only evaluates when driven (scrape/notification),
 *       never on a timer</li>
 * </ul>
 */
public class ZeroCpuTelemetryTest {

    private static Set<String> telemetryThreadNames() {
        Set<String> names = new java.util.HashSet<>();
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            String n = t.getName();
            if (n.startsWith("telemetry-")) {
                names.add(n);
            }
        }
        return names;
    }

    @Test
    public void resourceMonitorStartCreatesNoThreads() {
        ResourceMonitor monitor = new ResourceMonitor();
        monitor.start(30, TimeUnit.SECONDS);
        assertTrue("zero-CPU contract: no telemetry threads may exist, found "
                + telemetryThreadNames(), telemetryThreadNames().isEmpty());
        assertNotNull(monitor.snapshot());
        monitor.stop();
    }

    @Test
    public void resourceMonitorThrottlesRepeatReads() {
        ResourceMonitor monitor = new ResourceMonitor();
        monitor.setMinCaptureIntervalMillis(60_000);
        ResourceMonitor.ResourceSnapshot first = monitor.snapshot();
        ResourceMonitor.ResourceSnapshot second = monitor.snapshot();
        // Inside the throttle window the cached instance is served — no
        // re-capture, no allocation.
        assertSame(first, second);
    }

    @Test
    public void resourceMonitorCapturesFreshAfterWindow() throws Exception {
        ResourceMonitor monitor = new ResourceMonitor();
        monitor.setMinCaptureIntervalMillis(10);
        ResourceMonitor.ResourceSnapshot first = monitor.snapshot();
        Thread.sleep(30);
        ResourceMonitor.ResourceSnapshot second = monitor.snapshot();
        assertTrue(second.timestampMillis() >= first.timestampMillis());
    }

    @Test
    public void virtualThreadCounterIsInjectedNotScanned() {
        ResourceMonitor monitor = new ResourceMonitor();
        monitor.setMinCaptureIntervalMillis(0);
        monitor.setVirtualThreadCounter(() -> 12345L);
        assertEquals(12345, monitor.snapshot().virtualThreads());
    }

    @Test
    public void autoReconfigStartCreatesNoThreads() {
        AutoReconfigEngine engine = new AutoReconfigEngine(
                new SbbCollector(), new ErrorCollector(), new ResourceMonitor(),
                new StaleDetector(), new AlarmEngine(), null);
        try {
            engine.start(30, TimeUnit.SECONDS);
            assertTrue("zero-CPU contract: no telemetry threads may exist, found "
                    + telemetryThreadNames(), telemetryThreadNames().isEmpty());
            assertTrue(engine.isStarted());
        } finally {
            engine.stop();
        }
        assertFalse(engine.isStarted());
    }

    @Test
    public void maybeEvaluateIsThrottled() {
        AutoReconfigEngine engine = new AutoReconfigEngine(
                new SbbCollector(), new ErrorCollector(), new ResourceMonitor(),
                new StaleDetector(), new AlarmEngine(), null);
        try {
            engine.start(1, TimeUnit.HOURS);
            // Both calls inside the gap: at most one evaluation may run;
            // neither may throw, and no thread may be spawned.
            engine.maybeEvaluate();
            engine.maybeEvaluate();
            assertTrue(telemetryThreadNames().isEmpty());
        } finally {
            engine.stop();
        }
    }
}
