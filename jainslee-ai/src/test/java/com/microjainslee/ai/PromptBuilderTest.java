/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.RaCollector;
import com.microjainslee.telemetry.ResourceMonitor;
import com.microjainslee.telemetry.SbbCollector;
import com.microjainslee.telemetry.SpunkDetector;
import com.microjainslee.telemetry.StaleDetector;
import com.microjainslee.telemetry.TelemetryPort;
import com.microjainslee.telemetry.TelemetryPort.CustomMetric;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The prompt contract the whole safety model rests on: the snapshot JSON must
 * carry the metrics the model reasons over, and — critically — the exact
 * {@code leakedEntityIds} the model must pick RELEASE_ENTITY targets from
 * (only leaked entities, never idle ones). Also pins the audience-specific
 * report prompts and the analysis system prompt's action contract.
 */
public class PromptBuilderTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static TelemetryPort.TelemetrySnapshot richSnapshot() {
        var resources = new ResourceMonitor.ResourceSnapshot(
                256, 1024, 82.5, 0.44, 12, 2048, 7, 90, 130,
                System.currentTimeMillis());
        var sbbs = List.of(
                new SbbCollector.PerType("UssdSbb", 40, 3, 1, 950.0, 700),
                new SbbCollector.PerType("SmsSbb", 12, 0, 0, 120.0, 300));
        var ras = List.of(
                new RaCollector.RaStats("http-ra", "ACTIVE", 8081, 10_000, 5_000, 2));
        var spunks = List.of(new SpunkDetector.SpunkAlert(
                "UssdSbb", "u-9", "blocking>100ms", System.currentTimeMillis(), Map.of()));
        var stales = List.of(
                new StaleDetector.StaleAlert("UssdSbb/leaked-A", "UssdSbb",
                        System.currentTimeMillis() - 3_600_000, 3_600_000, true),
                new StaleDetector.StaleAlert("UssdSbb/leaked-B", "UssdSbb",
                        System.currentTimeMillis() - 3_600_000, 3_600_000, true),
                new StaleDetector.StaleAlert("UssdSbb/idle-C", "UssdSbb",
                        System.currentTimeMillis() - 600_000, 600_000, false));
        var alarms = List.of(new AlarmEngine.Alarm("ALM-1",
                AlarmEngine.TelemetryAlarmLevel.CRITICAL, "guardian",
                "heap 95%", System.currentTimeMillis(), Map.of(), false));
        var custom = List.of(
                new CustomMetric("ss7_tcap_total", Map.of("opcode", "begin"), 142, null, false),
                new CustomMetric("ss7_stale_dialogues", Map.of(), 0, 3, true));
        return new TelemetryPort.TelemetrySnapshot(
                sbbs, ras, resources, List.of(), spunks, stales, alarms, false, custom);
    }

    @Test
    public void snapshotJsonCarriesTheMetricsTheModelNeeds() throws Exception {
        JsonNode o = MAPPER.readTree(PromptBuilder.snapshotJson(richSnapshot()));

        assertEquals(82.5, o.get("heapPct").asDouble(), 1e-9);
        assertEquals(0.44, o.get("cpuLoad").asDouble(), 1e-9);
        assertEquals(2048, o.get("virtualThreads").asInt());

        assertEquals(2, o.get("sbbs").size());
        assertEquals("UssdSbb", o.get("sbbs").get(0).get("type").asText());
        assertEquals(3, o.get("sbbs").get(0).get("errors").asInt());

        assertEquals(1, o.get("ras").size());
        assertEquals("http-ra", o.get("ras").get(0).get("name").asText());

        assertEquals(1, o.get("spunkAlerts").asInt());
        assertEquals(1, o.get("activeAlarms").asInt());
        assertEquals("guardian", o.get("alarmDetails").get(0).get("source").asText());
        assertFalse(o.get("autoReconfigEnabled").asBoolean());
    }

    @Test
    public void leakedEntityIdsListsOnlyLeakedEntities() throws Exception {
        JsonNode o = MAPPER.readTree(PromptBuilder.snapshotJson(richSnapshot()));

        assertEquals("only leaked entities counted", 2, o.get("leakedEntities").asInt());
        JsonNode ids = o.get("leakedEntityIds");
        assertEquals(2, ids.size());
        List<String> idList = List.of(ids.get(0).asText(), ids.get(1).asText());
        assertTrue(idList.contains("UssdSbb/leaked-A"));
        assertTrue(idList.contains("UssdSbb/leaked-B"));
        assertFalse("idle entity must never be a RELEASE_ENTITY target",
                idList.contains("UssdSbb/idle-C"));
    }

    @Test
    public void customMetricsAppearWithGaugeAndCounterValues() throws Exception {
        JsonNode custom = MAPPER.readTree(PromptBuilder.snapshotJson(richSnapshot()))
                .get("customMetrics");
        assertEquals(2, custom.size());
        // counter carries its long value, gauge its numeric value
        assertEquals(142.0, custom.get(0).get("value").asDouble(), 1e-9);
        assertEquals(3.0, custom.get(1).get("value").asDouble(), 1e-9);
    }

    @Test
    public void snapshotJsonToleratesNullResources() throws Exception {
        var snap = new TelemetryPort.TelemetrySnapshot(
                List.of(), List.of(), null, List.of(), List.of(), List.of(),
                List.of(), true, List.of());
        JsonNode o = MAPPER.readTree(PromptBuilder.snapshotJson(snap));
        assertFalse("no heap block when resources are null", o.has("heapPct"));
        assertEquals(0, o.get("leakedEntities").asInt());
    }

    @Test
    public void analysisSystemPromptDeclaresTheActionContract() {
        String p = PromptBuilder.analysisSystemPrompt();
        assertTrue(p.contains("RELEASE_ENTITY"));
        assertTrue(p.contains("leakedEntityIds"));
        assertTrue(p.contains("TRIGGER_RELIEF"));
        assertTrue("must demand pure JSON", p.contains("ONLY valid JSON"));
    }

    @Test
    public void reportPromptsAreAudienceSpecific() {
        String user = PromptBuilder.reportSystemPrompt(ReportAudience.USER);
        String dev = PromptBuilder.reportSystemPrompt(ReportAudience.DEV);
        String boss = PromptBuilder.reportSystemPrompt(ReportAudience.BOSS);

        assertTrue(user.contains("NON-TECHNICAL"));
        assertTrue(dev.contains("engineering team"));
        assertTrue(boss.contains("executive summary"));
        // the three voices must be genuinely different
        assertFalse(user.equals(dev));
        assertFalse(dev.equals(boss));
    }
}
