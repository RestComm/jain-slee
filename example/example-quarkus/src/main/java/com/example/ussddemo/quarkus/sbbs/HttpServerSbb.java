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

import com.example.ussddemo.quarkus.events.HttpUssdBeginEvent;
import com.example.ussddemo.quarkus.events.Ss7UssdBeginEvent;
import com.example.ussddemo.quarkus.events.UssdCompleteEvent;
import com.example.ussddemo.quarkus.service.UssdSbbWiring;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.TimerFiredEvent;
import com.microjainslee.api.annotations.SbbAnnotation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GW-facing entry SBB — receives HTTP RA events, arms session timer,
 * routes to the internal SS7 MAP leg.
 */
@SbbAnnotation(name = "HttpServer", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public final class HttpServerSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HttpServerSbb.class);

    private final UssdSbbWiring wiring;

    public HttpServerSbb(UssdSbbWiring wiring) {
        this.wiring = wiring;
    }

    public HttpServerSbb() {
        this.wiring = null;
    }

    @Override public void sbbCreate() { LOG.debug("HttpServerSbb created"); }
    @Override public void sbbActivate() { LOG.debug("HttpServerSbb activated"); }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof HttpUssdBeginEvent) {
            onHttpBegin((HttpUssdBeginEvent) event, aci);
        } else if (event instanceof UssdCompleteEvent) {
            onComplete((UssdCompleteEvent) event);
        } else if (event instanceof TimerFiredEvent) {
            onTimer((TimerFiredEvent) event);
        }
    }

    private void onHttpBegin(HttpUssdBeginEvent event, ActivityContextInterface aci) {
        LOG.infof("[HTTP-SBB] begin session=%s msisdn=%s tier=%d",
                event.getSessionId(), event.getMsisdn(), event.getMenuTier());

        SbbLocalObject httpLocal = wiring.httpLocal(event.getSessionId());
        if (httpLocal != null) {
            long timerId = wiring.container().getTimerPort()
                    .setTimer(wiring.sessionTimeoutMs(), httpLocal);
            wiring.rememberTimer(event.getSessionId(), timerId);
        }

        wiring.routeEvent(new Ss7UssdBeginEvent(
                event.getSessionId(), event.getMsisdn(), event.getUssdString(),
                event.getMenuTier()), aci);
    }

    private void onComplete(UssdCompleteEvent event) {
        LOG.infof("[HTTP-SBB] complete session=%s", event.getSessionId());
        wiring.completeSession(event.getSessionId(), event.getResponseText());
    }

    private void onTimer(TimerFiredEvent event) {
        SbbLocalObject lo = event.getSbbLocalObject();
        if (lo == null) {
            return;
        }
        String entityId = lo.getSbbID().getId();
        String sessionId = entityId.endsWith("/http")
                ? entityId.substring(0, entityId.length() - "/http".length())
                : entityId;
        LOG.warnf("[HTTP-SBB] session timeout session=%s", sessionId);
        wiring.failSession(sessionId, "session timeout");
    }
}
