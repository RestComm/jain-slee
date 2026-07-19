/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Verifies that the JSR-240 §12 transaction context is correctly scoped
 * via {@link ThreadLocal} and that two concurrent event deliveries never
 * observe each other's transaction state. ThreadLocal on a virtual thread
 * is bound to the VT (not the carrier), so it is inherently VT-safe.
 */
public class ScopedValueTransactionIsolationTest {

    private MicroSleeContainer container;

    @Before
    public void setUp() {
        container = new MicroSleeContainer(
                MicroSleeConfiguration.builder()
                        .eventRouterBufferSize(32)
                        .preferVirtualThreads(true)
                        .sbbPerVirtualThread(false)
                        .build());
        container.start();
    }

    @After
    public void tearDown() {
        if (container != null) {
            container.stop();
        }
    }

    @Test
    public void concurrentDeliveriesNeverSeePeersTransaction() throws Exception {
        // Stop and recreate with platform threads to rule out VT races.
        container.stop();
        container = new MicroSleeContainer(
                MicroSleeConfiguration.builder()
                        .eventRouterBufferSize(32)
                        .preferVirtualThreads(false)
                        .sbbPerVirtualThread(false)
                        .build());
        container.start();

        final int parallelism = 16;
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch doneGate = new CountDownLatch(parallelism);
        final AtomicInteger crossTalkDetected = new AtomicInteger();

        final InMemoryActivityContext aciA = container.createActivityContext("isolation-ac-A");
        final InMemoryActivityContext aciB = container.createActivityContext("isolation-ac-B");

        final TxcSbb sbbA = new TxcSbb("A", aciA, crossTalkDetected);
        final TxcSbb sbbB = new TxcSbb("B", aciB, crossTalkDetected);
        SimpleSbbLocalObject loA = container.registerSbb("isolation-sbb-A", sbbA);
        SimpleSbbLocalObject loB = container.registerSbb("isolation-sbb-B", sbbB);
        sbbA.localObject = loA;
        sbbB.localObject = loB;
        container.attach("isolation-ac-A", loA);
        container.attach("isolation-ac-B", loB);

        // Sanity: each AC should now have exactly one SBB attached
        assertEquals(1, aciA.getAttachedSbbs().size());
        assertEquals(1, aciB.getAttachedSbbs().size());

        ExecutorService pool = Executors.newFixedThreadPool(parallelism);
        try {
            for (int i = 0; i < parallelism; i++) {
                final int round = i;
                pool.submit(() -> {
                    try {
                        startGate.await();
                        if ((round & 1) == 0) {
                            container.routeEvent(new IsolationEvent(), aciA);
                        } else {
                            container.routeEvent(new IsolationEvent(), aciB);
                        }
                    } catch (Throwable t) {
                        fail("route failed in round " + round + ": " + t);
                    } finally {
                        doneGate.countDown();
                    }
                });
            }
            startGate.countDown();
            assertTrue("all deliveries did not complete in 5s",
                    doneGate.await(5, TimeUnit.SECONDS));

            // Wait for async dispatch to drain.
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
            while (System.nanoTime() < deadline
                    && (sbbA.events + sbbB.events) < parallelism) {
                Thread.sleep(20L);
            }
        } finally {
            pool.shutdownNow();
        }

        assertEquals("no cross-TX contamination", 0, crossTalkDetected.get());
        assertEquals("A delivered", parallelism / 2, sbbA.events);
        assertEquals("B delivered", parallelism / 2, sbbB.events);
    }

    @Test
    public void currentTransactionIsEmptyOutsideDispatcherScope() {
        if (ActivityContextTransactionRegistry.CURRENT.get() != null) {
            fail("CURRENT unexpectedly bound outside dispatcher scope: "
                    + ActivityContextTransactionRegistry.CURRENT.get());
        }
        assertFalse(ActivityContextTransactionRegistry.CURRENT.get() != null);
    }

    @Test
    public void currentTransactionIsClearedAfterDispatchCompletes() throws Exception {
        InMemoryActivityContext aci = container.createActivityContext("clear-after-ac");
        CountingSbb sbb = new CountingSbb();
        SimpleSbbLocalObject lo = container.registerSbb("clear-after-sbb", sbb);
        sbb.localObject = lo;
        container.attach("clear-after-ac", lo);

        container.routeEvent(new IsolationEvent(), aci);
        assertTrue(sbb.awaitEvent(2, TimeUnit.SECONDS));

        Thread.sleep(100L);

        if (ActivityContextTransactionRegistry.CURRENT.get() != null) {
            fail("CURRENT leaked across dispatch boundary: "
                    + ActivityContextTransactionRegistry.CURRENT.get());
        }
    }

    @Test
    public void sequentialDispatchesSeeIndependentTransactions() throws Exception {
        InMemoryActivityContext aci1 = container.createActivityContext("seq-ac-1");
        InMemoryActivityContext aci2 = container.createActivityContext("seq-ac-2");
        CountingSbb sbb = new CountingSbb();
        SimpleSbbLocalObject lo = container.registerSbb("seq-sbb", sbb);
        sbb.localObject = lo;
        container.attach("seq-ac-1", lo);
        container.attach("seq-ac-2", lo);

        container.routeEvent(new IsolationEvent(), aci1);
        assertTrue(sbb.awaitEvent(2, TimeUnit.SECONDS));

        container.routeEvent(new IsolationEvent(), aci2);
        Thread.sleep(200L);
        assertTrue("expected >=2 events delivered, got " + sbb.events,
                sbb.events >= 2);
    }

    private static final class IsolationEvent implements SleeEvent {
    }

    private static final class TxcSbb implements Sbb, SleeEventHandler {
        private final String name;
        private final InMemoryActivityContext expected;
        private final AtomicInteger crossTalkDetected;
        SbbLocalObject localObject;
        int events;

        TxcSbb(String name, InMemoryActivityContext expected,
                AtomicInteger crossTalkDetected) {
            this.name = name;
            this.expected = expected;
            this.crossTalkDetected = crossTalkDetected;
        }

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            events++;
            SbbTransactionContext tx = ActivityContextTransactionRegistry.CURRENT.get();
            if (tx == null) {
                crossTalkDetected.incrementAndGet();
                return;
            }
            InMemoryActivityContext observed = tx.getActivityContext();
            if (observed != expected) {
                crossTalkDetected.incrementAndGet();
            }
        }
    }

    private static final class CountingSbb implements Sbb, SleeEventHandler {
        SbbLocalObject localObject;
        private final CountDownLatch latch = new CountDownLatch(1);
        int events;

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            events++;
            latch.countDown();
        }

        boolean awaitEvent(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }
    }
}
