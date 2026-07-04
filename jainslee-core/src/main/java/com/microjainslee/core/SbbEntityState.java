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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-SBB entity runtime state tracked by the container:
 * the current CMP fields, the registered child relations, the
 * lifecycle state machine pointer, and the {@link EventMask} of
 * event types this entity is willing to receive.
 *
 * <p>This is the in-memory shadow of what the spec calls an "SBB entity";
 * the {@link Sbb} instance itself is intentionally a POJO and does not
 * carry this state — the container keeps them synchronized via
 * {@link SbbLifecycleManager} (which drives
 * {@link Sbb#sbbLoad() sbbLoad} /
 * {@link Sbb#sbbStore() sbbStore}) and {@link CmpAccessorInvoker}
 * (which reads/writes individual fields).
 *
 * <h2>Event mask (§8.6)</h2>
 * Each entity carries an {@link #acceptedEventTypes} view of its
 * {@link EventMask}. {@link EventRouter} consults this set before
 * invoking {@code onEvent}, so SBBs are not woken up for events they
 * do not care about. The mask defaults to {@link EventMask#ACCEPT_ALL}
 * for backwards compatibility with SBBs that have not declared a
 * filter.
 *
 * @author Tran Nhan (nhanth87)
 */
public final class SbbEntityState {

    private final Map<String, Object> cmpFields = new ConcurrentHashMap<String, Object>();
    private final List<ChildRelationImpl> childRelations =
            Collections.synchronizedList(new ArrayList<ChildRelationImpl>());
    private volatile SbbLifecycleManager.State lifecycleState =
            SbbLifecycleManager.State.POOLED;
    private volatile boolean removed;
    /**
     * JAIN-SLEE 1.1 §8.6 — set of event types this entity accepts.
     * Backed by the {@link EventMask} supplied at registration time.
     * Never {@code null}; defaults to {@link EventMask#ACCEPT_ALL}
     * when the SBB did not declare a filter.
     */
    private volatile EventMask eventMask = EventMask.ACCEPT_ALL;

    public Map<String, Object> getCmpFields() {
        return cmpFields;
    }

    // §VT-PINNING: synchronized on childRelations is held only for a
    // defensive copy — microseconds, no I/O, no callbacks. Not on the
    // SBB hot path; safe for virtual threads.
    public List<ChildRelationImpl> getChildRelations() {
        synchronized (childRelations) {
            return new ArrayList<ChildRelationImpl>(childRelations);
        }
    }

    public void registerChildRelation(ChildRelationImpl relation) {
        if (relation == null) {
            throw new IllegalArgumentException("relation is required");
        }
        childRelations.add(relation);
    }

    public SbbLifecycleManager.State getLifecycleState() {
        return lifecycleState;
    }

    /**
     * Atomic state transition. Volatile write is enough for the read visibility
     * guarantees the JAIN SLEE 1.1 spec requires (single-state-at-a-time
     * observation across the SBB entity thread boundary), but we synchronize
     * to be defensive against concurrent transitions triggered by, e.g.,
     * cascading removal racing with normal passivation.
     *
     * <p>§VT-PINNING: this synchronized method is only called during
     * lifecycle transitions (activate/passivate/remove), NOT on the
     * SBB event-delivery hot path. Safe for virtual threads.</p>
     */
    public synchronized void transitionTo(SbbLifecycleManager.State next) {
        this.lifecycleState = next;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void markRemoved() {
        this.removed = true;
    }

    /**
     * JAIN-SLEE 1.1 §8.6 — set the {@link EventMask} this entity uses
     * to filter inbound events. Pass {@code null} to revert to
     * {@link EventMask#ACCEPT_ALL}.
     *
     * <p>Called once at registration time by
     * {@link MicroSleeContainer#registerSbb(String, com.microjainslee.api.Sbb, EventMask)};
     * re-assigning at runtime is safe but unusual.
     */
    public void setEventMask(EventMask mask) {
        this.eventMask = mask != null ? mask : EventMask.ACCEPT_ALL;
    }

    /**
     * @return the {@link EventMask} currently in effect; never {@code null}.
     */
    public EventMask getEventMask() {
        return eventMask;
    }

    /**
     * JAIN-SLEE 1.1 §8.6 — read-only view of the accepted event types.
     * Equivalent to {@code getEventMask().rawAccepted()} but tolerates
     * {@link EventMask#ACCEPT_ALL} (where {@code rawAccepted()} returns
     * {@code null}).
     *
     * @return the underlying allow-list, or {@code null} when the mask
     *         accepts every event.
     */
    public Set<Class<?>> getAcceptedEventTypes() {
        EventMask m = eventMask;
        return m == null ? null : m.rawAccepted();
    }
}