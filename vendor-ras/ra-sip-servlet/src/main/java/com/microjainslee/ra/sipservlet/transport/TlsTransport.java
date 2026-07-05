/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * TLS transport for SIP-over-TLS (SIPS:5061).
 * Note: Actual SSL context configuration (certificates, trust stores)
 * should be wired externally via a configurable SslContext provider.
 */
public final class TlsTransport extends AbstractNettyTransport {

    public TlsTransport(SipRaConfig config, SipMessageSink sink) {
        super(config, sink);
    }

    @Override
    public String protocol() { return "TLS"; }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup((int) config.nettyBossThreads());
        workerGroup = new NioEventLoopGroup((int) config.nettyWorkerThreads());
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(channelInitializer())
         .option(ChannelOption.SO_BACKLOG, (int) config.nettySoBacklog());
        try {
            channel = b.bind(new InetSocketAddress(config.host(), config.tlsPort()))
                        .sync().channel();
            log.info("[TLS] listening on {}:{}", config.host(), config.tlsPort());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("TLS bind interrupted", e);
        }
    }
}
