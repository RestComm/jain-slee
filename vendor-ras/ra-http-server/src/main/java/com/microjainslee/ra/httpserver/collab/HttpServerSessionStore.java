/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver.collab;

/**
 * Optional session store for GET polling of HTTP session state.
 */
public interface HttpServerSessionStore {

    SessionSnapshot get(String sessionId);

    interface SessionSnapshot {
        String getStatus();

        String getResponseText();

        String getErrorMessage();
    }
}
