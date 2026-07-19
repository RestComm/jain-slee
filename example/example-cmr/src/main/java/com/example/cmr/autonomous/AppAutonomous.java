package com.example.cmr.autonomous;

import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.TelemetryPort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Self-healing module — the drop-in {@code autonomous/} directory for any
 * micro-jainslee app. Copy this package, tune the watermarks, done.
 *
 * <p>Two layers, both zero-polling:</p>
 * <ol>
 *   <li><b>{@link AutonomousGuardian}</b> (from {@code jainslee-autonomous}) —
 *       a thread-less, reactive memory guardian. When the heap sits above the
 *       watermark it trims caches, compacts off-heap arenas and runs a guarded
 *       GC. Idle node → zero CPU, zero allocation.</li>
 *   <li><b>{@link HealthEvaluator}</b> — an app-level scorer that turns the
 *       telemetry snapshot into a GREEN/AMBER/RED verdict, raises alarms on
 *       transitions and pokes the guardian on RED.</li>
 * </ol>
 *
 * <p>The {@code GET /api/autonomous/health} endpoint is served by
 * {@code MonitorHandler} through the app's {@code ra-http-server}; this module
 * only supplies the JSON via {@link #healthJson()} — no Vert.x, no HTTP server.</p>
 */
public final class AppAutonomous implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AppAutonomous.class);
    private static final ObjectMapper JSON = new ObjectMapper();

    private AutonomousGuardian guardian;
    private HealthEvaluator health;

    /**
     * Wire and start the self-healing stack.
     *
     * @param container the running container (relief participants attach to it)
     * @param telemetry the bound telemetry port (health data source)
     */
    public void install(MicroSleeContainer container, TelemetryPort telemetry) {
        // 1. Memory guardian — no threads, driven reactively by heap pressure.
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
     * Current health as a JSON string — served by {@code MonitorHandler} at
     * {@code /api/autonomous/health}. Reuses the app's single HTTP RA rather
     * than opening a second server.
     */
    public String healthJson() {
        var report = health.latest();
        ObjectNode body = JSON.createObjectNode();
        body.put("status", report.status().name());
        body.put("heapPct", report.heapPct());
        body.put("cpuLoad", report.cpuLoad());
        body.put("errors", report.errors());
        body.put("spunks", report.spunks());
        ArrayNode reasons = body.putArray("reasons");
        report.reasons().forEach(reasons::add);
        body.put("guardianLevel", guardian.lastLevel().name());
        body.put("reliefRuns", guardian.reliefRunCount());
        body.put("ts", report.timestamp());
        return body.toString();
    }

    public HealthEvaluator health() {
        return health;
    }

    /** The wired guardian — handed to the AI agent as its control surface. */
    public AutonomousGuardian guardian() {
        return guardian;
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
