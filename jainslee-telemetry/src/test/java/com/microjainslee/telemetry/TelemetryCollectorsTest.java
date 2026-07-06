package com.microjainslee.telemetry;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Comprehensive tests for all telemetry collectors, alarm engine,
 * stale detector, spunk detector, auto-reconfig engine, and the
 * core Micrometer gauge registration.
 */
public class TelemetryCollectorsTest {

    // ── SbbCollector ──────────────────────────────────────────────────

    @Test
    public void sbbCollectorTracksEntities() {
        SbbCollector c = new SbbCollector();
        c.onEntityCreated("MySbb", "e1");
        c.onEntityCreated("MySbb", "e2");
        assertEquals(2, c.getTotalEntities());
        assertEquals(2, c.getActiveEntities());

        c.onEntityReleased("MySbb", "e1");
        assertEquals(2, c.getTotalEntities());
        assertEquals(1, c.getActiveEntities());
    }

    @Test
    public void sbbCollectorTracksEventsAndEps() {
        SbbCollector c = new SbbCollector();
        for (int i = 0; i < 120; i++) {
            c.onEventProcessed("MySbb", "e1", 1_000_000L, 0);
        }
        assertEquals(120, c.getEventsProcessed());
        double eps = c.getEventsPerSecond();
        assertTrue("EPS should be > 0, got " + eps, eps > 0);
    }

    @Test
    public void sbbCollectorTracksErrorsAndSpunks() {
        SbbCollector c = new SbbCollector();
        c.onEntityCreated("BadSbb", "e1");
        c.onError("BadSbb", "e1");
        c.onError("BadSbb", "e1");
        c.onSpunk("BadSbb", "e1");
        assertEquals(2, c.getErrorCount());
        assertEquals(1, c.getSpunkCount());
    }

    @Test
    public void sbbCollectorHealthCheck() {
        SbbCollector c = new SbbCollector();
        assertTrue(c.isHealthy());
        c.onEventProcessed("Sbb", "e1", 1_000_000L, 0);
        c.onError("Sbb", "e1");
        assertFalse(c.isHealthy());
    }

    @Test
    public void sbbCollectorPerTypeStats() {
        SbbCollector c = new SbbCollector();
        c.onEntityCreated("TypeA", "a1");
        c.onEntityCreated("TypeB", "b1");
        c.onEventProcessed("TypeA", "a1", 500_000L, 0);
        c.onEventProcessed("TypeA", "a1", 1_500_000L, 0);
        c.onError("TypeB", "b1");

        List<SbbCollector.PerType> perType = c.perType();
        assertEquals(2, perType.size());
        SbbCollector.PerType typeA = perType.stream()
                .filter(p -> p.sbbType().equals("TypeA")).findFirst().orElseThrow();
        assertEquals(1, typeA.active());
        assertEquals(0, typeA.errors());
    }

    @Test
    public void sbbCollectorBaselineEps() {
        SbbCollector c = new SbbCollector();
        assertEquals(0.0, c.getBaselineEps("NewSbb"), 0.001);
        c.setBaselineEps("NewSbb", 100.5);
        assertEquals(100.5, c.getBaselineEps("NewSbb"), 0.001);
    }

    @Test
    public void sbbCollectorStaleAndLeakedMarkers() {
        SbbCollector c = new SbbCollector();
        c.markStaleEntities(5);
        c.markLeakedEntities(2);
        assertEquals(5, c.getStaleEntities());
        assertEquals(2, c.getLeakedEntities());
    }

    // ── RaCollector ───────────────────────────────────────────────────

    @Test
    public void raCollectorTracksStateAndPort() {
        RaCollector c = new RaCollector();
        c.updateState("DiameterRA", "ACTIVE", 3868);
        List<RaCollector.RaStats> stats = c.stats();
        assertEquals(1, stats.size());
        assertEquals("DiameterRA", stats.get(0).raName());
        assertEquals("ACTIVE", stats.get(0).state());
        assertEquals(3868, stats.get(0).port());
    }

