/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.OutboundCommand;

/**
 * Outbound command for the gRPC menu RA.
 * Carries the USSD session parameters so the RA can perform
 * an async upstream menu lookup via
 * {@link GrpcMenuResourceAdaptor#requestMenu(String, String, String, ActivityContextInterface)}.
 *
 * <p>Sent from an SBB via {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.</p>
 */
public record GrpcMenuCommand(
        String sessionId,
        String msisdn,
        String ussdString,
        ActivityContextInterface responseAci) implements OutboundCommand {
}
