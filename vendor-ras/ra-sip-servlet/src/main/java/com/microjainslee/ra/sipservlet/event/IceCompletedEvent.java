package com.microjainslee.ra.sipservlet.event;

/** Fired when ICE negotiation completes successfully. */
public record IceCompletedEvent(
    String callId,
    String localAddress, int localPort,
    String remoteAddress, int remotePort
) implements SipEvent {
    @Override public String method() { return "ICE-COMPLETED"; }
}
