package com.microjainslee.ra.sipservlet.event;

/** SIP REGISTER event — UA registration request. */
public record SipRegisterEvent(
        String callId,
        String fromUri,
        String toUri,
        int expires)
        implements SipEvent {

    @Override public String method() { return "REGISTER"; }
}
