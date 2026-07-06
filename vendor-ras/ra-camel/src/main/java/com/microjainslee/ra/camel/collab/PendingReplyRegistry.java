/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.camel.collab;

import com.microjainslee.ra.camel.command.ReplyToExchange;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collaborator: exchangeId → waiting in-out consumer exchange. The Camel
 * consumer thread parks on the future (bounded by the RA reply timeout);
 * an SBB completes it with {@code ReplyToExchange}. Everything is failed
 * fast on RA deactivation so no consumer thread is left hanging.
 */
public final class PendingReplyRegistry {

    private final Map<String, CompletableFuture<ReplyToExchange>> pending =
            new ConcurrentHashMap<>();

    public CompletableFuture<ReplyToExchange> register(String exchangeId) {
        CompletableFuture<ReplyToExchange> future = new CompletableFuture<>();
        pending.put(exchangeId, future);
        return future;
    }

    /** Complete a waiting exchange; {@code false} when unknown/expired. */
    public boolean complete(String exchangeId, ReplyToExchange reply) {
        CompletableFuture<ReplyToExchange> future = pending.remove(exchangeId);
        if (future == null) {
            return false;
        }
        future.complete(reply);
        return true;
    }

    public void discard(String exchangeId) {
        pending.remove(exchangeId);
    }

    /** Fail every waiter (RA shutdown). */
    public void cancelAll() {
        pending.values().forEach(f -> f.cancel(true));
        pending.clear();
    }

    public int size() {
        return pending.size();
    }
}
