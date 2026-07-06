/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver.events;

import com.microjainslee.api.SleeEvent;

import java.util.Map;

/**
 * A unary gRPC request received by the generic server RA.
 *
 * <p>The RA is protocol-generic: {@code payload} is the raw request
 * message bytes (protobuf wire format). The application decodes it with
 * its own generated classes (or any schema tech) inside the SBB / a thin
 * mapper — the RA never depends on generated stubs.</p>
 *
 * @param callId     unique id of this call — echo it back in
 *                   {@code SendGrpcResponse} / {@code SendGrpcError}
 * @param fullMethod full gRPC method name, e.g. {@code "ussd.UssdMenuService/ResolveMenu"}
 * @param payload    raw request message bytes
 * @param metadata   ASCII metadata (headers) of the call
 * @param activityId SLEE activity id (correlation metadata value, or the
 *                   call id when no correlation key is configured)
 */
public record GrpcRequestEvent(
        String callId,
        String fullMethod,
        byte[] payload,
        Map<String, String> metadata,
        String activityId) implements SleeEvent {
}
