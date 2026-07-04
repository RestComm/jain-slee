/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.ra;

import com.example.ussddemo.quarkus.service.UssdSbbWiring;
import com.example.ussddemo.quarkus.service.UssdSessionStore;
import com.microjainslee.api.ResourceAdaptor;
import com.microjainslee.api.ResourceAdaptorContext;
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
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP ingress Resource Adaptor — JDK {@link HttpServer} front-end that
 * fires USSD begin events into the SLEE via {@link UssdSbbWiring}.
 */
public final class HttpIngressResourceAdaptor implements ResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpIngressResourceAdaptor.class);

    private ResourceAdaptorContext context;
    private UssdSbbWiring wiring;
    private UssdSessionStore sessionStore;
    private int port = 8080;
    private HttpServer server;

    public void setWiring(UssdSbbWiring wiring) {
        this.wiring = wiring;
    }

    public void setSessionStore(UssdSessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int port() {
        return server != null && server.getAddress() != null
                ? server.getAddress().getPort()
                : port;
    }

    @Override
    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        this.context = context;
    }

    @Override
    public void raConfigure() {
        LOG.infof("HTTP-RA configured on port %d", port);
    }

    @Override
    public void raActive() {
        try {
            startServer();
        } catch (IOException e) {
            throw new IllegalStateException("HTTP-RA failed to start", e);
        }
    }

    private void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        AtomicInteger n = new AtomicInteger();
        ThreadFactory tf = Thread.ofVirtual().name("http-ra-", n.getAndIncrement()).factory();
        server.setExecutor(Executors.newThreadPerTaskExecutor(tf));

        server.createContext("/health", new HealthHandler());
        server.createContext("/api/ussd/begin", new BeginHandler(false));
        server.createContext("/api/ussd/begin-callback", new BeginHandler(true));
        server.createContext("/api/ussd/sessions/", new SessionHandler());
        server.start();
        LOG.infof("HTTP-RA active at http://127.0.0.1:%d", port());
    }

    @Override
    public void raStopping() {
        LOG.info("HTTP-RA stopping");
    }

    @Override
    public void raInactive() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
        LOG.info("HTTP-RA inactive");
    }

    @Override
    public void raUnconfigure() {
        LOG.info("HTTP-RA unconfigured");
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
            String callbackUrl = queryParam(ex.getRequestURI(), "callbackUrl");
            if (requireCallback && (callbackUrl == null || callbackUrl.isEmpty())) {
                writeJson(ex, 400, "{\"error\":\"callbackUrl is required\"}");
                return;
            }
            beginAndRespond(ex, body, callbackUrl);
        }
    }

    private void beginAndRespond(HttpExchange ex, String body, String callbackUrl) throws IOException {
        String msisdn = extractJsonString(body, "msisdn");
        String ussdString = extractJsonString(body, "ussdString");
        if (msisdn == null || msisdn.trim().isEmpty()) {
            writeJson(ex, 400, "{\"error\":\"msisdn is required\"}");
            return;
        }
        if (ussdString == null || ussdString.trim().isEmpty()) {
            writeJson(ex, 400, "{\"error\":\"ussdString is required\"}");
            return;
        }
        String sessionId = UUID.randomUUID().toString();
        try {
            wiring.beginUssdSession(sessionId, msisdn.trim(), ussdString.trim(), callbackUrl);
        } catch (RuntimeException e) {
            LOG.error("begin failed", e);
            writeJson(ex, 400, "{\"error\":\"" + escape(e.getMessage()) + "\"}");
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

    static String extractJsonString(String json, String field) {
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
        if (valueEnd < 0) {
            return null;
        }
        return json.substring(valueStart, valueEnd);
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
