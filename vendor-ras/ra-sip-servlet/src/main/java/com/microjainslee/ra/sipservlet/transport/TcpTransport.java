/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public final class TcpTransport extends AbstractNettyTransport {
    public TcpTransport(SipRaConfig config, SipMessageSink sink) { super(config, sink); }

    @Override
    public String protocol() { return "TCP"; }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup((int) config.nettyBossThreads());
        workerGroup = new NioEventLoopGroup((int) config.nettyWorkerThreads());
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(channelInitializer())
         .option(ChannelOption.SO_BACKLOG, (int) config.nettySoBacklog())
         .childOption(ChannelOption.TCP_NODELAY, config.nettyTcpNoDelay())
         .childOption(ChannelOption.SO_KEEPALIVE, config.nettySoKeepAlive())
         .childOption(ChannelOption.SO_RCVBUF, config.nettySoRcvBuf())
         .childOption(ChannelOption.SO_SNDBUF, config.nettySoSndBuf());
        try {
            channel = b.bind(new InetSocketAddress(config.host(), config.tcpPort()))
                        .sync().channel();
            log.info("[TCP] listening on {}:{}", config.host(), config.tcpPort());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("TCP bind interrupted", e);
        }
    }

    @Override
    protected boolean connectAndSend(byte[] data, InetSocketAddress target) {
        if (!config.clientEnabled() || workerGroup == null) {
            return super.connectAndSend(data, target);
        }
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
         .channel(NioSocketChannel.class)
         .handler(channelInitializer())
         .option(ChannelOption.TCP_NODELAY, config.nettyTcpNoDelay());
        ChannelFuture connect = b.connect(target);
        connect.addListener(f -> {
            if (f.isSuccess()) {
                Channel ch = connect.channel();
                peerChannels.put(target, ch);
                ch.writeAndFlush(Unpooled.wrappedBuffer(data));
            } else {
                log.warn("[TCP] connect to {} failed: {}", target,
                        f.cause() == null ? "?" : f.cause().getMessage());
            }
        });
        return true;
    }
}
