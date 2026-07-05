/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

public final class UdpTransport extends AbstractNettyTransport {
    public UdpTransport(SipRaConfig config, SipMessageSink sink) { super(config, sink); }

    @Override
    public String protocol() { return "UDP"; }

    @Override
    protected ChannelInitializer<Channel> channelInitializer() {
        // Datagram pipeline — one packet per message, no stream framing.
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline().addLast(new SipMessageHandler(messageSink, protocol(), null));
            }
        };
    }

    @Override
    public void start() {
        workerGroup = new NioEventLoopGroup((int) config.nettyWorkerThreads());
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
         .channel(NioDatagramChannel.class)
         .handler(channelInitializer())
         .option(ChannelOption.SO_BROADCAST, true)
         .option(ChannelOption.SO_RCVBUF, config.nettySoRcvBuf())
         .option(ChannelOption.SO_SNDBUF, config.nettySoSndBuf());
        try {
            channel = b.bind(new InetSocketAddress(config.host(), config.udpPort()))
                        .sync().channel();
            log.info("[UDP] listening on {}:{}", config.host(), config.udpPort());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("UDP bind interrupted", e);
        }
    }
}
