/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import java.util.List;

/**
 * Structured result of one AI analysis pass over a telemetry snapshot.
 *
 * <p>{@code recommendations} is always safe to iterate — a parse failure
 * yields an analysis with an explanatory summary and <b>empty</b>
 * recommendations, so nothing can ever be executed off garbage output.</p>
 *
 * @param summary one-line summary of what the model found
 * @param risks detected risks with severity levels (may be empty)
 * @param recommendations suggested actions (may be empty; safe to iterate)
 * @param parsed true if the model reply was valid JSON, false if degraded
 * @param timestamp epoch millis when this analysis was produced
 */
public record AIAnalysis(
        String summary,
        List<Risk> risks,
        List<Recommendation> recommendations,
        boolean parsed,
        long timestamp
) {

    /** Severity levels for identified risks. */
    public enum RiskLevel { LOW, MEDIUM, HIGH, CRITICAL }

    /**
     * A risk identified by the model.
     *
     * @param description human-readable explanation
     * @param level severity
     * @param metric the specific metric that triggered this risk
     * @param confidence 0.0–1.0 how confident the model is
     */
    public record Risk(String description, RiskLevel level, String metric, double confidence) {}

    /**
     * One recommended action from the model.
     * {@code action} is validated against
     * {@link ActionGuard#ALLOWED_ACTIONS} before anything executes.
     *
     * @param action the action name (TRIGGER_RELIEF, RAISE_ALARM, etc.)
     * @param target the affected component or metric
     * @param reasoning why this action is recommended
     * @param confidence 0.0–1.0 how confident the model is
     */
    public record Recommendation(String action, String target, String reasoning, double confidence) {}

    /**
     * Factory for a degraded analysis produced when the model reply could
     * not be parsed as JSON. Always has empty risks and recommendations
     * — safe to iterate with zero side effects.
     *
     * @param raw the raw model reply (may be null)
     * @return an analysis with {@code parsed = false}
     */
    public static AIAnalysis unparsed(String raw) {
        String trimmed = raw == null ? "" : raw.strip();
        if (trimmed.length() > 500) {
            trimmed = trimmed.substring(0, 500) + "…";
        }
        return new AIAnalysis("(unparsed model reply) " + trimmed,
                List.of(), List.of(), false, System.currentTimeMillis());
    }
}
