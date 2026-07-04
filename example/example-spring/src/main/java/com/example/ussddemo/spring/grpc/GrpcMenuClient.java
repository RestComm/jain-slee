/*
 * micro-jainslee 1.1.0 -- example application (example-spring)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.grpc;

import com.example.ussddemo.spring.proto.MenuRequest;
import com.example.ussddemo.spring.proto.MenuResponse;
import com.example.ussddemo.spring.proto.UssdMenuServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/** gRPC client to the standalone grpc-simulator (default localhost:9090). */
public final class GrpcMenuClient {

    private static final Logger LOG = LogManager.getLogger(GrpcMenuClient.class);

    private final ManagedChannel channel;
    private final UssdMenuServiceGrpc.UssdMenuServiceBlockingStub stub;

    public GrpcMenuClient(String host, int port) {
        this(host, port, 5_000L);
    }

    public GrpcMenuClient(String host, int port, long deadlineMs) {
        this.channel = NettyChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = UssdMenuServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(deadlineMs, TimeUnit.MILLISECONDS);
    }

    public MenuResponse resolveMenu(String msisdn, String ussdString, String sessionId) {
        MenuRequest req = MenuRequest.newBuilder()
                .setMsisdn(msisdn)
                .setUssdString(ussdString)
                .setSessionId(sessionId == null ? "" : sessionId)
                .build();
        try {
            return stub.resolveMenu(req);
        } catch (StatusRuntimeException e) {
            Status s = e.getStatus();
            LOG.warnf("[grpc] resolveMenu failed: %s %s", s.getCode(), s.getDescription());
            return MenuResponse.newBuilder()
                    .setSessionId(req.getSessionId())
                    .setStatus("ERR")
                    .setError(s.getCode() + ": " + s.getDescription())
                    .build();
        }
    }

    public void close() {
        channel.shutdownNow();
        try {
            channel.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
