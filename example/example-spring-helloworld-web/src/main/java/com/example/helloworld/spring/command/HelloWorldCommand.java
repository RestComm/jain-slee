/*
 * micro-jainslee 1.1.0 -- example application (example-spring-helloworld-web)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.helloworld.spring.command;

import com.microjainslee.api.OutboundCommand;

/**
 * Sealed hierarchy of HelloWorld outbound commands sent from SBB to RA.
 */
public sealed interface HelloWorldCommand extends OutboundCommand
        permits HelloWorldCommand.HttpResponseCommand {

    /**
     * Send an HTTP response back through the HTTP server RA.
     */
    record HttpResponseCommand(String sessionId, int statusCode, String body)
            implements HelloWorldCommand {
    }
}
