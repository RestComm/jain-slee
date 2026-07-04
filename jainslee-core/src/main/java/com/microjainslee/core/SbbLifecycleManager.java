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

import com.microjainslee.api.CreateException;
import com.microjainslee.api.RolledBackContext;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * JAIN-SLEE 1.1 §6.3 — SBB lifecycle state machine.
 * <p>
 * Implements the spec-mandated transitions
 * {@code DOES_NOT_EXIST -> POOLED -> READY -> POOLED -> DOES_NOT_EXIST}
 * plus the {@code sbbPostCreate}, {@code sbbRolledBack},
 * {@code unsetSbbContext} callbacks introduced in §6.2/§6.10.
 * <p>
 * A {@link WeakHashMap} keyed by {@link Sbb} lets one manager drive many
 * SBBs without the caller threading a state token through every call.
 *
 * @author Tran Nhan (nhanth87)
 */
public class SbbLifecycleManager {

    /** Spec §6.3 — observable entity states. */
    public enum State { DOES_NOT_EXIST, POOLED, READY }

    private static final Logger LOG = LogManager.getLogger(SbbLifecycleManager.class);

    // §VT-PINNING: The states map uses Collections.synchronizedMap (synchronized
    // internally) but is only accessed during lifecycle transitions
    // (create/activate/passivate/remove), NOT on the SBB event-delivery hot
    // path. Safe for virtual threads.
    private final Map<Sbb, State> states =
            Collections.synchronizedMap(new WeakHashMap<Sbb, State>());

    public SbbLifecycleManager() {
    }

    // ---- Backward-compatible thin API (deprecated) ----------------------

    /** @deprecated prefer {@link #create(Sbb, SbbContext, Map)}. */
    @Deprecated
    public void create(Sbb sbb, SbbContext context) {
        try {
            create(sbb, context, null);
        } catch (CreateException e) {
            throw new RuntimeException(e);
        }
    }

    /** @deprecated prefer {@link #activate(Sbb, Map)}. */
    @Deprecated
    public void activate(Sbb sbb) {
        activate(sbb, null);
    }

    /** @deprecated prefer {@link #passivate(Sbb, Map)}. */
    @Deprecated
    public void passivate(Sbb sbb) {
        passivate(sbb, null);
    }

    /** @deprecated prefer {@link #removeEntity(Sbb)}. */
    @Deprecated
    public void remove(Sbb sbb) {
        removeEntity(sbb);
    }

    // ---- New state-machine-aware API ------------------------------------

    /** §6.3 — DOES_NOT_EXIST -> POOLED; invokes setSbbContext then sbbCreate. */
    public void create(Sbb sbb, SbbContext ctx, Map<String, Object> unused)
            throws CreateException {
        if (sbb == null) {
            throw new IllegalArgumentException("sbb is required");
        }
        if (ctx != null) {
            sbb.setSbbContext(ctx);
        }
        sbb.sbbCreate();
        transition(sbb, State.POOLED);
    }

    /** §6.3.4 — invoke {@code sbbPostCreate}. */
    public void postCreate(Sbb sbb) throws CreateException {
        if (sbb == null) {
            return;
        }
        sbb.sbbPostCreate();
    }

    /**
     * §6.2 — POOLED -> READY. Calls {@code sbbLoad} (if a CMP map is supplied)
     * then {@code sbbActivate}.
     */
    public void activate(Sbb sbb, Map<String, Object> cmpState) {
        if (sbb == null) {
            return;
        }
        if (cmpState != null) {
            try {
                sbb.sbbLoad();
            } catch (RuntimeException e) {
                LOG.warn("sbbLoad threw — continuing activate: {}", e.getMessage());
            }
        }
        sbb.sbbActivate();
        transition(sbb, State.READY);
    }

    /**
     * §6.2 — READY -> POOLED. Calls {@code sbbPassivate} then
     * {@code sbbStore} (if a CMP map is supplied).
     */
    public void passivate(Sbb sbb, Map<String, Object> cmpStateOut) {
        if (sbb == null) {
            return;
        }
        sbb.sbbPassivate();
        if (cmpStateOut != null) {
            try {
                sbb.sbbStore();
            } catch (RuntimeException e) {
                LOG.warn("sbbStore threw during passivate: {}", e.getMessage());
            }
        }
        transition(sbb, State.POOLED);
    }

    /** §6.2 — * -> DOES_NOT_EXIST. Calls {@code sbbRemove} then {@code unsetSbbContext}. */
    public void removeEntity(Sbb sbb) {
        if (sbb == null) {
            return;
        }
        try {
            sbb.sbbRemove();
        } finally {
            try {
                sbb.unsetSbbContext();
            } catch (RuntimeException e) {
                LOG.warn("unsetSbbContext threw — continuing remove: {}", e.getMessage());
            }
            transition(sbb, State.DOES_NOT_EXIST);
        }
    }

    /** §6.10.1 — invoke {@code sbbRolledBack} on the SBB. */
    public void rolledBack(Sbb sbb, RolledBackContext ctx) {
        if (sbb == null) {
            return;
        }
        try {
            sbb.sbbRolledBack(ctx);
        } catch (RuntimeException e) {
            LOG.warn("sbbRolledBack threw: {}", e.getMessage());
        }
    }

    /** Read the current state (defaults to {@code DOES_NOT_EXIST}). */
    public State getState(Sbb sbb) {
        if (sbb == null) {
            return State.DOES_NOT_EXIST;
        }
        State s = states.get(sbb);
        return s != null ? s : State.DOES_NOT_EXIST;
    }

    /** Direct state setter (tests + diagnostics). */
    public void setState(Sbb sbb, State state) {
        transition(sbb, state);
    }

    private void transition(Sbb sbb, State next) {
        states.put(sbb, next);
    }
}