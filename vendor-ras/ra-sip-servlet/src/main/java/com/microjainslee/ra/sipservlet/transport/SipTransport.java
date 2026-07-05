/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import java.net.InetSocketAddress;

/**
 * SIP transport abstraction. Deliberately narrow so the Netty
 * implementation can later be swapped for a DPDK/user-space datapath
 * without touching the RA: lifecycle, a peer-addressed send primitive,
 * and the protocol name.
 */
public interface SipTransport {

    void start();

    void stop();

    /** {@code "UDP"}, {@code "TCP"}, {@code "TLS"} or {@code "SCTP"}. */
    String protocol();

    /**
     * Send raw SIP bytes to {@code target}. For stream transports the
     * existing peer connection is reused when one is registered (RFC 3261
     * §18.2.2 — responses go back on the connection the request arrived
     * on); otherwise a client connection is attempted when the transport
     * supports it.
     *
     * @return {@code true} when the write was handed to the channel,
     *         {@code false} when no route to the peer exists
     */
    boolean send(byte[] data, InetSocketAddress target);
}
