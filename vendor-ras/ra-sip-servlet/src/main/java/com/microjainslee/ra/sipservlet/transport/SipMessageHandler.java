package com.microjainslee.ra.sipservlet.transport;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/** Netty handler: extracts raw bytes and pushes to message sink. */
final class SipMessageHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = LogManager.getLogger(SipMessageHandler.class);
    private final Consumer<byte[]> messageSink;

    SipMessageHandler(Consumer<byte[]> messageSink) { this.messageSink = messageSink; }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        byte[] raw;
        if (msg instanceof DatagramPacket dp) {
            ByteBuf buf = dp.content();
            raw = new byte[buf.readableBytes()];
            buf.readBytes(raw);
        } else if (msg instanceof ByteBuf bb) {
            raw = new byte[bb.readableBytes()];
            bb.readBytes(raw);
        } else {
            LOG.trace("Unhandled message type: {}", msg.getClass());
            return;
        }
        messageSink.accept(raw);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.warn("SIP transport error", cause);
        ctx.close();
    }
}
