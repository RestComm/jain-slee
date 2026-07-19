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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The {@link DispatchObserver} seam, end to end through a real container:
 * every delivered event reports type/entity/latency, a throwing SBB reports
 * the error, and a misbehaving observer can never disturb delivery.
 */
public class DispatchObserverTest {

    private MicroSleeContainer container;

    @Before
    public void setUp() {
        container = new MicroSleeContainer(
                MicroSleeConfiguration.builder()
                        .eventRouterBufferSize(64)
                        .preferVirtualThreads(false)
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

    /** Recording observer used by the tests. */
    private static final class RecordingObserver implements DispatchObserver {
        record Processed(String sbbType, String entityId, long latencyNs) {}
        record Failed(String sbbType, String entityId, Throwable error) {}
        final List<Processed> processed = new CopyOnWriteArrayList<>();
        final List<Failed> failed = new CopyOnWriteArrayList<>();

        @Override
        public void onEventProcessed(String sbbType, String entityId, long latencyNs) {
            processed.add(new Processed(sbbType, entityId, latencyNs));
        }

        @Override
        public void onDispatchError(String sbbType, String entityId, Throwable error) {
            failed.add(new Failed(sbbType, entityId, error));
        }
    }

    private static final class ProbeEvent implements SleeEvent {
    }

    private static final class HappySbb implements Sbb, SleeEventHandler {
        final AtomicInteger events = new AtomicInteger();
        SbbLocalObject localObject;
        @Override public void sbbCreate() { }
        @Override public void sbbActivate() { }
        @Override public void sbbPassivate() { }
        @Override public void sbbRemove() { }
        @Override public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            events.incrementAndGet();
        }
    }

    private static final class AngrySbb implements Sbb, SleeEventHandler {
        @Override public void sbbCreate() { }
        @Override public void sbbActivate() { }
        @Override public void sbbPassivate() { }
        @Override public void sbbRemove() { }
        @Override public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            throw new IllegalStateException("angry sbb");
        }
    }

    private static void await(java.util.function.BooleanSupplier done) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        while (System.nanoTime() < deadline && !done.getAsBoolean()) {
            Thread.sleep(20L);
        }
        assertTrue("condition not reached within 10s", done.getAsBoolean());
    }

    @Test
    public void everyDeliveryIsObservedWithTypeEntityAndLatency() throws Exception {
        RecordingObserver observer = new RecordingObserver();
        container.getEventRouter().setDispatchObserver(observer);

        InMemoryActivityContext aci = container.createActivityContext("obs-ac");
        HappySbb sbb = new HappySbb();
        sbb.localObject = container.registerSbb("obs-sbb-1", sbb,
                new EventMask(ProbeEvent.class));
        container.attach("obs-ac", sbb.localObject);

        int burst = 5;
        for (int i = 0; i < burst; i++) {
            container.routeEvent(new ProbeEvent(), aci);
        }
        await(() -> observer.processed.size() >= burst);

        assertEquals(burst, sbb.events.get());
        assertEquals(burst, observer.processed.size());
        assertTrue(observer.failed.isEmpty());
        for (RecordingObserver.Processed p : observer.processed) {
            assertEquals("HappySbb", p.sbbType());
            assertEquals("obs-sbb-1", p.entityId());
            assertTrue("latency must be measured", p.latencyNs() > 0);
        }
    }

    @Test
    public void throwingSbbIsReportedAsDispatchError() throws Exception {
        RecordingObserver observer = new RecordingObserver();
        container.getEventRouter().setDispatchObserver(observer);

        InMemoryActivityContext aci = container.createActivityContext("obs-err-ac");
        AngrySbb sbb = new AngrySbb();
        container.attach("obs-err-ac",
                container.registerSbb("obs-err-1", sbb, new EventMask(ProbeEvent.class)));

        container.routeEvent(new ProbeEvent(), aci);
        await(() -> !observer.failed.isEmpty());

        RecordingObserver.Failed f = observer.failed.get(0);
        assertEquals("AngrySbb", f.sbbType());
        assertEquals("obs-err-1", f.entityId());
        assertEquals("angry sbb", f.error().getMessage());
        assertTrue(observer.processed.isEmpty());
    }

    @Test
    public void throwingObserverNeverDisturbsDelivery() throws Exception {
        container.getEventRouter().setDispatchObserver(new DispatchObserver() {
            @Override public void onEventProcessed(String t, String e, long l) {
                throw new RuntimeException("observer bug");
            }
            @Override public void onDispatchError(String t, String e, Throwable err) {
                throw new RuntimeException("observer bug");
            }
        });

        InMemoryActivityContext aci = container.createActivityContext("obs-bug-ac");
        HappySbb sbb = new HappySbb();
        container.attach("obs-bug-ac",
                container.registerSbb("obs-bug-1", sbb, new EventMask(ProbeEvent.class)));

        int burst = 3;
        for (int i = 0; i < burst; i++) {
            container.routeEvent(new ProbeEvent(), aci);
        }
        await(() -> sbb.events.get() >= burst);
        assertEquals("all events must still be delivered", burst, sbb.events.get());
    }

    @Test
    public void observerCanBeClearedAtRuntime() throws Exception {
        RecordingObserver observer = new RecordingObserver();
        container.getEventRouter().setDispatchObserver(observer);
        container.getEventRouter().setDispatchObserver(null);   // cleared

        InMemoryActivityContext aci = container.createActivityContext("obs-off-ac");
        HappySbb sbb = new HappySbb();
        container.attach("obs-off-ac",
                container.registerSbb("obs-off-1", sbb, new EventMask(ProbeEvent.class)));

        container.routeEvent(new ProbeEvent(), aci);
        await(() -> sbb.events.get() >= 1);
        assertTrue("cleared observer must see nothing", observer.processed.isEmpty());
    }
}
