/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.grpcserver.collab;

import io.grpc.ServerCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collaborator: callId → open server call awaiting the SBB's answer.
 * Same leak-proofing template as every vendor RA registry: natural
 * removal (complete/cancel), deadline sweep, full drain on shutdown.
 */
public final class PendingCallRegistry {

    /** One in-flight unary call. */
    public static final class PendingCall {
        private final ServerCall<byte[], byte[]> call;
        private final String activityId;
        private final long deadlineMillis;
        private volatile boolean completed;

        public PendingCall(ServerCall<byte[], byte[]> call, String activityId,
                           long deadlineMillis) {
            this.call = call;
            this.activityId = activityId;
            this.deadlineMillis = deadlineMillis;
        }

        public ServerCall<byte[], byte[]> call() { return call; }
        public String activityId() { return activityId; }
        public long deadlineMillis() { return deadlineMillis; }
        public boolean isCompleted() { return completed; }
        public void markCompleted() { this.completed = true; }
    }

    private final Map<String, PendingCall> calls = new ConcurrentHashMap<>();

    public void register(String callId, PendingCall call) {
        calls.put(callId, call);
    }

    public PendingCall remove(String callId) {
        return calls.remove(callId);
    }

    public int size() {
        return calls.size();
    }

    /** Remove and return every call whose deadline passed. */
    public List<Map.Entry<String, PendingCall>> expireOverdue(long nowMillis) {
        List<Map.Entry<String, PendingCall>> overdue = new ArrayList<>();
        for (Map.Entry<String, PendingCall> e : calls.entrySet()) {
            if (e.getValue().deadlineMillis() < nowMillis) {
                PendingCall removed = calls.remove(e.getKey());
                if (removed != null) {
                    overdue.add(Map.entry(e.getKey(), removed));
                }
            }
        }
        return overdue;
    }

    /** Remove and return everything (RA shutdown). */
    public List<PendingCall> drainAll() {
        List<PendingCall> all = new ArrayList<>(calls.values());
        calls.clear();
        return all;
    }
}
