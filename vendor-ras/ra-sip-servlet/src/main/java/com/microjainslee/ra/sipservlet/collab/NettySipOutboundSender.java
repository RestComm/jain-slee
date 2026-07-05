/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import com.microjainslee.ra.sipservlet.command.SendAck;
import com.microjainslee.ra.sipservlet.command.SendBye;
import com.microjainslee.ra.sipservlet.command.SendCancel;
import com.microjainslee.ra.sipservlet.command.SendInvite;
import com.microjainslee.ra.sipservlet.command.SendResponse;
import com.microjainslee.ra.sipservlet.command.SendSdpUpdate;
import com.microjainslee.ra.sipservlet.command.SipOutboundCommand;
import com.microjainslee.ra.sipservlet.transport.SipTransport;

import gov.nist.javax.sip.address.AddressFactoryImpl;
import gov.nist.javax.sip.header.HeaderFactoryImpl;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Default {@link SipOutboundSender} — encodes SIP messages with the NIST
 * (JAIN-SIP RI) message classes and writes them through the RA's Netty
 * transports.
 *
 * <p>Responses are derived from the dialog's last inbound request and sent
 * back to the peer address / transport the request arrived on
 * (RFC 3261 §18.2.2). In-dialog requests (BYE, ACK, re-INVITE SDP update)
 * are built by reversing the stored request's From/To and targeting its
 * Contact. Out-of-dialog INVITEs are built from scratch and resolved via
 * the request-URI host/port.
 *
 * <p>ICE/keep-alive commands are handled by the RA itself and never reach
 * this sender.
 */
public final class NettySipOutboundSender implements SipOutboundSender {

    private static final Logger LOG = LogManager.getLogger(NettySipOutboundSender.class);

    private final SipRaConfig config;
    private final DialogRegistry dialogs;
    /** protocol name (upper case) → transport. */
    private final Map<String, SipTransport> transports;
    /** callId → locally generated To-tag (stable per dialog). */
    private final Map<String, String> localTags = new ConcurrentHashMap<>();

    private final MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    private final HeaderFactoryImpl headerFactory = new HeaderFactoryImpl();
    private final AddressFactoryImpl addressFactory = new AddressFactoryImpl();

    public NettySipOutboundSender(SipRaConfig config, DialogRegistry dialogs,
                                  Map<String, SipTransport> transports) {
        this.config = config;
        this.dialogs = dialogs;
        this.transports = transports;
    }

    /** Called by DialogRegistry owners when a dialog is torn down. */
    public void forgetDialog(String callId) {
        localTags.remove(callId);
    }

    @Override
    public void send(SipOutboundCommand cmd) {
        try {
            switch (cmd) {
                case SendResponse c  -> sendResponse(c.callId(), c.statusCode(), c.reason(), null);
                case SendSdpUpdate c -> sendResponse(c.callId(), 200, "OK", c.sdp());
                case SendBye c       -> sendInDialogRequest(c.callId(), Request.BYE);
                case SendAck c       -> sendAck(c.callId());
                case SendCancel c    -> sendCancel(c.callId());
                case SendInvite c    -> sendInvite(c);
                default -> LOG.warn("[sip-out] unsupported command {} — ignored",
                        cmd.getClass().getSimpleName());
            }
        } catch (Exception e) {
            LOG.error("[sip-out] failed to send {} for callId={}",
                    cmd.getClass().getSimpleName(), cmd.callId(), e);
        }
    }

    // ── responses ──────────────────────────────────────────────────

    private void sendResponse(String callId, int status, String reason, String sdp)
            throws Exception {
        DialogRegistry.Dialog dialog = dialogs.find(callId);
        if (dialog == null || dialog.lastRequest() == null) {
            LOG.warn("[sip-out] no dialog/request state for callId={} — cannot respond", callId);
            return;
        }
        SIPRequest request = dialog.lastRequest();
        SIPResponse response = request.createResponse(status,
                reason != null ? reason : SIPResponse.getReasonPhrase(status));
        ToHeader to = (ToHeader) response.getHeader(ToHeader.NAME);
        if (to != null && to.getTag() == null && status > 100) {
            to.setTag(localTag(callId));
        }
        if (status / 100 == 2 && needsContact(request.getMethod())) {
            response.setHeader(localContact(dialog.transport()));
        }
        if (sdp != null && !sdp.isEmpty()) {
            ContentTypeHeader ct = headerFactory.createContentTypeHeader("application", "sdp");
            response.setContent(sdp.getBytes(StandardCharsets.UTF_8), ct);
        }
        transmit(response, dialog.transport(), dialog.peer());
    }

    // ── in-dialog requests ─────────────────────────────────────────

