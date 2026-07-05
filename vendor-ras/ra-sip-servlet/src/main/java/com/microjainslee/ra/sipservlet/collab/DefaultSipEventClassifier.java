package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.event.*;
import javax.sip.header.*;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Default classifier — inspects SIP method/status and creates typed {@link SipEvent}.
 * <p>Uses only JAIN-SIP API ({@code javax.sip.*}) for header extraction.
 * The only NIST-specific call is {@code StringMsgParser.parseSIPMessage()}
 * in {@code SipServletResourceAdaptor.onRawMessage()}, which returns NIST
 * {@code SIPMessage} — but that class implements {@code javax.sip.message.Message},
 * so all downstream code uses the standard JAIN-SIP interfaces.
 */
public final class DefaultSipEventClassifier implements SipEventClassifier {

    private static final Logger LOG = LogManager.getLogger(DefaultSipEventClassifier.class);

    @Override
    public SipEvent classify(Object msg, String callId) {
        if (msg instanceof Request req) {
            return classifyRequest(req, callId);
        } else if (msg instanceof Response resp) {
            return classifyResponse(resp, callId);
        }
        LOG.warn("Unknown SIP message type: {}", msg.getClass().getName());
        return null;
    }

    private SipEvent classifyRequest(Request req, String callId) {
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

    private SipEvent classifyResponse(Response resp, String callId) {
        return new SipResponseEvent(callId,
                resp.getStatusCode(), resp.getReasonPhrase(),
                extractBody(resp), extractContentType(resp),
                extractViaHeaders(resp));
    }

    // --- Header extraction (all javax.sip.* API, no NIST internals) ---

    @SuppressWarnings("unchecked")
    private String extractFrom(Message msg) {
        FromHeader h = (FromHeader) msg.getHeader(FromHeader.NAME);
        return h != null ? h.toString() : "";
    }

    @SuppressWarnings("unchecked")
    private String extractTo(Message msg) {
        ToHeader h = (ToHeader) msg.getHeader(ToHeader.NAME);
        return h != null ? h.toString() : "";
    }

    @SuppressWarnings("unchecked")
    private String extractContact(Message msg) {
        ContactHeader h = (ContactHeader) msg.getHeader(ContactHeader.NAME);
        return h != null ? h.toString() : "";
    }

    @SuppressWarnings("unchecked")
    private List<String> extractViaHeaders(Message msg) {
        List<String> result = new ArrayList<>();
        ListIterator it = msg.getHeaders(ViaHeader.NAME);
        while (it != null && it.hasNext()) {
            ViaHeader via = (ViaHeader) it.next();
            result.add("SIP/2.0/" + via.getTransport().toUpperCase()
                    + " " + via.getHost() + ":" + via.getPort()
                    + ";branch=" + via.getBranch());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRecordRoute(Message msg) {
        List<String> result = new ArrayList<>();
        ListIterator it = msg.getHeaders(RecordRouteHeader.NAME);
        while (it != null && it.hasNext()) result.add(it.next().toString());
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoute(Message msg) {
        List<String> result = new ArrayList<>();
        ListIterator it = msg.getHeaders(RouteHeader.NAME);
        while (it != null && it.hasNext()) result.add(it.next().toString());
        return result;
    }

    private String extractBody(Message msg) {
        try {
            Object body = msg.getContent();
            if (body instanceof String s) return s;
            if (body instanceof byte[] b) return new String(b);
            return body != null ? body.toString() : "";
        } catch (Exception e) {
            LOG.debug("Failed to extract body content: {}", e.getMessage());
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractContentType(Message msg) {
        ContentTypeHeader h = (ContentTypeHeader) msg.getHeader(ContentTypeHeader.NAME);
        return h != null ? h.toString().trim() : "";
    }

    @SuppressWarnings("unchecked")
    private int extractExpires(Message msg) {
        ExpiresHeader h = (ExpiresHeader) msg.getHeader(ExpiresHeader.NAME);
        if (h != null) {
            try { return h.getExpires(); }
            catch (Exception ignored) { }
        }
        return 3600;
    }
}
