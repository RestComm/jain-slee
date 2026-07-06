package com.microjainslee.ra.sipservlet.events;

/** SIP OPTIONS event — capability query. */
public record SipOptionsEvent(String callId) implements SipEvent {
    @Override public String method() { return "OPTIONS"; }
}
