package com.example.helloworld.quarkus.support;

import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.ErrorCollector;
import com.microjainslee.telemetry.RaCollector;
import com.microjainslee.telemetry.ResourceMonitor;
import com.microjainslee.telemetry.SbbCollector;
import com.microjainslee.telemetry.SpunkDetector;
import com.microjainslee.telemetry.StaleDetector;
import com.microjainslee.telemetry.TelemetryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Test doubles for exercising the {@code telemetry/} and {@code autonomous/}
 * modules without a running container: a builder for
 * {@link TelemetryPort.TelemetrySnapshot} and a controllable fake
 * {@link TelemetryPort} backed by a real {@link AlarmEngine} (so alarm
 * assertions are exact).
 */
public final class TelemetryFixtures {

    private TelemetryFixtures() {
    }

    /** Snapshot with the given health-relevant signals; everything else nominal. */
    public static TelemetryPort.TelemetrySnapshot snapshot(double heapPct, double cpuLoad,
                                                           long errors, int spunks, long leaks) {
        var resources = new ResourceMonitor.ResourceSnapshot(
                /* heapUsedMb */ (long) (heapPct * 10), /* heapMaxMb */ 1000,
                heapPct, cpuLoad,
                /* activeThreads */ 8, /* virtualThreads */ 1042,
                /* gcCount */ 3, /* gcTimeMs */ 45, /* fds */ 128,
                System.currentTimeMillis());

        var sbbs = List.of(new SbbCollector.PerType(
                "HelloWorldSbb", /* active */ 42, errors, /* spunks */ spunks,
                /* eps */ 1234.5, /* p99us */ 450));

        List<SpunkDetector.SpunkAlert> spunkAlerts = new ArrayList<>();
        for (int i = 0; i < spunks; i++) {
            spunkAlerts.add(new SpunkDetector.SpunkAlert(
                    "HelloWorldSbb", "e" + i, "blocking>100ms",
                    System.currentTimeMillis(), Map.of()));
        }

        List<StaleDetector.StaleAlert> stales = new ArrayList<>();
        for (long i = 0; i < leaks; i++) {
            stales.add(new StaleDetector.StaleAlert(
                    "leaked" + i, "HelloWorldSbb",
                    System.currentTimeMillis() - 3_600_000, 3_600_000, /* leaked */ true));
        }
        // one non-leaked idle entity to prove only leaks are counted
        stales.add(new StaleDetector.StaleAlert(
                "idle", "HelloWorldSbb", System.currentTimeMillis() - 600_000,
                600_000, /* leaked */ false));

        return new TelemetryPort.TelemetrySnapshot(
                sbbs, List.of(), resources,
                List.of(), spunkAlerts, stales, List.of(),
                /* autoReconfigEnabled */ true, List.of());
    }

    /** Snapshot with a null resource block — exercises defensive null handling. */
    public static TelemetryPort.TelemetrySnapshot snapshotNullResources() {
        return new TelemetryPort.TelemetrySnapshot(
                List.of(), List.of(), null,
                List.of(), List.of(), List.of(), List.of(),
                false, List.of());
    }

    /** A {@link TelemetryPort} whose snapshot is settable and whose alarm engine is real. */
    public static final class FakeTelemetryPort implements TelemetryPort {
        private volatile TelemetrySnapshot current;
        private final AlarmEngine alarms = new AlarmEngine();

        public FakeTelemetryPort(TelemetrySnapshot initial) {
            this.current = initial;
        }

        public void setSnapshot(TelemetrySnapshot snap) {
            this.current = snap;
        }

        @Override public TelemetrySnapshot snapshot() { return current; }
        @Override public AlarmEngine alarmEngine() { return alarms; }

        @Override public boolean isAutoReconfigEnabled() { return true; }
        @Override public void setAutoReconfigEnabled(boolean enabled) { }
        @Override public String scrape() { return ""; }

        @Override public SbbCollector sbbCollector() { throw unsupported(); }
        @Override public RaCollector raCollector() { throw unsupported(); }
        @Override public ErrorCollector errorCollector() { throw unsupported(); }
        @Override public ResourceMonitor resourceMonitor() { throw unsupported(); }
        @Override public SpunkDetector spunkDetector() { throw unsupported(); }
        @Override public StaleDetector staleDetector() { throw unsupported(); }
        @Override public com.microjainslee.telemetry.AutoReconfigEngine autoReconfig() { throw unsupported(); }
        @Override public Counter customCounter(String name, String... tagPairs) { throw unsupported(); }
        @Override public Gauge customGauge(String name, Supplier<Number> supplier, String... tagPairs) { throw unsupported(); }

        private static UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("not needed for these tests");
        }
    }
}
