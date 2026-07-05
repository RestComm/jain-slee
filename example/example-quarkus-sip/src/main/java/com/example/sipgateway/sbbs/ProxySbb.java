package com.example.sipgateway.sbbs;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.ra.sipservlet.command.SendInvite;
import com.microjainslee.ra.sipservlet.command.SendResponse;
import com.microjainslee.ra.sipservlet.command.SendSdpUpdate;
import com.microjainslee.ra.sipservlet.event.SipInviteEvent;
import com.microjainslee.ra.sipservlet.event.SipResponseEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * SIP Proxy SBB — RFC 3261 request routing.
 * <p>Receives INVITE events from the SIP RA, looks up the target domain
 * in a routing table, and forwards the request via {@code @InjectRa}.
 * The SIP RA handles all DNS SRV/NAPTR resolution transparently.
 */
public class ProxySbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(ProxySbb.class);
    private static final Map<String, String> ROUTING_TABLE = Map.of(
            "example.com", "sip:pbx@example.com",
            "demo.local", "sip:trunk@demo.local"
    );

    @InjectRa(name = "sip-servlet-ra")
    private volatile RaCommandPort sipRa;

    public ProxySbb() { /* no-arg required for entity pool */ }

    @Override public void sbbCreate() { LOG.debug("ProxySbb created"); }
    @Override public void sbbActivate() { LOG.debug("ProxySbb activated"); }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof SipInviteEvent invite) {
            onSipInviteEvent(invite, aci);
        } else if (event instanceof SipResponseEvent response) {
            onSipResponseEvent(response, aci);
        }
    }

    public void onSipInviteEvent(SipInviteEvent event, ActivityContextInterface aci) {
        LOG.info("[ProxySbb] INVITE callId={} from={} to={}", event.callId(), event.fromUri(), event.toUri());
        String targetDomain = extractDomain(event.toUri());
        String nextHop = ROUTING_TABLE.getOrDefault(targetDomain, event.toUri());
        LOG.info("[ProxySbb] Routing INVITE callId={} domain={} nextHop={}", event.callId(), targetDomain, nextHop);
        RaCommandPort port = this.sipRa;
        if (port != null) {
            port.sendCommand(new SendInvite(event.callId(), nextHop, event.fromUri(), event.sdpBody()));
        } else {
            LOG.warn("[ProxySbb] sipRa not injected - INVITE dropped callId={}", event.callId());
        }
    }

    public void onSipResponseEvent(SipResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[ProxySbb] RESPONSE callId={} status={} {} provisional={} success={}",
                event.callId(), event.statusCode(), event.reasonPhrase(),
                event.isProvisional(), event.isSuccess());
        RaCommandPort port = this.sipRa;
        if (port == null) {
            LOG.warn("[ProxySbb] sipRa not injected - response dropped callId={}", event.callId());
            return;
        }
        if (event.isProvisional()) {
            port.sendCommand(new SendResponse(event.callId(), event.statusCode(), event.reasonPhrase()));
        } else if (event.isSuccess() && event.sdpBody() != null && !event.sdpBody().isEmpty()) {
            port.sendCommand(new SendSdpUpdate(event.callId(), event.sdpBody()));
        } else if (event.isFinal()) {
            port.sendCommand(new SendResponse(event.callId(), event.statusCode(), event.reasonPhrase()));
        }
    }

    private static String extractDomain(String uri) {
        if (uri == null) return "unknown";
        int atIdx = uri.indexOf('@');
        if (atIdx >= 0) {
            String afterAt = uri.substring(atIdx + 1);
            int semicolon = afterAt.indexOf(';');
            return semicolon >= 0 ? afterAt.substring(0, semicolon) : afterAt;
        }
        String stripped = uri.startsWith("sip:") ? uri.substring(4) : uri;
        int semicolon = stripped.indexOf(';');
        return semicolon >= 0 ? stripped.substring(0, semicolon) : stripped;
    }
}
