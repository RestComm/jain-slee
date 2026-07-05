/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpserver.events;

import com.microjainslee.api.SleeEvent;

/**
 * Builds the initial {@link SleeEvent} for an HTTP session begin request.
 * Applications supply the concrete event type (e.g. {@code HttpUssdBeginEvent}).
 */
@FunctionalInterface
public interface HttpBeginEventFactory {

    SleeEvent createBeginEvent(String sessionId, String msisdn, String ussdString,
                               String callbackUrl);
}
