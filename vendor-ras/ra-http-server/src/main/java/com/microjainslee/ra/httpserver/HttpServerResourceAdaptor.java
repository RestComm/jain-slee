/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ResourceAdaptorContext;
import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * HTTP ingress Resource Adaptor on <b>Vert.x core</b> — the same engine
 * that powers {@code quarkus-vertx-http}, i.e. the fastest HTTP path in
 * the Quarkus ecosystem and fully GraalVM-native ready.
 *
 * <p>Threading model: Vert.x event loops accept/parse requests; the
 * SLEE-facing work (session prepare + fireEvent, which may briefly block
 * on entity acquisition) runs on Vert.x worker threads via
 * {@code executeBlocking}, so event loops are never blocked.</p>
 *
 * <p>Routes (unchanged from the legacy JDK-HttpServer implementation):</p>
 * <pre>
 *   GET  /health                        → {"status":"ok"}
 *   POST /api/ussd/begin                → 202 {"sessionId":..,"status":"PROCESSING"}
 *   POST /api/ussd/begin-callback       → same, requires ?callbackUrl=
 *   GET  /api/ussd/sessions/{id}        → session snapshot JSON
 * </pre>
 */
public final class HttpServerResourceAdaptor extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpServerResourceAdaptor.class);

    private Vertx vertx;
    private HttpServer server;

    private HttpServerSessionStore sessionStore;
    private HttpServerSessionPreparer sessionPreparer;
    private HttpBeginEventFactory beginEventFactory;
    private ActivityContextFactory activityContextFactory;
    private int port = 8080;
    private String host = "127.0.0.1";
    private int eventLoopThreads = 0;   // 0 = Vert.x default (2 × cores)

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /** 0 = Vert.x default. Tune down for tiny containers. */
    public void setEventLoopThreads(int n) {
        this.eventLoopThreads = n;
    }

    public void setSessionStore(HttpServerSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public void setSessionPreparer(HttpServerSessionPreparer sessionPreparer) {
        this.sessionPreparer = sessionPreparer;
    }

    public void setBeginEventFactory(HttpBeginEventFactory beginEventFactory) {
        this.beginEventFactory = beginEventFactory;
    }

    public void setActivityContextFactory(ActivityContextFactory activityContextFactory) {
        this.activityContextFactory = activityContextFactory;
    }

    /** Actual bound port (after ephemeral bind when configured port is 0). */
    public int port() {
        return server != null ? server.actualPort() : port;
    }

    @Override
    public void raConfigure() {
        LOG.info(() -> "HTTP server RA configured (Vert.x) host=" + host + " port=" + port);
    }

    @Override
    public void raActive() {
        VertxOptions options = new VertxOptions();
        if (eventLoopThreads > 0) {
            options.setEventLoopPoolSize(eventLoopThreads);
        }
        vertx = Vertx.vertx(options);

        HttpServerOptions serverOptions = new HttpServerOptions()
                .setHost(host)
                .setPort(port)
                .setTcpNoDelay(true)
                .setCompressionSupported(false);

        server = vertx.createHttpServer(serverOptions).requestHandler(this::route);

        CountDownLatch bound = new CountDownLatch(1);
        Throwable[] failure = new Throwable[1];
        server.listen().onComplete(res -> {
            if (res.failed()) {
                failure[0] = res.cause();
            }
            bound.countDown();
        });
        try {
            if (!bound.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("HTTP server bind timed out on " + host + ":" + port);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("HTTP server bind interrupted", e);
        }
        if (failure[0] != null) {
            throw new IllegalStateException(
                    "Failed to start HTTP server on " + host + ":" + port, failure[0]);
        }
        LOG.info(() -> "HTTP server RA listening on http://" + host + ":" + server.actualPort()
                + " (Vert.x)");
    }

    @Override
    public void raStopping() {
        LOG.info("HTTP server RA stopping");
    }

    @Override
    public void raInactive() {
        CountDownLatch closed = new CountDownLatch(1);
        if (server != null) {
            server.close().onComplete(v -> closed.countDown());
            awaitQuietly(closed);
            server = null;
        }
        if (vertx != null) {
            CountDownLatch vertxClosed = new CountDownLatch(1);
            vertx.close().onComplete(v -> vertxClosed.countDown());
            awaitQuietly(vertxClosed);
            vertx = null;
        }
    }

    // ── routing ─────────────────────────────────────────────────────

    private void route(HttpServerRequest req) {
        String path = req.path();
        if (req.method() == HttpMethod.GET && "/health".equals(path)) {
            writeJson(req.response(), 200, "{\"status\":\"ok\"}");
            return;
        }
        if ("/api/ussd/begin".equals(path) || "/api/ussd/begin-callback".equals(path)) {
            if (req.method() != HttpMethod.POST) {
                writeJson(req.response(), 405, "{\"error\":\"method-not-allowed\"}");
                return;
            }
            boolean requireCallback = path.endsWith("begin-callback");
            req.body().onComplete(bodyRes -> {
                if (bodyRes.failed()) {
                    writeJson(req.response(), 400, "{\"error\":\"body-read-failed\"}");
                    return;
                }
                String body = bodyRes.result().toString(StandardCharsets.UTF_8);
                String callbackUrl = req.getParam("callbackUrl");
                handleBegin(req, body, callbackUrl, requireCallback);
            });
            return;
        }
        if (req.method() == HttpMethod.GET && path.startsWith("/api/ussd/sessions/")) {
            handleSessionQuery(req, path.substring(path.lastIndexOf('/') + 1));
            return;
        }
        writeJson(req.response(), 404, "{\"error\":\"not-found\"}");
    }

    private void handleBegin(HttpServerRequest req, String body, String callbackUrl,
                             boolean requireCallback) {
        if (requireCallback && (callbackUrl == null || callbackUrl.isEmpty())) {
            writeJson(req.response(), 400, "{\"error\":\"callbackUrl is required\"}");
            return;
        }
        String msisdn = HttpJson.extractString(body, "msisdn");
        String ussdString = HttpJson.extractString(body, "ussdString");
        if (msisdn == null || msisdn.trim().isEmpty()) {
            writeJson(req.response(), 400, "{\"error\":\"msisdn is required\"}");
            return;
        }
        if (ussdString == null || ussdString.trim().isEmpty()) {
            writeJson(req.response(), 400, "{\"error\":\"ussdString is required\"}");
            return;
        }

        String sessionId = UUID.randomUUID().toString();
        String trimmedMsisdn = msisdn.trim();
        String trimmedUssd = ussdString.trim();
        // fireHttpBegin touches SLEE internals (entity acquire, attach) that
        // may briefly block — keep it OFF the event loop.
        vertx.executeBlocking(() -> {
            fireHttpBegin(sessionId, trimmedMsisdn, trimmedUssd, callbackUrl);
            return null;
        }, false).onComplete(res -> {
            if (res.failed()) {
                LOG.error("HTTP begin failed session={}", sessionId, res.cause());
                writeJson(req.response(), 500,
                        "{\"error\":\"" + HttpJson.escape(String.valueOf(
                                res.cause() == null ? "internal" : res.cause().getMessage())) + "\"}");
                return;
            }
            HttpServerResponse response = req.response();
            response.putHeader("Content-Type", "application/json");
            if (callbackUrl != null) {
                response.putHeader("Location", callbackUrl + "?sessionId=" + sessionId);
            }
            response.setStatusCode(202)
                    .end("{\"sessionId\":\"" + sessionId + "\",\"status\":\"PROCESSING\"}");
        });
    }

    private void handleSessionQuery(HttpServerRequest req, String sessionId) {
        if (sessionStore == null) {
            writeJson(req.response(), 503, "{\"error\":\"session-store-unavailable\"}");
            return;
        }
        HttpServerSessionStore.SessionSnapshot rec = sessionStore.get(sessionId);
        if (rec == null) {
            writeJson(req.response(), 404, "{\"error\":\"unknown-session\"}");
            return;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append('{');
        sb.append("\"sessionId\":\"").append(HttpJson.escape(sessionId)).append("\",");
        sb.append("\"status\":\"").append(HttpJson.escape(rec.getStatus())).append("\",");
        if (rec.getResponseText() != null) {
            sb.append("\"responseText\":\"").append(HttpJson.escape(rec.getResponseText()))
                    .append("\",");
        }
        if (rec.getErrorMessage() != null) {
            sb.append("\"errorMessage\":\"").append(HttpJson.escape(rec.getErrorMessage()))
                    .append("\",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }
        sb.append('}');
        writeJson(req.response(), 200, sb.toString());
    }

    // ── SLEE-facing ─────────────────────────────────────────────────

    void fireHttpBegin(String sessionId, String msisdn, String ussdString, String callbackUrl) {
        if (beginEventFactory == null) {
            throw new IllegalStateException("HttpBeginEventFactory not configured");
        }
        if (activityContextFactory == null) {
            throw new IllegalStateException("ActivityContextFactory not configured");
        }
        ResourceAdaptorContext ctx = context();
        ActivityContextInterface aci = activityContextFactory.create(sessionId, ctx);
        if (sessionPreparer != null) {
            sessionPreparer.prepare(sessionId, callbackUrl, aci);
        }
        SleeEvent event = beginEventFactory.createBeginEvent(
                sessionId, msisdn, ussdString, callbackUrl);
        endpoint().fireEvent(new SimpleActivityContextHandle(sessionId), event);
    }

    /**
     * Creates an activity context for a new HTTP session. Injected at wiring time
     * by the application (e.g. {@code (sessionId, ctx) -> container.createActivityContext(sessionId)}).
     */
    public interface ActivityContextFactory {
        ActivityContextInterface create(String sessionId, ResourceAdaptorContext context);
    }

    // ── helpers ─────────────────────────────────────────────────────

    private static void writeJson(HttpServerResponse response, int status, String body) {
        response.putHeader("Content-Type", "application/json")
                .setStatusCode(status)
                .end(body);
    }

    private static void awaitQuietly(CountDownLatch latch) {
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
