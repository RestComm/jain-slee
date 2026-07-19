/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link DispatchObserver} coverage for the <b>virtual-thread</b> delivery
 * paths — the {@code entity.submit(...)} sites that the inline-only
 * {@link DispatchObserverTest} does not reach. Exercises both the SYNC
 * (latch) path and the ASYNC_COMMIT path with a real pooled entity, on the
 * delivering virtual thread.
 */
public class DispatchObserverPooledTest {

    private static final class RecordingObserver implements DispatchObserver {
        record Processed(String sbbType, String entityId, long latencyNs) {}
        final List<Processed> processed = new CopyOnWriteArrayList<>();
        final List<Throwable> failed = new CopyOnWriteArrayList<>();

        @Override public void onEventProcessed(String sbbType, String entityId, long latencyNs) {
            processed.add(new Processed(sbbType, entityId, latencyNs));
        }
        @Override public void onDispatchError(String sbbType, String entityId, Throwable error) {
            failed.add(error);
        }
    }

    public static final class ProbeEvent implements SleeEvent { }

    public static final class PooledHappySbb implements Sbb, SleeEventHandler {
        static final AtomicInteger DELIVERED = new AtomicInteger();
        @Override public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            DELIVERED.incrementAndGet();
        }
    }

    public static final class PooledAngrySbb implements Sbb, SleeEventHandler {
        @Override public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            throw new IllegalStateException("pooled boom");
        }
    }

    private MicroSleeContainer pooled(EventDeliveryMode mode) {
        MicroSleeContainer c = new MicroSleeContainer(
                MicroSleeConfiguration.builder()
                        .eventRouterBufferSize(64)
                        .sbbPoolMin(2)
                        .sbbPoolMax(16)
                        .sbbPerVirtualThread(true)
                        .eventDeliveryMode(mode)
                        .build());
        c.start();
        return c;
    }

    private static void await(java.util.function.BooleanSupplier done) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        while (System.nanoTime() < deadline && !done.getAsBoolean()) {
            Thread.sleep(20L);
        }
        assertTrue("condition not reached within 10s", done.getAsBoolean());
    }

    @Test
    public void syncVirtualThreadDeliveryIsObserved() throws Exception {
        PooledHappySbb.DELIVERED.set(0);
        MicroSleeContainer container = pooled(EventDeliveryMode.SYNC);
        RecordingObserver observer = new RecordingObserver();
        container.getEventRouter().setDispatchObserver(observer);
        try {
            container.registerSbbType(PooledHappySbb.class, PooledHappySbb::new);
            SbbLocalObject lo = container.acquireEntity("vt-1", PooledHappySbb.class);
            InMemoryActivityContext aci = container.createActivityContext("vt-1");
            container.attach("vt-1", lo);

            int burst = 4;
            for (int i = 0; i < burst; i++) {
                container.routeEvent(new ProbeEvent(), aci);
            }
            await(() -> observer.processed.size() >= burst);

            assertEquals(burst, PooledHappySbb.DELIVERED.get());
            assertEquals(burst, observer.processed.size());
            for (RecordingObserver.Processed p : observer.processed) {
                assertEquals("PooledHappySbb", p.sbbType());
                assertTrue("entity id must be resolved on the VT path",
                        p.entityId() != null && !p.entityId().equals("?"));
                assertTrue(p.latencyNs() > 0);
            }
            assertTrue(observer.failed.isEmpty());
        } finally {
            container.stop();
        }
    }

    @Test
    public void asyncCommitDeliveryIsObserved() throws Exception {
        PooledHappySbb.DELIVERED.set(0);
        MicroSleeContainer container = pooled(EventDeliveryMode.ASYNC_COMMIT);
        RecordingObserver observer = new RecordingObserver();
        container.getEventRouter().setDispatchObserver(observer);
        try {
            container.registerSbbType(PooledHappySbb.class, PooledHappySbb::new);
            SbbLocalObject lo = container.acquireEntity("vt-async-1", PooledHappySbb.class);
            InMemoryActivityContext aci = container.createActivityContext("vt-async-1");
            container.attach("vt-async-1", lo);

            int burst = 3;
            for (int i = 0; i < burst; i++) {
                container.routeEvent(new ProbeEvent(), aci);
            }
            await(() -> observer.processed.size() >= burst);

            assertEquals(burst, observer.processed.size());
            assertEquals("PooledHappySbb", observer.processed.get(0).sbbType());
            assertTrue(observer.failed.isEmpty());
        } finally {
            container.stop();
        }
    }

    @Test
    public void throwingSbbOnVirtualThreadIsReportedAsError() throws Exception {
        MicroSleeContainer container = pooled(EventDeliveryMode.SYNC);
        RecordingObserver observer = new RecordingObserver();
        container.getEventRouter().setDispatchObserver(observer);
        try {
            container.registerSbbType(PooledAngrySbb.class, PooledAngrySbb::new);
            SbbLocalObject lo = container.acquireEntity("vt-err-1", PooledAngrySbb.class);
            InMemoryActivityContext aci = container.createActivityContext("vt-err-1");
            container.attach("vt-err-1", lo);

            container.routeEvent(new ProbeEvent(), aci);
            await(() -> !observer.failed.isEmpty());

            assertEquals("pooled boom", observer.failed.get(0).getMessage());
            assertTrue(observer.processed.isEmpty());
        } finally {
            container.stop();
        }
    }
}
