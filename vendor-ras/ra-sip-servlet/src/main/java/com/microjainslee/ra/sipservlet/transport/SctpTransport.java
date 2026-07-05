/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.nio.NioSctpServerChannel;

import java.net.InetSocketAddress;

public final class SctpTransport extends AbstractNettyTransport {
    public SctpTransport(SipRaConfig config, SipMessageSink sink) { super(config, sink); }

    @Override
    public String protocol() { return "SCTP"; }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup((int) config.nettyBossThreads());
        workerGroup = new NioEventLoopGroup((int) config.nettyWorkerThreads());
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioSctpServerChannel.class)
         .childHandler(channelInitializer())
         .option(ChannelOption.SO_BACKLOG, (int) config.nettySoBacklog())
         .childOption(ChannelOption.SO_RCVBUF, config.nettySoRcvBuf())
         .childOption(ChannelOption.SO_SNDBUF, config.nettySoSndBuf());
        try {
            channel = b.bind(new InetSocketAddress(config.host(), config.sctpPort()))
                        .sync().channel();
            log.info("[SCTP] listening on {}:{}", config.host(), config.sctpPort());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("SCTP bind interrupted", e);
        }
    }

    @Override
    public boolean send(byte[] data, InetSocketAddress target) {
        Channel peer = peerChannels.get(target);
        if (peer != null && peer.isActive()) {
            peer.writeAndFlush(new SctpMessage(0, 0, Unpooled.wrappedBuffer(data)));
            return true;
        }
        log.warn("[SCTP] no association with {} — dropping {} bytes", target, data.length);
        return false;
    }
}
