/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.ra.grpc.command.InvokeGrpc;
import com.microjainslee.ra.grpc.events.GrpcInvokeResponseEvent;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.grpcserver.events.GrpcRequestEvent;
import com.microjainslee.ra.grpcserver.GrpcServerRa;
import com.microjainslee.ra.grpcserver.GrpcServerRaEndpoint;
import com.microjainslee.ra.grpcserver.command.SendGrpcResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Full loop between BOTH generic gRPC RAs inside one container:
 *
 * <pre>
 *   RequesterSbb —InvokeGrpc→ GenericGrpcClientRa ══gRPC/TCP══► GrpcServerRa
 *        ▲                                                        │ GrpcRequestEvent
 *        │ GrpcInvokeResponseEvent                                ▼
 *        └────────── GenericGrpcClientRa ◄══bytes══ EchoSbb (SendGrpcResponse)
 * </pre>
 *
 * Proves the stub-less dynamic client and the stub-less generic server
 * interoperate over a real socket with zero generated code.
 */
public class GenericGrpcClientEndToEndTest {

    public static class EchoSbb implements Sbb, SleeEventHandler {
        @InjectRa(name = "grpc-server-ra")
        private volatile RaCommandPort serverRa;

        public EchoSbb() {
        }

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            if (event instanceof GrpcRequestEvent request && serverRa != null) {
                String body = new String(request.payload(), StandardCharsets.UTF_8);
                serverRa.sendCommand(new SendGrpcResponse(request.callId(),
                        body.toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    public static class RequesterSbb implements Sbb, SleeEventHandler {
        static final CopyOnWriteArrayList<GrpcInvokeResponseEvent> RESPONSES =
                new CopyOnWriteArrayList<>();
        static volatile CountDownLatch LATCH = new CountDownLatch(1);

        public RequesterSbb() {
        }

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            if (event instanceof GrpcInvokeResponseEvent response) {
                RESPONSES.add(response);
                LATCH.countDown();
            }
        }
    }

    private MicroSleeContainer container;
    private GrpcServerRaEndpoint serverEndpoint;
    private GenericGrpcClientRaEndpoint clientEndpoint;
    private int serverPort;

    @Before
    public void setUp() {
        RequesterSbb.RESPONSES.clear();
        RequesterSbb.LATCH = new CountDownLatch(1);

        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(64)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        container.registerSbbType(EchoSbb.class, EchoSbb::new);
        container.registerSbbType(RequesterSbb.class, RequesterSbb::new);
        container.createIesDispatcher();
        container.mapEventToSbb(GrpcRequestEvent.class, "EchoSbb");
        container.mapEventToSbb(GrpcInvokeResponseEvent.class, "RequesterSbb");

        GrpcServerRa serverRa = new GrpcServerRa();
        serverRa.setHost("127.0.0.1");
        serverRa.setPort(0);
        serverEndpoint = new GrpcServerRaEndpoint(serverRa);
        container.registerRa(serverEndpoint, serverEndpoint);
        serverPort = serverRa.port();

        clientEndpoint = new GenericGrpcClientRaEndpoint();
        container.registerRa(clientEndpoint, clientEndpoint);
    }

    @After
    public void tearDown() {
        clientEndpoint.deactivate();
        serverEndpoint.deactivate();
        container.stop();
    }

    @Test
    public void dynamicClientCallsGenericServerThroughSbbs() throws Exception {
        clientEndpoint.sendCommand(new InvokeGrpc(
                "corr-1", "127.0.0.1:" + serverPort, "any.Service/Echo",
                "round trip".getBytes(StandardCharsets.UTF_8)));

        assertTrue("no response within 10s",
                RequesterSbb.LATCH.await(10, TimeUnit.SECONDS));
        GrpcInvokeResponseEvent response = RequesterSbb.RESPONSES.get(0);
        assertTrue("expected OK, got " + response.statusCode()
                + " " + response.statusDescription(), response.isOk());
        assertEquals("ROUND TRIP", new String(response.payload(), StandardCharsets.UTF_8));
        assertEquals("corr-1", response.correlationId());
    }

    @Test
    public void unreachableTargetYieldsErrorStatusEvent() throws Exception {
        clientEndpoint.sendCommand(new InvokeGrpc(
                "corr-err", "127.0.0.1:1", "any.Service/Echo",
                "x".getBytes(StandardCharsets.UTF_8), 2_000));

        assertTrue(RequesterSbb.LATCH.await(10, TimeUnit.SECONDS));
        GrpcInvokeResponseEvent response = RequesterSbb.RESPONSES.get(0);
        assertEquals(false, response.isOk());
    }
}
