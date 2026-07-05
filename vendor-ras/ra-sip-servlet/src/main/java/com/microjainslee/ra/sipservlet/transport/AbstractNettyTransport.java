package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

abstract class AbstractNettyTransport implements SipTransport {
    protected final Logger log = LogManager.getLogger(getClass());
    protected final SipRaConfig config;
    protected final Consumer<byte[]> messageSink;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workerGroup;
    protected Channel channel;

    protected AbstractNettyTransport(SipRaConfig config, Consumer<byte[]> messageSink) {
        this.config = config;
        this.messageSink = messageSink;
    }

    protected ChannelInitializer<?> channelInitializer() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline().addLast(new SipMessageHandler(messageSink));
            }
        };
    }

    @Override
    public void stop() {
        if (channel != null) {
            channel.close().awaitUninterruptibly();
            channel = null;
        }
        if (workerGroup != null) { workerGroup.shutdownGracefully(); workerGroup = null; }
        if (bossGroup != null) { bossGroup.shutdownGracefully(); bossGroup = null; }
        log.info("[{}] stopped", protocol());
    }
}
