package com.example.helloworld.quarkus.telemetry;

import com.microjainslee.telemetry.TelemetryPort;
import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Durable telemetry sink — batches compact operational summaries and flushes
 * them to Log4j2 as newline-delimited JSON.
 *
 * <p><b>Why a log sink alongside Prometheus?</b> Prometheus is a great
 * <i>live</i> pull-based metrics store, but it is not a durable event log and
 * it forgets everything on restart. This sink writes a compact JSON line per
 * sample to a dedicated logger ({@code microjainslee.telemetry}) which
 * {@code log4j2.xml} routes to an async rolling file. A downstream sidecar
 * (Filebeat / Promtail / Vector) can then ship those lines to Elasticsearch,
 * Loki or Splunk — <i>without</i> dragging a heavyweight, GraalVM-hostile
 * Elasticsearch client into the native image.</p>
 *
 * <p><b>Batching.</b> Snapshots accumulate in an in-memory buffer and are
 * emitted as a single Log4j event once the buffer reaches {@code batchSize}
 * <i>or</i> {@code maxBatchAgeMillis} elapses — whichever comes first. One
 * daemon virtual thread owns the sampling loop; the system is otherwise silent.</p>
 */
public final class TelemetryLogSink implements AutoCloseable {

    /** Dedicated logger — route this to a rolling file in {@code log4j2.xml}. */
    private static final Logger SINK = LogManager.getLogger("microjainslee.telemetry");
    private static final Logger LOG = LogManager.getLogger(TelemetryLogSink.class);

    private final TelemetryPort telemetry;
    private final long sampleIntervalMillis;
    private final int batchSize;
    private final long maxBatchAgeMillis;

    private final Deque<String> buffer = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private volatile long oldestBufferedAt;
    private volatile boolean running;
    private Thread worker;

    public TelemetryLogSink(TelemetryPort telemetry) {
        this(telemetry, 10_000L, 30, 60_000L);
    }

    public TelemetryLogSink(TelemetryPort telemetry, long sampleIntervalMillis,
                            int batchSize, long maxBatchAgeMillis) {
        this.telemetry = telemetry;
        this.sampleIntervalMillis = sampleIntervalMillis;
        this.batchSize = Math.max(1, batchSize);
        this.maxBatchAgeMillis = maxBatchAgeMillis;
    }

    /** Start the single daemon virtual thread that samples and batches. */
    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        worker = Thread.ofVirtual().name("telemetry-log-sink").start(this::loop);
        LOG.info("[telemetry] log sink started (sample={}ms, batch={}, maxAge={}ms)",
                sampleIntervalMillis, batchSize, maxBatchAgeMillis);
    }

    @Override
    public synchronized void close() {
        running = false;
        if (worker != null) {
            worker.interrupt();
            worker = null;
        }
        flush(); // drain whatever is buffered on shutdown
    }

    private void loop() {
        while (running) {
            try {
                sampleOnce();
                Thread.sleep(sampleIntervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (RuntimeException e) {
                LOG.warn("[telemetry] sink sample failed: {}", e.getMessage());
            }
        }
    }

    /** Capture one compact summary, buffer it, and flush if the batch is ready. */
    public void sampleOnce() {
        String line = summarize(telemetry.snapshot());
        boolean flushNow;
        lock.lock();
        try {
            if (buffer.isEmpty()) {
                oldestBufferedAt = System.currentTimeMillis();
            }
            buffer.addLast(line);
            flushNow = buffer.size() >= batchSize
                    || System.currentTimeMillis() - oldestBufferedAt >= maxBatchAgeMillis;
        } finally {
            lock.unlock();
        }
        if (flushNow) {
            flush();
        }
    }

    /** Emit the buffered lines as one Log4j event (newline-delimited JSON). */
    public void flush() {
        StringBuilder sb;
        int n;
        lock.lock();
        try {
            if (buffer.isEmpty()) {
                return;
            }
            n = buffer.size();
            sb = new StringBuilder(n * 256);
            for (String s : buffer) {
                sb.append(s).append('\n');
            }
            buffer.clear();
        } finally {
            lock.unlock();
        }
        // One batched log event carrying n JSON lines.
        SINK.info(sb.toString().stripTrailing());
        LOG.debug("[telemetry] flushed batch of {} sample(s)", n);
    }

    /**
     * Reduce a full snapshot to a compact operational summary. We deliberately
     * drop stack traces and per-entity detail — this line is for trend
     * analysis and alerting, not forensic replay (that stays in Prometheus and
     * the alarm ring buffer).
     */
    private static String summarize(TelemetrySnapshot snap) {
        var r = snap.resources();
        long totalActive = snap.sbbs().stream().mapToLong(s -> s.active()).sum();
        long totalErrors = snap.sbbs().stream().mapToLong(s -> s.errors()).sum();
        double totalEps = snap.sbbs().stream().mapToDouble(s -> s.eps()).sum();

        JsonArray sbbs = new JsonArray();
        for (var s : snap.sbbs()) {
            sbbs.add(new JsonObject()
                    .put("type", s.sbbType())
                    .put("active", s.active())
                    .put("errors", s.errors())
                    .put("spunks", s.spunks())
                    .put("eps", round(s.eps()))
                    .put("p99us", s.p99us()));
        }

        JsonObject o = new JsonObject()
                .put("ts", System.currentTimeMillis())
                .put("heapUsedMb", r == null ? 0 : r.heapUsedMb())
                .put("heapPct", r == null ? 0 : round(r.heapUsagePercent()))
                .put("cpu", r == null ? 0 : round(r.cpuLoad()))
                .put("vThreads", r == null ? 0 : r.virtualThreads())
                .put("gcMs", r == null ? 0 : r.gcTimeMs())
                .put("sbbActive", totalActive)
                .put("sbbErrors", totalErrors)
                .put("eps", round(totalEps))
                .put("spunks", snap.spunks().size())
                .put("staleLeaks", snap.stales().stream().filter(s -> s.leaked()).count())
                .put("alarms", snap.activeAlarms().size())
                .put("autoReconfig", snap.autoReconfigEnabled())
                .put("sbbs", sbbs);
        return o.encode();
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
