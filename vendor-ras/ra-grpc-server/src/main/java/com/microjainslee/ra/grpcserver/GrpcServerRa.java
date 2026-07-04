/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.grpcserver;

import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Inbound gRPC server Resource Adaptor — starts a gRPC server that
 * receives gRPC requests and fires application events into the SLEE
 * via {@link com.microjainslee.api.SleeEndpointPort}.
 *
 * <p>This is a placeholder implementation. The actual gRPC server
 * lifecycle is managed by the grpc-simulator external process.</p>
 */
public final class GrpcServerRa extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(GrpcServerRa.class);

    private int port = 9090;

    public void setPort(int port) {
        this.port = port;
    }

    public int port() {
        return port;
    }

    @Override
    public void raConfigure() {
        LOG.info(() -> "gRPC server RA configured on port " + port);
    }

    @Override
    public void raActive() {
        LOG.info(() -> "gRPC server RA would start on port " + port
                + " (placeholder — actual server lives in grpc-simulator)");
    }

    @Override
    public void raStopping() {
        LOG.info("gRPC server RA stopping");
    }

    @Override
    public void raInactive() {
        LOG.info("gRPC server RA inactive — server cleanup (placeholder)");
    }

    @Override
    public void raUnconfigure() {
        LOG.info("gRPC server RA unconfigured");
        super.raUnconfigure();
    }
}
