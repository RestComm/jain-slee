package com.microjainslee.ra.sipservlet.event;

/** SIP PRACK — RFC 3262 Provisional Response Acknowledgement. */
public record SipPrackEvent(
    String callId,
    String rackNumber,       // RSeq from 1xx response
    String rackMethod        // INVITE, UPDATE
) implements SipEvent {
    @Override public String method() { return "PRACK"; }
}
