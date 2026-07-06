package com.example.helloworld.quarkus.bootstrap;

import com.example.helloworld.quarkus.autonomous.AppAiAgent;
import com.example.helloworld.quarkus.autonomous.AppAutonomous;
import com.example.helloworld.quarkus.sbbs.HelloWorldSbb;
import com.example.helloworld.quarkus.sbbs.TelemetrySbb;
import com.example.helloworld.quarkus.telemetry.AppTelemetry;
import com.microjainslee.ai.AIAgentConfig;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.httpserver.collab.HttpServerSessionStore;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import com.microjainslee.telemetry.TelemetryPort;

import io.vertx.core.Vertx;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Bootstrap — wires ra-http-server + HelloWorldSBB + Telemetry into MicroSleeContainer.
 *
 * <p>Architecture:
 * <ul>
 *   <li>Quarkus port 8080 → serves index.html from META-INF/resources/ (UI)</li>
 *   <li>ra-http-server port 8081 → HTTP ingress → SBB event pipeline</li>
 *   <li>Telemetry on Vert.x port 8090 → /telemetry/ dashboard + /api/telemetry/*</li>
 * </ul>
 */
@ApplicationScoped
public final class HelloWorldBootstrap implements HelloWorldContext {

    private static final Logger LOG = LogManager.getLogger(HelloWorldBootstrap.class);

    @Inject
    MicroSleeContainer container;

    @Inject
    Vertx vertx;

    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "http.ra.port", defaultValue = "8081")
    int httpRaPort;

    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "microjainslee.telemetry.enabled", defaultValue = "true")
    boolean telemetryEnabled;

    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "microjainslee.autonomous.enabled", defaultValue = "true")
    boolean autonomousEnabled;

    @Inject
    org.eclipse.microprofile.config.Config mpConfig;

    private final ConcurrentHashMap<String, SessionRecord> sessions = new ConcurrentHashMap<>();
    private volatile HttpServerRaEndpoint httpEndpoint;
    private volatile TelemetryPort telemetry;

    // ── Drop-in modules (the app template) — each one is OPTIONAL. ──
    // An app can run with nothing but the core container; telemetry is the
    // data source, autonomous heals, the AI agent advises/acts on top.
    private final AppTelemetry appTelemetry = new AppTelemetry();
    private final AppAutonomous appAutonomous = new AppAutonomous();
    private final AppAiAgent appAiAgent = new AppAiAgent();

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }

        // ── 1. Telemetry (optional): collection + Prometheus + log sink + GUI ──
        if (telemetryEnabled) {
            telemetry = appTelemetry.install(container, vertx);
        } else {
            LOG.info("telemetry module disabled (microjainslee.telemetry.enabled=false)");
        }

        // ── 2. Autonomous (optional): memory guardian + health evaluator ──
        //     The health evaluator needs telemetry as its data source.
        if (autonomousEnabled && telemetry != null) {
            appAutonomous.install(container, telemetry);
            appAutonomous.mountRoutes(appTelemetry.router());
        } else if (autonomousEnabled) {
            LOG.warn("autonomous module skipped: it needs telemetry as data source");
        } else {
            LOG.info("autonomous module disabled (microjainslee.autonomous.enabled=false)");
        }

        // ── 3. AI agent (optional): LLM advisor over telemetry + guardian ──
        //     Starts paused when microjainslee.ai.enabled=false; the GUI
        //     toggle can enable it at runtime without a restart.
        if (telemetry != null) {
            AIAgentConfig aiConfig = AIAgentConfig.fromProperties(key ->
                    mpConfig.getOptionalValue(key, String.class).orElse(null));
            appAiAgent.install(aiConfig, telemetry,
                    autonomousEnabled ? appAutonomous.guardian() : null);
            appAiAgent.mountRoutes(appTelemetry.router());
        }

        // ── 4. Business wiring: HTTP ingress → HelloWorld SBB pipeline ──
        container.registerSbbType(HelloWorldSbb.class,
                () -> new HelloWorldSbb(container, this));
        container.registerSbbType(TelemetrySbb.class,
                () -> new TelemetrySbb(container, telemetry));
        container.createIesDispatcher();
        container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");
        wireHttpRa();
        LOG.info("HelloWorld bootstrap complete. UI at :{}, RA at :{}",
                quarkusPort(), httpRaPort);
    }

    @PreDestroy
    void shutdown() {
        appAiAgent.close();
        appAutonomous.close();
        appTelemetry.close();
        if (httpEndpoint != null) {
            httpEndpoint.deactivate();
        }
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }

    // ── HelloWorldContext ──

    @Override
    public MicroSleeContainer container() {
        return container;
    }

    @Override
    public void completeSession(String sessionId, String responseText) {
        sessions.put(sessionId, new SessionRecord(sessionId, "COMPLETED", responseText, null));
        LOG.debug("Session {} completed with response", sessionId);
    }

    @Override
    public void failSession(String sessionId, String message) {
        sessions.put(sessionId, new SessionRecord(sessionId, "FAILED", null, message));
        LOG.debug("Session {} failed: {}", sessionId, message);
    }

    @Override
    public String httpEntityId(String sessionId) {
        return "HelloWorld/" + sessionId;
    }

    // ── wiring ──

    private void wireHttpRa() {
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpRaPort);

        httpEndpoint = new HttpServerRaEndpoint(ra);
        httpEndpoint.setPort(httpRaPort);

        container.registerRa(httpEndpoint, httpEndpoint);
        LOG.info("ra-http-server registered on port {}", httpRaPort);
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) {
                return;
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private int quarkusPort() {
        return Integer.parseInt(System.getProperty("quarkus.http.port", "8080"));
    }

    // ── inner types ──

    record SessionRecord(String sessionId, String status,
                         String responseText, String errorMessage) {}

    static final class InMemorySessionStore implements HttpServerSessionStore {
        private final ConcurrentHashMap<String, SessionRecord> sessions;

        InMemorySessionStore(ConcurrentHashMap<String, SessionRecord> s) {
            this.sessions = s;
        }

        @Override
        public SessionSnapshot get(String sessionId) {
            SessionRecord r = sessions.get(sessionId);
            if (r == null) {
                return null;
            }
            return new SessionSnapshot() {
                @Override public String getStatus() { return r.status(); }
                @Override public String getResponseText() { return r.responseText(); }
                @Override public String getErrorMessage() { return r.errorMessage(); }
            };
        }
    }
}
