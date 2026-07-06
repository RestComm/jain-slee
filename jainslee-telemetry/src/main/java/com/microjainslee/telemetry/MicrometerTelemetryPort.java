package com.microjainslee.telemetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
// Note: io.micrometer.core.instrument.Gauge is used fully-qualified
// inside registerCoreMetrics() to avoid shadowing by TelemetryPort.Gauge.
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microjainslee.core.MicroSleeContainer;

public final class MicrometerTelemetryPort implements TelemetryPort {

    private static final Logger LOG
            = LogManager.getLogger(MicrometerTelemetryPort.class);

    private final SbbCollector sbbCollector;
    private final RaCollector raCollector;
    private final ErrorCollector errorCollector;
    private final ResourceMonitor resourceMonitor;
    private final SpunkDetector spunkDetector;
    private final StaleDetector staleDetector;
    private final AlarmEngine alarmEngine;
    private final AutoReconfigEngine autoReconfig;
    private final PrometheusExporter prometheusExporter;
    private final PrometheusMeterRegistry registry;
    private final MicroSleeContainer container;

    /** Registered custom counters: name→Counter */
    private final ConcurrentHashMap<String, TelemetryPort.Counter> customCounters = new ConcurrentHashMap<>();
    /** Registered custom gauges: name→Gauge */
    private final ConcurrentHashMap<String, TelemetryPort.Gauge> customGauges = new ConcurrentHashMap<>();
    private final AtomicBoolean autoReconfigEnabled = new AtomicBoolean(true);

    public MicrometerTelemetryPort(PrometheusMeterRegistry registry,
                                    MicroSleeContainer container) {
        this.registry = registry;
        this.container = container;
        this.sbbCollector = new SbbCollector();
        this.raCollector = new RaCollector();
        this.errorCollector = new ErrorCollector();
        this.resourceMonitor = new ResourceMonitor();
        this.spunkDetector = new SpunkDetector();
        this.staleDetector = new StaleDetector();
        this.alarmEngine = new AlarmEngine();
        this.autoReconfig = new AutoReconfigEngine(sbbCollector, errorCollector,
                resourceMonitor, staleDetector, alarmEngine, container);
        this.prometheusExporter = new PrometheusExporter(registry);
        registerCoreMetrics(registry);
        LOG.info("MicrometerTelemetryPort created");
    }

