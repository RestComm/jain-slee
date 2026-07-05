package com.microjainslee.ra.sipservlet.event;

/** Fired when ICE negotiation fails. */
public record IceFailedEvent(
    String callId,
    String reason
) implements SipEvent {
    @Override public String method() { return "ICE-FAILED"; }
}
