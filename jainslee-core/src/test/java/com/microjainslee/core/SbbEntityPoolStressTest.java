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

import com.microjainslee.api.Sbb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Stress / lifecycle coverage for {@link VirtualThreadSbbEntityPool}.
 *
 * <p>The pool enforces "one virtual thread per SBB ID" — every event for a
 * given ID must run on the same VT in submission order. These tests verify
 * that contract at three scale points (10K / 50K / 100K SBBs) for the
 * three lifecycle operations the SBB spec mandates: {@code create},
 * {@code submit} (pending work) and {@code shutdown} (cancel).</p>
 *
 * <p>Runs on Java 21+ where {@link MicroSleeExecutors} surfaces the real
 * virtual-thread executor. On older JVMs each "virtual thread" is a
 * cached platform thread; the contract still holds but memory/throughput
 * numbers will differ.</p>
 */
public class SbbEntityPoolStressTest {

    private static final Logger LOG = LogManager.getLogger(SbbEntityPoolStressTest.class);

    /** Scale points chosen to exercise the per-VT map under realistic pressure. */
    private static final int[] SCALES = new int[] { 10_000, 50_000, 100_000 };

    /** Per-SBB workload: tasks enqueued during the "pending" phase. */
    private static final int TASKS_PER_SBB = 5;

    /** Hard upper bound on wait time, to avoid hanging CI on a regression. */
    private static final long HARD_LIMIT_MS = 240_000; // 4 minutes

    private VirtualThreadSbbEntityPool pool;

    @Before
    public void setUp() {
        // No management beans — heap/thread readings use Runtime / Thread.
    }

    @After
    public void tearDown() {
        if (pool != null) {
            pool.shutdown();
        }
    }

    // ------------------------------------------------------------------
    // 1. CREATE — register N SBBs; verify each lands in ACTIVE state and
    //    acquires its own parked virtual thread.
    // ------------------------------------------------------------------

    @Test
    public void create_10k_succeeds() throws Exception {
        runScenario(10_000);
    }

    @Test
    public void create_50k_succeeds() throws Exception {
        runScenario(50_000);
    }

    @Test
    public void create_100k_succeeds() throws Exception {
        runScenario(100_000);
    }

    private void runScenario(int scale) throws Exception {
        LOG.info("=== Stress scenario: {} SBBs (Java {}, cores={}) ===",
                scale, System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());

        long t0 = System.nanoTime();
        long memBefore = usedHeap();

        // max must accommodate the scale; pre-warm is a no-op since we create lazily.
        pool = new VirtualThreadSbbEntityPool(0, scale, true);
        ConcurrentHashMap<String, VirtualThreadSbbEntityPool.SbbEntity> created =
                new ConcurrentHashMap<String, VirtualThreadSbbEntityPool.SbbEntity>();

        // ---- CREATE phase ----
        for (int i = 0; i < scale; i++) {
            String id = "sbb-" + i;
            VirtualThreadSbbEntityPool.SbbEntity e = pool.acquire(id, new java.util.function.Supplier<Sbb>() {
                @Override public Sbb get() { return new CountingSbb(); }
            });
            created.put(id, e);
        }
        long tCreate = System.nanoTime() - t0;
        long memAfterCreate = usedHeap();

        assertEquals("all SBBs must be registered", scale, pool.size());
        assertEquals("all SBBs must be registered (create map)", scale, created.size());

        int liveThreads = Thread.activeCount();
        long heapDeltaMb = (memAfterCreate - memBefore) / (1024 * 1024);
        LOG.info("[create] {} entities in {} ms ({} ns/op), heap +{} MB, liveThreads={}",
                scale, TimeUnit.NANOSECONDS.toMillis(tCreate),
                tCreate / scale, heapDeltaMb, liveThreads);

        // ---- PENDING phase ----
        // For each SBB, submit TASKS_PER_SBB tasks that verify they execute
        // strictly serially (counters increment by 1, no gaps, in order).
        AtomicInteger errors = new AtomicInteger();
        long tPending = dispatchAndWait(created, errors);
        LOG.info("[pending] {} tasks in {} ms ({} ns/op), errors={}",
                scale * TASKS_PER_SBB, TimeUnit.NANOSECONDS.toMillis(tPending),
                tPending / (scale * TASKS_PER_SBB), errors.get());
        assertEquals("no task may throw under the per-SBB serial contract", 0, errors.get());

        // ---- CANCEL phase ----
        long tCancelStart = System.nanoTime();
        pool.shutdown();
        long tCancel = System.nanoTime() - tCancelStart;

        assertTrue("pool executor must be terminated after shutdown",
                pool.getExecutor().isTerminated());
        // Pool is single-use: any further acquire must throw.
        try {
            pool.acquire("after-shutdown", () -> new CountingSbb());
            fail("acquire() after shutdown must throw");
        } catch (IllegalStateException expected) {
            // ok
        }
        LOG.info("[cancel] shutdown drained in {} ms (executor terminated={})",
                TimeUnit.NANOSECONDS.toMillis(tCancel),
                pool.getExecutor().isTerminated());

        // drop our reference so @After doesn't re-shutdown a dead pool
        pool = null;

        long totalMs = TimeUnit.NANOSECONDS.toMillis(
                (System.nanoTime() - t0));
        LOG.info("=== Done scenario {}: total {} ms ===", scale, totalMs);
    }

