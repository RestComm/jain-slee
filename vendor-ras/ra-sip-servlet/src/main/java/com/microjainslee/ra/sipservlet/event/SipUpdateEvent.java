package com.microjainslee.ra.sipservlet.event;

/** SIP UPDATE — RFC 3311 update session parameters before final answer. */
public record SipUpdateEvent(
    String callId,
    String fromUri,
    String toUri,
    String sdpBody
) implements SipEvent {
    @Override public String method() { return "UPDATE"; }
}
