/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.ra.sipservlet.SipRaConfig;
import com.microjainslee.ra.sipservlet.command.SendBye;
import com.microjainslee.ra.sipservlet.command.SendInvite;
import com.microjainslee.ra.sipservlet.command.SendResponse;
import com.microjainslee.ra.sipservlet.command.SendSdpUpdate;
import com.microjainslee.ra.sipservlet.transport.SipTransport;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.parser.StringMsgParser;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NettySipOutboundSenderTest {

    private static final String CALL_ID = "test-call-1@10.0.0.1";
    private static final InetSocketAddress PEER =
            new InetSocketAddress("127.0.0.1", 45060);

    private static final String INVITE =
            "INVITE sip:gw@127.0.0.1:5060 SIP/2.0\r\n"
            + "Via: SIP/2.0/UDP 10.0.0.1:5060;branch=z9hG4bKtest1\r\n"
            + "Max-Forwards: 70\r\n"
            + "To: <sip:gw@example.com>\r\n"
            + "From: \"Alice\" <sip:alice@example.com>;tag=fromtag1\r\n"
            + "Call-ID: " + CALL_ID + "\r\n"
            + "CSeq: 1 INVITE\r\n"
            + "Contact: <sip:alice@10.0.0.1:5060>\r\n"
            + "Content-Length: 0\r\n"
            + "\r\n";

    private static final class CapturingTransport implements SipTransport {
        final List<String> sentMessages = new ArrayList<>();
        final List<InetSocketAddress> targets = new ArrayList<>();

        @Override public void start() { }
        @Override public void stop() { }
        @Override public String protocol() { return "UDP"; }

        @Override
        public boolean send(byte[] data, InetSocketAddress target) {
            sentMessages.add(new String(data, StandardCharsets.UTF_8));
            targets.add(target);
            return true;
        }
    }

    private DialogRegistry registry;
    private CapturingTransport transport;
    private NettySipOutboundSender sender;

    @Before
    public void setUp() throws Exception {
        registry = new DialogRegistry();
        transport = new CapturingTransport();
        Map<String, SipTransport> transports = new HashMap<>();
        transports.put("UDP", transport);
        SipRaConfig config = new SipRaConfig();
        config.setHost("127.0.0.1");
        config.setUdpPort(5060);
        sender = new NettySipOutboundSender(config, registry, transports);

        SIPMessage invite = new StringMsgParser().parseSIPMessage(
                INVITE.getBytes(StandardCharsets.UTF_8), true, false, null);
        ActivityHandle handle = () -> CALL_ID;
        registry.recordInbound(CALL_ID, handle, invite, PEER, "UDP");
    }

    @Test
    public void sendResponseDerivesFromLastRequestAndTargetsPeer() {
        sender.send(new SendResponse(CALL_ID, 200, "OK"));

        assertEquals(1, transport.sentMessages.size());
        String wire = transport.sentMessages.get(0);
        assertTrue("must be a 200 response, got: " + firstLine(wire),
                wire.startsWith("SIP/2.0 200 OK"));
        assertTrue("Call-ID must be preserved", wire.contains(CALL_ID));
        assertTrue("2xx to INVITE must carry a To tag",
                wire.matches("(?s).*To:[^\r\n]*tag=.*"));
        assertTrue("2xx to INVITE must carry Contact", wire.contains("Contact:"));
        assertEquals(PEER, transport.targets.get(0));
    }

    @Test
    public void sendSdpUpdateProduces200WithSdpBody() {
        String sdp = "v=0\r\no=- 1 1 IN IP4 127.0.0.1\r\n";
        sender.send(new SendSdpUpdate(CALL_ID, sdp));

        String wire = transport.sentMessages.get(0);
        assertTrue(wire.startsWith("SIP/2.0 200"));
        assertTrue(wire.contains("application/sdp"));
        assertTrue(wire.contains("o=- 1 1 IN IP4 127.0.0.1"));
    }

    @Test
    public void sendByeReversesDirectionAndTargetsContact() {
        sender.send(new SendBye(CALL_ID));

        String wire = transport.sentMessages.get(0);
        assertTrue("BYE must target the caller's Contact, got: " + firstLine(wire),
                wire.startsWith("BYE sip:alice@10.0.0.1:5060"));
        // Direction reversed: our From is the original To, our To is the
        // original From (with the caller's tag preserved).
        assertTrue(wire.matches("(?s).*From:[^\r\n]*sip:gw@example\\.com.*"));
        assertTrue(wire.matches("(?s).*To:[^\r\n]*tag=fromtag1.*"));
        assertTrue("locally generated request needs a fresh branch",
                wire.contains("z9hG4bK-"));
        assertEquals(PEER, transport.targets.get(0));
    }

    @Test
    public void sendInviteBuildsFreshRequest() {
        sender.send(new SendInvite("new-call-1", "sip:pbx@127.0.0.1:5062",
                "sip:gw@127.0.0.1", "v=0\r\n"));

        String wire = transport.sentMessages.get(0);
        assertTrue(wire.startsWith("INVITE sip:pbx@127.0.0.1:5062 SIP/2.0"));
        assertTrue(wire.contains("Call-ID: new-call-1"));
        assertTrue(wire.contains("CSeq: 1 INVITE"));
        assertTrue("From must carry a tag", wire.matches("(?s).*From:[^\r\n]*tag=.*"));
        assertNotNull(transport.targets.get(0));
        assertEquals(5062, transport.targets.get(0).getPort());
    }

    @Test
    public void unknownDialogIsDroppedWithoutSending() {
        sender.send(new SendResponse("no-such-call", 200, "OK"));
        assertEquals(0, transport.sentMessages.size());
    }

    private static String firstLine(String wire) {
        int idx = wire.indexOf('\r');
        return idx < 0 ? wire : wire.substring(0, idx);
    }
}
