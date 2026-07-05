package com.example.helloworld.quarkus.bootstrap;

import com.example.helloworld.quarkus.sbbs.HelloWorldSbb;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.httpserver.collab.HttpServerSessionStore;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Bootstrap — wires ra-http-server + HelloWorldSBB into MicroSleeContainer.
 *
 * <p>Architecture:
 * <ul>
 *   <li>Quarkus port 8080 → serves index.html from META-INF/resources/ (UI)</li>
 *   <li>ra-http-server port 8081 → HTTP ingress → SBB event pipeline</li>
 *   <li>Browser accesses :8080 for UI, :8081/api/ussd/begin for JAIN SLEE path</li>
 * </ul>
 */
@ApplicationScoped
public final class HelloWorldBootstrap implements HelloWorldContext {

    private static final Logger LOG = LogManager.getLogger(HelloWorldBootstrap.class);

    @Inject
    MicroSleeContainer container;

    @org.eclipse.microprofile.config.inject.ConfigProperty(
            name = "http.ra.port", defaultValue = "8081")
    int httpRaPort;

    private final ConcurrentHashMap<String, SessionRecord> sessions = new ConcurrentHashMap<>();
    private volatile HttpServerRaEndpoint httpEndpoint;

    @PostConstruct
    void init() {
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }
        container.registerSbbType(HelloWorldSbb.class,
                () -> new HelloWorldSbb(container, this));
        container.createIesDispatcher();
        container.mapEventToSbb(HttpWebRequestEvent.class, "HelloWorldSbb");
        wireHttpRa();
        LOG.info("HelloWorld bootstrap complete. UI at :{}, RA at :{}",
                quarkusPort(), httpRaPort);
    }

    @PreDestroy
    void shutdown() {
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
