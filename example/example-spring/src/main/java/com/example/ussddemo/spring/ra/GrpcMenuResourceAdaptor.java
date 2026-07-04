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

import com.example.ussddemo.spring.events.GrpcMenuRequestEvent;
import com.example.ussddemo.spring.events.GrpcMenuResponseEvent;
import com.example.ussddemo.spring.grpc.GrpcMenuResolver;
import com.example.ussddemo.spring.proto.MenuResponse;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ResourceAdaptor;
import com.microjainslee.api.ResourceAdaptorContext;
import com.microjainslee.api.SleeEndpointPort;
import com.microjainslee.core.MicroSleeContainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * gRPC menu Resource Adaptor. SBBs call {@link #requestMenu} with the
 * session ACI; the RA fires {@link GrpcMenuRequestEvent}, performs the
 * upstream gRPC call asynchronously, then routes
 * {@link GrpcMenuResponseEvent} back on the same activity.
 */
public final class GrpcMenuResourceAdaptor implements ResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(GrpcMenuResourceAdaptor.class);

    private ResourceAdaptorContext context;
    private MicroSleeContainer container;
    private GrpcMenuResolver resolver;
    private ExecutorService workerPool;

    public void setContainer(MicroSleeContainer container) {
        this.container = container;
    }

    public void setGrpcMenuResolver(GrpcMenuResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        this.context = context;
    }

    @Override
    public void raConfigure() {
        workerPool = Executors.newVirtualThreadPerTaskExecutor();
        LOG.info("gRPC RA configured");
    }

    @Override
    public void raActive() {
        LOG.info("gRPC RA active");
    }

    @Override
    public void raStopping() {
        LOG.info("gRPC RA stopping");
    }

    @Override
    public void raInactive() {
        if (workerPool != null) {
            workerPool.shutdown();
        }
    }

    @Override
    public void raUnconfigure() {
        if (workerPool != null) {
            workerPool.shutdownNow();
            workerPool = null;
        }
    }

    public void requestMenu(String sessionId, String msisdn, String ussdString,
                            ActivityContextInterface aci) {
        Objects.requireNonNull(aci, "aci");
        if (resolver == null) {
            LOG.warn("gRPC RA.requestMenu called before resolver wired");
            return;
        }
        MicroSleeContainer c = this.container;
        if (c == null) {
            LOG.warn("gRPC RA has no MicroSleeContainer wired");
            return;
        }
        c.routeEvent(new GrpcMenuRequestEvent(sessionId, msisdn, ussdString), aci);
        workerPool.submit(() -> doCall(sessionId, msisdn, ussdString, aci, c));
    }

    private void doCall(String sessionId, String msisdn, String ussdString,
                        ActivityContextInterface aci, MicroSleeContainer container) {
        MenuResponse resp;
        try {
            resp = resolver.resolveMenu(msisdn, ussdString, sessionId);
        } catch (Throwable t) {
            LOG.warnf(t, "gRPC RA call failed session=%s", sessionId);
            container.routeEvent(new GrpcMenuResponseEvent(
                    sessionId, "ERR", null,
                    t.getClass().getSimpleName() + ": " + t.getMessage()), aci);
            return;
        }
        container.routeEvent(new GrpcMenuResponseEvent(
                resp.getSessionId(), resp.getStatus(), resp.getMenuText(), resp.getError()), aci);
    }

    public SleeEndpointPort endpoint() {
        return context == null ? null : context.getSleeEndpointPort();
    }
}
