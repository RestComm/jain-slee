/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.collab;

import com.microjainslee.api.ActivityHandle;
import com.microjainslee.api.RaBootstrapPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collaborator: correlation-id → SLEE activity, with last-touch
 * timestamps for idle expiry. The Camel twin of ra-sip-servlet's
 * {@code DialogRegistry} — the leak-proofing rules are identical:
 * a natural removal path (explicit end), an idle sweep, and a full clear
 * on RA deactivation.
 */
public final class CamelActivityRegistry {

    /** One live activity. */
    public static final class Entry {
        private final ActivityHandle handle;
        private volatile long lastTouchMillis;

        Entry(ActivityHandle handle) {
            this.handle = handle;
            this.lastTouchMillis = System.currentTimeMillis();
        }

        public ActivityHandle handle() { return handle; }
        public long lastTouchMillis() { return lastTouchMillis; }

        public void touch() {
            this.lastTouchMillis = System.currentTimeMillis();
        }
    }

    private final Map<String, Entry> activities = new ConcurrentHashMap<>();

    /** Get-or-create the activity for a correlation id (touches it). */
    public Entry acquire(String activityId, RaBootstrapPort bootstrap) {
        Entry entry = activities.computeIfAbsent(activityId,
                id -> new Entry(bootstrap.createActivityHandle(id)));
        entry.touch();
        return entry;
    }

    /** Remove and return the entry, or {@code null}. */
    public Entry remove(String activityId) {
        return activities.remove(activityId);
    }

    /** Remove every entry idle longer than {@code idleMillis}. */
    public List<Map.Entry<String, Entry>> expireIdle(long idleMillis) {
        long cutoff = System.currentTimeMillis() - idleMillis;
        List<Map.Entry<String, Entry>> expired = new ArrayList<>();
        for (Map.Entry<String, Entry> e : activities.entrySet()) {
            if (e.getValue().lastTouchMillis() < cutoff) {
                Entry removed = activities.remove(e.getKey());
                if (removed != null) {
                    expired.add(Map.entry(e.getKey(), removed));
                }
            }
        }
        return expired;
    }

    public int size() {
        return activities.size();
    }

    public void clear() {
        activities.clear();
    }
}
