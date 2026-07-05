package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.SleeEvent;
import gov.nist.javax.sip.message.SIPMessage;

/** SLEE event carrying a parsed SIP message. */
public record SipRaEvent(SIPMessage sipMessage) implements SleeEvent {
    public boolean isRequest() {
        return sipMessage instanceof gov.nist.javax.sip.message.SIPRequest;
    }
    public boolean isResponse() {
        return sipMessage instanceof gov.nist.javax.sip.message.SIPResponse;
    }
}
