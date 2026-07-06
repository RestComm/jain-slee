package com.microjainslee.telemetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import io.micrometer.core.instrument.Tags;
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
        LOG.info("MicrometerTelemetryPort created");
    }

    public void start() {
        resourceMonitor.start(30, TimeUnit.SECONDS);
        autoReconfig.start(30, TimeUnit.SECONDS);
        LOG.info("MicrometerTelemetryPort started (resmon+autoreconf @30s)");
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
            var g = io.micrometer.core.instrument.Gauge.builder(k, supplier, s -> s.get().doubleValue())
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

    private static Tags toTags(String... tagPairs) {
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
}