    @Test
    public void raCollectorTracksEventsCommandsAndFailures() {
        RaCollector c = new RaCollector();
        c.recordEventFired("SipRA");
        c.recordEventFired("SipRA");
        c.recordCommand("SipRA");
        c.recordFailure("SipRA");
        RaCollector.RaStats s = c.stats().get(0);
        assertEquals(2, s.eventsFired());
        assertEquals(1, s.commandsSent());
        assertEquals(1, s.failures());
    }

    @Test
    public void raCollectorMultipleRas() {
        RaCollector c = new RaCollector();
        c.updateState("RA1", "ACTIVE", 1111);
        c.updateState("RA2", "STOPPED", 2222);
        c.recordEventFired("RA1");
        assertEquals(2, c.stats().size());
    }

    // ── ResourceMonitor ───────────────────────────────────────────────

    @Test
    public void resourceMonitorSnapshotHasAllFields() {
        ResourceMonitor m = new ResourceMonitor();
        m.setMinCaptureIntervalMillis(0);
        ResourceMonitor.ResourceSnapshot snap = m.snapshot();
        assertNotNull(snap);
        assertTrue("heapMax > 0", snap.heapMaxMb() > 0);
        assertTrue("heapUsed >= 0", snap.heapUsedMb() >= 0);
        assertTrue("activeThreads > 0", snap.activeThreads() > 0);
        assertTrue("timestamp > 0", snap.timestampMillis() > 0);
        assertTrue("gcCount >= 0", snap.gcCount() >= 0);
        assertTrue("gcTimeMs >= 0", snap.gcTimeMs() >= 0);
    }

    @Test
    public void resourceMonitorVirtualThreadCounter() {
        ResourceMonitor m = new ResourceMonitor();
        m.setMinCaptureIntervalMillis(0);
        m.setVirtualThreadCounter(() -> 42L);
        assertEquals(42, m.snapshot().virtualThreads());
    }

    @Test
    public void resourceMonitorHistoryGrows() {
        ResourceMonitor m = new ResourceMonitor();
        m.setMinCaptureIntervalMillis(0);
        for (int i = 0; i < 5; i++) {
            m.snapshot();
            try { Thread.sleep(1); } catch (InterruptedException ignored) { }
        }
        long count = m.historyStream().count();
        assertTrue("History should have >= 2 entries, got " + count, count >= 2);
    }

    // ── ErrorCollector ────────────────────────────────────────────────

    @Test
    public void errorCollectorRecordsErrors() {
        ErrorCollector c = new ErrorCollector();
        c.record("MySbb", "e1", new RuntimeException("test failure"));
        List<ErrorCollector.ErrorEntry> recent = c.recent(1);
        assertEquals(1, recent.size());
        assertEquals("MySbb", recent.get(0).sbbType());
        assertTrue(recent.get(0).exceptionType().contains("RuntimeException"));
        assertEquals("test failure", recent.get(0).message());
    }

    @Test
    public void errorCollectorErrorRateByType() {
        ErrorCollector c = new ErrorCollector();
        c.record("Sbb", "e1", new IllegalArgumentException("bad arg"));
        c.record("Sbb", "e2", new IllegalArgumentException("bad arg again"));
        c.record("Sbb", "e3", new NullPointerException("null!"));
        Map<String, Long> rates = c.errorRateByType();
        assertEquals(2L, (long) rates.get("java.lang.IllegalArgumentException"));
        assertEquals(1L, (long) rates.get("java.lang.NullPointerException"));
    }

    // ── AlarmEngine ───────────────────────────────────────────────────

    @Test
    public void alarmEngineFireAndClear() {
        AlarmEngine engine = new AlarmEngine();
        String id = engine.fire(AlarmEngine.TelemetryAlarmLevel.WARNING,
                "Test", "test alarm", Map.of("key", "value"));
        assertNotNull(id);
        assertTrue(id.startsWith("ALM-"));
        assertEquals(1, engine.active().size());
        assertTrue(engine.clear(id));
        assertEquals(0, engine.active().size());
    }