    private void sendInDialogRequest(String callId, String method) throws Exception {
        DialogRegistry.Dialog dialog = dialogs.find(callId);
        if (dialog == null || dialog.lastRequest() == null) {
            LOG.warn("[sip-out] no dialog state for callId={} — cannot send {}", callId, method);
            return;
        }
        SIPRequest request = buildReversedRequest(dialog, method);
        transmit(request, dialog.transport(), dialog.peer());
    }

    /**
     * Build an in-dialog request from the UAS side: From/To are the
     * reverse of the stored inbound request, the request-URI targets its
     * Contact (fall back to the original From address), and a fresh local
     * Via/branch replaces the inbound Via stack.
     */
    private SIPRequest buildReversedRequest(DialogRegistry.Dialog dialog, String method)
            throws Exception {
        SIPRequest orig = dialog.lastRequest();
        FromHeader origFrom = (FromHeader) orig.getHeader(FromHeader.NAME);
        ToHeader origTo = (ToHeader) orig.getHeader(ToHeader.NAME);

        URI requestUri;
        ContactHeader origContact = (ContactHeader) orig.getHeader(ContactHeader.NAME);
        if (origContact != null && origContact.getAddress() != null) {
            requestUri = (URI) origContact.getAddress().getURI().clone();
        } else {
            requestUri = (URI) origFrom.getAddress().getURI().clone();
        }

        CallIdHeader callIdHeader = headerFactory.createCallIdHeader(dialog.callId);
        CSeqHeader cseq = headerFactory.createCSeqHeader(dialog.nextCseq(), method);
        FromHeader from = headerFactory.createFromHeader(
                origTo.getAddress(),
                origTo.getTag() != null ? origTo.getTag() : localTag(dialog.callId));
        ToHeader to = headerFactory.createToHeader(origFrom.getAddress(), origFrom.getTag());
        MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
        List<ViaHeader> vias = new ArrayList<>(1);
        vias.add(localVia(dialog.transport()));

        SIPRequest request = (SIPRequest) messageFactory.createRequest(
                requestUri, method, callIdHeader, cseq, from, to, vias, maxForwards);
        request.setHeader(localContact(dialog.transport()));
        return request;
    }

    private void sendAck(String callId) throws Exception {
        DialogRegistry.Dialog dialog = dialogs.find(callId);
        if (dialog == null) {
            LOG.warn("[sip-out] no dialog state for callId={} — cannot ACK", callId);
            return;
        }
        SIPResponse response = dialog.lastResponse();
        if (response == null) {
            // Nothing to acknowledge — an ACK outside a UAC INVITE
            // transaction is meaningless; RFC 3261 §13.2.2.4.
            LOG.warn("[sip-out] callId={} has no response to ACK", callId);
            return;
        }
        // ACK for a 2xx: new transaction, CSeq number of the INVITE with
        // method ACK, request-URI from the response Contact.
        ContactHeader contact = (ContactHeader) response.getHeader(ContactHeader.NAME);
        URI requestUri = contact != null && contact.getAddress() != null
                ? (URI) contact.getAddress().getURI().clone()
                : (URI) ((ToHeader) response.getHeader(ToHeader.NAME)).getAddress().getURI().clone();
        CallIdHeader callIdHeader = headerFactory.createCallIdHeader(callId);
        CSeqHeader inviteCseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
        CSeqHeader cseq = headerFactory.createCSeqHeader(
                inviteCseq != null ? inviteCseq.getSeqNumber() : 1L, Request.ACK);
        FromHeader from = (FromHeader) response.getHeader(FromHeader.NAME);
        ToHeader to = (ToHeader) response.getHeader(ToHeader.NAME);
        MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
        List<ViaHeader> vias = new ArrayList<>(1);
        vias.add(localVia(dialog.transport()));

        SIPRequest ack = (SIPRequest) messageFactory.createRequest(
                requestUri, Request.ACK, callIdHeader, cseq, from, to, vias, maxForwards);
        transmit(ack, dialog.transport(), dialog.peer());
    }

    private void sendCancel(String callId) throws Exception {
        DialogRegistry.Dialog dialog = dialogs.find(callId);
        if (dialog == null || dialog.lastRequest() == null) {
            LOG.warn("[sip-out] no dialog state for callId={} — cannot CANCEL", callId);
            return;
        }
        SIPRequest cancel = dialog.lastRequest().createCancelRequest();
        transmit(cancel, dialog.transport(), dialog.peer());
    }

    // ── out-of-dialog INVITE ───────────────────────────────────────

