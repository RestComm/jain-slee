package com.microjainslee.ra.sipservlet.event;

/** SIP BYE event fired when a dialog is terminated. */
public record SipByeEvent(String callId) implements SipEvent {
    @Override public String method() { return "BYE"; }
}