    @Test
    public void alarmEngineHistory() {
        AlarmEngine engine = new AlarmEngine();
        engine.fire(AlarmEngine.TelemetryAlarmLevel.INFO, "Src", "msg1", Map.of());
        engine.fire(AlarmEngine.TelemetryAlarmLevel.CRITICAL, "Src", "msg2", Map.of());
        assertEquals(2, engine.history(60).size());
    }

    // ── StaleDetector ─────────────────────────────────────────────────

    @Test
    public void staleDetectorTracksAndUntracks() {
        StaleDetector d = new StaleDetector();
        d.trackHeartbeat("e1", "TypeA");
        assertEquals(1, d.trackedEntityCount());
        d.untrackHeartbeat("e1");
        assertEquals(0, d.trackedEntityCount());
    }

    @Test
    public void staleDetectorDetectsStaleWithZeroThreshold() {
        StaleDetector d = new StaleDetector();
        d.trackHeartbeat("e1", "TypeA");
        List<StaleDetector.StaleAlert> stale = d.detectStale(0, 0);
        assertEquals(1, stale.size());
        assertTrue(stale.get(0).leaked());
    }

    // ── SpunkDetector ─────────────────────────────────────────────────

    @Test
    public void spunkDetectorFlagsSlowEvent() {
        SpunkDetector d = new SpunkDetector();
        d.onEventProcessed("SlowSbb", "e1", 200_000_000L, 0);
        assertEquals(1, d.activeSpunks().size());
        assertEquals("event_loop_gt_100ms", d.activeSpunks().get(0).reason());
    }

    @Test
    public void spunkDetectorFlagsMemorySpike() {
        SpunkDetector d = new SpunkDetector();
        d.onEventProcessed("MemSbb", "e1", 1_000_000L, 200 * 1024 * 1024);
        assertEquals(1, d.activeSpunks().size());
        assertEquals("mem_spike_gt_100MB", d.activeSpunks().get(0).reason());
    }

    @Test
    public void spunkDetectorNormalEventPasses() {
        SpunkDetector d = new SpunkDetector();
        d.onEventProcessed("OkSbb", "e1", 1_000_000L, 1024);
        assertEquals(0, d.activeSpunks().size());
    }

    // ── PrometheusExporter ────────────────────────────────────────────

    @Test
    public void prometheusExporterScrapeReturnsText() {
        PrometheusExporter exporter = new PrometheusExporter();
        String scrape = exporter.scrape();
        assertNotNull(scrape);
        // May contain only HELP/TYPE lines if no custom meters; that's valid
        assertFalse("Scrape should be non-null", scrape == null);
    }

    // ── MicrometerTelemetryPort core gauge registration ───────────────

