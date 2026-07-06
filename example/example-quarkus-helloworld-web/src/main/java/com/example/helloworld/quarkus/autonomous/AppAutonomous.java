package com.example.helloworld.quarkus.autonomous;

import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.TelemetryPort;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Self-healing module — the drop-in {@code autonomous/} directory for any
 * micro-jainslee app. Copy this package, tune the watermarks, done.
 *
 * <p>Two layers, both zero-polling:</p>
 * <ol>
 *   <li><b>{@link AutonomousGuardian}</b> (from {@code jainslee-autonomous}) —
 *       a thread-less, JVM-notification-driven memory guardian. It arms the
 *       tenured-pool collection-usage threshold and, when a GC leaves the heap
 *       above the watermark, trims caches, compacts off-heap arenas and runs a
 *       guarded GC. Idle node → zero CPU, zero allocation.</li>
 *   <li><b>{@link HealthEvaluator}</b> — an app-level scorer that turns the
 *       telemetry snapshot into a GREEN/AMBER/RED verdict, raises alarms on
 *       transitions and pokes the guardian on RED.</li>
 * </ol>
 *
 * <p>Exposes {@code GET /api/autonomous/health} on the telemetry Vert.x port.</p>
 */
public final class AppAutonomous implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AppAutonomous.class);

    private AutonomousGuardian guardian;
    private HealthEvaluator health;

    /**
     * Wire and start the self-healing stack.
     *
     * @param container the running container (relief participants attach to it)
     * @param telemetry the bound telemetry port (health data source)
     */
    public void install(MicroSleeContainer container, TelemetryPort telemetry) {
        // 1. Memory guardian — no threads, driven by JVM memory notifications.
        guardian = new AutonomousGuardian()
                .attach(container)
                .watermarks(0.75, 0.88, 0.96)
                .onEmergency(level -> {
                    LOG.error("[autonomous] EMERGENCY heap pressure ({}), shedding load", level);
                    if (telemetry != null) {
                        telemetry.alarmEngine().fire(AlarmEngine.TelemetryAlarmLevel.FATAL,
                                "guardian", "near-OOM emergency: " + level, null);
                    }
                });
        guardian.start();

        // 2. Holistic health evaluator — one daemon VT, edge-triggered alarms.
        health = new HealthEvaluator(telemetry, guardian);
        health.start();

        LOG.info("[autonomous] self-healing stack armed (guardian + health evaluator)");
    }

    /**
     * Contribute the health endpoint to the telemetry dashboard router. Vert.x
     * allows adding routes after the server is listening, so this reuses the
     * telemetry port rather than opening a second server.
     */
    public void mountRoutes(Router router) {
        router.get("/api/autonomous/health").handler(ctx -> {
            var report = health.latest();
            JsonArray reasons = new JsonArray();
            report.reasons().forEach(reasons::add);
            JsonObject body = new JsonObject()
                    .put("status", report.status().name())
                    .put("heapPct", report.heapPct())
                    .put("cpuLoad", report.cpuLoad())
                    .put("errors", report.errors())
                    .put("spunks", report.spunks())
                    .put("reasons", reasons)
                    .put("guardianLevel", guardian.lastLevel().name())
                    .put("reliefRuns", guardian.reliefRunCount())
                    .put("ts", report.timestamp());
            ctx.response().putHeader("Content-Type", "application/json").end(body.encode());
        });
        LOG.info("[autonomous] health endpoint mounted at /api/autonomous/health");
    }

    public HealthEvaluator health() {
        return health;
    }

    @Override
    public void close() {
        if (health != null) {
            health.close();
        }
        if (guardian != null) {
            guardian.stop();
        }
    }
}
