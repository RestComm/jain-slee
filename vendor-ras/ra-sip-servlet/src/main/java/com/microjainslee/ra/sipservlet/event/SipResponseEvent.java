package com.microjainslee.ra.sipservlet.event;

import java.util.List;

/** SIP response event — any 1xx-6xx response to a previous request. */
public record SipResponseEvent(
    String callId,
    int statusCode,
    String reasonPhrase,
    String sdpBody,              // SDP from 200 OK (contains remote candidates)
    String contentType,
    List<String> viaHeaders
) implements SipEvent {
    @Override public String method() { return "RESPONSE"; }
    public boolean isSuccess() { return statusCode >= 200 && statusCode < 300; }
    public boolean isFinal() { return statusCode >= 200; }
    public boolean isProvisional() { return statusCode >= 100 && statusCode < 200; }
}
