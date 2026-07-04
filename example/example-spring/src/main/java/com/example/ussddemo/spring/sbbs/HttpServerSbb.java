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

import com.example.ussddemo.spring.config.UssdDemoBootstrap;
import com.example.ussddemo.spring.events.HttpUssdBeginEvent;
import com.example.ussddemo.spring.events.Ss7UssdBeginEvent;
import com.example.ussddemo.spring.events.UssdResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.SbbLifecycleManager;
import com.microjainslee.core.SimpleSbbLocalObject;
import com.microjainslee.ra.httpclient.HttpCallbackCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GW-facing HTTP entry SBB. Receives {@link HttpUssdBeginEvent} from the
 * HTTP ingress RA, performs subscriber profile lookup, acquires the SS7
 * ingress entity for the session, and routes {@link Ss7UssdBeginEvent}.
 *
 * <p>Registered at runtime via {@code registerSbbType}.
 */
public final class HttpServerSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HttpServerSbb.class);

    private final MicroSleeContainer container;
    private final UssdDemoBootstrap bootstrap;
    private volatile SbbLocalObject self;

    /** Injected HTTP callback RA command port for async callback delivery. */
    @InjectRa(name = "httpCallbackRa")
    private volatile RaCommandPort httpCallbackPort;

    public HttpServerSbb(MicroSleeContainer container, UssdDemoBootstrap bootstrap) {
        this.container = container;
        this.bootstrap = bootstrap;
    }

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

    @Override
    public void sbbCreate() {
        LOG.debug("HttpServerSbb created");
    }

    @Override
    public void sbbActivate() {
        LOG.debug("HttpServerSbb activated");
    }

    @Override
    public void sbbPassivate() {
    }

    @Override
    public void sbbRemove() {
    }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof HttpUssdBeginEvent) {
            onHttpBegin((HttpUssdBeginEvent) event, aci);
        } else if (event instanceof UssdResponseEvent) {
            onUssdResponse((UssdResponseEvent) event, aci);
        }
    }

    private void onHttpBegin(HttpUssdBeginEvent event, ActivityContextInterface aci) {
        try {
            String tier = lookupTier(event.getMsisdn());
            LOG.info("[HTTP-server] begin session={} msisdn={} tier={}",
                    event.getSessionId(), event.getMsisdn(), tier);

            String ss7Id = this.bootstrap.ss7EntityId(event.getSessionId());
            SimpleSbbLocalObject ss7Lo = this.container.acquireEntity(ss7Id, Ss7UssdIngressSbb.class);
            ss7Lo.setPriority(10);
            Ss7UssdIngressSbb ss7Sbb = (Ss7UssdIngressSbb) ss7Lo.getSbb();
            ss7Sbb.bindSelf(ss7Lo);
            ss7Sbb.initCmp(event.getSessionId(), event.getMsisdn(), tier);
            this.container.attach(event.getSessionId(), ss7Lo);
            waitForActivation(ss7Lo);

            this.container.routeEvent(new Ss7UssdBeginEvent(
                    event.getSessionId(), event.getMsisdn(), event.getUssdString(), tier), aci);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Interrupted while activating SS7 ingress for session={}", event.getSessionId());
        } catch (RuntimeException e) {
            LOG.error("HTTP begin handling failed for session={}", event.getSessionId(), e);
        }
    }

    private void onUssdResponse(UssdResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[HTTP-server] USSD response ready session={}", event.getSessionId());
        publishCallback(event.getSessionId(), event.getResponseText(),
                this.bootstrap.callbackUrlFor(event.getSessionId()));
        this.bootstrap.releaseSession(event.getSessionId());
    }

    private String lookupTier(String msisdn) {
        return this.bootstrap.tierFor(msisdn);
    }

    /**
     * Publish an HTTP callback through the injected RA command port.
     * The {@link RaCommandPort} is populated via {@code @InjectRa} at SBB
     * creation time. The RA delivers the callback payload to the external
     * callback URL asynchronously.
     */
    public void publishCallback(String sessionId, String responseText, String callbackUrl) {
        RaCommandPort port = this.httpCallbackPort;
        if (port == null) {
            LOG.warn("[HTTP-server] httpCallbackPort not injected yet");
            return;
        }
        port.sendCommand(new HttpCallbackCommand(sessionId, callbackUrl, responseText));
        LOG.debug("[HTTP-server] Callback command queued for session={}", sessionId);
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) {
                return;
            }
            Thread.sleep(10L);
        }
    }
}
