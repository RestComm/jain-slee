/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus-ussdgw)
 */

package com.example.ussddemo.quarkus.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Internal MAP/USSD begin routed from {@code HttpServerSbb} to
 * {@code Ss7UssdIngressSbb} after subscriber profile lookup.
 */
@EventType(name = "Ss7UssdBegin", vendor = "com.example.ussddemo.quarkus", version = "1.0")
public final class Ss7UssdBeginEvent implements SleeEvent {

    private final String sessionId;
    private final String msisdn;
    private final String ussdString;
    private final String menuTier;

    public Ss7UssdBeginEvent(String sessionId, String msisdn, String ussdString,
                             String menuTier) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.ussdString = ussdString;
        this.menuTier = menuTier;
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

    public String getMenuTier() {
        return menuTier;
    }
}
