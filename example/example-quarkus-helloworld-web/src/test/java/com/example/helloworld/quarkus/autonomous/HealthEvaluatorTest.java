package com.example.helloworld.quarkus.autonomous;

import com.example.helloworld.quarkus.autonomous.HealthEvaluator.HealthReport;
import com.example.helloworld.quarkus.autonomous.HealthEvaluator.Status;
import com.example.helloworld.quarkus.support.TelemetryFixtures;
import com.example.helloworld.quarkus.support.TelemetryFixtures.FakeTelemetryPort;
import com.microjainslee.autonomous.AutonomousGuardian;
import com.microjainslee.autonomous.MemoryReliefParticipant;
import com.microjainslee.autonomous.PressureLevel;
import com.microjainslee.telemetry.AlarmEngine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Full behavioural coverage of the holistic health scorer: the GREEN/AMBER/RED
 * matrix and its boundaries, RED-over-AMBER precedence, edge-triggered alarms
 * (one per transition, none on repeats), the guardian poke on RED, null-guardian
 * safety, and the asynchronous evaluation loop.
 */
class HealthEvaluatorTest {

    private HealthEvaluator evaluator;

    @AfterEach
    void tearDown() {
        if (evaluator != null) {
            evaluator.close();
        }
    }

    private HealthEvaluator on(FakeTelemetryPort port, AutonomousGuardian guardian) {
        evaluator = new HealthEvaluator(port, guardian);
        return evaluator;
    }

    // ── scoring matrix ────────────────────────────────────────────────

