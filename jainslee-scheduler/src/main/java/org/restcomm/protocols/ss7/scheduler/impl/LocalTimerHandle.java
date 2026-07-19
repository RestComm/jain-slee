/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Vendored from jSS7 scheduler (RestComm/jss7 9.5.0).
 * Original package: org.restcomm.protocols.ss7.scheduler
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */


package org.restcomm.protocols.ss7.scheduler.impl;

import org.restcomm.protocols.ss7.scheduler.api.TimerHandle;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Timer handle with CAS-based state transitions for cancel and fire.
 */
public class LocalTimerHandle implements TimerHandle {

    private static final int STATE_PENDING = 0;
    private static final int STATE_CANCELLED = 1;
    private static final int STATE_FIRED = 2;

    private final AtomicInteger state = new AtomicInteger(STATE_PENDING);
    private volatile Runnable cancelAction;

    void setCancelAction(Runnable cancelAction) {
        this.cancelAction = cancelAction;
    }

    @Override
    public void cancel() {
        if (state.compareAndSet(STATE_PENDING, STATE_CANCELLED)) {
            Runnable current = cancelAction;
            if (current != null) {
                current.run();
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return state.get() == STATE_CANCELLED;
    }

    @Override
    public boolean hasFired() {
        return state.get() == STATE_FIRED;
    }

    boolean markFired() {
        return state.compareAndSet(STATE_PENDING, STATE_FIRED);
    }
}