    /**
     * Register core telemetry metrics as Micrometer gauges so they appear in
     * Prometheus scrape output alongside custom app-defined metrics.
     * All gauges are passive (zero-CPU when not scraped).
     */
    private void registerCoreMetrics(MeterRegistry registry) {
        Tags empty = Tags.empty();

        // ── SBB pool metrics ──
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_entities_total",
                        sbbCollector, SbbCollector::getTotalEntities)
                .description("Total SBB entities created since start")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_entities_active",
                        sbbCollector, SbbCollector::getActiveEntities)
                .description("Currently active SBB entities (pool size)")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_events_total",
                        sbbCollector, SbbCollector::getEventsProcessed)
                .description("Total events processed by SBBs")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_events_per_second",
                        sbbCollector, SbbCollector::getEventsPerSecond)
                .description("Event throughput (60s sliding window)")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_errors_total",
                        sbbCollector, SbbCollector::getErrorCount)
                .description("Total SBB processing errors")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_spunks_total",
                        sbbCollector, SbbCollector::getSpunkCount)
                .description("Total spunk (anomaly) detections")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_stale_entities",
                        sbbCollector, SbbCollector::getStaleEntities)
                .description("Stale (idle) SBB entities")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_leaked_entities",
                        sbbCollector, SbbCollector::getLeakedEntities)
                .description("Leaked SBB entities")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_sbb_healthy",
                        sbbCollector, c -> c.isHealthy() ? 1.0 : 0.0)
                .description("SBB health indicator")
                .strongReference(true).tags(empty).register(registry);

        // ── Resource / JVM metrics ──
        io.micrometer.core.instrument.Gauge.builder("jainslee_heap_used_mb",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.heapUsedMb() : 0.0;
                        })
                .description("Heap memory used (MB)")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_heap_max_mb",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.heapMaxMb() : 0.0;
                        })
                .description("Heap memory max (MB)")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_heap_usage_percent",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? snap.heapUsagePercent() : 0.0;
                        })
                .description("Heap usage percentage")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_cpu_load_percent",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? snap.cpuLoad() : -1.0;
                        })
                .description("Process CPU load percentage")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_threads_active",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.activeThreads() : 0.0;
                        })
                .description("Active platform threads")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_threads_virtual",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.virtualThreads() : -1.0;
                        })
                .description("Virtual threads (SBB entity pool size)")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_gc_count",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.gcCount() : 0.0;
                        })
                .description("Total GC collections")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_gc_time_ms",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.gcTimeMs() : 0.0;
                        })
                .description("Total GC time (milliseconds)")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_open_fds",
                        resourceMonitor, rm -> {
                            var snap = rm.snapshot();
                            return snap != null ? (double) snap.openFileDescriptors() : -1.0;
                        })
                .description("Open file descriptors")
                .strongReference(true).tags(empty).register(registry);

        // ── RA metrics ──
        io.micrometer.core.instrument.Gauge.builder("jainslee_ra_events_fired_total",
                        raCollector, ra -> ra.stats().stream()
                                .mapToDouble(RaCollector.RaStats::eventsFired).sum())
                .description("Total events fired by all RAs")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_ra_commands_sent_total",
                        raCollector, ra -> ra.stats().stream()
                                .mapToDouble(RaCollector.RaStats::commandsSent).sum())
                .description("Total commands sent to all RAs")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_ra_failures_total",
                        raCollector, ra -> ra.stats().stream()
                                .mapToDouble(RaCollector.RaStats::failures).sum())
                .description("Total RA failures")
                .strongReference(true).tags(empty).register(registry);

        // ── Stale / Alarm metrics ──
        io.micrometer.core.instrument.Gauge.builder("jainslee_stale_tracked_entities",
                        staleDetector, StaleDetector::trackedEntityCount)
                .description("Entities tracked for staleness")
                .strongReference(true).tags(empty).register(registry);
        io.micrometer.core.instrument.Gauge.builder("jainslee_alarms_active",
                        alarmEngine, a -> (double) a.active().size())
                .description("Currently active alarms")
                .strongReference(true).tags(empty).register(registry);

        LOG.info("Registered 23 core Micrometer gauges");
    }

    public void start() {
        // Zero-CPU telemetry: no timer threads anywhere. ResourceMonitor
        // captures lazily on read (30s throttle); AutoReconfigEngine is
        // armed on JVM memory-threshold notifications + scrape piggyback.
        resourceMonitor.start(30, TimeUnit.SECONDS);
        autoReconfig.start(30, TimeUnit.SECONDS);
        LOG.info("MicrometerTelemetryPort started (zero-CPU: lazy capture + event-driven reconfig)");
    }

    public void stop() {
        resourceMonitor.stop();
        autoReconfig.stop();
        LOG.info("MicrometerTelemetryPort stopped");
    }

    @Override public SbbCollector sbbCollector() { return sbbCollector; }
    @Override public RaCollector raCollector() { return raCollector; }
    @Override public ErrorCollector errorCollector() { return errorCollector; }
    @Override public ResourceMonitor resourceMonitor() { return resourceMonitor; }
    @Override public SpunkDetector spunkDetector() { return spunkDetector; }
    @Override public StaleDetector staleDetector() { return staleDetector; }
    @Override public AlarmEngine alarmEngine() { return alarmEngine; }
    @Override public AutoReconfigEngine autoReconfig() { return autoReconfig; }

    @Override
    public boolean isAutoReconfigEnabled() {
        return autoReconfigEnabled.get();
    }

    @Override
    public void setAutoReconfigEnabled(boolean enabled) {
        autoReconfigEnabled.set(enabled);
        if (enabled) {
            autoReconfig.start(30, TimeUnit.SECONDS);
        } else {
            autoReconfig.stop();
        }
        LOG.info("Auto-reconfig {}abled", enabled ? "en" : "dis");
    }

    @Override
    public String scrape() {
        // Scrape is the natural heartbeat of a pull-based system — piggyback
        // the throttled auto-reconfig evaluation here instead of a timer.
        autoReconfig.maybeEvaluate();
        return prometheusExporter.scrape();
    }

    // ── Custom metrics (extensible) ──

    @Override
    public TelemetryPort.Counter customCounter(String name, String... tagPairs) {
        return customCounters.computeIfAbsent(name, k -> {
            var c = io.micrometer.core.instrument.Counter.builder(k)
                    .tags(toTags(tagPairs))
                    .register(registry);
            return new MicrometerCounterAdapter(c);
        });
    }

    @Override
    public TelemetryPort.Gauge customGauge(String name, Supplier<Number> supplier, String... tagPairs) {
        return customGauges.computeIfAbsent(name, k -> {
            io.micrometer.core.instrument.Gauge.builder(k, supplier, s -> s.get().doubleValue())
                    .tags(toTags(tagPairs))
                    .register(registry);
            return () -> supplier.get();
        });
    }

    /** Snapshot all custom metrics for the dashboard. */
    private List<TelemetryPort.CustomMetric> customMetricsSnapshot() {
        List<TelemetryPort.CustomMetric> list = new ArrayList<>();
        customCounters.forEach((name, c) -> {
            list.add(new TelemetryPort.CustomMetric(name, Map.of(), c.count(), null, false));
        });
        customGauges.forEach((name, g) -> {
            list.add(new TelemetryPort.CustomMetric(name, Map.of(), 0L, g.value(), true));
        });
        return Collections.unmodifiableList(list);
    }

    static Tags toTags(String... tagPairs) {
        if (tagPairs == null || tagPairs.length == 0) return Tags.empty();
        Tags tags = Tags.empty();
        for (int i = 0; i < tagPairs.length - 1; i += 2) {
            tags = tags.and(tagPairs[i], tagPairs[i + 1]);
        }
        return tags;
    }

    /** Delegates to Micrometer Counter (zero-CPU increment). */
    private record MicrometerCounterAdapter(io.micrometer.core.instrument.Counter delegate)
            implements TelemetryPort.Counter {
        @Override public void increment() { delegate.increment(); }
        @Override public void increment(long n) { delegate.increment(n); }
        @Override public long count() { return (long) delegate.count(); }
    }

    @Override
    public TelemetrySnapshot snapshot() {
        return new TelemetrySnapshot(
                sbbCollector.perType(),
                raCollector.stats(),
                resourceMonitor.snapshot(),
                errorCollector.recent(5),
                spunkDetector.activeSpunks(),
                staleDetector.detectStale(5 * 60_000L, 30 * 60_000L),
                alarmEngine.active(),
                autoReconfigEnabled.get(),
                customMetricsSnapshot()
        );
    }

    public MicroSleeContainer container() { return container; }

    /** Exposed for testing and vertx HTTP endpoint — the Prometheus registry. */
    public PrometheusMeterRegistry registry() { return registry; }
}
