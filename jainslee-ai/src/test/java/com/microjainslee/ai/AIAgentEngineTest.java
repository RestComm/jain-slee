/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.microjainslee.ai.AIAnalysis.Recommendation;
import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.autonomous.MemoryReliefParticipant;
import com.microjainslee.autonomous.PressureLevel;
import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The control loop end to end with a scripted advisor: pre-filter economics,
 * mode gating (ADVISORY vs FULL_AUTO), the real control surface (guardian
 * poke, auto-reconfig toggle, alarm fire), null-guardian optionality, action
 * cooldown, and runtime enable/disable.
 */
public class AIAgentEngineTest {

    /** Advisor scripted to always recommend the given actions. */
    private static final class ScriptedAdvisor implements AIAdvisor {
        final AtomicInteger analyzeCalls = new AtomicInteger();
        final List<Recommendation> recs;

        ScriptedAdvisor(Recommendation... recs) {
            this.recs = List.of(recs);
        }

        @Override public AIAnalysis analyze(TelemetrySnapshot snapshot) {
            analyzeCalls.incrementAndGet();
            return new AIAnalysis("scripted", List.of(), recs, true, System.currentTimeMillis());
        }
        @Override public String report(ReportAudience audience, TelemetrySnapshot snapshot) {
            return "report for " + audience;
        }
        @Override public boolean isAvailable() { return true; }
    }

    private static AIAgentConfig config(AIMode mode, long cooldownSeconds) {
        return AIAgentConfig.fromProperties(Map.of(
                "microjainslee.ai.enabled", "true",
                "microjainslee.ai.api-key", "sk-test",
                "microjainslee.ai.mode", mode.name(),
                "microjainslee.ai.confidence-threshold", "0.7",
                "microjainslee.ai.action-cooldown-seconds", Long.toString(cooldownSeconds))::get);
    }

    private static AutonomousGuardian probeGuardian(AtomicInteger reliefCalls) {
        AutonomousGuardian g = new AutonomousGuardian()
                .watermarks(0.0001, 0.0002, 0.0003)   // any real heap trips relief
                .actionCooldownMillis(0)
                .gcCooldownMillis(Long.MAX_VALUE);
        g.register(new MemoryReliefParticipant() {
            @Override public String name() { return "probe"; }
            @Override public long relieve(PressureLevel level) {
                reliefCalls.incrementAndGet();
                return 1;
            }
        });
        return g;
    }

    // ── pre-filter ───────────────────────────────────────────────────

    @Test
    public void healthySnapshotIsObviouslyHealthy() {
        assertTrue(AIAgentEngine.isObviouslyHealthy(AiTestFixtures.healthy()));
        assertFalse(AIAgentEngine.isObviouslyHealthy(AiTestFixtures.unhealthy()));
        assertFalse("errors alone break 'obviously healthy'",
                AIAgentEngine.isObviouslyHealthy(AiTestFixtures.snapshot(30, 0.1, 5)));
    }

