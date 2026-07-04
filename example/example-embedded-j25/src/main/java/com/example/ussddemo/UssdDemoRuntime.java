/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Lightweight session-tracking runtime used by SBBs for
 * session lifecycle callbacks (fail/completion logging).
 */
public final class UssdDemoRuntime {

    private static final Logger LOG = LogManager.getLogger(UssdDemoRuntime.class);

    public void failSession(String sessionId, String reason) {
        LOG.warn("Session {} failed: {}", sessionId, reason);
    }

    public void completeSession(String sessionId, String responseText) {
        LOG.info("Session {} completed with response: {}", sessionId, responseText);
    }
}
