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
 * Final USSD menu text ready to be sent back toward the subscriber (MAP USSD response).
 */
@EventType(name = "UssdResponse", vendor = "com.example.ussddemo", version = "1.0")
public final class UssdResponseEvent implements SleeEvent {

    private final String sessionId;
    private final String responseText;

    public UssdResponseEvent(String sessionId, String responseText) {
        this.sessionId = sessionId;
        this.responseText = responseText;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getResponseText() {
        return responseText;
    }
}
