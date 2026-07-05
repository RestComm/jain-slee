/*
 * micro-jainslee 1.1.0 -- example application (example-spring-helloworld-web)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.helloworld.spring.events;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.annotations.EventType;

/**
 * Fired by ra-http-server when an HTTP request arrives.
 * Carries minimal HTTP request metadata for the SBB.
 */
@EventType(name = "HttpWebRequest", vendor = "com.example.helloworld", version = "1.0")
public final class HttpWebRequestEvent implements SleeEvent {

    private final String sessionId;
    private final String method;
    private final String path;
    private final String userAgent;

    public HttpWebRequestEvent(String sessionId, String method, String path, String userAgent) {
        this.sessionId = sessionId;
        this.method = method;
        this.path = path;
        this.userAgent = userAgent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "HttpWebRequestEvent{sessionId='" + sessionId + "', method=" + method
                + ", path='" + path + "'}";
    }
}
