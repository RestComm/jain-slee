/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.Message;
import org.jdiameter.client.impl.parser.MessageParser;

/** Netty channel handler: parse raw bytes → JDiameter Message → sink. */
final class DiameterNettyCodec extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger LOG = LogManager.getLogger(DiameterNettyCodec.class);
    private final MessageParser parser;
    private final Consumer<Message> messageSink;

    DiameterNettyCodec(MessageParser parser, Consumer<Message> messageSink) {
        this.parser = parser;
        this.messageSink = messageSink;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) {
        byte[] raw = new byte[buf.readableBytes()];
        buf.readBytes(raw);
        try {
            Message msg = parser.createMessage(raw);
            messageSink.accept(msg);
        } catch (Exception e) {
            LOG.warn("Diameter parse error", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.warn("Diameter transport error", cause);
        ctx.close();
    }
}
