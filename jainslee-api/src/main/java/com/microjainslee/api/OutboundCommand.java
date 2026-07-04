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
 * Marker interface for outbound commands sent from an SBB to a
 * Resource Adaptor via {@link RaCommandPort#sendCommand(OutboundCommand)}.
 *
 * <p>
 * Protocol-specific RAs define concrete command types (e.g.
 * {@code SendSmsCommand}, {@code StartCallCommand}) that implement
 * this interface. The RA inspects the command type at runtime and
 * dispatches to the appropriate protocol handler.
 *
 * @see RaCommandPort
 * @see RaEndpointPort
 */
public interface OutboundCommand {
}
