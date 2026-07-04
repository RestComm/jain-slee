/*
 * micro-jainslee 1.1.0 -- example application (example-spring)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.ra;

import com.example.ussddemo.spring.events.HttpUssdBeginEvent;
import com.example.ussddemo.spring.service.UssdSessionStore;
import com.microjainslee.api.ActivityContextHandle;
import com.microjainslee.api.ResourceAdaptor;
import com.microjainslee.api.ResourceAdaptorContext;
import com.microjainslee.api.SleeEndpointPort;
import com.microjainslee.api.SleeEvent;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP ingress Resource Adaptor. Serves the USSD callback REST API on the
 * configured port (default 8081) and fires {@link HttpUssdBeginEvent} into
 * the SLEE through {@link SleeEndpointPort}.
 */
public final class HttpIngressResourceAdaptor implements ResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpIngressResourceAdaptor.class);

    public static final class HttpSessionActivity {
        private final String sessionId;

        public HttpSessionActivity(String sessionId) {
            this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        }

        public String getSessionId() {
            return sessionId;
        }
    }

    private volatile ResourceAdaptorContext context;
    private volatile UssdSessionStore sessionStore;
    private volatile int port = 8081;
    private volatile String bindHost = "127.0.0.1";
    private HttpServer server;
    private final ConcurrentHashMap<String, ActivityContextHandle> sessions =
            new ConcurrentHashMap<>();

    public void setSessionStore(UssdSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setBindHost(String bindHost) {
        this.bindHost = bindHost;
    }

    public int boundPort() {
        return server == null ? -1 : server.getAddress().getPort();
    }

    @Override
    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        this.context = context;
    }

    @Override
    public void raConfigure() {
    }

    @Override
    public void raActive() {
        try {
            server = HttpServer.create(new InetSocketAddress(bindHost, port), 0);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start HTTP ingress RA on port " + port, e);
        }
        AtomicInteger n = new AtomicInteger();
        server.setExecutor(Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("http-ra-", n.getAndIncrement()).factory()));
        server.createContext("/health", new HealthHandler());
        server.createContext("/api/ussd/begin", ex -> handleBegin(ex, false));
        server.createContext("/api/ussd/begin-callback", ex -> handleBegin(ex, true));
        server.createContext("/api/ussd/sessions/", new SessionHandler());
        server.start();
        LOG.infof("HTTP ingress RA listening on http://%s:%d", bindHost, server.getAddress().getPort());
    }

    @Override
    public void raStopping() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Override
    public void raInactive() {
        sessions.clear();
        server = null;
    }

    @Override
    public void raUnconfigure() {
        context = null;
    }

    public void onHttpEnd(String sessionId) {
        ActivityContextHandle handle = sessions.remove(sessionId);
        if (handle != null && context != null && context.getSleeEndpointPort() != null) {
            context.getSleeEndpointPort().endActivity(handle);
        }
    }

    private void handleBegin(HttpExchange ex, boolean requireCallback) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            writeJson(ex, 405, "{\"error\":\"method-not-allowed\"}");
            return;
        }
        String callbackUrl = queryParam(ex.getRequestURI(), "callbackUrl");
        if (requireCallback && (callbackUrl == null || callbackUrl.isEmpty())) {
            writeJson(ex, 400, "{\"error\":\"callbackUrl is required\"}");
            return;
        }
        String body = readBody(ex);
        String msisdn = extractJsonString(body, "msisdn");
        String ussdString = extractJsonString(body, "ussdString");
        if (msisdn == null || msisdn.isEmpty() || ussdString == null || ussdString.isEmpty()) {
            writeJson(ex, 400, "{\"error\":\"msisdn and ussdString are required\"}");
            return;
        }
        String sessionId = UUID.randomUUID().toString();
        sessionStore.open(sessionId);
        if (callbackUrl != null && !callbackUrl.isEmpty()) {
            sessionStore.attachCallback(sessionId, callbackUrl);
        }
        try {
            onHttpBegin(sessionId, new HttpUssdBeginEvent(sessionId, msisdn, ussdString, callbackUrl));
        } catch (RuntimeException e) {
            LOG.error("HTTP RA begin failed", e);
            writeJson(ex, 500, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
            return;
        }
        String response = "{\"sessionId\":\"" + sessionId + "\",\"status\":\"PROCESSING\"}";
        ex.getResponseHeaders().add("Content-Type", "application/json");
        if (callbackUrl != null) {
            ex.getResponseHeaders().add("Location", callbackUrl + "?sessionId=" + sessionId);
        }
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(202, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void onHttpBegin(String sessionId, SleeEvent initialEvent) {
        HttpSessionActivity activity = new HttpSessionActivity(sessionId);
        ActivityContextHandle handle = context.createActivityContextHandle(activity);
        sessions.put(sessionId, handle);
        SleeEndpointPort endpoint = context.getSleeEndpointPort();
        if (endpoint == null) {
            throw new IllegalStateException("SleeEndpointPort not available");
        }
        endpoint.fireEvent(handle, initialEvent);
    }

    private final class SessionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
                writeJson(ex, 405, "{\"error\":\"method-not-allowed\"}");
                return;
            }
            String path = ex.getRequestURI().getPath();
            String sessionId = path.substring(path.lastIndexOf('/') + 1);
            UssdSessionStore.SessionRecord rec = sessionStore.get(sessionId);
            if (rec == null) {
                writeJson(ex, 404, "{\"error\":\"unknown-session\"}");
                return;
            }
            StringBuilder sb = new StringBuilder(128);
            sb.append('{');
            sb.append("\"sessionId\":\"").append(escape(sessionId)).append("\",");
            sb.append("\"status\":\"").append(rec.getStatus().name()).append("\",");
            if (rec.getResponseText() != null) {
                sb.append("\"responseText\":\"").append(escape(rec.getResponseText())).append("\",");
            }
            if (rec.getErrorMessage() != null) {
                sb.append("\"errorMessage\":\"").append(escape(rec.getErrorMessage())).append("\",");
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

    private static String queryParam(URI uri, String name) {
        String q = uri.getRawQuery();
        if (q == null) {
            return null;
        }
        for (String pair : q.split("&")) {
            int eq = pair.indexOf('=');
            if (eq < 0) {
                continue;
            }
            if (pair.substring(0, eq).equals(name)) {
                return java.net.URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private static String extractJsonString(String json, String field) {
        if (json == null) {
            return null;
        }
        String marker = "\"" + field + "\":\"";
        int start = json.indexOf(marker);
        if (start < 0) {
            return null;
        }
        int valueStart = start + marker.length();
        int valueEnd = json.indexOf('"', valueStart);
        return valueEnd < 0 ? null : json.substring(valueStart, valueEnd);
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
