package com.microjainslee.ra.sipservlet.events;

/** SIP MESSAGE — RFC 3428 instant messaging / SMS-over-IP. */
public record SipMessageEvent(
    String callId,
    String fromUri,
    String toUri,
    String contentType,
    String body             // text/plain, application/vnd.3gpp.sms, etc.
) implements SipEvent {
    @Override public String method() { return "MESSAGE"; }
}
