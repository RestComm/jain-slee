/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.command;

import java.util.Map;

/**
 * Async in-out producer call. The RA invokes the endpoint on a worker
 * thread and fires a {@code CamelResponseEvent} carrying
 * {@code correlationId} on the activity of the same name (created on
 * demand), so the requesting SBB — or any SBB mapped to
 * {@code CamelResponseEvent} — receives the reply without blocking its
 * entity thread.
 */
public record RequestFromEndpoint(String correlationId, String uri,
                                  Object body, Map<String, Object> headers)
        implements CamelCommand {

    public RequestFromEndpoint(String correlationId, String uri, Object body) {
        this(correlationId, uri, body, Map.of());
    }
}
