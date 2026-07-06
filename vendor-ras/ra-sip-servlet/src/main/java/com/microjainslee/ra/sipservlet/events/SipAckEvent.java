package com.microjainslee.ra.sipservlet.events;

/** SIP ACK event — confirms a final response to INVITE. */
public record SipAckEvent(String callId) implements SipEvent {
    @Override public String method() { return "ACK"; }
}
