/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.sipservlet.collab;

import com.microjainslee.api.ActivityHandle;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-Call-ID dialog state used by the outbound path and the idle sweeper.
 *
 * <p>This is NOT a full RFC 3261 dialog/transaction state machine — it is
 * the minimum state a B2B/gateway SBB needs to answer and terminate
 * dialogs: the last inbound request (to derive responses from), the peer
 * address and transport (where to send), and a last-activity timestamp
 * (to expire abandoned dialogs so the registry cannot leak).
 */
public final class DialogRegistry {

    /** Mutable per-dialog state; fields are volatile — single-writer per field. */
    public static final class Dialog {
        public final String callId;
        public final ActivityHandle handle;
        private volatile SIPRequest lastRequest;
        private volatile SIPResponse lastResponse;
        private volatile InetSocketAddress peer;
        private volatile String transport;
        private volatile long lastActivityMillis;
        private volatile long cseq;

        Dialog(String callId, ActivityHandle handle) {
            this.callId = callId;
            this.handle = handle;
            this.lastActivityMillis = System.currentTimeMillis();
        }

        public SIPRequest lastRequest() { return lastRequest; }
        public SIPResponse lastResponse() { return lastResponse; }
        public InetSocketAddress peer() { return peer; }
        public String transport() { return transport; }
        public long lastActivityMillis() { return lastActivityMillis; }

        /** Next CSeq number for locally generated in-dialog requests. */
        public long nextCseq() { return ++cseq; }

        void touch(InetSocketAddress peer, String transport) {
            this.peer = peer;
            this.transport = transport;
            this.lastActivityMillis = System.currentTimeMillis();
        }
    }

    private final Map<String, Dialog> dialogs = new ConcurrentHashMap<>();

    /** Create-or-update the dialog for an inbound message. */
    public Dialog recordInbound(String callId, ActivityHandle handle, Object sipMessage,
                                InetSocketAddress peer, String transport) {
        Dialog dialog = dialogs.computeIfAbsent(callId, id -> new Dialog(id, handle));
        if (sipMessage instanceof SIPRequest req) {
            dialog.lastRequest = req;
            long cseq = req.getCSeq() != null ? req.getCSeq().getSeqNumber() : 0L;
            if (cseq > dialog.cseq) {
                dialog.cseq = cseq;
            }
        } else if (sipMessage instanceof SIPResponse resp) {
            dialog.lastResponse = resp;
        }
        dialog.touch(peer, transport);
        return dialog;
    }

    public Dialog find(String callId) {
        return dialogs.get(callId);
    }

    public Dialog remove(String callId) {
        return dialogs.remove(callId);
    }

    public int size() {
        return dialogs.size();
    }

    public void clear() {
        dialogs.clear();
    }

    /** Remove and return every dialog idle for longer than {@code idleMillis}. */
    public List<Dialog> expireIdle(long idleMillis) {
        long cutoff = System.currentTimeMillis() - idleMillis;
        List<Dialog> expired = new ArrayList<>();
        for (Map.Entry<String, Dialog> entry : dialogs.entrySet()) {
            if (entry.getValue().lastActivityMillis < cutoff) {
                Dialog removed = dialogs.remove(entry.getKey());
                if (removed != null) {
                    expired.add(removed);
                }
            }
        }
        return expired;
    }
}
