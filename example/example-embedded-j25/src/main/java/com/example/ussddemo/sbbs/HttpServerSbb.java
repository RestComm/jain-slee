/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.sbbs;

import com.example.ussddemo.EmbeddedUssdBootstrap;
import com.example.ussddemo.events.HttpUssdBeginEvent;
import com.example.ussddemo.events.Ss7UssdBeginEvent;
import com.example.ussddemo.events.UssdResponseEvent;
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
import com.microjainslee.ra.httpclient.command.HttpCallbackCommand;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;

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
    private final EmbeddedUssdBootstrap bootstrap;
    private volatile SbbLocalObject self;

    /** Injected HTTP callback RA command port for async callback delivery. */
    @InjectRa(name = "http-callback-ra")
    private volatile RaCommandPort httpCallbackPort;

    public HttpServerSbb(MicroSleeContainer container, EmbeddedUssdBootstrap bootstrap) {
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
        } else if (event instanceof HttpWebRequestEvent req) {
            onHttpWebRequest(req, aci);
        } else if (event instanceof UssdResponseEvent) {
            onUssdResponse((UssdResponseEvent) event, aci);
        }
    }

    private void onHttpWebRequest(HttpWebRequestEvent req, ActivityContextInterface aci) {
        String path = req.getPath();
        String method = req.getMethod();
        LOG.info("[HTTP-server] RA web request session={} {} {}", req.getSessionId(), method, path);

        if ("POST".equalsIgnoreCase(method) && "/api/ussd/begin".equals(path)) {
            String body = req.getBody();
            if (body == null || body.isEmpty()) {
                LOG.warn("[HTTP-server] Empty body on USSD begin, session={}", req.getSessionId());
                return;
            }
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper =
                        new com.fasterxml.jackson.databind.ObjectMapper();
                @SuppressWarnings("unchecked")
                java.util.Map<String, String> map = mapper.readValue(body, java.util.Map.class);
                String msisdn = map.getOrDefault("msisdn", "unknown");
                String ussdString = map.getOrDefault("ussdString", "");
                String callbackUrl = map.getOrDefault("callbackUrl", null);

                LOG.info("[HTTP-server] RA USSD begin parsed: msisdn={} ussd={}", msisdn, ussdString);
                HttpUssdBeginEvent ussdEvent = new HttpUssdBeginEvent(
                        req.getSessionId(), msisdn, ussdString, callbackUrl);
                onHttpBegin(ussdEvent, aci);
            } catch (Exception e) {
                LOG.error("[HTTP-server] Failed to parse USSD body for session={}: {}",
                        req.getSessionId(), e.getMessage());
            }
        }
    }

    private void onHttpBegin(HttpUssdBeginEvent event, ActivityContextInterface aci) {
        try {
            String tier = lookupTier(event.getMsisdn());
            LOG.info("[HTTP-server] begin session={} msisdn={} tier={}",
                    event.getSessionId(), event.getMsisdn(), tier);

            String ss7Id = this.bootstrap.ss7EntityId(event.getSessionId());
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
        port.sendCommand(new HttpCallbackCommand.CallbackRequest(sessionId, callbackUrl, responseText));
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