    @Test
    public void analyzeNowForcesPastThePreFilter() {
        ScriptedAdvisor advisor = new ScriptedAdvisor();
        var port = new AiTestFixtures.FakePort(AiTestFixtures.healthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.ADVISORY, 0),
                advisor, port, null)) {
            AIAnalysis a = engine.analyzeNow();
            assertNotNull(a);
            assertEquals("forced cycle must call the advisor even when healthy",
                    1, advisor.analyzeCalls.get());
        }
    }

    // ── mode gating on the real control surface ──────────────────────

    @Test
    public void advisoryModeNeverTouchesTheGuardian() {
        AtomicInteger relief = new AtomicInteger();
        ScriptedAdvisor advisor = new ScriptedAdvisor(
                new Recommendation("TRIGGER_RELIEF", "", "heap high", 0.99));
        var port = new AiTestFixtures.FakePort(AiTestFixtures.unhealthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.ADVISORY, 0),
                advisor, port, probeGuardian(relief))) {
            engine.analyzeNow();
            assertEquals("ADVISORY must not execute", 0, relief.get());
            assertEquals(0, engine.status().actionsExecuted());
        }
    }

    @Test
    public void fullAutoExecutesThroughTheGuardian() {
        AtomicInteger relief = new AtomicInteger();
        ScriptedAdvisor advisor = new ScriptedAdvisor(
                new Recommendation("TRIGGER_RELIEF", "", "heap high", 0.99));
        var port = new AiTestFixtures.FakePort(AiTestFixtures.unhealthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.FULL_AUTO, 0),
                advisor, port, probeGuardian(relief))) {
            engine.analyzeNow();
            assertTrue("FULL_AUTO must poke guardian.checkNow()", relief.get() >= 1);
            assertEquals(1, engine.status().actionsExecuted());
        }
    }

    @Test
    public void autoReconfigToggleAndAlarmActionsHitTelemetry() {
        ScriptedAdvisor advisor = new ScriptedAdvisor(
                new Recommendation("DISABLE_AUTO_RECONFIG", "", "oscillating", 0.95),
                new Recommendation("RAISE_ALARM", "", "watch the heap", 0.90));
        var port = new AiTestFixtures.FakePort(AiTestFixtures.unhealthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.FULL_AUTO, 0),
                advisor, port, null)) {
            engine.analyzeNow();
            assertFalse("auto-reconfig must be disabled", port.isAutoReconfigEnabled());
            assertEquals("alarm must be fired by ai-agent", 1, port.alarms.active().size());
            assertEquals("ai-agent", port.alarms.active().get(0).source());
        }
    }

    @Test
    public void nullGuardianIsSafe_triggerReliefDowngradesToLog() {
        ScriptedAdvisor advisor = new ScriptedAdvisor(
                new Recommendation("TRIGGER_RELIEF", "", "heap high", 0.99));
        var port = new AiTestFixtures.FakePort(AiTestFixtures.unhealthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.FULL_AUTO, 0),
                advisor, port, null)) {
            engine.analyzeNow();   // must not throw
            assertEquals("no guardian → nothing executed", 0, engine.status().actionsExecuted());
        }
    }

    // ── cooldown ─────────────────────────────────────────────────────

    @Test
    public void mutatingActionsShareOneCooldownPassiveOnesDoNot() {
        AtomicInteger relief = new AtomicInteger();
        ScriptedAdvisor advisor = new ScriptedAdvisor(
                new Recommendation("TRIGGER_RELIEF", "", "r1", 0.99),
                new Recommendation("RAISE_ALARM", "", "a1", 0.99));
        var port = new AiTestFixtures.FakePort(AiTestFixtures.unhealthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.FULL_AUTO, 3600),
                advisor, port, probeGuardian(relief))) {
            engine.analyzeNow();
            engine.analyzeNow();
            assertEquals("second TRIGGER_RELIEF suppressed by cooldown", 1, relief.get());
            assertEquals("alarms are passive — both cycles fire", 2, port.alarms.active().size());
        }
    }

    // ── runtime control + loop ───────────────────────────────────────

    @Test
    public void disabledEngineLoopNeverCallsTheAdvisor() throws Exception {
        ScriptedAdvisor advisor = new ScriptedAdvisor();
        var port = new AiTestFixtures.FakePort(AiTestFixtures.unhealthy());
        AIAgentConfig cfg = config(AIMode.ADVISORY, 0).withEnabled(false);
        try (AIAgentEngine engine = new AIAgentEngine(cfg, advisor, port, null)) {
            engine.start();
            engine.start();   // idempotent
            Thread.sleep(50);
            assertEquals(0, advisor.analyzeCalls.get());
            assertFalse(engine.status().enabled());

            engine.setEnabled(true);
            assertTrue(engine.status().enabled());
        }
    }

    @Test
    public void statusAndModeSwitchAreLive() {
        ScriptedAdvisor advisor = new ScriptedAdvisor();
        var port = new AiTestFixtures.FakePort(AiTestFixtures.healthy());
        try (AIAgentEngine engine = new AIAgentEngine(config(AIMode.ADVISORY, 0),
                advisor, port, null)) {
            assertEquals("ADVISORY", engine.status().mode());
            engine.setMode(AIMode.FULL_AUTO);
            assertEquals("FULL_AUTO", engine.status().mode());
            engine.setMode(null);   // defensive: null → ADVISORY
            assertEquals("ADVISORY", engine.status().mode());
            assertEquals("report for BOSS", engine.report(ReportAudience.BOSS));
        }
    }
}
