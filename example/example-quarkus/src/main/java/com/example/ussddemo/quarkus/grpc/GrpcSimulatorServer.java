/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/** Lightweight in-process gRPC AS for tests (mirrors grpc-simulator). */
public final class GrpcSimulatorServer {

    private static final Logger LOG = LogManager.getLogger(GrpcSimulatorServer.class);

    private final Server server;
    private final int port;

    private GrpcSimulatorServer(int port, BindableService service) {
        this.port = port;
        this.server = NettyServerBuilder
                .forAddress(new InetSocketAddress("127.0.0.1", port))
                .addService(service)
                .build();
    }

    public synchronized void start() throws java.io.IOException {
        server.start();
    }

    public synchronized void close() {
        if (!server.isShutdown()) {
            server.shutdownNow();
            try {
                if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOG.warn("gRPC server did not terminate within 5s");
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int port() {
        int p = server.getPort();
        return p > 0 ? p : port;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private int port = 9090;
        private BindableService service = new DefaultMenuServiceImpl();

        public Builder port(int p) {
            this.port = p;
            return this;
        }

        public Builder service(BindableService s) {
            this.service = s;
            return this;
        }

        public GrpcSimulatorServer build() {
            return new GrpcSimulatorServer(port, service);
        }
    }
}
