/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 */

package com.example.ussddemo.quarkus.sbbs;

import com.example.ussddemo.quarkus.bootstrap.UssdDemoContext;
import com.example.ussddemo.quarkus.events.GrpcMenuRequestEvent;
import com.example.ussddemo.quarkus.events.GrpcMenuResponseEvent;
import com.example.ussddemo.quarkus.events.Ss7UssdBeginEvent;
import com.example.ussddemo.quarkus.events.UssdResponseEvent;
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

/**
 * Internal MAP/USSD service leg. Registered at runtime via
 * {@code registerSbbType}. Uses vendor-ras RAs via the endpoint pattern.
 */
@SbbAnnotation(name = "Ss7UssdIngress", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public abstract class Ss7UssdIngressSbb extends CmpBackedSbb implements SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(Ss7UssdIngressSbb.class);
    private static final long SESSION_TIMEOUT_MS = 30_000L;

    private final UssdDemoContext ctx;
    private volatile SbbLocalObject self;
    private volatile long sessionTimerId = -1L;

    public Ss7UssdIngressSbb(UssdDemoContext ctx) {
        this.ctx = ctx;
    }

    /** No-arg constructor required by IES dispatcher for TEMP instance evaluation. */
    public Ss7UssdIngressSbb() {
        this.ctx = null;
    }

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

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
    @Override public void sbbRemove() { cancelSessionTimer(); }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof Ss7UssdBeginEvent) {
            onSs7Begin((Ss7UssdBeginEvent) event, aci);
        } else if (event instanceof GrpcMenuResponseEvent) {
            onGrpcResponse((GrpcMenuResponseEvent) event, aci);
        } else if (event instanceof TimerFiredEvent) {
            onTimer((TimerFiredEvent) event, aci);
        }
    }

    private void onSs7Begin(Ss7UssdBeginEvent event, ActivityContextInterface aci) {
        LOG.info("[SS7-ingress] MAP begin session={} msisdn={} tier={} text={}",
                getSessionId(), getMsisdn(), getMenuTier(), event.getUssdString());

        MicroSleeContainer container = ctx.container();
        sessionTimerId = container.getTimerPort().setTimer(SESSION_TIMEOUT_MS, self);

        SimpleSbbLocalObject parentLo = (SimpleSbbLocalObject) self;
        ChildRelation grpcChildren = parentLo.getChildRelation("grpc",
                container.getChildRelationFactory(GrpcClientSbb.class));
        try {
            SbbLocalObject grpcLo = grpcChildren.create();
            grpcLo.setPriority(5);
            container.attach(getSessionId(), grpcLo);
            waitForActivation((SimpleSbbLocalObject) grpcLo);
            ((GrpcClientSbb) ((SimpleSbbLocalObject) grpcLo).getSbb()).bindSelf(grpcLo);
        } catch (Exception e) {
            LOG.error("Failed to create GrpcClientSbb child for session={}", getSessionId(), e);
            ctx.failSession(getSessionId(), "grpc-child-create-failed");
            return;
        }

        container.routeEvent(new GrpcMenuRequestEvent(
                getSessionId(), getMsisdn(), event.getUssdString()), aci);
    }

    private void onGrpcResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        cancelSessionTimer();
        String ussdText = "USSD menu for session " + getSessionId()
                + " (tier " + getMenuTier() + "):\n" + event.getMenuText();
        LOG.info("[SS7-ingress] MAP response ready session={}", getSessionId());
        ctx.container().routeEvent(
                new UssdResponseEvent(getSessionId(), ussdText), aci);
    }

    private void onTimer(TimerFiredEvent event, ActivityContextInterface aci) {
        if (event.getSbbLocalObject() != self) return;
        LOG.warn("[SS7-ingress] session timeout session={}", getSessionId());
        ctx.failSession(getSessionId(), "session timeout");
        ctx.releaseSession(getSessionId());
    }

    private void cancelSessionTimer() {
        if (sessionTimerId >= 0L) {
            ctx.container().getTimerPort().cancelTimer(sessionTimerId);
            sessionTimerId = -1L;
        }
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) return;
            Thread.sleep(10L);
        }
    }

    // ── $Concrete ──

    public static final class $Concrete extends Ss7UssdIngressSbb {

        private final java.util.Map<String, Object> local =
                new java.util.concurrent.ConcurrentHashMap<>();

        public $Concrete(UssdDemoContext ctx) { super(ctx); }
        public $Concrete() { super(); }

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
        public String getMenuTier() {
            Object v = local.get("menuTier");
            return v instanceof String s ? s : (String) cmpRead(method("getMenuTier"));
        }
        @Override
        public void setMenuTier(String menuTier) {
            local.put("menuTier", menuTier);
            cmpWrite(method("setMenuTier", String.class), menuTier);
        }

        private static Method method(String name, Class<?>... params) {
            try { return Ss7UssdIngressSbb.class.getMethod(name, params); }
            catch (NoSuchMethodException e) { throw new IllegalStateException(e); }
        }
    }
}
