/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.ra.diameter.collab.DiameterEventClassifier;
import com.microjainslee.ra.diameter.collab.DiameterOutboundSender;
import com.microjainslee.ra.diameter.command.DiameterCommand;
import com.microjainslee.ra.diameter.event.DiameterEvent;
import com.microjainslee.ra.diameter.transport.DiameterTransport;
import com.microjainslee.ra.diameter.transport.TcpDiameterTransport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Diameter Resource Adaptor — Netty TCP transport + 3-port contract.
 *
 * <p>Camel-generic: a single RA instance handles <em>any</em> Diameter
 * application (Cx/Dx, Sh, Gx, Ro) through a shared transport layer.
 * SBBs receive typed {@link DiameterEvent}s distinguished by
 * {@code applicationId} and {@code commandCode}.</p>
 *
 * <p>Lifecycle: raConfigure → raActive → (process Diameter) → raInactive → raUnconfigure.</p>
 */
public final class DiameterResourceAdaptor {
    private static final Logger LOG = LogManager.getLogger(DiameterResourceAdaptor.class);

    private DiameterRaConfig config = new DiameterRaConfig();
    private final List<DiameterTransport> transports = new ArrayList<>(2);
    private RaBootstrapPort bootstrapPort;
    private DiameterEventClassifier classifier = new DiameterEventClassifier();
    private DiameterOutboundSender outboundSender;
    private final Map<String, ActivityHandle> sessions = new ConcurrentHashMap<>();
    private final AtomicBoolean active = new AtomicBoolean(false);

    // ---- collaborator injection ----

    public void setBootstrapPort(RaBootstrapPort bp) { this.bootstrapPort = bp; }
    public void setConfig(DiameterRaConfig c) { this.config = c; }
    public void setClassifier(DiameterEventClassifier c) { this.classifier = c; }
    public void setOutboundSender(DiameterOutboundSender s) { this.outboundSender = s; }

    // ---- accessors ----

    public DiameterRaConfig config() { return config; }
    public boolean isActive() { return active.get(); }

    // ---- Lifecycle ----

    public void raConfigure() {
        LOG.info("[ra-diameter] raConfigure host={} port={} realm={}",
                config.host(), config.port(), config.realm());
    }

    public void raActive() {
        if (!active.compareAndSet(false, true)) return;
        if (config.tcpEnabled())
            transports.add(new TcpDiameterTransport(config, this::onRawMessage));
        transports.forEach(DiameterTransport::start);
        LOG.info("[ra-diameter] ACTIVE transports={}", transports.size());
    }

    public void raInactive() {
        if (!active.compareAndSet(true, false)) return;
        transports.forEach(DiameterTransport::stop);
        transports.clear();
        sessions.clear();
        LOG.info("[ra-diameter] INACTIVE");
    }

    public void raUnconfigure() {
        raInactive();
        LOG.info("[ra-diameter] UNCONFIGURED");
    }

    // ---- outbound (SBB → RA → wire) ----

    /** Called by {@code DiameterRaEndpoint.sendCommand} when an SBB
     * sends an outbound Diameter command. */
    public void sendOutbound(DiameterCommand cmd) {
        if (outboundSender != null) {
            outboundSender.send(cmd);
        } else {
            LOG.warn("[ra-diameter] No outbound sender, dropping {}",
                    cmd.getClass().getSimpleName());
        }
    }

    // ---- inbound (wire → RA → SLEE) ----

    private void onRawMessage(Message msg) {
        if (!active.get() || bootstrapPort == null) return;

        String sessionId = extractSessionId(msg);
        ActivityHandle handle = sessions.computeIfAbsent(sessionId,
                id -> bootstrapPort.createActivityHandle(id));

        DiameterEvent event = classifier.classify(msg);
        if (event != null) {
            bootstrapPort.fireEvent(event, handle, null);
        }
    }

    private static String extractSessionId(Message msg) {
        try {
            String sid = msg.getSessionId();
            if (sid != null && !sid.isBlank()) return sid;
        } catch (Exception ignored) { /* fall through */ }
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
