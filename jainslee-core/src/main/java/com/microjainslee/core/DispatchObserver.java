/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.core;

/**
 * Passive observation hook for SBB event delivery — the seam that lets an
 * observability layer (jainslee-telemetry) watch the {@link EventRouter}
 * without the core owing it a dependency.
 *
 * <p>The router invokes the observer <b>after</b> each {@code onEvent}
 * delivery (success or failure), on whichever thread performed the delivery
 * (inline caller or the entity's virtual thread). Contract for
 * implementations:</p>
 * <ul>
 *   <li><b>Fast</b> — a few counter increments; never block, never do I/O.</li>
 *   <li><b>Never throw</b> — the router additionally shields itself, but a
 *       throwing observer is a bug.</li>
 *   <li><b>Thread-safe</b> — deliveries happen concurrently across entities.</li>
 * </ul>
 *
 * <p>When no observer is registered the cost is one volatile read per
 * delivery — nothing else.</p>
 */
public interface DispatchObserver {

    /**
     * One {@code onEvent} completed normally.
     *
     * @param sbbType   simple class name of the SBB implementation
     * @param entityId  the SBB entity id the event was delivered to
     * @param latencyNs wall time spent inside {@code onEvent}, nanoseconds
     */
    void onEventProcessed(String sbbType, String entityId, long latencyNs);

    /**
     * One {@code onEvent} threw.
     *
     * @param sbbType  simple class name of the SBB implementation
     * @param entityId the SBB entity id the event was delivered to
     * @param error    what it threw
     */
    void onDispatchError(String sbbType, String entityId, Throwable error);
}
