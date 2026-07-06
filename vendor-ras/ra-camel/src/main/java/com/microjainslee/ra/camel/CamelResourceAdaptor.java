/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.camel.CamelRaConfig.CamelConsumerSpec;
import com.microjainslee.ra.camel.collab.CamelActivityRegistry;
import com.microjainslee.ra.camel.collab.CamelEventFactory;
import com.microjainslee.ra.camel.collab.PendingReplyRegistry;
import com.microjainslee.ra.camel.command.CamelCommand;
import com.microjainslee.ra.camel.command.EndCamelActivity;
import com.microjainslee.ra.camel.command.ReplyToExchange;
import com.microjainslee.ra.camel.command.RequestFromEndpoint;
import com.microjainslee.ra.camel.command.SendToEndpoint;
import com.microjainslee.ra.camel.events.CamelInboundEvent;
import com.microjainslee.ra.camel.events.CamelResponseEvent;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Generic Camel Resource Adaptor — one RA for <b>every</b> Camel / Camel
 * Quarkus component.
 *
 * <h2>How it works</h2>
 * <ul>
 *   <li>For each {@link CamelConsumerSpec} the RA adds a route
 *       {@code from(uri).process(...)}. Camel resolves the component from
 *       the URI scheme by its standard discovery — the user only has to
 *       put the component artifact on the classpath
 *       ({@code camel-quarkus-kafka}, {@code camel-quarkus-platform-http},
 *       {@code camel-quarkus-grpc}, ...).</li>
 *   <li>Every consumed Exchange becomes a {@link CamelInboundEvent}
 *       (or an app-typed event via {@link CamelEventFactory}) fired on an
 *       activity keyed by the configured correlation header — so stateful
 *       SBB session routing works out of the box.</li>
 *   <li>SBB commands: {@link SendToEndpoint} (InOnly),
 *       {@link RequestFromEndpoint} (async InOut →
 *       {@link CamelResponseEvent}), {@link ReplyToExchange} (answer an
 *       in-out consumer exchange), {@link EndCamelActivity}.</li>
 * </ul>
 *
 * <h2>Quarkus / Camel Quarkus</h2>
 * Add {@code camel-quarkus-core} + the extensions you need, inject the
 * managed {@link CamelContext} and hand it over via
 * {@link #setCamelContext(CamelContext)} <b>before</b> registering the RA.
 * The RA then only adds routes (context lifecycle stays with Quarkus, and
 * the extensions keep their GraalVM-native support). Standalone (no
 * Quarkus) the RA owns a {@link DefaultCamelContext}.
 */
public final class CamelResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(CamelResourceAdaptor.class);

    private CamelRaConfig config = new CamelRaConfig();
    private RaBootstrapPort bootstrap;
    private CamelContext camelContext;      // externally provided (Quarkus) or owned
    private boolean ownsContext;
    private ProducerTemplate template;
    private CamelEventFactory eventFactory = CamelInboundEvent::new;

    private final CamelActivityRegistry activities = new CamelActivityRegistry();
    private final PendingReplyRegistry pendingReplies = new PendingReplyRegistry();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private ExecutorService requestPool;
    private ScheduledExecutorService sweeper;

    // ── collaborator injection ──────────────────────────────────────

    public void setConfig(CamelRaConfig config) {
        this.config = config;
    }

    public void setBootstrapPort(RaBootstrapPort bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * Provide an externally managed CamelContext (Camel Quarkus). When
     * set, the RA only adds/removes its routes and never starts or stops
     * the context.
     */
    public void setCamelContext(CamelContext context) {
        this.camelContext = context;
    }

    /** Override the default {@link CamelInboundEvent} with app-typed events. */
    public void setEventFactory(CamelEventFactory factory) {
        this.eventFactory = factory != null ? factory : CamelInboundEvent::new;
    }

    public CamelRaConfig config() { return config; }
    public CamelContext camelContext() { return camelContext; }
    public boolean isActive() { return active.get(); }
    public int activeActivityCount() { return activities.size(); }
    public int pendingReplyCount() { return pendingReplies.size(); }
    public CamelActivityRegistry activityRegistry() { return activities; }

    // ── lifecycle ───────────────────────────────────────────────────

    public void raConfigure() {
        LOG.info("[ra-camel:{}] raConfigure consumers={} replyTimeoutMs={}",
                config.name(), config.consumers().size(), config.replyTimeoutMillis());
    }

    public void raActive() {
        if (!active.compareAndSet(false, true)) return;
        try {
            if (camelContext == null) {
                camelContext = new DefaultCamelContext();
                ownsContext = true;
            }
            for (CamelConsumerSpec spec : config.consumers()) {
                camelContext.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() {
                        from(spec.uri())
                                .routeId(routeIdFor(spec))
                                .process(exchange -> onExchange(spec, exchange));
                    }
                });
            }
            if (ownsContext) {
                camelContext.start();
            }
            template = camelContext.createProducerTemplate();
        } catch (Exception e) {
            active.set(false);
            throw new IllegalStateException("Failed to activate Camel RA ["
                    + config.name() + "]", e);
        }

        requestPool = Executors.newVirtualThreadPerTaskExecutor();
        if (config.activityIdleSecs() > 0) {
            long sweep = Math.max(1, config.activitySweepIntervalSecs());
            sweeper = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "camel-ra-activity-sweeper");
                t.setDaemon(true);
                return t;
            });
            sweeper.scheduleAtFixedRate(this::sweepIdleActivities, sweep, sweep, TimeUnit.SECONDS);
        }
        LOG.info("[ra-camel:{}] ACTIVE routes={} contextOwned={}",
                config.name(), config.consumers().size(), ownsContext);
    }

    public void raInactive() {
        if (!active.compareAndSet(true, false)) return;
        if (sweeper != null) { sweeper.shutdownNow(); sweeper = null; }
        if (requestPool != null) { requestPool.shutdown(); requestPool = null; }
        // Fail all pending replies so blocked consumer exchanges error out fast.
        pendingReplies.cancelAll();
        activities.clear();
        try {
            if (template != null) { template.stop(); template = null; }
            if (camelContext != null) {
                for (CamelConsumerSpec spec : config.consumers()) {
                    try {
                        camelContext.getRouteController().stopRoute(routeIdFor(spec));
                        camelContext.removeRoute(routeIdFor(spec));
                    } catch (Exception e) {
                        LOG.debug("[ra-camel:{}] route {} removal: {}",
                                config.name(), routeIdFor(spec), e.getMessage());
                    }
                }
                if (ownsContext) {
                    camelContext.stop();
                    camelContext = null;
                    ownsContext = false;
                }
            }
        } catch (Exception e) {
            LOG.warn("[ra-camel:{}] shutdown issue", config.name(), e);
        }
        LOG.info("[ra-camel:{}] INACTIVE", config.name());
    }

    public void raUnconfigure() {
        raInactive();
    }

    // ── inbound: Exchange → SleeEvent ───────────────────────────────

    private void onExchange(CamelConsumerSpec spec, Exchange exchange) throws Exception {
        RaBootstrapPort bp = this.bootstrap;
        if (bp == null) {
            LOG.warn("[ra-camel:{}] bootstrapPort not set — exchange {} dropped",
                    config.name(), exchange.getExchangeId());
            return;
        }
        Object body = exchange.getMessage().getBody();
        Map<String, Object> headers = new HashMap<>(exchange.getMessage().getHeaders());
        String exchangeId = exchange.getExchangeId();
        String activityId = resolveActivityId(spec, headers, exchangeId);

        CamelActivityRegistry.Entry activity = activities.acquire(activityId, bp);

        boolean requiresReply = spec.isInOut();
        CompletableFuture<ReplyToExchange> replyFuture = null;
        if (requiresReply) {
            replyFuture = pendingReplies.register(exchangeId);
        }

        SleeEvent event = eventFactory.create(
                spec.uri(), exchangeId, activityId, body, Map.copyOf(headers), requiresReply);
        bp.fireEvent(event, activity.handle(), null);

        if (requiresReply) {
            // Bounded wait on the Camel consumer thread. Camel components
            // that need a reply (platform-http, grpc, netty...) hold the
            // wire transaction open anyway; the timeout guarantees the
            // thread is always released.
            try {
                ReplyToExchange reply = replyFuture.get(
                        config.replyTimeoutMillis(), TimeUnit.MILLISECONDS);
                exchange.getMessage().setBody(reply.body());
                if (reply.headers() != null) {
                    exchange.getMessage().getHeaders().putAll(reply.headers());
                }
            } catch (TimeoutException te) {
                throw new IllegalStateException("No SBB reply within "
                        + config.replyTimeoutMillis() + "ms for exchange " + exchangeId);
            } catch (ExecutionException | java.util.concurrent.CancellationException e) {
                throw new IllegalStateException("Reply failed for exchange " + exchangeId, e);
            } finally {
                pendingReplies.discard(exchangeId);
            }
        }

        // No correlation configured → the activity lives for exactly one
        // exchange; end it now so nothing leaks.
        if (spec.correlationHeader() == null) {
            endActivity(activityId);
        }
    }

    private static String resolveActivityId(CamelConsumerSpec spec,
            Map<String, Object> headers, String exchangeId) {
        if (spec.correlationHeader() != null) {
            Object value = headers.get(spec.correlationHeader());
            if (value != null && !String.valueOf(value).isBlank()) {
                return String.valueOf(value);
            }
        }
        return exchangeId;
    }

    // ── outbound: SBB command → Camel producer ─────────────────────

    public void sendOutbound(CamelCommand command) {
        if (command == null) return;
        ProducerTemplate t = this.template;
        switch (command) {
            case ReplyToExchange reply -> {
                if (!pendingReplies.complete(reply.exchangeId(), reply)) {
                    LOG.warn("[ra-camel:{}] reply for unknown/expired exchange {} — dropped",
                            config.name(), reply.exchangeId());
                }
            }
            case SendToEndpoint send -> {
                if (t == null) { warnInactive(command); return; }
                try {
                    t.sendBodyAndHeaders(send.uri(), send.body(),
                            send.headers() == null ? Map.of() : send.headers());
                } catch (Exception e) {
                    LOG.error("[ra-camel:{}] send to {} failed", config.name(), send.uri(), e);
                }
            }
            case RequestFromEndpoint request -> {
                if (t == null || requestPool == null) { warnInactive(command); return; }
                requestPool.submit(() -> doRequest(request));
            }
            case EndCamelActivity end -> endActivity(end.activityId());
        }
    }

    private void doRequest(RequestFromEndpoint request) {
        RaBootstrapPort bp = this.bootstrap;
        SleeEvent responseEvent;
        try {
            Object response = template.requestBodyAndHeaders(request.uri(), request.body(),
                    request.headers() == null ? Map.of() : request.headers());
            responseEvent = new CamelResponseEvent(
                    request.correlationId(), request.uri(), response, Map.of(), null);
        } catch (Exception e) {
            LOG.warn("[ra-camel:{}] request to {} failed: {}",
                    config.name(), request.uri(), e.getMessage());
            responseEvent = new CamelResponseEvent(
                    request.correlationId(), request.uri(), null, null,
                    e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        }
        if (bp == null) return;
        CamelActivityRegistry.Entry activity =
                activities.acquire(request.correlationId(), bp);
        bp.fireEvent(responseEvent, activity.handle(), null);
    }

    // ── activity lifecycle ──────────────────────────────────────────

    public void endActivity(String activityId) {
        if (activityId == null) return;
        CamelActivityRegistry.Entry activity = activities.remove(activityId);
        if (activity != null && bootstrap != null) {
            bootstrap.endActivity(activity.handle());
        }
    }

    private void sweepIdleActivities() {
        try {
            long idleMillis = TimeUnit.SECONDS.toMillis(config.activityIdleSecs());
            for (Map.Entry<String, CamelActivityRegistry.Entry> expired
                    : activities.expireIdle(idleMillis)) {
                LOG.info("[ra-camel:{}] expiring idle activity {}",
                        config.name(), expired.getKey());
                if (bootstrap != null) {
                    bootstrap.endActivity(expired.getValue().handle());
                }
            }
        } catch (RuntimeException e) {
            LOG.warn("[ra-camel:{}] activity sweep failed", config.name(), e);
        }
    }

    // ── helpers ─────────────────────────────────────────────────────

    private String routeIdFor(CamelConsumerSpec spec) {
        return config.name() + ":" + spec.uri();
    }

    private void warnInactive(CamelCommand command) {
        LOG.warn("[ra-camel:{}] RA not active — {} dropped",
                config.name(), command.getClass().getSimpleName());
    }
}
