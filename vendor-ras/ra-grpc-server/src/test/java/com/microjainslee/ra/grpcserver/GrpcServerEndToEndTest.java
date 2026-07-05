/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;

import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.MethodDescriptor;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.ClientCalls;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Real-socket full loop: a stub-less gRPC client calls an arbitrary
 * method → generic server RA fires GrpcRequestEvent → EchoSbb answers
 * with SendGrpcResponse → client receives the bytes.
 */
public class GrpcServerEndToEndTest {

    public static class EchoSbb implements Sbb, SleeEventHandler {
        @InjectRa(name = "grpc-server-ra")
        private volatile RaCommandPort grpcRa;

        public EchoSbb() {
        }

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            if (event instanceof GrpcRequestEvent request) {
                RaCommandPort port = this.grpcRa;
                if (port == null) {
                    return;
                }
                if (request.fullMethod().endsWith("/Fail")) {
                    port.sendCommand(new SendGrpcError(request.callId(), 5, "not found (test)"));
                    return;
                }
                String body = new String(request.payload(), StandardCharsets.UTF_8);
                port.sendCommand(new SendGrpcResponse(request.callId(),
                        body.toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    private MicroSleeContainer container;
    private GrpcServerRaEndpoint endpoint;
    private ManagedChannel channel;

    @Before
    public void setUp() {
        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(64)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        container.registerSbbType(EchoSbb.class, EchoSbb::new);
        container.createIesDispatcher();
        container.mapEventToSbb(GrpcRequestEvent.class, "EchoSbb");

        GrpcServerRa ra = new GrpcServerRa();
        ra.setHost("127.0.0.1");
        ra.setPort(0); // ephemeral
        ra.setCallTimeoutMillis(10_000);
        endpoint = new GrpcServerRaEndpoint(ra);
        container.registerRa(endpoint, endpoint);

        channel = NettyChannelBuilder
                .forAddress("127.0.0.1", ra.port())
                .usePlaintext()
                .build();
    }

    @After
    public void tearDown() throws Exception {
        channel.shutdownNow();
        channel.awaitTermination(5, TimeUnit.SECONDS);
        endpoint.deactivate();
        container.stop();
    }

    private static MethodDescriptor<byte[], byte[]> method(String fullName) {
        return MethodDescriptor.<byte[], byte[]>newBuilder()
                .setType(MethodDescriptor.MethodType.UNARY)
                .setFullMethodName(fullName)
                .setRequestMarshaller(GrpcServerRa.BYTES_MARSHALLER)
                .setResponseMarshaller(GrpcServerRa.BYTES_MARSHALLER)
                .build();
    }

    @Test
    public void arbitraryUnaryMethodIsServedBySbb() {
        byte[] reply = ClientCalls.blockingUnaryCall(
                channel, method("any.Service/Echo"), CallOptions.DEFAULT
                        .withDeadlineAfter(10, TimeUnit.SECONDS),
                "hello grpc".getBytes(StandardCharsets.UTF_8));
        assertEquals("HELLO GRPC", new String(reply, StandardCharsets.UTF_8));
    }

    @Test
    public void differentServicesHitTheSameGenericHandler() {
        byte[] r1 = ClientCalls.blockingUnaryCall(channel, method("svc.A/M1"),
                CallOptions.DEFAULT.withDeadlineAfter(10, TimeUnit.SECONDS),
                "a".getBytes(StandardCharsets.UTF_8));
        byte[] r2 = ClientCalls.blockingUnaryCall(channel, method("totally.other.B/M2"),
                CallOptions.DEFAULT.withDeadlineAfter(10, TimeUnit.SECONDS),
                "b".getBytes(StandardCharsets.UTF_8));
        assertEquals("A", new String(r1, StandardCharsets.UTF_8));
        assertEquals("B", new String(r2, StandardCharsets.UTF_8));
    }

    @Test
    public void sbbCanFailTheCallWithGrpcStatus() {
        try {
            ClientCalls.blockingUnaryCall(channel, method("any.Service/Fail"),
                    CallOptions.DEFAULT.withDeadlineAfter(10, TimeUnit.SECONDS),
                    "x".getBytes(StandardCharsets.UTF_8));
            fail("expected StatusRuntimeException");
        } catch (StatusRuntimeException e) {
            assertEquals(5, e.getStatus().getCode().value()); // NOT_FOUND
            assertTrue(String.valueOf(e.getStatus().getDescription()).contains("test"));
        }
    }
}
