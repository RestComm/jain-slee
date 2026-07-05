/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.transport;

import java.net.InetSocketAddress;

/**
 * Inbound message sink — carries the raw SIP bytes together with the
 * remote peer address and the transport protocol they arrived on.
 *
 * <p>The peer address is mandatory context: without it a SIP stack cannot
 * route responses (UDP has no connection to answer on), apply
 * {@code received=}/{@code rport} handling, or key per-peer state. The
 * transport name ({@code "UDP"}, {@code "TCP"}, {@code "TLS"},
 * {@code "SCTP"}) lets the outbound path reply over the same transport the
 * request arrived on (RFC 3261 §18.2.2).
 */
@FunctionalInterface
public interface SipMessageSink {

    void onMessage(byte[] raw, InetSocketAddress peer, String transport);
}
