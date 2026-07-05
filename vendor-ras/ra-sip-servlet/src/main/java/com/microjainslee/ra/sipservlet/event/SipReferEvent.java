package com.microjainslee.ra.sipservlet.event;

/** SIP REFER — RFC 3515 call transfer. */
public record SipReferEvent(
    String callId,
    String fromUri,
    String toUri,
    String referToUri         // Refer-To header — transfer target
) implements SipEvent {
    @Override public String method() { return "REFER"; }
}
