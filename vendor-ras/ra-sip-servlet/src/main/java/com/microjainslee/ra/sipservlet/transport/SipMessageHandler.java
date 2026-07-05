/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Netty handler: extracts raw bytes plus the peer address and pushes them
 * to the {@link SipMessageSink}. For stream transports it also registers
 * the peer connection so responses can reuse it (RFC 3261 §18.2.2).
 */
final class SipMessageHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = LogManager.getLogger(SipMessageHandler.class);

    private final SipMessageSink messageSink;
    private final String protocol;
    /** Shared with the owning transport; {@code null} for UDP. */
    private final Map<InetSocketAddress, Channel> peerChannels;

    SipMessageHandler(SipMessageSink messageSink, String protocol,
                      Map<InetSocketAddress, Channel> peerChannels) {
        this.messageSink = messageSink;
        this.protocol = protocol;
        this.peerChannels = peerChannels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        registerPeer(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        unregisterPeer(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        byte[] raw;
        InetSocketAddress peer;
        if (msg instanceof DatagramPacket dp) {
            ByteBuf buf = dp.content();
            raw = new byte[buf.readableBytes()];
            buf.readBytes(raw);
            peer = dp.sender();
        } else if (msg instanceof ByteBuf bb) {
            raw = new byte[bb.readableBytes()];
            bb.readBytes(raw);
            peer = remoteAddress(ctx.channel());
        } else {
            LOG.trace("Unhandled message type: {}", msg.getClass());
            return;
        }
        messageSink.onMessage(raw, peer, protocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.warn("SIP transport error", cause);
        ctx.close();
    }

    private void registerPeer(Channel ch) {
        InetSocketAddress peer = remoteAddress(ch);
        if (peerChannels != null && peer != null) {
            peerChannels.put(peer, ch);
        }
    }

    private void unregisterPeer(Channel ch) {
        InetSocketAddress peer = remoteAddress(ch);
        if (peerChannels != null && peer != null) {
            peerChannels.remove(peer, ch);
        }
    }

    private static InetSocketAddress remoteAddress(Channel ch) {
        return ch.remoteAddress() instanceof InetSocketAddress isa ? isa : null;
    }
}
