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

/**
 * Per-thread registry for the logical transaction currently executing on
 * an activity context.
 *
 * <p>Uses a simple {@link ThreadLocal}; on virtual threads the value is
 * bound to the VT (not the carrier), so it is inherently VT-safe.
 *
 * <p>Callers should bracket transactional work with
 * {@link #runInTransaction(SbbTransactionContext, Runnable)} to ensure
 * proper cleanup.
 */
public final class ActivityContextTransactionRegistry {

    /** VT-safe current-transaction holder (bound to the VT, not carrier). */
    public static final ThreadLocal<SbbTransactionContext> CURRENT = new ThreadLocal<>();

    private ActivityContextTransactionRegistry() {
    }

    /**
     * Create and begin a transaction context. It is <b>not</b> bound to the
     * current thread — the caller MUST bracket the dispatch with
     * {@link #runInTransaction(SbbTransactionContext, Runnable)}, which binds
     * it on entry and restores the previous binding on exit. Binding here as
     * well would make {@code runInTransaction} capture this very context as its
     * {@code previous} value and restore it after the body, leaking it onto the
     * (pooled / reused) thread and tripping a spurious
     * {@code "Nested transaction mismatch"} on the next dispatch.
     */
    public static SbbTransactionContext begin(InMemoryActivityContext activityContext,
            SleeTimerSchedulerBridge timerBridge) {
        SbbTransactionContext context = new SbbTransactionContext(activityContext, timerBridge);
        context.begin();
        return context;
    }

    /** Read the current transaction from the thread-local, or {@code null}. */
    public static SbbTransactionContext current() {
        return CURRENT.get();
    }

    public static SbbTransactionContext currentFor(InMemoryActivityContext activityContext) {
        SbbTransactionContext context = CURRENT.get();
        if (context != null && context.isActive()
                && context.getActivityContext() == activityContext) {
            return context;
        }
        return null;
    }

    /** Bind {@code context} to the current thread. */
    public static void install(SbbTransactionContext context) {
        SbbTransactionContext existing = CURRENT.get();
        if (existing != null && existing != context) {
            throw new IllegalStateException(
                    "Nested transaction mismatch: existing="
                            + existing + " requested=" + context);
        }
        CURRENT.set(context);
    }

    /**
     * Convenience: run {@code body} with {@code context} bound to the
     * thread, then clear it in a finally block.
     */
    public static void runInTransaction(SbbTransactionContext context, Runnable body) {
        SbbTransactionContext previous = CURRENT.get();
        CURRENT.set(context);
        try {
            body.run();
        } finally {
            if (previous != null) {
                CURRENT.set(previous);
            } else {
                CURRENT.remove();
            }
        }
    }

    /** Clear the transaction binding — call from a finally block. */
    public static void clear(SbbTransactionContext context) {
        if (CURRENT.get() == context) {
            CURRENT.remove();
        }
    }
}
