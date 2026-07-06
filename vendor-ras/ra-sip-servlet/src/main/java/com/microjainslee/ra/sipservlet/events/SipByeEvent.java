package com.microjainslee.ra.sipservlet.events;

/** SIP BYE event fired when a dialog is terminated. */
public record SipByeEvent(String callId) implements SipEvent {
    @Override public String method() { return "BYE"; }
}
