/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microjainslee.ai.AIAnalysis.Recommendation;
import com.microjainslee.ai.AIAnalysis.Risk;
import com.microjainslee.ai.AIAnalysis.RiskLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Lenient parser for the model's analysis reply. Tolerates markdown fences
 * and leading chatter around the JSON object; on genuine garbage it degrades
 * to {@link AIAnalysis#unparsed(String)} — never throws, never yields
 * executable recommendations from an unparseable reply.
 *
 * <p>Thread-safe: the shared {@link ObjectMapper} is immutable after
 * construction and all methods are pure functions.</p>
 */
public final class ResponseParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ResponseParser() {
    }

    /**
     * Parses a raw model reply into a structured {@link AIAnalysis}.
     * Handles three cases:
     * <ol>
     *   <li>Clean JSON → parsed analysis with risks and recommendations.</li>
     *   <li>Markdown-fenced or chatty JSON → extracts the object and parses.</li>
     *   <li>Garbage / null / blank → returns {@code unparsed} with empty
     *       recommendations (safe to iterate).</li>
     * </ol>
     *
     * @param raw the raw text reply from the model (may be null)
     * @return a structured analysis, never null
     */
    public static AIAnalysis parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return AIAnalysis.unparsed(raw);
        }
        String json = extractJsonObject(raw);
        if (json == null) {
            return AIAnalysis.unparsed(raw);
        }
        try {
            JsonNode root = MAPPER.readTree(json);
            String summary = root.path("summary").asText("");

            List<Risk> risks = new ArrayList<>();
            for (JsonNode r : root.path("risks")) {
                risks.add(new Risk(
                        r.path("description").asText(""),
                        riskLevel(r.path("level").asText("LOW")),
                        r.path("metric").asText(""),
                        clamp(r.path("confidence").asDouble(0.0))));
            }

            List<Recommendation> recs = new ArrayList<>();
            for (JsonNode r : root.path("recommendations")) {
                recs.add(new Recommendation(
                        r.path("action").asText("NONE").trim().toUpperCase(),
                        r.path("target").asText(""),
                        r.path("reasoning").asText(""),
                        clamp(r.path("confidence").asDouble(0.0))));
            }
            return new AIAnalysis(summary, List.copyOf(risks), List.copyOf(recs),
                    true, System.currentTimeMillis());
        } catch (Exception e) {
            return AIAnalysis.unparsed(raw);
        }
    }

    /**
     * Extracts the outermost JSON object from a raw model reply by slicing
     * from the first '{' to the last '}'. This handles ```json fences,
     * leading prose, and trailing chatter.
     *
     * <p><b>Note:</b> this is a simple brace-scan — it works for the
     * flat analysis JSON shape the system prompt mandates, but is not a
     * general JSON extractor for nested objects.</p>
     *
     * @param raw the raw model reply
     * @return the extracted JSON substring, or null if no braces found
     */
    static String extractJsonObject(String raw) {
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start < 0 || end <= start) {
            return null;
        }
        return raw.substring(start, end + 1);
    }

    private static RiskLevel riskLevel(String s) {
        try {
            return RiskLevel.valueOf(s.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return RiskLevel.LOW;
        }
    }

    private static double clamp(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }
}
