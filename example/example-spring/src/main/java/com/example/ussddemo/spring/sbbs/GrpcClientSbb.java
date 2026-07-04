/*
 * micro-jainslee 1.1.0 -- example application (example-spring)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.sbbs;

import com.example.ussddemo.spring.events.GrpcMenuRequestEvent;
import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.SbbAnnotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Child SBB on the gRPC leg. Observes {@link GrpcMenuRequestEvent} fired
 * by the gRPC RA before the upstream call is made.
 */
@SbbAnnotation(name = "GrpcClient", vendor = "com.example.ussddemo", version = "1.0")
public final class GrpcClientSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(GrpcClientSbb.class);

    @Override public void sbbCreate() { }
    @Override public void sbbActivate() { }
    @Override public void sbbPassivate() { }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof GrpcMenuRequestEvent) {
            GrpcMenuRequestEvent req = (GrpcMenuRequestEvent) event;
            LOG.infof("[gRPC-client] ResolveMenu session=%s msisdn=%s",
                    req.getSessionId(), req.getMsisdn());
        }
    }
}
