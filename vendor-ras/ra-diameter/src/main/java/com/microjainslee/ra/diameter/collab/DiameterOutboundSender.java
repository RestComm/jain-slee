/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.collab;

import com.microjainslee.ra.diameter.command.DiameterCommand;

/** Sends an outbound DiameterCommand to the wire. */
@FunctionalInterface
public interface DiameterOutboundSender {
    void send(DiameterCommand cmd);
}
