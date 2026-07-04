/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.sbbs;

import com.example.ussddemo.quarkus.events.GrpcMenuRequestEvent;
import com.example.ussddemo.quarkus.service.UssdSbbWiring;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.SbbAnnotation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Child SBB of {@link HttpServerSbb} — observes gRPC RA request events.
 * The RA performs the real upstream call asynchronously.
 */
@SbbAnnotation(name = "GrpcClient", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public final class GrpcClientSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(GrpcClientSbb.class);

    private final UssdSbbWiring wiring;

    public GrpcClientSbb(UssdSbbWiring wiring) {
        this.wiring = wiring;
    }

    public GrpcClientSbb() {
        this.wiring = null;
    }

    @Override public void sbbCreate() { LOG.debug("GrpcClientSbb created"); }
    @Override public void sbbActivate() { LOG.debug("GrpcClientSbb activated"); }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof GrpcMenuRequestEvent) {
            GrpcMenuRequestEvent req = (GrpcMenuRequestEvent) event;
            LOG.infof("[gRPC-child] observed GrpcMenuRequest session=%s msisdn=%s",
                    req.getSessionId(), req.getMsisdn());
        }
    }
}
