/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.command;

import java.util.Map;

/** Send an outbound Diameter request to a peer (client-initiated). */
public record SendDiameterRequest(
    String sessionId,
    long applicationId,
    int commandCode,
    String destinationHost,
    String destinationRealm,
    Map<Integer, String> avps
) implements DiameterCommand {}
