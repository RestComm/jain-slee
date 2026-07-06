/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.events;

import com.microjainslee.api.SleeEvent;

import java.util.Map;

/**
 * Response to a {@code RequestFromEndpoint} command (in-out producer call).
 * Fired on the activity named by the command's correlation id.
 *
 * @param correlationId the id the SBB supplied in the command
 * @param endpointUri   producer endpoint that was called
 * @param body          response body ({@code null} on error)
 * @param headers       response headers ({@code null} on error)
 * @param error         error message, or {@code null} on success
 */
public record CamelResponseEvent(
        String correlationId,
        String endpointUri,
        Object body,
        Map<String, Object> headers,
        String error) implements SleeEvent {

    public boolean isSuccess() {
        return error == null;
    }
}
