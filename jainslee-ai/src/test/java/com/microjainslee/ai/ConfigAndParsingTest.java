/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Config loading (properties, defaults, bad values, key redaction) and the
 * lenient response parser (clean JSON, fenced JSON, chatter, garbage).
 */
public class ConfigAndParsingTest {

    // ── AIAgentConfig ────────────────────────────────────────────────

    @Test
    public void defaultsAreSafeAndDisabled() {
        AIAgentConfig d = AIAgentConfig.defaults();
        assertFalse(d.enabled());
        assertEquals(AIMode.ADVISORY, d.mode());
        assertEquals("https://api.deepseek.com/v1", d.baseUrl());
        assertEquals("deepseek-chat", d.model());
        assertFalse(d.hasCredentials());
    }

    @Test
    public void fromPropertiesReadsAllKeys() {
        Map<String, String> props = Map.of(
                "microjainslee.ai.enabled", "true",
                "microjainslee.ai.api-key", "sk-test",
                "microjainslee.ai.base-url", "http://localhost:11434/v1",
                "microjainslee.ai.model", "qwen2.5",
                "microjainslee.ai.mode", "full_auto",
                "microjainslee.ai.interval-seconds", "30",
                "microjainslee.ai.confidence-threshold", "0.9");
        AIAgentConfig c = AIAgentConfig.fromProperties(props::get);
        assertTrue(c.enabled());
        assertEquals("sk-test", c.apiKey());
        assertEquals("http://localhost:11434/v1", c.baseUrl());
        assertEquals("qwen2.5", c.model());
        assertEquals(AIMode.FULL_AUTO, c.mode());
        assertEquals(30, c.intervalSeconds());
        assertEquals(0.9, c.confidenceThreshold(), 1e-9);
        assertTrue(c.hasCredentials());
    }

    @Test
    public void badValuesFallBackToDefaults() {
        Map<String, String> props = Map.of(
                "microjainslee.ai.mode", "SKYNET",
                "microjainslee.ai.interval-seconds", "not-a-number",
                "microjainslee.ai.confidence-threshold", "");
        AIAgentConfig c = AIAgentConfig.fromProperties(props::get);
        assertEquals("unknown mode must fall back to ADVISORY (safety)",
                AIMode.ADVISORY, c.mode());
        assertEquals(60, c.intervalSeconds());
        assertEquals(0.70, c.confidenceThreshold(), 1e-9);
    }

    @Test
    public void toStringNeverLeaksTheApiKey() {
        AIAgentConfig c = AIAgentConfig.defaults().withEnabled(true);
        AIAgentConfig withKey = AIAgentConfig.fromProperties(
                Map.of("microjainslee.ai.api-key", "sk-SECRET-123")::get);
        assertFalse(withKey.toString().contains("SECRET"));
        assertTrue(c.toString().contains("(unset)"));
    }

    @Test
    public void withersPreserveEverythingElse() {
        AIAgentConfig c = AIAgentConfig.defaults().withEnabled(true).withMode(AIMode.SEMI_AUTO);
        assertTrue(c.enabled());
        assertEquals(AIMode.SEMI_AUTO, c.mode());
        assertEquals(AIAgentConfig.defaults().baseUrl(), c.baseUrl());
    }

    // ── ResponseParser ───────────────────────────────────────────────

    private static final String CLEAN = """
            {"summary":"heap pressure rising","risks":[{"description":"heap 92%",\
            "level":"HIGH","metric":"heapPct","confidence":0.95}],\
            "recommendations":[{"action":"trigger_relief","target":"",\
            "reasoning":"trim caches before OOM","confidence":0.88}]}""";

    @Test
    public void parsesCleanJson() {
        AIAnalysis a = ResponseParser.parse(CLEAN);
        assertTrue(a.parsed());
        assertEquals("heap pressure rising", a.summary());
        assertEquals(1, a.risks().size());
        assertEquals(AIAnalysis.RiskLevel.HIGH, a.risks().get(0).level());
        assertEquals("action is upper-cased for the guard",
                "TRIGGER_RELIEF", a.recommendations().get(0).action());
        assertEquals(0.88, a.recommendations().get(0).confidence(), 1e-9);
    }

    @Test
    public void parsesFencedAndChattyReplies() {
        AIAnalysis fenced = ResponseParser.parse("```json\n" + CLEAN + "\n```");
        assertTrue(fenced.parsed());
        AIAnalysis chatty = ResponseParser.parse("Sure! Here is the analysis:\n"
                + CLEAN + "\nLet me know if you need more.");
        assertTrue(chatty.parsed());
        assertEquals(1, chatty.recommendations().size());
    }

    @Test
    public void garbageDegradesToUnparsedWithNoRecommendations() {
        AIAnalysis a = ResponseParser.parse("The system looks fine to me, no JSON here.");
        assertFalse(a.parsed());
        assertTrue("unparsed replies must never yield executable actions",
                a.recommendations().isEmpty());
        AIAnalysis b = ResponseParser.parse(null);
        assertFalse(b.parsed());
    }

    @Test
    public void confidenceIsClampedAndUnknownRiskLevelDefaultsLow() {
        AIAnalysis a = ResponseParser.parse("""
                {"summary":"x","risks":[{"description":"d","level":"APOCALYPTIC",\
                "confidence":7.5}],"recommendations":[{"action":"NONE","confidence":-3}]}""");
        assertTrue(a.parsed());
        assertEquals(AIAnalysis.RiskLevel.LOW, a.risks().get(0).level());
        assertEquals(1.0, a.risks().get(0).confidence(), 1e-9);
        assertEquals(0.0, a.recommendations().get(0).confidence(), 1e-9);
    }
}
