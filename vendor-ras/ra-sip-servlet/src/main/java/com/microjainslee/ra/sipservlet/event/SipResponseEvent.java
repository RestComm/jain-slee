package com.microjainslee.ra.sipservlet.event;

/** SIP response event — any 1xx-6xx response to a previous request. */
public record SipResponseEvent(
        String callId,
        int statusCode,
        String reasonPhrase)
        implements SipEvent {

    @Override public String method() { return "RESPONSE"; }
}
