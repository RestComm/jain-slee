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

import com.example.ussddemo.spring.UssdDemoRuntime;
import com.example.ussddemo.spring.UssdDemoContext;
import com.example.ussddemo.spring.events.GrpcMenuRequestEvent;
import com.example.ussddemo.spring.events.GrpcMenuResponseEvent;
import com.example.ussddemo.spring.events.Ss7UssdBeginEvent;
import com.example.ussddemo.spring.events.UssdResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ChildRelation;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.TimerFiredEvent;
import com.microjainslee.api.annotations.CmpField;
import com.microjainslee.api.annotations.InitialEventSelect;
import com.microjainslee.api.annotations.SbbAnnotation;
import com.microjainslee.core.CmpBackedSbb;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.core.ies.InitialEventSelectCondition;
import com.microjainslee.core.ies.InitialEventSelectResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

@SbbAnnotation(name = "Ss7UssdIngress", vendor = "com.example.ussddemo", version = "1.0")
public abstract class Ss7UssdIngressSbb extends CmpBackedSbb implements SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(Ss7UssdIngressSbb.class);
    private static final long SESSION_TIMEOUT_MS = 30_000L;

    protected final MicroSleeContainer container;
    protected final UssdDemoContext bootstrap;
    protected final UssdDemoRuntime runtime;

    private volatile SbbLocalObject self;
    private volatile long sessionTimerId = -1L;

    protected Ss7UssdIngressSbb(MicroSleeContainer container, UssdDemoContext bootstrap, UssdDemoRuntime runtime) {
        this.container = container;
        this.bootstrap = bootstrap;
        this.runtime = runtime;
    }

    public void bindSelf(SbbLocalObject self) { this.self = self; }

    public void initCmp(String sessionId, String msisdn, String menuTier) {
        setSessionId(sessionId);
        setMsisdn(msisdn);
        setMenuTier(menuTier);
    }

    @CmpField("sessionId")
    public abstract String getSessionId();
    @CmpField("sessionId")
    public abstract void setSessionId(String sessionId);
    @CmpField("msisdn")
    public abstract String getMsisdn();
    @CmpField("msisdn")
    public abstract void setMsisdn(String msisdn);
    @CmpField("menuTier")
    public abstract String getMenuTier();
    @CmpField("menuTier")
    public abstract void setMenuTier(String menuTier);

    @InitialEventSelect(name = "ussd-session-convergence")
    public InitialEventSelectResult selectInitialEvent(InitialEventSelectCondition c) {
        Object event = c.getEvent();
        if (event instanceof Ss7UssdBeginEvent e) {
            String msisdn = e.getMsisdn() == null ? "anon" : e.getMsisdn();
            return InitialEventSelectResult.forSession(msisdn, true);
        }
        return InitialEventSelectResult.builder().convergenceName(null).initialEvent(false).build();
    }

    @Override public void sbbCreate() { LOG.debug("Ss7UssdIngressSbb created"); }
    @Override public void sbbActivate() { LOG.debug("Ss7UssdIngressSbb activated"); }
    @Override public void sbbPassivate() {}
    @Override public void sbbRemove() { cancelSessionTimer(); }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof Ss7UssdBeginEvent e) onSs7Begin(e, aci);
        else if (event instanceof GrpcMenuResponseEvent e) onGrpcResponse(e, aci);
        else if (event instanceof TimerFiredEvent e) onTimer(e, aci);
    }

    private void onSs7Begin(Ss7UssdBeginEvent event, ActivityContextInterface aci) {
        LOG.info("[SS7-ingress] MAP begin session={} msisdn={} tier={} text={}",
                getSessionId(), getMsisdn(), getMenuTier(), event.getUssdString());
        sessionTimerId = this.container.getTimerPort().setTimer(SESSION_TIMEOUT_MS, self);
        SimpleSbbLocalObject parentLo = (SimpleSbbLocalObject) self;
        ChildRelation grpcChildren = parentLo.getChildRelation("grpc",
                this.container.getChildRelationFactory(GrpcClientSbb.class));
        try {
            SbbLocalObject grpcLo = grpcChildren.create();
            grpcLo.setPriority(5);
            this.container.attach(getSessionId(), grpcLo);
            waitForActivation((SimpleSbbLocalObject) grpcLo);
            ((GrpcClientSbb) ((SimpleSbbLocalObject) grpcLo).getSbb()).bindSelf(grpcLo);
        } catch (Exception e) {
            LOG.error("Failed to create GrpcClientSbb child for session={}", getSessionId(), e);
            this.runtime.failSession(getSessionId(), "grpc-child-create-failed");
            return;
        }
        this.container.routeEvent(new GrpcMenuRequestEvent(
                getSessionId(), getMsisdn(), event.getUssdString()), aci);
    }

    private void onGrpcResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        cancelSessionTimer();
        String ussdText = "USSD menu for session " + getSessionId()
                + " (tier " + getMenuTier() + "):\n" + event.getMenuText();
        LOG.info("[SS7-ingress] MAP response ready session={}", getSessionId());
        this.container.routeEvent(
                new UssdResponseEvent(getSessionId(), ussdText), aci);
    }

    private void onTimer(TimerFiredEvent event, ActivityContextInterface aci) {
        if (event.getSbbLocalObject() != self) return;
        LOG.warn("[SS7-ingress] session timeout session={}", getSessionId());
        this.runtime.failSession(getSessionId(), "session timeout");
        this.bootstrap.releaseSession(getSessionId());
    }

    private void cancelSessionTimer() {
        if (sessionTimerId >= 0L) {
            this.container.getTimerPort().cancelTimer(sessionTimerId);
            sessionTimerId = -1L;
        }
    }

    private static Method getter(String name) {
        try { return Ss7UssdIngressSbb.class.getMethod(name); }
        catch (NoSuchMethodException e) { throw new IllegalStateException(e); }
    }
    private static Method setter(String name, Class<?> type) {
        try { return Ss7UssdIngressSbb.class.getMethod(name, type); }
        catch (NoSuchMethodException e) { throw new IllegalStateException(e); }
    }
    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) return;
            Thread.sleep(10L);
        }
    }

    public static final class $Concrete extends Ss7UssdIngressSbb {
        private final java.util.Map<String, Object> local = new java.util.concurrent.ConcurrentHashMap<>();
        public $Concrete(MicroSleeContainer container, UssdDemoContext bootstrap, UssdDemoRuntime runtime) {
            super(container, bootstrap, runtime);
        }
        @Override public String getSessionId() { Object v = local.get("sessionId"); return v instanceof String s ? s : (String) cmpRead(getter("getSessionId")); }
        @Override public void setSessionId(String sessionId) { local.put("sessionId", sessionId); cmpWrite(setter("setSessionId", String.class), sessionId); }
        @Override public String getMsisdn() { Object v = local.get("msisdn"); return v instanceof String s ? s : (String) cmpRead(getter("getMsisdn")); }
        @Override public void setMsisdn(String msisdn) { local.put("msisdn", msisdn); cmpWrite(setter("setMsisdn", String.class), msisdn); }
        @Override public String getMenuTier() { Object v = local.get("menuTier"); return v instanceof String s ? s : (String) cmpRead(getter("getMenuTier")); }
        @Override public void setMenuTier(String menuTier) { local.put("menuTier", menuTier); cmpWrite(setter("setMenuTier", String.class), menuTier); }
    }
}
