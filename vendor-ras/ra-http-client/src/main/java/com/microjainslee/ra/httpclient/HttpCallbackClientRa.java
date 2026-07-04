/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient;

import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Async outbound HTTP callback Resource Adaptor.
 *
 * <p>Purely an outbound client — no HTTP server. Sends asynchronous
 * POST callbacks to external URLs using {@link java.net.http.HttpClient}
 * on a virtual-thread worker pool. Callbacks carry a JSON payload
 * with session status information.</p>
 *
 * <p>Callers use {@link #sendCallback(String, String, String)} to trigger
 * a fire-and-forget POST to the given {@code callbackUrl}.</p>
 */
public final class HttpCallbackClientRa extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(HttpCallbackClientRa.class);

    private ExecutorService workerPool;
    private HttpClient httpClient;

    @Override
    public void raConfigure() {
        workerPool = Executors.newVirtualThreadPerTaskExecutor();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .executor(workerPool)
                .build();
        LOG.info("HTTP callback client RA configured (virtual-thread worker pool)");
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
        httpClient = null;
        super.raUnconfigure();
    }

    /**
     * Sends an async HTTP POST callback to the given URL with session
     * status as a JSON payload.
     *
     * <p>This is a fire-and-forget operation; the POST is submitted to
     * the virtual-thread worker pool. If {@code callbackUrl} is {@code null}
     * or blank, the call is silently logged and skipped.</p>
     *
     * @param sessionId    the USSD session identifier
     * @param callbackUrl  the external URL to POST the callback to
     * @param responseText the final response text to include
     */
    public void sendCallback(String sessionId, String callbackUrl, String responseText) {
        if (callbackUrl == null || callbackUrl.isBlank()) {
            LOG.debug(() -> "HTTP callback client RA: no callback URL for session " + sessionId);
            return;
        }
        workerPool.submit(() -> doSendCallback(sessionId, callbackUrl, responseText));
    }

    private void doSendCallback(String sessionId, String callbackUrl, String responseText) {
        String payload = String.format(
                "{\"sessionId\":\"%s\",\"status\":\"OK\",\"responseText\":\"%s\"}",
                escapeJson(sessionId),
                escapeJson(responseText));
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(callbackUrl))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            LOG.info(() -> "HTTP callback RA: POST " + callbackUrl
                    + " → " + response.statusCode()
                    + " for session " + sessionId);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LOG.warn("HTTP callback RA: POST failed for session {} to {}: {}",
                    sessionId, callbackUrl, e.getMessage());
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
