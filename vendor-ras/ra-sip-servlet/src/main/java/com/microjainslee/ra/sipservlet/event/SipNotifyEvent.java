package com.microjainslee.ra.sipservlet.event;

/** SIP NOTIFY — RFC 6665 event notification. */
public record SipNotifyEvent(
    String callId,
    String fromUri,
    String toUri,
    String eventType,
    String subscriptionState,   // "active", "pending", "terminated;reason=..."
    String body                  // application/pidf+xml, application/dialog-info+xml, etc.
) implements SipEvent {
    @Override public String method() { return "NOTIFY"; }
}
