/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Configuration for the generic Camel RA.
 *
 * <p>The whole point of this RA: the user only declares <b>which Camel
 * endpoints to consume</b> — any component from the Camel / Camel Quarkus
 * catalogue works as long as its artifact is on the classpath
 * ({@code camel-quarkus-kafka}, {@code camel-quarkus-platform-http},
 * {@code camel-quarkus-grpc}, {@code camel-quarkus-paho-mqtt5}, ...).
 * Camel resolves the component from the URI scheme per its standard
 * discovery; the RA never hard-codes any component.</p>
 *
 * <pre>{@code
 * CamelRaConfig config = new CamelRaConfig()
 *     .name("camel-ra")
 *     .consume(CamelConsumerSpec.inOnly("kafka:orders?brokers=localhost:9092")
 *                               .correlatedBy("kafka.KEY"))
 *     .consume(CamelConsumerSpec.inOut("platform-http:/api/charge")
 *                               .correlatedBy("sessionId"));
 * }</pre>
 */
public final class CamelRaConfig {

    /** One consumer route the RA will create. */
    public static final class CamelConsumerSpec {
        private final String uri;
        private final boolean inOut;
        private String correlationHeader;

        private CamelConsumerSpec(String uri, boolean inOut) {
            if (uri == null || uri.isBlank()) {
                throw new IllegalArgumentException("consumer uri is required");
            }
            this.uri = uri;
            this.inOut = inOut;
        }

        /** Fire-and-forget consumer (events only, no reply). */
        public static CamelConsumerSpec inOnly(String uri) {
            return new CamelConsumerSpec(uri, false);
        }

        /**
         * Request/reply consumer: the Camel exchange waits (bounded by
         * {@link CamelRaConfig#replyTimeoutMillis()}) until an SBB sends
         * {@code ReplyToExchange(exchangeId, ...)}.
         */
        public static CamelConsumerSpec inOut(String uri) {
            return new CamelConsumerSpec(uri, true);
        }

        /**
         * Use this message header as the SLEE activity id, so every
         * exchange carrying the same header value converges on the same
         * activity (and therefore the same stateful SBB entity). Without
         * it each exchange gets its own short-lived activity.
         */
        public CamelConsumerSpec correlatedBy(String headerName) {
            this.correlationHeader = headerName;
            return this;
        }

        public String uri() { return uri; }
        public boolean isInOut() { return inOut; }
        public String correlationHeader() { return correlationHeader; }
    }

    private String name = "camel-ra";
    private final List<CamelConsumerSpec> consumers = new ArrayList<>();
    private long replyTimeoutMillis = 30_000L;
    private long activityIdleSecs = 300L;
    private long activitySweepIntervalSecs = 30L;

    /** RA name — the value SBBs use in {@code @InjectRa(name = ...)}. */
    public CamelRaConfig name(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("ra name is required");
        }
        this.name = name;
        return this;
    }

    public CamelRaConfig consume(CamelConsumerSpec spec) {
        consumers.add(spec);
        return this;
    }

    /** Max time an in-out exchange waits for the SBB reply. */
    public CamelRaConfig replyTimeoutMillis(long millis) {
        this.replyTimeoutMillis = millis;
        return this;
    }

    /** Idle seconds before an activity is expired (0 = never expire). */
    public CamelRaConfig activityIdleSecs(long secs) {
        this.activityIdleSecs = secs;
        return this;
    }

    public CamelRaConfig activitySweepIntervalSecs(long secs) {
        this.activitySweepIntervalSecs = secs;
        return this;
    }

    public String name() { return name; }
    public List<CamelConsumerSpec> consumers() { return Collections.unmodifiableList(consumers); }
    public long replyTimeoutMillis() { return replyTimeoutMillis; }
    public long activityIdleSecs() { return activityIdleSecs; }
    public long activitySweepIntervalSecs() { return activitySweepIntervalSecs; }
}
