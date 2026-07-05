/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SipTcpFrameDecoderTest {

    private static final String OPTIONS =
            "OPTIONS sip:bob@example.com SIP/2.0\r\n"
            + "Via: SIP/2.0/TCP client.example.com:5060;branch=z9hG4bK1\r\n"
            + "Max-Forwards: 70\r\n"
            + "To: <sip:bob@example.com>\r\n"
            + "From: <sip:alice@example.com>;tag=1\r\n"
            + "Call-ID: a1@example.com\r\n"
            + "CSeq: 1 OPTIONS\r\n"
            + "Content-Length: 0\r\n"
            + "\r\n";

    private static final String BODY = "v=0\r\no=- 0 0 IN IP4 127.0.0.1\r\n";

    private static final String INVITE_WITH_BODY =
            "INVITE sip:bob@example.com SIP/2.0\r\n"
            + "Via: SIP/2.0/TCP client.example.com:5060;branch=z9hG4bK2\r\n"
            + "Max-Forwards: 70\r\n"
            + "To: <sip:bob@example.com>\r\n"
            + "From: <sip:alice@example.com>;tag=2\r\n"
            + "Call-ID: a2@example.com\r\n"
            + "CSeq: 1 INVITE\r\n"
            + "Content-Type: application/sdp\r\n"
            + "Content-Length: " + BODY.length() + "\r\n"
            + "\r\n"
            + BODY;

    @Test
    public void framesSingleMessage() {
        EmbeddedChannel ch = new EmbeddedChannel(new SipTcpFrameDecoder());
        ch.writeInbound(buf(OPTIONS));
        assertEquals(OPTIONS, readFrame(ch));
        assertNull(ch.readInbound());
    }

    @Test
    public void framesPipelinedMessages() {
        EmbeddedChannel ch = new EmbeddedChannel(new SipTcpFrameDecoder());
        ch.writeInbound(buf(OPTIONS + INVITE_WITH_BODY));
        assertEquals(OPTIONS, readFrame(ch));
        assertEquals(INVITE_WITH_BODY, readFrame(ch));
        assertNull(ch.readInbound());
    }

    @Test
    public void reassemblesFragmentedMessage() {
        EmbeddedChannel ch = new EmbeddedChannel(new SipTcpFrameDecoder());
        String msg = INVITE_WITH_BODY;
        int cut = msg.indexOf("Content-Length");
        ch.writeInbound(buf(msg.substring(0, cut)));
        assertNull(ch.readInbound());
        int cut2 = msg.length() - 5; // split inside the body too
        ch.writeInbound(buf(msg.substring(cut, cut2)));
        assertNull(ch.readInbound());
        ch.writeInbound(buf(msg.substring(cut2)));
        assertEquals(msg, readFrame(ch));
    }

    @Test
    public void swallowsCrlfKeepAlives() {
        EmbeddedChannel ch = new EmbeddedChannel(new SipTcpFrameDecoder());
        ch.writeInbound(buf("\r\n\r\n" + OPTIONS));
        assertEquals(OPTIONS, readFrame(ch));
        assertNull(ch.readInbound());
    }

    private static ByteBuf buf(String s) {
        return Unpooled.copiedBuffer(s, StandardCharsets.US_ASCII);
    }

    private static String readFrame(EmbeddedChannel ch) {
        ByteBuf frame = ch.readInbound();
        try {
            return frame.toString(StandardCharsets.US_ASCII);
        } finally {
            frame.release();
        }
    }
}
