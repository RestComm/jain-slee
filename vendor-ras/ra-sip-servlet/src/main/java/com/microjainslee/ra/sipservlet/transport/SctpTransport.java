package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public final class SctpTransport extends AbstractNettyTransport {
    public SctpTransport(SipRaConfig config, Consumer<byte[]> sink) { super(config, sink); }

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
}
