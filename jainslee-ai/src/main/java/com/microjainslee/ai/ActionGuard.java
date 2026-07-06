/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.microjainslee.ai.AIAnalysis.Recommendation;

import java.util.List;
import java.util.Set;

/**
 * Guardrail layer between the model and the runtime. An LLM reply is input,
 * not authority: only allow-listed actions above the confidence threshold
 * pass, everything else is reported but never executed.
 *
 * <p>Thread-safe: all state is immutable after construction. Instances are
 * cheap and can be created per engine cycle if desired, though the default
 * usage is a single guard per {@link AIAgentEngine}.</p>
 */
public final class ActionGuard {

    /** The complete control surface the AI may touch. Deliberately small. */
    public static final Set<String> ALLOWED_ACTIONS = Set.of(
            "TRIGGER_RELIEF",
            "ENABLE_AUTO_RECONFIG",
            "DISABLE_AUTO_RECONFIG",
            "RAISE_ALARM",
            "INVESTIGATE",
            "NONE");

    /** Actions that only observe/annotate — executable even in SEMI_AUTO at any confidence. */
    public static final Set<String> PASSIVE_ACTIONS = Set.of("RAISE_ALARM", "INVESTIGATE", "NONE");

    private final double confidenceThreshold;

    /**
     * Creates a guard with the given confidence threshold.
     *
     * @param confidenceThreshold minimum confidence (0.0–1.0) for a
     *        recommendation to pass the guard in FULL_AUTO mode;
     *        values outside [0, 1] are clamped
     */
    public ActionGuard(double confidenceThreshold) {
        this.confidenceThreshold = Math.max(0.0, Math.min(1.0, confidenceThreshold));
    }

    /**
     * Filters recommendations down to those allowed to execute under
     * the given mode. The rules are:
     * <ul>
     *   <li>{@link AIMode#ADVISORY} — returns an empty list, always.</li>
     *   <li>{@link AIMode#FULL_AUTO} — all allow-listed actions above the
     *       confidence threshold pass.</li>
     *   <li>{@link AIMode#SEMI_AUTO} — passive actions pass at the
     *       threshold; mutating actions require ≥ 0.85 confidence.</li>
     * </ul>
     *
     * @param recs the raw recommendations from the model (may be empty)
     * @param mode the current trust mode
     * @return filtered list of executable recommendations (never null)
     */
    public List<Recommendation> executable(List<Recommendation> recs, AIMode mode) {
        if (mode == AIMode.ADVISORY) {
            return List.of();   // advisory mode executes nothing, ever
        }
        return recs.stream()
                .filter(r -> ALLOWED_ACTIONS.contains(r.action()))
                .filter(r -> r.confidence() >= confidenceThreshold)
                .filter(r -> mode == AIMode.FULL_AUTO
                        || PASSIVE_ACTIONS.contains(r.action())
                        || r.confidence() >= 0.85)   // SEMI_AUTO: mutating actions need HIGH confidence
                .toList();
    }

    /**
     * The complement of {@link #executable}: recommendations that were
     * dropped by the guard. Useful for audit logging and operator dashboards.
     *
     * @param recs the raw recommendations from the model
     * @param mode the current trust mode
     * @return recommendations that will NOT be executed (never null)
     */
    public List<Recommendation> rejected(List<Recommendation> recs, AIMode mode) {
        List<Recommendation> ok = executable(recs, mode);
        return recs.stream().filter(r -> !ok.contains(r)).toList();
    }
}
