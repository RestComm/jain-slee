package com.example.helloworld.quarkus.telemetry;

import com.example.helloworld.quarkus.support.TelemetryFixtures;
import com.example.helloworld.quarkus.support.TelemetryFixtures.FakeTelemetryPort;

import io.vertx.core.json.JsonObject;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Coverage of the batched Log4j sink: the compact-summary JSON contract,
 * defensive null handling, batching by size and by age, drain-on-close, the
 * empty-buffer no-op, and the asynchronous sampling loop. Emission is captured
 * through the package-private test seam — no Log4j appender required.
 */
class TelemetryLogSinkTest {

    private static FakeTelemetryPort portAt(double heapPct, double cpu, long errors, int spunks, long leaks) {
        return new FakeTelemetryPort(TelemetryFixtures.snapshot(heapPct, cpu, errors, spunks, leaks));
    }

    // ── summarize() JSON contract ─────────────────────────────────────

    @Test
    void summarizeEmitsCompactOperationalJson() {
        var snap = TelemetryFixtures.snapshot(25.0, 0.15, 7, 2, 1);
        JsonObject o = new JsonObject(TelemetryLogSink.summarize(snap));

        assertEquals(25.0, o.getDouble("heapPct"), 1e-9);
        assertEquals(0.15, o.getDouble("cpu"), 1e-9);
        assertEquals(7L, (long) o.getLong("sbbErrors"));
        assertEquals(2, (int) o.getInteger("spunks"));
        assertEquals(1L, (long) o.getLong("staleLeaks"), "only leaked entities counted");
        assertEquals(42L, (long) o.getLong("sbbActive"));
        assertTrue(o.getBoolean("autoReconfig"));
        assertEquals(1, o.getJsonArray("sbbs").size());
        // No forensic detail leaks into the compact line.
        assertFalse(o.encode().contains("stackTrace"));
    }

    @Test
    void summarizeToleratesNullResources() {
        JsonObject o = new JsonObject(TelemetryLogSink.summarize(TelemetryFixtures.snapshotNullResources()));
        assertEquals(0L, (long) o.getLong("heapUsedMb"));
        assertEquals(0.0, o.getDouble("heapPct"), 1e-9);
    }

    // ── batching ───────────────────────────────────────────────────────

    @Test
    void flushesWhenBatchSizeReached() {
        List<String> emitted = new CopyOnWriteArrayList<>();
        var sink = new TelemetryLogSink(portAt(20, 0.1, 0, 0, 0),
                /* sampleInterval */ 1_000_000, /* batchSize */ 3, /* maxAge */ 1_000_000);
        sink.emitter(emitted::add);

        sink.sampleOnce();
        sink.sampleOnce();
        assertTrue(emitted.isEmpty(), "must not flush before batch is full");

        sink.sampleOnce();
        assertEquals(1, emitted.size(), "third sample completes the batch");
        assertEquals(3, emitted.get(0).split("\n").length, "batch carries all 3 lines");
    }

    @Test
    void flushesWhenBatchAgeExceeded() throws Exception {
        List<String> emitted = new CopyOnWriteArrayList<>();
        var sink = new TelemetryLogSink(portAt(20, 0.1, 0, 0, 0),
                /* sampleInterval */ 1_000_000, /* batchSize */ 10_000, /* maxAge */ 50);
        sink.emitter(emitted::add);

        sink.sampleOnce();
        assertTrue(emitted.isEmpty(), "fresh buffer must not flush on age");

        Thread.sleep(70);
        sink.sampleOnce();
        assertEquals(1, emitted.size(), "aged buffer flushes on next sample");
        assertEquals(2, emitted.get(0).split("\n").length);
    }

    @Test
    void closeDrainsBufferedSamples() {
        List<String> emitted = new CopyOnWriteArrayList<>();
        var sink = new TelemetryLogSink(portAt(20, 0.1, 0, 0, 0),
                1_000_000, 10_000, 1_000_000);
        sink.emitter(emitted::add);

        sink.sampleOnce();
        assertTrue(emitted.isEmpty());
        sink.close();
        assertEquals(1, emitted.size(), "close() must flush the tail");
    }

    @Test
    void emptyBufferNeverEmits() {
        List<String> emitted = new CopyOnWriteArrayList<>();
        var sink = new TelemetryLogSink(portAt(20, 0.1, 0, 0, 0), 1_000_000, 10_000, 1_000_000);
        sink.emitter(emitted::add);
        sink.flush();
        sink.close();
        assertTrue(emitted.isEmpty());
    }

    // ── async loop ──────────────────────────────────────────────────────

    @Test
    void asyncLoopSamplesAndEmitsValidJson() throws Exception {
        List<String> emitted = new CopyOnWriteArrayList<>();
        var sink = new TelemetryLogSink(portAt(25, 0.15, 0, 0, 0),
                /* sampleInterval */ 10, /* batchSize */ 1, /* maxAge */ 1_000_000);
        sink.emitter(emitted::add);
        sink.start();
        sink.start(); // idempotent

        long deadline = System.currentTimeMillis() + 2_000;
        while (emitted.isEmpty() && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        sink.close();

        assertFalse(emitted.isEmpty(), "background loop must have emitted at least one batch");
        JsonObject first = new JsonObject(emitted.get(0).split("\n")[0]);
        assertEquals(25.0, first.getDouble("heapPct"), 1e-9);
    }
}