    private void sendInvite(SendInvite cmd) throws Exception {
        URI requestUri = addressFactory.createURI(cmd.toUri());
        if (!(requestUri instanceof SipURI target)) {
            LOG.warn("[sip-out] SendInvite target is not a SIP URI: {}", cmd.toUri());
            return;
        }
        String transport = target.getTransportParam() != null
                ? target.getTransportParam().toUpperCase(Locale.ROOT) : "UDP";

        Address toAddress = addressFactory.createAddress(requestUri);
        Address fromAddress = addressFactory.createAddress(
                addressFactory.createURI(cmd.fromUri()));

        CallIdHeader callIdHeader = headerFactory.createCallIdHeader(cmd.callId());
        CSeqHeader cseq = headerFactory.createCSeqHeader(1L, Request.INVITE);
        FromHeader from = headerFactory.createFromHeader(fromAddress, newTag());
        ToHeader to = headerFactory.createToHeader(toAddress, null);
        MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
        List<ViaHeader> vias = new ArrayList<>(1);
        vias.add(localVia(transport));

        SIPRequest invite;
        if (cmd.sdp() != null && !cmd.sdp().isEmpty()) {
            ContentTypeHeader ct = headerFactory.createContentTypeHeader("application", "sdp");
            invite = (SIPRequest) messageFactory.createRequest(requestUri, Request.INVITE,
                    callIdHeader, cseq, from, to, vias, maxForwards, ct,
                    cmd.sdp().getBytes(StandardCharsets.UTF_8));
        } else {
            invite = (SIPRequest) messageFactory.createRequest(requestUri, Request.INVITE,
                    callIdHeader, cseq, from, to, vias, maxForwards);
        }
        invite.setHeader(localContact(transport));

        int port = target.getPort() > 0 ? target.getPort() : 5060;
        InetSocketAddress peer =
                new InetSocketAddress(InetAddress.getByName(target.getHost()), port);
        transmit(invite, transport, peer);
    }

    // ── wire helpers ───────────────────────────────────────────────

    private void transmit(SIPMessage message, String transport, InetSocketAddress peer)
            throws Exception {
        if (peer == null) {
            LOG.warn("[sip-out] no peer address for callId={} — dropping {}",
                    message.getCallId() != null ? message.getCallId().getCallId() : "?",
                    message.getFirstLine() == null ? "?" : message.getFirstLine().trim());
            return;
        }
        String proto = transport != null ? transport.toUpperCase(Locale.ROOT) : "UDP";
        SipTransport sipTransport = transports.get(proto);
        if (sipTransport == null) {
            // Fall back to any running transport rather than silently dropping.
            sipTransport = transports.values().stream().findFirst().orElse(null);
        }
        if (sipTransport == null) {
            LOG.error("[sip-out] no transport available for {} — dropping", proto);
            return;
        }
        byte[] wire = message.encodeAsBytes(proto);
        if (sipTransport.send(wire, peer)) {
            LOG.debug("[sip-out] {} {} bytes → {} via {}",
                    message.getFirstLine() == null ? "?" : message.getFirstLine().trim(),
                    wire.length, peer, proto);
        }
    }

    private ViaHeader localVia(String transport) throws Exception {
        String proto = transport != null ? transport.toUpperCase(Locale.ROOT) : "UDP";
        ViaHeader via = headerFactory.createViaHeader(
                localHost(), localPort(proto), proto, "z9hG4bK-" + newTag());
        return via;
    }

    private ContactHeader localContact(String transport) throws Exception {
        String proto = transport != null ? transport.toUpperCase(Locale.ROOT) : "UDP";
        SipURI uri = addressFactory.createSipURI(config.contactUser(), localHost());
        uri.setPort(localPort(proto));
        if (!"UDP".equals(proto)) {
            uri.setTransportParam(proto.toLowerCase(Locale.ROOT));
        }
        return headerFactory.createContactHeader(addressFactory.createAddress(uri));
    }

    private String localHost() {
        String host = config.host();
        if (host == null || host.isEmpty() || "0.0.0.0".equals(host) || "::".equals(host)) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                return "127.0.0.1";
            }
        }
        return host;
    }

    private int localPort(String proto) {
        return switch (proto) {
            case "TCP" -> config.tcpPort() > 0 ? config.tcpPort() : 5060;
            case "TLS" -> config.tlsPort() > 0 ? config.tlsPort() : 5061;
            case "SCTP" -> config.sctpPort() > 0 ? config.sctpPort() : 5060;
            default -> config.udpPort() > 0 ? config.udpPort() : 5060;
        };
    }

    private static boolean needsContact(String method) {
        return Request.INVITE.equals(method) || Request.SUBSCRIBE.equals(method)
                || Request.REGISTER.equals(method) || Request.REFER.equals(method);
    }

    private String localTag(String callId) {
        return localTags.computeIfAbsent(callId, id -> newTag());
    }

    private static String newTag() {
        return Long.toHexString(ThreadLocalRandom.current().nextLong() & 0x7fffffffffffffffL);
    }
}
