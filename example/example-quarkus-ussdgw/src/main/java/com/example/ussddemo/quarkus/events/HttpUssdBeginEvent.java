/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Fired by the HTTP ingress RA when an external USSD gateway posts
 * {@code /api/ussd/begin-callback} (or the polling {@code /begin} path).
 */
@EventType(name = "HttpUssdBegin", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public final class HttpUssdBeginEvent implements SleeEvent {

    private final String sessionId;
    private final String msisdn;
    private final String ussdString;
    private final String callbackUrl;

    public HttpUssdBeginEvent(String sessionId, String msisdn, String ussdString,
                              String callbackUrl) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.ussdString = ussdString;
        this.callbackUrl = callbackUrl;
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

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
