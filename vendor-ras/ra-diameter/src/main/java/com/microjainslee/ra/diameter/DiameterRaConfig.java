/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter;

/** Diameter RA config — camel-generic (any Diameter app). */
public final class DiameterRaConfig {
    private String host = "0.0.0.0";
    private int port = 3868;
    private String realm = "mobicents.org";
    private String originHost = "server.mobicents.org";
    private String productName = "micro-jainslee-diameter";
    private long vendorId = 0;
    private boolean tcpEnabled = true;
    private boolean sctpEnabled = false;

    public DiameterRaConfig() {}

    // --- getters / setters ---
    public String host() { return host; }
    public void setHost(String v) { host = v; }
    public int port() { return port; }
    public void setPort(int v) { port = v; }
    public String realm() { return realm; }
    public void setRealm(String v) { realm = v; }
    public String originHost() { return originHost; }
    public void setOriginHost(String v) { originHost = v; }
    public String productName() { return productName; }
    public void setProductName(String v) { productName = v; }
    public long vendorId() { return vendorId; }
    public void setVendorId(long v) { vendorId = v; }
    public boolean tcpEnabled() { return tcpEnabled; }
    public void setTcpEnabled(boolean v) { tcpEnabled = v; }
    public boolean sctpEnabled() { return sctpEnabled; }
    public void setSctpEnabled(boolean v) { sctpEnabled = v; }
}