    @Test
    public void micrometerPortRegistersAllCoreGauges() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);

        // Verify via meter count — all 23 core gauges + any JVM built-ins
        var meters = registry.getMeters();
        assertTrue("Should have at least 23 meters, got " + meters.size(),
                meters.size() >= 23);

        // Verify specific metric names are registered
        java.util.Set<String> names = new java.util.HashSet<>();
        for (var m : meters) {
            names.add(m.getId().getName());
        }

        String[] expected = {
                "jainslee_sbb_entities_total",
                "jainslee_sbb_entities_active",
                "jainslee_sbb_events_total",
                "jainslee_sbb_events_per_second",
                "jainslee_sbb_errors_total",
                "jainslee_sbb_spunks_total",
                "jainslee_sbb_stale_entities",
                "jainslee_sbb_leaked_entities",
                "jainslee_sbb_healthy",
                "jainslee_heap_used_mb",
                "jainslee_heap_max_mb",
                "jainslee_heap_usage_percent",
                "jainslee_cpu_load_percent",
                "jainslee_threads_active",
                "jainslee_threads_virtual",
                "jainslee_gc_count",
                "jainslee_gc_time_ms",
                "jainslee_open_fds",
                "jainslee_ra_events_fired_total",
                "jainslee_ra_commands_sent_total",
                "jainslee_ra_failures_total",
                "jainslee_stale_tracked_entities",
                "jainslee_alarms_active",
        };
        for (String exp : expected) {
            assertTrue("Registry missing: " + exp, names.contains(exp));
        }
    }

    @Test
    public void micrometerPortCustomCounterAppearsInScrape() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        TelemetryPort.Counter c = port.customCounter("my_custom_total", "env", "test");
        c.increment();
        c.increment();
        String scrape = port.scrape();
        assertTrue(scrape.contains("my_custom_total"));
    }

    @Test
    public void micrometerPortCustomGaugeAppearsInScrape() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        java.util.concurrent.atomic.AtomicLong val = new java.util.concurrent.atomic.AtomicLong(42);
        port.customGauge("my_gauge", val::get, "host", "H1");
        assertTrue(port.scrape().contains("my_gauge"));
    }

    @Test
    public void micrometerPortSnapshotIncludesAllCollectors() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        port.sbbCollector().onEntityCreated("TestSbb", "e1");
        port.raCollector().updateState("RA1", "ACTIVE", 1234);
        port.errorCollector().record("Sbb", "e1", new Exception("test"));
        TelemetryPort.TelemetrySnapshot snap = port.snapshot();
        assertNotNull(snap);
        assertNotNull(snap.sbbs());
        assertNotNull(snap.ras());
        assertNotNull(snap.resources());
        assertNotNull(snap.recentErrors());
    }

    @Test
    public void micrometerPortAutoReconfigToggle() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        assertTrue(port.isAutoReconfigEnabled());
        port.setAutoReconfigEnabled(false);
        assertFalse(port.isAutoReconfigEnabled());
        port.setAutoReconfigEnabled(true);
        assertTrue(port.isAutoReconfigEnabled());
    }

    @Test
    public void micrometerPortReturnsRegistry() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        assertSame(registry, port.registry());
    }

    @Test
    public void micrometerCounterAdapterDelegates() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        TelemetryPort.Counter c = port.customCounter("test_counter");
        assertEquals(0, c.count());
        c.increment();
        assertEquals(1, c.count());
        c.increment(5);
        assertEquals(6, c.count());
    }

    @Test
    public void micrometerGaugePollsSupplier() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        MicrometerTelemetryPort port = new MicrometerTelemetryPort(registry, null);
        java.util.concurrent.atomic.AtomicLong val = new java.util.concurrent.atomic.AtomicLong(10);
        TelemetryPort.Gauge g = port.customGauge("test_gauge", val::get);
        assertEquals(10L, g.value().longValue());
        val.set(99);
        assertEquals(99L, g.value().longValue());
    }

    // ── toTags ────────────────────────────────────────────────────────

    @Test
    public void toTagsWithEmptyInput() {
        assertNotNull(MicrometerTelemetryPort.toTags());
        assertNotNull(MicrometerTelemetryPort.toTags((String[]) null));
    }

    @Test
    public void toTagsWithPairs() {
        var tags = MicrometerTelemetryPort.toTags("k1", "v1", "k2", "v2");
        String rendered = tags.stream()
                .map(t -> t.getKey() + "=" + t.getValue())
                .reduce((a, b) -> a + "," + b).orElse("");
        assertTrue(rendered.contains("k1=v1"));
        assertTrue(rendered.contains("k2=v2"));
    }

    // ── AutoReconfigEngine ────────────────────────────────────────────

    @Test
    public void autoReconfigStartStop() {
        AutoReconfigEngine engine = new AutoReconfigEngine(
                new SbbCollector(), new ErrorCollector(), new ResourceMonitor(),
                new StaleDetector(), new AlarmEngine(), null);
        assertFalse(engine.isStarted());
        engine.start(30, TimeUnit.SECONDS);
        assertTrue(engine.isStarted());
        engine.stop();
        assertFalse(engine.isStarted());
    }

    @Test
    public void autoReconfigMaybeEvaluateDoesNotThrow() {
        AutoReconfigEngine engine = new AutoReconfigEngine(
                new SbbCollector(), new ErrorCollector(), new ResourceMonitor(),
                new StaleDetector(), new AlarmEngine(), null);
        engine.start(30, TimeUnit.SECONDS);
        try {
            engine.maybeEvaluate();
        } finally {
            engine.stop();
        }
    }
}
