/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.sbbs;

import com.example.ussddemo.quarkus.events.GrpcMenuResponseEvent;
import com.example.ussddemo.quarkus.events.Ss7UssdBeginEvent;
import com.example.ussddemo.quarkus.events.UssdCompleteEvent;
import com.example.ussddemo.quarkus.service.UssdSbbWiring;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.CmpField;
import com.microjainslee.api.annotations.InitialEventSelect;
import com.microjainslee.api.annotations.SbbAnnotation;
import com.microjainslee.core.CmpBackedSbb;
import com.microjainslee.core.ies.InitialEventSelectCondition;
import com.microjainslee.core.ies.InitialEventSelectResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Internal MAP/USSD service leg — CMP fields + gRPC RA delegate.
 *
 * <p>Updated for Perfect Core S1-S5 (2026-06-28):</p>
 * <ul>
 *   <li><b>S2</b> — CMP accessors declared as {@code abstract} so the
 *       Javassist concrete-SBB generator can produce the backing getters
 *       and setters that delegate to {@link CmpBackedSbb#cmpRead(Method)} /
 *       {@link CmpBackedSbb#cmpWrite(Method, Object)}. Concrete helpers
 *       ({@code cmpRead}/{@code cmpWrite}) keep the call sites type-safe.</li>
 *   <li><b>S3</b> — {@link InitialEventSelect @InitialEventSelect} method
 *       routes every USSD session to the same entity by msisdn+dialogId.
 *       The {@code Ss7UssdBegin} event is the only initial event;
 *       subsequent {@code GrpcMenuResponse} events for the same
 *       convergence key are delivered to the same SBB instance so CMP
 *       state survives.</li>
 *   <li><b>P1</b> — safe under {@code txEnabled=false} because no
 *       transactional context is acquired; CMP writes go straight to the
 *       in-memory store through {@code cmpWrite}.</li>
 * </ul>
 */
@SbbAnnotation(name = "Ss7UssdIngress", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public abstract class Ss7UssdIngressSbb extends CmpBackedSbb implements SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(Ss7UssdIngressSbb.class);

    private UssdSbbWiring wiring;

    /**
     * Concrete helper constructor — used by the SBB factory that the
     * Quarkus bootstrap registers via {@code registerSbbType(...)}.
     */
    public Ss7UssdIngressSbb(UssdSbbWiring wiring) {
        this.wiring = wiring;
    }

    /**
     * No-arg constructor — required by the {@link InitialEventSelect}
     * dispatcher so it can spawn a TEMP SBB instance to evaluate IES
     * without touching the pooled entity.
     */
    public Ss7UssdIngressSbb() {
        this.wiring = null;
    }

    // ─────────────────────────────────────────────────────────────────
    //  CMP accessors — abstract on purpose (Perfect Core S2).
    //  The Javassist concrete generator produces the field + getter/setter
    //  bodies; user code only ever calls the abstract method through the
    //  reflective cmpRead/cmpWrite bridge in CmpBackedSbb.
    // ─────────────────────────────────────────────────────────────────

    @CmpField("sessionId")
    public abstract String getSessionId();

    @CmpField("sessionId")
    public abstract void setSessionId(String sessionId);

    @CmpField("msisdn")
    public abstract String getMsisdn();

    @CmpField("msisdn")
    public abstract void setMsisdn(String msisdn);

    @CmpField("menuTier")
    public abstract int getMenuTier();

    @CmpField("menuTier")
    public abstract void setMenuTier(int menuTier);

    /**
     * Optional dialog correlation id used by {@link #selectInitialEvent}
     * to distinguish concurrent USSD sessions for the same subscriber.
     */
    @CmpField("dialogId")
    public abstract String getDialogId();

    @CmpField("dialogId")
    public abstract void setDialogId(String dialogId);

    // ─────────────────────────────────────────────────────────────────
    //  Initial Event Selection (Perfect Core S3).
    //  Runs on a TEMP instance — must be free of side effects on CMP state.
    // ─────────────────────────────────────────────────────────────────

    /**
     * IES — convergence name = {@code msisdn + ":" + dialogId}. The first
     * {@link Ss7UssdBeginEvent} for a given (msisdn, dialogId) tuple is
     * the initial event; subsequent events are routed to the same entity
     * per spec §7.5.5.
     *
     * <p>The current {@link Ss7UssdBeginEvent} does not yet carry a
     * dialogId field, so convergence collapses to msisdn-only. When a
     * dialog id is added (see {@code getDialogId()}) the rule will
     * automatically use the longer key without further SBB edits.</p>
     */
    @InitialEventSelect(name = "ussd-session-convergence")
    public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
        Object event = c.getEvent();
        if (event instanceof Ss7UssdBeginEvent) {
            Ss7UssdBeginEvent e = (Ss7UssdBeginEvent) event;
            String msisdn = e.getMsisdn() == null ? "anon" : e.getMsisdn();
            // Ss7UssdBeginEvent has no dialogId yet — use the msisdn-only
            // convergence key so all subsequent events for the same
            // subscriber route to the same entity. The CMP accessor
            // pair (getDialogId/setDialogId) is declared above so a
            // future event rev that exposes dialogId can switch this
            // to "msisdn:dialogId" without re-compiling the kernel.
            return InitialEventSelectResult.forSession(msisdn, true);
        }
        // GrpcMenuResponse etc. — non-initial by definition (only valid
        // once an Ss7UssdBegin has allocated the entity).
        return InitialEventSelectResult.builder()
                .convergenceName(null)
                .initialEvent(false)
                .build();
    }

    @Override public void sbbCreate() { LOG.debug("Ss7UssdIngressSbb created"); }
    @Override public void sbbActivate() { LOG.debug("Ss7UssdIngressSbb activated"); }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof Ss7UssdBeginEvent) {
            onSs7Begin((Ss7UssdBeginEvent) event, aci);
        } else if (event instanceof GrpcMenuResponseEvent) {
            onGrpcResponse((GrpcMenuResponseEvent) event, aci);
        }
    }

    private void onSs7Begin(Ss7UssdBeginEvent event, ActivityContextInterface aci) {
        cmpWrite(method("setSessionId", String.class), event.getSessionId());
        cmpWrite(method("setMsisdn", String.class), event.getMsisdn());
        cmpWrite(method("setMenuTier", int.class), event.getMenuTier());
        LOG.infof("[SS7-ingress] MAP begin session=%s msisdn=%s tier=%d cmpTier=%d",
                event.getSessionId(), event.getMsisdn(), event.getMenuTier(), getMenuTier());
        // Pass `aci` as the 4th arg so the gRPC response is routed back to
        // the SS7-ingress SBB instead of staying on the gRPC-client's
        // request ACI. Matches the canonical `ras/ra-grpc-client` 4-arg
        // signature introduced in audit fix 2026-06-28.
        wiring.grpcRa().requestMenu(event.getSessionId(), event.getMsisdn(), event.getUssdString(), aci);
    }

    private void onGrpcResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        String menu = "OK".equals(event.getStatus())
                ? event.getMenuText()
                : "ERR: " + event.getError();
        int tier = 1;
        Object tierObj = cmpRead(method("getMenuTier"));
        if (tierObj instanceof Integer) {
            tier = ((Integer) tierObj).intValue();
        }
        String ussdText = "USSD menu for session " + event.getSessionId()
                + " (tier " + tier + "):\n" + menu;
        LOG.infof("[SS7-ingress] MAP response ready session=%s", event.getSessionId());
        wiring.routeEvent(new UssdCompleteEvent(event.getSessionId(), ussdText), aci);
    }

    private Method method(String name, Class<?>... params) {
        try {
            return Ss7UssdIngressSbb.class.getMethod(name, params);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  Concrete companion (Perfect Core S2).
    //
    //  Production deployments generate this class at deploy-time via
    //  {@code com.microjainslee.codegen.ConcreteSbbGenerator} which
    //  reads the @CmpField annotations above and emits a real subclass
    //  with backing fields and direct getter/setter bodies. The hand-
    //  written $Concrete stub below lets this example run without the
    //  codegen step on the classpath; it stores CMP fields in a
    //  per-entity map and delegates all reads/writes through
    //  {@link #cmpRead(Method)} / {@link #cmpWrite(Method, Object)}.
    // ─────────────────────────────────────────────────────────────────

    /**
     * Concrete subclass generated by {@code ConcreteSbbGenerator} at
     * deploy time. Kept here so the example can run without the codegen
     * tool on the classpath.
     */
    public static final class $Concrete extends Ss7UssdIngressSbb {

        private final java.util.Map<String, Object> local = new java.util.concurrent.ConcurrentHashMap<>();

        public $Concrete() {
            super();
        }

        public $Concrete(UssdSbbWiring wiring) {
            super(wiring);
        }

        @Override
        public String getSessionId() {
            Object v = local.get("sessionId");
            return v instanceof String s ? s : (String) cmpRead(method("getSessionId"));
        }

        @Override
        public void setSessionId(String sessionId) {
            local.put("sessionId", sessionId);
            cmpWrite(method("setSessionId", String.class), sessionId);
        }

        @Override
        public String getMsisdn() {
            Object v = local.get("msisdn");
            return v instanceof String s ? s : (String) cmpRead(method("getMsisdn"));
        }

        @Override
        public void setMsisdn(String msisdn) {
            local.put("msisdn", msisdn);
            cmpWrite(method("setMsisdn", String.class), msisdn);
        }

        @Override
        public int getMenuTier() {
            Object v = local.get("menuTier");
            if (v instanceof Integer i) {
                return i;
            }
            Object r = cmpRead(method("getMenuTier"));
            return r instanceof Integer i ? i : 1;
        }

        @Override
        public void setMenuTier(int menuTier) {
            local.put("menuTier", menuTier);
            cmpWrite(method("setMenuTier", int.class), menuTier);
        }

        @Override
        public String getDialogId() {
            Object v = local.get("dialogId");
            return v instanceof String s ? s : (String) cmpRead(method("getDialogId"));
        }

        @Override
        public void setDialogId(String dialogId) {
            local.put("dialogId", dialogId);
            cmpWrite(method("setDialogId", String.class), dialogId);
        }

        private static Method method(String name, Class<?>... params) {
            try {
                return Ss7UssdIngressSbb.class.getMethod(name, params);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
