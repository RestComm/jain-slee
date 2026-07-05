package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.event.*;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

import java.util.ArrayList;
import java.util.List;

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
                    extractFrom(req), extractTo(req),
                    extractContact(req), extractViaHeaders(req),
                    extractRecordRoute(req), extractRoute(req),
                    extractBody(req), extractContentType(req));
            case "BYE"    -> new SipByeEvent(callId);
            case "ACK"    -> new SipAckEvent(callId);
            case "CANCEL" -> new SipCancelEvent(callId);
            case "REGISTER" -> new SipRegisterEvent(callId,
                    extractFrom(req), extractTo(req),
                    extractContact(req),
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
                resp.getStatusCode(), resp.getReasonPhrase(),
                extractBody(resp), extractContentType(resp),
                extractViaHeaders(resp));
    }

    // --- Header extraction helpers ---

    private String extractFrom(SIPMessage msg) {
        var hdr = msg.getHeader("From");
        return hdr != null ? hdr.toString() : "";
    }

    private String extractTo(SIPMessage msg) {
        var hdr = msg.getHeader("To");
        return hdr != null ? hdr.toString() : "";
    }

    private String extractContact(SIPMessage msg) {
        var hdr = msg.getHeader("Contact");
        return hdr != null ? hdr.toString() : "";
    }

    private List<String> extractViaHeaders(SIPMessage msg) {
        List<String> result = new ArrayList<>();
        var it = msg.getHeaders("Via");
        if (it != null) {
            while (it.hasNext()) {
                var via = it.next();
                if (via instanceof Via v) {
                    result.add("SIP/2.0/" + v.getTransport().toUpperCase()
                            + " " + v.getHost() + ":" + v.getPort()
                            + ";branch=" + v.getBranch());
                } else {
                    result.add(via.toString());
                }
            }
        }
        return result;
    }

    private List<String> extractRecordRoute(SIPMessage msg) {
        List<String> result = new ArrayList<>();
        var it = msg.getHeaders("Record-Route");
        if (it != null) {
            while (it.hasNext()) {
                result.add(it.next().toString());
            }
        }
        return result;
    }

    private List<String> extractRoute(SIPMessage msg) {
        List<String> result = new ArrayList<>();
        var it = msg.getHeaders("Route");
        if (it != null) {
            while (it.hasNext()) {
                result.add(it.next().toString());
            }
        }
        return result;
    }

    private String extractBody(SIPMessage msg) {
        try {
            Object body = msg.getMessageContent();
            if (body instanceof String s) return s;
            if (body instanceof byte[] b) return new String(b);
            return body != null ? body.toString() : "";
        } catch (Exception e) {
            LOG.debug("Failed to extract body content: {}", e.getMessage());
            return "";
        }
    }

    private String extractContentType(SIPMessage msg) {
        var hdr = msg.getHeader("Content-Type");
        return hdr != null ? hdr.toString().trim() : "";
    }

    private int extractExpires(SIPMessage msg) {
        var hdr = msg.getHeader("Expires");
        if (hdr != null) {
            try { return Integer.parseInt(hdr.toString().trim()); }
            catch (NumberFormatException ignored) { }
        }
        return 3600;
    }
}
