/*
 * micro-jainslee 1.1.0 -- example application (example-spring-ussdgw)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring;

import com.microjainslee.core.MicroSleeContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring-managed singleton that holds the container, runtime, and
 * session-tracking state. Provides static accessors so SBBs copied
 * from example-embedded-j25-ussdgw need minimal adaptation.
 *
 * <p>Populated by {@code UssdDemoBootstrap} during Spring context startup.</p>
 */
@Component
public final class UssdDemoContext {

    private static volatile MicroSleeContainer container;
    private static volatile UssdDemoRuntime runtime;
    private static volatile UssdDemoContext instance;

    private final ConcurrentHashMap<String, String> tiersByMsisdn = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> callbackUrls = new ConcurrentHashMap<>();

    public UssdDemoContext() {
        instance = this;
    }

    // ---- static accessors (used by SBBs) ----

    public static MicroSleeContainer container() {
        return require(container, "container");
    }

    public static UssdDemoContext context() {
        return require(instance, "context");
    }

    public static UssdDemoRuntime runtime() {
        return require(runtime, "runtime");
    }

    // ---- externally populated ----

    public void setContainer(MicroSleeContainer c) {
        container = c;
    }

    public void setRuntime(UssdDemoRuntime r) {
        runtime = r;
    }

    // ---- session tracking (mirrors EmbeddedUssdBootstrap) ----

    public String tierFor(String msisdn) {
        return tiersByMsisdn.getOrDefault(msisdn, "STANDARD");
    }

    public void seedTier(String msisdn, String tier) {
        tiersByMsisdn.put(msisdn, tier);
    }

    public String httpEntityId(String sessionId) { return "HttpServer/" + sessionId; }
    public String ss7EntityId(String sessionId) { return "Ss7UssdIngress/" + sessionId; }

    public void storeCallbackUrl(String sessionId, String callbackUrl) {
        if (callbackUrl != null && !callbackUrl.isEmpty()) callbackUrls.put(sessionId, callbackUrl);
    }

    public String callbackUrlFor(String sessionId) { return callbackUrls.get(sessionId); }

    public void releaseSession(String sessionId) {
        container.releaseEntity(ss7EntityId(sessionId));
        container.releaseEntity(httpEntityId(sessionId));
        callbackUrls.remove(sessionId);
    }

    // ---- internal ----

    private static <T> T require(T ref, String name) {
        if (ref == null) throw new IllegalStateException("UssdDemoContext not started yet: " + name);
        return ref;
    }
}
