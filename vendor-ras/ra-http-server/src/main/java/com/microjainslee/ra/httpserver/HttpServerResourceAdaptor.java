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

import com.microjainslee.ra.httpserver.events.HttpUpload;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        // vertx-web Router: BodyHandler parses form fields, multipart file
        // uploads and cookies for us, so SBBs receive them already structured.
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()
                .setHandleFileUploads(true)
                .setDeleteUploadedFilesOnEnd(true)
                .setBodyLimit(32L * 1024 * 1024));
        router.get("/health").handler(ctx ->
                writeJson(ctx.response(), 200, "{\"status\":\"ok\"}"));
        router.route().handler(this::handle);

        server = vertx.createHttpServer(serverOptions).requestHandler(router);

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

    /**
     * Catch-all handler. The {@code BodyHandler} has already parsed the body,
     * form fields, multipart uploads and cookies; we snapshot them into an
     * {@link HttpWebRequestEvent} and fire it, storing the pending response so
     * the SBB can reply via a response command.
     */
    private void handle(RoutingContext ctx) {
        HttpServerRequest req = ctx.request();
        String sessionId = UUID.randomUUID().toString();
        pendingResponses.put(sessionId, ctx.response());

        Map<String, String> headers = new HashMap<>();
        req.headers().forEach(e -> headers.put(e.getKey(), e.getValue()));

        Map<String, String> queryParams = new HashMap<>();
        ctx.queryParams().forEach(e -> queryParams.put(e.getKey(), e.getValue()));

        Map<String, String> formAttrs = new HashMap<>();
        req.formAttributes().forEach(e -> formAttrs.put(e.getKey(), e.getValue()));

        Map<String, String> cookies = new HashMap<>();
        req.cookies().forEach(c -> cookies.put(c.getName(), c.getValue()));

        List<HttpUpload> uploads = new ArrayList<>();
        for (FileUpload fu : ctx.fileUploads()) {
            byte[] content = readUpload(fu);
            uploads.add(new HttpUpload(fu.name(), fu.fileName(), fu.contentType(), content));
        }

        String body = null;
        byte[] bytes = null;
        Buffer buf = ctx.body() != null ? ctx.body().buffer() : null;
        if (buf != null && buf.length() > 0) {
            bytes = buf.getBytes();
            body = buf.toString(StandardCharsets.UTF_8);
        }

        HttpWebRequestEvent event = new HttpWebRequestEvent(
                sessionId, req.method().name(), req.path(), headers, body, bytes,
                queryParams, formAttrs, cookies, uploads);

        // fireEvent may briefly block on entity acquisition — keep it off the event loop
        vertx.executeBlocking(() -> {
            endpoint().fireEvent(new SimpleActivityContextHandle(sessionId), event);
            return null;
        }, false).onComplete(res -> {
            // NOTE: the activity context is ended by the RA endpoint after the
            // response command is sent (see HttpServerRaEndpoint.sendCommand),
            // NOT here — event dispatch is asynchronous (Disruptor ring), so
            // ending the activity on this thread would race the consumer that
            // still has the request event queued.
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
        endRequestActivity(sessionId);
    }

    /**
     * Full-fidelity response: arbitrary headers ({@code Set-Cookie},
     * {@code Location}, {@code Cache-Control}, …) plus a text <i>or</i> binary
     * body. This is what lets a complete web app (redirects, sessions, static
     * assets, images) live behind the RA contract instead of app-level Vert.x.
     *
     * @param sessionId   session id from the inbound event
     * @param statusCode  HTTP status
     * @param contentType Content-Type value (may be null)
     * @param textBody    UTF-8 text body, or null
     * @param binaryBody  raw bytes body, or null (takes precedence when both set)
     * @param headers     extra response headers (may be null)
     */
    public void sendHttpResponse(String sessionId, int statusCode, String contentType,
                                 String textBody, byte[] binaryBody,
                                 Map<String, String> headers) {
        HttpServerResponse response = pendingResponses.remove(sessionId);
        if (response == null) {
            LOG.warn(() -> "No pending response for sessionId=" + sessionId
                    + " — may have already been sent or timed out");
            return;
        }
        if (headers != null) {
            headers.forEach(response::putHeader);
        }
        if (contentType != null && !contentType.isEmpty()) {
            response.putHeader("Content-Type", contentType);
        }
        response.setStatusCode(statusCode);
        if (binaryBody != null) {
            response.end(Buffer.buffer(binaryBody));
        } else if (textBody != null) {
            response.end(textBody);
        } else {
            response.end();
        }
        endRequestActivity(sessionId);
    }

    /**
     * End the per-request activity context after its response has been written.
     *
     * <p>Deferred onto the Vert.x event loop on purpose: {@code sendHttpResponse}
     * is invoked from the SBB on the event-router (Disruptor) consumer thread,
     * still inside the request event's transaction. Ending the activity there
     * would fire {@code ActivityEndedEvent} re-entrantly and corrupt that
     * thread's transaction context ("nested transaction mismatch"). Running it
     * via {@link io.vertx.core.Vertx#runOnContext} hops off the consumer thread,
     * so the end is a clean top-level dispatch and the named activity is
     * released instead of leaking in the naming facility.</p>
     */
    private void endRequestActivity(String sessionId) {
        io.vertx.core.Vertx v = this.vertx;
        if (v == null || sessionId == null) {
            return;
        }
        v.runOnContext(ignore -> {
            try {
                endpoint().endActivity(new SimpleActivityContextHandle(sessionId));
            } catch (RuntimeException e) {
                LOG.debug(() -> "endActivity(" + sessionId + ") ignored: " + e.getMessage());
            }
        });
    }

    // ── helpers ─────────────────────────────────────────────────────

    /** Read an uploaded multipart file (written to disk by BodyHandler) into memory. */
    private static byte[] readUpload(FileUpload fu) {
        try {
            return Files.readAllBytes(Path.of(fu.uploadedFileName()));
        } catch (Exception e) {
            LOG.warn("Failed to read uploaded file {}: {}", fu.fileName(), e.getMessage());
            return new byte[0];
        }
    }

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
