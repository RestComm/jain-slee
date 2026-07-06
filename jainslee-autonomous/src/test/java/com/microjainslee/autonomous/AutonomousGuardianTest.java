/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.autonomous;

import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Self-protection contract: zero threads, escalation ladder ordering,
 * cooldowns, participant fault isolation, and container attachment.
 */
public class AutonomousGuardianTest {

    private final List<AutoCloseable> toClose = new ArrayList<>();

    @After
    public void tearDown() throws Exception {
        for (AutoCloseable c : toClose) {
            c.close();
        }
    }

    private AutonomousGuardian guardian() {
        AutonomousGuardian g = new AutonomousGuardian()
                .actionCooldownMillis(0)
                .gcCooldownMillis(Long.MAX_VALUE); // never actually System.gc() in tests
        toClose.add(g);
        return g;
    }

    private static MemoryReliefParticipant recording(String name,
            List<PressureLevel> sink) {
        return new MemoryReliefParticipant() {
            @Override public String name() { return name; }
            @Override public long relieve(PressureLevel level) {
                sink.add(level);
                return 1;
            }
        };
    }

    @Test
    public void startCreatesNoThreads() {
        int before = Thread.activeCount();
        AutonomousGuardian g = guardian();
        g.start();
        assertTrue(g.isStarted());
        // No guardian-owned thread may appear (JMX listener is passive).
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            assertFalse("no autonomous thread allowed: " + t.getName(),
                    t.getName().contains("autonomous"));
        }
        g.stop();
        assertFalse(g.isStarted());
        assertTrue(Thread.activeCount() <= before + 1); // JMX infra may lazily add one
    }

    @Test
    public void escalationLadderMapsUsageToLevels() {
        AutonomousGuardian g = guardian().watermarks(0.75, 0.88, 0.96);
        assertEquals(PressureLevel.NORMAL, g.evaluate(0.50));
        assertEquals(PressureLevel.ELEVATED, g.evaluate(0.80));
        assertEquals(PressureLevel.CRITICAL, g.evaluate(0.90));
        assertEquals(PressureLevel.EMERGENCY, g.evaluate(0.99));
    }

    @Test
    public void participantsRunUnderPressureButNotWhenNormal() {
        List<PressureLevel> calls = new CopyOnWriteArrayList<>();
        AutonomousGuardian g = guardian();
        g.register(recording("cache", calls));

        g.evaluate(0.10);
        assertTrue("NORMAL must not trigger relief", calls.isEmpty());

        g.evaluate(0.80);
        assertEquals(List.of(PressureLevel.ELEVATED), calls);
    }

    @Test
    public void reliefIsCooldownProtected() {
        List<PressureLevel> calls = new CopyOnWriteArrayList<>();
        AutonomousGuardian g = new AutonomousGuardian()
                .actionCooldownMillis(60_000)
                .gcCooldownMillis(Long.MAX_VALUE);
        toClose.add(g);
        g.register(recording("cache", calls));

        g.evaluate(0.80);
        g.evaluate(0.80);
        g.evaluate(0.80);
        assertEquals("cooldown must collapse repeated pressure into one run",
                1, calls.size());
    }

    @Test
    public void faultyParticipantDoesNotBreakTheChain() {
        List<PressureLevel> calls = new CopyOnWriteArrayList<>();
        AutonomousGuardian g = guardian();
        g.register(new MemoryReliefParticipant() {
            @Override public String name() { return "broken"; }
            @Override public long relieve(PressureLevel level) {
                throw new IllegalStateException("boom");
            }
        });
        g.register(recording("healthy", calls));

        g.evaluate(0.90);
        assertEquals("healthy participant must still run", 1, calls.size());
    }

    @Test
    public void emergencyHookFiresOnlyAtEmergency() {
        AtomicInteger hookCalls = new AtomicInteger();
        AutonomousGuardian g = guardian().onEmergency(level -> hookCalls.incrementAndGet());

        g.evaluate(0.80);
        g.evaluate(0.90);
        assertEquals(0, hookCalls.get());
        g.evaluate(0.99);
        assertEquals(1, hookCalls.get());
    }

    @Test
    public void pressureEvaluateHookBridgesToTelemetry() {
        AtomicInteger evals = new AtomicInteger();
        AutonomousGuardian g = guardian().onPressureEvaluate(evals::incrementAndGet);
        g.evaluate(0.80);
        assertEquals(1, evals.get());
    }

    @Test
    public void attachRegistersStandardContainerParticipants() {
        MicroSleeContainer container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(16)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        try {
            AutonomousGuardian g = guardian().attach(container);
            List<String> names = g.participants().stream()
                    .map(MemoryReliefParticipant::name).toList();
            assertTrue(names.contains("dedup-window"));
            assertTrue(names.contains("out-of-order-buffer"));
            assertTrue(names.contains("ies-result-cache"));
            assertTrue(names.contains("offheap-arena-compaction"));
            // A full ladder run against the real container must not throw.
            g.evaluate(0.99);
            assertTrue(g.reliefRunCount() >= 1);
        } finally {
            container.stop();
        }
    }
}
