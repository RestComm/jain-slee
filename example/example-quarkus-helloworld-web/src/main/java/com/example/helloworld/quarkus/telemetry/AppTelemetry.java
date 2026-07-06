package com.example.helloworld.quarkus.telemetry;

import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.prometheus.PrometheusRaEndpoint;
import com.microjainslee.ra.prometheus.PrometheusResourceAdaptor;
import com.microjainslee.telemetry.MicrometerTelemetryPort;
import com.microjainslee.telemetry.TelemetryPort;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Observability module — the drop-in {@code telemetry/} directory for any
 * micro-jainslee app. Copy this package, change the ports, done.
 *
 * <p>One call to {@link #install(MicroSleeContainer, Vertx)} wires the entire
 * zero-CPU observability stack:</p>
 * <ol>
 *   <li><b>MicrometerTelemetryPort</b> — passive AtomicLong collectors, bound
 *       to the container and fed by the EventRouter (no polling).</li>
 *   <li><b>Prometheus</b> — live pull-based metrics via the exporter RA
 *       ({@code :9090/metrics}) and the Micrometer scrape endpoint.</li>
 *   <li><b>{@link TelemetryLogSink}</b> — durable, batched JSON-lines log for
 *       downstream shipping (Loki / ES / Splunk) — native-image friendly.</li>
 *   <li><b>Steampunk dashboard + REST API</b> — mounted on a dedicated Vert.x
 *       port ({@code :8090/telemetry}).</li>
 * </ol>
 *
 * <p>The {@link TelemetryPort} it returns is the single data contract that the
 * {@code autonomous/} module consumes to make healing decisions.</p>
 */
public final class AppTelemetry implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AppTelemetry.class);

    public static final int DASHBOARD_PORT = 8090;
    public static final int PROMETHEUS_PORT = 9090;

    private MicrometerTelemetryPort telemetry;
    private TelemetryLogSink logSink;
    private PrometheusRaEndpoint prometheusEndpoint;
    private Router router;

    /** Wire and start the full telemetry stack. Returns the shared port. */
    public TelemetryPort install(MicroSleeContainer container, Vertx vertx) {
        // 1. Passive collection engine. start() arms the zero-CPU resource
        //    monitor + auto-reconfig (lazy capture, no timer threads).
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        telemetry = new MicrometerTelemetryPort(registry, container);
        telemetry.start();

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

        // 4. Dashboard + REST API on a dedicated Vert.x port.
        mountRoutes(vertx);

        LOG.info("[telemetry] observability stack armed — dashboard http://localhost:{}/telemetry",
                DASHBOARD_PORT);
        return telemetry;
    }

    public TelemetryPort port() {
        return telemetry;
    }

    /**
     * The live dashboard router. Other modules (e.g. {@code autonomous/}) may
     * add routes to it even after the server is listening — Vert.x consults the
     * router per request.
     */
    public Router router() {
        return router;
    }

    private void mountRoutes(Vertx vertx) {
        router = Router.router(vertx);

        // Steampunk dashboard UI (served from jainslee-telemetry-vertx JAR).
        router.route("/telemetry/*").handler(StaticHandler.create("META-INF/resources"));
        router.route("/telemetry").handler(ctx -> ctx.reroute("/telemetry/index.html"));

        router.get("/api/telemetry/snapshot").handler(ctx -> ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(Json.encode(telemetry.snapshot())));

        router.get("/api/telemetry/metrics").handler(ctx -> ctx.response()
                .putHeader("Content-Type", "text/plain")
                .end(telemetry.scrape()));

        router.get("/api/telemetry/alarms").handler(ctx -> ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(Json.encode(telemetry.alarmEngine().active())));

        router.post("/api/telemetry/alarms/:id/clear").handler(ctx -> {
            telemetry.alarmEngine().clear(ctx.pathParam("id"));
            ctx.response().setStatusCode(200).end("{\"status\":\"ok\"}");
        });

        router.post("/api/telemetry/config").handler(ctx -> {
            var body = ctx.body().asJsonObject();
            if (body != null && body.containsKey("autoReconfig")) {
                telemetry.setAutoReconfigEnabled(body.getBoolean("autoReconfig"));
            }
            ctx.response().setStatusCode(200).end("{\"status\":\"ok\"}");
        });

        vertx.createHttpServer().requestHandler(router).listen(DASHBOARD_PORT, ar -> {
            if (ar.succeeded()) {
                LOG.info("[telemetry] dashboard listening on :{}", DASHBOARD_PORT);
            } else {
                LOG.warn("[telemetry] dashboard failed to bind :{} — {}",
                        DASHBOARD_PORT, ar.cause().getMessage());
            }
        });
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
