/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * P0 — detects Activity Context leaks by tracking creation timestamps
 * and surfacing ACs that have lived beyond a configurable timeout.
 *
 * <p>Intended for R&amp;D diagnostics: call {@link #onCreated(String)} when
 * an activity context is created, {@link #onEnded(String)} when it ends,
 * and periodically poll {@link #findLeaked()} to detect stale ACs.
 *
 * <p>Thread-safe: all methods use {@link ConcurrentHashMap} for lock-free
 * tracking on the hot path.
 */
public class ActivityContextLeakDetector {

    /** acId → creation timestamp (ms since epoch). */
    private final ConcurrentHashMap<String, Long> creationTimes = new ConcurrentHashMap<>();

    /** Maximum age in milliseconds before an AC is considered leaked. */
    private final long timeoutMs;

    /**
     * @param timeoutMs maximum age in milliseconds; ACs older than this
     *                  are reported by {@link #findLeaked()}
     */
    public ActivityContextLeakDetector(long timeoutMs) {
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("timeoutMs must be > 0");
        }
        this.timeoutMs = timeoutMs;
    }

    /**
     * Record the creation of an activity context.
     * Idempotent: calling twice for the same id overwrites the timestamp.
     */
    public void onCreated(String acId) {
        if (acId == null) {
            return;
        }
        creationTimes.put(acId, System.currentTimeMillis());
    }

    /**
     * Mark an activity context as ended (no longer tracked).
     * Best-effort no-op when the id was never tracked.
     */
    public void onEnded(String acId) {
        if (acId == null) {
            return;
        }
        creationTimes.remove(acId);
    }

    /**
     * Return the ids of all activity contexts that have exceeded
     * {@link #timeoutMs} without being ended. The returned list is a
     * snapshot copy — callers may mutate it freely.
     *
     * @return list of leaked AC ids (may be empty)
     */
    public List<String> findLeaked() {
        long now = System.currentTimeMillis();
        List<String> leaked = new ArrayList<>();
        creationTimes.forEach((id, created) -> {
            if (now - created > timeoutMs) {
                leaked.add(id);
            }
        });
        return leaked;
    }

    /**
     * Return the number of currently tracked (not yet ended) activity contexts.
     */
    public int activeCount() {
        return creationTimes.size();
    }

    /**
     * Clear all tracking state. Useful for reset during container restart.
     */
    public void clear() {
        creationTimes.clear();
    }

    /**
     * Return the configured timeout in milliseconds.
     */
    public long getTimeoutMs() {
        return timeoutMs;
    }
}
