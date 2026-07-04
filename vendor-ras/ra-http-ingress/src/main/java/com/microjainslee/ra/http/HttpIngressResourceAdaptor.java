/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.http;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ResourceAdaptorContext;
import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HTTP ingress Resource Adaptor — JDK {@link HttpServer} front-end that fires
 * application events into the SLEE via {@link com.microjainslee.api.SleeEndpointPort}.
 */
public final class HttpIngressResourceAdaptor extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpIngressResourceAdaptor.class);

    private HttpServer server;
    private HttpIngressSessionStore sessionStore;
    private HttpIngressSessionPreparer sessionPreparer;
    private HttpBeginEventFactory beginEventFactory;
    private ActivityContextFactory activityContextFactory;
    private int port = 8080;

    public void setPort(int port) {
        this.port = port;
    }

    public void setSessionStore(HttpIngressSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public void setSessionPreparer(HttpIngressSessionPreparer sessionPreparer) {
        this.sessionPreparer = sessionPreparer;
    }

    public void setBeginEventFactory(HttpBeginEventFactory beginEventFactory) {
        this.beginEventFactory = beginEventFactory;
    }

    public void setActivityContextFactory(ActivityContextFactory activityContextFactory) {
        this.activityContextFactory = activityContextFactory;
    }

    public int port() {
        return server != null && server.getAddress() != null
                ? server.getAddress().getPort()
                : port;
    }

    @Override
    public void raConfigure() {
        LOG.info(() -> "HTTP ingress RA configured on port " + port);
    }

    @Override
    public void raActive() {
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start HTTP ingress server on port " + port, e);
        }
        AtomicInteger n = new AtomicInteger();
        ThreadFactory tf = Thread.ofVirtual().name("http-ingress-", n.getAndIncrement()).factory();
        server.setExecutor(Executors.newThreadPerTaskExecutor(tf));
        server.createContext("/health", new HealthHandler());
        server.createContext("/api/ussd/begin", new BeginHandler(false));
        server.createContext("/api/ussd/begin-callback", new BeginHandler(true));
        server.createContext("/api/ussd/sessions/", new SessionHandler());
        server.start();
        port = server.getAddress().getPort();
        LOG.info(() -> "HTTP ingress RA listening on http://127.0.0.1:" + port);
    }

    @Override
    public void raStopping() {
        LOG.info("HTTP ingress RA stopping");
    }

    @Override
    public void raInactive() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

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

    private void onHttpBegin(String body, String callbackUrl, boolean requireCallback,
                             HttpExchange exchange) throws IOException {
        if (requireCallback && (callbackUrl == null || callbackUrl.isEmpty())) {
            writeJson(exchange, 400, "{\"error\":\"callbackUrl is required\"}");
            return;
        }
        String msisdn = HttpJson.extractString(body, "msisdn");
        String ussdString = HttpJson.extractString(body, "ussdString");
        if (msisdn == null || msisdn.trim().isEmpty()) {
            writeJson(exchange, 400, "{\"error\":\"msisdn is required\"}");
            return;
        }
        if (ussdString == null || ussdString.trim().isEmpty()) {
            writeJson(exchange, 400, "{\"error\":\"ussdString is required\"}");
            return;
        }

        String sessionId = UUID.randomUUID().toString();
        try {
            fireHttpBegin(sessionId, msisdn.trim(), ussdString.trim(), callbackUrl);
        } catch (RuntimeException e) {
            LOG.error("HTTP begin failed session={}", sessionId, e);
            writeJson(exchange, 500, "{\"error\":\"" + HttpJson.escape(e.getMessage()) + "\"}");
            return;
        }

        String response = "{\"sessionId\":\"" + sessionId + "\",\"status\":\"PROCESSING\"}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        if (callbackUrl != null) {
            exchange.getResponseHeaders().add("Location",
                    callbackUrl + "?sessionId=" + sessionId);
        }
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(202, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Creates an activity context for a new HTTP session. Injected at wiring time
     * by the application (e.g. {@code (sessionId, ctx) -> container.createActivityContext(sessionId)}).
     */
    public interface ActivityContextFactory {
        ActivityContextInterface create(String sessionId, ResourceAdaptorContext context);
    }

    private final class BeginHandler implements HttpHandler {
        private final boolean requireCallback;

        BeginHandler(boolean requireCallback) {
            this.requireCallback = requireCallback;
        }

        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
                writeJson(ex, 405, "{\"error\":\"method-not-allowed\"}");
                return;
            }
            String body = readBody(ex);
            String callbackUrl = HttpJson.queryParam(ex.getRequestURI(), "callbackUrl");
            onHttpBegin(body, callbackUrl, requireCallback, ex);
        }
    }

    private final class SessionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
                writeJson(ex, 405, "{\"error\":\"method-not-allowed\"}");
                return;
            }
            if (sessionStore == null) {
                writeJson(ex, 503, "{\"error\":\"session-store-unavailable\"}");
                return;
            }
            String path = ex.getRequestURI().getPath();
            String sessionId = path.substring(path.lastIndexOf('/') + 1);
            HttpIngressSessionStore.SessionSnapshot rec = sessionStore.get(sessionId);
            if (rec == null) {
                writeJson(ex, 404, "{\"error\":\"unknown-session\"}");
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
            writeJson(ex, 200, sb.toString());
        }
    }

    private static final class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            writeJson(ex, 200, "{\"status\":\"ok\"}");
        }
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void writeJson(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
