package com.microjainslee.ra.sipservlet.event;

import java.util.List;

/** SIP INVITE event fired when a new dialog is initiated. */
public record SipInviteEvent(
    String callId,
    String fromUri,       // From header
    String toUri,         // To header
    String contactUri,    // Contact header
    List<String> viaHeaders,     // Via headers (list for proxy chains)
    List<String> recordRoute,    // Record-Route headers  
    List<String> route,          // Route headers
    String sdpBody,              // SDP body (for ICE candidates)
    String contentType           // Content-Type
) implements SipEvent {
    @Override public String method() { return "INVITE"; }
}
