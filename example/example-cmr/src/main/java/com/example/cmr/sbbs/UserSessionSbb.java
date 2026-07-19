/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.sbbs;

import com.example.cmr.events.user.UserLoginEvent;
import com.example.cmr.events.user.UserSessionExpiredEvent;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.SbbAnnotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks admin sessions for audit and dashboard metrics. JWTs are stateless
 * (validated by {@code JwtAuthRa}), so this SBB is not on the auth critical
 * path — it observes {@code UserLogin}/{@code UserSessionExpired} to keep a
 * live count and an audit trail, the CMR analogue of a telecom dialog SBB.
 */
@SbbAnnotation(name = "UserSessionSbb", vendor = "cmr", version = "1.0")
public final class UserSessionSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(UserSessionSbb.class);

    /** sessionId → username, shared across pooled entities. */
    private static final ConcurrentHashMap<String, String> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof UserLoginEvent e) {
            SESSIONS.put(e.sessionId(), e.username());
            LOG.info("[session] login user={} ip={} ttl={}s active={}",
                    e.username(), e.remoteIp(), e.ttlSeconds(), SESSIONS.size());
        } else if (event instanceof UserSessionExpiredEvent e) {
            SESSIONS.remove(e.sessionId());
            LOG.info("[session] expired user={} active={}", e.username(), SESSIONS.size());
        }
    }

    /** Live session count — surfaced on the admin dashboard. */
    public static int activeSessions() {
        return SESSIONS.size();
    }
}
