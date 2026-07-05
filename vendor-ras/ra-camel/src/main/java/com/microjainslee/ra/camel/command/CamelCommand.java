/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.command;

import com.microjainslee.api.OutboundCommand;

import java.util.Map;

/**
 * Commands an SBB may send to the generic Camel RA.
 *
 * <ul>
 *   <li>{@link SendToEndpoint} — fire-and-forget producer send (InOnly).</li>
 *   <li>{@link RequestFromEndpoint} — async request/reply producer call
 *       (InOut); the response comes back as a {@code CamelResponseEvent}
 *       on the activity named by {@code correlationId}.</li>
 *   <li>{@link ReplyToExchange} — complete a pending in-out <i>consumer</i>
 *       exchange (e.g. answer an HTTP request that arrived through
 *       {@code platform-http:}).</li>
 *   <li>{@link EndCamelActivity} — explicitly end a correlated activity
 *       when the application-level session is over.</li>
 * </ul>
 */
public sealed interface CamelCommand extends OutboundCommand
        permits SendToEndpoint, RequestFromEndpoint, ReplyToExchange, EndCamelActivity {
}
