package com.example.sipgateway.sbbs;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.ra.sipservlet.command.SelectIceCandidate;
import com.microjainslee.ra.sipservlet.events.IceCandidateEvent;
import com.microjainslee.ra.sipservlet.events.IceCompletedEvent;
import com.microjainslee.ra.sipservlet.events.IceFailedEvent;
import com.microjainslee.ra.sipservlet.stun.IceCandidateCollector.Candidate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;

/**
 * ICE Negotiation SBB — RFC 8445 candidate selection.
 * <p>Receives {@link IceCandidateEvent} from the SIP RA after STUN binding,
 * selects the optimal candidate pair using RFC 5245 priority formula,
 * and commands the RA via {@code @InjectRa}.
 */
public class IceNegotiationSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(IceNegotiationSbb.class);

    @InjectRa(name = "sip-servlet-ra")
    private volatile RaCommandPort sipRa;

    public IceNegotiationSbb() { /* no-arg required for entity pool */ }

    @Override public void sbbCreate() { LOG.debug("IceNegotiationSbb created"); }
    @Override public void sbbActivate() { LOG.debug("IceNegotiationSbb activated"); }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof IceCandidateEvent iceEvent) {
            onIceCandidateEvent(iceEvent, aci);
        } else if (event instanceof IceCompletedEvent completed) {
            onIceCompletedEvent(completed, aci);
        } else if (event instanceof IceFailedEvent failed) {
            onIceFailedEvent(failed, aci);
        }
    }

    public void onIceCandidateEvent(IceCandidateEvent event, ActivityContextInterface aci) {
        List<Candidate> candidates = event.candidates();
        if (candidates == null || candidates.isEmpty()) {
            LOG.warn("[IceNegotiationSbb] No candidates for callId={}", event.callId());
            return;
        }
        LOG.info("[IceNegotiationSbb] {} candidates for callId={}", candidates.size(), event.callId());

        List<Candidate> sorted = candidates.stream()
                .sorted(Comparator.comparingLong(IceNegotiationSbb::computeEffectivePriority).reversed())
                .toList();

        Candidate best = sorted.get(0);
        LOG.info("[IceNegotiationSbb] Selected best candidate: type={} addr={}:{} pri={} callId={}",
                best.type(), best.address(), best.port(), best.priority(), event.callId());

        RaCommandPort port = this.sipRa;
        if (port != null) {
            port.sendCommand(new SelectIceCandidate(
                    event.callId(), best.address(), best.port(), best.type()));
        } else {
            LOG.warn("[IceNegotiationSbb] sipRa not injected - candidate selection dropped");
        }
    }

    public void onIceCompletedEvent(IceCompletedEvent event, ActivityContextInterface aci) {
        LOG.info("[IceNegotiationSbb] ICE completed callId={} local={}:{} remote={}:{}",
                event.callId(), event.localAddress(), event.localPort(),
                event.remoteAddress(), event.remotePort());
    }

    public void onIceFailedEvent(IceFailedEvent event, ActivityContextInterface aci) {
        LOG.warn("[IceNegotiationSbb] ICE failed callId={} reason={}", event.callId(), event.reason());
    }

    private static long computeEffectivePriority(Candidate c) {
        long typePref = switch (c.type()) {
            case "host"  -> 126L;
            case "srflx" -> 100L;
            default      -> 0L;
        };
        return (typePref << 24) | (c.priority() & 0x00FFFFFFL);
    }
}
