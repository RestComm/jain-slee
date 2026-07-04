/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 */

package com.example.ussddemo.quarkus.sbbs;

import com.example.ussddemo.quarkus.bootstrap.UssdSubscriberProfile;
import com.example.ussddemo.quarkus.events.HttpUssdBeginEvent;
import com.example.ussddemo.quarkus.events.Ss7UssdBeginEvent;
import com.example.ussddemo.quarkus.events.UssdResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.ProfileID;
import com.microjainslee.api.ProfileLocalObject;
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
 * <p>Registered at runtime via {@code registerSbbType} — not APT auto-deployed.
 * Uses vendor-ras {@code HttpServerResourceAdaptor} via the endpoint pattern.
 */
public final class HttpServerSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(HttpServerSbb.class);

    private final MicroSleeContainer container;
    private volatile SbbLocalObject self;

    /** GOAL 1-5 — injected HTTP server RA command port (vendor-ras endpoint name). */
    @InjectRa(name = "http-server-ra")
    private volatile RaCommandPort httpCommandPort;

    public HttpServerSbb(MicroSleeContainer container) {
        this.container = container;
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

            String ss7Id = "Ss7UssdIngress/" + event.getSessionId();
            SimpleSbbLocalObject ss7Lo = container.acquireEntity(ss7Id, Ss7UssdIngressSbb.class);
            ss7Lo.setPriority(10);
            Ss7UssdIngressSbb ss7Sbb = (Ss7UssdIngressSbb) ss7Lo.getSbb();
            ss7Sbb.bindSelf(ss7Lo);
            ss7Sbb.initCmp(event.getSessionId(), event.getMsisdn(), tier);
            container.attach(event.getSessionId(), ss7Lo);
            waitForActivation(ss7Lo);

            container.routeEvent(new Ss7UssdBeginEvent(
                    event.getSessionId(), event.getMsisdn(), event.getUssdString(), tier), aci);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Interrupted while activating SS7 ingress for session={}", event.getSessionId());
        } catch (RuntimeException e) {
            LOG.error("HTTP begin handling failed for session={}", event.getSessionId(), e);
            LOG.warn("Session {} failed: {}", event.getSessionId(), e.getMessage());
        }
    }

    private void onUssdResponse(UssdResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[HTTP-server] USSD response ready session={}", event.getSessionId());
        LOG.info("Session {} completed with response: {}", event.getSessionId(), event.getResponseText());
        container.releaseEntity("Ss7UssdIngress/" + event.getSessionId());
        container.releaseEntity("HttpServer/" + event.getSessionId());
    }

    private String lookupTier(String msisdn) {
        ProfileLocalObject plo = container.getProfileFacility()
                .getProfile(new ProfileID(UssdSubscriberProfile.TABLE_NAME, msisdn));
        if (plo != null && plo.getProfile() instanceof UssdSubscriberProfile sub) {
            return sub.getTier();
        }
        return "STANDARD";
    }

    private static void waitForActivation(SimpleSbbLocalObject lo) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            if (lo.getEntityState().getLifecycleState() == SbbLifecycleManager.State.READY) {
                return;
            }
            Thread.sleep(10L);
        }
    }

    /**
     * GOAL 1-5 — publish an HTTP callback through the injected RA command port.
     */
    public void publishCallback(String sessionId, String responseText, String callbackUrl) {
        RaCommandPort port = this.httpCommandPort;
        if (port == null) {
            LOG.warn("[HTTP-server] httpCommandPort not injected yet");
            return;
        }
        port.sendCommand(new HttpCallbackCommand(sessionId, callbackUrl, responseText));
        LOG.debug("[HTTP-server] Callback command queued for session={}", sessionId);
    }
}
