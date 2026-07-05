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

import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Generic HTTP ingress Resource Adaptor on <b>Vert.x core</b> — the same engine
 * that powers {@code quarkus-vertx-http}, i.e. the fastest HTTP path in
 * the Quarkus ecosystem and fully GraalVM-native ready.
 *
 * <p>Threading model: Vert.x event loops accept/parse requests; the
 * SLEE-facing work (fireEvent, which may briefly block on entity acquisition)
 * runs on Vert.x worker threads via {@code executeBlocking}, so event loops
 * are never blocked.</p>
 *
 * <p>Routes:</p>
 * <pre>
 *   GET  /health      → {"status":"ok"}
 *   ANY  /{path}      → fires {@link HttpWebRequestEvent} with full request metadata
 * </pre>
 *
 * <p>Application SBBs receive the event and can respond via
 * {@link #sendHttpResponse(String, int, String, String)} which resolves
 * the pending Vert.x response.</p>
 */
public final class HttpServerResourceAdaptor extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpServerResourceAdaptor.class);

    private Vertx vertx;
    private HttpServer server;

    private int port = 8080;
    private String host = "127.0.0.1";
    private int eventLoopThreads = 0;   // 0 = Vert.x default (2 × cores)

    /** Maps sessionId → pending HttpServerResponse for async resolution. */
    private final ConcurrentHashMap<String, HttpServerResponse> pendingResponses =
            new ConcurrentHashMap<>();

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

    /** Actual bound port (after ephemeral bind when configured port is 0). */
    public int port() {
        return server != null ? server.actualPort() : port;
    }

    // ── lifecycle ────────────────────────────────────────────────────

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
        pendingResponses.clear();
    }

    // ── routing ─────────────────────────────────────────────────────

    private void route(HttpServerRequest req) {
        String path = req.path();
        if (req.method() == HttpMethod.GET && "/health".equals(path)) {
            writeJson(req.response(), 200, "{\"status\":\"ok\"}");
            return;
        }

        // For all other requests: read body (if any) and fire generic event
        req.body().onComplete(bodyRes -> {
            String body = null;
            if (bodyRes.succeeded() && bodyRes.result().length() > 0) {
                body = bodyRes.result().toString(StandardCharsets.UTF_8);
            }
            fireHttpRequest(req, body);
        });
    }

    // ── SLEE-facing ─────────────────────────────────────────────────

    /**
     * Fires an {@link HttpWebRequestEvent} for the given request, storing the
     * response handle so the application SBB can reply asynchronously via
     * {@link #sendHttpResponse(String, int, String, String)}.
     */
    private void fireHttpRequest(HttpServerRequest req, String body) {
        String sessionId = UUID.randomUUID().toString();
        pendingResponses.put(sessionId, req.response());

        Map<String, String> headers = new HashMap<>();
        req.headers().forEach(e -> headers.put(e.getKey(), e.getValue()));

        HttpWebRequestEvent event = new HttpWebRequestEvent(
                sessionId,
                req.method().name(),
                req.path(),
                headers,
                body);

        // fireEvent may briefly block on entity acquisition — keep it off the event loop
        vertx.executeBlocking(() -> {
            endpoint().fireEvent(new SimpleActivityContextHandle(sessionId), event);
            return null;
        }, false).onComplete(res -> {
            if (res.failed()) {
                LOG.error("HTTP fireEvent failed session={}", sessionId, res.cause());
                HttpServerResponse response = pendingResponses.remove(sessionId);
                if (response != null) {
                    writeJson(response, 500,
                            "{\"error\":\""
                                    + HttpJson.escape(String.valueOf(
                                            res.cause() == null ? "internal"
                                                    : res.cause().getMessage()))
                                    + "\"}");
                }
            }
        });
    }

    /**
     * Sends an HTTP response to the pending request identified by {@code sessionId}.
     * Called by the application SBB (via the endpoint) to reply to an inbound
     * {@link HttpWebRequestEvent}.
     *
     * @param sessionId   the session ID from the original event
     * @param statusCode  HTTP status code (e.g. 200, 404)
     * @param contentType content-type header value (e.g. "application/json")
     * @param body        response body (may be null for empty body)
     */
    public void sendHttpResponse(String sessionId, int statusCode, String contentType,
                                  String body) {
        HttpServerResponse response = pendingResponses.remove(sessionId);
        if (response == null) {
            LOG.warn(() -> "No pending response for sessionId=" + sessionId
                    + " — may have already been sent or timed out");
            return;
        }
        if (contentType != null && !contentType.isEmpty()) {
            response.putHeader("Content-Type", contentType);
        }
        response.setStatusCode(statusCode);
        if (body != null) {
            response.end(body);
        } else {
            response.end();
        }
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
