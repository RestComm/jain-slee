package com.microjainslee.ra.sipservlet.transport;

import com.microjainslee.ra.sipservlet.SipRaConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public final class TcpTransport extends AbstractNettyTransport {
    public TcpTransport(SipRaConfig config, Consumer<byte[]> sink) { super(config, sink); }

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
}
