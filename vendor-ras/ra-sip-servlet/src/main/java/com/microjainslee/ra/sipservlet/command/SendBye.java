package com.microjainslee.ra.sipservlet.command;

/** Send a BYE request to terminate a dialog. */
public record SendBye(String callId) implements SipOutboundCommand {}
