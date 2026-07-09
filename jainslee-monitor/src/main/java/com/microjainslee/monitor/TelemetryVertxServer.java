package com.microjainslee.telemetry.vertx;

import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.MicrometerTelemetryPort;
import com.microjainslee.telemetry.TelemetryPort;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Vert.x HTTP server exposing telemetry REST endpoints backed by
 * {@link MicrometerTelemetryPort}.
 *
 * <h3>Endpoints</h3>
 * <ul>
 *   <li>{@code GET /metrics} — Prometheus OpenMetrics text format</li>
 *   <li>{@code GET /api/telemetry/snapshot} — JSON snapshot of all collectors</li>
 *   <li>{@code GET /api/telemetry/alarms} — active alarms (JSON)</li>
 *   <li>{@code GET /api/telemetry/alarms/history?minutes=60} — alarm history</li>
 *   <li>{@code GET /api/telemetry/health} — health status (JSON)</li>
 * </ul>
 *
 * <p>Zero-CPU at idle: Vert.x event-loop thread blocks only on actual
 * HTTP requests; no background polling.</p>
 */
public final class TelemetryVertxServer implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(TelemetryVertxServer.class);

    private final Vertx vertx;
    private final HttpServer server;
    private final MicrometerTelemetryPort telemetryPort;
    private final int port;

    public TelemetryVertxServer(MicrometerTelemetryPort telemetryPort, int port) {
        this.telemetryPort = telemetryPort;
        this.port = port;
        this.vertx = Vertx.vertx();
        this.server = vertx.createHttpServer();
    }

    /** Start the HTTP server and return this for method chaining. */
    public TelemetryVertxServer start() {
        Router router = Router.router(vertx);

        // ── Prometheus scrape endpoint ────────────────────────────────
        router.get("/metrics").handler(this::handleMetrics);

        // ── JSON snapshot ─────────────────────────────────────────────
        router.get("/api/telemetry/snapshot").handler(this::handleSnapshot);

        // ── Alarms ────────────────────────────────────────────────────
        router.get("/api/telemetry/alarms").handler(this::handleAlarms);
        router.get("/api/telemetry/alarms/history").handler(this::handleAlarmHistory);

        // ── Health ────────────────────────────────────────────────────
        router.get("/api/telemetry/health").handler(this::handleHealth);

        // ── Custom metrics (list names) ───────────────────────────────
        router.get("/api/telemetry/custom").handler(this::handleCustomMetrics);

        server.requestHandler(router).listen(port, ar -> {
            if (ar.succeeded()) {
                LOG.info("TelemetryVertxServer listening on port {}", port);
            } else {
                LOG.error("TelemetryVertxServer failed to start on port {}: {}",
                        port, ar.cause().getMessage());
            }
        });
        return this;
    }

    public int port() { return port; }

    @Override
    public void close() {
        server.close(ar -> {
            if (ar.succeeded()) {
                LOG.info("TelemetryVertxServer stopped");
            }
        });
        vertx.close();
    }

    // ── Handlers ─────────────────────────────────────────────────────

    private void handleMetrics(RoutingContext ctx) {
        try {
            String scrape = telemetryPort.scrape();
            ctx.response()
                    .putHeader("Content-Type", "text/plain; version=0.0.4; charset=utf-8")
                    .end(scrape);
        } catch (Exception e) {
            LOG.warn("Metrics scrape failed: {}", e.getMessage());
            ctx.response().setStatusCode(500).end("Scrape error: " + e.getMessage());
        }
    }

    private void handleSnapshot(RoutingContext ctx) {
        try {
            TelemetryPort.TelemetrySnapshot snap = telemetryPort.snapshot();
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Json.encode(toSnapshotJson(snap)));
        } catch (Exception e) {
            ctx.response().setStatusCode(500).end("Snapshot error: " + e.getMessage());
        }
    }

    private void handleAlarms(RoutingContext ctx) {
        var alarms = telemetryPort.alarmEngine().active();
        JsonArray arr = new JsonArray();
        for (var a : alarms) {
            arr.add(alarmToJson(a));
        }
        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(arr.encode());
    }

    private void handleAlarmHistory(RoutingContext ctx) {
        int minutes = 60;
        try {
            String m = ctx.request().getParam("minutes");
            if (m != null) minutes = Integer.parseInt(m);
        } catch (NumberFormatException ignored) { }

        var alarms = telemetryPort.alarmEngine().history(minutes);
        JsonArray arr = new JsonArray();
        for (var a : alarms) {
            arr.add(alarmToJson(a));
        }
        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(arr.encode());
    }

    private void handleHealth(RoutingContext ctx) {
        var snap = telemetryPort.resourceMonitor().snapshot();
        boolean healthy = telemetryPort.sbbCollector().isHealthy();
        int activeAlarms = telemetryPort.alarmEngine().active().size();

        JsonObject health = new JsonObject()
                .put("status", healthy && activeAlarms == 0 ? "UP" : "DEGRADED")
                .put("sbbHealthy", healthy)
                .put("activeAlarms", activeAlarms)
                .put("heapUsagePercent", snap != null ? snap.heapUsagePercent() : -1.0)
                .put("cpuLoadPercent", snap != null ? snap.cpuLoad() : -1.0)
                .put("activeThreads", snap != null ? snap.activeThreads() : -1)
                .put("autoReconfigEnabled", telemetryPort.isAutoReconfigEnabled());

        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(health.encode());
    }

    private void handleCustomMetrics(RoutingContext ctx) {
        var custom = telemetryPort.snapshot().customMetrics();
        JsonArray arr = new JsonArray();
        for (var cm : custom) {
            JsonObject obj = new JsonObject()
                    .put("name", cm.name())
                    .put("isGauge", cm.isGauge())
                    .put("tags", cm.tags());
            if (cm.isGauge()) {
                obj.put("value", cm.gaugeValue());
            } else {
                obj.put("value", cm.counterValue());
            }
            arr.add(obj);
        }
        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(arr.encode());
    }

    // ── JSON helpers ──────────────────────────────────────────────────

    static JsonObject toSnapshotJson(TelemetryPort.TelemetrySnapshot snap) {
        JsonObject json = new JsonObject();

        // SBBs
        JsonArray sbbs = new JsonArray();
        for (var s : snap.sbbs()) {
            sbbs.add(new JsonObject()
                    .put("sbbType", s.sbbType())
                    .put("active", s.active())
                    .put("errors", s.errors())
                    .put("spunks", s.spunks())
                    .put("eps", s.eps())
                    .put("p99us", s.p99us()));
        }
        json.put("sbbs", sbbs);

        // RAs
        JsonArray ras = new JsonArray();
        for (var r : snap.ras()) {
            ras.add(new JsonObject()
                    .put("raName", r.raName())
                    .put("state", r.state())
                    .put("port", r.port())
                    .put("eventsFired", r.eventsFired())
                    .put("commandsSent", r.commandsSent())
                    .put("failures", r.failures()));
        }
        json.put("ras", ras);

        // Resources
        var res = snap.resources();
        if (res != null) {
            json.put("resources", new JsonObject()
                    .put("heapUsedMb", res.heapUsedMb())
                    .put("heapMaxMb", res.heapMaxMb())
                    .put("heapUsagePercent", res.heapUsagePercent())
                    .put("cpuLoad", res.cpuLoad())
                    .put("activeThreads", res.activeThreads())
                    .put("virtualThreads", res.virtualThreads())
                    .put("gcCount", res.gcCount())
                    .put("gcTimeMs", res.gcTimeMs())
                    .put("openFileDescriptors", res.openFileDescriptors())
                    .put("timestampMillis", res.timestampMillis()));
        }

        // Errors
        JsonArray errors = new JsonArray();
        for (var e : snap.recentErrors()) {
            errors.add(new JsonObject()
                    .put("sbbType", e.sbbType())
                    .put("entityId", e.entityId())
                    .put("exceptionType", e.exceptionType())
                    .put("message", e.message())
                    .put("timestamp", e.timestamp()));
        }
        json.put("recentErrors", errors);

        // Spunks
        JsonArray spunks = new JsonArray();
        for (var s : snap.spunks()) {
            spunks.add(new JsonObject()
                    .put("sbbType", s.sbbType())
                    .put("entityId", s.entityId())
                    .put("reason", s.reason())
                    .put("timestamp", s.timestamp()));
        }
        json.put("spunks", spunks);

        // Stale
        JsonArray stales = new JsonArray();
        for (var s : snap.stales()) {
            stales.add(new JsonObject()
                    .put("entityId", s.entityId())
                    .put("sbbType", s.sbbType())
                    .put("idleDurationMs", s.idleDurationMs())
                    .put("leaked", s.leaked()));
        }
        json.put("stales", stales);

        // Active alarms
        JsonArray alarms = new JsonArray();
        for (var a : snap.activeAlarms()) {
            alarms.add(alarmToJson(a));
        }
        json.put("activeAlarms", alarms);

        json.put("autoReconfigEnabled", snap.autoReconfigEnabled());

        return json;
    }

    static JsonObject alarmToJson(AlarmEngine.Alarm a) {
        return new JsonObject()
                .put("id", a.id())
                .put("level", a.level().name())
                .put("source", a.source())
                .put("message", a.message())
                .put("timestamp", a.timestamp())
                .put("ctx", a.ctx())
                .put("cleared", a.cleared());
    }
}
