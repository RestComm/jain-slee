/*
 * micro-jainslee example :: HelloWorld Web
 */
package com.example.helloworld.quarkus.http;

import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import com.microjainslee.telemetry.TelemetryPort;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Optional;

/**
 * Observability surface — steampunk dashboard ({@code jainslee-monitor}) plus
 * {@code /api/telemetry/*}, served through {@code ra-http-server}.
 */
public final class MonitorHandler {

    private static final Logger LOG = LogManager.getLogger(MonitorHandler.class);
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final String GUI_ROOT = "META-INF/resources";

    private final TelemetryPort telemetry;

    public MonitorHandler(TelemetryPort telemetry) {
        this.telemetry = telemetry;
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
        return Optional.empty();
    }

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
