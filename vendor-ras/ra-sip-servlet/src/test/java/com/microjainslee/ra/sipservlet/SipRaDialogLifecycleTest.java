/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.Address;
import com.microjainslee.api.RaBootstrapPort;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.sipservlet.events.SipByeEvent;
import com.microjainslee.ra.sipservlet.events.SipInviteEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Verifies the dialog lifecycle of the RA without any network I/O:
 * inbound INVITE creates dialog state and fires a typed event; BYE fires
 * the event, releases the dialog and ends the SLEE activity.
 */
public class SipRaDialogLifecycleTest {

    private static final String CALL_ID = "lifecycle-1@10.0.0.9";
    private static final InetSocketAddress PEER = new InetSocketAddress("127.0.0.1", 45061);

    private static final class RecordingBootstrapPort implements RaBootstrapPort {
        final List<SleeEvent> firedEvents = new ArrayList<>();
        final List<String> endedActivities = new ArrayList<>();

        @Override
        public ActivityHandle createActivityHandle(String id) {
            return () -> id;
        }

        @Override
        public void fireEvent(SleeEvent event, ActivityHandle handle, Address address) {
            firedEvents.add(event);
        }

        @Override
        public void endActivity(ActivityHandle handle) {
            endedActivities.add(handle.getId());
        }
    }

    private SipServletResourceAdaptor ra;
    private RecordingBootstrapPort bootstrap;

    @Before
    public void setUp() {
        ra = new SipServletResourceAdaptor();
        bootstrap = new RecordingBootstrapPort();
        SipRaConfig config = new SipRaConfig();
        // no listeners — pure in-process lifecycle test
        config.setUdpPort(0);
        config.setTcpPort(0);
        config.setSctpPort(0);
        config.setTlsPort(0);
        config.setDnsEnabled(false);
        ra.setConfig(config);
        ra.setBootstrapPort(bootstrap);
        ra.raConfigure();
        ra.raActive();
    }

    @After
    public void tearDown() {
        ra.raUnconfigure();
    }

    @Test
    public void inviteCreatesDialogAndFiresTypedEvent() {
        ra.onRawMessage(invite(), PEER, "UDP");

        assertEquals(1, bootstrap.firedEvents.size());
        assertTrue(bootstrap.firedEvents.get(0) instanceof SipInviteEvent);
        var dialog = ra.dialogRegistry().find(CALL_ID);
        assertNotNull("dialog state must be recorded", dialog);
        assertEquals(PEER, dialog.peer());
        assertEquals("UDP", dialog.transport());
        assertTrue("activity must still be alive", bootstrap.endedActivities.isEmpty());
    }

    @Test
    public void byeFiresEventThenEndsDialogAndActivity() {
        ra.onRawMessage(invite(), PEER, "UDP");
        ra.onRawMessage(bye(), PEER, "UDP");

        assertEquals(2, bootstrap.firedEvents.size());
        assertTrue(bootstrap.firedEvents.get(1) instanceof SipByeEvent);
        assertNull("dialog state must be released", ra.dialogRegistry().find(CALL_ID));
        assertEquals(List.of(CALL_ID), bootstrap.endedActivities);
    }

    @Test
    public void raInactiveClearsAllDialogState() {
        ra.onRawMessage(invite(), PEER, "UDP");
        ra.raInactive();
        assertEquals(0, ra.dialogRegistry().size());
    }

    private static byte[] invite() {
        return ("INVITE sip:gw@127.0.0.1:5060 SIP/2.0\r\n"
                + "Via: SIP/2.0/UDP 10.0.0.9:5060;branch=z9hG4bKlc1\r\n"
                + "Max-Forwards: 70\r\n"
                + "To: <sip:gw@example.com>\r\n"
                + "From: <sip:alice@example.com>;tag=lct1\r\n"
                + "Call-ID: " + CALL_ID + "\r\n"
                + "CSeq: 1 INVITE\r\n"
                + "Contact: <sip:alice@10.0.0.9:5060>\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n").getBytes(StandardCharsets.US_ASCII);
    }

    private static byte[] bye() {
        return ("BYE sip:gw@127.0.0.1:5060 SIP/2.0\r\n"
                + "Via: SIP/2.0/UDP 10.0.0.9:5060;branch=z9hG4bKlc2\r\n"
                + "Max-Forwards: 70\r\n"
                + "To: <sip:gw@example.com>;tag=gwtag\r\n"
                + "From: <sip:alice@example.com>;tag=lct1\r\n"
                + "Call-ID: " + CALL_ID + "\r\n"
                + "CSeq: 2 BYE\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n").getBytes(StandardCharsets.US_ASCII);
    }
}
