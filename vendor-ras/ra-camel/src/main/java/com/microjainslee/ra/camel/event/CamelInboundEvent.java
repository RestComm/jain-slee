/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.event;

import com.microjainslee.api.SleeEvent;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Default event fired for every consumed Camel exchange.
 *
 * @param endpointUri  the consumer endpoint URI the message arrived on
 *                     (lets one SBB switch over multiple sources)
 * @param exchangeId   Camel exchange id — the key an SBB must echo back in
 *                     {@code ReplyToExchange} for in-out consumers
 * @param activityId   SLEE activity id (correlation header value, or the
 *                     exchange id when no correlation is configured)
 * @param body         message body as received from the component
 * @param headers      immutable copy of the message headers
 * @param requiresReply true when the exchange is in-out and an SBB is
 *                     expected to send {@code ReplyToExchange}
 */
public record CamelInboundEvent(
        String endpointUri,
        String exchangeId,
        String activityId,
        Object body,
        Map<String, Object> headers,
        boolean requiresReply) implements SleeEvent {

    /** Body as UTF-8 string (converts byte[] and falls back to toString). */
    public String bodyAsString() {
        if (body == null) {
            return null;
        }
        if (body instanceof String s) {
            return s;
        }
        if (body instanceof byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return String.valueOf(body);
    }

    public Object header(String name) {
        return headers == null ? null : headers.get(name);
    }
}
