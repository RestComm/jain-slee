package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.event.*;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default classifier that inspects the SIP method / status line and
 * creates the appropriate typed {@link SipEvent}.
 */
public final class DefaultSipEventClassifier implements SipEventClassifier {

    private static final Logger LOG = LogManager.getLogger(DefaultSipEventClassifier.class);

    @Override
    public SipEvent classify(SIPMessage msg, String callId) {
        if (msg instanceof SIPRequest req) {
            return classifyRequest(req, callId);
        } else if (msg instanceof SIPResponse resp) {
            return classifyResponse(resp, callId);
        }
        LOG.warn("Unknown SIP message type: {}", msg.getClass().getName());
        return null;
    }

    private SipEvent classifyRequest(SIPRequest req, String callId) {
        String method = req.getMethod();
        if (method == null) return null;
        return switch (method.toUpperCase()) {
            case "INVITE" -> new SipInviteEvent(callId,
                    headerValue(req, "From"),
                    headerValue(req, "To"),
                    bodyContent(req));
            case "BYE"    -> new SipByeEvent(callId);
            case "ACK"    -> new SipAckEvent(callId);
            case "CANCEL" -> new SipCancelEvent(callId);
            case "REGISTER" -> new SipRegisterEvent(callId,
                    headerValue(req, "From"),
                    headerValue(req, "To"),
                    extractExpires(req));
            case "OPTIONS"  -> new SipOptionsEvent(callId);
            default -> {
                LOG.debug("Unhandled SIP method: {}", method);
                yield null;
            }
        };
    }

    private SipEvent classifyResponse(SIPResponse resp, String callId) {
        return new SipResponseEvent(callId,
                resp.getStatusCode(),
                resp.getReasonPhrase());
    }

    private static String headerValue(SIPRequest req, String name) {
        var hdr = req.getHeader(name);
        return hdr != null ? hdr.toString() : "";
    }

    private static String bodyContent(SIPRequest req) {
        try {
            Object body = req.getMessageContent();
            if (body instanceof String s) return s;
            if (body instanceof byte[] b) return new String(b);
            return body != null ? body.toString() : "";
        } catch (Exception e) {
            LOG.debug("Failed to extract body content: {}", e.getMessage());
            return "";
        }
    }

    private static int extractExpires(SIPRequest req) {
        var hdr = req.getHeader("Expires");
        if (hdr != null) {
            try { return Integer.parseInt(hdr.toString().trim()); }
            catch (NumberFormatException ignored) { }
        }
        return 3600;
    }
}
