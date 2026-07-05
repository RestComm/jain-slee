package com.microjainslee.ra.sipservlet.command;

/** Select the optimal ICE candidate pair for a call. */
public record SelectIceCandidate(String callId, String address, int port, String type)
        implements SipOutboundCommand {}
