/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

/**
 * Who a generated report is written for. Same data, three very different
 * voices — the audience shapes the system prompt.
 */
public enum ReportAudience {
    /** End users / support desk: plain language, no jargon, "is the service OK?". */
    USER,
    /** Engineers: metrics, anomalies, root-cause hypotheses, concrete next steps. */
    DEV,
    /** Executives: availability, risk, business impact — ten lines maximum. */
    BOSS;

    /** Lenient parse — unknown/blank falls back to DEV. */
    public static ReportAudience parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return DEV;
        }
        try {
            return valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return DEV;
        }
    }
}