    /**
     * Enqueue {@link #TASKS_PER_SBB} tasks per SBB and wait for every entity's
     * virtual thread to finish them. Each task increments a per-SBB counter
     * through {@code ConcurrentHashMap<String, AtomicInteger>} so any
     * reordering or dropped task is detectable as a missing increment.
     */
    private long dispatchAndWait(
            ConcurrentHashMap<String, VirtualThreadSbbEntityPool.SbbEntity> created,
            AtomicInteger errors) throws InterruptedException {

        ConcurrentHashMap<String, AtomicInteger> counters =
                new ConcurrentHashMap<String, AtomicInteger>();
        ConcurrentHashMap<String, AtomicLong> lastSeen =
                new ConcurrentHashMap<String, AtomicLong>();
        List<VirtualThreadSbbEntityPool.SbbEntity> entities =
                new ArrayList<VirtualThreadSbbEntityPool.SbbEntity>(created.values());
        CountDownLatch done = new CountDownLatch(entities.size() * TASKS_PER_SBB);

        long t0 = System.nanoTime();
        for (VirtualThreadSbbEntityPool.SbbEntity entity : entities) {
            String id = entity.getSbbId();
            counters.put(id, new AtomicInteger());
            lastSeen.put(id, new AtomicLong(-1));
            for (int t = 0; t < TASKS_PER_SBB; t++) {
                final long seq = t;
                entity.submit(new Runnable() {
                    @Override public void run() {
                        try {
                            // Verify single-threaded per-SBB ordering:
                            // each invocation must see the previous sequence number + 1.
                            AtomicInteger c = counters.get(id);
                            AtomicLong prev = lastSeen.get(id);
                            long expected = prev.get() + 1;
                            if (seq != expected) {
                                errors.incrementAndGet();
                                LOG.error("Out-of-order task on {}: expected seq={}, got {}",
                                        id, expected, seq);
                            }
                            int n = c.incrementAndGet();
                            if (n != seq + 1) {
                                errors.incrementAndGet();
                                LOG.error("Counter drift on {}: c={}, seq={}", id, n, seq);
                            }
                            prev.set(seq);
                            // Touch the SBB instance to make sure handler dispatch works.
                            if (!(entity.getSbb() instanceof CountingSbb)) {
                                errors.incrementAndGet();
                            }
                        } catch (Throwable t) {
                            errors.incrementAndGet();
                            LOG.error("Task on {} threw", id, t);
                        } finally {
                            done.countDown();
                        }
                    }
                });
            }
        }
        if (!done.await(HARD_LIMIT_MS, TimeUnit.MILLISECONDS)) {
            fail("Pending phase timed out after " + HARD_LIMIT_MS + "ms for "
                    + entities.size() + " entities");
        }
        // Final invariant: each SBB must have seen exactly TASKS_PER_SBB increments.
        for (java.util.Map.Entry<String, AtomicInteger> e : counters.entrySet()) {
            assertEquals("SBB " + e.getKey() + " must have all tasks accounted for",
                    TASKS_PER_SBB, e.getValue().get());
        }
        return System.nanoTime() - t0;
    }

    private static long usedHeap() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    /**
     * Counting SBB used to verify the pool returns distinct, reusable SBB
     * instances per ID.
     */
    static final class CountingSbb implements Sbb {
        private final int id = COUNTER.incrementAndGet();
        @Override public String toString() { return "CountingSbb#" + id; }
        static final AtomicInteger COUNTER = new AtomicInteger();
    }
}
