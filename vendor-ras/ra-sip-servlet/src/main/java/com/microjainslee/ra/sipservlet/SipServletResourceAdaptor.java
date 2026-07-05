/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.ra.sipservlet.collab.*;
import com.microjainslee.ra.sipservlet.command.SipOutboundCommand;
import com.microjainslee.ra.sipservlet.dns.DnsResolver;
import com.microjainslee.ra.sipservlet.event.SipEvent;
import com.microjainslee.ra.sipservlet.stun.IceCandidateCollector;
import com.microjainslee.ra.sipservlet.stun.StunClient;
import com.microjainslee.ra.sipservlet.transport.*;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.parser.StringMsgParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SIP-Servlet Resource Adaptor — Netty transports + 3-port contract.
 *
 * <p>Lifecycle: raConfigure → raActive → (process SIP) → raInactive → raUnconfigure.</p>
 *
 * <p>Events are typed via {@link SipEventClassifier} and fired into the SLEE
 * through {@link RaBootstrapPort#fireEvent}. Outbound commands come in
 * through {@link #sendOutbound} and are dispatched via
 * {@link SipOutboundSender}.</p>
 */
public final class SipServletResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(SipServletResourceAdaptor.class);

    private SipRaConfig config = new SipRaConfig();
    private final List<SipTransport> transports = new ArrayList<>(3);
    private RaBootstrapPort bootstrapPort;
    private SipEventClassifier classifier = new DefaultSipEventClassifier();
    private SipOutboundSender outboundSender;
    private final Map<String, ActivityHandle> dialogs = new ConcurrentHashMap<>();
    private final AtomicBoolean active = new AtomicBoolean(false);

    // ---- DNS / STUN / ICE ----
    private DnsResolver dnsResolver;
    private StunClient stunClient;
    private IceCandidateCollector iceCollector;

    // ---- collaborator injection ----

    public void setBootstrapPort(RaBootstrapPort bp) {
        this.bootstrapPort = bp;
    }

    public void setConfig(SipRaConfig c) {
        this.config = c;
    }

    public void setClassifier(SipEventClassifier c) {
        this.classifier = c;
    }

    public void setOutboundSender(SipOutboundSender s) {
        this.outboundSender = s;
    }

    // ---- accessors ----

    public SipRaConfig config() { return config; }
    public boolean isActive() { return active.get(); }

    // ---- Lifecycle ----

    public void raConfigure() {
        LOG.info("[ra-sip-servlet] raConfigure host={} tcp={} udp={} sctp={} client={}",
                config.host(), config.tcpPort(), config.udpPort(),
                config.sctpPort(), config.clientEnabled());
    }

    public void raActive() {
        if (!active.compareAndSet(false, true)) return;
        if (config.tcpPort() > 0)
            transports.add(new TcpTransport(config, this::onRawMessage));
        if (config.udpPort() > 0)
            transports.add(new UdpTransport(config, this::onRawMessage));
        if (config.sctpPort() > 0)
            transports.add(new SctpTransport(config, this::onRawMessage));
        if (config.tlsPort() > 0)
            transports.add(new TlsTransport(config, this::onRawMessage));
        transports.forEach(SipTransport::start);
        // DNS resolver
        if (config.dnsEnabled()) {
            dnsResolver = new DnsResolver(true, config.dnsCacheTtlSecs());
        }
        // STUN client + ICE collector
        if (config.iceEnabled() && config.stunServer() != null
                && !config.stunServer().isBlank()) {
            stunClient = new StunClient(config.stunServer(), config.stunPort());
            iceCollector = new IceCandidateCollector(stunClient);
            iceCollector.setBootstrapPort(bootstrapPort);
            stunClient.startKeepAlive(config.iceKeepAliveSecs());
            LOG.info("[ra-sip-servlet] STUN client started server={}",
                    config.stunServer());
        }
        LOG.info("[ra-sip-servlet] ACTIVE transports={}", transports.size());
    }

    public void raInactive() {
        if (!active.compareAndSet(true, false)) return;
        transports.forEach(SipTransport::stop);
        transports.clear();
        dialogs.clear();
        if (stunClient != null) { stunClient.close(); stunClient = null; }
        if (dnsResolver != null) { dnsResolver.clearCache(); dnsResolver = null; }
        iceCollector = null;
        LOG.info("[ra-sip-servlet] INACTIVE");
    }

    public void raUnconfigure() {
        raInactive();
        LOG.info("[ra-sip-servlet] UNCONFIGURED");
    }

    // ---- outbound (SBB → RA) ----

    /**
     * Called by {@code SipServletRaEndpoint.sendCommand} when an SBB
     * sends an outbound SIP command.
     */
    public void sendOutbound(SipOutboundCommand cmd) {
        if (outboundSender != null) {
            outboundSender.send(cmd);
        } else {
            LOG.warn("[ra-sip-servlet] Outbound sender not configured, dropping: {}",
                    cmd.getClass().getSimpleName());
        }
    }

    // ---- inbound (transport → SLEE) ----

    private void onRawMessage(byte[] raw) {
        if (!active.get()) return;
        try {
            StringMsgParser parser = new StringMsgParser();
            SIPMessage sipMsg = parser.parseSIPMessage(raw, true, false, null);
            if (sipMsg != null) onSipEvent(sipMsg);
        } catch (ParseException e) {
            LOG.warn("[ra-sip-servlet] Parse error", e);
        }
    }

    private void onSipEvent(SIPMessage msg) {
        if (bootstrapPort == null) {
            LOG.warn("[ra-sip-servlet] bootstrapPort not set, dropping message");
            return;
        }
        String callId = deriveCallId(msg);
        ActivityHandle handle = dialogs.computeIfAbsent(callId,
                id -> bootstrapPort.createActivityHandle(id));
        SipEvent event = classifier.classify(msg, callId);
        if (event != null) {
            bootstrapPort.fireEvent(event, handle, null);
        }
    }

    // ---- helpers ----

    /**
     * Extract Call-ID using javax.sip.* API (standard, not NIST-specific).
     * The NIST SIPMessage implements javax.sip.message.Message, so
     * {@code msg.getHeader(CallIdHeader.NAME)} works on all message types.
     */
    @SuppressWarnings("unchecked")
    private static String deriveCallId(SIPMessage msg) {
        javax.sip.header.CallIdHeader callIdHdr =
                (javax.sip.header.CallIdHeader) msg.getHeader(
                        javax.sip.header.CallIdHeader.NAME);
        if (callIdHdr != null) {
            String cid = callIdHdr.getCallId();
            if (cid != null && !cid.isBlank()) return cid;
        }
        return UUID.randomUUID().toString();
    }
}

