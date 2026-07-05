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

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.ra.httpclient.collab.HttpClientSessionStore;
import com.microjainslee.ra.httpclient.events.HttpCallbackCompletedEvent;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Outbound HTTP callback RA on <b>java.net.http.HttpClient</b> (Java 11+)
 * — non-blocking sends, GraalVM-native ready, zero third-party HTTP
 * dependencies.
 *
 * <p>Delivery policy: POST JSON, up to {@link #setMaxRetries(int)} retries
 * with exponential backoff for connect errors and 5xx responses. 4xx is
 * treated as a permanent receiver error (no retry).
 *
 * <p>On every terminal outcome the RA completes the
 * {@link HttpClientSessionStore} entry and fires an
 * {@link HttpCallbackCompletedEvent} so SBBs can react.
 */
public final class HttpCallbackClientRa extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpCallbackClientRa.class);

    private HttpClient httpClient;
    private ScheduledExecutorService scheduler;
    private HttpClientSessionStore sessionStore;
    private RaBootstrapPort bootstrapPort;
    private final AtomicBoolean active = new AtomicBoolean(false);

    private int connectTimeoutMs = 10_000;
    private int requestTimeoutMs = 15_000;
    private int maxRetries = 2;
    private long retryBackoffMs = 500;

    // -- configuration setters ------------------------------------------

    public void setConnectTimeoutMs(int ms) { this.connectTimeoutMs = ms; }
    public void setRequestTimeoutMs(int ms) { this.requestTimeoutMs = ms; }
    public void setMaxRetries(int n) { this.maxRetries = Math.max(0, n); }
    public void setRetryBackoffMs(long ms) { this.retryBackoffMs = ms; }

    public void setSessionStore(HttpClientSessionStore store) { this.sessionStore = store; }
    public void setBootstrapPort(RaBootstrapPort port) { this.bootstrapPort = port; }

    // -- lifecycle -------------------------------------------------------

    @Override
    public void raConfigure() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .build();
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ra-http-callback-retry");
            t.setDaemon(true);
            return t;
        });
        if (sessionStore == null) {
            sessionStore = new HttpClientSessionStore.InMemoryHttpClientSessionStore();
        }
        LOG.info("HTTP callback client RA configured (java.net.http.HttpClient, retries={})", maxRetries);
    }

    @Override
    public void raActive() {
        active.set(true);
        LOG.info("HTTP callback client RA active");
    }

    @Override
    public void raStopping() {
        active.set(false);
        LOG.info("HTTP callback client RA stopping");
    }

    @Override
    public void raInactive() {
        // keep client until unconfigure — a stopping RA may still flush callbacks
    }

    @Override
    public void raUnconfigure() {
        active.set(false);
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
        httpClient = null;
        super.raUnconfigure();
    }

    // -- send callback ---------------------------------------------------

    /**
     * Fire-and-forget JSON callback delivery with bounded retries.
     * Non-blocking — safe to call from SBB entity threads.
     */
    public void sendCallback(String sessionId, String callbackUrl, String payload) {
        if (callbackUrl == null || callbackUrl.isBlank()) {
            LOG.debug(() -> "HTTP callback client RA: no callback URL for session " + sessionId);
            return;
        }
        if (!active.get()) {
            LOG.warn("HTTP callback RA not active — callback for session {} dropped", sessionId);
            return;
        }
        HttpClient client = this.httpClient;
        if (client == null) {
            LOG.warn("HTTP callback RA not configured — callback for session {} dropped", sessionId);
            return;
        }

        sessionStore.track(sessionId, callbackUrl);
        String json = "{\"sessionId\":\"" + escapeJson(sessionId)
                + "\",\"status\":\"OK\",\"payload\":\"" + escapeJson(payload) + "\"}";
        attemptSend(client, sessionId, callbackUrl, json, 0);
    }

    private void attemptSend(HttpClient client, String sessionId, String callbackUrl,
                             String json, int attempt) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(callbackUrl))
                    .timeout(Duration.ofMillis(requestTimeoutMs))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        } catch (IllegalArgumentException e) {
            LOG.warn("HTTP callback RA: invalid URL '{}' for session {}", callbackUrl, sessionId, e);
            completeWithError(sessionId, 0, "Invalid URL: " + e.getMessage());
            return;
        }

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    int status = res.statusCode();
                    if (status < 500) {
                        if (status >= 400) {
                            LOG.warn("HTTP callback RA: POST {} -> {} for session {} "
                                    + "(receiver error, not retried)",
                                    callbackUrl, status, sessionId);
                        } else {
                            LOG.info("HTTP callback RA: POST {} -> {} for session {}",
                                    callbackUrl, status, sessionId);
                        }
                        completeWithSuccess(sessionId, status, res.body());
                        return;
                    }
                    retryOrGiveUp(client, sessionId, callbackUrl, json, attempt,
                            "HTTP " + status, status, res.body());
                })
                .exceptionally(ex -> {
                    retryOrGiveUp(client, sessionId, callbackUrl, json, attempt,
                            ex.getMessage(), 0, null);
                    return null;
                });
    }

    private void retryOrGiveUp(HttpClient client, String sessionId, String callbackUrl,
                               String json, int attempt, String reason,
                               int lastStatus, String lastBody) {
        if (attempt >= maxRetries || !active.get()) {
            LOG.warn("HTTP callback RA: giving up after {} attempt(s) for session {} to {} ({})",
                    attempt + 1, sessionId, callbackUrl, reason);
            completeWithError(sessionId, lastStatus,
                    "Gave up after " + (attempt + 1) + " attempt(s): " + reason);
            return;
        }
        long delay = retryBackoffMs * (1L << attempt); // 500, 1000, 2000, ...
        LOG.info("HTTP callback RA: retry {}/{} in {}ms for session {} ({})",
                attempt + 1, maxRetries, delay, sessionId, reason);
        ScheduledExecutorService s = this.scheduler;
        if (s != null && !s.isShutdown()) {
            s.schedule(() -> attemptSend(client, sessionId, callbackUrl, json, attempt + 1),
                    delay, TimeUnit.MILLISECONDS);
        }
    }

    // -- completion helpers ----------------------------------------------

    private void completeWithSuccess(String sessionId, int statusCode, String responseBody) {
        sessionStore.complete(sessionId, statusCode, responseBody);
        fireCompletedEvent(sessionId, statusCode, responseBody, null);
    }

    private void completeWithError(String sessionId, int statusCode, String errorMessage) {
        sessionStore.complete(sessionId, statusCode, null);
        fireCompletedEvent(sessionId, statusCode, null, errorMessage);
    }

    private void fireCompletedEvent(String sessionId, int statusCode,
                                    String responseBody, String errorMessage) {
        RaBootstrapPort bp = this.bootstrapPort;
        if (bp == null) {
            LOG.debug("No bootstrap port — skipping HttpCallbackCompletedEvent for session {}", sessionId);
            return;
        }
        try {
            ActivityHandle handle = bp.createActivityHandle(sessionId);
            bp.fireEvent(
                    new HttpCallbackCompletedEvent(sessionId, statusCode, responseBody, errorMessage),
                    handle,
                    null);
        } catch (RuntimeException e) {
            LOG.warn("Failed to fire HttpCallbackCompletedEvent for session {}", sessionId, e);
        }
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
