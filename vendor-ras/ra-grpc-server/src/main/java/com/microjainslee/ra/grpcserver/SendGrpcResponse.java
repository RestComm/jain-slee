/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver;

/** Complete a pending call with OK status and this response message. */
public record SendGrpcResponse(String callId, byte[] payload) implements GrpcServerCommand {
}
