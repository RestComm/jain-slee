/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.command;

import java.util.Map;

/** Fire-and-forget producer send: {@code template.sendBodyAndHeaders(uri, ...)}. */
public record SendToEndpoint(String uri, Object body, Map<String, Object> headers)
        implements CamelCommand {

    public SendToEndpoint(String uri, Object body) {
        this(uri, body, Map.of());
    }
}
