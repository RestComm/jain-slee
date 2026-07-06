/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.microjainslee.ai.AIAnalysis.Recommendation;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * ActionGuard allow-list/threshold/mode gating, and the OpenAI-compatible
 * advisor over a fake transport: request shape, content extraction, report
 * audiences, and the failure circuit breaker.
 */
public class GuardAndAdvisorTest {

    private static Recommendation rec(String action, double confidence) {
        return new Recommendation(action, "t", "because", confidence);
    }

    // ── ActionGuard ──────────────────────────────────────────────────

    @Test
    public void advisoryModeExecutesNothing() {
        ActionGuard g = new ActionGuard(0.5);
        List<Recommendation> recs = List.of(rec("TRIGGER_RELIEF", 0.99), rec("RAISE_ALARM", 0.99));
        assertTrue(g.executable(recs, AIMode.ADVISORY).isEmpty());
        assertEquals(2, g.rejected(recs, AIMode.ADVISORY).size());
    }

    @Test
    public void unknownActionsAndLowConfidenceAreDropped() {
        ActionGuard g = new ActionGuard(0.7);
        List<Recommendation> recs = List.of(
                rec("DELETE_ALL_DATA", 0.99),      // not on the allow-list
                rec("TRIGGER_RELIEF", 0.50),       // below threshold
                rec("TRIGGER_RELIEF", 0.90));      // OK
        List<Recommendation> ok = g.executable(recs, AIMode.FULL_AUTO);
        assertEquals(1, ok.size());
        assertEquals(0.90, ok.get(0).confidence(), 1e-9);
    }

    @Test
    public void semiAutoRequiresHighConfidenceForMutatingActionsOnly() {
        ActionGuard g = new ActionGuard(0.7);
        List<Recommendation> recs = List.of(
                rec("TRIGGER_RELIEF", 0.75),       // mutating, < 0.85 → advisory in SEMI_AUTO
                rec("RAISE_ALARM", 0.75),          // passive → executes at threshold
                rec("DISABLE_AUTO_RECONFIG", 0.90)); // mutating, ≥ 0.85 → executes
        List<Recommendation> ok = g.executable(recs, AIMode.SEMI_AUTO);
        assertEquals(2, ok.size());
        assertFalse(ok.stream().anyMatch(r -> "TRIGGER_RELIEF".equals(r.action())));
    }

    // ── OpenAiCompatAdvisor ──────────────────────────────────────────

    private static AIAgentConfig cfg() {
        return AIAgentConfig.fromProperties(Map.of(
                "microjainslee.ai.enabled", "true",
                "microjainslee.ai.api-key", "sk-test",
                "microjainslee.ai.model", "test-model")::get);
    }

    @Test
    public void analyzeSendsSnapshotAndParsesStructuredReply() {
        AtomicReference<String> sentBody = new AtomicReference<>();
        OpenAiCompatAdvisor advisor = new OpenAiCompatAdvisor(cfg(), body -> {
            sentBody.set(body);
            return AiTestFixtures.completion(
                    "{\"summary\":\"cpu high\",\"risks\":[],\"recommendations\":"
                    + "[{\"action\":\"RAISE_ALARM\",\"confidence\":0.8}]}");
        });

        AIAnalysis a = advisor.analyze(AiTestFixtures.unhealthy());
        assertTrue(a.parsed());
        assertEquals("cpu high", a.summary());
        assertEquals(1, a.recommendations().size());

        String body = sentBody.get();
        assertTrue("request carries the model", body.contains("\"model\":\"test-model\""));
        assertTrue("request carries system+user messages", body.contains("\"role\":\"system\""));
        assertTrue("snapshot metrics reach the model", body.contains("heapPct"));
        assertTrue("sbb stats reach the model", body.contains("TestSbb"));
    }

    @Test
    public void reportUsesAudienceSpecificPrompt() {
        AtomicReference<String> sentBody = new AtomicReference<>();
        OpenAiCompatAdvisor advisor = new OpenAiCompatAdvisor(cfg(), body -> {
            sentBody.set(body);
            return AiTestFixtures.completion("All good, nothing to worry about.");
        });

        String userReport = advisor.report(ReportAudience.USER, AiTestFixtures.healthy());
        assertEquals("All good, nothing to worry about.", userReport);
        assertTrue(sentBody.get().contains("NON-TECHNICAL"));

        advisor.report(ReportAudience.BOSS, AiTestFixtures.healthy());
        assertTrue(sentBody.get().contains("executive summary"));

        advisor.report(ReportAudience.DEV, AiTestFixtures.healthy());
        assertTrue(sentBody.get().contains("engineering team"));
    }

    @Test
    public void circuitOpensAfterThreeConsecutiveFailures() {
        AtomicInteger calls = new AtomicInteger();
        OpenAiCompatAdvisor advisor = new OpenAiCompatAdvisor(cfg(), body -> {
            calls.incrementAndGet();
            throw new IOException("endpoint down");
        });

        assertTrue(advisor.isAvailable());
        for (int i = 0; i < 3; i++) {
            AIAnalysis a = advisor.analyze(AiTestFixtures.unhealthy());
            assertFalse("failed calls degrade, never throw", a.parsed());
        }
        assertFalse("circuit must open after 3 failures", advisor.isAvailable());
        // Open circuit short-circuits: no further transport calls.
        advisor.analyze(AiTestFixtures.unhealthy());
        assertEquals(3, calls.get());
    }

    @Test
    public void unconfiguredAdvisorIsUnavailableAndDegrades() {
        OpenAiCompatAdvisor advisor = new OpenAiCompatAdvisor(AIAgentConfig.defaults(),
                body -> { throw new AssertionError("must not be called without credentials"); });
        assertFalse(advisor.isAvailable());
        assertFalse(advisor.analyze(AiTestFixtures.unhealthy()).parsed());
        assertTrue(advisor.report(ReportAudience.DEV, AiTestFixtures.healthy())
                .contains("unavailable"));
    }

    @Test
    public void malformedEnvelopeCountsAsFailure() throws IOException {
        try {
            OpenAiCompatAdvisor.extractContent("{\"unexpected\":true}");
            throw new AssertionError("expected IOException");
        } catch (IOException expected) {
            // missing choices[0].message.content
        }
        assertEquals("hello",
                OpenAiCompatAdvisor.extractContent(AiTestFixtures.completion("hello")));
    }
}
