package com.microjainslee.telemetry;

import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

/**
 * Prometheus OpenMetrics exporter wrapping a Micrometer MeterRegistry.
 * Uses battle-tested Micrometer for zero-overhead metric collection.
 */
public final class PrometheusExporter {

    private final PrometheusMeterRegistry registry;

    public PrometheusExporter() {
        this(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));
    }

    public PrometheusExporter(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }

    public PrometheusMeterRegistry registry() {
        return registry;
    }

    /** Return OpenMetrics text format for Prometheus scraping. */
    public String scrape() {
        return registry.scrape();
    }
}
