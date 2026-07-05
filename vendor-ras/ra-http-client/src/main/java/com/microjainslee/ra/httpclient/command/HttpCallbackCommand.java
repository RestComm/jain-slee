/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed outbound command hierarchy for the HTTP callback RA.
 *
 * <p>Sent from an SBB via
 * {@link com.microjainslee.api.RaCommandPort#sendCommand(OutboundCommand)}.
 * The RA endpoint pattern-matches the concrete permitted subtype to
 * dispatch to the correct handler.
 */
public sealed interface HttpCallbackCommand extends OutboundCommand
        permits HttpCallbackCommand.CallbackRequest {

    /**
     * Request an asynchronous HTTP POST callback to an external system.
     *
     * @param sessionId   the SLEE session identifier
     * @param callbackUrl the absolute URL to POST to
     * @param payload     the JSON payload to deliver
     */
    record CallbackRequest(String sessionId, String callbackUrl, String payload)
            implements HttpCallbackCommand, OutboundCommand {
    }
}
