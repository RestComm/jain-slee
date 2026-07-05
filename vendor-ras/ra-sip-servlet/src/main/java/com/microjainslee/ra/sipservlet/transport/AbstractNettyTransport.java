/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractNettyTransport implements SipTransport {
    protected final Logger log = LogManager.getLogger(getClass());
    protected final SipRaConfig config;
    protected final SipMessageSink messageSink;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;
    protected Channel channel;

    /**
     * peer → accepted/connected channel, for stream transports (TCP/TLS/
     * SCTP). Responses reuse the connection the request arrived on
     * (RFC 3261 §18.2.2). UDP never populates this map.
     */
    protected final Map<InetSocketAddress, Channel> peerChannels = new ConcurrentHashMap<>();

    protected AbstractNettyTransport(SipRaConfig config, SipMessageSink messageSink) {
        this.config = config;
        this.messageSink = messageSink;
    }

    /** Stream pipelines get SIP framing; datagram pipelines override. */
    protected ChannelInitializer<Channel> channelInitializer() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline()
                  .addLast(new SipTcpFrameDecoder())
                  .addLast(new SipMessageHandler(messageSink, protocol(), peerChannels));
            }
        };
    }

    @Override
    public boolean send(byte[] data, InetSocketAddress target) {
        if (data == null || target == null) {
            return false;
        }
        Channel serverChannel = this.channel;
        if (serverChannel != null && serverChannel instanceof io.netty.channel.socket.DatagramChannel) {
            serverChannel.writeAndFlush(
                    new DatagramPacket(Unpooled.wrappedBuffer(data), target));
            return true;
        }
        Channel peer = peerChannels.get(target);
        if (peer != null && peer.isActive()) {
            peer.writeAndFlush(Unpooled.wrappedBuffer(data));
            return true;
        }
        return connectAndSend(data, target);
    }

    /**
     * No existing connection to {@code target}. Stream transports may
     * open a client connection here; the default declines.
     */
    protected boolean connectAndSend(byte[] data, InetSocketAddress target) {
        log.warn("[{}] no connection to {} and client connect not supported — dropping {} bytes",
                protocol(), target, data.length);
        return false;
    }

    @Override
    public void stop() {
        for (Channel peer : peerChannels.values()) {
            peer.close();
        }
        peerChannels.clear();
        if (channel != null) {
            channel.close().awaitUninterruptibly();
            channel = null;
        }
        if (workerGroup != null) { workerGroup.shutdownGracefully(); workerGroup = null; }
        if (bossGroup != null) { bossGroup.shutdownGracefully(); bossGroup = null; }
        log.info("[{}] stopped", protocol());
    }
}
