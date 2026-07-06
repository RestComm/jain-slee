/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.ra.grpc.command.InvokeGrpc;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 3-port endpoint for {@link GenericGrpcClientRa}. RA name:
 * {@code "grpc-client-ra"} (override with {@link #setRaName(String)}).
 */
public final class GenericGrpcClientRaEndpoint implements RaEndpointPort, RaCommandPort {

    private static final Logger LOG = LogManager.getLogger(GenericGrpcClientRaEndpoint.class);

    private final GenericGrpcClientRa delegate;
    private String raName = "grpc-client-ra";

    public GenericGrpcClientRaEndpoint(GenericGrpcClientRa delegate) {
        this.delegate = delegate;
    }

    public GenericGrpcClientRaEndpoint() {
        this(new GenericGrpcClientRa());
    }

    public void setRaName(String raName) {
        this.raName = raName;
    }

    public GenericGrpcClientRa delegate() {
        return delegate;
    }

    @Override
    public String getRaName() {
        return raName;
    }

    @Override
    public void activate(RaBootstrapPort bootstrap) {
        delegate.setBootstrapPort(bootstrap);
        delegate.raActive();
        LOG.info("Generic gRPC client RA endpoint [{}] activated", raName);
    }

    @Override
    public void deactivate() {
        delegate.raInactive();
        LOG.info("Generic gRPC client RA endpoint [{}] deactivated", raName);
    }

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof InvokeGrpc invoke) {
            delegate.sendOutbound(invoke);
        } else {
            LOG.warn("Generic gRPC client RA [{}] received unknown command: {}",
                    raName, command == null ? "null" : command.getClass().getName());
        }
    }
}
