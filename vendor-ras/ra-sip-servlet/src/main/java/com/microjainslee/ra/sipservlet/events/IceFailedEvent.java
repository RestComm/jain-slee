package com.microjainslee.ra.sipservlet.events;

/** Fired when ICE negotiation fails. */
public record IceFailedEvent(
    String callId,
    String reason
) implements SipEvent {
    @Override public String method() { return "ICE-FAILED"; }
}
