/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.transport;

import com.microjainslee.ra.diameter.DiameterRaConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.Message;
import org.jdiameter.client.impl.parser.MessageParser;

/** Netty TCP server transport for Diameter (RFC 6733). */
public final class TcpDiameterTransport implements DiameterTransport {
    private static final Logger LOG = LogManager.getLogger(TcpDiameterTransport.class);
    private static final int MAX_FRAME = 65536;

    private final DiameterRaConfig config;
    private final Consumer<Message> messageSink;
    private EventLoopGroup bossGroup, workerGroup;
    private Channel channel;
    private final MessageParser parser = new MessageParser();

    public TcpDiameterTransport(DiameterRaConfig config, Consumer<Message> messageSink) {
        this.config = config;
        this.messageSink = messageSink;
    }

    @Override public String protocol() { return "TCP"; }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(new ChannelInitializer<>() {
             @Override protected void initChannel(Channel ch) {
                 ch.pipeline().addLast(
                     // Diameter: version(1) + length(3) at offset 1, 3 bytes
                     new LengthFieldBasedFrameDecoder(MAX_FRAME, 1, 3, 0, 0),
                     new DiameterNettyCodec(parser, messageSink));
             }
         })
         .option(ChannelOption.SO_BACKLOG, 1024);
        try {
            channel = b.bind(new InetSocketAddress(config.host(), config.port())).sync().channel();
            LOG.info("[Diameter-TCP] listening on {}:{}", config.host(), config.port());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Diameter TCP bind failed", e);
        }
    }

    @Override public void stop() {
        if (channel != null) { channel.close().awaitUninterruptibly(); channel = null; }
        if (workerGroup != null) { workerGroup.shutdownGracefully(); workerGroup = null; }
        if (bossGroup != null) { bossGroup.shutdownGracefully(); bossGroup = null; }
        LOG.info("[Diameter-TCP] stopped");
    }

    /** Expose parser for outbound encoding. */
    public MessageParser parser() { return parser; }
}
