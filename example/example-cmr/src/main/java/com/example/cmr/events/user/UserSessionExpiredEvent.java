/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.events.user;

import com.example.cmr.events.CmrEvent;
import com.microjainslee.api.annotations.EventType;

import java.time.Instant;

/**
 * Fired by the SLEE timer bridge once a session's JWT TTL elapses.
 * {@code UserSessionSbb} invalidates the session and releases its activity
 * context.
 */
@EventType(name = "UserSessionExpired", vendor = "cmr", version = "1.0")
public record UserSessionExpiredEvent(String sessionId, String username, Instant expiredAt)
        implements CmrEvent {

    @Override
    public String initiator() {
        return username;
    }
}
