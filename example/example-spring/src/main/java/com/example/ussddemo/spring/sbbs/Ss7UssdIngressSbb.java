/*
 * micro-jainslee 1.1.0 -- example application (example-spring)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.sbbs;

import com.example.ussddemo.spring.events.GrpcMenuResponseEvent;
import com.example.ussddemo.spring.events.Ss7UssdBeginEvent;
import com.example.ussddemo.spring.events.UssdCompleteEvent;
import com.example.ussddemo.spring.service.UssdWiring;
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
 * Internal SS7/MAP USSD leg. Persists session state in CMP fields and
 * delegates menu resolution to the gRPC RA.
 *
 * <p>Updated for Perfect Core S1-S5 (2026-06-28):</p>
 * <ul>
 *   <li><b>S2</b> — CMP accessors declared as {@code abstract} so the
 *       Javassist concrete generator can produce the backing getters
 *       and setters. A hand-written {@code $Concrete} companion keeps
 *       the example self-contained.</li>
 *   <li><b>S3</b> — {@link InitialEventSelect @InitialEventSelect} method
 *       routes every USSD session to the same entity by msisdn so the
 *       CMP state survives across the request / response round-trip.</li>
 *   <li><b>P1</b> — safe under {@code txEnabled=false}; CMP writes go
 *       straight to the in-memory store through {@code cmpWrite}.</li>
 * </ul>
 */
@SbbAnnotation(name = "Ss7UssdIngress", vendor = "com.example.ussddemo", version = "1.0")
public abstract class Ss7UssdIngressSbb extends CmpBackedSbb implements SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(Ss7UssdIngressSbb.class);

    private UssdWiring wiring;

    public Ss7UssdIngressSbb() {
    }

    public Ss7UssdIngressSbb(UssdWiring wiring) {
        this.wiring = wiring;
    }

    public void setWiring(UssdWiring wiring) {
        this.wiring = wiring;
    }

    // ─────────────────────────────────────────────────────────────────
    //  CMP accessors — abstract on purpose (Perfect Core S2).
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

    // ─────────────────────────────────────────────────────────────────
    //  Initial Event Selection (Perfect Core S3).
    //  Runs on a TEMP instance — must be free of side effects on CMP state.
    // ─────────────────────────────────────────────────────────────────

    /**
     * IES — convergence name = {@code msisdn}. The first
     * {@link Ss7UssdBeginEvent} for a given msisdn is the initial event;
     * subsequent events are routed to the same entity per spec §7.5.5.
     */
    @InitialEventSelect(name = "ussd-session-convergence")
    public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
        Object event = c.getEvent();
        if (event instanceof Ss7UssdBeginEvent) {
            Ss7UssdBeginEvent e = (Ss7UssdBeginEvent) event;
            String msisdn = e.getMsisdn() == null ? "anon" : e.getMsisdn();
            return InitialEventSelectResult.forSession(msisdn, true);
        }
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
        setSessionId(event.getSessionId());
        setMsisdn(event.getMsisdn());
        setMenuTier(event.getMenuTier());
        LOG.infof("[SS7-ingress] MAP begin session=%s msisdn=%s tier=%d",
                event.getSessionId(), event.getMsisdn(), event.getMenuTier());
        if (wiring != null && wiring.grpcRa() != null) {
            wiring.grpcRa().requestMenu(event.getSessionId(), event.getMsisdn(),
                    event.getUssdString(), aci);
        }
    }

    private void onGrpcResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        if (wiring == null || wiring.container() == null) {
            return;
        }
        if (!"OK".equalsIgnoreCase(event.getStatus())) {
            wiring.container().routeEvent(new UssdCompleteEvent(
                    event.getSessionId(), null, event.getError()), aci);
            return;
        }
        String ussdText = "USSD menu for session " + event.getSessionId() + ":\n"
                + event.getMenuText();
        LOG.infof("[SS7-ingress] MAP response ready session=%s", event.getSessionId());
        wiring.container().routeEvent(new UssdCompleteEvent(
                event.getSessionId(), ussdText, null), aci);
    }

    private static Method accessor(String name, Class<?>... params) {
        try {
            return Ss7UssdIngressSbb.class.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  Concrete companion (Perfect Core S2).
    //
    //  Production deployments generate this class at deploy-time via
    //  {@code com.microjainslee.codegen.ConcreteSbbGenerator}; the
    //  hand-written stub below keeps the Spring example self-contained.
    // ─────────────────────────────────────────────────────────────────

    /**
     * Concrete subclass generated by {@code ConcreteSbbGenerator} at
     * deploy time.
     */
    public static final class $Concrete extends Ss7UssdIngressSbb {

        private final java.util.Map<String, Object> local =
                new java.util.concurrent.ConcurrentHashMap<>();

        public $Concrete() {
            super();
        }

        public $Concrete(UssdWiring wiring) {
            super(wiring);
        }

        @Override
        public String getSessionId() {
            Object v = local.get("sessionId");
            return v instanceof String s ? s : (String) cmpRead(accessor("getSessionId"));
        }

        @Override
        public void setSessionId(String sessionId) {
            local.put("sessionId", sessionId);
            cmpWrite(accessor("setSessionId", String.class), sessionId);
        }

        @Override
        public String getMsisdn() {
            Object v = local.get("msisdn");
            return v instanceof String s ? s : (String) cmpRead(accessor("getMsisdn"));
        }

        @Override
        public void setMsisdn(String msisdn) {
            local.put("msisdn", msisdn);
            cmpWrite(accessor("setMsisdn", String.class), msisdn);
        }

        @Override
        public int getMenuTier() {
            Object v = local.get("menuTier");
            if (v instanceof Integer i) {
                return i;
            }
            Object r = cmpRead(accessor("getMenuTier"));
            return r instanceof Integer i ? i : 0;
        }

        @Override
        public void setMenuTier(int menuTier) {
            local.put("menuTier", menuTier);
            cmpWrite(accessor("setMenuTier", int.class), menuTier);
        }

        private static Method accessor(String name, Class<?>... params) {
            try {
                return Ss7UssdIngressSbb.class.getMethod(name, params);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
