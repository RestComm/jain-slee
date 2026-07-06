/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ai;

import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.AutoReconfigEngine;
import com.microjainslee.telemetry.ErrorCollector;
import com.microjainslee.telemetry.RaCollector;
import com.microjainslee.telemetry.ResourceMonitor;
import com.microjainslee.telemetry.SbbCollector;
import com.microjainslee.telemetry.SpunkDetector;
import com.microjainslee.telemetry.StaleDetector;
import com.microjainslee.telemetry.TelemetryPort;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/** Shared test doubles: snapshot builders + a controllable TelemetryPort. */
final class AiTestFixtures {

    private AiTestFixtures() {
    }

    static TelemetryPort.TelemetrySnapshot snapshot(double heapPct, double cpu, long errors) {
        var resources = new ResourceMonitor.ResourceSnapshot(
                (long) heapPct * 10, 1000, heapPct, cpu, 8, 100, 3, 40, 64,
                System.currentTimeMillis());
        var sbbs = List.of(new SbbCollector.PerType("TestSbb", 10, errors, 0, 100.0, 250));
        return new TelemetryPort.TelemetrySnapshot(sbbs, List.of(), resources,
                List.of(), List.of(), List.of(), List.of(), true, List.of());
    }

    static TelemetryPort.TelemetrySnapshot healthy() {
        return snapshot(30, 0.10, 0);
    }

    static TelemetryPort.TelemetrySnapshot unhealthy() {
        return snapshot(95, 0.90, 42);
    }

    static final class FakePort implements TelemetryPort {
        volatile TelemetrySnapshot current;
        final AlarmEngine alarms = new AlarmEngine();
        final AtomicBoolean autoReconfig = new AtomicBoolean(true);

        FakePort(TelemetrySnapshot initial) {
            this.current = initial;
        }

        @Override public TelemetrySnapshot snapshot() { return current; }
        @Override public AlarmEngine alarmEngine() { return alarms; }
        @Override public boolean isAutoReconfigEnabled() { return autoReconfig.get(); }
        @Override public void setAutoReconfigEnabled(boolean enabled) { autoReconfig.set(enabled); }
        @Override public String scrape() { return ""; }

        @Override public SbbCollector sbbCollector() { throw new UnsupportedOperationException(); }
        @Override public RaCollector raCollector() { throw new UnsupportedOperationException(); }
        @Override public ErrorCollector errorCollector() { throw new UnsupportedOperationException(); }
        @Override public ResourceMonitor resourceMonitor() { throw new UnsupportedOperationException(); }
        @Override public SpunkDetector spunkDetector() { throw new UnsupportedOperationException(); }
        @Override public StaleDetector staleDetector() { throw new UnsupportedOperationException(); }
        @Override public AutoReconfigEngine autoReconfig() { throw new UnsupportedOperationException(); }
        @Override public Counter customCounter(String name, String... tagPairs) { throw new UnsupportedOperationException(); }
        @Override public Gauge customGauge(String name, Supplier<Number> supplier, String... tagPairs) { throw new UnsupportedOperationException(); }
    }

    /** Chat-completions envelope wrapping the given assistant content. */
    static String completion(String content) {
        return "{\"choices\":[{\"message\":{\"role\":\"assistant\",\"content\":"
                + jsonString(content) + "}}]}";
    }

    private static String jsonString(String s) {
        return '"' + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + '"';
    }
}
