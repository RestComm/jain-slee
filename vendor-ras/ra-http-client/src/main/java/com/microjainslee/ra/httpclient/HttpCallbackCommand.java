/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient;

import com.microjainslee.api.OutboundCommand;

/**
 * Outbound command for the HTTP callback RA.
 *
 * <p>Carries the parameters for an async HTTP POST callback so the RA can
 * notify an external system about a completed USSD session.
 * Sent from an SBB via
 * {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.</p>
 */
public record HttpCallbackCommand(
        String sessionId,
        String callbackUrl,
        String responseText) implements OutboundCommand {
}
