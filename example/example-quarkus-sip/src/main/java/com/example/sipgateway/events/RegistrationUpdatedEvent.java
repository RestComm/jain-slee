/*
 * micro-jainslee example-sip-quarkus
 */

package com.example.sipgateway.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Fired when an AoR registration is created, updated, or removed.
 * Carries the Address-of-Record, the Contact URI, and the new
 * expires value (0 = unregistered).
 */
@EventType(name = "RegistrationUpdated", vendor = "com.example.sipgateway", version = "1.0")
public final class RegistrationUpdatedEvent implements SleeEvent {

    private final String aor;
    private final String contactUri;
    private final int expires;

    public RegistrationUpdatedEvent(String aor, String contactUri, int expires) {
        this.aor = aor;
        this.contactUri = contactUri;
        this.expires = expires;
    }

    public String getAor() { return aor; }
    public String getContactUri() { return contactUri; }
    public int getExpires() { return expires; }

    @Override
    public String toString() {
        return "RegistrationUpdatedEvent{aor='" + aor + "', contact='" + contactUri + "', expires=" + expires + '}';
    }
}
