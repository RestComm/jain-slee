/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 */

package com.example.ussddemo.quarkus.bootstrap;

import com.microjainslee.ra.httpserver.HttpServerSessionStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** CDI session store implementing vendor-ras {@link HttpServerSessionStore}. */
@ApplicationScoped
public final class UssdSessionStore implements HttpServerSessionStore {

    private final Map<String, Snapshot> sessions = new ConcurrentHashMap<>();

    public void open(String sessionId) {
        sessions.put(sessionId, new Snapshot("PROCESSING", null, null));
    }

    public void attachCallback(String sessionId, String callbackUrl) {
        Snapshot s = sessions.get(sessionId);
        if (s != null) s.callbackUrl = callbackUrl;
    }

    public void complete(String sessionId, String responseText) {
        Snapshot s = sessions.get(sessionId);
        if (s != null) {
            s.responseText = responseText;
            s.status = "COMPLETED";
        }
    }

    public void fail(String sessionId, String errorMessage) {
        Snapshot s = sessions.get(sessionId);
        if (s != null) {
            s.errorMessage = errorMessage;
            s.status = "FAILED";
        }
    }

    @Override
    public SessionSnapshot get(String sessionId) {
        return sessions.get(sessionId);
    }

    public String callbackUrl(String sessionId) {
        Snapshot s = sessions.get(sessionId);
        return s != null ? s.callbackUrl : null;
    }

    private static final class Snapshot implements SessionSnapshot {
        volatile String status;
        volatile String responseText;
        volatile String errorMessage;
        volatile String callbackUrl;

        Snapshot(String status, String responseText, String errorMessage) {
            this.status = status;
            this.responseText = responseText;
            this.errorMessage = errorMessage;
        }

        @Override public String getStatus() { return status; }
        @Override public String getResponseText() { return responseText; }
        @Override public String getErrorMessage() { return errorMessage; }
    }
}
