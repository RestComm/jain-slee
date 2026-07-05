package com.microjainslee.ra.sipservlet.command;

/** Send a SIP response (1xx-6xx) back to the originator. */
public record SendResponse(String callId, int statusCode, String reason)
        implements SipOutboundCommand {}
