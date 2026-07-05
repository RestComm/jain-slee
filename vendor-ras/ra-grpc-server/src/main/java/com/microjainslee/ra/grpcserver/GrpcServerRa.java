/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.grpcserver;

import com.microjainslee.api.SimpleActivityContextHandle;
import com.microjainslee.ra.spi.AbstractResourceAdaptor;

import io.grpc.HandlerRegistry;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerMethodDefinition;
import io.grpc.Status;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Generic bytes-level gRPC server RA — accepts <b>any</b> unary gRPC
 * method without generated stubs.
 *
 * <p>A fallback {@link HandlerRegistry} materialises a
 * {@code MethodDescriptor<byte[], byte[]>} for whatever full method name
 * a client calls, so one RA instance serves every service/method. The
 * request message bytes are fired to SBBs as {@link GrpcRequestEvent};
 * the SBB completes the call with {@link SendGrpcResponse} /
 * {@link SendGrpcError}. Schema (protobuf) encoding/decoding belongs to
 * the application layer — the RA is pure transport, like every other
 * micro-jainslee RA.</p>
 *
 * <p>Session correlation: {@link #setCorrelationMetadataKey(String)}
 * converges calls carrying the same metadata value onto one SLEE activity
 * (stateful SBB sessions). Default: one activity per call, ended when the
 * call completes.</p>
 */
public final class GrpcServerRa extends AbstractResourceAdaptor {

    private static final Logger LOG = LogManager.getLogger(GrpcServerRa.class);

    /** Identity marshaller — hands the raw message bytes through. */
    public static final MethodDescriptor.Marshaller<byte[]> BYTES_MARSHALLER =
            new MethodDescriptor.Marshaller<>() {
                @Override
                public InputStream stream(byte[] value) {
                    return new ByteArrayInputStream(value == null ? new byte[0] : value);
                }

                @Override
                public byte[] parse(InputStream stream) {
                    try {
                        return stream.readAllBytes();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read gRPC message", e);
                    }
                }
            };

    private static final class PendingCall {
        final ServerCall<byte[], byte[]> call;
        final String activityId;
        final long deadlineMillis;
        volatile boolean completed;

        PendingCall(ServerCall<byte[], byte[]> call, String activityId, long deadlineMillis) {
            this.call = call;
            this.activityId = activityId;
            this.deadlineMillis = deadlineMillis;
        }
    }

    private int port = 9090;
    private String host = "0.0.0.0";
    private String correlationMetadataKey;
    private long callTimeoutMillis = 30_000L;

    private Server server;
    private ScheduledExecutorService sweeper;
    private final Map<String, PendingCall> pendingCalls = new ConcurrentHashMap<>();

    public void setPort(int port) { this.port = port; }
    public void setHost(String host) { this.host = host; }

    /** Metadata (ASCII) key whose value becomes the SLEE activity id. */
    public void setCorrelationMetadataKey(String key) { this.correlationMetadataKey = key; }

    /** Max time an SBB has to answer before the call fails DEADLINE_EXCEEDED. */
    public void setCallTimeoutMillis(long millis) { this.callTimeoutMillis = millis; }

    /** Actual bound port (after ephemeral bind when configured port is 0). */
    public int port() {
        return server != null ? server.getPort() : port;
    }

    public int pendingCallCount() {
        return pendingCalls.size();
    }

    @Override
    public void raConfigure() {
        LOG.info("gRPC server RA configured {}:{} (generic bytes-level)", host, port);
    }

    @Override
    public void raActive() {
        try {
            server = NettyServerBuilder
                    .forAddress(new InetSocketAddress(host, port))
                    .fallbackHandlerRegistry(new HandlerRegistry() {
                        @Override
                        public ServerMethodDefinition<?, ?> lookupMethod(
                                String methodName, String authority) {
                            return genericMethod(methodName);
                        }
                    })
                    .build()
                    .start();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to start gRPC server on " + host + ":" + port, e);
        }
        sweeper = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "grpc-ra-call-sweeper");
            t.setDaemon(true);
            return t;
        });
        sweeper.scheduleAtFixedRate(this::sweepExpiredCalls, 5, 5, TimeUnit.SECONDS);
        LOG.info("gRPC server RA listening on {}:{}", host, server.getPort());
    }

    @Override
    public void raStopping() {
        LOG.info("gRPC server RA stopping");
    }

    @Override
    public void raInactive() {
        if (sweeper != null) { sweeper.shutdownNow(); sweeper = null; }
        for (PendingCall pending : pendingCalls.values()) {
            closeQuietly(pending, Status.UNAVAILABLE.withDescription("RA shutting down"));
        }
        pendingCalls.clear();
        if (server != null) {
            server.shutdown();
            try {
                if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
                    server.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                server.shutdownNow();
            }
            server = null;
        }
    }

    @Override
    public void raUnconfigure() {
        raInactive();
        super.raUnconfigure();
    }

    // ── generic handler ─────────────────────────────────────────────

    private ServerMethodDefinition<byte[], byte[]> genericMethod(String methodName) {
        MethodDescriptor<byte[], byte[]> descriptor = MethodDescriptor
                .<byte[], byte[]>newBuilder()
                .setType(MethodDescriptor.MethodType.UNARY)
                .setFullMethodName(methodName)
                .setRequestMarshaller(BYTES_MARSHALLER)
                .setResponseMarshaller(BYTES_MARSHALLER)
                .build();
        ServerCallHandler<byte[], byte[]> handler = (call, headers) ->
                startUnaryCall(methodName, call, headers);
        return ServerMethodDefinition.create(descriptor, handler);
    }

    private ServerCall.Listener<byte[]> startUnaryCall(String methodName,
            ServerCall<byte[], byte[]> call, Metadata headers) {
        String callId = UUID.randomUUID().toString();
        Map<String, String> metadata = copyAsciiMetadata(headers);
        String activityId = resolveActivityId(metadata, callId);

        pendingCalls.put(callId, new PendingCall(call, activityId,
                System.currentTimeMillis() + callTimeoutMillis));
        call.request(2); // unary: one message expected; the extra credit detects abuse

        return new ServerCall.Listener<>() {
            private byte[] request;

            @Override
            public void onMessage(byte[] message) {
                if (request != null) {
                    closeCall(callId, Status.INVALID_ARGUMENT
                            .withDescription("Only unary calls are supported"));
                    return;
                }
                request = message;
            }

            @Override
            public void onHalfClose() {
                if (request == null) {
                    closeCall(callId, Status.INVALID_ARGUMENT
                            .withDescription("Missing request message"));
                    return;
                }
                fireRequest(callId, methodName, request, metadata, activityId);
            }

            @Override
            public void onCancel() {
                PendingCall removed = pendingCalls.remove(callId);
                if (removed != null && !removed.completed) {
                    LOG.debug("gRPC call {} cancelled by client", callId);
                }
                endCallActivity(activityId);
            }
        };
    }

    private void fireRequest(String callId, String methodName, byte[] payload,
                             Map<String, String> metadata, String activityId) {
        try {
            endpoint().startActivity(new SimpleActivityContextHandle(activityId), null);
        } catch (RuntimeException e) {
            LOG.debug("startActivity for {} — {}", activityId, e.getMessage());
        }
        try {
            endpoint().fireEvent(new SimpleActivityContextHandle(activityId),
                    new GrpcRequestEvent(callId, methodName, payload, metadata, activityId));
        } catch (RuntimeException e) {
            LOG.error("gRPC RA failed to fire event for call {}", callId, e);
            closeCall(callId, Status.INTERNAL.withDescription("event routing failed"));
        }
    }

    // ── outbound (SBB → RA) ─────────────────────────────────────────

    public void sendOutbound(GrpcServerCommand command) {
        switch (command) {
            case SendGrpcResponse response -> completeCall(response.callId(), pending -> {
                pending.call.sendHeaders(new Metadata());
                pending.call.sendMessage(
                        response.payload() == null ? new byte[0] : response.payload());
                pending.call.close(Status.OK, new Metadata());
            });
            case SendGrpcError error -> completeCall(error.callId(), pending ->
                    pending.call.close(
                            Status.fromCodeValue(error.statusCode())
                                    .withDescription(error.description()),
                            new Metadata()));
        }
    }

    private void completeCall(String callId, Consumer<PendingCall> completion) {
        PendingCall pending = pendingCalls.remove(callId);
        if (pending == null) {
            LOG.warn("gRPC RA: response for unknown/expired call {} — dropped", callId);
            return;
        }
        pending.completed = true;
        try {
            completion.accept(pending);
        } catch (RuntimeException e) {
            LOG.warn("gRPC RA: completing call {} failed: {}", callId, e.getMessage());
        }
        endCallActivity(pending.activityId);
    }

    private void closeCall(String callId, Status status) {
        PendingCall pending = pendingCalls.remove(callId);
        if (pending != null) {
            closeQuietly(pending, status);
            endCallActivity(pending.activityId);
        }
    }

    private static void closeQuietly(PendingCall pending, Status status) {
        try {
            pending.completed = true;
            pending.call.close(status, new Metadata());
        } catch (RuntimeException ignored) {
            // call may already be closed by the transport
        }
    }

    private void sweepExpiredCalls() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, PendingCall> entry : pendingCalls.entrySet()) {
            if (entry.getValue().deadlineMillis < now) {
                LOG.warn("gRPC RA: no SBB reply within {}ms for call {} — DEADLINE_EXCEEDED",
                        callTimeoutMillis, entry.getKey());
                closeCall(entry.getKey(), Status.DEADLINE_EXCEEDED
                        .withDescription("No SBB reply within " + callTimeoutMillis + "ms"));
            }
        }
    }

    // ── helpers ─────────────────────────────────────────────────────

    private void endCallActivity(String activityId) {
        // Per-call activities end with the call. Correlated (session)
        // activities stay alive; the application ends them, or they are
        // dropped when the RA deactivates.
        if (correlationMetadataKey == null) {
            try {
                endpoint().endActivity(new SimpleActivityContextHandle(activityId));
            } catch (RuntimeException e) {
                LOG.debug("endActivity {} — {}", activityId, e.getMessage());
            }
        }
    }

    private String resolveActivityId(Map<String, String> metadata, String callId) {
        if (correlationMetadataKey != null) {
            String value = metadata.get(correlationMetadataKey.toLowerCase(Locale.ROOT));
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return callId;
    }

    private static Map<String, String> copyAsciiMetadata(Metadata headers) {
        Map<String, String> copy = new HashMap<>();
        for (String key : headers.keys()) {
            if (!key.endsWith(Metadata.BINARY_HEADER_SUFFIX)) {
                String value = headers.get(
                        Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER));
                if (value != null) {
                    copy.put(key, value);
                }
            }
        }
        return Map.copyOf(copy);
    }
}
