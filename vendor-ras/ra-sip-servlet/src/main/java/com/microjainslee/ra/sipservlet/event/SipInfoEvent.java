package com.microjainslee.ra.sipservlet.event;

/** SIP INFO — RFC 6086 mid-dialog signaling (DTMF, ISUP, etc.). */
public record SipInfoEvent(
    String callId,
    String fromUri,
    String toUri,
    String contentType,
    String body             // application/dtmf-relay, application/isup, etc.
) implements SipEvent {
    @Override public String method() { return "INFO"; }
}
