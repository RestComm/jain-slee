/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.transport;

import com.microjainslee.ra.jss7.Ss7RaConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.restcomm.protocols.ss7.cap.CAPStackImpl;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPStack;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.sccpext.impl.SccpExtModuleImpl;
import org.restcomm.protocols.ss7.sccpext.router.RouterExt;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterfaceImpl;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Bootstraps and owns the full RestComm jSS7 protocol stack for the RA:
 * <pre>SCTP (Netty) → M3UA → SCCP (+ext) → TCAP → MAP / CAP</pre>
 *
 * <p>The provider objects exposed here are what the listener adapters
 * register against and what the outbound sender uses to originate dialogs.
 * INAP has no provider in the jSS7 tree (codecs/primitives only), so it is
 * handled at the TCAP-component level rather than here.</p>
 *
 * <p>Bootstrap sequence mirrors the proven {@code map-load} test harness.</p>
 */
public final class Ss7Stack {

    private static final Logger LOG = LogManager.getLogger(Ss7Stack.class);

    private final Ss7RaConfig cfg;

    private Management sctp;
    private M3UAManagementImpl m3ua;
    private SccpStackImpl sccp;
    private SccpExtModuleImpl sccpExt;
    private TCAPStack tcapStack;
    private MAPStack mapStack;
    private CAPStack capStack;

    private volatile boolean started;

    public Ss7Stack(Ss7RaConfig cfg) {
        this.cfg = cfg;
    }

    // ── provider accessors (used by listener adapters / outbound sender) ──
    public TCAPProvider tcapProvider() { return tcapStack.getProvider(); }
    public SccpProvider sccpProvider() { return sccp.getSccpProvider(); }
    public MAPProvider mapProvider()   { return mapStack == null ? null : mapStack.getMAPProvider(); }
    public CAPProvider capProvider()   { return capStack == null ? null : capStack.getCAPProvider(); }
    public boolean isStarted()         { return started; }

    // ── lifecycle ─────────────────────────────────────────────
    public synchronized void start() throws Exception {
        if (started) return;
        LOG.info("[ra-jss7] bootstrapping jSS7 stack: {}", cfg);
        initSctp();
        initM3ua();
        initSccp();
        initTcap();
        if (cfg.mapEnabled()) initMap();
        if (cfg.capEnabled()) initCap();
        started = true;
        LOG.info("[ra-jss7] jSS7 stack STARTED (map={} cap={})", cfg.mapEnabled(), cfg.capEnabled());
    }

    public synchronized void stop() {
        if (!started) return;
        started = false;
        quietly(() -> { if (capStack != null) capStack.stop(); });
        quietly(() -> { if (mapStack != null) mapStack.stop(); });
        quietly(() -> { if (tcapStack != null) tcapStack.stop(); });
        quietly(() -> { if (sccp != null) sccp.stop(); });
        quietly(() -> { if (m3ua != null) m3ua.stop(); });
        quietly(() -> { if (sctp != null) sctp.stop(); });
        LOG.info("[ra-jss7] jSS7 stack STOPPED");
    }

    // ── SCTP ──────────────────────────────────────────────────
    private void initSctp() throws Exception {
        // Programs against the SCTP project's Management abstraction; the
        // concrete impl is resolved by SctpManagementFactory (no direct
        // netty/impl import here).
        sctp = SctpManagementFactory.create(cfg.stackName() + "-sctp");
        sctp.setWorkerThreads(cfg.sctpWorkerThreads());
        sctp.setOptionSctpInitMaxstreams_MaxInStreams(256);
        sctp.setOptionSctpInitMaxstreams_MaxOutStreams(256);
        sctp.start();
        sctp.setConnectDelay(1000);

        IpChannelType channel = "TCP".equalsIgnoreCase(cfg.ipChannelType())
                ? IpChannelType.TCP : IpChannelType.SCTP;
        sctp.addAssociation(cfg.hostIp(), cfg.hostPort(), cfg.peerIp(), cfg.peerPort(),
                cfg.associationName(), channel, null);
    }

