package com.microjainslee.ra.sipservlet.command;

/** Cancel a pending INVITE before it receives a final response. */
public record SendCancel(String callId) implements SipOutboundCommand {}
