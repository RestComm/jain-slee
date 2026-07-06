/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.ra.sipservlet.collab.*;
import com.microjainslee.ra.sipservlet.command.SelectIceCandidate;
import com.microjainslee.ra.sipservlet.command.SendMediaKeepAlive;
import com.microjainslee.ra.sipservlet.command.SipOutboundCommand;
import com.microjainslee.ra.sipservlet.command.StartIce;
import com.microjainslee.ra.sipservlet.dns.DnsResolver;
import com.microjainslee.ra.sipservlet.events.SipEvent;
import com.microjainslee.ra.sipservlet.stun.IceCandidateCollector;
import com.microjainslee.ra.sipservlet.stun.StunClient;
import com.microjainslee.ra.sipservlet.transport.*;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.parser.StringMsgParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SIP-Servlet Resource Adaptor — Netty transports + 3-port contract.
 *
 * <p>Lifecycle: raConfigure → raActive → (process SIP) → raInactive → raUnconfigure.</p>
 *
 * <p>Inbound: transports push {@code (bytes, peer, transport)} into
 * {@link #onRawMessage}; messages are parsed, recorded in the
 * {@link DialogRegistry}, classified into typed {@link SipEvent}s and fired
 * through {@link RaBootstrapPort#fireEvent}.</p>
 *
 * <p>Outbound: SBB commands arrive via {@link #sendOutbound}. ICE commands
 * are handled here; SIP commands go to the {@link SipOutboundSender} —
 * by default a {@link NettySipOutboundSender} wired over the same
 * transports, so the RA answers out of the box.</p>
 *
 * <p>Dialog lifecycle: dialogs end on BYE, on a final non-2xx response to
 * the initial INVITE, or after {@link SipRaConfig#dialogIdleSecs()} of
 * inactivity (periodic sweep). Ending a dialog releases the registry entry
 * and calls {@link RaBootstrapPort#endActivity} so attached SBB entities
 * are notified and the activity context is reclaimed — no leaks.</p>
 */
public final class SipServletResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(SipServletResourceAdaptor.class);

    private SipRaConfig config = new SipRaConfig();
    private final Map<String, SipTransport> transports = new ConcurrentHashMap<>();
    private RaBootstrapPort bootstrapPort;
    private SipEventClassifier classifier = new DefaultSipEventClassifier();
    private SipOutboundSender outboundSender;
    private NettySipOutboundSender defaultSender;
    private final DialogRegistry dialogRegistry = new DialogRegistry();
    private final Map<String, ActivityHandle> dialogs = new ConcurrentHashMap<>();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private ScheduledExecutorService sweeper;

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

    /** Override the default Netty-backed sender (tests, custom stacks). */
    public void setOutboundSender(SipOutboundSender s) {
        this.outboundSender = s;
    }

    // ---- accessors ----

    public SipRaConfig config() { return config; }
    public boolean isActive() { return active.get(); }
    public DialogRegistry dialogRegistry() { return dialogRegistry; }

    // ---- Lifecycle ----

    public void raConfigure() {
        LOG.info("[ra-sip-servlet] raConfigure host={} tcp={} udp={} sctp={} client={}",
                config.host(), config.tcpPort(), config.udpPort(),
                config.sctpPort(), config.clientEnabled());
    }

    public void raActive() {
        if (!active.compareAndSet(false, true)) return;
        if (config.tcpPort() > 0)
            registerTransport(new TcpTransport(config, this::onRawMessage));
        if (config.udpPort() > 0)
            registerTransport(new UdpTransport(config, this::onRawMessage));
        if (config.sctpPort() > 0)
            registerTransport(new SctpTransport(config, this::onRawMessage));
        if (config.tlsPort() > 0)
            registerTransport(new TlsTransport(config, this::onRawMessage));
        transports.values().forEach(SipTransport::start);

        // Default outbound path — SBB commands work with zero extra wiring.
        if (outboundSender == null) {
            defaultSender = new NettySipOutboundSender(config, dialogRegistry, transports);
            outboundSender = defaultSender;
            LOG.info("[ra-sip-servlet] default Netty outbound sender wired");
        }

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

        // Idle-dialog sweeper — dialogs abandoned without BYE must not leak.
        long sweepSecs = Math.max(1, config.dialogSweepIntervalSecs());
        sweeper = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "sip-ra-dialog-sweeper");
            t.setDaemon(true);
            return t;
        });
        sweeper.scheduleAtFixedRate(this::sweepIdleDialogs, sweepSecs, sweepSecs, TimeUnit.SECONDS);

        LOG.info("[ra-sip-servlet] ACTIVE transports={}", transports.size());
    }

    public void raInactive() {
        if (!active.compareAndSet(true, false)) return;
        if (sweeper != null) { sweeper.shutdownNow(); sweeper = null; }
        transports.values().forEach(SipTransport::stop);
        transports.clear();
        dialogs.clear();
        dialogRegistry.clear();
        if (outboundSender == defaultSender) { outboundSender = null; }
        defaultSender = null;
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
     * sends an outbound command. ICE commands are handled by the RA;
     * SIP commands are delegated to the outbound sender.
     */
    public void sendOutbound(SipOutboundCommand cmd) {
        if (cmd == null) return;
        switch (cmd) {
            case StartIce c -> startIce(c.callId());
            case SelectIceCandidate c ->
                    LOG.info("[ra-sip-servlet] ICE candidate selected callId={} {}:{} ({})",
                            c.callId(), c.address(), c.port(), c.type());
            case SendMediaKeepAlive c ->
                    LOG.info("[ra-sip-servlet] media keep-alive {} for callId={}",
                            c.enable() ? "ON" : "OFF", c.callId());
            default -> {
                if (outboundSender != null) {
                    outboundSender.send(cmd);
                } else {
                    LOG.warn("[ra-sip-servlet] Outbound sender not configured, dropping: {}",
                            cmd.getClass().getSimpleName());
                }
            }
        }
    }

    private void startIce(String callId) {
        IceCandidateCollector collector = this.iceCollector;
        if (collector == null) {
            LOG.warn("[ra-sip-servlet] StartIce for callId={} but ICE is disabled", callId);
            return;
        }
        collector.gatherAll().whenComplete((candidates, error) -> {
            if (error != null) {
                LOG.warn("[ra-sip-servlet] ICE gathering failed for callId={}", callId, error);
            } else {
                collector.fireCandidates(callId, candidates);
            }
        });
    }

    // ---- inbound (transport → SLEE) ----

    void onRawMessage(byte[] raw, InetSocketAddress peer, String transport) {
        if (!active.get()) return;
        try {
            StringMsgParser parser = new StringMsgParser();
            SIPMessage sipMsg = parser.parseSIPMessage(raw, true, false, null);
            if (sipMsg != null) onSipMessage(sipMsg, peer, transport);
        } catch (ParseException e) {
            LOG.warn("[ra-sip-servlet] Parse error from {}", peer, e);
        }
    }

    private void onSipMessage(SIPMessage msg, InetSocketAddress peer, String transport) {
        if (bootstrapPort == null) {
            LOG.warn("[ra-sip-servlet] bootstrapPort not set, dropping message");
            return;
        }
        String callId = deriveCallId(msg);
        ActivityHandle handle = dialogs.computeIfAbsent(callId,
                id -> bootstrapPort.createActivityHandle(id));
        dialogRegistry.recordInbound(callId, handle, msg, peer, transport);

        SipEvent event = classifier.classify(msg, callId);
        if (event != null) {
            bootstrapPort.fireEvent(event, handle, null);
        }

        if (isDialogTerminating(msg)) {
            // Give the SBB one event-loop turn to react to the BYE/final
            // response before the activity ends; endDialog fires the
            // ActivityEndedEvent through the same ordered path.
            endDialog(callId);
        }
    }

    /** BYE requests and final non-2xx INVITE responses terminate the dialog. */
    private static boolean isDialogTerminating(SIPMessage msg) {
        if (msg instanceof SIPRequest req) {
            return Request.BYE.equals(req.getMethod());
        }
        if (msg instanceof SIPResponse resp) {
            int status = resp.getStatusCode();
            CSeqHeader cseq = (CSeqHeader) resp.getHeader(CSeqHeader.NAME);
            String method = cseq != null ? cseq.getMethod() : null;
            if (Request.BYE.equals(method) && status >= 200) {
                return true;
            }
            return Request.INVITE.equals(method) && status >= 300;
        }
        return false;
    }

    /** Tear down all per-dialog state and end the SLEE activity. */
    public void endDialog(String callId) {
        if (callId == null) return;
        dialogRegistry.remove(callId);
        if (defaultSender != null) {
            defaultSender.forgetDialog(callId);
        }
        ActivityHandle handle = dialogs.remove(callId);
        if (handle != null && bootstrapPort != null) {
            bootstrapPort.endActivity(handle);
            LOG.debug("[ra-sip-servlet] dialog ended callId={}", callId);
        }
    }

    private void sweepIdleDialogs() {
        try {
            long idleMillis = TimeUnit.SECONDS.toMillis(Math.max(1, config.dialogIdleSecs()));
            for (DialogRegistry.Dialog dialog : dialogRegistry.expireIdle(idleMillis)) {
                LOG.info("[ra-sip-servlet] expiring idle dialog callId={} (idle>{}s)",
                        dialog.callId, config.dialogIdleSecs());
                endDialog(dialog.callId);
            }
        } catch (RuntimeException e) {
            LOG.warn("[ra-sip-servlet] dialog sweep failed", e);
        }
    }

    private void registerTransport(SipTransport transport) {
        transports.put(transport.protocol(), transport);
    }

    // ---- helpers ----

    /**
     * Extract Call-ID using javax.sip.* API (standard, not NIST-specific).
     * The NIST SIPMessage implements javax.sip.message.Message, so
     * {@code msg.getHeader(CallIdHeader.NAME)} works on all message types.
     */
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
