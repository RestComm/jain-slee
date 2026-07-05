/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.api;

/**
 * Bootstrap port handed to a Resource Adaptor during
 * {@link RaEndpointPort#activate(RaBootstrapPort)}.
 *
 * <p>
 * Provides the two primitives every RA needs to participate in the SLEE
 * event model:
 * <ul>
 *   <li>{@link #createActivityHandle(String)} — create an opaque handle
 *       that identifies a protocol activity (e.g. a SIP dialog, an SS7 TCAP
 *       dialogue).</li>
 *   <li>{@link #fireEvent(SleeEvent, ActivityHandle, Address)} — fire an
 *       event into the SLEE event router for the given activity and address.</li>
 * </ul>
 *
 * @see RaEndpointPort
 * @see ActivityHandle
 * @see SleeEvent
 */
public interface RaBootstrapPort {

    /**
     * Create an activity handle with the given opaque identifier.
     *
     * @param id a stable, unique identifier for the activity
     * @return a new activity handle
     */
    ActivityHandle createActivityHandle(String id);

    /**
     * Fire an event into the SLEE event router. The event is published
     * to the Disruptor ring buffer and routed to interested SBBs
     * asynchronously.
     *
     * @param event  the SLEE event to fire
     * @param handle the activity handle identifying the activity context
     * @param address the address for event routing (e.g. MSISDN, SIP URI)
     */
    void fireEvent(SleeEvent event, ActivityHandle handle, Address address);

    /**
     * End the activity identified by {@code handle}. The SLEE fires an
     * {@link ActivityEndedEvent} to the SBBs attached to the activity
     * context and then releases the context. RAs MUST call this when the
     * underlying protocol activity terminates (e.g. SIP dialog closed by
     * BYE, Diameter session ended by STR) — otherwise the activity context
     * and its attached SBB entities leak.
     *
     * <p>Default is a no-op for backward compatibility with bootstrap
     * implementations that predate activity-end propagation.
     *
     * @param handle the activity handle previously obtained from
     *               {@link #createActivityHandle(String)}
     */
    default void endActivity(ActivityHandle handle) {
    }
}
