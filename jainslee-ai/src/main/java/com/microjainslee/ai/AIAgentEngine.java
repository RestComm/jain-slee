/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.microjainslee.ai.AIAnalysis.Recommendation;
import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.TelemetryPort;
import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The AI agent control loop. One daemon virtual thread wakes every
 * {@code interval-seconds}, and each cycle:
 *
 * <ol>
 *   <li><b>Pre-filter</b> — if the node is obviously healthy (low heap/CPU,
 *       no alarms/spunks/leaks) the AI is <i>not</i> called. Zero token cost
 *       on a quiet system.</li>
 *   <li><b>Analyze</b> — {@link AIAdvisor#analyze} over the current snapshot.</li>
 *   <li><b>Guard</b> — {@link ActionGuard} allow-lists + confidence-gates
 *       the recommendations per the current {@link AIMode}.</li>
 *   <li><b>Act</b> — surviving actions execute against the deliberately small
 *       control surface: poke the {@link AutonomousGuardian}, toggle the
 *       auto-reconfig engine, or raise an alarm. Cooldown-protected.</li>
 * </ol>
 *
 * <p><b>Optionality:</b> {@code telemetry} is required (it is the data
 * source); {@code guardian} may be {@code null} — an app that skipped the
 * autonomous module still gets analysis, alarms and reports, and
 * {@code TRIGGER_RELIEF} recommendations are downgraded to log lines.</p>
 *
 * <p>Runtime control: apps steer the agent exclusively through the
 * {@link AIAgentControl} interface this engine implements — REST/GUI are just
 * transports over the same methods.</p>
 */
public final class AIAgentEngine implements AIAgentControl, AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(AIAgentEngine.class);

    /**
     * Immutable status snapshot suitable for REST endpoints and dashboards.
     * All fields are primitive or immutable, so this record is safe to
     * cache or serialise directly.
     *
     * @param enabled whether the agent is currently enabled
     * @param running whether the agent loop is running
     * @param available whether the advisor backend is reachable
     * @param mode the current trust mode name (ADVISORY / SEMI_AUTO / FULL_AUTO)
     * @param model the configured model name
     * @param cycles total loop cycles executed
     * @param skippedHealthy cycles skipped by the pre-filter
     * @param analyses total AI analyses performed
     * @param actionsExecuted total actions executed through the guard
     * @param lastAnalysisAt epoch millis of the last completed analysis (0 if none)
     */
    public record Status(boolean enabled, boolean running, boolean available,
                         String mode, String model, long cycles, long skippedHealthy,
                         long analyses, long actionsExecuted, long lastAnalysisAt) {}

    private final AIAdvisor advisor;
    private final TelemetryPort telemetry;
    private final AutonomousGuardian guardian;   // nullable — autonomous module is optional
    private final ActionGuard guard;
    private final long intervalMillis;
    private final long actionCooldownMillis;

    private volatile boolean enabled;
    private volatile AIMode mode;
    private volatile boolean running;
    private Thread worker;

    private final AtomicLong cycles = new AtomicLong();
    private final AtomicLong skippedHealthy = new AtomicLong();
    private final AtomicLong analyses = new AtomicLong();
    private final AtomicLong actionsExecuted = new AtomicLong();
    private final AtomicLong lastActionAt = new AtomicLong();
    private final AtomicReference<AIAnalysis> lastAnalysis = new AtomicReference<>();
    private final String model;

    /** Optional hook for RELEASE_ENTITY — wired to {@code container::releaseEntity}. */
    private volatile java.util.function.Consumer<String> entityReleaser;

    /**
     * Creates the engine. Nothing starts until {@link #start()} is called.
     *
     * @param config the initial configuration (enabled, mode, thresholds)
     * @param advisor the AI backend (e.g. {@link OpenAiCompatAdvisor})
     * @param telemetry the data source for snapshot collection
     * @param guardian the autonomous relief engine (nullable — if null,
     *        {@code TRIGGER_RELIEF} actions are downgraded to log lines)
     */
    public AIAgentEngine(AIAgentConfig config, AIAdvisor advisor,
                         TelemetryPort telemetry, AutonomousGuardian guardian) {
        this.advisor = advisor;
        this.telemetry = telemetry;
        this.guardian = guardian;
        this.guard = new ActionGuard(config.confidenceThreshold());
        this.intervalMillis = Math.max(1, config.intervalSeconds()) * 1000L;
        this.actionCooldownMillis = config.actionCooldownSeconds() * 1000L;
        this.enabled = config.enabled();
        this.mode = config.mode();
        this.model = config.model();
    }

    /**
     * Wires the RELEASE_ENTITY action to the container (or any releaser).
     * Without it, RELEASE_ENTITY recommendations downgrade to log lines.
     *
     * @param releaser e.g. {@code container::releaseEntity}
     * @return this engine, for fluent wiring
     */
    public AIAgentEngine entityReleaser(java.util.function.Consumer<String> releaser) {
        this.entityReleaser = releaser;
        return this;
    }

    // ── lifecycle ────────────────────────────────────────────────────

    /**
     * Starts the agent's daemon virtual thread. Idempotent — calling
     * multiple times is safe (second call is a no-op). The thread
     * terminates when {@link #close()} is called.
     */
    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        worker = Thread.ofVirtual().name("ai-agent").start(this::loop);
        LOG.info("[ai] agent started (enabled={}, mode={}, model={})", enabled, mode, model);
    }

    @Override
    public synchronized void close() {
        running = false;
        if (worker != null) {
            worker.interrupt();
            worker = null;
        }
    }

    // ── runtime control (REST/GUI) ───────────────────────────────────

    /**
     * Enables or disables the agent. When disabled, the loop still runs
     * (the virtual thread stays alive) but cycles are skipped — no
     * tokens are consumed and no actions are executed.
     *
     * @param enabled the new enabled state
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        LOG.info("[ai] agent {}", enabled ? "ENABLED" : "DISABLED");
    }

    /**
     * Sets the trust mode. {@code null} is treated as {@link AIMode#ADVISORY}
     * (safety fallback).
     *
     * @param mode the new mode (null-safe)
     */
    public void setMode(AIMode mode) {
        this.mode = mode == null ? AIMode.ADVISORY : mode;
        LOG.info("[ai] mode → {}", this.mode);
    }

    /**
     * Returns the current trust mode.
     *
     * @return the current mode (never null)
     */
    public AIMode mode() {
        return mode;
    }

    /**
     * Returns the most recent analysis result, or null if none yet.
     *
     * @return the last completed analysis, or null
     */
    public AIAnalysis lastAnalysis() {
        return lastAnalysis.get();
    }

    /**
     * Returns an immutable status snapshot for dashboards and REST endpoints.
     *
     * @return current agent status
     */
    public Status status() {
        AIAnalysis last = lastAnalysis.get();
        return new Status(enabled, running, advisor.isAvailable(), mode.name(), model,
                cycles.get(), skippedHealthy.get(), analyses.get(), actionsExecuted.get(),
                last == null ? 0 : last.timestamp());
    }

    /**
     * Forces one full analysis cycle immediately (the GUI "Analyze now"
     * button). Skips the pre-filter but still respects the guard and mode.
     * Blocking — runs on the calling thread.
     *
     * @return the analysis result
     */
    public AIAnalysis analyzeNow() {
        return runCycle(true);
    }

    /**
     * Generates an audience-specific report from a fresh live snapshot.
     * Blocking — runs on the calling thread.
     *
     * @param audience the target reader
     * @return the generated report text
     */
    public String report(ReportAudience audience) {
        return advisor.report(audience, telemetry.snapshot());
    }

    // ── the loop ─────────────────────────────────────────────────────

    private void loop() {
        while (running) {
            try {
                if (enabled) {
                    runCycle(false);
                }
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (RuntimeException e) {
                LOG.warn("[ai] cycle failed: {}", e.getMessage());
            }
        }
    }

    private AIAnalysis runCycle(boolean forced) {
        cycles.incrementAndGet();
        TelemetrySnapshot snapshot = telemetry.snapshot();

        if (!forced && isObviouslyHealthy(snapshot)) {
            skippedHealthy.incrementAndGet();
            return lastAnalysis.get();   // don't burn tokens on a quiet node
        }
        if (!advisor.isAvailable()) {
            LOG.debug("[ai] advisor unavailable, skipping cycle");
            return lastAnalysis.get();
        }

        AIAnalysis analysis = advisor.analyze(snapshot);
        analyses.incrementAndGet();
        lastAnalysis.set(analysis);
        LOG.info("[ai] analysis: {} ({} risks, {} recommendations, mode={})",
                truncate(analysis.summary()), analysis.risks().size(),
                analysis.recommendations().size(), mode);

        for (Recommendation rec : guard.executable(analysis.recommendations(), mode)) {
            execute(rec);
        }
        for (Recommendation rec : guard.rejected(analysis.recommendations(), mode)) {
            LOG.info("[ai] advisory only ({}): {} — {}", mode, rec.action(),
                    truncate(rec.reasoning()));
        }
        return analysis;
    }

    /**
     * Pre-AI filter — mirrors the research spec: skip when the node is
     * clearly healthy. Saves tokens and endpoint latency on quiet systems.
     *
     * <p>Conditions: heap &lt; 50%, CPU &lt; 0.30, zero SBB errors, no active
     * alarms, no spunks, no leaked entities.</p>
     *
     * @param snap the current telemetry snapshot
     * @return true if the node appears healthy enough to skip AI analysis
     */
    static boolean isObviouslyHealthy(TelemetrySnapshot snap) {
        var r = snap.resources();
        double heap = r == null ? 0 : r.heapUsagePercent();
        double cpu = r == null ? 0 : r.cpuLoad();
        long errors = snap.sbbs().stream().mapToLong(s -> s.errors()).sum();
        boolean leaks = snap.stales().stream().anyMatch(s -> s.leaked());
        return heap < 50.0 && cpu < 0.30 && errors == 0
                && snap.activeAlarms().isEmpty() && snap.spunks().isEmpty() && !leaks;
    }

    // ── the control surface ──────────────────────────────────────────

    private void execute(Recommendation rec) {
        // Mutating actions share one cooldown; passive ones (alarms) do not.
        boolean passive = ActionGuard.PASSIVE_ACTIONS.contains(rec.action());
        if (!passive && !cooldownPermits()) {
            LOG.info("[ai] action {} suppressed by cooldown", rec.action());
            return;
        }
        switch (rec.action()) {
            case "TRIGGER_RELIEF" -> {
                if (guardian != null) {
                    var level = guardian.checkNow();
                    LOG.warn("[ai] TRIGGER_RELIEF executed → guardian level {}", level);
                } else {
                    LOG.warn("[ai] TRIGGER_RELIEF recommended but no guardian wired "
                            + "(autonomous module not installed): {}", truncate(rec.reasoning()));
                    return;
                }
            }
            case "ENABLE_AUTO_RECONFIG" -> {
                telemetry.setAutoReconfigEnabled(true);
                LOG.warn("[ai] auto-reconfig ENABLED: {}", truncate(rec.reasoning()));
            }
            case "DISABLE_AUTO_RECONFIG" -> {
                telemetry.setAutoReconfigEnabled(false);
                LOG.warn("[ai] auto-reconfig DISABLED: {}", truncate(rec.reasoning()));
            }
            case "RELEASE_ENTITY" -> {
                var releaser = this.entityReleaser;
                String target = rec.target();
                if (releaser == null) {
                    LOG.warn("[ai] RELEASE_ENTITY recommended but no releaser wired: {}",
                            truncate(rec.reasoning()));
                    return;
                }
                if (target == null || target.isBlank()) {
                    LOG.warn("[ai] RELEASE_ENTITY rejected — no target entity id");
                    return;
                }
                releaser.accept(target);
                LOG.warn("[ai] RELEASE_ENTITY executed for '{}': {}", target,
                        truncate(rec.reasoning()));
            }
            case "RAISE_ALARM" -> telemetry.alarmEngine().fire(
                    AlarmEngine.TelemetryAlarmLevel.WARNING, "ai-agent",
                    rec.reasoning(), Map.of("confidence", rec.confidence()));
            case "INVESTIGATE" -> LOG.warn("[ai] INVESTIGATE: {} (target={}, confidence={})",
                    truncate(rec.reasoning()), rec.target(), rec.confidence());
            default -> {   // NONE and anything unexpected: log only
                LOG.debug("[ai] no-op action {}", rec.action());
                return;
            }
        }
        actionsExecuted.incrementAndGet();
    }

    private boolean cooldownPermits() {
        long now = System.currentTimeMillis();
        long prev = lastActionAt.get();
        return now - prev >= actionCooldownMillis && lastActionAt.compareAndSet(prev, now);
    }

    private static String truncate(String s) {
        return s == null ? "" : s.length() > 160 ? s.substring(0, 160) + "…" : s;
    }
}
