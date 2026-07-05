package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public final class UdpTransport extends AbstractNettyTransport {
    public UdpTransport(SipRaConfig config, Consumer<byte[]> sink) { super(config, sink); }

    @Override
    public String protocol() { return "UDP"; }

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
