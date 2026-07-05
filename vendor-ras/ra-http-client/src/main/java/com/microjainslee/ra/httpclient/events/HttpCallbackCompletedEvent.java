/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Fired by the HTTP callback RA when an asynchronous callback POST
 * completes (success or failure).
 *
 * <p>SBBs subscribe to this event to react to callback outcomes —
 * e.g. logging, retry at the application layer, or tearing down
 * session resources.
 */
@EventType(name = "HttpCallbackCompleted", vendor = "com.microjainslee", version = "1.0")
public final class HttpCallbackCompletedEvent implements SleeEvent {

    private final String sessionId;
    private final int statusCode;
    private final String responseBody;
    private final String errorMessage;

    public HttpCallbackCompletedEvent(String sessionId, int statusCode,
                                      String responseBody, String errorMessage) {
        this.sessionId = sessionId;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.errorMessage = errorMessage;
    }

    public String getSessionId() { return sessionId; }
    public int getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
    public String getErrorMessage() { return errorMessage; }

    @Override
    public String toString() {
        return "HttpCallbackCompletedEvent{sessionId='" + sessionId
                + "', statusCode=" + statusCode
                + ", errorMessage='" + errorMessage + "'}";
    }
}
