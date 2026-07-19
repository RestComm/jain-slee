package com.example.cmr.autonomous;

import com.microjainslee.ai.AIAgentConfig;
import com.microjainslee.ai.AIAgentEngine;
import com.microjainslee.ai.OpenAiCompatAdvisor;
import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.telemetry.TelemetryPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AI agent module — the optional third leg of the {@code autonomous/}
 * directory. Reads {@code microjainslee.ai.*} from application.properties,
 * wires the {@link AIAgentEngine} to the telemetry snapshot (data source) and
 * the {@link AutonomousGuardian} (control surface).
 *
 * <p><b>Optionality:</b> requires telemetry (its data source); tolerates a
 * missing guardian. When {@code microjainslee.ai.enabled=false} the engine
 * still starts <i>paused</i>, so the GUI toggle can switch it on at runtime
 * without a restart. If no API key is configured the agent reports
 * {@code available:false} and does nothing.</p>
 *
 * <p>The {@code /api/ai/*} REST surface the 🤖 tab talks to is served by
 * {@code MonitorHandler} through the app's {@code ra-http-server}; this module
 * only owns the engine — no Vert.x, no HTTP server.</p>
 */
public final class AppAiAgent implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AppAiAgent.class);

    private AIAgentEngine engine;

    /**
     * Wire and start the agent loop (paused when {@code enabled=false}).
     *
     * @param config    parsed {@code microjainslee.ai.*} configuration
     * @param container the running container — control surface for
     *                  {@code RELEASE_ENTITY} (leaked-entity healing)
     * @param telemetry required — the snapshot data source
     * @param guardian  nullable — apps without the autonomous module still
     *                  get analysis, alarms and reports
     */
    public void install(AIAgentConfig config, MicroSleeContainer container,
                        TelemetryPort telemetry, AutonomousGuardian guardian) {
        engine = new AIAgentEngine(config, new OpenAiCompatAdvisor(config),
                telemetry, guardian)
                .entityReleaser(container::releaseEntity);
        engine.start();
        LOG.info("[ai] agent installed: {}", config);
    }

    public AIAgentEngine engine() {
        return engine;
    }

    @Override
    public void close() {
        if (engine != null) {
            engine.close();
        }
    }
}
