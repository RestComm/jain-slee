/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.http;

import com.microjainslee.ai.AIAgentEngine;
import com.microjainslee.ai.AIAnalysis;
import com.microjainslee.ai.AIMode;
import com.microjainslee.ai.ReportAudience;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import com.microjainslee.telemetry.TelemetryPort;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Serves the observability surface — the steampunk dashboard GUI (from the
 * {@code jainslee-monitor} jar) plus the {@code /api/telemetry/*},
 * {@code /api/autonomous/*} and {@code /api/ai/*} JSON endpoints — entirely
 * through the {@code ra-http-server} RA. No Vert.x, no second HTTP server: the
 * dashboard the old {@code AppTelemetry} opened on its own port now lives
 * behind the SLEE RA contract like every other resource.
 *
 * <p>Returns {@link Optional#empty()} for any path it does not own, so the
 * {@code HttpGatewaySbb} can fall through to the CMR site handler.</p>
 */
public final class MonitorHandler {

    private static final Logger LOG = LogManager.getLogger(MonitorHandler.class);
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final String GUI_ROOT = "META-INF/resources";

    private final TelemetryPort telemetry;
    private final Supplier<String> healthJson;   // nullable — autonomous optional
    private final AIAgentEngine ai;              // nullable — AI optional

    public MonitorHandler(TelemetryPort telemetry, Supplier<String> healthJson, AIAgentEngine ai) {
        this.telemetry = telemetry;
        this.healthJson = healthJson;
        this.ai = ai;
    }

    /** Handle a monitor path, or empty if it is not ours. */
    public Optional<HttpReply> handle(HttpWebRequestEvent e) {
        String path = e.getPath();
        if (path.equals("/telemetry") || path.startsWith("/telemetry/")) {
            return Optional.of(serveGui(path));
        }
        if (path.startsWith("/api/telemetry")) {
            return Optional.of(telemetryApi(e, path));
        }
        if (path.equals("/api/autonomous/health")) {
            return Optional.of(healthJson == null
                    ? HttpReply.json("{\"status\":\"UNKNOWN\"}") : HttpReply.json(healthJson.get()));
        }
        if (path.startsWith("/api/ai")) {
            return Optional.of(aiApi(e, path));
        }
        return Optional.empty();
    }

    // ── GUI static assets (served from the monitor jar's classpath) ──

    private HttpReply serveGui(String path) {
        String rest = path.substring("/telemetry".length());
        if (rest.isEmpty() || rest.equals("/")) {
            rest = "/index.html";
        }
        String resource = GUI_ROOT + rest;
        try (InputStream in = classLoader().getResourceAsStream(resource)) {
            if (in == null) {
                return HttpReply.notFound();
            }
            return HttpReply.bytes(contentType(rest), in.readAllBytes());
        } catch (Exception ex) {
            LOG.warn("[monitor] failed to serve {}: {}", resource, ex.getMessage());
            return HttpReply.notFound();
        }
    }

    // ── telemetry API ──

    private HttpReply telemetryApi(HttpWebRequestEvent e, String path) {
        try {
            if (path.equals("/api/telemetry/snapshot")) {
                return HttpReply.json(JSON.writeValueAsString(telemetry.snapshot()));
            }
            if (path.equals("/api/telemetry/metrics")) {
                return HttpReply.text("text/plain; charset=utf-8", telemetry.scrape());
            }
            if (path.equals("/api/telemetry/alarms")) {
                return HttpReply.json(JSON.writeValueAsString(telemetry.alarmEngine().active()));
            }
            if (path.startsWith("/api/telemetry/alarms/") && path.endsWith("/clear")
                    && e.getMethod().equalsIgnoreCase("POST")) {
                String id = path.substring("/api/telemetry/alarms/".length(),
                        path.length() - "/clear".length());
                telemetry.alarmEngine().clear(id);
                return HttpReply.json("{\"status\":\"ok\"}");
            }
            if (path.equals("/api/telemetry/config") && e.getMethod().equalsIgnoreCase("POST")) {
                JsonNode body = readBody(e);
                if (body != null && body.has("autoReconfig")) {
                    telemetry.setAutoReconfigEnabled(body.get("autoReconfig").asBoolean());
                }
                return HttpReply.json("{\"status\":\"ok\"}");
            }
        } catch (Exception ex) {
            return HttpReply.html(500, "telemetry error: " + ex.getMessage());
        }
        return HttpReply.notFound();
    }

    // ── AI agent API ──

    private HttpReply aiApi(HttpWebRequestEvent e, String path) {
        if (ai == null) {
            return HttpReply.json("{\"available\":false,\"reason\":\"AI module not installed\"}");
        }
        try {
            switch (path) {
                case "/api/ai/status":
                    return HttpReply.json(JSON.writeValueAsString(ai.status()));
                case "/api/ai/analysis": {
                    AIAnalysis last = ai.lastAnalysis();
                    return last == null ? HttpReply.noContent()
                            : HttpReply.json(JSON.writeValueAsString(last));
                }
                case "/api/ai/analyze": {
                    AIAnalysis a = ai.analyzeNow();
                    return a == null
                            ? HttpReply.html(503, "{\"error\":\"AI unavailable — check api-key\"}")
                            : HttpReply.json(JSON.writeValueAsString(a));
                }
                case "/api/ai/report": {
                    ReportAudience audience = ReportAudience.parse(e.getQueryParam("audience"));
                    return HttpReply.text("text/plain; charset=utf-8", ai.report(audience));
                }
                case "/api/ai/config": {
                    JsonNode body = readBody(e);
                    if (body != null) {
                        if (body.has("enabled")) {
                            ai.setEnabled(body.get("enabled").asBoolean());
                        }
                        if (body.has("mode")) {
                            ai.setMode(AIMode.parse(body.get("mode").asText()));
                        }
                    }
                    return HttpReply.json("{\"status\":\"ok\"}");
                }
                default:
                    return HttpReply.notFound();
            }
        } catch (Exception ex) {
            return HttpReply.html(500, "ai error: " + ex.getMessage());
        }
    }

    // ── helpers ──

    private static JsonNode readBody(HttpWebRequestEvent e) {
        String body = e.getBody();
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            return JSON.readTree(body);
        } catch (Exception ex) {
            return null;
        }
    }

    private static ClassLoader classLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl != null ? cl : MonitorHandler.class.getClassLoader();
    }

    private static String contentType(String path) {
        String p = path.toLowerCase();
        if (p.endsWith(".html")) return "text/html; charset=utf-8";
        if (p.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (p.endsWith(".css")) return "text/css; charset=utf-8";
        if (p.endsWith(".json")) return "application/json";
        if (p.endsWith(".svg")) return "image/svg+xml";
        if (p.endsWith(".png")) return "image/png";
        if (p.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }
}
