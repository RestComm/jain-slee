/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.diameter.events;

import com.microjainslee.api.SleeEvent;

/** Sealed hierarchy root for all Diameter SLEE events. */
public sealed interface DiameterEvent extends SleeEvent
        permits DiameterRequestEvent, DiameterAnswerEvent {
    String sessionId();
    long applicationId();
}
