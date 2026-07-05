package com.microjainslee.ra.sipservlet.event;

/** SIP INVITE event fired when a new dialog is initiated. */
public record SipInviteEvent(
        String callId,
        String fromUri,
        String toUri,
        String sdp)
        implements SipEvent {

    @Override public String method() { return "INVITE"; }
}
