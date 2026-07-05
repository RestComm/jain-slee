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

import com.microjainslee.api.ChildRelation;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbContext;
import com.microjainslee.api.SbbID;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.ServiceID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Edge-case tests for {@link MicroSleeContainer} lifecycle and SBB
 * registration invariants.
 */
public class EdgeCaseContainerTest {

    private MicroSleeContainer container;

    @Before
    public void startContainer() {
        container = new MicroSleeContainer();
        container.start();
    }

    @After
    public void stopContainer() {
        if (container != null) {
            container.stop();
        }
        CmpFieldStoreLocator.set(null);
    }

    /** Test-only SBB with a single CMP field. */
    public abstract static class CounterSbb extends CmpBackedSbb {
        public void writeCounter(int v) {
            try {
                Method setter = CounterSbb.class.getMethod("setCounter", int.class);
                cmpWrite(setter, Integer.valueOf(v));
            } catch (NoSuchMethodException e) { throw new RuntimeException(e); }
        }
        public int readCounter() {
            try {
                Method getter = CounterSbb.class.getMethod("getCounter");
                return ((Integer) cmpRead(getter)).intValue();
            } catch (NoSuchMethodException e) { throw new RuntimeException(e); }
        }
        public abstract void setCounter(int v);
        public abstract int getCounter();
    }

    private CounterSbb newConcreteCounter(int initial) {
        return new CounterSbb() {
            private int counter = initial;
            @Override public void setCounter(int v) { this.counter = v; }
            @Override public int getCounter() { return counter; }
        };
    }

    // ---- Lifecycle ----

