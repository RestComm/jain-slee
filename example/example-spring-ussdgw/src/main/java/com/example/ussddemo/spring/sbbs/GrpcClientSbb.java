/*
 * micro-jainslee 1.1.0 -- example application (example-spring-ussdgw)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.sbbs;

import com.example.ussddemo.spring.UssdDemoContext;
import com.example.ussddemo.spring.events.GrpcMenuRequestEvent;
import com.example.ussddemo.spring.events.GrpcMenuResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.grpc.GrpcMenuCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Child SBB that bridges the USSD session to the gRPC menu RA.
 * Registered at runtime via {@code registerSbbType}.
 */
public final class GrpcClientSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(GrpcClientSbb.class);

    private final MicroSleeContainer container;
    private volatile SbbLocalObject self;

    /** Injected gRPC RA command port. */
    @InjectRa(name = "grpc-menu-ra")
    private volatile RaCommandPort grpcCommandPort;

    public GrpcClientSbb(MicroSleeContainer container, UssdDemoContext bootstrap) {
        this.container = container;
    }

    public void bindSelf(SbbLocalObject self) {
        this.self = self;
    }

    @Override
    public void sbbCreate() {
        LOG.debug("GrpcClientSbb created");
    }

    @Override
    public void sbbActivate() {
        LOG.debug("GrpcClientSbb activated");
    }

    @Override
    public void sbbPassivate() {
    }

    @Override
    public void sbbRemove() {
    }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof GrpcMenuRequestEvent) {
            onGrpcRequest((GrpcMenuRequestEvent) event, aci);
        } else if (event instanceof GrpcMenuResponseEvent) {
            onGrpcMenuResponse((GrpcMenuResponseEvent) event, aci);
        }
    }

    private void onGrpcRequest(GrpcMenuRequestEvent event, ActivityContextInterface aci) {
        LOG.info("[gRPC-client] ResolveMenu session={} msisdn={}",
                event.getSessionId(), event.getMsisdn());
        RaCommandPort port = this.grpcCommandPort;
        if (port != null) {
            port.sendCommand(new GrpcMenuCommand(
                    event.getSessionId(), event.getMsisdn(), event.getUssdString(), aci));
        } else {
            LOG.warn("[gRPC-client] grpcCommandPort not injected, cannot dispatch menu request");
        }
    }

    private void onGrpcMenuResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[gRPC-client] menu response session={} status={}",
                event.getSessionId(), event.getStatus());
        // The RA already fired this event onto the session ACI, so every
        // attached SBB (including the parent Ss7UssdIngressSbb) receives it
        // directly. Re-routing it here would loop the same event through
        // the router forever.
    }
}
