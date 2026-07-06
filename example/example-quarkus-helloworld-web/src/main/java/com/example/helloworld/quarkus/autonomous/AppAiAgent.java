package com.example.helloworld.quarkus.autonomous;

import com.microjainslee.ai.AIAgentConfig;
import com.microjainslee.ai.AIAgentEngine;
import com.microjainslee.ai.AIAnalysis;
import com.microjainslee.ai.AIMode;
import com.microjainslee.ai.OpenAiCompatAdvisor;
import com.microjainslee.ai.ReportAudience;
import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.telemetry.TelemetryPort;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AI agent module — the optional third leg of the {@code autonomous/}
 * directory. Reads {@code microjainslee.ai.*} from application.properties,
 * wires the {@link AIAgentEngine} to the telemetry snapshot (data source) and
 * the {@link AutonomousGuardian} (control surface), and mounts the REST
 * surface the Monitoring Window's 🤖 tab talks to.
 *
 * <p><b>Optionality:</b> requires telemetry (its data source); tolerates a
 * missing guardian. When {@code microjainslee.ai.enabled=false} the engine
 * still starts <i>paused</i>, so the GUI toggle can switch it on at runtime
 * without a restart. If no API key is configured the agent reports
 * {@code available:false} and does nothing.</p>
 *
 * <h3>REST surface (mounted on the monitoring router)</h3>
 * <pre>
 * GET  /api/ai/status                    engine counters + mode + availability
 * GET  /api/ai/analysis                  latest AIAnalysis (204 if none yet)
 * POST /api/ai/analyze                   force one cycle now, returns analysis
 * GET  /api/ai/report?audience=user|dev|boss   live report, three voices
 * POST /api/ai/config {enabled?, mode?}  runtime on/off + trust ladder
 * </pre>
 */
public final class AppAiAgent implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AppAiAgent.class);

    private AIAgentEngine engine;

    /**
     * Wire and start the agent loop (paused when {@code enabled=false}).
     *
     * @param config    parsed {@code microjainslee.ai.*} configuration
     * @param telemetry required — the snapshot data source
     * @param guardian  nullable — apps without the autonomous module still
     *                  get analysis, alarms and reports
     */
    public void install(AIAgentConfig config, TelemetryPort telemetry,
                        AutonomousGuardian guardian) {
        engine = new AIAgentEngine(config, new OpenAiCompatAdvisor(config),
                telemetry, guardian);
        engine.start();
        LOG.info("[ai] agent installed: {}", config);
    }

    public AIAgentEngine engine() {
        return engine;
    }

    /** Contribute the /api/ai/* routes to the monitoring router. */
    public void mountRoutes(Router router) {
        router.get("/api/ai/status").handler(ctx -> ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(Json.encode(engine.status())));

        router.get("/api/ai/analysis").handler(ctx -> {
            AIAnalysis last = engine.lastAnalysis();
            if (last == null) {
                ctx.response().setStatusCode(204).end();
            } else {
                ctx.response().putHeader("Content-Type", "application/json")
                        .end(Json.encode(last));
            }
        });

        // Blocking: one LLM round-trip — keep it off the event loop.
        router.post("/api/ai/analyze").blockingHandler(ctx -> {
            AIAnalysis analysis = engine.analyzeNow();
            if (analysis == null) {
                ctx.response().setStatusCode(503)
                        .end("{\"error\":\"AI unavailable — check api-key and endpoint\"}");
            } else {
                ctx.response().putHeader("Content-Type", "application/json")
                        .end(Json.encode(analysis));
            }
        });

        // Blocking: one LLM round-trip per report.
        router.get("/api/ai/report").blockingHandler(ctx -> {
            ReportAudience audience = ReportAudience.parse(ctx.request().getParam("audience"));
            String report = engine.report(audience);
            ctx.response().putHeader("Content-Type", "text/plain; charset=utf-8").end(report);
        });

        router.post("/api/ai/config").handler(ctx -> {
            JsonObject body = ctx.body().asJsonObject();
            if (body != null) {
                if (body.containsKey("enabled")) {
                    engine.setEnabled(body.getBoolean("enabled"));
                }
                if (body.containsKey("mode")) {
                    engine.setMode(AIMode.parse(body.getString("mode")));
                }
            }
            ctx.response().setStatusCode(200).end("{\"status\":\"ok\"}");
        });

        LOG.info("[ai] REST surface mounted at /api/ai/*");
    }

    @Override
    public void close() {
        if (engine != null) {
            engine.close();
        }
    }
}
