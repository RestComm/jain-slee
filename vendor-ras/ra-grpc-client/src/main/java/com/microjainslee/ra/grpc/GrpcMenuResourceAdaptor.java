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

    // ── transport (owned by the RA, never by the app) ──
    // When a target is configured the RA builds and manages the gRPC
    // ManagedChannel itself. Applications must NOT create channels; they obtain
    // this one via channel() and use it only to build their generated stub —
    // exactly the way an SBB uses an injected RA command port. This keeps all
    // transport (Netty / connection lifecycle) inside the RA.
    private String targetHost;
    private int targetPort = -1;
    private volatile io.grpc.ManagedChannel channel;

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

    /**
     * Configure the upstream gRPC endpoint. When set, the RA owns the
     * {@link io.grpc.ManagedChannel} (plaintext) for its whole active lifetime;
     * the application obtains it via {@link #channel()} to build its generated
     * stub and never creates a channel itself.
     */
    public void setTarget(String host, int port) {
        this.targetHost = host;
        this.targetPort = port;
    }

    /**
     * The RA-managed gRPC channel, or {@code null} when no target was configured
     * (e.g. a test/stub upstream). Applications use it only to build a generated
     * stub — {@code SomeServiceGrpc.newBlockingStub(ra.channel())}.
     */
    public io.grpc.Channel channel() {
        return channel;
    }

    @Override
    public void raConfigure() {
        workerPool = Executors.newVirtualThreadPerTaskExecutor();
        LOG.info("gRPC menu RA configured (virtual-thread worker pool)");
    }

    @Override
    public void raActive() {
        if (targetHost != null && targetPort > 0 && channel == null) {
            channel = io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
                    .forAddress(targetHost, targetPort)
                    .usePlaintext()
                    .build();
            LOG.info("gRPC menu RA opened channel to {}:{}", targetHost, targetPort);
        }
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
        shutdownChannel();
        sequenceCounters.clear();
    }

    @Override
    public void raUnconfigure() {
        if (workerPool != null) {
            workerPool.shutdownNow();
            workerPool = null;
        }
        shutdownChannel();
        sequenceCounters.clear();
        super.raUnconfigure();
    }

    private void shutdownChannel() {
        io.grpc.ManagedChannel c = this.channel;
        if (c != null) {
            c.shutdownNow();
            this.channel = null;
        }
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
        sequenceCounters
                .computeIfAbsent(sessionId, k -> new AtomicLong(0L))
                .incrementAndGet();
        // NOTE: the request event is deliberately NOT re-published here.
        // The command that triggered this call originated from an SBB that
        // already observed the request event on the session ACI — mirroring
        // it back would bounce the same request between that SBB and this
        // RA forever (infinite event loop).
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
        routeResponse(sessionId, responseAci, responseEvent);
    }

    /**
     * Route a response event onto the session. Preferred path: the live
     * container + the caller-supplied ACI. Fallback (3-port wiring, where
     * the bridged context exposes no container): fire through the SLEE
     * endpoint on the session activity handle — never drop the response.
     */
    private void routeResponse(String sessionId, ActivityContextInterface responseAci,
                               SleeEvent event) {
        Object c = container();
        if (responseAci != null && c instanceof com.microjainslee.core.MicroSleeContainer mc) {
            mc.routeEvent(event, responseAci);
            return;
        }
        try {
            publish(sessionId, event);
        } catch (RuntimeException e) {
            LOG.warn("gRPC menu RA could not route response for session={}: {}",
                    sessionId, e.getMessage());
        }
    }
}
