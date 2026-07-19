package com.example.helloworld.quarkus.telemetry;

import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.prometheus.PrometheusRaEndpoint;
import com.microjainslee.ra.prometheus.PrometheusResourceAdaptor;
import com.microjainslee.telemetry.MicrometerTelemetryPort;
import com.microjainslee.telemetry.TelemetryPort;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Observability module — the drop-in {@code telemetry/} directory for any
 * micro-jainslee app. Copy this package, change the port, done.
 *
 * <p>One call to {@link #install(MicroSleeContainer)} wires the entire
 * zero-CPU observability stack:</p>
 * <ol>
 *   <li><b>MicrometerTelemetryPort</b> — passive AtomicLong collectors, bound
 *       to the container and fed by the EventRouter (no polling).</li>
 *   <li><b>Prometheus</b> — live pull-based metrics via the exporter RA
 *       ({@code :9090/metrics}) and the Micrometer scrape endpoint.</li>
 *   <li><b>{@link TelemetryLogSink}</b> — durable, batched JSON-lines log for
 *       downstream shipping (Loki / ES / Splunk) — native-image friendly.</li>
 * </ol>
 *
 * <p>The steampunk dashboard + REST API are <b>not</b> served here: they ride
 * the app's single {@code ra-http-server} via {@code MonitorHandler}, so this
 * module never touches Vert.x or opens a second HTTP port. The
 * {@link TelemetryPort} it returns feeds the dashboard and
 * {@code /api/telemetry/*} endpoints.</p>
 */
public final class AppTelemetry implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AppTelemetry.class);

    public static final int PROMETHEUS_PORT = 9090;

    private MicrometerTelemetryPort telemetry;
    private TelemetryLogSink logSink;
    private PrometheusRaEndpoint prometheusEndpoint;

    /** Wire and start the full telemetry stack. Returns the shared port. */
    public TelemetryPort install(MicroSleeContainer container) {
        // 1. Passive collection engine. start() arms the zero-CPU resource
        //    monitor + auto-reconfig (lazy capture, no timer threads).
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        telemetry = new MicrometerTelemetryPort(registry, container);
        telemetry.start();

        // 1b. Feed the passive collectors from the dispatch path: every
        //     onEvent delivery reports throughput/latency/errors/heartbeats.
        //     Cost when telemetry is absent: one volatile read per event.
        container.getEventRouter().setDispatchObserver(
                new com.microjainslee.telemetry.TelemetryDispatchObserver(telemetry));

        // 2. Durable batched log sink (complements Prometheus, survives restart).
        logSink = new TelemetryLogSink(telemetry);
        logSink.start();

        // 3. Prometheus exporter RA — pull-based scrape target. The endpoint
        //    implements both RaEndpointPort and RaCommandPort.
        var promRa = new PrometheusResourceAdaptor();
        promRa.setPort(PROMETHEUS_PORT);
        prometheusEndpoint = new PrometheusRaEndpoint(promRa);
        container.registerRa(prometheusEndpoint, prometheusEndpoint);
        LOG.info("[telemetry] Prometheus exporter RA on :{}", promRa.port());

        LOG.info("[telemetry] observability stack armed — dashboard served via "
                + "ra-http-server at /telemetry");
        return telemetry;
    }

    public TelemetryPort port() {
        return telemetry;
    }

    @Override
    public void close() {
        if (logSink != null) {
            logSink.close();
        }
        if (prometheusEndpoint != null) {
            prometheusEndpoint.deactivate();
        }
    }
}
