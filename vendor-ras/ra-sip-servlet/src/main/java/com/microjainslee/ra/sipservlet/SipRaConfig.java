package com.microjainslee.ra.sipservlet;

/**
 * Configuration for SIP-Servlet RA.
 * Mutable so it can be populated incrementally at wiring time.
 */
public final class SipRaConfig {

    // ---- network ----
    private String host = "0.0.0.0";
    private int tcpPort = 5060;
    private int udpPort = 5060;
    private int sctpPort = 0;

    // ---- Netty tuning ----
    private long nettyBossThreads = 1L;
    private long nettyWorkerThreads = 0L;
    private long nettySoBacklog = 1024L;
    private boolean nettyTcpNoDelay = true;
    private boolean nettySoKeepAlive = true;
    private int nettySoRcvBuf = 262144;
    private int nettySoSndBuf = 262144;

    // ---- client / outbound ----
    private boolean clientEnabled = false;
    private String outboundProxy;

    // ---- DNS ----
    private boolean dnsEnabled = true;
    private long dnsCacheTtlSecs = 300;

    // ---- STUN / ICE ----
    private String stunServer;
    private int stunPort;           // 0 = default 3478
    private boolean iceEnabled = false;
    private long iceKeepAliveSecs = 30;

    // ---- legacy (kept for backward compat, no longer drives Disruptor) ----
    private int ringBufferSize = 4096;
    private int virtualThreads = 0;

    // ---- accessors ----

    public String host() { return host; }
    public void setHost(String host) { this.host = host; }

    public int tcpPort() { return tcpPort; }
    public void setTcpPort(int tcpPort) { this.tcpPort = tcpPort; }

    public int udpPort() { return udpPort; }
    public void setUdpPort(int udpPort) { this.udpPort = udpPort; }

    public int sctpPort() { return sctpPort; }
    public void setSctpPort(int sctpPort) { this.sctpPort = sctpPort; }

    public long nettyBossThreads() { return nettyBossThreads; }
    public void setNettyBossThreads(long n) { this.nettyBossThreads = n; }

    public long nettyWorkerThreads() { return nettyWorkerThreads; }
    public void setNettyWorkerThreads(long n) { this.nettyWorkerThreads = n; }

    public long nettySoBacklog() { return nettySoBacklog; }
    public void setNettySoBacklog(long n) { this.nettySoBacklog = n; }

    public boolean nettyTcpNoDelay() { return nettyTcpNoDelay; }
    public void setNettyTcpNoDelay(boolean v) { this.nettyTcpNoDelay = v; }

    public boolean nettySoKeepAlive() { return nettySoKeepAlive; }
    public void setNettySoKeepAlive(boolean v) { this.nettySoKeepAlive = v; }

    public int nettySoRcvBuf() { return nettySoRcvBuf; }
    public void setNettySoRcvBuf(int v) { this.nettySoRcvBuf = v; }

    public int nettySoSndBuf() { return nettySoSndBuf; }
    public void setNettySoSndBuf(int v) { this.nettySoSndBuf = v; }

    public boolean clientEnabled() { return clientEnabled; }
    public void setClientEnabled(boolean v) { this.clientEnabled = v; }

    public String outboundProxy() { return outboundProxy; }
    public void setOutboundProxy(String v) { this.outboundProxy = v; }

    // ---- DNS ----
    public boolean dnsEnabled() { return dnsEnabled; }
    public void setDnsEnabled(boolean v) { this.dnsEnabled = v; }

    public long dnsCacheTtlSecs() { return dnsCacheTtlSecs; }
    public void setDnsCacheTtlSecs(long v) { this.dnsCacheTtlSecs = v; }

    // ---- STUN / ICE ----
    public String stunServer() { return stunServer; }
    public void setStunServer(String v) { this.stunServer = v; }

    public int stunPort() { return stunPort; }
    public void setStunPort(int v) { this.stunPort = v; }

    public boolean iceEnabled() { return iceEnabled; }
    public void setIceEnabled(boolean v) { this.iceEnabled = v; }

    public long iceKeepAliveSecs() { return iceKeepAliveSecs; }
    public void setIceKeepAliveSecs(long v) { this.iceKeepAliveSecs = v; }

    public int ringBufferSize() { return ringBufferSize; }
    public void setRingBufferSize(int v) { this.ringBufferSize = v; }

    public int virtualThreads() { return virtualThreads; }
    public void setVirtualThreads(int v) { this.virtualThreads = v; }
}
