package com.microjainslee.ra.sipservlet.command;

/** Toggle media keep-alive for an established call (RFC 5626 CRLF pings). */
public record SendMediaKeepAlive(String callId, boolean enable) implements SipOutboundCommand {}
