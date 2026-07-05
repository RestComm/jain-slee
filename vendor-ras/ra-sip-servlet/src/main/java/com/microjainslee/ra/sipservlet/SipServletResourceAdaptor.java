/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;
import com.microjainslee.ra.sipservlet.dispatcher.SipEventDispatcher;
import com.microjainslee.ra.sipservlet.transport.*;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.parser.StringMsgParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SIP-Servlet Resource Adaptor — Netty + corsac-sip + LMAX Disruptor + VT.
 * Lifecycle: raConfigure → raActive → (process SIP) → raInactive → raUnconfigure.
 */
public final class SipServletResourceAdaptor extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(SipServletResourceAdaptor.class);

    private SipRaConfig config = new SipRaConfig();
    private final List<SipTransport> transports = new ArrayList<>(3);
    private SipEventDispatcher dispatcher;
    private ExecutorService executor;
    private final AtomicBoolean active = new AtomicBoolean(false);

    @Override
    public void raConfigure() {
        LOG.info("[ra-sip-servlet] raConfigure host={} tcp={} udp={} sctp={}",
                config.host(), config.tcpPort(), config.udpPort(), config.sctpPort());
    }

    @Override
    public void raActive() {
        if (!active.compareAndSet(false, true)) return;
        executor = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("sip-ra-", 1).factory());
        dispatcher = new SipEventDispatcher(config.ringBufferSize(),
                this::onSipEvent, executor);
        if (config.tcpPort() > 0)
            transports.add(new TcpTransport(config, this::onRawMessage));
        if (config.udpPort() > 0)
            transports.add(new UdpTransport(config, this::onRawMessage));
        if (config.sctpPort() > 0)
            transports.add(new SctpTransport(config, this::onRawMessage));
        transports.forEach(SipTransport::start);
        dispatcher.start();
        LOG.info("[ra-sip-servlet] ACTIVE transports={}", transports.size());
    }

    @Override
    public void raInactive() {
        if (!active.compareAndSet(true, false)) return;
        transports.forEach(SipTransport::stop);
        transports.clear();
        if (dispatcher != null) { dispatcher.stop(); dispatcher = null; }
        if (executor != null) { executor.close(); executor = null; }
        LOG.info("[ra-sip-servlet] INACTIVE");
    }

    @Override
    public void raUnconfigure() {
        raInactive();
        LOG.info("[ra-sip-servlet] UNCONFIGURED");
    }

    // --- internal handlers ---

    private void onRawMessage(byte[] raw) {
        if (!active.get()) return;
        try {
            StringMsgParser parser = new StringMsgParser();
            SIPMessage sipMsg = parser.parseSIPMessage(raw, true, false, null);
            if (sipMsg != null) dispatcher.publish(sipMsg);
        } catch (ParseException e) {
            LOG.warn("[ra-sip-servlet] Parse error", e);
        }
    }

    private void onSipEvent(SIPMessage msg) {
        String callId = deriveCallId(msg);
        publish(callId, new SipRaEvent(msg));
    }

    private static String deriveCallId(SIPMessage msg) {
        if (msg instanceof SIPRequest r && r.getCallIdHeader() != null)
            return r.getCallIdHeader().getCallId();
        if (msg instanceof SIPResponse r && r.getCallIdHeader() != null)
            return r.getCallIdHeader().getCallId();
        return UUID.randomUUID().toString();
    }

    // --- accessors ---
    public void setConfig(SipRaConfig c) { this.config = c; }
    public SipRaConfig config() { return config; }
    public boolean isActive() { return active.get(); }
}