    @Test
    public void registerBeforeStartThrowsISE() {
        MicroSleeContainer unstarted = new MicroSleeContainer();
        try {
            unstarted.registerSbb("too-early", new Sbb() { });
            fail("expected ISE");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("started"));
        }
    }

    @Test
    public void registerAfterStopThrowsISE() {
        container.stop();
        try {
            container.registerSbb("too-late", new Sbb() { });
            fail("expected ISE");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("started"));
        }
    }

    @Test
    public void stopIsIdempotent() {
        container.stop();
        container.stop();
    }

    @Test
    public void startIsIdempotent() {
        container.start();
        container.start();
    }

    @Test
    public void stopThenStartRoundTrip() throws Exception {
        container.stop();
        container.start();
        // Give the executor time to come back up
        Thread.sleep(50);
        SimpleSbbLocalObject lo = container.registerSbb("after-restart", new Sbb() { });
        assertNotNull(lo);
    }

    // ---- Accessors ----

    @Test
    public void getCmpFieldStoreReturnsSameInstance() {
        assertSame(container.getCmpFieldStore(), container.getCmpFieldStore());
    }

    @Test
    public void getSbbLifecycleManagerReturnsSameInstance() {
        assertSame(container.getSbbLifecycleManager(),
                container.getSbbLifecycleManager());
    }

    @Test
    public void getChildRelationFactoryReturnsNonNullUsableFactory() {
        // Use the supplier-based overload so anonymous inner classes are
        // supported.
        ChildRelationFactory f = container.getChildRelationFactory(
                childId -> new CounterSbb() {
                    private int counter;
                    @Override public void setCounter(int v) { this.counter = v; }
                    @Override public int getCounter() { return counter; }
                });
        assertNotNull(f);
        SimpleSbbLocalObject parent = container.registerSbb("parent-for-factory", new Sbb() { });
        SbbLocalObject child = f.createChild(parent.getSbbID().getId());
        assertNotNull(child);
        assertTrue(child.getSbbID().getId().startsWith(parent.getSbbID().getId()));
    }

    @Test
    public void createChildSbbUnknownParentThrows() {
        try {
            container.createChildSbb("does-not-exist", "child-x");
            fail("unknown parent must throw");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("Unknown parent"));
        }
    }

    @Test
    public void createChildSbbNullArgsThrow() {
        container.registerSbb("real-parent", new Sbb() { });
        try { container.createChildSbb(null, "c"); fail(); }
        catch (IllegalArgumentException expected) {}
        try { container.createChildSbb("real-parent", null); fail(); }
        catch (IllegalArgumentException expected) {}
    }

    // ---- registerSbb ----

    @Test
    public void registerSbbTwiceReturnsSameInstance() {
        Sbb sbb = new Sbb() { };
        SimpleSbbLocalObject a = container.registerSbb("dup", sbb);
        SimpleSbbLocalObject b = container.registerSbb("dup", sbb);
        assertSame(a, b);
    }

    @Test
    public void registerSbbCreatesDistinctLocalObjectsForDistinctIds() {
        SimpleSbbLocalObject a = container.registerSbb("sbb-A", new Sbb() { });
        SimpleSbbLocalObject b = container.registerSbb("sbb-B", new Sbb() { });
        assertNotSame(a, b);
        assertEquals("sbb-A", a.getSbbID().getId());
        assertEquals("sbb-B", b.getSbbID().getId());
    }

    @Test
    public void registerSbbRunsFullLifecycleOnTheEntityThread() throws Exception {
        final java.util.concurrent.atomic.AtomicBoolean ctxSet =
                new java.util.concurrent.atomic.AtomicBoolean(false);
        final java.util.concurrent.atomic.AtomicBoolean created =
                new java.util.concurrent.atomic.AtomicBoolean(false);
        final java.util.concurrent.atomic.AtomicBoolean activated =
                new java.util.concurrent.atomic.AtomicBoolean(false);
        Sbb sbb = new Sbb() {
            @Override public void setSbbContext(SbbContext ctx) { ctxSet.set(true); }
            @Override public void sbbCreate() { created.set(true); }
            @Override public void sbbActivate() { activated.set(true); }
        };
        container.registerSbb("lifecycle-sbb", sbb);
        long deadline = System.currentTimeMillis() + 1000;
        while (System.currentTimeMillis() < deadline
                && !(ctxSet.get() && created.get() && activated.get())) {
            Thread.sleep(10);
        }
        assertTrue("setSbbContext must be called", ctxSet.get());
        assertTrue("sbbCreate must be called", created.get());
        assertTrue("sbbActivate must be called", activated.get());
    }

    @Test
    public void registerSbbReturnsSimpleSbbLocalObjectWithEntityState() throws Exception {
        CounterSbb sbb = newConcreteCounter(0);
        SimpleSbbLocalObject lo = container.registerSbb("state-sbb", (Sbb) sbb);
        assertNotNull(lo.getEntityState());
        // Activation is asynchronous (entity virtual thread) — the entity may
        // or may not be READY immediately after registerSbb returns, so only
        // the eventual READY state is asserted (no racy intermediate check).
        assertTrue(lo.awaitReady(5, java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test
    public void removedEntityDetachesAndCleansCmpStore() throws Exception {
        CounterSbb sbb = newConcreteCounter(0);
        SimpleSbbLocalObject lo = container.registerSbb("cleanup-target", (Sbb) sbb);
        Thread.sleep(100);
        sbb.writeCounter(99);
        assertEquals(Integer.valueOf(99),
                ((InMemoryCmpFieldStore) container.getCmpFieldStore())
                        .load("cleanup-target").get("counter"));

        lo.remove();
        // After remove, the entity's CMP state must be dropped.
        assertFalse(((InMemoryCmpFieldStore) container.getCmpFieldStore())
                .contains("cleanup-target"));
        assertTrue(lo.isRemoved());
    }

    // ---- Child creation ----

    @Test
    public void childSbbCreatedByFactoryHasItsOwnEntity() throws Exception {
        SimpleSbbLocalObject parent = container.registerSbb("p-1", new Sbb() { });
        ChildRelationFactory factory = container.getChildRelationFactory(
                childId -> new Sbb() { });
        SbbLocalObject child = factory.createChild(parent.getSbbID().getId());
        assertNotNull(child);
        assertNotSame(parent.getSbbID(), child.getSbbID());
        Thread.sleep(100);
        assertNotNull(child.getSbbID().getId());
    }

    // ---- CmpFieldStoreLocator ----

    @Test
    public void cmpStoreLocatorBoundDuringContainerLifecycle() {
        assertNotNull(CmpFieldStoreLocator.get());
        assertSame(container.getCmpFieldStore(), CmpFieldStoreLocator.get());
    }

    @Test
    public void cmpStoreLocatorClearedAfterStop() {
        container.stop();
        assertNull(CmpFieldStoreLocator.get());
    }
}
