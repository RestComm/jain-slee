/*
 * micro-jainslee 1.1.0 -- example application (example-spring-helloworld-web)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.helloworld.spring;

import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.telemetry.TelemetryPort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring-managed singleton that holds the container, telemetry port, and session-tracking state.
 * Provides static accessors so SBBs mirror the embedded HelloWorld patterns.
 *
 * <p>Populated by {@code HelloWorldBootstrap} during Spring context startup.</p>
 */
@Component
public final class HelloWorldContext {

    private static volatile MicroSleeContainer container;
    private static volatile TelemetryPort telemetryPort;
    private static volatile HelloWorldContext instance;

    private final ConcurrentHashMap<String, SessionRecord> sessions = new ConcurrentHashMap<>();

    public HelloWorldContext() {
        instance = this;
    }

    // ---- static accessors (used by SBBs) ----

    public static MicroSleeContainer container() {
        return require(container, "container");
    }

    public static TelemetryPort telemetryPort() {
        return require(telemetryPort, "telemetryPort");
    }

    public static HelloWorldContext context() {
        return require(instance, "context");
    }

    // ---- externally populated ----

    public void setContainer(MicroSleeContainer c) {
        container = c;
    }

    public void setTelemetryPort(TelemetryPort tp) {
        telemetryPort = tp;
    }

    // ---- session tracking ----

    public String httpEntityId(String sessionId) {
        return "HelloWorld/" + sessionId;
    }

    public void completeSession(String sessionId, String response) {
        sessions.put(sessionId, new SessionRecord(sessionId, "COMPLETED", response, null));
    }

    public void failSession(String sessionId, String msg) {
        sessions.put(sessionId, new SessionRecord(sessionId, "FAILED", null, msg));
    }

    // ---- internal ----

    private static <T> T require(T ref, String name) {
        if (ref == null) {
            throw new IllegalStateException("HelloWorldContext not started yet: " + name);
        }
        return ref;
    }

    // ── inner types ──

    record SessionRecord(String sessionId, String status,
                         String responseText, String errorMessage) {}
}
