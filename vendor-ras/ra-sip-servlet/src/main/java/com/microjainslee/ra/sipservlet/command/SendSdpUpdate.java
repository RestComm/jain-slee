package com.microjainslee.ra.sipservlet.command;

/** Push an updated SDP (e.g., after ICE candidate selection) to the remote party. */
public record SendSdpUpdate(String callId, String sdp) implements SipOutboundCommand {}
