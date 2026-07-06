/*
 * micro-jainslee 1.1.0 -- example application (example-spring-ussdgw)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.spring;

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
