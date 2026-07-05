package com.microjainslee.ra.sipservlet.command;

/** Send an INVITE request — SBB provides AoR, RA resolves DNS. */
public record SendInvite(String callId, String toUri, String fromUri, String sdp)
        implements SipOutboundCommand {}
