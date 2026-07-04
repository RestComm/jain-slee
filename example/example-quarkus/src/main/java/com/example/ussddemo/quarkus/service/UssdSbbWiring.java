/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.service;

import com.example.ussddemo.quarkus.ra.GrpcMenuResourceAdaptor;
import com.example.ussddemo.quarkus.ra.HttpIngressResourceAdaptor;
import com.example.ussddemo.quarkus.sbbs.GrpcClientSbb;
import com.example.ussddemo.quarkus.sbbs.HttpServerSbb;
import com.example.ussddemo.quarkus.sbbs.Ss7UssdIngressSbb;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.InMemoryActivityContext;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SimpleSbbLocalObject;
import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared wiring between CDI bootstrap, HTTP RA, gRPC RA, and pooled SBBs.
 */
@ApplicationScoped
@Unremovable
public final class UssdSbbWiring {

    private static final Logger LOG = LogManager.getLogger(UssdSbbWiring.class);

    private volatile MicroSleeContainer container;
    private volatile UssdSessionStore sessionStore;
    private volatile UssdCallbackDispatcher callbackDispatcher;
    private volatile HttpIngressResourceAdaptor httpRa;
    private volatile GrpcMenuResourceAdaptor grpcRa;
    private volatile long sessionTimeoutMs = 30_000L;

    private final Map<String, SbbLocalObject> httpLocals = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionTimers = new ConcurrentHashMap<>();
    private final Map<String, Integer> menuTiersByMsisdn = new ConcurrentHashMap<>();

    public void install(MicroSleeContainer container,
                        UssdSessionStore sessionStore,
                        UssdCallbackDispatcher callbackDispatcher,
                        long sessionTimeoutMs) {
        this.container = container;
        this.sessionStore = sessionStore;
        this.callbackDispatcher = callbackDispatcher;
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public void setHttpRa(HttpIngressResourceAdaptor httpRa) {
        this.httpRa = httpRa;
    }

    public void setGrpcRa(GrpcMenuResourceAdaptor grpcRa) {
        this.grpcRa = grpcRa;
    }

    public MicroSleeContainer container() {
        return container;
    }

    public UssdSessionStore sessionStore() {
        return sessionStore;
    }

    public GrpcMenuResourceAdaptor grpcRa() {
        return grpcRa;
    }

    public long sessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void bindHttpLocal(String sessionId, SbbLocalObject local) {
        httpLocals.put(sessionId, local);
    }

    public SbbLocalObject httpLocal(String sessionId) {
        return httpLocals.get(sessionId);
    }

    public void rememberTimer(String sessionId, long timerId) {
        sessionTimers.put(sessionId, timerId);
    }

    public Long timerFor(String sessionId) {
        return sessionTimers.get(sessionId);
    }

    public void clearSession(String sessionId) {
        httpLocals.remove(sessionId);
        Long timerId = sessionTimers.remove(sessionId);
        if (timerId != null && container != null) {
            container.getTimerPort().cancelTimer(timerId);
        }
    }

    public void beginUssdSession(String sessionId, String msisdn, String ussdString, String callbackUrl) {
        sessionStore.open(sessionId);
        sessionStore.attachCallback(sessionId, callbackUrl);

        MicroSleeContainer c = container;
        InMemoryActivityContext aci = c.createActivityContext(sessionId);

        SimpleSbbLocalObject http = c.registerSbb(sessionId + "/http", new HttpServerSbb(this));
        // Perfect Core S2 — Ss7UssdIngressSbb is abstract with @CmpField-
        // annotated accessors; the runtime works against its concrete
        // companion $Concrete (hand-written here, normally produced by
        // com.microjainslee.codegen.ConcreteSbbGenerator).
        SimpleSbbLocalObject ss7 = c.registerSbb(sessionId + "/ss7",
                new Ss7UssdIngressSbb.$Concrete(this));
        SimpleSbbLocalObject grpcChild = c.registerSbb(sessionId + "/grpc", new GrpcClientSbb(this));
        http.setPriority(8);
        ss7.setPriority(7);
        grpcChild.setPriority(5);
        c.attach(sessionId, http);
        c.attach(sessionId, ss7);
        c.attach(sessionId, grpcChild);
        bindHttpLocal(sessionId, http);

        int menuTier = resolveMenuTier(msisdn);
        LOG.infof("HTTP-RA firing HttpUssdBeginEvent session=%s msisdn=%s tier=%d callback=%s",
                sessionId, msisdn, menuTier, callbackUrl == null ? "<polling>" : callbackUrl);
        c.routeEvent(new com.example.ussddemo.quarkus.events.HttpUssdBeginEvent(
                sessionId, msisdn, ussdString, callbackUrl, menuTier), aci);
    }

    public void seedMenuTier(String msisdn, int tier) {
        menuTiersByMsisdn.put(msisdn, tier);
    }

    private int resolveMenuTier(String msisdn) {
        Integer tier = menuTiersByMsisdn.get(msisdn);
        return tier != null ? tier.intValue() : 1;
    }

    public void routeEvent(SleeEvent event, ActivityContextInterface aci) {
        container.routeEvent(event, aci);
    }

    public void completeSession(String sessionId, String responseText) {
        clearSession(sessionId);
        sessionStore.complete(sessionId, responseText);
        UssdSessionStore.SessionRecord record = sessionStore.get(sessionId);
        if (record != null && record.getCallbackUrl() != null) {
            callbackDispatcher.dispatch(record.getCallbackUrl(), sessionId, "COMPLETED",
                    responseText, null);
        }
    }

    public void failSession(String sessionId, String message) {
        clearSession(sessionId);
        sessionStore.fail(sessionId, message);
        UssdSessionStore.SessionRecord record = sessionStore.get(sessionId);
        if (record != null && record.getCallbackUrl() != null) {
            callbackDispatcher.dispatch(record.getCallbackUrl(), sessionId, "FAILED",
                    null, message);
        }
    }

    public HttpIngressResourceAdaptor httpRa() {
        return httpRa;
    }
}
