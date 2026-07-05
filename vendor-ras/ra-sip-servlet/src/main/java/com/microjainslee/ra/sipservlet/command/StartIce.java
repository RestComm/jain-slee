package com.microjainslee.ra.sipservlet.command;

/** Request the RA to begin ICE candidate gathering for a call. */
public record StartIce(String callId) implements SipOutboundCommand {}
