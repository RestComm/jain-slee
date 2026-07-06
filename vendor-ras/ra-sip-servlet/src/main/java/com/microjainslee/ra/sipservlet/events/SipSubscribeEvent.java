package com.microjainslee.ra.sipservlet.events;

import java.util.List;

/** SIP SUBSCRIBE — RFC 6665 event subscription (presence, dialog, reg). */
public record SipSubscribeEvent(
    String callId,
    String fromUri,
    String toUri,
    String eventType,           // "presence", "dialog", "reg", "refer"
    int expires,
    List<String> acceptHeaders
) implements SipEvent {
    @Override public String method() { return "SUBSCRIBE"; }
}
