/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.transport;

/** Transport abstraction for Diameter (TCP, SCTP). */
public interface DiameterTransport {
    String protocol();
    void start();
    void stop();
}
