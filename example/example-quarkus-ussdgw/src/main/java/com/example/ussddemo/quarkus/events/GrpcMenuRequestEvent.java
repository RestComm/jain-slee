/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Internal trace event fired by the gRPC RA when a menu lookup starts.
 */
@EventType(name = "GrpcMenuRequest", vendor = "com.example.ussddemo.quarkus", version = "1.0")
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
