/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link AIAdvisor} over the OpenAI-compatible {@code /chat/completions}
 * dialect — one small class covers DeepSeek (default), OpenAI, Ollama, vLLM,
 * LM Studio and most gateways just by changing {@code base-url}.
 *
 * <p>Pure JDK {@link HttpClient} + Jackson: zero framework, GraalVM-native
 * friendly. A tiny circuit breaker opens after 3 consecutive failures and
 * half-opens after 60s, so a dead endpoint costs one cheap check per minute
 * instead of a hung loop.</p>
 *
 * <p>Thread-safe: the circuit breaker state uses atomic counters and
 * {@link #analyze}/{@link #report} can be called concurrently. The
 * underlying {@link HttpClient} is inherently concurrent.</p>
 */
public final class OpenAiCompatAdvisor implements AIAdvisor {

    private static final Logger LOG = LogManager.getLogger(OpenAiCompatAdvisor.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final int CIRCUIT_OPEN_AFTER = 3;
    private static final long CIRCUIT_RETRY_MILLIS = 60_000L;

    /**
     * Test seam / transport abstraction: request JSON in → response body out.
     * The default transport is a real {@link HttpClient} call; tests inject
     * a lambda that returns canned responses.
     */
    @FunctionalInterface
    public interface HttpTransport {
        /**
         * Sends a JSON request body and returns the full response body.
         *
         * @param requestJson the serialised chat-completions request
         * @return raw response JSON
         * @throws IOException on transport or HTTP error
         * @throws InterruptedException if the calling thread is interrupted
         */
        String send(String requestJson) throws IOException, InterruptedException;
    }

    private final AIAgentConfig config;
    private final HttpTransport transport;
    private final AtomicInteger consecutiveFailures = new AtomicInteger();
    private final AtomicLong lastFailureAt = new AtomicLong();

    /**
     * Creates an advisor that calls the configured endpoint via a real
     * {@link HttpClient}.
     *
     * @param config endpoint, model, timeout and API-key configuration
     */
    public OpenAiCompatAdvisor(AIAgentConfig config) {
        this(config, defaultTransport(config));
    }

    /**
     * Creates an advisor with a custom transport (test seam).
     *
     * @param config endpoint and model configuration
     * @param transport the transport to use for HTTP calls
     */
    public OpenAiCompatAdvisor(AIAgentConfig config, HttpTransport transport) {
        this.config = config;
        this.transport = transport;
    }

    private static HttpTransport defaultTransport(AIAgentConfig cfg) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(cfg.timeoutSeconds()))
                .build();
        String url = cfg.baseUrl().replaceAll("/+$", "") + "/chat/completions";
        return body -> {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(cfg.timeoutSeconds()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + cfg.apiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new IOException("AI endpoint HTTP " + response.statusCode()
                        + ": " + truncate(response.body()));
            }
            return response.body();
        };
    }

    // ── AIAdvisor ────────────────────────────────────────────────────

    @Override
    public AIAnalysis analyze(TelemetrySnapshot snapshot) {
        String content = complete(PromptBuilder.analysisSystemPrompt(),
                PromptBuilder.snapshotJson(snapshot));
        return content == null ? AIAnalysis.unparsed("(AI unavailable)")
                : ResponseParser.parse(content);
    }

    @Override
    public String report(ReportAudience audience, TelemetrySnapshot snapshot) {
        String content = complete(PromptBuilder.reportSystemPrompt(audience),
                PromptBuilder.snapshotJson(snapshot));
        return content == null ? "(AI unavailable — check configuration and endpoint)" : content;
    }

    @Override
    public boolean isAvailable() {
        if (!config.hasCredentials()) {
            return false;
        }
        if (consecutiveFailures.get() < CIRCUIT_OPEN_AFTER) {
            return true;
        }
        // Circuit open — half-open after the retry window.
        return System.currentTimeMillis() - lastFailureAt.get() >= CIRCUIT_RETRY_MILLIS;
    }

    // ── internals ────────────────────────────────────────────────────

    /**
     * One chat completion: sends system + user prompts, returns the
     * assistant content string. Returns null on any failure (circuit open,
     * transport error, interrupted) — callers degrade gracefully.
     *
     * @param systemPrompt the system-level instruction
     * @param userPrompt the user-level data/message
     * @return assistant reply content, or null on failure
     */
    private String complete(String systemPrompt, String userPrompt) {
        if (!isAvailable()) {
            return null;
        }
        try {
            String response = transport.send(requestBody(systemPrompt, userPrompt));
            consecutiveFailures.set(0);
            return extractContent(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            int failures = consecutiveFailures.incrementAndGet();
            lastFailureAt.set(System.currentTimeMillis());
            LOG.warn("[ai] completion failed ({} consecutive): {}", failures, e.getMessage());
            return null;
        }
    }

    /**
     * Builds the JSON request body for a chat-completions call with
     * deterministic temperature (0.2) suited for ops analysis.
     */
    private String requestBody(String systemPrompt, String userPrompt) {
        ObjectNode body = MAPPER.createObjectNode();
        body.put("model", config.model());
        body.put("temperature", 0.2);   // ops analysis wants determinism, not creativity
        ArrayNode messages = body.putArray("messages");
        messages.addObject().put("role", "system").put("content", systemPrompt);
        messages.addObject().put("role", "user").put("content", userPrompt);
        return body.toString();
    }

    /**
     * Extracts the assistant's text content from a chat-completions
     * response envelope. Package-visible for direct testing.
     *
     * @param responseJson the full JSON response body
     * @return the assistant's message content string
     * @throws IOException if the expected path is missing or null
     */
    static String extractContent(String responseJson) throws IOException {
        JsonNode root = MAPPER.readTree(responseJson);
        JsonNode content = root.path("choices").path(0).path("message").path("content");
        if (content.isMissingNode() || content.isNull()) {
            throw new IOException("no choices[0].message.content in AI response");
        }
        return content.asText();
    }

    private static String truncate(String s) {
        return s == null ? "" : s.length() > 200 ? s.substring(0, 200) + "…" : s;
    }
}
