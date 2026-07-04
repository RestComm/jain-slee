/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Internal event fired by the gRPC RA when a menu lookup starts.
 * Routed to {@code GrpcClientSbb} for processing.
 */
@EventType(name = "GrpcMenuRequest", vendor = "com.example.ussddemo", version = "1.0")
public final class GrpcMenuRequestEvent implements SleeEvent {

    private final String sessionId;
    private final String msisdn;
    private final String ussdString;

    public GrpcMenuRequestEvent(String sessionId, String msisdn, String ussdString) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.ussdString = ussdString;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getUssdString() {
        return ussdString;
    }
}
