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
import java.util.List;
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
    private final byte[] bodyBytes;
    private final String userAgent;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final Map<String, String> formAttributes;
    private final Map<String, String> cookies;
    private final List<HttpUpload> uploads;

    public HttpWebRequestEvent(String sessionId, String method, String path,
                               Map<String, String> headers, String body) {
        this(sessionId, method, path, headers, body, null, null, null, null, null);
    }

    public HttpWebRequestEvent(String sessionId, String method, String path,
                               Map<String, String> headers, String body, byte[] bodyBytes) {
        this(sessionId, method, path, headers, body, bodyBytes, null, null, null, null);
    }

    public HttpWebRequestEvent(String sessionId, String method, String path,
                               Map<String, String> headers, String body, byte[] bodyBytes,
                               Map<String, String> queryParams) {
        this(sessionId, method, path, headers, body, bodyBytes, queryParams, null, null, null);
    }

    /**
     * Full constructor. Beyond method/path/headers/body it carries everything a
     * complete web app needs, all parsed by the RA so SBBs stay pure:
     * <ul>
     *   <li>{@code bodyBytes} — raw request bytes for binary payloads;</li>
     *   <li>{@code queryParams} — parsed {@code ?a=b} query;</li>
     *   <li>{@code formAttributes} — parsed {@code application/x-www-form-urlencoded} fields;</li>
     *   <li>{@code cookies} — parsed request cookies (name → value);</li>
     *   <li>{@code uploads} — parsed {@code multipart/form-data} file parts (bytes in memory).</li>
     * </ul>
     */
    public HttpWebRequestEvent(String sessionId, String method, String path,
                               Map<String, String> headers, String body, byte[] bodyBytes,
                               Map<String, String> queryParams, Map<String, String> formAttributes,
                               Map<String, String> cookies, List<HttpUpload> uploads) {
        this.sessionId = sessionId;
        this.method = method;
        this.path = path;
        this.headers = headers != null
                ? Collections.unmodifiableMap(headers)
                : Collections.emptyMap();
        this.body = body;
        this.bodyBytes = bodyBytes;
        this.queryParams = queryParams != null
                ? Collections.unmodifiableMap(queryParams)
                : Collections.emptyMap();
        this.formAttributes = formAttributes != null
                ? Collections.unmodifiableMap(formAttributes)
                : Collections.emptyMap();
        this.cookies = cookies != null
                ? Collections.unmodifiableMap(cookies)
                : Collections.emptyMap();
        this.uploads = uploads != null
                ? Collections.unmodifiableList(uploads)
                : Collections.emptyList();
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

    /** Raw request bytes (non-null only for binary/multipart uploads). */
    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /** Parsed query parameters (empty when the request had no query string). */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /** Convenience: a single query parameter, or {@code null} if absent. */
    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    /** Parsed {@code x-www-form-urlencoded} form fields (empty if none). */
    public Map<String, String> getFormAttributes() {
        return formAttributes;
    }

    /** Convenience: a single form field, or {@code null} if absent. */
    public String getFormAttribute(String name) {
        return formAttributes.get(name);
    }

    /** Parsed request cookies, name → value (empty if none). */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /** Convenience: a single cookie value, or {@code null} if absent. */
    public String getCookie(String name) {
        return cookies.get(name);
    }

    /** Parsed {@code multipart/form-data} file parts (empty if none). */
    public List<HttpUpload> getUploads() {
        return uploads;
    }

    @Override
    public String toString() {
        return "HttpWebRequestEvent{sessionId='" + sessionId + "', method=" + method
                + ", path='" + path + "', headers=" + headers
                + ", body=" + (body != null ? "'" + body + "'" : "null") + '}';
    }
}
