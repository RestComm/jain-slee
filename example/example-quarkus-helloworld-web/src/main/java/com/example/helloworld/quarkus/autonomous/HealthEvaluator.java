package com.example.helloworld.quarkus.autonomous;

import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.autonomous.PressureLevel;
import com.microjainslee.telemetry.AlarmEngine;
import com.microjainslee.telemetry.TelemetryPort;
import com.microjainslee.telemetry.TelemetryPort.TelemetrySnapshot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Holistic health scorer — reads the {@link TelemetryPort} snapshot on a fixed
 * cadence and condenses it into a single traffic-light verdict.
 *
 * <p>Where {@link AutonomousGuardian} reacts to <i>memory</i> pressure at the
 * JVM level, this evaluator judges the whole node: heap, CPU, error rate,
 * spunk (misbehaving SBB) alerts, leaked entities and outstanding alarms. It is
 * the app's opinion on "is jainslee healthy right now?" and it is exposed at
 * {@code GET /api/autonomous/health}.</p>
 *
 * <p>Escalation is graduated:</p>
 * <ul>
 *   <li><b>GREEN</b> — nominal, no action.</li>
 *   <li><b>AMBER</b> — degraded; a WARNING alarm is raised.</li>
 *   <li><b>RED</b> — unhealthy; a CRITICAL alarm is raised and the guardian is
 *       poked ({@link AutonomousGuardian#checkNow()}) to run relief immediately
 *       rather than waiting for the next JVM notification.</li>
 * </ul>
 *
 * <p>Zero thread pools: one daemon virtual thread owns the evaluation loop.</p>
 */
public final class HealthEvaluator implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(HealthEvaluator.class);

    public enum Status { GREEN, AMBER, RED }

    public record HealthReport(Status status, List<String> reasons,
                               double heapPct, double cpuLoad,
                               long errors, int spunks, long timestamp) {}

    // ── thresholds (tune per deployment) ──
    private volatile double heapAmberPct = 75.0;
    private volatile double heapRedPct = 90.0;
    private volatile double cpuAmber = 0.80;
    private volatile double cpuRed = 0.95;
    private volatile long errorsAmber = 25;
    private volatile long errorsRed = 100;
    private volatile int spunksAmber = 1;
    private volatile int leaksRed = 1;

    private final TelemetryPort telemetry;
    private final AutonomousGuardian guardian;
    private final long intervalMillis;

    private final AtomicReference<HealthReport> latest =
            new AtomicReference<>(new HealthReport(Status.GREEN, List.of(),
                    0, 0, 0, 0, System.currentTimeMillis()));
    private volatile Status lastAlarmedStatus = Status.GREEN;
    private volatile boolean running;
    private Thread worker;

    public HealthEvaluator(TelemetryPort telemetry, AutonomousGuardian guardian) {
        this(telemetry, guardian, 15_000L);
    }

    public HealthEvaluator(TelemetryPort telemetry, AutonomousGuardian guardian,
                           long intervalMillis) {
        this.telemetry = telemetry;
        this.guardian = guardian;
        this.intervalMillis = intervalMillis;
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        worker = Thread.ofVirtual().name("autonomous-health").start(this::loop);
        LOG.info("[autonomous] health evaluator started (every {}ms)", intervalMillis);
    }

    @Override
    public synchronized void close() {
        running = false;
        if (worker != null) {
            worker.interrupt();
            worker = null;
        }
    }

    /** Latest verdict — served over REST and safe to read from any thread. */
    public HealthReport latest() {
        return latest.get();
    }

    private void loop() {
        while (running) {
            try {
                evaluate();
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (RuntimeException e) {
                LOG.warn("[autonomous] health evaluation failed: {}", e.getMessage());
            }
        }
    }

    /** Score the current snapshot, publish the verdict, and escalate. */
    public HealthReport evaluate() {
        TelemetrySnapshot snap = telemetry.snapshot();
        var r = snap.resources();
        double heapPct = r == null ? 0 : r.heapUsagePercent();
        double cpu = r == null ? 0 : r.cpuLoad();
        long errors = snap.sbbs().stream().mapToLong(s -> s.errors()).sum();
        int spunks = snap.spunks().size();
        long leaks = snap.stales().stream().filter(s -> s.leaked()).count();

        List<String> reasons = new ArrayList<>();
        Status status = Status.GREEN;

        // RED conditions.
        if (heapPct >= heapRedPct) { status = Status.RED; reasons.add("heap " + pct(heapPct)); }
        if (cpu >= cpuRed)         { status = Status.RED; reasons.add("cpu " + pct(cpu * 100)); }
        if (errors >= errorsRed)   { status = Status.RED; reasons.add(errors + " sbb errors"); }
        if (leaks >= leaksRed)     { status = Status.RED; reasons.add(leaks + " leaked entities"); }

        // AMBER conditions (only if not already RED).
        if (status != Status.RED) {
            if (heapPct >= heapAmberPct) { status = Status.AMBER; reasons.add("heap " + pct(heapPct)); }
            if (cpu >= cpuAmber)         { status = Status.AMBER; reasons.add("cpu " + pct(cpu * 100)); }
            if (errors >= errorsAmber)   { status = Status.AMBER; reasons.add(errors + " sbb errors"); }
            if (spunks >= spunksAmber)   { status = Status.AMBER; reasons.add(spunks + " spunk alerts"); }
        }

        HealthReport report = new HealthReport(status, List.copyOf(reasons),
                round(heapPct), round(cpu), errors, spunks, System.currentTimeMillis());
        latest.set(report);
        escalate(report);
        return report;
    }

    private void escalate(HealthReport report) {
        if (report.status() == lastAlarmedStatus) {
            return; // edge-triggered — one alarm per transition, no storm
        }
        lastAlarmedStatus = report.status();
        String detail = String.join(", ", report.reasons());
        switch (report.status()) {
            case GREEN -> LOG.info("[autonomous] health recovered → GREEN");
            case AMBER -> {
                LOG.warn("[autonomous] health degraded → AMBER: {}", detail);
                fire(AlarmEngine.TelemetryAlarmLevel.WARNING, detail);
            }
            case RED -> {
                LOG.error("[autonomous] health critical → RED: {}", detail);
                fire(AlarmEngine.TelemetryAlarmLevel.CRITICAL, detail);
                if (guardian != null) {
                    PressureLevel level = guardian.checkNow(); // force relief now
                    LOG.warn("[autonomous] guardian poked on RED → {}", level);
                }
            }
        }
    }

    private void fire(AlarmEngine.TelemetryAlarmLevel level, String detail) {
        try {
            telemetry.alarmEngine().fire(level, "health-evaluator",
                    "node health " + lastAlarmedStatus + ": " + detail, null);
        } catch (RuntimeException e) {
            LOG.warn("[autonomous] alarm fire failed: {}", e.getMessage());
        }
    }

    private static String pct(double v) {
        return Math.round(v) + "%";
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
