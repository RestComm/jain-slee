package com.microjainslee.ra.sipservlet.command;

/** Send an ACK to complete a 3-way handshake after a final response. */
public record SendAck(String callId) implements SipOutboundCommand {}
