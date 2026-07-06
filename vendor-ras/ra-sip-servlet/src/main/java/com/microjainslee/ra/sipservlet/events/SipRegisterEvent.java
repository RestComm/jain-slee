package com.microjainslee.ra.sipservlet.events;

/** SIP REGISTER event — UA registration request. */
public record SipRegisterEvent(
    String callId,
    String fromUri,
    String toUri,
    String contactUri,     // Contact: <sip:alice@192.168.1.5:5060>
    int expires            // Expires header (seconds)
) implements SipEvent {
    @Override public String method() { return "REGISTER"; }
}
