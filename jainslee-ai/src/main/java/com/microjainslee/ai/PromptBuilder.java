/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

/**
 * Turns a {@link TelemetrySnapshot} into compact prompts. Single-snapshot
 * strategy (~500–2000 tokens) — cheap, good enough for real-time anomaly
 * detection; aggregated history can come later.
 *
 * <p>All methods are static; the class is a pure function library with no
 * mutable state. The shared {@link ObjectMapper} is thread-safe.</p>
 */
public final class PromptBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PromptBuilder() {
    }

    /**
     * System prompt for the analysis loop — strict JSON contract with the
     * allowed action vocabulary and safety rules.
     *
     * @return system prompt string for chat-completions
     */
    public static String analysisSystemPrompt() {
        return """
                You are an AI operations engineer for micro-jainslee, an embeddable \
                JAIN SLEE 1.1 telecom event server on Java 25 (virtual threads, LMAX \
                Disruptor). You receive one telemetry snapshot as JSON.

                Identify anomalies, risks, and concrete recommendations.

                Allowed actions (anything else is ignored):
                - TRIGGER_RELIEF          poke the memory guardian to trim caches now
                - RELEASE_ENTITY          force-release one leaked SBB entity; \
                "target" MUST be an id from leakedEntityIds
                - ENABLE_AUTO_RECONFIG    turn the rule-based auto-reconfig engine on
                - DISABLE_AUTO_RECONFIG   turn it off (e.g. it is oscillating)
                - RAISE_ALARM             surface a warning to operators
                - INVESTIGATE             flag for human attention, no automation
                - NONE                    system is healthy

                Rules:
                - Be conservative. Only recommend an action when confidence >= 0.7.
                - If everything is normal, say so — do NOT invent problems.
                - Return ONLY valid JSON, no markdown fences, exactly this shape:
                {"summary":"...","risks":[{"description":"...","level":"LOW|MEDIUM|HIGH|CRITICAL",\
                "metric":"...","confidence":0.0}],"recommendations":[{"action":"...",\
                "target":"...","reasoning":"...","confidence":0.0}]}""";
    }

    /**
     * System prompt for report generation, shaped by the audience.
     * Each audience gets a distinct voice: plain-language reassurance
     * for {@code USER}, technical detail for {@code DEV}, an executive
     * verdict for {@code BOSS}.
     *
     * @param audience the target reader (never null)
     * @return system prompt string for chat-completions
     */
    public static String reportSystemPrompt(ReportAudience audience) {
        String base = "You are the operations reporter for micro-jainslee, a telecom "
                + "event server. You receive one telemetry snapshot as JSON. ";
        return switch (audience) {
            case USER -> base + """
                    Write a short service-status update for NON-TECHNICAL end users. \
                    Plain language, no jargon, no metric names. Answer: is the service \
                    working normally? Any impact they might notice? Max 6 sentences. \
                    Plain text only.""";
            case DEV -> base + """
                    Write a technical report for the engineering team in Markdown: \
                    current metrics table, anomalies observed, root-cause hypotheses, \
                    and a prioritized list of concrete next actions (config keys, \
                    endpoints, thresholds). Be specific and terse.""";
            case BOSS -> base + """
                    Write an executive summary, 10 lines maximum: overall availability \
                    verdict (one line, first), current risk level (LOW/MEDIUM/HIGH), \
                    business impact if any, and whether engineering action is needed. \
                    No metric dumps, no jargon. Plain text.""";
        };
    }

    /**
     * Builds the user-message JSON: one compact object describing the node
     * right now — resources, SBB stats, RA states, alarms, spunks, leaks,
     * and custom metrics (capped at 30 entries).
     *
     * @param snap the current telemetry snapshot (never null)
     * @return serialised JSON string (~500–2000 tokens)
     */
    public static String snapshotJson(TelemetrySnapshot snap) {
        ObjectNode o = MAPPER.createObjectNode();
        var r = snap.resources();
        if (r != null) {
            o.put("heapUsedMb", r.heapUsedMb()).put("heapMaxMb", r.heapMaxMb())
                    .put("heapPct", r.heapUsagePercent()).put("cpuLoad", r.cpuLoad())
                    .put("platformThreads", r.activeThreads())
                    .put("virtualThreads", r.virtualThreads())
                    .put("gcCount", r.gcCount()).put("gcTimeMs", r.gcTimeMs());
        }
        ArrayNode sbbs = o.putArray("sbbs");
        for (var s : snap.sbbs()) {
            sbbs.addObject().put("type", s.sbbType()).put("active", s.active())
                    .put("errors", s.errors()).put("spunks", s.spunks())
                    .put("eps", s.eps()).put("p99us", s.p99us());
        }
        ArrayNode ras = o.putArray("ras");
        for (var ra : snap.ras()) {
            ras.addObject().put("name", ra.raName()).put("state", ra.state())
                    .put("eventsFired", ra.eventsFired());
        }
        o.put("spunkAlerts", snap.spunks().size());
        o.put("leakedEntities", snap.stales().stream().filter(s -> s.leaked()).count());
        ArrayNode leakedIds = o.putArray("leakedEntityIds");   // targets for RELEASE_ENTITY
        snap.stales().stream().filter(s -> s.leaked()).limit(20)
                .forEach(s -> leakedIds.add(s.entityId()));
        o.put("activeAlarms", snap.activeAlarms().size());
        ArrayNode alarms = o.putArray("alarmDetails");
        snap.activeAlarms().stream().limit(10).forEach(a ->
                alarms.addObject().put("level", a.level().name())
                        .put("source", a.source()).put("message", a.message()));
        o.put("autoReconfigEnabled", snap.autoReconfigEnabled());
        ArrayNode custom = o.putArray("customMetrics");
        snap.customMetrics().stream().limit(30).forEach(m ->
                custom.addObject().put("name", m.name())
                        .put("value", m.isGauge()
                                ? m.gaugeValue() == null ? 0.0 : m.gaugeValue().doubleValue()
                                : m.counterValue()));
        return o.toString();
    }
}
