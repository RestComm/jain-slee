/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver;

/**
 * Optional session store for GET {@code /api/ussd/sessions/{id}} polling.
 */
public interface HttpServerSessionStore {

    SessionSnapshot get(String sessionId);

    interface SessionSnapshot {
        String getStatus();

        String getResponseText();

        String getErrorMessage();
    }
}
