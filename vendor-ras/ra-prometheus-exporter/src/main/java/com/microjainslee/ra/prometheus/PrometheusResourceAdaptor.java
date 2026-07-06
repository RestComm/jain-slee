/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.prometheus;

import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.ra.prometheus.collab.PrometheusMetricsStore;
import com.microjainslee.ra.prometheus.events.PrometheusMetricsExportedEvent;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Prometheus metrics exporter Resource Adaptor backed by Vert.x.
 *
 * <p>Exposes two HTTP endpoints:</p>
 * <pre>
 *   GET /metrics → OpenMetrics text/plain (Prometheus scrape target)
 *   GET /health  → {"status":"ok"}
 * </pre>
 *
 * <p>Application SBBs register counters and gauges via the
 * {@link com.microjainslee.ra.prometheus.command.PrometheusCommand}
 * outbound command interface. After each scrape, a
 * {@link PrometheusMetricsExportedEvent} is fired so SBBs can react.</p>
 */
public final class PrometheusResourceAdaptor extends AbstractResourceAdaptor {

    private static final Logger LOG =
            LogManager.getLogger(PrometheusResourceAdaptor.class);

    private Vertx vertx;
    private HttpServer server;
    private PrometheusMetricsStore metricsStore;

    private int port = 9090;
    private String host = "0.0.0.0";
    private int eventLoopThreads = 0; // 0 = Vert.x default

    // ── config setters ──────────────────────────────────────────────

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setEventLoopThreads(int n) {
        this.eventLoopThreads = n;
    }

    public void setMetricsStore(PrometheusMetricsStore store) {
        this.metricsStore = store;
    }

    public PrometheusMetricsStore getMetricsStore() {
        return metricsStore;
    }

    public int port() {
        return server != null ? server.actualPort() : port;
    }

    // ── lifecycle ──────────────────────────────────────────────────

    @Override
    public void raConfigure() {
        if (metricsStore == null) {
            metricsStore = new PrometheusMetricsStore.InMemory();
        }
        LOG.info(() -> "Prometheus exporter RA configured host=" + host
                + " port=" + port);
    }

    @Override
    public void raActive() {
        VertxOptions options = new VertxOptions();
        if (eventLoopThreads > 0) {
            options.setEventLoopPoolSize(eventLoopThreads);
        }
        vertx = Vertx.vertx(options);

        HttpServerOptions serverOptions = new HttpServerOptions()
                .setHost(host)
                .setPort(port)
                .setTcpNoDelay(true)
                .setCompressionSupported(false);

        server = vertx.createHttpServer(serverOptions)
                .requestHandler(this::route);

        CountDownLatch bound = new CountDownLatch(1);
        Throwable[] failure = new Throwable[1];
        server.listen().onComplete(res -> {
            if (res.failed()) {
                failure[0] = res.cause();
            }
            bound.countDown();
        });
        try {
            if (!bound.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException(
                        "Prometheus server bind timed out on " + host + ":" + port);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Prometheus server bind interrupted", e);
        }
        if (failure[0] != null) {
            throw new IllegalStateException(
                    "Failed to start Prometheus server on " + host + ":" + port,
                    failure[0]);
        }
        LOG.info(() -> "Prometheus exporter RA listening on http://" + host + ":"
                + server.actualPort() + " (Vert.x)");
    }

    @Override
    public void raStopping() {
        LOG.info("Prometheus exporter RA stopping");
    }

    @Override
    public void raInactive() {
        CountDownLatch closed = new CountDownLatch(1);
        if (server != null) {
            server.close().onComplete(v -> closed.countDown());
            awaitQuietly(closed);
            server = null;
        }
        if (vertx != null) {
            CountDownLatch vertxClosed = new CountDownLatch(1);
            vertx.close().onComplete(v -> vertxClosed.countDown());
            awaitQuietly(vertxClosed);
            vertx = null;
        }
    }

    // ── routing ────────────────────────────────────────────────────

    private void route(io.vertx.core.http.HttpServerRequest req) {
        String path = req.path();
        if (req.method() == HttpMethod.GET && "/health".equals(path)) {
            writeJson(req.response(), 200, "{\"status\":\"ok\"}");
            return;
        }
        if (req.method() == HttpMethod.GET && "/metrics".equals(path)) {
            handleMetrics(req.response());
            return;
        }
        writeJson(req.response(), 404,
                "{\"error\":\"not found, try /metrics or /health\"}");
    }

    private void handleMetrics(HttpServerResponse response) {
        String prometheusText = scrape();
        int count = metricsStore.count();
        response.putHeader("Content-Type",
                "text/plain; version=0.0.4; charset=utf-8");
        response.setStatusCode(200);
        response.end(prometheusText);

        // Fire post-scrape event on Vert.x worker thread
        vertx.executeBlocking(() -> {
            fireMetricsExportedEvent(count);
            return null;
        }, false).onComplete(res -> {
            if (res.failed()) {
                LOG.error("Post-scrape event fire failed", res.cause());
            }
        });
    }

    // ── SLEE-facing ────────────────────────────────────────────────

    /**
     * Fire a {@link PrometheusMetricsExportedEvent} so application SBBs
     * can react to each scrape cycle.
     */
    void fireMetricsExportedEvent(int count) {
        String sessionId = "prom-scrape-" + UUID.randomUUID();
        PrometheusMetricsExportedEvent event =
                new PrometheusMetricsExportedEvent(count);
        endpoint().fireEvent(
                new SimpleActivityContextHandle(sessionId), event);
    }

    // ── metrics operations ─────────────────────────────────────────

    /** Return Prometheus OpenMetrics text for the current state. */
    public String scrape() {
        return metricsStore.toPrometheusText();
    }

    /** Increment a counter by {@code n}. */
    public void incrementCounter(String name, long n, String... tags) {
        metricsStore.incrementCounter(name, n, tags);
    }

    /** Set a gauge to an absolute value. */
    public void setGauge(String name, double value, String... tags) {
        metricsStore.setGauge(name, value, tags);
    }

    // ── helpers ────────────────────────────────────────────────────

    private static void writeJson(HttpServerResponse response, int status,
                                   String body) {
        response.putHeader("Content-Type", "application/json")
                .setStatusCode(status)
                .end(body);
    }

    private static void awaitQuietly(CountDownLatch latch) {
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
