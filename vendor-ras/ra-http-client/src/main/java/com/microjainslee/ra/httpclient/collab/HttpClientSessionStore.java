/*
 * micro-jainslee 1.2.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.ra.httpclient.collab;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks pending HTTP callback sessions so the RA can correlate
 * asynchronous HTTP responses back to the originating session.
 *
 * <p>A session is {@link #track(String, String) tracked} when a callback
 * is dispatched and {@link #complete(String, int, String) completed} when
 * the HTTP response arrives.  {@link #get(String)} returns a point-in-time
 * snapshot suitable for diagnostics or event construction.
 */
public interface HttpClientSessionStore {

    /** Record that a callback is in-flight for the given session. */
    void track(String sessionId, String callbackUrl);

    /** Record the final outcome of a callback. */
    void complete(String sessionId, int statusCode, String responseBody);

    /** Return a point-in-time snapshot, or {@code null} if unknown. */
    SessionSnapshot get(String sessionId);

    /** Immutable snapshot of a callback session state. */
    interface SessionSnapshot {
        String getCallbackUrl();
        int getStatusCode();
        String getResponseBody();
    }

    // ---- default implementation ----

    /** Thread-safe in-memory store backed by {@link ConcurrentHashMap}. */
    final class InMemoryHttpClientSessionStore implements HttpClientSessionStore {

        private final ConcurrentHashMap<String, Record> store = new ConcurrentHashMap<>();

        @Override
        public void track(String sessionId, String callbackUrl) {
            store.put(sessionId, new Record(callbackUrl, 0, null));
        }

        @Override
        public void complete(String sessionId, int statusCode, String responseBody) {
            store.computeIfPresent(sessionId, (k, r) ->
                    new Record(r.callbackUrl, statusCode, responseBody));
        }

        @Override
        public SessionSnapshot get(String sessionId) {
            Record r = store.get(sessionId);
            return r != null ? new Snapshot(r) : null;
        }

        private record Record(String callbackUrl, int statusCode, String responseBody) {}

        private record Snapshot(String callbackUrl, int statusCode, String responseBody)
                implements SessionSnapshot {
            Snapshot(Record r) { this(r.callbackUrl, r.statusCode, r.responseBody); }
            @Override public String getCallbackUrl() { return callbackUrl; }
            @Override public int getStatusCode() { return statusCode; }
            @Override public String getResponseBody() { return responseBody; }
        }
    }
}
