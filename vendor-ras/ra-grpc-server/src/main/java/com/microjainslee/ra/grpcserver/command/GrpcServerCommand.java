/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver.command;

import com.microjainslee.api.OutboundCommand;
import com.microjainslee.ra.grpcserver.events.GrpcRequestEvent;

/** Commands an SBB sends to the generic gRPC server RA. */
public sealed interface GrpcServerCommand extends OutboundCommand
        permits SendGrpcResponse, SendGrpcError {

    /** The call id from the triggering {@link GrpcRequestEvent}. */
    String callId();
}
