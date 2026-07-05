/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver;

/**
 * Complete a pending call with a gRPC error status.
 *
 * @param callId      call to fail
 * @param statusCode  gRPC status code value (e.g. 5 = NOT_FOUND,
 *                    13 = INTERNAL — see io.grpc.Status.Code)
 * @param description human-readable status description
 */
public record SendGrpcError(String callId, int statusCode, String description)
        implements GrpcServerCommand {
}
