/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Final USSD menu text ready to be sent back toward the subscriber (MAP USSD response).
 */
@EventType(name = "UssdResponse", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public final class UssdResponseEvent implements SleeEvent {

    private final String sessionId;
    private final String responseText;

    public UssdResponseEvent(String sessionId, String responseText) {
        this.sessionId = sessionId;
        this.responseText = responseText;
    }

    public String getSessionId() { return sessionId; }
    public String getResponseText() { return responseText; }
}
