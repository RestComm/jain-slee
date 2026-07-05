/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.command;

import java.util.Map;

/**
 * Completes a pending in-out consumer exchange. {@code exchangeId} must be
 * the value carried by the triggering {@code CamelInboundEvent}. Sending a
 * reply for an unknown/expired exchange is a warn-and-drop no-op.
 */
public record ReplyToExchange(String exchangeId, Object body, Map<String, Object> headers)
        implements CamelCommand {

    public ReplyToExchange(String exchangeId, Object body) {
        this(exchangeId, body, Map.of());
    }
}
