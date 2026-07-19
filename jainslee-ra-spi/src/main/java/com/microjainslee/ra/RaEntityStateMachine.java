/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra;

import com.microjainslee.api.ResourceAdaptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Perfect Core S5 — RA entity state machine per JAIN SLEE 1.1 §12.4.
 *
 * States:
 *   INACTIVE -> (activate) -> ACTIVE -> (deactivate) -> STOPPING -> (stopComplete) -> INACTIVE
 *
 * Callbacks:
 *   raActive()    - RA has become active, can fire events, start accepting traffic
 *   raStopping()  - RA should drain in-flight work, stop accepting new requests
 *   raInactive()  - RA fully stopped, release resources
 *
 * When STOPPING:
 *   - fireEvent() calls are rejected (IllegalStateException)
 *   - activityStarted() calls are rejected
 *   - RA drains in-flight, then calls stopComplete()
 *
 * When INACTIVE:
 *   - ResourceAdaptorContext is null
 *   - No events can be fired
 *
 * Module: jainslee-ra-spi (S5 wiring)
 */
public class RaEntityStateMachine {

    private static final Logger LOG = LogManager.getLogger(RaEntityStateMachine.class);

    public enum State {
        INACTIVE,
        ACTIVE,
        STOPPING
    }

    private final AtomicReference<State> state = new AtomicReference<>(State.INACTIVE);
    private final ReentrantLock transitionLock = new ReentrantLock();
    private final ResourceAdaptor ra;
    private final String raEntityName;

    public RaEntityStateMachine(ResourceAdaptor ra, String raEntityName) {
        if (ra == null) {
            throw new IllegalArgumentException("ra is required");
        }
        if (raEntityName == null || raEntityName.isEmpty()) {
            throw new IllegalArgumentException("raEntityName is required");
        }
        this.ra = ra;
        this.raEntityName = raEntityName;
    }

    /**
     * INACTIVE -> ACTIVE.
     * Called from management (Quarkus startup, Spring Boot listener, admin REST).
     */
    public void activate() {
        transitionLock.lock();
        try {
            State current = state.get();
            if (current != State.INACTIVE) {
                throw new IllegalStateException(
                        "Cannot activate RA [" + raEntityName + "] from state " + current);
            }
            state.set(State.ACTIVE);
            LOG.info("RA entity [{}] -> ACTIVE", raEntityName);

            // Spec: call raActive() after state transition
            try {
                ra.raActive();
            } catch (Exception e) {
                // raActive() failed -> rollback to INACTIVE
                state.set(State.INACTIVE);
                LOG.error("raActive() failed for [{}] -- reverted to INACTIVE: {}",
                        raEntityName, e.getMessage(), e);
                throw new RuntimeException("raActive() failed", e);
            }
        } finally {
            transitionLock.unlock();
        }
    }

    /**
     * ACTIVE -> STOPPING.
     * Spec: RA receives raStopping() and must drain then call stopComplete().
     */
    public void deactivate() {
        transitionLock.lock();
        try {
            State current = state.get();
            if (current != State.ACTIVE) {
                throw new IllegalStateException(
                        "Cannot deactivate RA [" + raEntityName + "] from state " + current);
            }
            state.set(State.STOPPING);
            LOG.info("RA entity [{}] -> STOPPING", raEntityName);

            // Spec: call raStopping() after state transition
            // RA must call stopComplete() asynchronously when drained
            try {
                ra.raStopping();
            } catch (Exception e) {
                LOG.error("raStopping() threw exception for [{}]: {}",
                        raEntityName, e.getMessage(), e);
                // Don't revert - state is STOPPING regardless
            }
        } finally {
            transitionLock.unlock();
        }
    }

    /**
     * STOPPING -> INACTIVE.
     * Called by RA itself when it has drained all in-flight events.
     * Typically: ra.raStopping() starts async drain, then calls sleeEndpoint.stopComplete()
     * which triggers this method.
     */
    public void stopComplete() {
        transitionLock.lock();
        try {
            State current = state.get();
            if (current != State.STOPPING) {
                LOG.warn("stopComplete() called on RA [{}] in state {} -- ignoring",
                        raEntityName, current);
                return;
            }
            state.set(State.INACTIVE);
            LOG.info("RA entity [{}] -> INACTIVE (stop complete)", raEntityName);

            // Spec: call raInactive() after state transition
            try {
                ra.raInactive();
            } catch (Exception e) {
                LOG.error("raInactive() threw exception for [{}]: {}",
                        raEntityName, e.getMessage(), e);
            }
        } finally {
            transitionLock.unlock();
        }
    }

    /** @return current state (for fireEvent() validation in SleeEndpoint) */
    public State getState() {
        return state.get();
    }

    public boolean isActive() {
        return state.get() == State.ACTIVE;
    }

    public boolean canFireEvents() {
        // Only ACTIVE can fire events - STOPPING, INACTIVE both reject
        return state.get() == State.ACTIVE;
    }

    public void assertCanFireEvent() {
        State s = state.get();
        if (s != State.ACTIVE) {
            throw new IllegalStateException(
                    "RA [" + raEntityName + "] cannot fire events in state " + s +
                    " -- only ACTIVE state allows fireEvent()");
        }
    }

    /** Generic guarded transition - only valid pairs are accepted. */
    public void transition(State from, State to) {
        transitionLock.lock();
        try {
            State current = state.get();
            if (current != from) {
                throw new IllegalStateException(
                        "Cannot transition RA [" + raEntityName + "] from " + current +
                        " (expected " + from + ")");
            }
            boolean valid = switch (from) {
                case INACTIVE -> to == State.ACTIVE;
                case ACTIVE   -> to == State.STOPPING;
                case STOPPING -> to == State.INACTIVE;
            };
            if (!valid) {
                throw new IllegalStateException(
                        "Invalid transition for RA [" + raEntityName + "]: " + from + " -> " + to);
            }
            state.set(to);
            LOG.info("RA entity [{}] -> {}", raEntityName, to);
        } finally {
            transitionLock.unlock();
        }
    }

    public String getRaEntityName() {
        return raEntityName;
    }

    @Override
    public String toString() {
        return "RaEntity[" + raEntityName + ":" + state.get() + "]";
    }
}