    // ── M3UA ──────────────────────────────────────────────────
    private void initM3ua() throws Exception {
        m3ua = new M3UAManagementImpl(cfg.stackName() + "-m3ua", null, new Ss7ExtInterfaceImpl());
        m3ua.setTransportManagement(sctp);
        m3ua.setDeliveryMessageThreadCount(cfg.deliveryMessageThreadCount());
        m3ua.start();
        m3ua.removeAllResources();

        ParameterFactoryImpl factory = new ParameterFactoryImpl();
        RoutingContext rc = factory.createRoutingContext(new long[] { cfg.routingContext() });
        TrafficModeType tmt = factory.createTrafficModeType(TrafficModeType.Loadshare);
        NetworkAppearance na = factory.createNetworkAppearance(cfg.networkAppearance());

        Functionality functionality = cfg.ipspClient() ? Functionality.IPSP : Functionality.AS;
        IPSPType ipspType = cfg.ipspClient() ? IPSPType.CLIENT : null;

        m3ua.createAs("AS1", functionality, ExchangeType.SE, ipspType, rc, tmt, 1, na);
        m3ua.createAspFactory("ASP1", cfg.associationName());
        m3ua.assignAspToAs("AS1", "ASP1");
        m3ua.addRoute(cfg.destinationPointCode(), cfg.originatingPointCode(),
                cfg.serviceIndicator(), "AS1");
    }

    // ── SCCP (+ ext) ──────────────────────────────────────────
    private void initSccp() throws Exception {
        Ss7ExtInterface ss7Ext = new Ss7ExtInterfaceImpl();
        sccpExt = new SccpExtModuleImpl();
        ss7Ext.setSs7ExtSccpInterface(sccpExt);
        sccp = new SccpStackImpl(cfg.stackName() + "-sccp", ss7Ext);
        sccp.setMtp3UserPart(1, m3ua);
        sccp.start();
        sccp.removeAllResources();

        Router router = sccp.getRouter();
        RouterExt routerExt = sccpExt.getRouterExt();
        SccpResource sccpResource = sccp.getSccpResource();

        int opc = cfg.originatingPointCode();
        int dpc = cfg.destinationPointCode();

        sccpResource.addRemoteSpc(1, dpc, 0, 0);
        sccpResource.addRemoteSsn(1, dpc, cfg.remoteSsn(), 0, false);

        router.addMtp3ServiceAccessPoint(1, 1, opc, cfg.networkIndicator(), 0, null);
        router.addMtp3Destination(1, 1, dpc, dpc, 0, 255, 255);

        org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl fact =
                new org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gtLocal = fact.createGlobalTitle("-", 0, NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        GlobalTitle gtRemote = fact.createGlobalTitle("-", 0, NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtLocal, opc, 0);
        routerExt.addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRemote, dpc, 0);
        routerExt.addRoutingAddress(2, remoteAddress);

        GlobalTitle gtPattern = fact.createGlobalTitle("*", 0, NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtPattern, 0, 0);
        routerExt.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE,
                pattern, "K", 1, -1, null, 0, null);
        routerExt.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL,
                pattern, "K", 2, -1, null, 0, null);
    }

    // ── TCAP ──────────────────────────────────────────────────
    private void initTcap() throws Exception {
        List<Integer> extraSsns = new ArrayList<>();
        tcapStack = new TCAPStackImpl(cfg.stackName() + "-tcap", sccp.getSccpProvider(), cfg.localSsn());
        tcapStack.setExtraSsns(extraSsns);
        tcapStack.start();
        tcapStack.setDialogIdleTimeout(cfg.dialogIdleTimeoutMs());
        tcapStack.setInvokeTimeout(cfg.invokeTimeoutMs());
        tcapStack.setMaxDialogs(cfg.maxDialogs());
    }

    // ── MAP ───────────────────────────────────────────────────
    private void initMap() throws Exception {
        mapStack = new MAPStackImpl(cfg.stackName() + "-map", tcapStack.getProvider());
        mapStack.start();
    }

    // ── CAP ───────────────────────────────────────────────────
    private void initCap() throws Exception {
        capStack = new CAPStackImpl(cfg.stackName() + "-cap", tcapStack.getProvider());
        capStack.start();
    }

    private void quietly(ThrowingRunnable r) {
        try { r.run(); } catch (Exception e) { LOG.warn("[ra-jss7] stack teardown step failed: {}", e.toString()); }
    }

    @FunctionalInterface
    private interface ThrowingRunnable { void run() throws Exception; }
}
