/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

/**
 * The control API a jainslee app programs against — the <b>only</b> surface an
 * application needs to steer the AI agent. The operating model is:
 * <i>the agent runs the node; the app holds this remote control.</i>
 *
 * <p>Everything else (analysis cadence, guardrails, action execution, circuit
 * breaking) is the agent's own responsibility and is deliberately not
 * exposed. Wire these methods to REST, JMX-free admin sockets, CLI, or call
 * them straight from application code:</p>
 *
 * <pre>{@code
 * AIAgentControl agent = appAiAgent.engine();
 *
 * agent.setEnabled(true);              // hand the node over
 * agent.setMode(AIMode.FULL_AUTO);     // full trust
 * agent.analyzeNow();                  // force a cycle before a maintenance window
 * String brief = agent.report(ReportAudience.BOSS);
 * if (!agent.status().available()) { ... }   // endpoint down? rule-based guardian still runs
 * }</pre>
 *
 * <p>All methods are safe from any thread and take effect immediately — no
 * restart, no re-wiring.</p>
 */
public interface AIAgentControl {

    /** Pause/resume the agent loop at runtime. */
    void setEnabled(boolean enabled);

    /** Move the agent along the trust ladder ({@code null} → ADVISORY). */
    void setMode(AIMode mode);

    /** Current trust mode. */
    AIMode mode();

    /** Counters, availability and mode — cheap, no LLM call. */
    AIAgentEngine.Status status();

    /** Latest analysis, or {@code null} before the first cycle. */
    AIAnalysis lastAnalysis();

    /** Force one full analyze-guard-act cycle right now. Blocking (one LLM call). */
    AIAnalysis analyzeNow();

    /** Live report for the given audience. Blocking (one LLM call). */
    String report(ReportAudience audience);
}
