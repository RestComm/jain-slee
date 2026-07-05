package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.event.SipEvent;

/**
 * Classifies a parsed SIP message into a typed {@link SipEvent}.
 * <p>Receives a JAIN-SIP {@code javax.sip.message.Message} (NIST
 * {@code SIPMessage} implements it) and returns the appropriate
 * sealed event subtype, or {@code null} to silently drop.
 */
@FunctionalInterface
public interface SipEventClassifier {
    /** Classify a parsed SIP message.
     * @param msg    JAIN-SIP Message (usually NIST SIPMessage instance)
     * @param callId pre-extracted Call-ID value
     * @return typed event, or {@code null} to drop
     */
    SipEvent classify(Object msg, String callId);
}
