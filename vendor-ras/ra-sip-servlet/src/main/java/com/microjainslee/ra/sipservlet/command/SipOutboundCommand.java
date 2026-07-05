/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed hierarchy of SIP outbound commands sent from SBB to RA
 * via {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.
 *
 * <p>Each command carries at minimum a {@code callId} identifying
 * the target dialog. Concrete command records live in their own
 * source files under this package.</p>
 */
public sealed interface SipOutboundCommand extends OutboundCommand
        permits SendInvite, SendBye, SendResponse, SendAck, SendCancel,
                StartIce, SelectIceCandidate, SendSdpUpdate, SendMediaKeepAlive {

    /** Call-ID identifying the target dialog. */
    String callId();
}