    @Test
    void nominalIsGreenWithNoReasons() {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(20, 0.10, 0, 0, 0));
        HealthReport r = on(port, null).evaluate();
        assertEquals(Status.GREEN, r.status());
        assertTrue(r.reasons().isEmpty());
    }

    @Test
    void heapDrivesAmberThenRed() {
        assertEquals(Status.AMBER, evalOf(80, 0.10, 0, 0, 0).status());
        assertEquals(Status.RED, evalOf(95, 0.10, 0, 0, 0).status());
    }

    @Test
    void cpuDrivesAmberThenRed() {
        assertEquals(Status.AMBER, evalOf(20, 0.85, 0, 0, 0).status());
        assertEquals(Status.RED, evalOf(20, 0.97, 0, 0, 0).status());
    }

    @Test
    void errorsDriveAmberThenRed() {
        assertEquals(Status.AMBER, evalOf(20, 0.10, 30, 0, 0).status());
        assertEquals(Status.RED, evalOf(20, 0.10, 150, 0, 0).status());
    }

    @Test
    void spunksDriveAmberOnly() {
        HealthReport r = evalOf(20, 0.10, 0, 2, 0);
        assertEquals(Status.AMBER, r.status());
        assertTrue(r.reasons().stream().anyMatch(s -> s.contains("spunk")));
    }

    @Test
    void aLeakedEntityIsAlwaysRed() {
        HealthReport r = evalOf(20, 0.10, 0, 0, 1);
        assertEquals(Status.RED, r.status());
        assertTrue(r.reasons().stream().anyMatch(s -> s.contains("leaked")));
    }

    @Test
    void redTakesPrecedenceOverAmberSignals() {
        // heap RED + spunks (an AMBER signal) → RED, and the amber reason is dropped.
        HealthReport r = evalOf(95, 0.10, 0, 5, 0);
        assertEquals(Status.RED, r.status());
        assertFalse(r.reasons().stream().anyMatch(s -> s.contains("spunk")));
    }

    // ── threshold boundaries (>= is inclusive) ────────────────────────

    @Test
    void boundariesAreInclusive() {
        assertEquals(Status.AMBER, evalOf(75.0, 0.10, 0, 0, 0).status(), "heap==75 → AMBER");
        assertEquals(Status.RED, evalOf(90.0, 0.10, 0, 0, 0).status(), "heap==90 → RED");
        assertEquals(Status.AMBER, evalOf(20, 0.80, 0, 0, 0).status(), "cpu==0.80 → AMBER");
        assertEquals(Status.RED, evalOf(20, 0.95, 0, 0, 0).status(), "cpu==0.95 → RED");
        assertEquals(Status.AMBER, evalOf(20, 0.10, 25, 0, 0).status(), "errors==25 → AMBER");
        assertEquals(Status.RED, evalOf(20, 0.10, 100, 0, 0).status(), "errors==100 → RED");
    }

    @Test
    void justBelowThresholdsStayGreen() {
        assertEquals(Status.GREEN, evalOf(74.99, 0.79, 24, 0, 0).status());
    }

    @Test
    void reportRoundsGaugesToTwoDecimals() {
        HealthReport r = evalOf(24.567, 0.123, 0, 0, 0);
        assertEquals(24.57, r.heapPct(), 1e-9);
        assertEquals(0.12, r.cpuLoad(), 1e-9);
    }

    // ── alarms are edge-triggered ─────────────────────────────────────

    @Test
    void alarmsFireOncePerTransitionNotPerEvaluation() {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(20, 0.10, 0, 0, 0));
        AlarmEngine alarms = port.alarmEngine();
        HealthEvaluator e = on(port, null);

        e.evaluate();                       // GREEN → GREEN: no alarm
        assertEquals(0, alarms.active().size());

        port.setSnapshot(TelemetryFixtures.snapshot(80, 0.10, 0, 0, 0));
        e.evaluate();                       // GREEN → AMBER: +1
        e.evaluate();                       // AMBER → AMBER: no new alarm
        assertEquals(1, alarms.active().size());
        assertEquals(AlarmEngine.TelemetryAlarmLevel.WARNING, alarms.active().get(0).level());

        port.setSnapshot(TelemetryFixtures.snapshot(95, 0.10, 0, 0, 0));
        e.evaluate();                       // AMBER → RED: +1
        e.evaluate();                       // RED → RED: no new alarm
        assertEquals(2, alarms.active().size());
        assertEquals(AlarmEngine.TelemetryAlarmLevel.CRITICAL, alarms.active().get(1).level());

        port.setSnapshot(TelemetryFixtures.snapshot(20, 0.10, 0, 0, 0));
        e.evaluate();                       // RED → GREEN: recovery logs, no alarm
        assertEquals(2, alarms.active().size());

        port.setSnapshot(TelemetryFixtures.snapshot(80, 0.10, 0, 0, 0));
        e.evaluate();                       // GREEN → AMBER again: +1
        assertEquals(3, alarms.active().size());
    }

    // ── guardian interaction ──────────────────────────────────────────

    @Test
    void redPokesTheGuardianToRelieveNow() {
        AtomicInteger reliefCalls = new AtomicInteger();
        AutonomousGuardian guardian = new AutonomousGuardian()
                .watermarks(0.0001, 0.0002, 0.0003) // any real heap ratio trips relief
                .actionCooldownMillis(0)
                .gcCooldownMillis(Long.MAX_VALUE);   // never actually System.gc()
        guardian.register(new MemoryReliefParticipant() {
            @Override public String name() { return "probe"; }
            @Override public long relieve(PressureLevel level) {
                reliefCalls.incrementAndGet();
                return 1;
            }
        });

        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(95, 0.10, 0, 0, 0));
        on(port, guardian).evaluate();       // RED → checkNow() → participant relieves
        assertTrue(reliefCalls.get() >= 1, "RED must poke guardian.checkNow()");
    }

    @Test
    void amberDoesNotPokeTheGuardian() {
        AtomicInteger reliefCalls = new AtomicInteger();
        AutonomousGuardian guardian = new AutonomousGuardian()
                .watermarks(0.0001, 0.0002, 0.0003)
                .actionCooldownMillis(0)
                .gcCooldownMillis(Long.MAX_VALUE);
        guardian.register(new MemoryReliefParticipant() {
            @Override public String name() { return "probe"; }
            @Override public long relieve(PressureLevel level) {
                reliefCalls.incrementAndGet();
                return 1;
            }
        });

        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(80, 0.10, 0, 0, 0));
        on(port, guardian).evaluate();       // AMBER → no poke
        assertEquals(0, reliefCalls.get());
    }

    @Test
    void nullGuardianIsSafeOnRed() {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(99, 0.99, 500, 9, 3));
        HealthEvaluator e = on(port, null);
        assertDoesNotThrow(e::evaluate);
        assertEquals(Status.RED, e.latest().status());
    }

    // ── latest() + async loop ─────────────────────────────────────────

    @Test
    void latestDefaultsToGreenBeforeFirstEvaluation() {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(99, 0.99, 0, 0, 0));
        HealthEvaluator e = on(port, null);
        assertEquals(Status.GREEN, e.latest().status());
    }

    @Test
    void evaluatePublishesLatest() {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(95, 0.10, 0, 0, 0));
        HealthEvaluator e = on(port, null);
        HealthReport returned = e.evaluate();
        assertSame(returned, e.latest());
    }

    @Test
    void asyncLoopEvaluatesOnItsOwnThread() throws Exception {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(95, 0.10, 0, 0, 0));
        evaluator = new HealthEvaluator(port, null, 10L); // tight interval
        evaluator.start();
        evaluator.start(); // idempotent — must not spawn a second worker

        // The worker publishes latest() then fires the alarm; poll for the
        // alarm so we observe the whole evaluate() cycle, not a mid-cycle state.
        long deadline = System.currentTimeMillis() + 2_000;
        while (port.alarmEngine().active().isEmpty()
                && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
        }
        assertEquals(Status.RED, evaluator.latest().status());
        assertTrue(port.alarmEngine().active().size() >= 1);
    }

    // ── helper ─────────────────────────────────────────────────────────

    private HealthReport evalOf(double heapPct, double cpu, long errors, int spunks, long leaks) {
        var port = new FakeTelemetryPort(TelemetryFixtures.snapshot(heapPct, cpu, errors, spunks, leaks));
        return on(port, null).evaluate();
    }
}
