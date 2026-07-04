/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.sbbs;

import com.example.ussddemo.events.GrpcMenuRequestEvent;
import com.example.ussddemo.EmbeddedUssdMain;

import com.example.ussddemo.events.GrpcMenuResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.ra.grpc.GrpcMenuCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Child SBB that bridges the USSD session to the gRPC menu RA.
 * Registered at runtime via {@code registerSbbType}.
 */
public final class GrpcClientSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(GrpcClientSbb.class);

    private volatile SbbLocalObject self;

    /** Injected gRPC RA command port. */
    @InjectRa(name = "grpcMenuRa")
    private volatile RaCommandPort grpcCommandPort;

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
        // Route response back onto the session activity context so the
        // parent Ss7UssdIngressSbb (which also listens on this session)
        // picks it up.
        EmbeddedUssdMain.container().routeEvent(event, aci);
    }
}
