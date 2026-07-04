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

import com.example.ussddemo.spring.events.HttpUssdBeginEvent;
import com.example.ussddemo.spring.events.Ss7UssdBeginEvent;
import com.example.ussddemo.spring.events.UssdCompleteEvent;
import com.example.ussddemo.spring.profile.UssdSubscriberProfile;
import com.example.ussddemo.spring.service.UssdCallbackDispatcher;
import com.example.ussddemo.spring.service.UssdSessionStore;
import com.example.ussddemo.spring.service.UssdWiring;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.api.ProfileTable;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbContext;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.TimerFiredEvent;
import com.microjainslee.api.annotations.SbbAnnotation;
import com.microjainslee.core.InMemoryActivityContext;
import com.microjainslee.core.SimpleSbbLocalObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GW-facing entry SBB. Receives {@link HttpUssdBeginEvent} from the HTTP RA,
 * performs profile lookup, arms a session timer, and routes to the SS7 leg.
 */
@SbbAnnotation(name = "HttpServer", vendor = "com.example.ussddemo", version = "1.0")
public final class HttpServerSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HttpServerSbb.class);
    private static final long SESSION_TIMEOUT_MS = 30_000L;
    private static final String PROFILE_TABLE = "ussdSubscribers";

    private UssdWiring wiring;
    private UssdSessionStore sessionStore;
    private UssdCallbackDispatcher callbackDispatcher;
    private SbbContext sbbContext;
    private long sessionTimerId;

    public HttpServerSbb() {
    }

    public HttpServerSbb(UssdWiring wiring, UssdSessionStore sessionStore,
                         UssdCallbackDispatcher callbackDispatcher) {
        this.wiring = wiring;
        this.sessionStore = sessionStore;
        this.callbackDispatcher = callbackDispatcher;
    }

    public void setWiring(UssdWiring wiring) { this.wiring = wiring; }
    public void setSessionStore(UssdSessionStore sessionStore) { this.sessionStore = sessionStore; }
    public void setCallbackDispatcher(UssdCallbackDispatcher callbackDispatcher) {
        this.callbackDispatcher = callbackDispatcher;
    }

    @Override
    public void setSbbContext(SbbContext context) {
        this.sbbContext = context;
    }

    @Override public void sbbCreate() { }
    @Override public void sbbActivate() { }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof HttpUssdBeginEvent) {
            onHttpBegin((HttpUssdBeginEvent) event, aci);
        } else if (event instanceof UssdCompleteEvent) {
            onUssdComplete((UssdCompleteEvent) event);
        } else if (event instanceof TimerFiredEvent) {
            onTimer((TimerFiredEvent) event);
        }
    }

    private void onHttpBegin(HttpUssdBeginEvent event, ActivityContextInterface aci) {
        int tier = lookupMenuTier(event.getMsisdn());
        LOG.infof("[HttpServer] begin session=%s msisdn=%s tier=%d",
                event.getSessionId(), event.getMsisdn(), tier);

        String aciName = aci.getActivityContextName();
        String sessionId = event.getSessionId();
        // Perfect Core S2 — Ss7UssdIngressSbb is abstract with @CmpField-
        // annotated accessors; the runtime works against its concrete
        // companion $Concrete (hand-written here, normally produced by
        // com.microjainslee.codegen.ConcreteSbbGenerator).
        Ss7UssdIngressSbb ss7Sbb = new Ss7UssdIngressSbb.$Concrete(wiring);
        GrpcClientSbb grpcSbb = new GrpcClientSbb();
        SimpleSbbLocalObject ss7 = wiring.container().registerSbb(sessionId + "/Ss7", ss7Sbb);
        SimpleSbbLocalObject grpc = wiring.container().registerSbb(sessionId + "/Grpc", grpcSbb);
        ss7.setPriority(7);
        grpc.setPriority(5);
        if (aci instanceof InMemoryActivityContext) {
            InMemoryActivityContext ctx = (InMemoryActivityContext) aci;
            ctx.attachImmediate(ss7);
            ctx.attachImmediate(grpc);
        } else {
            wiring.container().attach(aciName, ss7);
            wiring.container().attach(aciName, grpc);
        }

        if (sbbContext != null) {
            sessionTimerId = sbbContext.getTimerFacility().setTimer(
                    SESSION_TIMEOUT_MS, sbbContext.getSbbLocalObject());
        }

        wiring.container().routeEvent(
                new Ss7UssdBeginEvent(event.getSessionId(), event.getMsisdn(),
                        event.getUssdString(), tier),
                aci);
    }

    private int lookupMenuTier(String msisdn) {
        try {
            ProfileFacility facility = wiring.container().getProfileFacility();
            if (facility == null) {
                return 1;
            }
            ProfileTable table = facility.getProfileTable(PROFILE_TABLE);
            if (table == null) {
                return 1;
            }
            ProfileLocalObject plo = table.getProfile(msisdn);
            if (plo == null) {
                return 1;
            }
            Profile profile = plo.getProfile();
            if (profile instanceof UssdSubscriberProfile) {
                return ((UssdSubscriberProfile) profile).getMenuTier();
            }
            Object tier = profile.getCmpField("menuTier");
            return tier instanceof Number ? ((Number) tier).intValue() : 1;
        } catch (Exception e) {
            LOG.debugf("No profile for msisdn=%s, default tier=1", msisdn);
            return 1;
        }
    }

    private void onUssdComplete(UssdCompleteEvent event) {
        String sessionId = event.getSessionId();
        if (event.getErrorMessage() != null) {
            sessionStore.fail(sessionId, event.getErrorMessage());
            UssdSessionStore.SessionRecord record = sessionStore.get(sessionId);
            if (record != null && record.getCallbackUrl() != null) {
                callbackDispatcher.dispatch(record.getCallbackUrl(), sessionId, "FAILED",
                        null, event.getErrorMessage());
            }
        } else {
            sessionStore.complete(sessionId, event.getResponseText());
            UssdSessionStore.SessionRecord record = sessionStore.get(sessionId);
            if (record != null && record.getCallbackUrl() != null) {
                callbackDispatcher.dispatch(record.getCallbackUrl(), sessionId, "COMPLETED",
                        event.getResponseText(), null);
            }
        }
        if (sessionTimerId != 0L && sbbContext != null) {
            sbbContext.getTimerFacility().cancelTimer(sessionTimerId);
            sessionTimerId = 0L;
        }
        wiring.httpRa().onHttpEnd(sessionId);
        LOG.infof("[HttpServer] session complete session=%s", sessionId);
    }

    private void onTimer(TimerFiredEvent event) {
        if (event.getSbbLocalObject() != sbbContext.getSbbLocalObject()) {
            return;
        }
        LOG.warn("[HttpServer] session timeout");
    }
}
