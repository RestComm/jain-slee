/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.prometheus.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Fired after every successful Prometheus {@code /metrics} scrape.
 *
 * <p>Application SBBs can subscribe to this event to react to scrape
 * cycles (e.g. log stats, reset ephemeral counters, or trigger
 * downstream alert evaluation).
 */
@EventType(name = "PrometheusMetricsExported", vendor = "com.microjainslee", version = "1.0")
public final class PrometheusMetricsExportedEvent implements SleeEvent {

    private final int metricCount;
    private final long timestamp;

    public PrometheusMetricsExportedEvent(int metricCount) {
        this.metricCount = metricCount;
        this.timestamp = System.currentTimeMillis();
    }

    /** Number of metrics included in this scrape. */
    public int getMetricCount() {
        return metricCount;
    }

    /** Epoch millis when the scrape completed. */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PrometheusMetricsExportedEvent{metricCount=" + metricCount
                + ", timestamp=" + timestamp + '}';
    }
}
