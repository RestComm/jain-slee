package com.microjainslee.ra.sipservlet;

/** Configuration record for SIP-Servlet RA. */
public record SipRaConfig(
    String host,
    int tcpPort,
    int udpPort,
    int sctpPort,
    int ringBufferSize,
    int virtualThreads,
    long nettyBossThreads,
    long nettyWorkerThreads,
    long nettySoBacklog,
    boolean nettyTcpNoDelay,
    boolean nettySoKeepAlive,
    int nettySoRcvBuf,
    int nettySoSndBuf
) {
    public SipRaConfig() {
        this("0.0.0.0", 5060, 5060, 0, 4096, 0, 1L, 0L, 1024L,
             true, true, 262144, 262144);
    }
}
