/*
 * micro-jainslee 1.1.0 -- example application (example-spring-ussdgw)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Fired by the gRPC RA back onto the USSD session activity when the
 * upstream menu lookup completes.
 */
@EventType(name = "GrpcMenuResponse", vendor = "com.example.ussddemo", version = "1.0")
public final class GrpcMenuResponseEvent implements SleeEvent {

    private final String sessionId;
    private final String status;
    private final String menuText;
    private final String error;

    public GrpcMenuResponseEvent(String sessionId, String status, String menuText,
                                 String error) {
        this.sessionId = sessionId;
        this.status = status;
        this.menuText = menuText;
        this.error = error;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getStatus() {
        return status;
    }

    public String getMenuText() {
        return menuText;
    }

    public String getError() {
        return error;
    }
}
