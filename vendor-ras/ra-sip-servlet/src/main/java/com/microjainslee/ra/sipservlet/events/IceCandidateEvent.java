package com.microjainslee.ra.sipservlet.events;

import com.microjainslee.ra.sipservlet.stun.IceCandidateCollector;
import java.util.List;

/** Fired when RA gathers ICE candidates for a call. */
public record IceCandidateEvent(
    String callId,
    List<IceCandidateCollector.Candidate> candidates
) implements SipEvent {
    @Override public String method() { return "ICE-CANDIDATE"; }
}
