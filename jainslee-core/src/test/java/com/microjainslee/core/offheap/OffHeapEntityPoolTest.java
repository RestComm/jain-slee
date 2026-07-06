/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core.offheap;

import com.microjainslee.api.OffHeapBindable;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.annotations.CmpField;
import com.microjainslee.api.annotations.OffHeap;
import com.microjainslee.api.annotations.StorageType;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SimpleSbbLocalObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration of the off-heap arena with the entity pool lifecycle:
 * bind on acquire, free on release, live rebinding during compaction,
 * and the {@code offHeapEnabled(false)} kill switch.
 */
public class OffHeapEntityPoolTest {

    /**
     * Hand-written equivalent of a generated {@code $Concrete}: carries
     * {@code @OffHeap} + {@code @CmpField} metadata and reads/writes its
     * slot through {@link OffHeapCmpAccessor} exactly like emitted code.
     */
    @OffHeap(storage = StorageType.DIRECT, maxSlots = 32)
    public static class SessionSbb implements Sbb, OffHeapBindable {
        private volatile long base;
        private volatile String entityId;

        private static OffHeapLayout layout() {
            return OffHeapRuntime.layoutFor(SessionSbb.class);
        }

        @Override public void bindSlot(long baseAddr) { this.base = baseAddr; }
        @Override public void unbindSlot() { this.base = 0L; }
        @Override public void setEntityId(String id) { this.entityId = id; }
        @Override public long slotAddress() { return base; }

        @CmpField("msisdn")
        public String getMsisdn() {
            return OffHeapCmpAccessor.readString(layout(), base, layout().indexOf("msisdn"));
        }

        public void setMsisdn(String v) {
            OffHeapCmpAccessor.writeString(layout(), base, layout().indexOf("msisdn"), v);
        }

        @CmpField("step")
        public int getStep() {
            return OffHeapCmpAccessor.readInt(layout(), base, layout().indexOf("step"));
        }

        public void setStep(int v) {
            OffHeapCmpAccessor.writeInt(layout(), base, layout().indexOf("step"), v);
        }
    }

    private MicroSleeContainer container;

    @Before
    public void setUp() {
        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(16)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        container.registerSbbType(SessionSbb.class, SessionSbb::new);
    }

    @After
    public void tearDown() {
        container.stop();
    }

    private SessionSbb acquire(String id) throws Exception {
        SimpleSbbLocalObject lo = container.acquireEntity(id, SessionSbb.class);
        assertTrue(lo.awaitReady(5, TimeUnit.SECONDS));
        return (SessionSbb) lo.getSbb();
    }

    private OffHeapArena arena() {
        Collection<OffHeapArena> arenas =
                container.getSbbEntityPool().getOffHeapArenas();
        assertEquals(1, arenas.size());
        return arenas.iterator().next();
    }

    @Test
    public void acquireBindsSlotAndStateLivesOffHeap() throws Exception {
        SessionSbb sbb = acquire("s1");
        assertNotEquals("acquire must bind an off-heap slot", 0L, sbb.slotAddress());

        sbb.setMsisdn("84900000009");
        sbb.setStep(4);
        assertEquals("84900000009", sbb.getMsisdn());
        assertEquals(4, sbb.getStep());
        assertEquals(1, arena().occupiedCount());
    }

    @Test
    public void releaseFreesTheSlot() throws Exception {
        SessionSbb sbb = acquire("s1");
        assertEquals(1, arena().occupiedCount());
        container.releaseEntity("s1");
        // release is asynchronous through the local object — wait for it
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (arena().occupiedCount() > 0 && System.nanoTime() < deadline) {
            Thread.sleep(10);
        }
        assertEquals("slot must return to the free list", 0, arena().occupiedCount());
        assertEquals("SBB must be unbound", 0L, sbb.slotAddress());
    }

    @Test
    public void compactionRebindsLiveEntities() throws Exception {
        SessionSbb a = acquire("a");
        SessionSbb b = acquire("b");
        SessionSbb c = acquire("c");
        c.setMsisdn("KEEP-ME");
        c.setStep(9);

        long bAddr = b.slotAddress();
        container.releaseEntity("b");
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (arena().occupiedCount() > 2 && System.nanoTime() < deadline) {
            Thread.sleep(10);
        }

        int moved = arena().compact();
        assertEquals(1, moved);
        assertEquals("live SBB must be rebound to the moved slot",
                bAddr, c.slotAddress());
        assertEquals("KEEP-ME", c.getMsisdn());
        assertEquals(9, c.getStep());
        assertEquals("84-prefix state of A untouched", 0, a.getStep());
    }

    @Test
    public void offHeapDisabledConfigSkipsBinding() throws Exception {
        MicroSleeContainer disabled = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(16)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .offHeapEnabled(false)
                .build());
        disabled.start();
        try {
            disabled.registerSbbType(SessionSbb.class, SessionSbb::new);
            SimpleSbbLocalObject lo = disabled.acquireEntity("x", SessionSbb.class);
            assertTrue(lo.awaitReady(5, TimeUnit.SECONDS));
            SessionSbb sbb = (SessionSbb) lo.getSbb();
            assertEquals("kill switch must prevent slot binding", 0L, sbb.slotAddress());
            assertTrue(disabled.getSbbEntityPool().getOffHeapArenas().isEmpty());
        } finally {
            disabled.stop();
        }
    }

    @Test
    public void containerStopClosesArenas() throws Exception {
        acquire("s1");
        OffHeapArena arena = arena();
        container.stop();
        try {
            arena.allocate("post-stop");
            throw new AssertionError("expected closed arena");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("closed"));
        }
    }
}
