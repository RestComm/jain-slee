/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

/**
 * Trust ladder for the AI agent. Start at {@link #ADVISORY}, promote only
 * after the agent has earned trust in your environment.
 */
public enum AIMode {
    /** Analyze and report only — never executes an action. */
    ADVISORY,
    /** Executes only HIGH-confidence (≥ 0.85) validated actions; the rest are reported. */
    SEMI_AUTO,
    /** Executes every validated action above the configured confidence threshold. */
    FULL_AUTO;

    /** Lenient parse — unknown/blank input falls back to ADVISORY (safety first). */
    public static AIMode parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return ADVISORY;
        }
        try {
            return valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ADVISORY;
        }
    }
}
