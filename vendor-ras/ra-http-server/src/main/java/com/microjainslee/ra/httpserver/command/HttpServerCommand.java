/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed hierarchy of outbound commands targeting the HTTP server RA.
 */
public sealed interface HttpServerCommand extends OutboundCommand
        permits HttpServerCommand.HttpResponseCommand {

    /**
     * Command to send an HTTP response back to a pending request identified
     * by its {@code sessionId}.
     */
    record HttpResponseCommand(String sessionId, int statusCode, String contentType,
                               String body) implements HttpServerCommand, OutboundCommand {
    }
}
