/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.event;

import java.util.Map;

/** Generic Diameter request event — any application, any command. */
public record DiameterRequestEvent(
    String sessionId,
    long applicationId,
    int commandCode,
    long hopByHopId,
    long endToEndId,
    String originHost,
    String originRealm,
    String destinationHost,
    String destinationRealm,
    Map<Integer, String> avps   // avpCode → string value
) implements DiameterEvent {}
