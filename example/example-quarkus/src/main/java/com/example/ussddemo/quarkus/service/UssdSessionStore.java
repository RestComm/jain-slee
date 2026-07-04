/*
 * micro-jainslee 1.1.0 -- example application (example-quarkus)
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.example.ussddemo.quarkus.service;

import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.core.removal.EntityRemovalEvent;
import com.microjainslee.core.removal.EntityRemovalEvent.RemovalReason;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/** Same store as embedded, just a CDI @ApplicationScoped bean here. */
@ApplicationScoped
public final class UssdSessionStore {

    private static final Logger LOG = LogManager.getLogger(UssdSessionStore.class);

    /** Conventional suffix the embedded SBB uses to derive entity ids. */
    private static final String ENTITY_ID_SUFFIX = "/http";

    public enum Status {
        PROCESSING,
        COMPLETED,
        FAILED
    }

    public static final class SessionRecord {
        private volatile Status status = Status.PROCESSING;
        private volatile String responseText;
        private volatile String errorMessage;
        private volatile String callbackUrl;

        public Status getStatus() { return status; }
        public String getResponseText() { return responseText; }
        public String getErrorMessage() { return errorMessage; }
        public String getCallbackUrl() { return callbackUrl; }

        SessionRecord withStatus(Status newStatus) {
            this.status = newStatus;
            return this;
        }

        SessionRecord withMessage(String msg) {
            this.errorMessage = msg;
            return this;
        }
    }

    private final Map<String, SessionRecord> sessions =
            new ConcurrentHashMap<String, SessionRecord>();

    /** Sprint S6 — the listener we registered on the container's removal bus. */
    private volatile Consumer<EntityRemovalEvent> removalListener;
    /** Sprint S6 — the container we are bound to, so {@link #destroy()} can unsubscribe. */
    private volatile MicroSleeContainer boundContainer;

    public SessionRecord open(String sessionId) {
        SessionRecord record = new SessionRecord();
        sessions.put(sessionId, record);
        return record;
    }

    public SessionRecord get(String sessionId) { return sessions.get(sessionId); }

    public void attachCallback(String sessionId, String callbackUrl) {
        SessionRecord record = sessions.get(sessionId);
        if (record != null) {
            record.callbackUrl = callbackUrl;
        }
    }

    public void complete(String sessionId, String responseText) {
        SessionRecord record = sessions.get(sessionId);
        if (record != null) {
            record.responseText = responseText;
            record.status = Status.COMPLETED;
        }
    }

    public void fail(String sessionId, String message) {
        SessionRecord record = sessions.get(sessionId);
        if (record != null) {
            record.errorMessage = message;
            record.status = Status.FAILED;
        }
    }

    // ───────────────────────────────────────────────────────────────
    // Sprint S6 — Observability & Removal Notification
    // ───────────────────────────────────────────────────────────────

    /**
     * Register a removal listener with {@code container} so any SBB entity
     * that gets removed mid-session auto-marks the corresponding session
     * as {@link Status#FAILED}. Idempotent: calling twice replaces the
     * previous listener instead of duplicating it.
     *
     * <p><b>R6 mitigation</b>: a session already in
     * {@link Status#COMPLETED} is NEVER overridden to {@code FAILED} —
     * the listener guards on the existing status before mutating it, so a
     * late removal event (delivered after {@link #complete(String, String)}
     * ran) is silently absorbed.
     */
    public void bindToContainer(MicroSleeContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("container is required");
        }
        // Idempotent — unsubscribe previous listener if any.
        destroy();
        this.removalListener = event -> {
            String sessionId = stripSuffix(event.entityId(), ENTITY_ID_SUFFIX);
            if (sessionId != null && sessions.containsKey(sessionId)) {
                sessions.computeIfPresent(sessionId, (k, existing) -> {
                    if (existing.getStatus() == Status.PROCESSING) {
                        return existing.withStatus(Status.FAILED)
                                       .withMessage("Session removed: " + event.reason());
                    }
                    // COMPLETED stays COMPLETED (Risk R6); FAILED stays FAILED.
                    return existing;
                });
                LOG.infof("UssdSessionStore: session %s auto-failed due to %s",
                        sessionId, event.reason());
            }
        };
        container.addEntityRemovalListener(this.removalListener);
        this.boundContainer = container;
    }

    /**
     * Tear down — unsubscribe the removal listener so a subsequent
     * {@link #bindToContainer(MicroSleeContainer)} starts clean.
     */
    public void destroy() {
        if (boundContainer != null && removalListener != null) {
            boundContainer.removeEntityRemovalListener(removalListener);
        }
        removalListener = null;
        boundContainer = null;
    }

    private static String stripSuffix(String entityId, String suffix) {
        return entityId != null && suffix != null && entityId.endsWith(suffix)
                ? entityId.substring(0, entityId.length() - suffix.length())
                : null;
    }
}
