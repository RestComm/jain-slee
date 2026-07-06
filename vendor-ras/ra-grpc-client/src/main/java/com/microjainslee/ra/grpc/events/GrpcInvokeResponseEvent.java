/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpc.events;

import com.microjainslee.api.SleeEvent;

/**
 * Result of an {@link InvokeGrpc} command, fired on the activity named by
 * the command's correlation id.
 *
 * @param correlationId     id supplied in the command
 * @param target            "host:port" that was called
 * @param fullMethod        full gRPC method name that was called
 * @param payload           response message bytes ({@code null} on error)
 * @param statusCode        gRPC status code value (0 = OK)
 * @param statusDescription status description ({@code null} when OK)
 */
public record GrpcInvokeResponseEvent(
        String correlationId,
        String target,
        String fullMethod,
        byte[] payload,
        int statusCode,
        String statusDescription) implements SleeEvent {

    public boolean isOk() {
        return statusCode == 0;
    }
}
