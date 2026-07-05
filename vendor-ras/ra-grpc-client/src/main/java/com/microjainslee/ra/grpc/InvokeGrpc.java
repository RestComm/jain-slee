/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpc;

import com.microjainslee.api.OutboundCommand;

/**
 * Generic dynamic gRPC unary call — no generated stubs needed. The
 * response arrives as a {@link GrpcInvokeResponseEvent} on the activity
 * named {@code correlationId}. Payload bytes are protobuf wire format;
 * encoding/decoding is the application's concern.
 *
 * @param correlationId  activity id the response event is fired on
 * @param target         "host:port" of the gRPC server
 * @param fullMethod     e.g. {@code "billing.ChargingService/Charge"}
 * @param payload        request message bytes
 * @param deadlineMillis per-call deadline (&le;0 → RA default)
 */
public record InvokeGrpc(String correlationId, String target, String fullMethod,
                         byte[] payload, long deadlineMillis) implements OutboundCommand {

    public InvokeGrpc(String correlationId, String target, String fullMethod, byte[] payload) {
        this(correlationId, target, fullMethod, payload, 0L);
    }
}
