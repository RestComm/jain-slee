/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import java.util.function.Function;

/**
 * AI agent configuration — read from the application's own config source
 * (Quarkus {@code application.properties}, Spring, plain {@link java.util.Properties})
 * via a simple {@code key → value} lookup function, so this module stays
 * framework-free.
 *
 * <h3>application.properties keys</h3>
 * <pre>
 * microjainslee.ai.enabled=false
 * microjainslee.ai.api-key=            # or env MICROJAINSLEE_AI_API_KEY / DEEPSEEK_API_KEY
 * microjainslee.ai.base-url=https://api.deepseek.com/v1
 * microjainslee.ai.model=deepseek-chat
 * microjainslee.ai.mode=ADVISORY       # ADVISORY | SEMI_AUTO | FULL_AUTO
 * microjainslee.ai.interval-seconds=60
 * microjainslee.ai.timeout-seconds=15
 * microjainslee.ai.confidence-threshold=0.70
 * microjainslee.ai.action-cooldown-seconds=300
 * </pre>
 *
 * <p>The API key is <b>never</b> logged and should come from an environment
 * variable in production, not a committed file.</p>
 */
public record AIAgentConfig(
        boolean enabled,
        String apiKey,
        String baseUrl,
        String model,
        AIMode mode,
        long intervalSeconds,
        long timeoutSeconds,
        double confidenceThreshold,
        long actionCooldownSeconds
) {

    public static final String PREFIX = "microjainslee.ai.";

    /** Sensible defaults: disabled, DeepSeek endpoint, ADVISORY. */
    public static AIAgentConfig defaults() {
        return new AIAgentConfig(false, "", "https://api.deepseek.com/v1",
                "deepseek-chat", AIMode.ADVISORY, 60, 15, 0.70, 300);
    }

    /**
     * Build from a config lookup (e.g. {@code props::getProperty} or a Quarkus
     * {@code Config} lambda). Missing keys fall back to {@link #defaults()};
     * a blank api-key falls back to the {@code MICROJAINSLEE_AI_API_KEY} then
     * {@code DEEPSEEK_API_KEY} environment variables.
     */
    public static AIAgentConfig fromProperties(Function<String, String> lookup) {
        AIAgentConfig d = defaults();
        String apiKey = orDefault(lookup.apply(PREFIX + "api-key"), "");
        if (apiKey.isBlank()) {
            apiKey = firstNonBlank(System.getenv("MICROJAINSLEE_AI_API_KEY"),
                    System.getenv("DEEPSEEK_API_KEY"), "");
        }
        return new AIAgentConfig(
                Boolean.parseBoolean(orDefault(lookup.apply(PREFIX + "enabled"), "false")),
                apiKey,
                orDefault(lookup.apply(PREFIX + "base-url"), d.baseUrl()),
                orDefault(lookup.apply(PREFIX + "model"), d.model()),
                AIMode.parse(lookup.apply(PREFIX + "mode")),
                parseLong(lookup.apply(PREFIX + "interval-seconds"), d.intervalSeconds()),
                parseLong(lookup.apply(PREFIX + "timeout-seconds"), d.timeoutSeconds()),
                parseDouble(lookup.apply(PREFIX + "confidence-threshold"), d.confidenceThreshold()),
                parseLong(lookup.apply(PREFIX + "action-cooldown-seconds"), d.actionCooldownSeconds()));
    }

    /**
     * Returns true when there is a usable endpoint + key.
     *
     * @return true if apiKey, baseUrl and model are all non-blank
     */
    public boolean hasCredentials() {
        return !apiKey.isBlank() && !baseUrl.isBlank() && !model.isBlank();
    }

    /**
     * Returns a copy of this config with a different mode.
     *
     * @param newMode the replacement mode
     * @return a new config record with the mode changed
     */
    public AIAgentConfig withMode(AIMode newMode) {
        return new AIAgentConfig(enabled, apiKey, baseUrl, model, newMode,
                intervalSeconds, timeoutSeconds, confidenceThreshold, actionCooldownSeconds);
    }

    /**
     * Returns a copy of this config with a different enabled flag.
     *
     * @param newEnabled the replacement enabled state
     * @return a new config record with enabled changed
     */
    public AIAgentConfig withEnabled(boolean newEnabled) {
        return new AIAgentConfig(newEnabled, apiKey, baseUrl, model, mode,
                intervalSeconds, timeoutSeconds, confidenceThreshold, actionCooldownSeconds);
    }

    /**
     * Returns the trimmed value or {@code def} when the value is null/blank.
     */
    private static String orDefault(String v, String def) {
        return v == null || v.isBlank() ? def : v.trim();
    }

    /**
     * Returns the first non-blank string from the given array, or "".
     */
    private static String firstNonBlank(String... vs) {
        for (String v : vs) {
            if (v != null && !v.isBlank()) {
                return v.trim();
            }
        }
        return "";
    }

    /**
     * Parses a long with fallback; non-numeric values silently return the default.
     */
    private static long parseLong(String v, long def) {
        try {
            return v == null || v.isBlank() ? def : Long.parseLong(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Parses a double with fallback; non-numeric values silently return the default.
     */
    private static double parseDouble(String v, double def) {
        try {
            return v == null || v.isBlank() ? def : Double.parseDouble(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /** Redacted — never leaks the key into logs. */
    @Override
    public String toString() {
        return "AIAgentConfig[enabled=" + enabled + ", baseUrl=" + baseUrl
                + ", model=" + model + ", mode=" + mode
                + ", apiKey=" + (apiKey.isBlank() ? "(unset)" : "****")
                + ", interval=" + intervalSeconds + "s]";
    }
}
