/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.user;

import com.example.cmr.events.CmrEvent;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired after a successful admin login (credentials are verified in the
 * router; the JWT is already issued). Purely an audit/telemetry signal —
 * {@code UserSessionSbb} records the session and arms an expiry timer.
 */
@EventType(name = "UserLogin", vendor = "cmr", version = "1.0")
public record UserLoginEvent(String username, String sessionId, String remoteIp,
                             long ttlSeconds, Instant firedAt)
        implements CmrEvent {

    public UserLoginEvent(String username, String sessionId, String remoteIp, long ttlSeconds) {
        this(username, sessionId, remoteIp, ttlSeconds, Instant.now());
    }

    @Override
    public String initiator() {
        return username;
    }
}
