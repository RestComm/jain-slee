package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.event.SipEvent;
import gov.nist.javax.sip.message.SIPMessage;

/**
 * Classifies a raw parsed {@link SIPMessage} into a typed {@link SipEvent}.
 * Injected at wiring time so applications can customize event creation.
 */
@FunctionalInterface
public interface SipEventClassifier {
    /**
     * Classify a raw SIP message.
     *
     * @param msg    the parsed SIP message (request or response)
     * @param callId pre-extracted Call-ID header value
     * @return the typed event, or {@code null} to silently drop the message
     */
    SipEvent classify(SIPMessage msg, String callId);
}
