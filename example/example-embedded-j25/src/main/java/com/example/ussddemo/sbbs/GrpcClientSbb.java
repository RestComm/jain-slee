/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo.sbbs;

import com.example.ussddemo.commands.GrpcMenuCommand;
import com.example.ussddemo.embedded.EmbeddedUssdMain;
import com.example.ussddemo.events.GrpcBackendRequestEvent;
import com.example.ussddemo.events.GrpcBackendResponseEvent;
import com.example.ussddemo.events.GrpcMenuResponseEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SbbLocalObject;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Child SBB that bridges the USSD session to the gRPC menu RA.
 * Registered at runtime via {@code registerSbbType}.
 */
public final class GrpcClientSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(GrpcClientSbb.class);

    private volatile SbbLocalObject self;

    /** GOAL 1-5 — injected gRPC RA command port. */
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
        if (event instanceof GrpcBackendRequestEvent) {
            onGrpcRequest((GrpcBackendRequestEvent) event, aci);
        } else if (event instanceof GrpcMenuResponseEvent) {
            onGrpcMenuResponse((GrpcMenuResponseEvent) event, aci);
        }
    }

    private void onGrpcRequest(GrpcBackendRequestEvent event, ActivityContextInterface aci) {
        LOG.info("[gRPC-client] ResolveMenu session={} msisdn={}",
                event.getSessionId(), event.getMsisdn());
        EmbeddedUssdMain.grpcRa().requestMenu(
                event.getSessionId(), event.getMsisdn(), event.getUssdString(), aci);
    }

    private void onGrpcMenuResponse(GrpcMenuResponseEvent event, ActivityContextInterface aci) {
        String menu = "OK".equals(event.getStatus())
                ? event.getMenuText()
                : "ERR: " + event.getError();
        LOG.info("[gRPC-client] menu response session={} status={}",
                event.getSessionId(), event.getStatus());
        EmbeddedUssdMain.container().routeEvent(
                new GrpcBackendResponseEvent(event.getSessionId(), menu), aci);
    }

    /**
     * GOAL 1-5 — send a gRPC menu request through the injected RA command port.
     * The {@link RaCommandPort} is populated via {@code @InjectRa} at SBB creation
     * time, decoupling the SBB from the static {@code EmbeddedUssdMain.grpcRa()}
     * call. The RA processes the command asynchronously and fires a response event
     * back on the session activity context.
     */
    public void sendMenuRequest(String menuRequest) {
        RaCommandPort port = this.grpcCommandPort;
        if (port == null) {
            LOG.warn("[gRPC-client] grpcCommandPort not injected yet, falling back to static RA");
            return;
        }
        port.sendCommand(new GrpcMenuCommand(menuRequest));
    }
}
