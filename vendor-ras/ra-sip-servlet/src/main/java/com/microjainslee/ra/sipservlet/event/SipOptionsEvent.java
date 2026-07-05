package com.microjainslee.ra.sipservlet.event;

/** SIP OPTIONS event — capability query. */
public record SipOptionsEvent(String callId) implements SipEvent {
    @Override public String method() { return "OPTIONS"; }
}
