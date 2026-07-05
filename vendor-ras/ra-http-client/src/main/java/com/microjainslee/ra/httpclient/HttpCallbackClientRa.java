/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient;

import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Outbound HTTP callback RA on <b>Vert.x WebClient</b> — pooled keep-alive
 * connections, non-blocking sends, GraalVM-native ready (same client
 * family the Quarkus ecosystem rides on).
 *
 * <p>Delivery policy: POST JSON, up to {@link #setMaxRetries(int)} retries
 * with exponential backoff for connect errors and 5xx responses. 4xx is
 * treated as a permanent receiver error (no retry).</p>
 */
public final class HttpCallbackClientRa extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpCallbackClientRa.class);

    private Vertx vertx;
    private WebClient webClient;
    private int connectTimeoutMs = 10_000;
    private int requestTimeoutMs = 15_000;
    private int maxRetries = 2;
    private long retryBackoffMs = 500;

    public void setConnectTimeoutMs(int ms) { this.connectTimeoutMs = ms; }
    public void setRequestTimeoutMs(int ms) { this.requestTimeoutMs = ms; }
    public void setMaxRetries(int n) { this.maxRetries = Math.max(0, n); }
    public void setRetryBackoffMs(long ms) { this.retryBackoffMs = ms; }

    @Override
    public void raConfigure() {
        vertx = Vertx.vertx();
        webClient = WebClient.create(vertx, new WebClientOptions()
                .setConnectTimeout(connectTimeoutMs)
                .setKeepAlive(true)
                .setMaxPoolSize(64)
                .setTcpNoDelay(true));
        LOG.info("HTTP callback client RA configured (Vert.x WebClient, retries={})", maxRetries);
    }

    @Override
    public void raActive() {
        LOG.info("HTTP callback client RA active");
    }

    @Override
    public void raStopping() {
        LOG.info("HTTP callback client RA stopping");
    }

    @Override
    public void raInactive() {
        // keep client until unconfigure — a stopping RA may still flush callbacks
    }

    @Override
    public void raUnconfigure() {
        if (webClient != null) {
            webClient.close();
            webClient = null;
        }
        if (vertx != null) {
            CountDownLatch closed = new CountDownLatch(1);
            vertx.close().onComplete(v -> closed.countDown());
            try {
                closed.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            vertx = null;
        }
        super.raUnconfigure();
    }

    /**
     * Fire-and-forget JSON callback delivery with bounded retries.
     * Non-blocking — safe to call from SBB entity threads.
     */
    public void sendCallback(String sessionId, String callbackUrl, String responseText) {
        if (callbackUrl == null || callbackUrl.isBlank()) {
            LOG.debug(() -> "HTTP callback client RA: no callback URL for session " + sessionId);
            return;
        }
        WebClient client = this.webClient;
        if (client == null) {
            LOG.warn("HTTP callback RA not configured — callback for session {} dropped", sessionId);
            return;
        }
        String payload = "{\"sessionId\":\"" + escapeJson(sessionId)
                + "\",\"status\":\"OK\",\"responseText\":\"" + escapeJson(responseText) + "\"}";
        attemptSend(client, sessionId, callbackUrl, payload, 0);
    }

    private void attemptSend(WebClient client, String sessionId, String callbackUrl,
                             String payload, int attempt) {
        client.postAbs(callbackUrl)
                .timeout(requestTimeoutMs)
                .putHeader("Content-Type", "application/json")
                .sendBuffer(Buffer.buffer(payload))
                .onComplete(res -> {
                    if (res.succeeded()) {
                        int status = res.result().statusCode();
                        if (status < 500) {
                            if (status >= 400) {
                                LOG.warn("HTTP callback RA: POST {} -> {} for session {} "
                                        + "(receiver error, not retried)",
                                        callbackUrl, status, sessionId);
                            } else {
                                LOG.info("HTTP callback RA: POST {} -> {} for session {}",
                                        callbackUrl, status, sessionId);
                            }
                            return;
                        }
                        retryOrGiveUp(client, sessionId, callbackUrl, payload, attempt,
                                "HTTP " + status);
                        return;
                    }
                    retryOrGiveUp(client, sessionId, callbackUrl, payload, attempt,
                            String.valueOf(res.cause() == null ? "?" : res.cause().getMessage()));
                });
    }

    private void retryOrGiveUp(WebClient client, String sessionId, String callbackUrl,
                               String payload, int attempt, String reason) {
        if (attempt >= maxRetries || vertx == null) {
            LOG.warn("HTTP callback RA: giving up after {} attempt(s) for session {} to {} ({})",
                    attempt + 1, sessionId, callbackUrl, reason);
            return;
        }
        long delay = retryBackoffMs * (1L << attempt); // 500, 1000, 2000, ...
        LOG.info("HTTP callback RA: retry {}/{} in {}ms for session {} ({})",
                attempt + 1, maxRetries, delay, sessionId, reason);
        vertx.setTimer(delay, id ->
                attemptSend(client, sessionId, callbackUrl, payload, attempt + 1));
    }

    /** Minimal JSON string escaping — replaces backslash, double-quote, and control chars. */
    private static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
