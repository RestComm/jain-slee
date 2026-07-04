/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Async gRPC menu Resource Adaptor - fires request/response {@link SleeEvent}s
 * on the USSD session activity via {@link com.microjainslee.api.SleeEndpointPort}.
 *
 * <h2>Sprint S8 - sequence stamping</h2>
 * <p>Every {@code requestMenu} call now stamps a per-session
 * monotonically increasing sequence number from {@link #sequenceCounters}.
 * The request event is built through
 * {@link GrpcMenuEventFactory#createRequestEvent(String, String, String, long)}
 * which the kernel honors for dedup and out-of-order buffering via
 * {@link com.microjainslee.api.SequencedEvent}. The counter survives
 * session churn (a fresh USSD dialog gets a new counter via
 * {@code computeIfAbsent}).</p>
 */
public final class GrpcMenuResourceAdaptor extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(GrpcMenuResourceAdaptor.class);

    private GrpcMenuUpstream upstream;
    private GrpcMenuEventFactory eventFactory;
    private GrpcActivityContextLookup activityContextLookup;
    private ExecutorService workerPool;

    /**
     * Sprint S8 - per-session atomic sequence counter. Populated on
     * first {@link #requestMenu} for a given session; survives until
     * the RA is reconfigured.
     */
    private final ConcurrentMap<String, AtomicLong> sequenceCounters =
            new ConcurrentHashMap<>();

    public void setGrpcMenuUpstream(GrpcMenuUpstream upstream) {
        this.upstream = upstream;
    }

    public void setEventFactory(GrpcMenuEventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    public void setActivityContextLookup(GrpcActivityContextLookup activityContextLookup) {
        this.activityContextLookup = activityContextLookup;
    }

    @Override
    public void raConfigure() {
        workerPool = Executors.newVirtualThreadPerTaskExecutor();
        LOG.info("gRPC menu RA configured (virtual-thread worker pool)");
    }

    @Override
    public void raActive() {
        LOG.info("gRPC menu RA active");
    }

    @Override
    public void raStopping() {
        LOG.info("gRPC menu RA stopping");
    }

    @Override
    public void raInactive() {
        if (workerPool != null) {
            workerPool.shutdown();
        }
        sequenceCounters.clear();
    }

    @Override
    public void raUnconfigure() {
        if (workerPool != null) {
            workerPool.shutdownNow();
            workerPool = null;
        }
        sequenceCounters.clear();
        super.raUnconfigure();
    }

    /**
     * Starts an async upstream menu lookup for the given USSD session.
     *
     * <p>Request event fires on the session ACI (looked up via the
     * configured {@link GrpcActivityContextLookup}); response event fires
     * on the separate {@code responseAci} supplied by the caller. This
     * matches the spec usage where request and response may live on
     * different activity contexts (e.g. SS7 dialog vs gRPC correlation).
     * The {@link com.microjainslee.core.MicroSleeContainer} is required for
     * the response leg because {@link SleeEndpointPort} only fires via
     * activity handle, not via an existing ACI reference.</p>
     */
    public void requestMenu(String sessionId, String msisdn, String ussdString,
                            ActivityContextInterface responseAci) {
        if (upstream == null) {
            LOG.warn("gRPC menu RA requestMenu called before setGrpcMenuUpstream");
            return;
        }
        if (eventFactory == null) {
            LOG.warn("gRPC menu RA requestMenu called before setEventFactory");
            return;
        }
        if (activityContextLookup == null) {
            LOG.warn("gRPC menu RA requestMenu called before setActivityContextLookup");
            return;
        }
        ActivityContextInterface sessionAci = activityContextLookup.lookup(sessionId);
        if (sessionAci == null) {
            LOG.warn(() -> "gRPC menu RA unknown session activity: " + sessionId);
            return;
        }
        // Sprint S8 - stamp a per-session monotonic sequence number.
        long seq = sequenceCounters
                .computeIfAbsent(sessionId, k -> new AtomicLong(0L))
                .incrementAndGet();
        // Request event: session ACI via SleeEndpointPort (canonical hot path).
        // Use the sequence-aware overload so a SequencedEvent factory
        // can record the seq for dedup; legacy 3-arg factories get
        // the default fallback (seq dropped) via the interface default.
        publish(sessionId,
                eventFactory.createRequestEvent(sessionId, msisdn, ussdString, seq));
        workerPool.submit(() -> doCall(sessionId, msisdn, ussdString, responseAci));
    }

    /**
     * Diagnostic accessor - current sequence counter for {@code sessionId}
     * (or {@code 0} when no request has been seen for the session).
     */
    public long currentSequenceFor(String sessionId) {
        if (sessionId == null) return 0L;
        AtomicLong c = sequenceCounters.get(sessionId);
        return c == null ? 0L : c.get();
    }

    private void doCall(String sessionId, String msisdn, String ussdString,
                        ActivityContextInterface responseAci) {
        SleeEvent responseEvent;
        try {
            GrpcMenuUpstreamResult resp = upstream.resolveMenu(msisdn, ussdString, sessionId);
            responseEvent = eventFactory.createResponseEvent(
                    resp.getSessionId(), resp.getStatus(), resp.getMenuText(), resp.getError());
        } catch (Throwable t) {
            LOG.warn("gRPC menu RA call failed for session={}", sessionId, t);
            responseEvent = eventFactory.createErrorResponseEvent(sessionId, t);
        }
        routeResponse(responseAci, responseEvent);
    }

    /**
     * Route a response event to the caller-supplied ACI via the live
     * container. Returns silently when the response ACI is {@code null}
     * (caller did not opt into separate response activity).
     */
    private void routeResponse(ActivityContextInterface responseAci, SleeEvent event) {
        if (responseAci == null) {
            return;
        }
        Object c = container();
        if (c instanceof com.microjainslee.core.MicroSleeContainer mc) {
            mc.routeEvent(event, responseAci);
            return;
        }
        LOG.warn(() -> "gRPC menu RA cannot route response to ACI - "
                + "no live MicroSleeContainer available; response event dropped");
    }
}
