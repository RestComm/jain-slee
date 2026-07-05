/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.sipservlet.command.SendResponse;
import com.microjainslee.ra.sipservlet.event.SipInviteEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Full-loop verification over a real UDP socket:
 *
 * <pre>
 *   UDP INVITE → UdpTransport → SipServletResourceAdaptor → fireEvent
 *     → MicroSleeContainer.routeEvent (mapEventToSbb + container IES)
 *     → ProxyTestSbb (@InjectRa command port) → SendResponse(200)
 *     → NettySipOutboundSender → UdpTransport.send → back to this socket
 * </pre>
 *
 * This exercises the exact wiring an application uses: registerSbbType +
 * createIesDispatcher + mapEventToSbb + registerRa, with zero manual
 * entity/attach plumbing.
 */
public class SipEndToEndTest {

    public static class ProxyTestSbb implements Sbb, SleeEventHandler {
        @InjectRa(name = "sip-servlet-ra")
        private volatile RaCommandPort sipRa;

        public ProxyTestSbb() {
        }

        @Override
        public void onEvent(SleeEvent event, ActivityContextInterface aci) {
            if (event instanceof SipInviteEvent invite) {
                RaCommandPort port = this.sipRa;
                if (port != null) {
                    port.sendCommand(new SendResponse(invite.callId(), 200, "OK"));
                }
            }
        }
    }

    private MicroSleeContainer container;
    private SipServletRaEndpoint endpoint;
    private int sipPort;

    @Before
    public void setUp() throws Exception {
        try (DatagramSocket probe = new DatagramSocket(0)) {
            sipPort = probe.getLocalPort();
        }

        container = new MicroSleeContainer(MicroSleeConfiguration.builder()
                .eventRouterBufferSize(64)
                .preferVirtualThreads(false)
                .sbbPerVirtualThread(false)
                .build());
        container.start();
        container.registerSbbType(ProxyTestSbb.class, ProxyTestSbb::new);
        container.createIesDispatcher();
        container.mapEventToSbb(SipInviteEvent.class, "ProxyTestSbb");

        SipRaConfig config = new SipRaConfig();
        config.setHost("127.0.0.1");
        config.setUdpPort(sipPort);
        config.setTcpPort(0);
        config.setSctpPort(0);
        config.setTlsPort(0);
        config.setDnsEnabled(false);

        SipServletResourceAdaptor ra = new SipServletResourceAdaptor();
        endpoint = new SipServletRaEndpoint(ra);
        endpoint.setConfig(config);
        container.registerRa(endpoint, endpoint);
    }

    @After
    public void tearDown() {
        if (endpoint != null) {
            endpoint.deactivate();
        }
        if (container != null) {
            container.stop();
        }
    }

    @Test
    public void inviteOverUdpGetsA200Response() throws Exception {
        String callId = "e2e-" + System.nanoTime() + "@127.0.0.1";
        try (DatagramSocket socket = new DatagramSocket(0,
                InetAddress.getByName("127.0.0.1"))) {
            socket.setSoTimeout(10_000);
            String invite =
                    "INVITE sip:gw@127.0.0.1:" + sipPort + " SIP/2.0\r\n"
                    + "Via: SIP/2.0/UDP 127.0.0.1:" + socket.getLocalPort()
                            + ";branch=z9hG4bKe2e1\r\n"
                    + "Max-Forwards: 70\r\n"
                    + "To: <sip:gw@example.com>\r\n"
                    + "From: <sip:alice@example.com>;tag=e2etag\r\n"
                    + "Call-ID: " + callId + "\r\n"
                    + "CSeq: 1 INVITE\r\n"
                    + "Contact: <sip:alice@127.0.0.1:" + socket.getLocalPort() + ">\r\n"
                    + "Content-Length: 0\r\n"
                    + "\r\n";
            byte[] out = invite.getBytes(StandardCharsets.US_ASCII);
            socket.send(new DatagramPacket(out, out.length,
                    new InetSocketAddress("127.0.0.1", sipPort)));

            byte[] buf = new byte[4096];
            DatagramPacket in = new DatagramPacket(buf, buf.length);
            String response;
            try {
                socket.receive(in);
                response = new String(in.getData(), 0, in.getLength(),
                        StandardCharsets.US_ASCII);
            } catch (SocketTimeoutException e) {
                throw new AssertionError(
                        "No SIP response within 10s — outbound path broken", e);
            }
            assertNotNull(response);
            assertTrue("expected 200 OK, got:\n" + response,
                    response.startsWith("SIP/2.0 200 OK"));
            assertTrue("response must echo the Call-ID", response.contains(callId));
            assertTrue("2xx INVITE response must carry a To tag",
                    response.matches("(?s).*To:[^\r\n]*tag=.*"));
        }
    }
}
