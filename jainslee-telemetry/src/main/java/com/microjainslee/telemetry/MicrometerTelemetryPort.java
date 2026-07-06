package com.microjainslee.telemetry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microjainslee.core.MicroSleeContainer;

/**
 * Full-featured TelemetryPort implementation backed by Micrometer.
 * Passive collection only — callbacks from EventRouter/RA endpoints.
 * Single daemon VT for periodic evaluation (ResourceMonitor + AutoReconfigEngine).
 */
public final class MicrometerTelemetryPort implements TelemetryPort {

    private static final Logger LOG = LogManager.getLogger(MicrometerTelemetryPort.class);

    private final SbbCollector sbbCollector;
    private final RaCollector raCollector;
    private final ErrorCollector errorCollector;
    private final ResourceMonitor resourceMonitor;
    private final SpunkDetector spunkDetector;
    private final StaleDetector staleDetector;
    private final AlarmEngine alarmEngine;
    private final AutoReconfigEngine autoReconfig;
    private final PrometheusExporter prometheusExporter;
    private final MicroSleeContainer container;

    private final AtomicBoolean autoReconfigEnabled = new AtomicBoolean(true);


    public MicrometerTelemetryPort(PrometheusMeterRegistry registry,
                                    MicroSleeContainer container) {
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

    // ── TelemetryPort ──

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
                autoReconfigEnabled.get()
        );
    }

    public MicroSleeContainer container() { return container; }
}

        LOG.info("MicrometerTelemetryPort stopped");
    }
