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
import com.microjainslee.api.annotations.EventType;

import java.util.Collections;
import java.util.Map;

/**
 * Generic HTTP web request event fired for every non-/health request
 * received by the HTTP server RA.
 *
 * <p>Carries all request metadata so application SBBs can route based on
 * method, path, headers, and body without any USSD-specific coupling.</p>
 */
@EventType(name = "HttpWebRequest", vendor = "com.microjainslee", version = "1.0")
public final class HttpWebRequestEvent implements SleeEvent {

    private final String sessionId;
    private final String method;
    private final String path;
    private final String body;
    private final String userAgent;
    private final Map<String, String> headers;

    public HttpWebRequestEvent(String sessionId, String method, String path,
                               Map<String, String> headers, String body) {
        this.sessionId = sessionId;
        this.method = method;
        this.path = path;
        this.headers = headers != null
                ? Collections.unmodifiableMap(headers)
                : Collections.emptyMap();
        this.body = body;
        this.userAgent = this.headers.getOrDefault("User-Agent", null);
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

    public String getBody() {
        return body;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "HttpWebRequestEvent{sessionId='" + sessionId + "', method=" + method
                + ", path='" + path + "', headers=" + headers
                + ", body=" + (body != null ? "'" + body + "'" : "null") + '}';
    }
}
