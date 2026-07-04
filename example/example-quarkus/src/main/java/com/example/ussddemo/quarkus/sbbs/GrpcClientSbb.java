/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 */

package com.example.ussddemo.quarkus.sbbs;

import com.example.ussddemo.quarkus.bootstrap.UssdDemoContext;
import com.example.ussddemo.quarkus.events.GrpcMenuRequestEvent;
import com.example.ussddemo.quarkus.events.GrpcMenuResponseEvent;
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
 * Uses vendor-ras {@code GrpcMenuResourceAdaptor} via the endpoint pattern.
 */
public final class GrpcClientSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(GrpcClientSbb.class);

    private final UssdDemoContext ctx;
    private volatile SbbLocalObject self;

    /** GOAL 1-5 — injected gRPC RA command port (vendor-ras endpoint name). */
    @InjectRa(name = "grpc-menu-ra")
    private volatile RaCommandPort grpcCommandPort;

    public GrpcClientSbb(UssdDemoContext ctx) {
        this.ctx = ctx;
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

        // GOAL 1-5 — send the menu request through the injected RaCommandPort.
        // The vendor-ras GrpcMenuRaEndpoint.sendCommand() dispatches
        // GrpcMenuCommand → delegate.requestMenu(...).
        RaCommandPort port = this.grpcCommandPort;
        if (port != null) {
            port.sendCommand(new GrpcMenuCommand(
                    event.getSessionId(), event.getMsisdn(), event.getUssdString(), aci));
        } else {
            LOG.warn("[gRPC-client] grpcCommandPort not injected — menu request dropped");
        }
    }

    private void onGrpcMenuResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        LOG.info("[gRPC-client] menu response session={} status={}",
                event.getSessionId(), event.getStatus());
        // Route response back onto the session activity context so the
        // parent Ss7UssdIngressSbb picks it up.
        ctx.container().routeEvent(event, aci);
    }
}
