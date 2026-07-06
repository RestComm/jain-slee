/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.events;

import java.util.Map;

/** Generic Diameter answer event — any application, any command. */
public record DiameterAnswerEvent(
    String sessionId,
    long applicationId,
    int commandCode,
    long resultCode,
    long hopByHopId,
    long endToEndId,
    String originHost,
    String originRealm,
    Map<Integer, String> avps
) implements DiameterEvent {}
