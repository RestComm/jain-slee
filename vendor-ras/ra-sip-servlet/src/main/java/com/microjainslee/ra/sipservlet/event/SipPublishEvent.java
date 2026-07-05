package com.microjainslee.ra.sipservlet.event;

/** SIP PUBLISH — RFC 3903 presence publication. */
public record SipPublishEvent(
    String callId,
    String fromUri,
    String eventType,       // "presence"
    int expires,
    String body             // application/pidf+xml
) implements SipEvent {
    @Override public String method() { return "PUBLISH"; }
}
