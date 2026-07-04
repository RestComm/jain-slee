/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.api;

/**
 * SBB-to-RA command port. An SBB obtains this port from its
 * {@code ResourceAdaptorContext} and uses it to send outbound commands
 * (e.g. protocol requests) to a Resource Adaptor.
 *
 * <p>
 * This is the SBB-facing half of the 3-port contract. The RA-facing
 * half is {@link RaEndpointPort}.
 *
 * @see OutboundCommand
 * @see RaEndpointPort
 */
public interface RaCommandPort {

    /**
     * Send an outbound command to the RA. The RA processes the command
     * asynchronously — this method returns immediately after queuing.
     *
     * @param command the outbound command to send
     */
    void sendCommand(OutboundCommand command);
}
