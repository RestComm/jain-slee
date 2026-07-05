package com.microjainslee.ra.sipservlet.event;

/** SIP CANCEL event — cancels a pending INVITE. */
public record SipCancelEvent(String callId) implements SipEvent {
    @Override public String method() { return "CANCEL"; }
}
