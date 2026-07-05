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

import com.microjainslee.api.ActivityContextInterface;

/**
 * Prepares pooled SBB entities and session state before the HTTP RA fires the
 * initial event on {@link com.microjainslee.api.SleeEndpointPort}.
 */
@FunctionalInterface
public interface HttpServerSessionPreparer {

    void prepare(String sessionId, String callbackUrl, ActivityContextInterface aci);
}
