/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

/**
 * Pluggable AI backend. The default implementation
 * ({@link OpenAiCompatAdvisor}) speaks the OpenAI-compatible
 * chat-completions dialect, which covers DeepSeek, OpenAI, Ollama, vLLM,
 * LM Studio and most self-hosted gateways just by changing the base URL.
 *
 * <p>Implementations must be thread-safe: {@link #analyze} and
 * {@link #report} may be called from different threads concurrently.</p>
 */
public interface AIAdvisor {

    /**
     * Analyze one snapshot → structured risks + recommendations. Blocking.
     *
     * @param snapshot the current telemetry snapshot to analyze
     * @return a structured analysis (never null); on failure an
     *         {@link AIAnalysis#unparsed(String)} instance is returned
     *         so callers never see null
     */
    AIAnalysis analyze(TelemetrySnapshot snapshot);

    /**
     * Generate a human-readable report for the given audience. Blocking.
     *
     * @param audience shapes the tone and detail level
     * @param snapshot the current telemetry snapshot
     * @return plain text or Markdown report; a fallback string on failure
     */
    String report(ReportAudience audience, TelemetrySnapshot snapshot);

    /**
     * Returns {@code false} when unconfigured or the circuit breaker is open.
     *
     * @return whether the advisor is ready to accept calls
     */
    boolean isAvailable();
}
