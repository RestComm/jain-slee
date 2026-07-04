package com.microjainslee.ra;

import com.microjainslee.api.ResourceAdaptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RA entity state machine per JAIN SLEE 1.1 §12.4.
 *
 * States:
 *   INACTIVE → (activate) → ACTIVE → (deactivate) → STOPPING → (stopComplete) → INACTIVE
 *
 * Callbacks:
 *   raActive()    — RA has become active, can fire events, start accepting traffic
 *   raStopping()  — RA should drain in-flight work, stop accepting new requests
 *   raInactive()  — RA fully stopped, release resources
 *
 * Khi STOPPING:
 *   - fireEvent() calls are rejected (IllegalStateException)
 *   - activityStarted() calls are rejected
 *   - RA drain in-flight, then call stopComplete()
 *
 * Khi INACTIVE:
 *   - ResourceAdaptorContext is null
 *   - No events can be fired
 *
 * Module: jainslee-ra-spi (update)
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
        this.ra = ra;
        this.raEntityName = raEntityName;
    }

    /**
     * INACTIVE → ACTIVE.
     * Gọi từ management (MBean, Quarkus startup, Spring Boot listener).
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
            LOG.infof("RA entity [%s] → ACTIVE", raEntityName);

            // Spec: call raActive() after state transition
            try {
                ra.raActive();
            } catch (Exception e) {
                // raActive() failed → rollback to INACTIVE
                state.set(State.INACTIVE);
                LOG.errorf(e, "raActive() failed for [%s] — reverted to INACTIVE", raEntityName);
                throw new RuntimeException("raActive() failed", e);
            }
        } finally {
            transitionLock.unlock();
        }
    }

    /**
     * ACTIVE → STOPPING.
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
            LOG.infof("RA entity [%s] → STOPPING", raEntityName);

            // Spec: call raStopping() after state transition
            // RA must call stopComplete() asynchronously when drained
            try {
                ra.raStopping();
            } catch (Exception e) {
                LOG.errorf(e, "raStopping() threw exception for [%s]", raEntityName);
                // Don't revert — state is STOPPING regardless
            }
        } finally {
            transitionLock.unlock();
        }
    }

    /**
     * STOPPING → INACTIVE.
     * Called by RA itself when it has drained all in-flight events.
     * Typically: ra.raStopping() starts async drain, then calls sleeEndpoint.stopComplete()
     * which triggers this method.
     */
    public void stopComplete() {
        transitionLock.lock();
        try {
            State current = state.get();
            if (current != State.STOPPING) {
                LOG.warnf("stopComplete() called on RA [%s] in state %s — ignoring",
                        raEntityName, current);
                return;
            }
            state.set(State.INACTIVE);
            LOG.infof("RA entity [%s] → INACTIVE (stop complete)", raEntityName);

            // Spec: call raInactive() after state transition
            try {
                ra.raInactive();
            } catch (Exception e) {
                LOG.errorf(e, "raInactive() threw exception for [%s]", raEntityName);
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
        // Only ACTIVE can fire events — STOPPING, INACTIVE both reject
        return state.get() == State.ACTIVE;
    }

    public void assertCanFireEvent() {
        State s = state.get();
        if (s != State.ACTIVE) {
            throw new IllegalStateException(
                    "RA [" + raEntityName + "] cannot fire events in state " + s +
                    " — only ACTIVE state allows fireEvent()");
        }
    }

    @Override
    public String toString() {
        return "RaEntity[" + raEntityName + ":" + state.get() + "]";
    }
}
