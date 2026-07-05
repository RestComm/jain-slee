/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.command;

import com.microjainslee.api.OutboundCommand;

/** Sealed hierarchy of Diameter outbound commands sent from SBB to RA. */
public sealed interface DiameterCommand extends OutboundCommand
        permits SendDiameterAnswer, SendDiameterRequest {
    String sessionId();
    long applicationId();
}
